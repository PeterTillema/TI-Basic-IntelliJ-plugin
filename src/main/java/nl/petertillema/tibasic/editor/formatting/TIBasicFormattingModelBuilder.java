package nl.petertillema.tibasic.editor.formatting;

import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import nl.petertillema.tibasic.language.TIBasicLanguage;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.syntax.TIBasicTokenSets;
import org.jetbrains.annotations.NotNull;

public final class TIBasicFormattingModelBuilder implements FormattingModelBuilder {

    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider.createFormattingModelForPsiFile(
                formattingContext.getContainingFile(),
                new TIBasicBlock(
                        formattingContext.getNode(),
                        Indent.getNoneIndent(),
                        createSpacingBuilder(codeStyleSettings)
                ),
                codeStyleSettings
        );
    }

    private static SpacingBuilder createSpacingBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, TIBasicLanguage.INSTANCE)
                // Keywords
                .after(TIBasicTypes.IF).spaces(1)
                .after(TIBasicTypes.THEN).none()
                .after(TIBasicTypes.ELSE).none()
                .after(TIBasicTypes.END).none()
                .after(TIBasicTypes.WHILE).spaces(1)
                .after(TIBasicTypes.REPEAT).spaces(1)
                .after(TIBasicTypes.FOR).none()
                .after(TIBasicTypes.GOTO).spaces(1)
                .after(TIBasicTypes.LBL).spaces(1)
                .between(TIBasicTypes.COMMAND_WITH_PARENS, TIBasicTypes.LPAREN).none()
                // Functions
                .between(TIBasicTypes.EXPR_FUNCTIONS_WITH_ARGS, TIBasicTypes.LPAREN).none()
                // Operators
                .around(TIBasicTypes.STO).none()
                .before(TIBasicTypes.POW2).none()
                .before(TIBasicTypes.POW3).none()
                .before(TIBasicTypes.TRANSPOSE).none()
                .before(TIBasicTypes.TO_DEGREE).none()
                .before(TIBasicTypes.TO_RADIAN).none()
                .before(TIBasicTypes.INVERSE).none()
                .around(TIBasicTokenSets.OPERATORS).none()
                .after(TIBasicTypes.NEG).none()
                .around(TIBasicTypes.OR).spaces(1)
                .around(TIBasicTypes.XOR).spaces(1)
                .around(TIBasicTypes.AND).spaces(1)
                .around(TIBasicTypes.NPR).spaces(1)
                .around(TIBasicTypes.NCR).spaces(1)
                // Punctuation
                .around(TIBasicTypes.COMMA).none()
                .after(TIBasicTypes.LPAREN).none()
                .before(TIBasicTypes.RPAREN).none()
                .after(TIBasicTypes.LCURLY).none()
                .before(TIBasicTypes.RCURLY).none()
                .after(TIBasicTypes.LBRACKET).none()
                .before(TIBasicTypes.RBRACKET).none()
                ;
    }

}
