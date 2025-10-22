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
  // CRLF | COLON
  static boolean NEWLINE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NEWLINE")) return false;
    if (!nextTokenIs(b, "", COLON, CRLF)) return false;
    boolean r;
    r = consumeToken(b, CRLF);
    if (!r) r = consumeToken(b, COLON);
    return r;
  }

  /* ********************************************************** */
  // LCURLY expr (COMMA expr)* [RCURLY]
  public static boolean anonymous_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_list")) return false;
    if (!nextTokenIs(b, LCURLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ANONYMOUS_LIST, null);
    r = consumeToken(b, LCURLY);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1, -1));
    r = p && report_error_(b, anonymous_list_2(b, l + 1)) && r;
    r = p && anonymous_list_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA expr)*
  private static boolean anonymous_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_list_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!anonymous_list_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "anonymous_list_2", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean anonymous_list_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_list_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [RCURLY]
  private static boolean anonymous_list_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_list_3")) return false;
    consumeToken(b, RCURLY);
    return true;
  }

  /* ********************************************************** */
  // LBRACKET anonymous_matrix_row (COMMA anonymous_matrix_row)* [RBRACKET]
  public static boolean anonymous_matrix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ANONYMOUS_MATRIX, null);
    r = consumeToken(b, LBRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, anonymous_matrix_row(b, l + 1));
    r = p && report_error_(b, anonymous_matrix_2(b, l + 1)) && r;
    r = p && anonymous_matrix_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA anonymous_matrix_row)*
  private static boolean anonymous_matrix_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!anonymous_matrix_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "anonymous_matrix_2", c)) break;
    }
    return true;
  }

  // COMMA anonymous_matrix_row
  private static boolean anonymous_matrix_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && anonymous_matrix_row(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [RBRACKET]
  private static boolean anonymous_matrix_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_3")) return false;
    consumeToken(b, RBRACKET);
    return true;
  }

  /* ********************************************************** */
  // LBRACKET expr (COMMA expr)* [RBRACKET]
  public static boolean anonymous_matrix_row(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_row")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ANONYMOUS_MATRIX_ROW, null);
    r = consumeToken(b, LBRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1, -1));
    r = p && report_error_(b, anonymous_matrix_row_2(b, l + 1)) && r;
    r = p && anonymous_matrix_row_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA expr)*
  private static boolean anonymous_matrix_row_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_row_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!anonymous_matrix_row_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "anonymous_matrix_row_2", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean anonymous_matrix_row_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_row_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [RBRACKET]
  private static boolean anonymous_matrix_row_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anonymous_matrix_row_3")) return false;
    consumeToken(b, RBRACKET);
    return true;
  }

  /* ********************************************************** */
  // COMMAND_WITH_PARENS LPAREN expr (COMMA expr)* [RPAREN]
  static boolean arguments_command(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_command")) return false;
    if (!nextTokenIs(b, COMMAND_WITH_PARENS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, COMMAND_WITH_PARENS, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1, -1));
    r = p && report_error_(b, arguments_command_3(b, l + 1)) && r;
    r = p && arguments_command_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA expr)*
  private static boolean arguments_command_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_command_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!arguments_command_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arguments_command_3", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean arguments_command_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_command_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [RPAREN]
  private static boolean arguments_command_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_command_4")) return false;
    consumeToken(b, RPAREN);
    return true;
  }

  /* ********************************************************** */
  // expr STO assignment_target
  public static boolean assignment_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNMENT_STATEMENT, "<assignment statement>");
    r = expr(b, l + 1, -1);
    r = r && consumeToken(b, STO);
    p = r; // pin = 2
    r = r && assignment_target(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // "dim(" (LIST_VARIABLE | MATRIX_VARIABLE) [RPAREN]
  static boolean assignment_target_dim(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_dim")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, "dim(");
    p = r; // pin = 1
    r = r && report_error_(b, assignment_target_dim_1(b, l + 1));
    r = p && assignment_target_dim_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // LIST_VARIABLE | MATRIX_VARIABLE
  private static boolean assignment_target_dim_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_dim_1")) return false;
    boolean r;
    r = consumeToken(b, LIST_VARIABLE);
    if (!r) r = consumeToken(b, MATRIX_VARIABLE);
    return r;
  }

  // [RPAREN]
  private static boolean assignment_target_dim_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_dim_2")) return false;
    consumeToken(b, RPAREN);
    return true;
  }

  /* ********************************************************** */
  // LIST_VARIABLE LPAREN expr [RPAREN]
  static boolean assignment_target_list_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_list_index")) return false;
    if (!nextTokenIs(b, LIST_VARIABLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 2, LIST_VARIABLE, LPAREN);
    p = r; // pin = 2
    r = r && report_error_(b, expr(b, l + 1, -1));
    r = p && assignment_target_list_index_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [RPAREN]
  private static boolean assignment_target_list_index_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_list_index_3")) return false;
    consumeToken(b, RPAREN);
    return true;
  }

  /* ********************************************************** */
  // MATRIX_VARIABLE LPAREN expr COMMA expr [RPAREN]
  static boolean assignment_target_matrix_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_matrix_index")) return false;
    if (!nextTokenIs(b, MATRIX_VARIABLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 2, MATRIX_VARIABLE, LPAREN);
    p = r; // pin = 2
    r = r && report_error_(b, expr(b, l + 1, -1));
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, expr(b, l + 1, -1)) && r;
    r = p && assignment_target_matrix_index_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [RPAREN]
  private static boolean assignment_target_matrix_index_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_target_matrix_index_5")) return false;
    consumeToken(b, RPAREN);
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
  // simple_command | arguments_command
  public static boolean command_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_statement")) return false;
    if (!nextTokenIs(b, "<command statement>", COMMAND_NO_PARENS, COMMAND_WITH_PARENS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMAND_STATEMENT, "<command statement>");
    r = simple_command(b, l + 1);
    if (!r) r = arguments_command(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // if_statement | while_statement | repeat_statement | for_statement
  static boolean compound_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_statement")) return false;
    boolean r;
    r = if_statement(b, l + 1);
    if (!r) r = while_statement(b, l + 1);
    if (!r) r = repeat_statement(b, l + 1);
    if (!r) r = for_statement(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // ("DelVar " (LIST_VARIABLE | EQUATION_VARIABLE_1 | EQUATION_VARIABLE_2 | EQUATION_VARIABLE_3 | EQUATION_VARIABLE_4 | STRING_VARIABLE | SIMPLE_VARIABLE | MATRIX_VARIABLE))+ [statement]
  public static boolean delvar_command(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delvar_command")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DELVAR_COMMAND, "<delvar command>");
    r = delvar_command_0(b, l + 1);
    p = r; // pin = 1
    r = r && delvar_command_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // ELSE end_block [END]
  public static boolean else_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_statement")) return false;
    if (!nextTokenIs(b, ELSE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && end_block(b, l + 1);
    r = r && else_statement_2(b, l + 1);
    exit_section_(b, m, ELSE_STATEMENT, r);
    return r;
  }

  // [END]
  private static boolean else_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_statement_2")) return false;
    consumeToken(b, END);
    return true;
  }

  /* ********************************************************** */
  // NEWLINE+ (!"End" (statement NEWLINE*))*
  public static boolean end_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block")) return false;
    if (!nextTokenIs(b, "<end block>", COLON, CRLF)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, END_BLOCK, "<end block>");
    r = end_block_0(b, l + 1);
    r = r && end_block_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NEWLINE+
  private static boolean end_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = NEWLINE(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!NEWLINE(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "end_block_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // (!"End" (statement NEWLINE*))*
  private static boolean end_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!end_block_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "end_block_1", c)) break;
    }
    return true;
  }

  // !"End" (statement NEWLINE*)
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

  // statement NEWLINE*
  private static boolean end_block_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    r = r && end_block_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEWLINE*
  private static boolean end_block_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "end_block_1_0_1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!NEWLINE(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "end_block_1_0_1_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SIMPLE_VARIABLE
  public static boolean for_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_identifier")) return false;
    if (!nextTokenIs(b, SIMPLE_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SIMPLE_VARIABLE);
    exit_section_(b, m, FOR_IDENTIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // FOR LPAREN for_identifier COMMA expr COMMA expr [COMMA expr] [RPAREN] end_block [END]
  public static boolean for_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement")) return false;
    if (!nextTokenIs(b, FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, FOR, LPAREN);
    r = r && for_identifier(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, COMMA);
    r = r && expr(b, l + 1, -1);
    r = r && for_statement_7(b, l + 1);
    r = r && for_statement_8(b, l + 1);
    r = r && end_block(b, l + 1);
    r = r && for_statement_10(b, l + 1);
    exit_section_(b, m, FOR_STATEMENT, r);
    return r;
  }

  // [COMMA expr]
  private static boolean for_statement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_7")) return false;
    for_statement_7_0(b, l + 1);
    return true;
  }

  // COMMA expr
  private static boolean for_statement_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [RPAREN]
  private static boolean for_statement_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_8")) return false;
    consumeToken(b, RPAREN);
    return true;
  }

  // [END]
  private static boolean for_statement_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_10")) return false;
    consumeToken(b, END);
    return true;
  }

  /* ********************************************************** */
  // (SIMPLE_VARIABLE | NUMBER) [SIMPLE_VARIABLE | NUMBER]
  public static boolean goto_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goto_name")) return false;
    if (!nextTokenIs(b, "<goto name>", NUMBER, SIMPLE_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GOTO_NAME, "<goto name>");
    r = goto_name_0(b, l + 1);
    r = r && goto_name_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SIMPLE_VARIABLE | NUMBER
  private static boolean goto_name_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goto_name_0")) return false;
    boolean r;
    r = consumeToken(b, SIMPLE_VARIABLE);
    if (!r) r = consumeToken(b, NUMBER);
    return r;
  }

  // [SIMPLE_VARIABLE | NUMBER]
  private static boolean goto_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goto_name_1")) return false;
    goto_name_1_0(b, l + 1);
    return true;
  }

  // SIMPLE_VARIABLE | NUMBER
  private static boolean goto_name_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goto_name_1_0")) return false;
    boolean r;
    r = consumeToken(b, SIMPLE_VARIABLE);
    if (!r) r = consumeToken(b, NUMBER);
    return r;
  }

  /* ********************************************************** */
  // GOTO goto_name
  public static boolean goto_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goto_statement")) return false;
    if (!nextTokenIs(b, GOTO)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GOTO_STATEMENT, null);
    r = consumeToken(b, GOTO);
    p = r; // pin = 1
    r = r && goto_name(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // IF expr NEWLINE (then_statement | statement)
  public static boolean if_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IF);
    r = r && expr(b, l + 1, -1);
    r = r && NEWLINE(b, l + 1);
    r = r && if_statement_3(b, l + 1);
    exit_section_(b, m, IF_STATEMENT, r);
    return r;
  }

  // then_statement | statement
  private static boolean if_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_3")) return false;
    boolean r;
    r = then_statement(b, l + 1);
    if (!r) r = statement(b, l + 1);
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
  // LBL lbl_name
  public static boolean lbl_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lbl_statement")) return false;
    if (!nextTokenIs(b, LBL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LBL_STATEMENT, null);
    r = consumeToken(b, LBL);
    p = r; // pin = 1
    r = r && lbl_name(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // LIST_VARIABLE LPAREN expr [RPAREN]
  public static boolean list_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_index")) return false;
    if (!nextTokenIs(b, LIST_VARIABLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LIST_INDEX, null);
    r = consumeTokens(b, 2, LIST_VARIABLE, LPAREN);
    p = r; // pin = 2
    r = r && report_error_(b, expr(b, l + 1, -1));
    r = p && list_index_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [RPAREN]
  private static boolean list_index_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_index_3")) return false;
    consumeToken(b, RPAREN);
    return true;
  }

  /* ********************************************************** */
  // MATRIX_VARIABLE LPAREN expr COMMA expr [RPAREN]
  public static boolean matrix_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "matrix_index")) return false;
    if (!nextTokenIs(b, MATRIX_VARIABLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MATRIX_INDEX, null);
    r = consumeTokens(b, 2, MATRIX_VARIABLE, LPAREN);
    p = r; // pin = 2
    r = r && report_error_(b, expr(b, l + 1, -1));
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, expr(b, l + 1, -1)) && r;
    r = p && matrix_index_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [RPAREN]
  private static boolean matrix_index_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "matrix_index_5")) return false;
    consumeToken(b, RPAREN);
    return true;
  }

  /* ********************************************************** */
  // REPEAT expr end_block [END]
  public static boolean repeat_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeat_statement")) return false;
    if (!nextTokenIs(b, REPEAT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, REPEAT);
    r = r && expr(b, l + 1, -1);
    r = r && end_block(b, l + 1);
    r = r && repeat_statement_3(b, l + 1);
    exit_section_(b, m, REPEAT_STATEMENT, r);
    return r;
  }

  // [END]
  private static boolean repeat_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeat_statement_3")) return false;
    consumeToken(b, END);
    return true;
  }

  /* ********************************************************** */
  // COMMAND_NO_PARENS [(expr (COMMA expr)*)]
  static boolean simple_command(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command")) return false;
    if (!nextTokenIs(b, COMMAND_NO_PARENS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMAND_NO_PARENS);
    p = r; // pin = 1
    r = r && simple_command_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [(expr (COMMA expr)*)]
  private static boolean simple_command_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command_1")) return false;
    simple_command_1_0(b, l + 1);
    return true;
  }

  // expr (COMMA expr)*
  private static boolean simple_command_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1, -1);
    r = r && simple_command_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA expr)*
  private static boolean simple_command_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!simple_command_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "simple_command_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean simple_command_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_command_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // small_statement
  static boolean simple_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = small_statement(b, l + 1);
    exit_section_(b, l, m, r, false, TIBasicParser::statement_recover);
    return r;
  }

  /* ********************************************************** */
  // delvar_command | goto_statement | lbl_statement | command_statement | assignment_statement | expr
  static boolean small_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "small_statement")) return false;
    boolean r;
    r = delvar_command(b, l + 1);
    if (!r) r = goto_statement(b, l + 1);
    if (!r) r = lbl_statement(b, l + 1);
    if (!r) r = command_statement(b, l + 1);
    if (!r) r = assignment_statement(b, l + 1);
    if (!r) r = expr(b, l + 1, -1);
    return r;
  }

  /* ********************************************************** */
  // compound_statement | simple_statement
  public static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT, "<statement>");
    r = compound_statement(b, l + 1);
    if (!r) r = simple_statement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !NEWLINE
  static boolean statement_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !NEWLINE(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // statement (NEWLINE+ statement)*
  static boolean statements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statements")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    r = r && statements_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (NEWLINE+ statement)*
  private static boolean statements_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statements_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statements_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "statements_1", c)) break;
    }
    return true;
  }

  // NEWLINE+ statement
  private static boolean statements_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statements_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statements_1_0_0(b, l + 1);
    r = r && statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEWLINE+
  private static boolean statements_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statements_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = NEWLINE(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!NEWLINE(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "statements_1_0_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // NEWLINE+ (!"Else" !"End" (statement NEWLINE*))*
  public static boolean then_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block")) return false;
    if (!nextTokenIs(b, "<then block>", COLON, CRLF)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, THEN_BLOCK, "<then block>");
    r = then_block_0(b, l + 1);
    r = r && then_block_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NEWLINE+
  private static boolean then_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = NEWLINE(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!NEWLINE(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "then_block_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // (!"Else" !"End" (statement NEWLINE*))*
  private static boolean then_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!then_block_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "then_block_1", c)) break;
    }
    return true;
  }

  // !"Else" !"End" (statement NEWLINE*)
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

  // statement NEWLINE*
  private static boolean then_block_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_1_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    r = r && then_block_1_0_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEWLINE*
  private static boolean then_block_1_0_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_block_1_0_2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!NEWLINE(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "then_block_1_0_2_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // THEN then_block [else_statement | END]
  public static boolean then_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_statement")) return false;
    if (!nextTokenIs(b, THEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, THEN);
    r = r && then_block(b, l + 1);
    r = r && then_statement_2(b, l + 1);
    exit_section_(b, m, THEN_STATEMENT, r);
    return r;
  }

  // [else_statement | END]
  private static boolean then_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_statement_2")) return false;
    then_statement_2_0(b, l + 1);
    return true;
  }

  // else_statement | END
  private static boolean then_statement_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "then_statement_2_0")) return false;
    boolean r;
    r = else_statement(b, l + 1);
    if (!r) r = consumeToken(b, END);
    return r;
  }

  /* ********************************************************** */
  // NEWLINE* [statements] NEWLINE*
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

  // NEWLINE*
  private static boolean tibasicFile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tibasicFile_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!NEWLINE(b, l + 1)) break;
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

  // NEWLINE*
  private static boolean tibasicFile_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tibasicFile_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!NEWLINE(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tibasicFile_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TOKEN
  static boolean unused_(PsiBuilder b, int l) {
    return consumeToken(b, TOKEN);
  }

  /* ********************************************************** */
  // WHILE expr end_block [END]
  public static boolean while_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement")) return false;
    if (!nextTokenIs(b, WHILE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHILE);
    r = r && expr(b, l + 1, -1);
    r = r && end_block(b, l + 1);
    r = r && while_statement_3(b, l + 1);
    exit_section_(b, m, WHILE_STATEMENT, r);
    return r;
  }

  // [END]
  private static boolean while_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement_3")) return false;
    consumeToken(b, END);
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
      else if (g < 1 && consumeTokenSmart(b, OR)) {
        r = expr(b, l, 1);
        exit_section_(b, l, m, OR_EXPR, r, true, null);
      }
      else if (g < 1 && consumeTokenSmart(b, XOR)) {
        r = expr(b, l, 1);
        exit_section_(b, l, m, XOR_EXPR, r, true, null);
      }
      else if (g < 2 && consumeTokenSmart(b, AND)) {
        r = expr(b, l, 2);
        exit_section_(b, l, m, AND_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, EQ)) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, EQ_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, NE)) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, NE_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, GT)) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, GT_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, GE)) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, GE_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, LT)) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, LT_EXPR, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, LE)) {
        r = expr(b, l, 3);
        exit_section_(b, l, m, LE_EXPR, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, PLUS)) {
        r = expr(b, l, 4);
        exit_section_(b, l, m, PLUS_EXPR, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, MINUS)) {
        r = expr(b, l, 4);
        exit_section_(b, l, m, MINUS_EXPR, r, true, null);
      }
      else if (g < 5 && consumeTokenSmart(b, TIMES)) {
        r = expr(b, l, 5);
        exit_section_(b, l, m, MUL_EXPR, r, true, null);
      }
      else if (g < 5 && consumeTokenSmart(b, DIVIDE)) {
        r = expr(b, l, 5);
        exit_section_(b, l, m, DIV_EXPR, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, NPR)) {
        r = expr(b, l, 6);
        exit_section_(b, l, m, NPR_EXPR, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, NCR)) {
        r = expr(b, l, 6);
        exit_section_(b, l, m, NCR_EXPR, r, true, null);
      }
      else if (g < 8 && consumeTokenSmart(b, POW)) {
        r = expr(b, l, 8);
        exit_section_(b, l, m, POW_EXPR, r, true, null);
      }
      else if (g < 8 && consumeTokenSmart(b, "×√")) {
        r = expr(b, l, 8);
        exit_section_(b, l, m, XROOT_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, TO_RADIAN)) {
        r = true;
        exit_section_(b, l, m, RADIAN_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, TO_DEGREE)) {
        r = true;
        exit_section_(b, l, m, DEGREE_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, INVERSE)) {
        r = true;
        exit_section_(b, l, m, INVERSE_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, POW2)) {
        r = true;
        exit_section_(b, l, m, POW_2_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, TRANSPOSE)) {
        r = true;
        exit_section_(b, l, m, TRANSPOSE_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, POW3)) {
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
    if (!nextTokenIsSmart(b, NEG)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, NEG);
    p = r;
    r = p && expr(b, l, 7);
    exit_section_(b, l, m, NEGATION_EXPR, r, p, null);
    return r || p;
  }

  // list_index | matrix_index | EXPR_FUNCTIONS_NO_ARGS | ANS_VARIABLE | LIST_VARIABLE | EQUATION_VARIABLE_1 | EQUATION_VARIABLE_2 | EQUATION_VARIABLE_3 | EQUATION_VARIABLE_4 | STRING_VARIABLE | SIMPLE_VARIABLE | MATRIX_VARIABLE | COLOR_VARIABLE | MATH_VARIABLE | NUMBER | STRING | anonymous_list | anonymous_matrix
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
    if (!r) r = consumeTokenSmart(b, MATH_VARIABLE);
    if (!r) r = consumeTokenSmart(b, NUMBER);
    if (!r) r = consumeTokenSmart(b, STRING);
    if (!r) r = anonymous_list(b, l + 1);
    if (!r) r = anonymous_matrix(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // EXPR_FUNCTIONS_WITH_ARGS LPAREN expr (COMMA expr)* [RPAREN]
  public static boolean func_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_expr")) return false;
    if (!nextTokenIsSmart(b, EXPR_FUNCTIONS_WITH_ARGS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNC_EXPR, null);
    r = consumeTokensSmart(b, 1, EXPR_FUNCTIONS_WITH_ARGS, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1, -1));
    r = p && report_error_(b, func_expr_3(b, l + 1)) && r;
    r = p && func_expr_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA expr)*
  private static boolean func_expr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_expr_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!func_expr_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "func_expr_3", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean func_expr_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_expr_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [RPAREN]
  private static boolean func_expr_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_expr_4")) return false;
    consumeTokenSmart(b, RPAREN);
    return true;
  }

  public static boolean paren_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_expr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && expr(b, l, -1);
    r = p && report_error_(b, paren_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, PAREN_EXPR, r, p, null);
    return r || p;
  }

  // [RPAREN]
  private static boolean paren_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_expr_1")) return false;
    consumeToken(b, RPAREN);
    return true;
  }

}
