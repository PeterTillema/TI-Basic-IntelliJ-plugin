package nl.petertillema.tibasic.run;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import nl.petertillema.tibasic.language.TIBasicIcons;

public final class TIBasicConfigurationType extends ConfigurationTypeBase {

    static final String ID = "TIBasicRunConfiguration";

    private TIBasicConfigurationType() {
        super(ID, "TI-Basic", "TI-Basic run configuration tye", TIBasicIcons.FILE);
        addFactory(new TIBasicConfigurationFactory(this));
    }

}
