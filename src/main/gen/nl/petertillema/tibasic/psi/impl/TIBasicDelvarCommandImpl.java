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

public class TIBasicDelvarCommandImpl extends ASTWrapperPsiElement implements TIBasicDelvarCommand {

  public TIBasicDelvarCommandImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TIBasicVisitor visitor) {
    visitor.visitDelvarCommand(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TIBasicVisitor) accept((TIBasicVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TIBasicArgumentsCommand getArgumentsCommand() {
    return findChildByClass(TIBasicArgumentsCommand.class);
  }

  @Override
  @Nullable
  public TIBasicAssignment getAssignment() {
    return findChildByClass(TIBasicAssignment.class);
  }

  @Override
  @Nullable
  public TIBasicDelvarCommand getDelvarCommand() {
    return findChildByClass(TIBasicDelvarCommand.class);
  }

  @Override
  @Nullable
  public TIBasicExpr getExpr() {
    return findChildByClass(TIBasicExpr.class);
  }

  @Override
  @Nullable
  public TIBasicFor getFor() {
    return findChildByClass(TIBasicFor.class);
  }

  @Override
  @Nullable
  public TIBasicGoto getGoto() {
    return findChildByClass(TIBasicGoto.class);
  }

  @Override
  @Nullable
  public TIBasicIf getIf() {
    return findChildByClass(TIBasicIf.class);
  }

  @Override
  @Nullable
  public TIBasicLbl getLbl() {
    return findChildByClass(TIBasicLbl.class);
  }

  @Override
  @Nullable
  public TIBasicRepeat getRepeat() {
    return findChildByClass(TIBasicRepeat.class);
  }

  @Override
  @Nullable
  public TIBasicSimpleCommand getSimpleCommand() {
    return findChildByClass(TIBasicSimpleCommand.class);
  }

  @Override
  @Nullable
  public TIBasicWhile getWhile() {
    return findChildByClass(TIBasicWhile.class);
  }

}
