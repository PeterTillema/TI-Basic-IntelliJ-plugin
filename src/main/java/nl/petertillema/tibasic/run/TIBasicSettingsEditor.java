package nl.petertillema.tibasic.run;

import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class TIBasicSettingsEditor extends SettingsEditor<TIBasicRunConfiguration> {

    private final TIBasicSettingsEditorForm form;

    public TIBasicSettingsEditor() {
        this.form = new TIBasicSettingsEditorForm();
    }

    @Override
    protected void resetEditorFrom(@NotNull TIBasicRunConfiguration s) {
        this.form.inputPathField.setText(s.getOptions().getInputPathField());
        this.form.outputPathField.setText(s.getOptions().getOutputPathField());
        this.form.programNameField.setText(s.getOptions().getProgramNameField());
        this.form.programTypeField.setSelectedItem(s.getOptions().getProgramTypeField());
        this.form.archivedField.setSelected(s.getOptions().getArchivedField());
    }

    @Override
    protected void applyEditorTo(@NotNull TIBasicRunConfiguration s) {
        s.getOptions().setInputPathField(this.form.inputPathField.getText());
        s.getOptions().setOutputPathField(this.form.outputPathField.getText());
        s.getOptions().setProgramNameField(this.form.programNameField.getText());
        s.getOptions().setProgramTypeField((String) this.form.programTypeField.getSelectedItem());
        s.getOptions().setArchivedField(this.form.archivedField.isSelected());
    }

    @Override
    protected @NotNull JComponent createEditor() {
        return this.form.getMainPanel();
    }

}
