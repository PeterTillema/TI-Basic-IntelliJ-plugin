package nl.petertillema.tibasic.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfigurationBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class TIBasicRunConfiguration extends LocatableConfigurationBase<TIBasicRunConfigurationOptions> {

    protected TIBasicRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, @Nullable String name) {
        super(project, factory, name);
    }

    @Override
    protected @NotNull TIBasicRunConfigurationOptions getOptions() {
        return (TIBasicRunConfigurationOptions) super.getOptions();
    }

    public String getInputPathField() {
        return this.getOptions().getInputPathField();
    }

    public void setInputPathField(String value) {
        this.getOptions().setInputPathField(value);
    }

    public String getOutputPathField() {
        return this.getOptions().getOutputPathField();
    }

    public void setOutputPathField(String value) {
        this.getOptions().setOutputPathField(value);
    }

    @Override
    public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new TIBasicSettingsEditor();
    }

    @Override
    public boolean isGeneratedName() {
        return false;
    }

    @Override
    public @Nullable @NlsActions.ActionText String suggestedName() {
        var path = this.getInputPathField();
        return new File(path).getName();
    }

    @Override
    public @Nullable RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        // todo
        return new CommandLineState(environment) {
            @Override
            protected @NotNull ProcessHandler startProcess() {
                return null;
            }
        };
    }
}
