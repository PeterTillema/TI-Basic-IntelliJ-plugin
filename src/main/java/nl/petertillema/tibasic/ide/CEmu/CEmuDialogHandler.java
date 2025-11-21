package nl.petertillema.tibasic.ide.CEmu;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefFileDialogCallback;
import org.cef.handler.CefDialogHandler;

import java.util.List;
import java.util.Vector;

public class CEmuDialogHandler implements CefDialogHandler {

    @Override
    public boolean onFileDialog(CefBrowser browser, FileDialogMode mode, String title, String defaultFilePath, Vector<String> acceptFilters, CefFileDialogCallback callback) {
        ApplicationManager.getApplication().invokeLater(() -> {
            String[] extensions = acceptFilters.stream()
                    .map(extension -> extension.startsWith(".") ? extension.substring(1) : extension)
                    .toList()
                    .toArray(String[]::new);
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.singleFile();
            if (extensions.length > 0) {
                descriptor = descriptor.withExtensionFilter("Abc", extensions);
            }
            var chosenFile = FileChooser.chooseFile(descriptor, null, null);
            if (chosenFile != null) {
                callback.Continue(new Vector<>(List.of(chosenFile.getPath())));
            } else {
                callback.Cancel();
            }
        });

        return true;
    }

}
