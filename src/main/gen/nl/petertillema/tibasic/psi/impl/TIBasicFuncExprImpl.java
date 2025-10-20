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
import com.intellij.psi.PsiReference;

public class TIBasicFuncExprImpl extends TIBasicExprImpl implements TIBasicFuncExpr {

  public TIBasicFuncExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitFuncExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TIBasicVisitor) accept((TIBasicVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TIBasicExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicExpr.class);
  }

  @Override
  public PsiReference[] getReferences() {
    return TIBasicPsiImplUtil.getReferences(this);
  }

}
