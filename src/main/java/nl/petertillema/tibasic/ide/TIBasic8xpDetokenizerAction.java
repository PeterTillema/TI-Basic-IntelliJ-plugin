package nl.petertillema.tibasic.ide;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.PsiFileFactory;
import com.intellij.refactoring.SkipOverwriteChoice;
import com.intellij.util.containers.ContainerUtil;
import nl.petertillema.tibasic.language.TIBasicFileType;
import nl.petertillema.tibasic.tokenization.TIBasicTokenizerService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class TIBasic8xpDetokenizerAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        var file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        var psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (file == null || e.getProject() == null || psiFile == null) {
            e.getPresentation().setVisible(false);
        } else {
            var name = file.getName();
            var index = name.lastIndexOf('.');
            String extension = null;
            if (index > 0) {
                extension = name.substring(index + 1);
            }

            if (!"8xp".equalsIgnoreCase(extension)) {
                e.getPresentation().setVisible(false);
            }
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        var file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        var psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (file == null || e.getProject() == null || psiFile == null) return;

        var name = file.getName();
        var index = name.lastIndexOf('.');
        String newName;
        String extension = null;
        if (index > 0) {
            newName = name.substring(0, index) + ".basic";
            extension = name.substring(index + 1);
        } else {
            newName = name + ".basic";
        }

        var parentDirectory = psiFile.getContainingDirectory();
        if (!"8xp".equalsIgnoreCase(extension)) return;

        try {
            var bytes = file.contentsToByteArray();

            var service = ApplicationManager.getApplication().getService(TIBasicTokenizerService.class);
            var detokenized = service.detokenize(bytes);
            var newFile = PsiFileFactory.getInstance(e.getProject()).createFileFromText(newName, TIBasicFileType.INSTANCE, detokenized);

            // Eventually add to the parent directory
            var caseSensitive = parentDirectory.getVirtualFile().isCaseSensitive();
            var existing = ContainerUtil.find(parentDirectory.getVirtualFile().getChildren(),
                    item -> Comparing.strEqual(item.getName(), newName, caseSensitive));
            if (existing != null) {
                var choice = SkipOverwriteChoice.askUser(parentDirectory, newName, "Create", false);
                if (choice == SkipOverwriteChoice.OVERWRITE) {
                    ApplicationManager.getApplication().runWriteAction(() -> {
                        try {
                            existing.delete(null);
                            parentDirectory.add(newFile);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
            } else {
                ApplicationManager.getApplication().runWriteAction(() -> {
                    parentDirectory.add(newFile);
                });
            }
        } catch (Exception ignored) {
        }
    }
}
