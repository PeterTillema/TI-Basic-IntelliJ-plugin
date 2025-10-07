package nl.petertillema.tibasic.language;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TIBasicFileType extends LanguageFileType {

    public static final TIBasicFileType INSTANCE = new TIBasicFileType();

    protected TIBasicFileType() {
        super(TIBasicLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "TI-Basic file";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "TI-Basic source file";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "basic";
    }

    @Override
    public Icon getIcon() {
        return TIBasicIcons.FILE;
    }

}
