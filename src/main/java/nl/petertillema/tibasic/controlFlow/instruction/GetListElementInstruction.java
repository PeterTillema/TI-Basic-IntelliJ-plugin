package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.EvalInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.descriptor.ListElementDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static nl.petertillema.tibasic.controlFlow.descriptor.ListElementDescriptor.extractIntegerIndexCandidates;

/**
 * IR instruction that conceptually reads an element from a TI-BASIC list using a dynamic index.
 *
 * Stack contract (arity = 2):
 *   - consumes: [listVar, index]
 *   - pushes: element value (conservative for now)
 *
 * Notes:
 *   Performs bounds-aware extraction using the list length and index type. For singleton
 *   index candidates, a concrete element variable is pushed; for a small finite set, the
 *   joined type of candidate elements is pushed; otherwise, TOP is pushed.
 */
public class GetListElementInstruction extends EvalInstruction {

    public GetListElementInstruction(@Nullable DfaAnchor anchor) {
        super(anchor, 2);
    }

    @Override
    public @NotNull DfaValue eval(@NotNull DfaValueFactory factory,
                                  @NotNull DfaMemoryState state,
                                  @NotNull DfaValue @NotNull ... arguments) {
        DfaValue listVal = arguments[0];
        DfaValue indexVal = arguments[1];

        if (!(listVal instanceof DfaVariableValue variableVar)) {
            return factory.getUnknown();
        }

        // Determine length upper bound if available
        Integer maxLen = null;
//        DfaVariableValue lenVar = factory.getVarFactory().createVariableValue(new ListLengthDescriptor(), listVar);
//        DfType lenType = state.getDfType(lenVar);
//        if (lenType instanceof DfBigDecimalType lenNum) {
//            try {
//                BigDecimal max = lenNum.range().max();
//                // clip to a reasonable int bound
//                if (max.compareTo(BigDecimal.ZERO) >= 0 && max.compareTo(new BigDecimal(Integer.MAX_VALUE)) <= 0) {
//                    maxLen = max.intValue();
//                }
//            } catch (Exception ignored) {
//                // unknown/empty length
//            }
//        }

        Set<Integer> candidates = extractIntegerIndexCandidates(state.getDfType(indexVal), maxLen, 16);

        if (candidates.isEmpty()) {
            return factory.getUnknown();
        }
        if (candidates.size() == 1) {
            int idx = candidates.iterator().next();
            return new ListElementDescriptor(idx).createValue(factory, listVal);
        }

        // Join candidate element types for a small finite set
        DfType joined = DfType.BOTTOM;
        for (int idx : candidates) {
            DfaValue elementVar = new ListElementDescriptor(idx).createValue(factory, variableVar);
            DfType t = state.getDfType(elementVar);
            joined = joined.join(t);
        }
        return factory.fromDfType(joined);
    }

    @Override
    public String toString() {
        return "GET_LIST_ELEMENT";
    }

}
