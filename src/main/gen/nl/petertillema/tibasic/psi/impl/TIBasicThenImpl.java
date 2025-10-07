// This is a generated file. Not intended for manual editing.
package nl.petertillema.tibasic.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static nl.petertillema.tibasic.psi.TIBasicTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import nl.petertillema.tibasic.psi.*;

public class TIBasicThenImpl extends ASTWrapperPsiElement implements TIBasicThen {

  public TIBasicThenImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitThen(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TIBasicVisitor) accept((TIBasicVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TIBasicElse getElse() {
    return findChildByClass(TIBasicElse.class);
  }

  @Override
  @NotNull
  public TIBasicThenBlock getThenBlock() {
    return findNotNullChildByClass(TIBasicThenBlock.class);
  }

}
