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

public class TIBasicThenBlockImpl extends ASTWrapperPsiElement implements TIBasicThenBlock {

  public TIBasicThenBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitThenBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TIBasicVisitor) accept((TIBasicVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TIBasicArgumentsCommand> getArgumentsCommandList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicArgumentsCommand.class);
  }

  @Override
  @NotNull
  public List<TIBasicAssignment> getAssignmentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicAssignment.class);
  }

  @Override
  @NotNull
  public List<TIBasicDelvarCommand> getDelvarCommandList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicDelvarCommand.class);
  }

  @Override
  @NotNull
  public List<TIBasicExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicExpr.class);
  }

  @Override
  @NotNull
  public List<TIBasicFor> getForList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicFor.class);
  }

  @Override
  @NotNull
  public List<TIBasicGoto> getGotoList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicGoto.class);
  }

  @Override
  @NotNull
  public List<TIBasicIf> getIfList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicIf.class);
  }

  @Override
  @NotNull
  public List<TIBasicLbl> getLblList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicLbl.class);
  }

  @Override
  @NotNull
  public List<TIBasicRepeat> getRepeatList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicRepeat.class);
  }

  @Override
  @NotNull
  public List<TIBasicSimpleCommand> getSimpleCommandList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicSimpleCommand.class);
  }

  @Override
  @NotNull
  public List<TIBasicWhile> getWhileList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TIBasicWhile.class);
  }

}
