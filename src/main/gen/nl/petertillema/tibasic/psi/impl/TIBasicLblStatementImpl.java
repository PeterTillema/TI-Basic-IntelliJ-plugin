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

public class TIBasicLblStatementImpl extends TIBasicNamedElementImpl implements TIBasicLblStatement {

  public TIBasicLblStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitLblStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TIBasicVisitor) accept((TIBasicVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TIBasicLblName getLblName() {
    return findNotNullChildByClass(TIBasicLblName.class);
  }

  @Override
  public String getName() {
    return TIBasicPsiImplUtil.getName(this);
  }

  @Override
  public PsiElement setName(String name) {
    return TIBasicPsiImplUtil.setName(this, name);
  }

  @Override
  public PsiElement getNameIdentifier() {
    return TIBasicPsiImplUtil.getNameIdentifier(this);
  }

}
