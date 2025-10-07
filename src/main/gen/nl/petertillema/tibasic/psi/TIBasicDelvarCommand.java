// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TIBasicDelvarCommand extends PsiElement {

  @Nullable
  TIBasicArgumentsCommand getArgumentsCommand();

  @Nullable
  TIBasicAssignment getAssignment();

  @Nullable
  TIBasicDelvarCommand getDelvarCommand();

  @Nullable
  TIBasicExpr getExpr();

  @Nullable
  TIBasicFor getFor();

  @Nullable
  TIBasicGoto getGoto();

  @Nullable
  TIBasicIf getIf();

  @Nullable
  TIBasicLbl getLbl();

  @Nullable
  TIBasicRepeat getRepeat();

  @Nullable
  TIBasicSimpleCommand getSimpleCommand();

  @Nullable
  TIBasicWhile getWhile();

}
