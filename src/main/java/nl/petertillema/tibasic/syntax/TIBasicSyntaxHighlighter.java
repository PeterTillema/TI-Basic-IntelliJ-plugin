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

import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public final class TIBasicSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey COMMENT = createTextAttributesKey("TIBASIC_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey STRING = createTextAttributesKey("TIBASIC_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER = createTextAttributesKey("TIBASIC_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey COMMAND = createTextAttributesKey("TIBASIC_COMMAND", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
    public static final TextAttributesKey FUNCTION = createTextAttributesKey("TIBASIC_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_CALL);
    public static final TextAttributesKey LOGICAL_OPERATOR = createTextAttributesKey("TIBASIC_LOGICAL_OPERATOR", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
    public static final TextAttributesKey OPERATOR = createTextAttributesKey("TIBASIC_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey COMMA = createTextAttributesKey("TIBASIC_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey PARENTHESES = createTextAttributesKey("TIBASIC_PARENTHESIS", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey BRACKETS = createTextAttributesKey("TIBASIC_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey BRACES = createTextAttributesKey("TIBASIC_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey PRGM_CALL = createTextAttributesKey("TIBASIC_PRGM_CALL");
    public static final TextAttributesKey ANS_IDENTIFIER = createTextAttributesKey("TIBASIC_ANS_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey LIST_IDENTIFIER = createTextAttributesKey("TIBASIC_LIST_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey EQUATION_IDENTIFIER = createTextAttributesKey("TIBASIC_EQUATION_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey PICTURE_IDENTIFIER = createTextAttributesKey("TIBASIC_PICTURE_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey STRING_IDENTIFIER = createTextAttributesKey("TIBASIC_STRING_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey SIMPLE_IDENTIFIER = createTextAttributesKey("TIBASIC_SIMPLE_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey MATRIX_IDENTIFIER = createTextAttributesKey("TIBASIC_MATRIX_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey COLOR_IDENTIFIER = createTextAttributesKey("TIBASIC_COLOR_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey WINDOW_IDENTIFIER = createTextAttributesKey("TIBASIC_WINDOW_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("TIBASIC_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final Map<IElementType, TextAttributesKey> keys;

    static {
        keys = new HashMap<>();

        // Basic things
        keys.put(TIBasicTypes.COMMENT, COMMENT);
        keys.put(TIBasicTypes.STRING, STRING);
        keys.put(TIBasicTypes.NUMBER, NUMBER);
        keys.put(TIBasicTypes.COMMA, COMMA);

        // Brackets et alia
        keys.put(TIBasicTypes.LPAREN, PARENTHESES);
        keys.put(TIBasicTypes.RPAREN, PARENTHESES);
        keys.put(TIBasicTypes.LBRACKET, BRACKETS);
        keys.put(TIBasicTypes.RBRACKET, BRACKETS);
        keys.put(TIBasicTypes.LCURLY, BRACES);
        keys.put(TIBasicTypes.RCURLY, BRACES);

        // Commands and functions
        keys.put(TIBasicTypes.PRGM_CALL, PRGM_CALL);
        fillMap(keys, TIBasicTokenSets.COMMANDS, COMMAND);
        fillMap(keys, TIBasicTokenSets.FUNCTIONS, FUNCTION);

        // Variables
        keys.put(TIBasicTypes.ANS_VARIABLE, ANS_IDENTIFIER);
        keys.put(TIBasicTypes.LIST_VARIABLE, LIST_IDENTIFIER);
        keys.put(TIBasicTypes.EQUATION_VARIABLE, EQUATION_IDENTIFIER);
        keys.put(TIBasicTypes.PICTURE_VARIABLE, PICTURE_IDENTIFIER);
        keys.put(TIBasicTypes.STRING_VARIABLE, STRING_IDENTIFIER);
        keys.put(TIBasicTypes.SIMPLE_VARIABLE, SIMPLE_IDENTIFIER);
        keys.put(TIBasicTypes.MATRIX_VARIABLE, MATRIX_IDENTIFIER);
        keys.put(TIBasicTypes.COLOR_VARIABLE, COLOR_IDENTIFIER);
        keys.put(TIBasicTypes.WINDOW_VARIABLE, WINDOW_IDENTIFIER);

        // Operators
        fillMap(keys, TIBasicTokenSets.OPERATORS, OPERATOR);
        keys.put(TIBasicTypes.AND, LOGICAL_OPERATOR);
        keys.put(TIBasicTypes.OR, LOGICAL_OPERATOR);
        keys.put(TIBasicTypes.XOR, LOGICAL_OPERATOR);

        keys.put(TokenType.BAD_CHARACTER, BAD_CHARACTER);
    }

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new TIBasicLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        return SyntaxHighlighterBase.pack(keys.get(tokenType));
    }

}
