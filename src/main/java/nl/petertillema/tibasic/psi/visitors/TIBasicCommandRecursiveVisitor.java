package nl.petertillema.tibasic.psi.visitors;

import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.psi.TIBasicDelvarCommand;
import nl.petertillema.tibasic.psi.TIBasicElseStatement;
import nl.petertillema.tibasic.psi.TIBasicEndBlock;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicThen;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicThenStatement;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import org.jetbrains.annotations.NotNull;

public class TIBasicCommandRecursiveVisitor extends TIBasicVisitor {

    @Override
    public void visitFile(@NotNull PsiFile file) {
        file.acceptChildren(this);
    }

    @Override
    public void visitRepeatStatement(@NotNull TIBasicRepeatStatement o) {
        this.visitEndBlock(o.getEndBlock());
    }

    @Override
    public void visitWhileStatement(@NotNull TIBasicWhileStatement o) {
        this.visitEndBlock(o.getEndBlock());
    }

    @Override
    public void visitForStatement(@NotNull TIBasicForStatement o) {
        this.visitEndBlock(o.getEndBlock());
    }

    @Override
    public void visitIfStatement(@NotNull TIBasicIfStatement o) {
        if (o.getStatement() != null) {
            this.visitStatement(o.getStatement());
        }
        if (o.getThenStatement() != null) {
            this.visitThenStatement(o.getThenStatement());
        }
    }

    @Override
    public void visitThenStatement(@NotNull TIBasicThenStatement o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitThenBlock(@NotNull TIBasicThenBlock o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitElseStatement(@NotNull TIBasicElseStatement o) {
        this.visitEndBlock(o.getEndBlock());
    }

    @Override
    public void visitEndBlock(@NotNull TIBasicEndBlock o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitDelvarCommand(@NotNull TIBasicDelvarCommand o) {
        o.acceptChildren(this);
    }
}
