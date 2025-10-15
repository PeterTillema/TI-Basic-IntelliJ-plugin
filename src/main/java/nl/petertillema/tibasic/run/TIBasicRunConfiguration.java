package nl.petertillema.tibasic.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import nl.petertillema.tibasic.tokenization.TIBasicFileTokenizer;
import nl.petertillema.tibasic.tokenization.TIBasicTokenizerProcessHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.PsiManager.getInstance;

public final class TIBasicRunConfiguration extends RunConfigurationBase<TIBasicRunConfigurationOptions> {

    TIBasicRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, @Nullable String name) {
        super(project, factory, name);
    }

    @Override
    protected @NotNull TIBasicRunConfigurationOptions getOptions() {
        return (TIBasicRunConfigurationOptions) super.getOptions();
    }

    @Override
    public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new TIBasicSettingsEditor();
    }

    @Override
    public @NotNull RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return new CommandLineState(environment) {
            @Override
            protected @NotNull ProcessHandler startProcess() {
                var project = getProject();
                var options = TIBasicRunConfiguration.this.getOptions();

                // We'll manage the process lifecycle manually
                var handler = new TIBasicTokenizerProcessHandler();

                var path = options.getInputPathField();
                if (path == null || path.isBlank()) {
                    handler.println("No input file configured.");
                    handler.finish(1);
                    return handler;
                }

                var vfs = LocalFileSystem.getInstance().findFileByPath(path);
                if (vfs == null) {
                    handler.println("Cannot find file: " + path);
                    handler.finish(1);
                    return handler;
                }

                var psi = getInstance(project).findFile(vfs);
                if (psi == null) {
                    handler.println("Selected file is not a PSI file: " + path);
                    handler.finish(1);
                    return handler;
                }

                var outPath = options.getOutputPathField();
                if (outPath == null || outPath.isBlank()) {
                    handler.println("No output file configured.");
                    handler.finish(1);
                    return handler;
                }

                handler.println("Tokenizing " + vfs.getName() + "...");
                TIBasicFileTokenizer.tokenize(project, psi, handler::println, () -> handler.finish(0), options);

                return handler;
            }
        };
    }
}
