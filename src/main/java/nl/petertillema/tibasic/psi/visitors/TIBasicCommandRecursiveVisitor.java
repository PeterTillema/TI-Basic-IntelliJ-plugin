package nl.petertillema.tibasic.psi.visitors;

import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.psi.TIBasicDelvarStatement;
import nl.petertillema.tibasic.psi.TIBasicElseBlock;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
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
        o.acceptChildren(this);
    }

    @Override
    public void visitWhileStatement(@NotNull TIBasicWhileStatement o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitForStatement(@NotNull TIBasicForStatement o) {
        for (var statement : o.getStatementList()) {
            statement.accept(this);
        }
    }

    @Override
    public void visitIfStatement(@NotNull TIBasicIfStatement o) {
        if (o.getStatement() != null) {
            o.getStatement().accept(this);
        }
        if (o.getThenBlock() != null) {
            this.visitThenBlock(o.getThenBlock());
        }
        if (o.getElseBlock() != null) {
            this.visitElseBlock(o.getElseBlock());
        }
    }

    @Override
    public void visitThenBlock(@NotNull TIBasicThenBlock o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitElseBlock(@NotNull TIBasicElseBlock o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitDelvarStatement(@NotNull TIBasicDelvarStatement o) {
        o.acceptChildren(this);
    }
}
