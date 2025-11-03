// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TIBasicStatement extends PsiElement {

  @Nullable
  TIBasicAssignmentStatement getAssignmentStatement();

  @Nullable
  TIBasicCommandStatement getCommandStatement();

  @Nullable
  TIBasicDelvarCommand getDelvarCommand();

  @Nullable
  TIBasicExprStatement getExprStatement();

  @Nullable
  TIBasicForStatement getForStatement();

  @Nullable
  TIBasicGotoStatement getGotoStatement();

  @Nullable
  TIBasicIfStatement getIfStatement();

  @Nullable
  TIBasicLblStatement getLblStatement();

  @Nullable
  TIBasicPrgmStatement getPrgmStatement();

  @Nullable
  TIBasicRepeatStatement getRepeatStatement();

  @Nullable
  TIBasicWhileStatement getWhileStatement();

}
