// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import nl.petertillema.tibasic.psi.impl.*;

public interface TIBasicTypes {

  IElementType AND_EXPR = new TIBasicElementType("AND_EXPR");
  IElementType ANONYMOUS_LIST = new TIBasicElementType("ANONYMOUS_LIST");
  IElementType ANONYMOUS_MATRIX = new TIBasicElementType("ANONYMOUS_MATRIX");
  IElementType ANONYMOUS_MATRIX_ROW = new TIBasicElementType("ANONYMOUS_MATRIX_ROW");
  IElementType ASSIGNMENT_STATEMENT = new TIBasicElementType("ASSIGNMENT_STATEMENT");
  IElementType ASSIGNMENT_TARGET = new TIBasicElementType("ASSIGNMENT_TARGET");
  IElementType COMMAND_STATEMENT = new TIBasicElementType("COMMAND_STATEMENT");
  IElementType DEGREE_EXPR = new TIBasicElementType("DEGREE_EXPR");
  IElementType DELVAR_STATEMENT = new TIBasicElementType("DELVAR_STATEMENT");
  IElementType DISP_STATEMENT = new TIBasicElementType("DISP_STATEMENT");
  IElementType DIV_EXPR = new TIBasicElementType("DIV_EXPR");
  IElementType ELSE_BLOCK = new TIBasicElementType("ELSE_BLOCK");
  IElementType EQ_EXPR = new TIBasicElementType("EQ_EXPR");
  IElementType EXPR = new TIBasicElementType("EXPR");
  IElementType EXPR_STATEMENT = new TIBasicElementType("EXPR_STATEMENT");
  IElementType FACTORIAL_EXPR = new TIBasicElementType("FACTORIAL_EXPR");
  IElementType FOR_IDENTIFIER = new TIBasicElementType("FOR_IDENTIFIER");
  IElementType FOR_INITIALIZER = new TIBasicElementType("FOR_INITIALIZER");
  IElementType FOR_STATEMENT = new TIBasicElementType("FOR_STATEMENT");
  IElementType FUNC_EXPR = new TIBasicElementType("FUNC_EXPR");
  IElementType FUNC_OPTIONAL_EXPR = new TIBasicElementType("FUNC_OPTIONAL_EXPR");
  IElementType GE_EXPR = new TIBasicElementType("GE_EXPR");
  IElementType GOTO_NAME = new TIBasicElementType("GOTO_NAME");
  IElementType GOTO_STATEMENT = new TIBasicElementType("GOTO_STATEMENT");
  IElementType GT_EXPR = new TIBasicElementType("GT_EXPR");
  IElementType IF_STATEMENT = new TIBasicElementType("IF_STATEMENT");
  IElementType IMPLIED_MUL_EXPR = new TIBasicElementType("IMPLIED_MUL_EXPR");
  IElementType INVERSE_EXPR = new TIBasicElementType("INVERSE_EXPR");
  IElementType LBL_NAME = new TIBasicElementType("LBL_NAME");
  IElementType LBL_STATEMENT = new TIBasicElementType("LBL_STATEMENT");
  IElementType LE_EXPR = new TIBasicElementType("LE_EXPR");
  IElementType LIST_INDEX = new TIBasicElementType("LIST_INDEX");
  IElementType LITERAL_EXPR = new TIBasicElementType("LITERAL_EXPR");
  IElementType LT_EXPR = new TIBasicElementType("LT_EXPR");
  IElementType MATRIX_INDEX = new TIBasicElementType("MATRIX_INDEX");
  IElementType MENU_OPTION = new TIBasicElementType("MENU_OPTION");
  IElementType MENU_STATEMENT = new TIBasicElementType("MENU_STATEMENT");
  IElementType MINUS_EXPR = new TIBasicElementType("MINUS_EXPR");
  IElementType MUL_EXPR = new TIBasicElementType("MUL_EXPR");
  IElementType NCR_EXPR = new TIBasicElementType("NCR_EXPR");
  IElementType NEGATION_EXPR = new TIBasicElementType("NEGATION_EXPR");
  IElementType NE_EXPR = new TIBasicElementType("NE_EXPR");
  IElementType NPR_EXPR = new TIBasicElementType("NPR_EXPR");
  IElementType OR_EXPR = new TIBasicElementType("OR_EXPR");
  IElementType PAREN_EXPR = new TIBasicElementType("PAREN_EXPR");
  IElementType PLOT_STATEMENT = new TIBasicElementType("PLOT_STATEMENT");
  IElementType PLUS_EXPR = new TIBasicElementType("PLUS_EXPR");
  IElementType POW_2_EXPR = new TIBasicElementType("POW_2_EXPR");
  IElementType POW_3_EXPR = new TIBasicElementType("POW_3_EXPR");
  IElementType POW_EXPR = new TIBasicElementType("POW_EXPR");
  IElementType PRGM_STATEMENT = new TIBasicElementType("PRGM_STATEMENT");
  IElementType RADIAN_EXPR = new TIBasicElementType("RADIAN_EXPR");
  IElementType REPEAT_STATEMENT = new TIBasicElementType("REPEAT_STATEMENT");
  IElementType THEN_BLOCK = new TIBasicElementType("THEN_BLOCK");
  IElementType TRANSPOSE_EXPR = new TIBasicElementType("TRANSPOSE_EXPR");
  IElementType WHILE_STATEMENT = new TIBasicElementType("WHILE_STATEMENT");
  IElementType XOR_EXPR = new TIBasicElementType("XOR_EXPR");
  IElementType XROOT_EXPR = new TIBasicElementType("XROOT_EXPR");

  IElementType AND = new TIBasicTokenType("AND");
  IElementType ANS_VARIABLE = new TIBasicTokenType("ANS_VARIABLE");
  IElementType COLON = new TIBasicTokenType("COLON");
  IElementType COLOR_VARIABLE = new TIBasicTokenType("COLOR_VARIABLE");
  IElementType COMMA = new TIBasicTokenType("COMMA");
  IElementType COMMAND_NO_PARENS = new TIBasicTokenType("COMMAND_NO_PARENS");
  IElementType COMMAND_WITH_PARENS = new TIBasicTokenType("COMMAND_WITH_PARENS");
  IElementType COMMENT = new TIBasicTokenType("COMMENT");
  IElementType CRLF = new TIBasicTokenType("CRLF");
  IElementType CUSTOM_LIST_L = new TIBasicTokenType("CUSTOM_LIST_L");
  IElementType DELVAR = new TIBasicTokenType("DELVAR");
  IElementType DIM = new TIBasicTokenType("DIM");
  IElementType DISP = new TIBasicTokenType("DISP");
  IElementType DIVIDE = new TIBasicTokenType("DIVIDE");
  IElementType ELSE = new TIBasicTokenType("ELSE");
  IElementType END = new TIBasicTokenType("END");
  IElementType EQ = new TIBasicTokenType("EQ");
  IElementType EQUATION_VARIABLE = new TIBasicTokenType("EQUATION_VARIABLE");
  IElementType EXPR_FUNCTIONS_NO_ARGS = new TIBasicTokenType("EXPR_FUNCTIONS_NO_ARGS");
  IElementType EXPR_FUNCTIONS_OPTIONAL_ARGS = new TIBasicTokenType("EXPR_FUNCTIONS_OPTIONAL_ARGS");
  IElementType EXPR_FUNCTIONS_WITH_ARGS = new TIBasicTokenType("EXPR_FUNCTIONS_WITH_ARGS");
  IElementType EXPR_MODIFIER = new TIBasicTokenType("EXPR_MODIFIER");
  IElementType FACTORIAL = new TIBasicTokenType("FACTORIAL");
  IElementType FOR = new TIBasicTokenType("FOR");
  IElementType GE = new TIBasicTokenType("GE");
  IElementType GOTO = new TIBasicTokenType("GOTO");
  IElementType GT = new TIBasicTokenType("GT");
  IElementType IF = new TIBasicTokenType("IF");
  IElementType INVERSE = new TIBasicTokenType("INVERSE");
  IElementType LBL = new TIBasicTokenType("LBL");
  IElementType LBRACKET = new TIBasicTokenType("LBRACKET");
  IElementType LCURLY = new TIBasicTokenType("LCURLY");
  IElementType LE = new TIBasicTokenType("LE");
  IElementType LIST_VARIABLE = new TIBasicTokenType("LIST_VARIABLE");
  IElementType LPAREN = new TIBasicTokenType("LPAREN");
  IElementType LT = new TIBasicTokenType("LT");
  IElementType MATH_VARIABLE = new TIBasicTokenType("MATH_VARIABLE");
  IElementType MATRIX_VARIABLE = new TIBasicTokenType("MATRIX_VARIABLE");
  IElementType MINUS = new TIBasicTokenType("MINUS");
  IElementType NCR = new TIBasicTokenType("NCR");
  IElementType NE = new TIBasicTokenType("NE");
  IElementType NEG = new TIBasicTokenType("NEG");
  IElementType NPR = new TIBasicTokenType("NPR");
  IElementType NUMBER = new TIBasicTokenType("NUMBER");
  IElementType OR = new TIBasicTokenType("OR");
  IElementType PICTURE_VARIABLE = new TIBasicTokenType("PICTURE_VARIABLE");
  IElementType PLOT_COMMAND = new TIBasicTokenType("PLOT_COMMAND");
  IElementType PLOT_MARK = new TIBasicTokenType("PLOT_MARK");
  IElementType PLOT_TYPE = new TIBasicTokenType("PLOT_TYPE");
  IElementType PLUS = new TIBasicTokenType("PLUS");
  IElementType POW = new TIBasicTokenType("POW");
  IElementType POW2 = new TIBasicTokenType("POW2");
  IElementType POW3 = new TIBasicTokenType("POW3");
  IElementType PRGM_CALL = new TIBasicTokenType("PRGM_CALL");
  IElementType RBRACKET = new TIBasicTokenType("RBRACKET");
  IElementType RCURLY = new TIBasicTokenType("RCURLY");
  IElementType REPEAT = new TIBasicTokenType("REPEAT");
  IElementType RPAREN = new TIBasicTokenType("RPAREN");
  IElementType SIMPLE_VARIABLE = new TIBasicTokenType("SIMPLE_VARIABLE");
  IElementType STO = new TIBasicTokenType("STO");
  IElementType STRING = new TIBasicTokenType("STRING");
  IElementType STRING_VARIABLE = new TIBasicTokenType("STRING_VARIABLE");
  IElementType THEN = new TIBasicTokenType("THEN");
  IElementType TIMES = new TIBasicTokenType("TIMES");
  IElementType TOKEN = new TIBasicTokenType("TOKEN");
  IElementType TO_DEGREE = new TIBasicTokenType("TO_DEGREE");
  IElementType TO_RADIAN = new TIBasicTokenType("TO_RADIAN");
  IElementType TRANSPOSE = new TIBasicTokenType("TRANSPOSE");
  IElementType WHILE = new TIBasicTokenType("WHILE");
  IElementType WINDOW_TOKENS = new TIBasicTokenType("WINDOW_TOKENS");
  IElementType XOR = new TIBasicTokenType("XOR");
  IElementType XROOT = new TIBasicTokenType("XROOT");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == AND_EXPR) {
        return new TIBasicAndExprImpl(node);
      }
      else if (type == ANONYMOUS_LIST) {
        return new TIBasicAnonymousListImpl(node);
      }
      else if (type == ANONYMOUS_MATRIX) {
        return new TIBasicAnonymousMatrixImpl(node);
      }
      else if (type == ANONYMOUS_MATRIX_ROW) {
        return new TIBasicAnonymousMatrixRowImpl(node);
      }
      else if (type == ASSIGNMENT_STATEMENT) {
        return new TIBasicAssignmentStatementImpl(node);
      }
      else if (type == ASSIGNMENT_TARGET) {
        return new TIBasicAssignmentTargetImpl(node);
      }
      else if (type == COMMAND_STATEMENT) {
        return new TIBasicCommandStatementImpl(node);
      }
      else if (type == DEGREE_EXPR) {
        return new TIBasicDegreeExprImpl(node);
      }
      else if (type == DELVAR_STATEMENT) {
        return new TIBasicDelvarStatementImpl(node);
      }
      else if (type == DISP_STATEMENT) {
        return new TIBasicDispStatementImpl(node);
      }
      else if (type == DIV_EXPR) {
        return new TIBasicDivExprImpl(node);
      }
      else if (type == ELSE_BLOCK) {
        return new TIBasicElseBlockImpl(node);
      }
      else if (type == EQ_EXPR) {
        return new TIBasicEqExprImpl(node);
      }
      else if (type == EXPR_STATEMENT) {
        return new TIBasicExprStatementImpl(node);
      }
      else if (type == FACTORIAL_EXPR) {
        return new TIBasicFactorialExprImpl(node);
      }
      else if (type == FOR_IDENTIFIER) {
        return new TIBasicForIdentifierImpl(node);
      }
      else if (type == FOR_INITIALIZER) {
        return new TIBasicForInitializerImpl(node);
      }
      else if (type == FOR_STATEMENT) {
        return new TIBasicForStatementImpl(node);
      }
      else if (type == FUNC_EXPR) {
        return new TIBasicFuncExprImpl(node);
      }
      else if (type == FUNC_OPTIONAL_EXPR) {
        return new TIBasicFuncOptionalExprImpl(node);
      }
      else if (type == GE_EXPR) {
        return new TIBasicGeExprImpl(node);
      }
      else if (type == GOTO_NAME) {
        return new TIBasicGotoNameImpl(node);
      }
      else if (type == GOTO_STATEMENT) {
        return new TIBasicGotoStatementImpl(node);
      }
      else if (type == GT_EXPR) {
        return new TIBasicGtExprImpl(node);
      }
      else if (type == IF_STATEMENT) {
        return new TIBasicIfStatementImpl(node);
      }
      else if (type == IMPLIED_MUL_EXPR) {
        return new TIBasicImpliedMulExprImpl(node);
      }
      else if (type == INVERSE_EXPR) {
        return new TIBasicInverseExprImpl(node);
      }
      else if (type == LBL_NAME) {
        return new TIBasicLblNameImpl(node);
      }
      else if (type == LBL_STATEMENT) {
        return new TIBasicLblStatementImpl(node);
      }
      else if (type == LE_EXPR) {
        return new TIBasicLeExprImpl(node);
      }
      else if (type == LIST_INDEX) {
        return new TIBasicListIndexImpl(node);
      }
      else if (type == LITERAL_EXPR) {
        return new TIBasicLiteralExprImpl(node);
      }
      else if (type == LT_EXPR) {
        return new TIBasicLtExprImpl(node);
      }
      else if (type == MATRIX_INDEX) {
        return new TIBasicMatrixIndexImpl(node);
      }
      else if (type == MENU_OPTION) {
        return new TIBasicMenuOptionImpl(node);
      }
      else if (type == MENU_STATEMENT) {
        return new TIBasicMenuStatementImpl(node);
      }
      else if (type == MINUS_EXPR) {
        return new TIBasicMinusExprImpl(node);
      }
      else if (type == MUL_EXPR) {
        return new TIBasicMulExprImpl(node);
      }
      else if (type == NCR_EXPR) {
        return new TIBasicNcrExprImpl(node);
      }
      else if (type == NEGATION_EXPR) {
        return new TIBasicNegationExprImpl(node);
      }
      else if (type == NE_EXPR) {
        return new TIBasicNeExprImpl(node);
      }
      else if (type == NPR_EXPR) {
        return new TIBasicNprExprImpl(node);
      }
      else if (type == OR_EXPR) {
        return new TIBasicOrExprImpl(node);
      }
      else if (type == PAREN_EXPR) {
        return new TIBasicParenExprImpl(node);
      }
      else if (type == PLOT_STATEMENT) {
        return new TIBasicPlotStatementImpl(node);
      }
      else if (type == PLUS_EXPR) {
        return new TIBasicPlusExprImpl(node);
      }
      else if (type == POW_2_EXPR) {
        return new TIBasicPow2ExprImpl(node);
      }
      else if (type == POW_3_EXPR) {
        return new TIBasicPow3ExprImpl(node);
      }
      else if (type == POW_EXPR) {
        return new TIBasicPowExprImpl(node);
      }
      else if (type == PRGM_STATEMENT) {
        return new TIBasicPrgmStatementImpl(node);
      }
      else if (type == RADIAN_EXPR) {
        return new TIBasicRadianExprImpl(node);
      }
      else if (type == REPEAT_STATEMENT) {
        return new TIBasicRepeatStatementImpl(node);
      }
      else if (type == THEN_BLOCK) {
        return new TIBasicThenBlockImpl(node);
      }
      else if (type == TRANSPOSE_EXPR) {
        return new TIBasicTransposeExprImpl(node);
      }
      else if (type == WHILE_STATEMENT) {
        return new TIBasicWhileStatementImpl(node);
      }
      else if (type == XOR_EXPR) {
        return new TIBasicXorExprImpl(node);
      }
      else if (type == XROOT_EXPR) {
        return new TIBasicXrootExprImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
