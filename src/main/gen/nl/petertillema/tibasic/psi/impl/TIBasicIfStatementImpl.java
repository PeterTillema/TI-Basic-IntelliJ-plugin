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

public class TIBasicIfStatementImpl extends ASTWrapperPsiElement implements TIBasicIfStatement {

  public TIBasicIfStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitIfStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TIBasicVisitor) accept((TIBasicVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TIBasicElseStatement getElseStatement() {
    return findChildByClass(TIBasicElseStatement.class);
  }

  @Override
  @NotNull
  public TIBasicExpr getExpr() {
    return findNotNullChildByClass(TIBasicExpr.class);
  }

  @Override
  @Nullable
  public TIBasicStatement getStatement() {
    return findChildByClass(TIBasicStatement.class);
  }

  @Override
  @Nullable
  public TIBasicThenStatement getThenStatement() {
    return findChildByClass(TIBasicThenStatement.class);
  }

}
