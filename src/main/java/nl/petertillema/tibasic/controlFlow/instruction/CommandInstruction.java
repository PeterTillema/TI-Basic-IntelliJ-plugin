package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.Instruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import nl.petertillema.tibasic.controlFlow.commandFunction.commands.TIBasicCommand;
import org.jetbrains.annotations.NotNull;

import static nl.petertillema.tibasic.controlFlow.commandFunction.TIBasicCommandFunctionMap.COMMAND_MAP;

/**
 * Executes a built-in command with the given arguments. Most commands don't do anything, some commands edit built-in
 * variables. If an unknown command is passed, nothing is done. No result is pushed at all, since this is supposed to
 * be a terminal operation.
 */
public class CommandInstruction extends Instruction {

    private final String commandName;
    private final int operands;

    public CommandInstruction(String commandName, int operands) {
        this.commandName = commandName;
        this.operands = operands;
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
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
        TIBasicCommand commandImpl = COMMAND_MAP.get(commandName);
        if (commandImpl != null) commandImpl.evalCommand(stateBefore, args);

        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "COMMAND " + commandName;
    }
}
