package nl.petertillema.tibasic.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public final class TIBasicConfigurationFactory extends ConfigurationFactory {

    TIBasicConfigurationFactory(ConfigurationType type) {
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
    public @NotNull Class<? extends BaseState> getOptionsClass() {
        return TIBasicRunConfigurationOptions.class;
    }
}
