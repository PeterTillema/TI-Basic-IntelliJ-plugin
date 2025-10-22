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

public class TIBasicStatementImpl extends ASTWrapperPsiElement implements TIBasicStatement {

  public TIBasicStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TIBasicVisitor) accept((TIBasicVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TIBasicAssignmentStatement getAssignmentStatement() {
    return findChildByClass(TIBasicAssignmentStatement.class);
  }

  @Override
  @Nullable
  public TIBasicCommandStatement getCommandStatement() {
    return findChildByClass(TIBasicCommandStatement.class);
  }

  @Override
  @Nullable
  public TIBasicDelvarCommand getDelvarCommand() {
    return findChildByClass(TIBasicDelvarCommand.class);
  }

  @Override
  @Nullable
  public TIBasicExprStatement getExprStatement() {
    return findChildByClass(TIBasicExprStatement.class);
  }

  @Override
  @Nullable
  public TIBasicForStatement getForStatement() {
    return findChildByClass(TIBasicForStatement.class);
  }

  @Override
  @Nullable
  public TIBasicGotoStatement getGotoStatement() {
    return findChildByClass(TIBasicGotoStatement.class);
  }

  @Override
  @Nullable
  public TIBasicIfStatement getIfStatement() {
    return findChildByClass(TIBasicIfStatement.class);
  }

  @Override
  @Nullable
  public TIBasicLblStatement getLblStatement() {
    return findChildByClass(TIBasicLblStatement.class);
  }

  @Override
  @Nullable
  public TIBasicRepeatStatement getRepeatStatement() {
    return findChildByClass(TIBasicRepeatStatement.class);
  }

  @Override
  @Nullable
  public TIBasicWhileStatement getWhileStatement() {
    return findChildByClass(TIBasicWhileStatement.class);
  }

}
