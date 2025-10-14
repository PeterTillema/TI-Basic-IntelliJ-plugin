package nl.petertillema.tibasic.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TIBasicSettingsEditor extends SettingsEditor<TIBasicRunConfiguration> {

    private final JPanel jPanel;
    private final TextFieldWithBrowseButton inputPathField;
    private final TextFieldWithBrowseButton outputPathField;

    public TIBasicSettingsEditor() {
        this.inputPathField = new TextFieldWithBrowseButton();
        this.inputPathField.addBrowseFolderListener(null, FileChooserDescriptorFactory.singleFile().withTitle("Select Input File"));
        this.outputPathField = new TextFieldWithBrowseButton();
        this.outputPathField.addBrowseFolderListener(null, FileChooserDescriptorFactory.singleFile().withTitle("Select Output File"));
        this.jPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Input file", this.inputPathField)
                .addLabeledComponent("Output file", this.outputPathField)
                .getPanel();
    }

    @Override
    protected void resetEditorFrom(@NotNull TIBasicRunConfiguration s) {
        this.inputPathField.setText(s.getInputPathField());
        this.outputPathField.setText(s.getOutputPathField());
    }

    @Override
    protected void applyEditorTo(@NotNull TIBasicRunConfiguration s) {
        s.setInputPathField(this.inputPathField.getText());
        s.setOutputPathField(this.outputPathField.getText());
    }

    @Override
    protected @NotNull JComponent createEditor() {
        return this.jPanel;
    }

}
