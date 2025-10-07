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

public class TIBasicLiteralExprImpl extends TIBasicExprImpl implements TIBasicLiteralExpr {

  public TIBasicLiteralExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitLiteralExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TIBasicVisitor) accept((TIBasicVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TIBasicAnonymousList getAnonymousList() {
    return findChildByClass(TIBasicAnonymousList.class);
  }

  @Override
  @Nullable
  public TIBasicAnonymousMatrix getAnonymousMatrix() {
    return findChildByClass(TIBasicAnonymousMatrix.class);
  }

  @Override
  @Nullable
  public TIBasicListIndex getListIndex() {
    return findChildByClass(TIBasicListIndex.class);
  }

  @Override
  @Nullable
  public TIBasicMatrixIndex getMatrixIndex() {
    return findChildByClass(TIBasicMatrixIndex.class);
  }

}
