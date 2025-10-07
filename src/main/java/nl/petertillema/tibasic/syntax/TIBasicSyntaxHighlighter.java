package nl.petertillema.tibasic.syntax;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class TIBasicSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey COMMENT = createTextAttributesKey("TIBASIC_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey STRING = createTextAttributesKey("TIBASIC_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER = createTextAttributesKey("TIBASIC_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey COMMAND = createTextAttributesKey("TIBASIC_COMMAND", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
    public static final TextAttributesKey FUNCTION = createTextAttributesKey("TIBASIC_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_CALL);
    public static final TextAttributesKey ANS_IDENTIFIER = createTextAttributesKey("TIBASIC_ANS_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey LIST_IDENTIFIER = createTextAttributesKey("TIBASIC_LIST_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey EQUATION_IDENTIFIER = createTextAttributesKey("TIBASIC_EQUATION_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey PICTURE_IDENTIFIER = createTextAttributesKey("TIBASIC_PICTURE_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey GDB_IDENTIFIER = createTextAttributesKey("TIBASIC_GDB_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey STRING_IDENTIFIER = createTextAttributesKey("TIBASIC_STRING_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey SIMPLE_IDENTIFIER = createTextAttributesKey("TIBASIC_SIMPLE_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey MATRIX_IDENTIFIER = createTextAttributesKey("TIBASIC_MATRIX_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey STATISTIC_IDENTIFIER = createTextAttributesKey("TIBASIC_STATISTIC_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey COLOR_IDENTIFIER = createTextAttributesKey("TIBASIC_COLOR_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey IMAGE_IDENTIFIER = createTextAttributesKey("TIBASIC_IMAGE_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("TIBASIC_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] COMMAND_KEYS = new TextAttributesKey[]{COMMAND};
    private static final TextAttributesKey[] FUNCTION_KEYS = new TextAttributesKey[]{FUNCTION};
    private static final TextAttributesKey[] ANS_IDENTIFIER_KEYS = new TextAttributesKey[]{ANS_IDENTIFIER};
    private static final TextAttributesKey[] LIST_IDENTIFIER_KEYS = new TextAttributesKey[]{LIST_IDENTIFIER};
    private static final TextAttributesKey[] EQUATION_IDENTIFIER_KEYS = new TextAttributesKey[]{EQUATION_IDENTIFIER};
    private static final TextAttributesKey[] PICTURE_IDENTIFIER_KEYS = new TextAttributesKey[]{PICTURE_IDENTIFIER};
    private static final TextAttributesKey[] GDB_IDENTIFIER_KEYS = new TextAttributesKey[]{GDB_IDENTIFIER};
    private static final TextAttributesKey[] STRING_IDENTIFIER_KEYS = new TextAttributesKey[]{STRING_IDENTIFIER};
    private static final TextAttributesKey[] SIMPLE_IDENTIFIER_KEYS = new TextAttributesKey[]{SIMPLE_IDENTIFIER};
    private static final TextAttributesKey[] MATRIX_IDENTIFIER_KEYS = new TextAttributesKey[]{MATRIX_IDENTIFIER};
    private static final TextAttributesKey[] STATISTIC_IDENTIFIER_KEYS = new TextAttributesKey[]{STATISTIC_IDENTIFIER};
    private static final TextAttributesKey[] COLOR_IDENTIFIER_KEYS = new TextAttributesKey[]{COLOR_IDENTIFIER};
    private static final TextAttributesKey[] IMAGE_IDENTIFIER_KEYS = new TextAttributesKey[]{IMAGE_IDENTIFIER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new TIBasicLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(TIBasicTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.STRING)) {
            return STRING_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.NUMBER)) {
            return NUMBER_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.COMMAND_NO_PARENS) || tokenType.equals(TIBasicTypes.COMMAND_WITH_PARENS)) {
            return COMMAND_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.EXPR_FUNCTIONS_NO_ARGS) || tokenType.equals(TIBasicTypes.EXPR_FUNCTIONS_WITH_ARGS)) {
            return FUNCTION_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.ANS_VARIABLE)) {
            return ANS_IDENTIFIER_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.LIST_VARIABLE)) {
            return LIST_IDENTIFIER_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.EQUATION_VARIABLE_1) || tokenType.equals(TIBasicTypes.EQUATION_VARIABLE_2) || tokenType.equals(TIBasicTypes.EQUATION_VARIABLE_3) || tokenType.equals(TIBasicTypes.EQUATION_VARIABLE_4)) {
            return EQUATION_IDENTIFIER_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.STRING_VARIABLE)) {
            return STRING_IDENTIFIER_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.SIMPLE_VARIABLE)) {
            return SIMPLE_IDENTIFIER_KEYS;
        }
        if (tokenType.equals(TIBasicTypes.MATRIX_VARIABLE)) {
            return MATRIX_IDENTIFIER_KEYS;
        }
        if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }

        return EMPTY_KEYS;
    }

}
