package nl.petertillema.tibasic.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.language.TIBasicLanguage;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.syntax.TIBasicLexerAdapter;
import nl.petertillema.tibasic.syntax.TIBasicTokenSets;
import org.jetbrains.annotations.NotNull;

public class TIBasicParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(TIBasicLanguage.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new TIBasicLexerAdapter();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new TIBasicParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return TIBasicTokenSets.COMMENTS;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TIBasicTokenSets.STRINGS;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode astNode) {
        return TIBasicTypes.Factory.createElement(astNode);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider fileViewProvider) {
        return new TIBasicFile(fileViewProvider);
    }

}
