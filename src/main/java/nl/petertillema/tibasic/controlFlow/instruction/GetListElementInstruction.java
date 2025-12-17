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
 * Instruction which fetches an element with a given index from a list. The length of the list must be known before
 * executing this instruction, otherwise the result will be an unknown value. If multiple integer indexes are candidate,
 * the result will be joined from all found values.
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

        Set<Integer> candidates = extractIntegerIndexCandidates(state.getDfType(indexVal), null, 16);

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
