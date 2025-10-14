package nl.petertillema.tibasic.run;

import com.intellij.execution.configurations.LocatableRunConfigurationOptions;
import com.intellij.openapi.components.StoredProperty;

public class TIBasicRunConfigurationOptions extends LocatableRunConfigurationOptions {

    private final StoredProperty<String> inputPathField = string("").provideDelegate(this, "inputPathField");
    private final StoredProperty<String> outputPathField = string("").provideDelegate(this, "outputPathField");

    public String getInputPathField() {
        return this.inputPathField.getValue(this);
    }

    public void setInputPathField(String value) {
        this.inputPathField.setValue(this, value);
    }

    public String getOutputPathField() {
        return this.outputPathField.getValue(this);
    }

    public void setOutputPathField(String value) {
        this.outputPathField.setValue(this, value);
    }

}
