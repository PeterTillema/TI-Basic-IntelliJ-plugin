// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TIBasicEndBlock extends PsiElement {

  @NotNull
  List<TIBasicArgumentsCommand> getArgumentsCommandList();

  @NotNull
  List<TIBasicAssignment> getAssignmentList();

  @NotNull
  List<TIBasicDelvarCommand> getDelvarCommandList();

  @NotNull
  List<TIBasicExpr> getExprList();

  @NotNull
  List<TIBasicFor> getForList();

  @NotNull
  List<TIBasicGoto> getGotoList();

  @NotNull
  List<TIBasicIf> getIfList();

  @NotNull
  List<TIBasicLbl> getLblList();

  @NotNull
  List<TIBasicRepeat> getRepeatList();

  @NotNull
  List<TIBasicSimpleCommand> getSimpleCommandList();

  @NotNull
  List<TIBasicWhile> getWhileList();

}
