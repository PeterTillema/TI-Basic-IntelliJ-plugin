package nl.petertillema.tibasic.psi.visitors;

import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.psi.TIBasicEndBlock;
import nl.petertillema.tibasic.psi.TIBasicFor;
import nl.petertillema.tibasic.psi.TIBasicIf;
import nl.petertillema.tibasic.psi.TIBasicRepeat;
import nl.petertillema.tibasic.psi.TIBasicThen;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.TIBasicWhile;
import org.jetbrains.annotations.NotNull;

public class TIBasicCommandRecursiveVisitor extends TIBasicVisitor {

    @Override
    public void visitFile(@NotNull PsiFile file) {
        file.acceptChildren(this);
    }

    @Override
    public void visitRepeat(@NotNull TIBasicRepeat o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitWhile(@NotNull TIBasicWhile o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitFor(@NotNull TIBasicFor o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitIf(@NotNull TIBasicIf o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitThen(@NotNull TIBasicThen o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitEndBlock(@NotNull TIBasicEndBlock o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitThenBlock(@NotNull TIBasicThenBlock o) {
        o.acceptChildren(this);
    }
}
