package nl.petertillema.tibasic.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import javax.swing.*;

public class TIBasicSettingsEditorForm {
    private JPanel panel1;
    protected TextFieldWithBrowseButton inputPathField;
    protected TextFieldWithBrowseButton outputPathField;
    protected JTextField programNameField;
    protected JComboBox<String> programTypeField;
    protected JCheckBox archivedField;

    public TIBasicSettingsEditorForm() {
        this.inputPathField.addBrowseFolderListener(null, FileChooserDescriptorFactory.singleFile().withTitle("Select Input File"));
        this.outputPathField.addBrowseFolderListener(null, FileChooserDescriptorFactory.singleFile().withTitle("Select Output File"));
        this.programNameField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                var text = ((JTextField) input).getText();
                return !text.isEmpty() && text.length() <= 8;
            }
        });
    }

    public JPanel getMainPanel() {
        return this.panel1;
    }
}
