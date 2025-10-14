package nl.petertillema.tibasic.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TIBasicConfigurationFactory extends ConfigurationFactory {

    protected TIBasicConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    public @NotNull @NonNls String getId() {
        return TIBasicConfigurationType.ID;
    }

    @Override
    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new TIBasicRunConfiguration(project, this, "TI-Basic");
    }

    @Override
    public @Nullable Class<? extends BaseState> getOptionsClass() {
        return TIBasicRunConfigurationOptions.class;
    }
}
