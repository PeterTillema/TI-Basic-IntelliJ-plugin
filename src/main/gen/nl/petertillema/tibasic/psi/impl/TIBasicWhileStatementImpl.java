// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static nl.petertillema.tibasic.psi.TIBasicTypes.*;
import nl.petertillema.tibasic.psi.*;

public class TIBasicWhileStatementImpl extends TIBasicStatementImpl implements TIBasicWhileStatement {

  public TIBasicWhileStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitWhileStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TIBasicVisitor) accept((TIBasicVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TIBasicExpr getExpr() {
    return findNotNullChildByClass(TIBasicExpr.class);
  }

  @Override
  @NotNull
  public List<TIBasicStatement> getStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicStatement.class);
  }

}
