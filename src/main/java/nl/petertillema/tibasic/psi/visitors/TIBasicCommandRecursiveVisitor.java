package nl.petertillema.tibasic.psi.visitors;

import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.psi.TIBasicDelvarCommand;
import nl.petertillema.tibasic.psi.TIBasicElseStatement;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicStatement;
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
    public void visitStatement(@NotNull TIBasicStatement o) {
        o.acceptChildren(this);
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
            visitStatement(statement);
        }
    }

    @Override
    public void visitIfStatement(@NotNull TIBasicIfStatement o) {
        if (o.getStatement() != null) {
            this.visitStatement(o.getStatement());
        }
        if (o.getThenStatement() != null) {
            this.visitThenStatement(o.getThenStatement());
        }
        if (o.getElseStatement() != null) {
            this.visitElseStatement(o.getElseStatement());
        }
    }

    @Override
    public void visitThenStatement(@NotNull TIBasicThenStatement o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitElseStatement(@NotNull TIBasicElseStatement o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitDelvarCommand(@NotNull TIBasicDelvarCommand o) {
        o.acceptChildren(this);
    }
}
