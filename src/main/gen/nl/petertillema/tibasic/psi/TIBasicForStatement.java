// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface TIBasicForStatement extends TIBasicStatement {

  @NotNull
  List<TIBasicExpr> getExprList();

  @Nullable
  TIBasicForIdentifier getForIdentifier();

  @NotNull
  List<TIBasicStatement> getStatementList();

  ItemPresentation getPresentation();

}
