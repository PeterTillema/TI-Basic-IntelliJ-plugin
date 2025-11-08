package nl.petertillema.tibasic.structure;

import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension;
import com.intellij.lang.Language;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.language.TIBasicLanguage;
import nl.petertillema.tibasic.psi.TIBasicElseBlock;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.petertillema.tibasic.psi.TIBasicUtil.getTextUntilNewline;

public class TIBasicStructureAwareNavbar extends StructureAwareNavBarModelExtension {

    @Override
    protected @NotNull Language getLanguage() {
        return TIBasicLanguage.INSTANCE;
    }

    @Override
    public @Nullable String getPresentableText(Object o) {
        return switch (o) {
            case TIBasicFile file -> file.getName();
            case TIBasicWhileStatement statement -> getTextUntilNewline(statement);
            case TIBasicRepeatStatement statement -> getTextUntilNewline(statement);
            case TIBasicForStatement statement -> getTextUntilNewline(statement);
            case TIBasicIfStatement statement -> getTextUntilNewline(statement);
            case TIBasicThenBlock statement -> getTextUntilNewline(statement);
            case TIBasicElseBlock statement -> getTextUntilNewline(statement);
            default -> null;
        };
    }
}
