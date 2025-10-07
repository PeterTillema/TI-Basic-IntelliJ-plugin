// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static nl.petertillema.tibasic.psi.TIBasicTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TIBasicParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return tibasicFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(AND_EXPR, DEC_EXPR, DEGREE_EXPR, DIV_EXPR,
      DMS_EXPR, EQ_EXPR, EXPR, FRAC_EXPR,
      FUNC_EXPR, GE_EXPR, GT_EXPR, IMPLIED_MUL_EXPR,
      INVERSE_EXPR, LE_EXPR, LITERAL_EXPR, LT_EXPR,
      MINUS_EXPR, MUL_EXPR, NCR_EXPR, NEGATION_EXPR,
      NE_EXPR, NPR_EXPR, OR_EXPR, PAREN_EXPR,
      PLUS_EXPR, POW_2_EXPR, POW_3_EXPR, POW_EXPR,
      RADIAN_EXPR, TRANSPOSE_EXPR, XOR_EXPR, XROOT_EXPR),
  };

  /* ********************************************************** */
  // "{" expr ("," expr)* ["}"]
  public static boolean anonymous_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ANONYMOUS_LIST, "<anonymous list>");
    r = consumeToken(b, "{");
    r = r && expr(b, l + 1, -1);
    r = r && anonymous_list_2(b, l + 1);
    r = r && anonymous_list_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ("," expr)*
  private static boolean anonymous_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_list_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!anonymous_list_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "anonymous_list_2", c)) break;
    }
    return true;
  }

  // "," expr
  private static boolean anonymous_list_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_list_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ",");
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ["}"]
  private static boolean anonymous_list_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_list_3")) return false;
    consumeToken(b, "}");
    return true;
  }

  /* ********************************************************** */
  // "[" anonymous_matrix_row ("," anonymous_matrix_row)* ["]"]
  public static boolean anonymous_matrix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ANONYMOUS_MATRIX, "<anonymous matrix>");
    r = consumeToken(b, "[");
    r = r && anonymous_matrix_row(b, l + 1);
    r = r && anonymous_matrix_2(b, l + 1);
    r = r && anonymous_matrix_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ("," anonymous_matrix_row)*
  private static boolean anonymous_matrix_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!anonymous_matrix_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "anonymous_matrix_2", c)) break;
    }
    return true;
  }

  // "," anonymous_matrix_row
  private static boolean anonymous_matrix_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ",");
    r = r && anonymous_matrix_row(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ["]"]
  private static boolean anonymous_matrix_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_3")) return false;
    consumeToken(b, "]");
    return true;
  }

  /* ********************************************************** */
  // "[" expr ("," expr)* ["]"]
  public static boolean anonymous_matrix_row(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_row")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ANONYMOUS_MATRIX_ROW, "<anonymous matrix row>");
    r = consumeToken(b, "[");
    r = r && expr(b, l + 1, -1);
    r = r && anonymous_matrix_row_2(b, l + 1);
    r = r && anonymous_matrix_row_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ("," expr)*
  private static boolean anonymous_matrix_row_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_row_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!anonymous_matrix_row_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "anonymous_matrix_row_2", c)) break;
    }
    return true;
  }

  // "," expr
  private static boolean anonymous_matrix_row_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_row_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ",");
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ["]"]
  private static boolean anonymous_matrix_row_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_row_3")) return false;
    consumeToken(b, "]");
    return true;
  }

  /* ********************************************************** */
  // COMMAND_WITH_PARENS expr ("," expr)* [")"]
  public static boolean arguments_command(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_command")) return false;
    if (!nextTokenIs(b, COMMAND_WITH_PARENS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMAND_WITH_PARENS);
    r = r && expr(b, l + 1, -1);
    r = r && arguments_command_2(b, l + 1);
    r = r && arguments_command_3(b, l + 1);
    exit_section_(b, m, ARGUMENTS_COMMAND, r);
    return r;
  }

  // ("," expr)*
  private static boolean arguments_command_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_command_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!arguments_command_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arguments_command_2", c)) break;
    }
    return true;
  }

  // "," expr
  private static boolean arguments_command_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_command_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ",");
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [")"]
  private static boolean arguments_command_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_command_3")) return false;
    consumeToken(b, ")");
    return true;
  }

  /* ********************************************************** */
  // expr "->" assignment_target
  public static boolean assignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNMENT, "<assignment>");
    r = expr(b, l + 1, -1);
    r = r && consumeToken(b, "->");
    r = r && assignment_target(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // assignment_target_matrix_index | assignment_target_list_index | assignment_target_variable | assignment_target_dim
  public static boolean assignment_target(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNMENT_TARGET, "<assignment target>");
    r = assignment_target_matrix_index(b, l + 1);
    if (!r) r = assignment_target_list_index(b, l + 1);
    if (!r) r = assignment_target_variable(b, l + 1);
    if (!r) r = assignment_target_dim(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "dim(" (LIST_VARIABLE | MATRIX_VARIABLE) [")"]
  static boolean assignment_target_dim(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_dim")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "dim(");
    r = r && assignment_target_dim_1(b, l + 1);
    r = r && assignment_target_dim_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LIST_VARIABLE | MATRIX_VARIABLE
  private static boolean assignment_target_dim_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_dim_1")) return false;
    boolean r;
    r = consumeToken(b, LIST_VARIABLE);
    if (!r) r = consumeToken(b, MATRIX_VARIABLE);
    return r;
  }

  // [")"]
  private static boolean assignment_target_dim_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_dim_2")) return false;
    consumeToken(b, ")");
    return true;
  }

  /* ********************************************************** */
  // LIST_VARIABLE "(" expr [")"]
  static boolean assignment_target_list_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_list_index")) return false;
    if (!nextTokenIs(b, LIST_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LIST_VARIABLE);
    r = r && consumeToken(b, "(");
    r = r && expr(b, l + 1, -1);
    r = r && assignment_target_list_index_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [")"]
  private static boolean assignment_target_list_index_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_list_index_3")) return false;
    consumeToken(b, ")");
    return true;
  }

  /* ********************************************************** */
  // MATRIX_VARIABLE "(" expr "," expr [")"]
  static boolean assignment_target_matrix_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_matrix_index")) return false;
    if (!nextTokenIs(b, MATRIX_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MATRIX_VARIABLE);
    r = r && consumeToken(b, "(");
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, ",");
    r = r && expr(b, l + 1, -1);
    r = r && assignment_target_matrix_index_5(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [")"]
  private static boolean assignment_target_matrix_index_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_matrix_index_5")) return false;
    consumeToken(b, ")");
    return true;
  }

  /* ********************************************************** */
  // LIST_VARIABLE | LIST_VARIABLE_NAME | EQUATION_VARIABLE_1 | EQUATION_VARIABLE_2 | EQUATION_VARIABLE_3 | EQUATION_VARIABLE_4 | STRING_VARIABLE | SIMPLE_VARIABLE | MATRIX_VARIABLE | WINDOW_TOKENS
  static boolean assignment_target_variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_variable")) return false;
    boolean r;
    r = consumeToken(b, LIST_VARIABLE);
    if (!r) r = consumeToken(b, LIST_VARIABLE_NAME);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_1);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_2);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_3);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_4);
    if (!r) r = consumeToken(b, STRING_VARIABLE);
    if (!r) r = consumeToken(b, SIMPLE_VARIABLE);
    if (!r) r = consumeToken(b, MATRIX_VARIABLE);
    if (!r) r = consumeToken(b, WINDOW_TOKENS);
    return r;
  }

  /* ********************************************************** */
  // if | while | repeat | for
  static boolean compound_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_statement")) return false;
    boolean r;
    r = if_$(b, l + 1);
    if (!r) r = while_$(b, l + 1);
    if (!r) r = repeat(b, l + 1);
    if (!r) r = for_$(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // ("DelVar " (LIST_VARIABLE | EQUATION_VARIABLE_1 | EQUATION_VARIABLE_2 | EQUATION_VARIABLE_3 | EQUATION_VARIABLE_4 | STRING_VARIABLE | SIMPLE_VARIABLE | MATRIX_VARIABLE))+ [statement]
  public static boolean delvar_command(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delvar_command")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DELVAR_COMMAND, "<delvar command>");
    r = delvar_command_0(b, l + 1);
    r = r && delvar_command_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ("DelVar " (LIST_VARIABLE | EQUATION_VARIABLE_1 | EQUATION_VARIABLE_2 | EQUATION_VARIABLE_3 | EQUATION_VARIABLE_4 | STRING_VARIABLE | SIMPLE_VARIABLE | MATRIX_VARIABLE))+
  private static boolean delvar_command_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delvar_command_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = delvar_command_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!delvar_command_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "delvar_command_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // "DelVar " (LIST_VARIABLE | EQUATION_VARIABLE_1 | EQUATION_VARIABLE_2 | EQUATION_VARIABLE_3 | EQUATION_VARIABLE_4 | STRING_VARIABLE | SIMPLE_VARIABLE | MATRIX_VARIABLE)
  private static boolean delvar_command_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delvar_command_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "DelVar ");
    r = r && delvar_command_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LIST_VARIABLE | EQUATION_VARIABLE_1 | EQUATION_VARIABLE_2 | EQUATION_VARIABLE_3 | EQUATION_VARIABLE_4 | STRING_VARIABLE | SIMPLE_VARIABLE | MATRIX_VARIABLE
  private static boolean delvar_command_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delvar_command_0_0_1")) return false;
    boolean r;
    r = consumeToken(b, LIST_VARIABLE);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_1);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_2);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_3);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_4);
    if (!r) r = consumeToken(b, STRING_VARIABLE);
    if (!r) r = consumeToken(b, SIMPLE_VARIABLE);
    if (!r) r = consumeToken(b, MATRIX_VARIABLE);
    return r;
  }

  // [statement]
  private static boolean delvar_command_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delvar_command_1")) return false;
    statement(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // "Else" end_block ["End"]
  public static boolean else_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_$")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ELSE, "<else $>");
    r = consumeToken(b, "Else");
    r = r && end_block(b, l + 1);
    r = r && else_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ["End"]
  private static boolean else_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_2")) return false;
    consumeToken(b, "End");
    return true;
  }

  /* ********************************************************** */
  // newline+ (!"End" (statement newline*))*
  public static boolean end_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, END_BLOCK, "<end block>");
    r = end_block_0(b, l + 1);
    r = r && end_block_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // newline+
  private static boolean end_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = newline(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!newline(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "end_block_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // (!"End" (statement newline*))*
  private static boolean end_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!end_block_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "end_block_1", c)) break;
    }
    return true;
  }

  // !"End" (statement newline*)
  private static boolean end_block_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = end_block_1_0_0(b, l + 1);
    r = r && end_block_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !"End"
  private static boolean end_block_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, "End");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // statement newline*
  private static boolean end_block_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    r = r && end_block_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // newline*
  private static boolean end_block_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_1_0_1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!newline(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "end_block_1_0_1_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // "For(" SIMPLE_VARIABLE "," expr "," expr ["," expr] [")"] end_block ["End"]
  public static boolean for_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_$")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FOR, "<for $>");
    r = consumeToken(b, "For(");
    r = r && consumeToken(b, SIMPLE_VARIABLE);
    r = r && consumeToken(b, ",");
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, ",");
    r = r && expr(b, l + 1, -1);
    r = r && for_6(b, l + 1);
    r = r && for_7(b, l + 1);
    r = r && end_block(b, l + 1);
    r = r && for_9(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ["," expr]
  private static boolean for_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_6")) return false;
    for_6_0(b, l + 1);
    return true;
  }

  // "," expr
  private static boolean for_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ",");
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [")"]
  private static boolean for_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_7")) return false;
    consumeToken(b, ")");
    return true;
  }

  // ["End"]
  private static boolean for_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_9")) return false;
    consumeToken(b, "End");
    return true;
  }

  /* ********************************************************** */
  // "Goto " lbl_name
  public static boolean goto_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goto_$")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GOTO, "<goto $>");
    r = consumeToken(b, "Goto ");
    r = r && lbl_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "If " expr newline (then | if_statement)
  public static boolean if_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_$")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IF, "<if $>");
    r = consumeToken(b, "If ");
    r = r && expr(b, l + 1, -1);
    r = r && newline(b, l + 1);
    r = r && if_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // then | if_statement
  private static boolean if_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_3")) return false;
    boolean r;
    r = then(b, l + 1);
    if (!r) r = if_statement(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // statement
  public static boolean if_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IF_STATEMENT, "<if statement>");
    r = statement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // NUMBER | EXPR_FUNCTIONS_NO_ARGS | ANS_VARIABLE | LIST_VARIABLE | EQUATION_VARIABLE_1 | EQUATION_VARIABLE_2 | EQUATION_VARIABLE_3 | EQUATION_VARIABLE_4 | SIMPLE_VARIABLE | COLOR_VARIABLE | paren_expr | func_expr
  static boolean implied_mul_arg(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "implied_mul_arg")) return false;
    boolean r;
    r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, EXPR_FUNCTIONS_NO_ARGS);
    if (!r) r = consumeToken(b, ANS_VARIABLE);
    if (!r) r = consumeToken(b, LIST_VARIABLE);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_1);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_2);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_3);
    if (!r) r = consumeToken(b, EQUATION_VARIABLE_4);
    if (!r) r = consumeToken(b, SIMPLE_VARIABLE);
    if (!r) r = consumeToken(b, COLOR_VARIABLE);
    if (!r) r = paren_expr(b, l + 1);
    if (!r) r = func_expr(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // "Lbl " lbl_name
  public static boolean lbl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lbl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LBL, "<lbl>");
    r = consumeToken(b, "Lbl ");
    r = r && lbl_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (SIMPLE_VARIABLE | NUMBER) [SIMPLE_VARIABLE | NUMBER]
  public static boolean lbl_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lbl_name")) return false;
    if (!nextTokenIs(b, "<lbl name>", NUMBER, SIMPLE_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LBL_NAME, "<lbl name>");
    r = lbl_name_0(b, l + 1);
    r = r && lbl_name_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SIMPLE_VARIABLE | NUMBER
  private static boolean lbl_name_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lbl_name_0")) return false;
    boolean r;
    r = consumeToken(b, SIMPLE_VARIABLE);
    if (!r) r = consumeToken(b, NUMBER);
    return r;
  }

  // [SIMPLE_VARIABLE | NUMBER]
  private static boolean lbl_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lbl_name_1")) return false;
    lbl_name_1_0(b, l + 1);
    return true;
  }

  // SIMPLE_VARIABLE | NUMBER
  private static boolean lbl_name_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lbl_name_1_0")) return false;
    boolean r;
    r = consumeToken(b, SIMPLE_VARIABLE);
    if (!r) r = consumeToken(b, NUMBER);
    return r;
  }

  /* ********************************************************** */
  // LIST_VARIABLE "(" expr [")"]
  public static boolean list_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_index")) return false;
    if (!nextTokenIs(b, LIST_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LIST_VARIABLE);
    r = r && consumeToken(b, "(");
    r = r && expr(b, l + 1, -1);
    r = r && list_index_3(b, l + 1);
    exit_section_(b, m, LIST_INDEX, r);
    return r;
  }

  // [")"]
  private static boolean list_index_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_index_3")) return false;
    consumeToken(b, ")");
    return true;
  }

  /* ********************************************************** */
  // MATRIX_VARIABLE "(" expr "," expr [")"]
  public static boolean matrix_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "matrix_index")) return false;
    if (!nextTokenIs(b, MATRIX_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MATRIX_VARIABLE);
    r = r && consumeToken(b, "(");
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, ",");
    r = r && expr(b, l + 1, -1);
    r = r && matrix_index_5(b, l + 1);
    exit_section_(b, m, MATRIX_INDEX, r);
    return r;
  }

  // [")"]
  private static boolean matrix_index_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "matrix_index_5")) return false;
    consumeToken(b, ")");
    return true;
  }

  /* ********************************************************** */
  // CRLF | ":"
  static boolean newline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "newline")) return false;
    boolean r;
    r = consumeToken(b, CRLF);
    if (!r) r = consumeToken(b, ":");
    return r;
  }

  /* ********************************************************** */
  // "Repeat " expr end_block ["End"]
  public static boolean repeat(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeat")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, REPEAT, "<repeat>");
    r = consumeToken(b, "Repeat ");
    r = r && expr(b, l + 1, -1);
    r = r && end_block(b, l + 1);
    r = r && repeat_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ["End"]
  private static boolean repeat_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeat_3")) return false;
    consumeToken(b, "End");
    return true;
  }

  /* ********************************************************** */
  // COMMAND_NO_PARENS [expr ("," expr)*]
  public static boolean simple_command(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command")) return false;
    if (!nextTokenIs(b, COMMAND_NO_PARENS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMAND_NO_PARENS);
    r = r && simple_command_1(b, l + 1);
    exit_section_(b, m, SIMPLE_COMMAND, r);
    return r;
  }

  // [expr ("," expr)*]
  private static boolean simple_command_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command_1")) return false;
    simple_command_1_0(b, l + 1);
    return true;
  }

  // expr ("," expr)*
  private static boolean simple_command_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1, -1);
    r = r && simple_command_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ("," expr)*
  private static boolean simple_command_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!simple_command_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "simple_command_1_0_1", c)) break;
    }
    return true;
  }

  // "," expr
  private static boolean simple_command_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ",");
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // small_statement
  static boolean simple_statement(PsiBuilder b, int l) {
    return small_statement(b, l + 1);
  }

  /* ********************************************************** */
  // delvar_command | goto | lbl | simple_command | arguments_command | assignment | expr
  static boolean small_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "small_statement")) return false;
    boolean r;
    r = delvar_command(b, l + 1);
    if (!r) r = goto_$(b, l + 1);
    if (!r) r = lbl(b, l + 1);
    if (!r) r = simple_command(b, l + 1);
    if (!r) r = arguments_command(b, l + 1);
    if (!r) r = assignment(b, l + 1);
    if (!r) r = expr(b, l + 1, -1);
    return r;
  }

  /* ********************************************************** */
  // compound_statement | simple_statement
  static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    r = compound_statement(b, l + 1);
    if (!r) r = simple_statement(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // statement (newline+ statement)*
  static boolean statements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statements")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    r = r && statements_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (newline+ statement)*
  private static boolean statements_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statements_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statements_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "statements_1", c)) break;
    }
    return true;
  }

  // newline+ statement
  private static boolean statements_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statements_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statements_1_0_0(b, l + 1);
    r = r && statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // newline+
  private static boolean statements_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statements_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = newline(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!newline(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "statements_1_0_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // "Then" then_block [else | "End"]
  public static boolean then(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, THEN, "<then>");
    r = consumeToken(b, "Then");
    r = r && then_block(b, l + 1);
    r = r && then_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [else | "End"]
  private static boolean then_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_2")) return false;
    then_2_0(b, l + 1);
    return true;
  }

  // else | "End"
  private static boolean then_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_2_0")) return false;
    boolean r;
    r = else_$(b, l + 1);
    if (!r) r = consumeToken(b, "End");
    return r;
  }

  /* ********************************************************** */
  // newline+ (!"Else" !"End" (statement newline*))*
  public static boolean then_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, THEN_BLOCK, "<then block>");
    r = then_block_0(b, l + 1);
    r = r && then_block_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // newline+
  private static boolean then_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = newline(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!newline(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "then_block_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // (!"Else" !"End" (statement newline*))*
  private static boolean then_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!then_block_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "then_block_1", c)) break;
    }
    return true;
  }

  // !"Else" !"End" (statement newline*)
  private static boolean then_block_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = then_block_1_0_0(b, l + 1);
    r = r && then_block_1_0_1(b, l + 1);
    r = r && then_block_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !"Else"
  private static boolean then_block_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, "Else");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // !"End"
  private static boolean then_block_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, "End");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // statement newline*
  private static boolean then_block_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_1_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    r = r && then_block_1_0_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // newline*
  private static boolean then_block_1_0_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_1_0_2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!newline(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "then_block_1_0_2_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // newline* [statements] newline*
  static boolean tibasicFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tibasicFile")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = tibasicFile_0(b, l + 1);
    r = r && tibasicFile_1(b, l + 1);
    r = r && tibasicFile_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // newline*
  private static boolean tibasicFile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tibasicFile_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!newline(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tibasicFile_0", c)) break;
    }
    return true;
  }

  // [statements]
  private static boolean tibasicFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tibasicFile_1")) return false;
    statements(b, l + 1);
    return true;
  }

  // newline*
  private static boolean tibasicFile_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tibasicFile_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!newline(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tibasicFile_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SPECIAL_CHARACTER | COMMENT | TOKEN
  static boolean unused_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unused_")) return false;
    boolean r;
    r = consumeToken(b, SPECIAL_CHARACTER);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, TOKEN);
    return r;
  }

  /* ********************************************************** */
  // "While " expr end_block ["End"]
  public static boolean while_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_$")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, WHILE, "<while $>");
    r = consumeToken(b, "While ");
    r = r && expr(b, l + 1, -1);
    r = r && end_block(b, l + 1);
    r = r && while_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ["End"]
  private static boolean while_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_3")) return false;
    consumeToken(b, "End");
    return true;
  }

  /* ********************************************************** */
  // Expression root: expr
  // Operator priority table:
  // 0: POSTFIX(dms_expr) POSTFIX(dec_expr) POSTFIX(frac_expr)
  // 1: BINARY(or_expr) BINARY(xor_expr)
  // 2: BINARY(and_expr)
  // 3: BINARY(eq_expr) BINARY(ne_expr) BINARY(gt_expr) BINARY(ge_expr)
  //    BINARY(lt_expr) BINARY(le_expr)
  // 4: BINARY(plus_expr) BINARY(minus_expr)
  // 5: BINARY(mul_expr) BINARY(div_expr) ATOM(implied_mul_expr)
  // 6: BINARY(npr_expr) BINARY(ncr_expr)
  // 7: PREFIX(negation_expr)
  // 8: BINARY(pow_expr) BINARY(xroot_expr)
  // 9: POSTFIX(radian_expr) POSTFIX(degree_expr) POSTFIX(inverse_expr) POSTFIX(pow2_expr)
  //    POSTFIX(transpose_expr) POSTFIX(pow3_expr)
  // 10: ATOM(literal_expr) ATOM(func_expr) PREFIX(paren_expr)
  public static boolean expr(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expr")) return false;
    addVariant(b, "<expr>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expr>");
    r = implied_mul_expr(b, l + 1);
    if (!r) r = negation_expr(b, l + 1);
    if (!r) r = literal_expr(b, l + 1);
    if (!r) r = func_expr(b, l + 1);
    if (!r) r = paren_expr(b, l + 1);
    p = r;
    r = r && expr_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean expr_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expr_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 0 && consumeTokenSmart(b, "►DMS")) {
        r = true;
        exit_section_(b, l, m, DMS_EXPR, r, true, null);
      }
      else if (g < 0 && consumeTokenSmart(b, "►Dec")) {
        r = true;
        exit_section_(b, l, m, DEC_EXPR, r, true, null);
      }
      else if (g < 0 && consumeTokenSmart(b, "►Frac")) {
        r = true;
        exit_section_(b, l, m, FRAC_EXPR, r, true, null);
      }
      else if (g < 1 && consumeTokenSmart(b, " or ")) {
        r = expr(b, l, 1);
        exit_section_(b, l, m, OR_EXPR, r, true, null);
      }
      else if (g < 1 && consumeTokenSmart(b, " xor ")) {
        r = expr(b, l, 1);
        exit_section_(b, l, m, XOR_EXPR, r, true, null);
      }
      else if (g < 2 && consumeTokenSmart(b, " and ")) {
        r = expr(b, l, 2);
        exit_section_(b, l, m, AND_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, "=")) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, EQ_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, "!=")) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, NE_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, ">")) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, GT_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, ">=")) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, GE_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, "<")) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, LT_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, "<=")) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, LE_EXPR, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, "+")) {
        r = expr(b, l, 4);
        exit_section_(b, l, m, PLUS_EXPR, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, "-")) {
        r = expr(b, l, 4);
        exit_section_(b, l, m, MINUS_EXPR, r, true, null);
      }
      else if (g < 5 && consumeTokenSmart(b, "*")) {
        r = expr(b, l, 5);
        exit_section_(b, l, m, MUL_EXPR, r, true, null);
      }
      else if (g < 5 && consumeTokenSmart(b, "/")) {
        r = expr(b, l, 5);
        exit_section_(b, l, m, DIV_EXPR, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, " nPr ")) {
        r = expr(b, l, 6);
        exit_section_(b, l, m, NPR_EXPR, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, " nCr ")) {
        r = expr(b, l, 6);
        exit_section_(b, l, m, NCR_EXPR, r, true, null);
      }
      else if (g < 8 && consumeTokenSmart(b, "^")) {
        r = expr(b, l, 8);
        exit_section_(b, l, m, POW_EXPR, r, true, null);
      }
      else if (g < 8 && consumeTokenSmart(b, "×√")) {
        r = expr(b, l, 8);
        exit_section_(b, l, m, XROOT_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, "^^r")) {
        r = true;
        exit_section_(b, l, m, RADIAN_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, "^^o")) {
        r = true;
        exit_section_(b, l, m, DEGREE_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, "^^-1")) {
        r = true;
        exit_section_(b, l, m, INVERSE_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, "^^2")) {
        r = true;
        exit_section_(b, l, m, POW_2_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, "^^T")) {
        r = true;
        exit_section_(b, l, m, TRANSPOSE_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, "^^3")) {
        r = true;
        exit_section_(b, l, m, POW_3_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // implied_mul_arg implied_mul_arg+
  public static boolean implied_mul_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "implied_mul_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, IMPLIED_MUL_EXPR, "<implied mul expr>");
    r = implied_mul_arg(b, l + 1);
    r = r && implied_mul_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // implied_mul_arg+
  private static boolean implied_mul_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "implied_mul_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = implied_mul_arg(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!implied_mul_arg(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "implied_mul_expr_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean negation_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negation_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = negation_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 7);
    exit_section_(b, l, m, NEGATION_EXPR, r, p, null);
    return r || p;
  }

  // "‾" | "~"
  private static boolean negation_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negation_expr_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, "‾");
    if (!r) r = consumeTokenSmart(b, "~");
    return r;
  }

  // list_index | matrix_index | EXPR_FUNCTIONS_NO_ARGS | ANS_VARIABLE | LIST_VARIABLE | EQUATION_VARIABLE_1 | EQUATION_VARIABLE_2 | EQUATION_VARIABLE_3 | EQUATION_VARIABLE_4 | STRING_VARIABLE | SIMPLE_VARIABLE | MATRIX_VARIABLE | COLOR_VARIABLE | NUMBER | STRING | anonymous_list | anonymous_matrix
  public static boolean literal_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL_EXPR, "<literal expr>");
    r = list_index(b, l + 1);
    if (!r) r = matrix_index(b, l + 1);
    if (!r) r = consumeTokenSmart(b, EXPR_FUNCTIONS_NO_ARGS);
    if (!r) r = consumeTokenSmart(b, ANS_VARIABLE);
    if (!r) r = consumeTokenSmart(b, LIST_VARIABLE);
    if (!r) r = consumeTokenSmart(b, EQUATION_VARIABLE_1);
    if (!r) r = consumeTokenSmart(b, EQUATION_VARIABLE_2);
    if (!r) r = consumeTokenSmart(b, EQUATION_VARIABLE_3);
    if (!r) r = consumeTokenSmart(b, EQUATION_VARIABLE_4);
    if (!r) r = consumeTokenSmart(b, STRING_VARIABLE);
    if (!r) r = consumeTokenSmart(b, SIMPLE_VARIABLE);
    if (!r) r = consumeTokenSmart(b, MATRIX_VARIABLE);
    if (!r) r = consumeTokenSmart(b, COLOR_VARIABLE);
    if (!r) r = consumeTokenSmart(b, NUMBER);
    if (!r) r = consumeTokenSmart(b, STRING);
    if (!r) r = anonymous_list(b, l + 1);
    if (!r) r = anonymous_matrix(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // EXPR_FUNCTIONS_WITH_ARGS expr ("," expr)* [")"]
  public static boolean func_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_expr")) return false;
    if (!nextTokenIsSmart(b, EXPR_FUNCTIONS_WITH_ARGS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EXPR_FUNCTIONS_WITH_ARGS);
    r = r && expr(b, l + 1, -1);
    r = r && func_expr_2(b, l + 1);
    r = r && func_expr_3(b, l + 1);
    exit_section_(b, m, FUNC_EXPR, r);
    return r;
  }

  // ("," expr)*
  private static boolean func_expr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_expr_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!func_expr_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "func_expr_2", c)) break;
    }
    return true;
  }

  // "," expr
  private static boolean func_expr_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_expr_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, ",");
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [")"]
  private static boolean func_expr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_expr_3")) return false;
    consumeTokenSmart(b, ")");
    return true;
  }

  public static boolean paren_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, "(");
    p = r;
    r = p && expr(b, l, -1);
    r = p && report_error_(b, paren_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, PAREN_EXPR, r, p, null);
    return r || p;
  }

  // [")"]
  private static boolean paren_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_expr_1")) return false;
    consumeToken(b, ")");
    return true;
  }

}
