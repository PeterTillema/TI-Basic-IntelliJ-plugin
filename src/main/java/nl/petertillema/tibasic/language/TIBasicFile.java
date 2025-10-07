package nl.petertillema.tibasic.language;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class TIBasicFile extends PsiFileBase {

    public TIBasicFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, TIBasicLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return TIBasicFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "TI-Basic file";
    }

}
