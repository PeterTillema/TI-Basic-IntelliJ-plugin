package nl.petertillema.tibasic.editor;

import com.intellij.lang.Language;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.language.TIBasicLanguage;
import nl.petertillema.tibasic.psi.TIBasicElseBlock;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import org.jetbrains.annotations.NotNull;

import static nl.petertillema.tibasic.psi.TIBasicUtil.getTextUntilNewline;

public class TIBasicBreadCrumbsProvider implements BreadcrumbsProvider {

    @Override
    public Language[] getLanguages() {
        return new Language[]{TIBasicLanguage.INSTANCE};
    }

    @Override
    public boolean acceptElement(@NotNull PsiElement element) {
        return element instanceof TIBasicWhileStatement ||
                element instanceof TIBasicRepeatStatement ||
                element instanceof TIBasicForStatement ||
                element instanceof TIBasicIfStatement ||
                element instanceof TIBasicElseBlock;
    }

    @Override
    public @NotNull @NlsSafe String getElementInfo(@NotNull PsiElement element) {
        return switch (element) {
            case TIBasicFile file -> file.getName();
            case TIBasicWhileStatement statement -> getTextUntilNewline(statement);
            case TIBasicRepeatStatement statement -> getTextUntilNewline(statement);
            case TIBasicForStatement statement -> getTextUntilNewline(statement);
            case TIBasicIfStatement statement -> getTextUntilNewline(statement);
            case TIBasicThenBlock statement -> getTextUntilNewline(statement);
            case TIBasicElseBlock statement -> getTextUntilNewline(statement);
            default -> "";
        };
    }

}
