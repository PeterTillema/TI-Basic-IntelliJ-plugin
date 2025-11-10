package nl.petertillema.tibasic.syntax;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import nl.petertillema.tibasic.psi.TIBasicTypes;

public interface TIBasicTokenSets {
    TokenSet COMMENTS = TokenSet.create(TIBasicTypes.COMMENT);
    TokenSet STRINGS = TokenSet.create(TIBasicTypes.STRING);
    TokenSet WHITESPACE = TokenSet.create(TokenType.WHITE_SPACE);

    TokenSet OPERATORS = TokenSet.create(
            TIBasicTypes.PLUS,
            TIBasicTypes.MINUS,
            TIBasicTypes.TIMES,
            TIBasicTypes.DIVIDE,
            TIBasicTypes.EQ,
            TIBasicTypes.NE,
            TIBasicTypes.GT,
            TIBasicTypes.GE,
            TIBasicTypes.LT,
            TIBasicTypes.LE
    );
    TokenSet COMMANDS = TokenSet.create(
            TIBasicTypes.IF,
            TIBasicTypes.THEN,
            TIBasicTypes.ELSE,
            TIBasicTypes.END,
            TIBasicTypes.REPEAT,
            TIBasicTypes.WHILE,
            TIBasicTypes.FOR,
            TIBasicTypes.GOTO,
            TIBasicTypes.LBL,
            TIBasicTypes.DELVAR,
            TIBasicTypes.COMMAND_NO_PARENS,
            TIBasicTypes.COMMAND_WITH_PARENS
    );
    TokenSet LOOPS = TokenSet.create(
            TIBasicTypes.FOR_STATEMENT,
            TIBasicTypes.REPEAT_STATEMENT,
            TIBasicTypes.WHILE_STATEMENT,
            TIBasicTypes.THEN_BLOCK,
            TIBasicTypes.ELSE_BLOCK
    );
}
