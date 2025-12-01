package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.Instruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import nl.petertillema.tibasic.analysis.commands.TIBasicCommand;
import org.jetbrains.annotations.NotNull;

import static nl.petertillema.tibasic.analysis.TIBasicCommandFunctionMap.COMMAND_MAP;

public class CommandInstruction extends Instruction {

    private final String commandName;
    private final int operands;

    public CommandInstruction(String commandName, int operands) {
        this.commandName = commandName;
        this.operands = operands;
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        TIBasicCommand commandImpl = COMMAND_MAP.get(commandName);
        if (commandImpl != null) {
            int operands = this.operands;
            DfaValue[] args;
            if (operands == 0) {
                args = DfaValue.EMPTY_ARRAY;
            } else {
                args = new DfaValue[operands];
                for (int i = operands - 1; i >= 0; i--) {
                    args[i] = stateBefore.pop();
                }
            }
            commandImpl.evalCommand(args);
        }
        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "COMMAND " + commandName;
    }
}
