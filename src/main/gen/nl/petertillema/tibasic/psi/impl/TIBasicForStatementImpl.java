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
import com.intellij.navigation.ItemPresentation;

public class TIBasicForStatementImpl extends TIBasicStatementImpl implements TIBasicForStatement {

  public TIBasicForStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitForStatement(this);
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
  @Nullable
  public TIBasicForIdentifier getForIdentifier() {
    return findChildByClass(TIBasicForIdentifier.class);
  }

  @Override
  @NotNull
  public List<TIBasicStatement> getStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicStatement.class);
  }

  @Override
  public ItemPresentation getPresentation() {
    return TIBasicPsiImplUtil.getPresentation(this);
  }

}
