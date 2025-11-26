// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface TIBasicWhileStatement extends TIBasicStatement {

  @Nullable
  TIBasicExpr getExpr();

  @NotNull
  List<TIBasicStatement> getStatementList();

  ItemPresentation getPresentation();

}
