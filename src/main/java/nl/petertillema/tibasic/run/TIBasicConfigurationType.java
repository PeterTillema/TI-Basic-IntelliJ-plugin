package nl.petertillema.tibasic.run;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import nl.petertillema.tibasic.language.TIBasicIcons;

public class TIBasicConfigurationType extends ConfigurationTypeBase {

    static final String ID = "TIBasicRunConfiguration";

    protected TIBasicConfigurationType() {
        super(ID, "TI-Basic", "TI-Basic run configuration tye", TIBasicIcons.FILE);
        addFactory(new TIBasicConfigurationFactory(this));
    }

}
