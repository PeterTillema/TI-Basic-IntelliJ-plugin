// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TIBasicAssignmentTarget extends TIBasicNamedElement {

  @Nullable
  TIBasicCustomList getCustomList();

  @NotNull
  List<TIBasicExpr> getExprList();

  String getName();

  PsiElement setName(String name);

  PsiElement getNameIdentifier();

}
