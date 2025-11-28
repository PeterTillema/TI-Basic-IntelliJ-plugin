package nl.petertillema.tibasic.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicStatement;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.patterns.StandardPatterns.or;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static nl.petertillema.tibasic.completion.TIBasicPatterns.AFTER_STO_DIM;
import static nl.petertillema.tibasic.completion.TIBasicPatterns.CONTROL_FLOW_INITIALIZER;
import static nl.petertillema.tibasic.completion.TIBasicPatterns.START_OF_FILE;
import static nl.petertillema.tibasic.completion.TIBasicPatterns.START_OF_LINE;
import static nl.petertillema.tibasic.completion.TIBasicPatterns.WITHIN_EXPRESSION;
import static nl.petertillema.tibasic.completion.TIBasicPatterns.afterType;

/**
 * The completion contributor has several cases where to add suggestions to the completion contributor.
 * Based on commands:
 *   - After -> operator dim( and the variables: LIST, EQUATION, STRING, SIMPLE, MATRIX and WINDOW
 *   - After -> operator and dim( function, LIST and MATRIX
 *   - After DelVar: LIST, EQUATION, STRING, SIMPLE, MATRIX, WINDOW and PICTURE
 *   - After Goto: NONE (default, since it's not an expression)
 *   - After Lbl: NONE (default, since it's not an expression)
 *   - After Asm(: NONE (default, since it's not an expression)
 *   - After Disp: all expression functions, variables and EXPR_MODIFIER
 *   - todo: After Menu(: NONE (since most of the time the options are just strings)
 *   - todo: After Plot(:
 *       first argument: PLOT_TYPE
 *       second/third argument: all expression functions and variables
 *       fourth argument: PLOT_MARK/all expression functions and variables
 *       fifth argument: all expression functions and variables
 *   - After any other command: all expression functions and variables
 *   - Within If statement: THEN
 *   - Within Then block: ELSE
 *   - Within any block which is not an If statement: END
 *   - After IS>( / DS<( / For(: SIMPLE
 *   - After any other flow command: all expression functions and variables
 *   - In any expression context, without the mentioned control flow initializers: all expression functions and variables
 */
public class TIBasicCompletionContributor extends CompletionContributor {

    public TIBasicCompletionContributor() {
        extend(CompletionType.BASIC, AFTER_STO_DIM, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                addFromSet(result, TIBasicKeywords.LIST_MATRIX_VARIABLES);
            }
        });

        extend(CompletionType.BASIC, afterType(TIBasicTypes.STO), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                addFromSet(result, TIBasicKeywords.SIMPLE_VARIABLES);
                addFromSet(result, TIBasicKeywords.LIST_MATRIX_VARIABLES);
                addFromSet(result, TIBasicKeywords.STORE_VARIABLES);
            }
        });

        extend(CompletionType.BASIC, afterType(TIBasicTypes.DELVAR), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                addFromSet(result, TIBasicKeywords.SIMPLE_VARIABLES);
                addFromSet(result, TIBasicKeywords.LIST_MATRIX_VARIABLES);
                addFromSet(result, TIBasicKeywords.STORE_VARIABLES);
                addFromSet(result, TIBasicKeywords.OTHER_VARIABLES);
            }
        });

        extend(CompletionType.BASIC, or(START_OF_FILE, START_OF_LINE), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                PsiElement element = parameters.getPosition();
                TIBasicStatement statement = getParentOfType(element, TIBasicStatement.class, false);
                if (statement == null) return;

                // Manually add the loop control statements
                if (statement.getParent() instanceof TIBasicIfStatement)
                    result.addElement(LookupElementBuilder.create("Then"));
                if (statement.getParent() instanceof TIBasicThenBlock)
                    result.addElement(LookupElementBuilder.create("Else"));
                if (!(statement.getParent() instanceof TIBasicFile) && !(statement.getParent() instanceof TIBasicIfStatement))
                    result.addElement(LookupElementBuilder.create("End"));

                addFromSet(result, TIBasicKeywords.SIMPLE_VARIABLES);
                addFromSet(result, TIBasicKeywords.LIST_MATRIX_VARIABLES);
                addFromSet(result, TIBasicKeywords.STORE_VARIABLES);
                addFromSet(result, TIBasicKeywords.OTHER_VARIABLES);
                addCommands(result);
                addExprFunctions(result);
                result.addElement(LookupElementBuilder.create("Ans"));
            }
        });

        extend(CompletionType.BASIC, WITHIN_EXPRESSION.andNot(CONTROL_FLOW_INITIALIZER), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                addFromSet(result, TIBasicKeywords.SIMPLE_VARIABLES);
                addExprFunctions(result);
            }
        });
    }

    private static void addFromSet(CompletionResultSet result, List<String> set) {
        for (String s : set) {
            result.addElement(LookupElementBuilder.create(s).withTypeText("variable"));
        }
    }

    private static void addExprFunctions(CompletionResultSet result) {
        for (String s : TIBasicKeywords.EXPR_FUNCTIONS_NO_ARGS) {
            result.addElement(LookupElementBuilder.create(s).withTypeText("expression", true));
        }
        for (String s : TIBasicKeywords.EXPR_FUNCTIONS_OPTIONAL_ARGS) {
            result.addElement(LookupElementBuilder.create(s)
                    .withTailText("(...)", true)
                    .withTypeText("expression", true));
        }
        for (String s : TIBasicKeywords.EXPR_FUNCTIONS_WITH_ARGS) {
            result.addElement(LookupElementBuilder.create(s)
                    .withTailText("(...)", true)
                    .withTypeText("expression", true));
        }
    }

    private static void addCommands(CompletionResultSet result) {
        for (String s : TIBasicKeywords.CONTROL_FLOW_KEYWORDS) {
            result.addElement(LookupElementBuilder.create(s).withTypeText("command", true));
        }
        for (String s : TIBasicKeywords.COMMANDS_NO_PARENS) {
            result.addElement(LookupElementBuilder.create(s).withTypeText("command", true));
        }
        for (String s : TIBasicKeywords.COMMANDS_WITH_PARENS) {
            result.addElement(LookupElementBuilder.create(s).withTailText("(...)", true).withTypeText("command", true));
        }
        for (String s : TIBasicKeywords.PLOT_COMMANDS) {
            result.addElement(LookupElementBuilder.create(s).withTailText("(...)", true).withTypeText("command", true));
        }
    }
}
