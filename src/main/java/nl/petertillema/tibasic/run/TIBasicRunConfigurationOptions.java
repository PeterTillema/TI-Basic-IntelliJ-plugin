package nl.petertillema.tibasic.run;

import com.intellij.execution.configurations.RunConfigurationOptions;
import com.intellij.openapi.components.StoredProperty;

public final class TIBasicRunConfigurationOptions extends RunConfigurationOptions {

    private final StoredProperty<String> inputPathField = string("").provideDelegate(this, "inputPathField");
    private final StoredProperty<String> outputPathField = string("").provideDelegate(this, "outputPathField");
    private final StoredProperty<String> programNameField = string("").provideDelegate(this, "programNameField");
    private final StoredProperty<String> programTypeField = string("").provideDelegate(this, "programTypeField");
    private final StoredProperty<Boolean> archivedField = property(false).provideDelegate(this, "archivedField");

    private TIBasicRunConfigurationOptions() {}

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

    public String getProgramNameField() {
        return this.programNameField.getValue(this);
    }

    public void setProgramNameField(String value) {
        this.programNameField.setValue(this, value);
    }

    public String getProgramTypeField() {
        return this.programTypeField.getValue(this);
    }

    public void setProgramTypeField(String value) {
        this.programTypeField.setValue(this, value);
    }

    public boolean getArchivedField() {
        return this.archivedField.getValue(this);
    }

    public void setArchivedField(boolean value) {
        this.archivedField.setValue(this, value);
    }

}
