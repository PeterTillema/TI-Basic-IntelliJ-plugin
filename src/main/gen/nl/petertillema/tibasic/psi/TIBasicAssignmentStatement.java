// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TIBasicAssignmentStatement extends TIBasicStatement {

  @Nullable
  TIBasicAssignmentTarget getAssignmentTarget();

  @NotNull
  TIBasicExpr getExpr();

}
