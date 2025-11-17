package nl.petertillema.tibasic.controlFlow;

import com.intellij.codeInspection.dataFlow.interpreter.ReachabilityCountingInterpreter;
import com.intellij.codeInspection.dataFlow.interpreter.RunnerResult;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.DfaListener;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryStateImpl;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaTypeValue;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

public class TIBasicCommonDataflow {

    private static class DataflowPoint {
        @NotNull DfType myDfType = DfType.BOTTOM;
        // empty = top; null = bottom
        @Nullable Set<Object> myPossibleValues = Collections.emptySet();
        boolean myMayFailByContract = false;

        DataflowPoint() {}

        DataflowPoint(DataflowPoint other) {
            myDfType = other.myDfType;
            myPossibleValues = other.myPossibleValues;
            myMayFailByContract = other.myMayFailByContract;
        }

        void addValue(DfaMemoryState memState, DfaValue value) {
            if (myPossibleValues == null) return;
            DfType dfType = memState.getDfType(value);
            Object newValue = dfType.getConstantOfType(Object.class);
            if (newValue == null) {
                myPossibleValues = null;
                return;
            }
            if (myPossibleValues.contains(newValue)) return;
            if (myPossibleValues.isEmpty()) {
                myPossibleValues = Collections.singleton(newValue);
            }
            else {
                myPossibleValues = new HashSet<>(myPossibleValues);
                myPossibleValues.add(newValue);
            }
        }

        void addFacts(DfaMemoryState memState, DfaValue value) {
            if (myDfType == DfType.TOP) return;
            DfType newType = memState.getDfTypeIncludingDerived(value);
            myDfType = myDfType.join(newType);
        }
    }

    public static class DataflowResult {

        private final Map<TIBasicDfaAnchor, DataflowPoint> data = new HashMap<>();
        private final @NotNull List<PsiElement> myUnreachable = new ArrayList<>();
        private final @NotNull RunnerResult result;

        public DataflowResult(@NotNull RunnerResult result) {
            this.result = result;
        }

        @NotNull
        private TIBasicCommonDataflow.DataflowResult copy() {
            DataflowResult copy = new DataflowResult(result);
            data.forEach((anchor, point) -> copy.data.put(anchor, new DataflowPoint(point)));
            copy.myUnreachable.addAll(myUnreachable);
            return copy;
        }

        private void add(TIBasicDfaAnchor anchor, DfaMemoryState memState, DfaValue value) {
            updateDataPoint(data, anchor, memState, value);
        }

        private void updateDataPoint(Map<TIBasicDfaAnchor, DataflowPoint> data,
                                     TIBasicDfaAnchor anchor,
                                     DfaMemoryState memState,
                                     DfaValue value) {
            DataflowPoint point = data.computeIfAbsent(anchor, e -> new DataflowPoint());
            if (DfaTypeValue.isContractFail(value)) {
                point.myMayFailByContract = true;
                return;
            }
            point.addFacts(memState, value);
            point.addValue(memState, value);
        }

        public @NotNull Collection<PsiElement> getUnreachableRanges() {
            return result != RunnerResult.OK ? Collections.emptyList() : myUnreachable;
        }

        public @NotNull DfType getDfType(@NotNull TIBasicDfaAnchor anchor) {
            DataflowPoint point = data.get(anchor);
            return point == null ? DfType.TOP : point.myDfType;
        }
    }

    private static class TIBasicDataflowListener implements DfaListener {
        private final DataflowResult result = new DataflowResult(RunnerResult.OK);

        @Override
        public void beforePush(@NotNull DfaValue @NotNull [] args, @NotNull DfaValue value, @NotNull DfaAnchor anchor, @NotNull DfaMemoryState state) {
            if (anchor instanceof TIBasicDfaAnchor tiBasicDfaAnchor) {
                result.add(tiBasicDfaAnchor, state, value);
            }
        }
    }

    public static DataflowResult getDataflowResult(PsiElement element) {
        var fileMap = CachedValuesManager.getCachedValue(element, () ->
                CachedValueProvider.Result.create(new ConcurrentHashMap<PsiElement, DataflowResult>(), PsiModificationTracker.MODIFICATION_COUNT));

        class ManagedCompute implements ForkJoinPool.ManagedBlocker {
            private DataflowResult result;

            @Override
            public boolean block() {
                result = fileMap.computeIfAbsent(element, TIBasicCommonDataflow::runDFA);
                return true;
            }

            @Override
            public boolean isReleasable() {
                result = fileMap.get(element);
                return result != null;
            }

            DataflowResult getResult() {
                return result;
            }
        }

        var managedCompute = new ManagedCompute();
        try {
            ForkJoinPool.managedBlock(managedCompute);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return managedCompute.getResult();
    }

    private static DataflowResult runDFA(PsiElement element) {
        var listener = new TIBasicDataflowListener();
        var factory = new DfaValueFactory(element.getProject());
        var flow = new TIBasicControlFlowAnalyzer(factory, element).buildControlFlow();
        var interpreter = new ReachabilityCountingInterpreter(flow, listener, false, false, 0);
        var stateImpl = new DfaMemoryStateImpl(factory);

        var result = interpreter.interpret(stateImpl);
        if (result != RunnerResult.OK) return new DataflowResult(result);
        listener.result.myUnreachable.addAll(interpreter.getUnreachable());
        return listener.result.copy();
    }
}
