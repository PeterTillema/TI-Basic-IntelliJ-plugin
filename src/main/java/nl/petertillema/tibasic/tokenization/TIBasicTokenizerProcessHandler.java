package nl.petertillema.tibasic.tokenization;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;

/**
 * Minimal controllable ProcessHandler used to model a short-lived background task.
 */
public class TIBasicTokenizerProcessHandler extends ProcessHandler {

    @Override
    protected void destroyProcessImpl() {
        // Consider normal termination when the user stops the process.
        notifyProcessTerminated(0);
    }

    @Override
    protected void detachProcessImpl() {
        notifyProcessDetached();
    }

    @Override
    public boolean detachIsDefault() {
        return false;
    }

    @Override
    public @Nullable OutputStream getProcessInput() {
        return null;
    }

    public void print(String text, Key<?> outputType) {
        notifyTextAvailable(text, outputType);
    }

    public void println(String line) {
        notifyTextAvailable(line + "\n", ProcessOutputTypes.STDOUT);
    }

    public void finish(int exitCode) {
        notifyProcessTerminated(exitCode);
    }
}
