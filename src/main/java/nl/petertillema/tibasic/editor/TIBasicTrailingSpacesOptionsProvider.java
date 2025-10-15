package nl.petertillema.tibasic.editor;

import com.intellij.openapi.fileEditor.TrailingSpacesOptionsProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import nl.petertillema.tibasic.language.TIBasicFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TIBasicTrailingSpacesOptionsProvider implements TrailingSpacesOptionsProvider {
    @Override
    public @Nullable Options getOptions(@NotNull Project project, @NotNull VirtualFile file) {
        if (!file.getFileType().equals(TIBasicFileType.INSTANCE)) {
            return null;
        }

        return new Options() {
            @Override
            public @NotNull Boolean getStripTrailingSpaces() {
                return false;
            }

            @Override
            public @NotNull Boolean getEnsureNewLineAtEOF() {
                return false;
            }

            @Override
            public @NotNull Boolean getRemoveTrailingBlankLines() {
                return false;
            }

            @Override
            public @NotNull Boolean getChangedLinesOnly() {
                return false;
            }

            @Override
            public @NotNull Boolean getKeepTrailingSpacesOnCaretLine() {
                return true;
            }
        };
    }
}
