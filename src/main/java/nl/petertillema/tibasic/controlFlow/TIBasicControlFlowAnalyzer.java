package nl.petertillema.tibasic.controlFlow;

import com.intellij.codeInspection.dataFlow.lang.ir.ConditionalGotoInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.ControlFlow;
import com.intellij.codeInspection.dataFlow.lang.ir.GotoInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.Instruction;
import com.intellij.codeInspection.dataFlow.lang.ir.PopInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.PushInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.PushValueInstruction;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.controlFlow.descriptor.ExpressionFunctionDescriptor;
import nl.petertillema.tibasic.controlFlow.descriptor.SimpleVariableDescriptor;
import nl.petertillema.tibasic.controlFlow.instruction.AssignVariableInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.BooleanBinaryInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.NumericBinaryInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.ReadVariableInstruction;
import nl.petertillema.tibasic.controlFlow.type.BinaryOperator;
import nl.petertillema.tibasic.controlFlow.type.DfDoubleConstantType;
import nl.petertillema.tibasic.psi.TIBasicAssignmentStatement;
import nl.petertillema.tibasic.psi.TIBasicCommandStatement;
import nl.petertillema.tibasic.psi.TIBasicDelvarStatement;
import nl.petertillema.tibasic.psi.TIBasicDivExpr;
import nl.petertillema.tibasic.psi.TIBasicEqExpr;
import nl.petertillema.tibasic.psi.TIBasicExprStatement;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicGeExpr;
import nl.petertillema.tibasic.psi.TIBasicGotoStatement;
import nl.petertillema.tibasic.psi.TIBasicGtExpr;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.TIBasicLeExpr;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import nl.petertillema.tibasic.psi.TIBasicLtExpr;
import nl.petertillema.tibasic.psi.TIBasicMinusExpr;
import nl.petertillema.tibasic.psi.TIBasicMulExpr;
import nl.petertillema.tibasic.psi.TIBasicNeExpr;
import nl.petertillema.tibasic.psi.TIBasicPlusExpr;
import nl.petertillema.tibasic.psi.TIBasicPrgmStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static nl.petertillema.tibasic.controlFlow.descriptor.ExpressionFunctionDescriptor.GETKEY_DOMAIN;
import static nl.petertillema.tibasic.controlFlow.descriptor.ExpressionFunctionDescriptor.RAND_DOMAIN;

public class TIBasicControlFlowAnalyzer extends TIBasicVisitor {

    private final DfaValueFactory factory;
    private final PsiElement psiBlock;
    private final Map<String, Integer> labelMap = new HashMap<>();
    private final Map<String, ControlFlow.DeferredOffset> gotoMap = new HashMap<>();
    private ControlFlow currentFlow;

    public TIBasicControlFlowAnalyzer(@NotNull DfaValueFactory factory, @NotNull PsiElement psiBlock) {
        this.factory = factory;
        this.psiBlock = psiBlock;
    }

    public ControlFlow buildControlFlow() {
        currentFlow = new ControlFlow(factory, psiBlock);
        labelMap.clear();
        gotoMap.clear();
        psiBlock.accept(this);
        updateGotoOffsets();
        currentFlow.finish();
        System.out.println(Arrays.toString(currentFlow.getInstructions()));
        return currentFlow;
    }

    private void updateGotoOffsets() {
        for (var gotoStatement : gotoMap.entrySet()) {
            var gotoLabel = gotoStatement.getKey();
            var foundLabel = labelMap.get(gotoLabel);
            if (foundLabel != null) {
                gotoStatement.getValue().setOffset(foundLabel);
            } else {
                System.out.println("Corresponding label not found!");
            }
        }
    }

    @Override
    public void visitFile(@NotNull PsiFile psiFile) {
        psiFile.acceptChildren(this);
    }

    @Override
    public void visitIfStatement(@NotNull TIBasicIfStatement statement) {
        startElement(statement);

        statement.getExpr().accept(this);

        var thenBlock = statement.getThenBlock();
        var elseBlock = statement.getElseBlock();
        var singleStatement = statement.getStatement();

        // Single statement
        if (thenBlock == null && singleStatement != null) {
            addInstruction(new ConditionalGotoInstruction(getEndOffset(statement), new DfDoubleConstantType(0.0), statement));
            singleStatement.accept(this);
        }

        // Then block
        if (thenBlock != null && elseBlock == null) {
            addInstruction(new ConditionalGotoInstruction(getEndOffset(statement), new DfDoubleConstantType(0.0), statement));

            for (var _statement : thenBlock.getStatementList()) {
                _statement.accept(this);
            }
        }

        // Then-Else block
        if (thenBlock != null && elseBlock != null) {
            addInstruction(new ConditionalGotoInstruction(getStartOffset(elseBlock), new DfDoubleConstantType(0.0), statement));

            for (var _statement : thenBlock.getStatementList()) {
                _statement.accept(this);
            }
            addInstruction(new GotoInstruction(getEndOffset(statement)));

            for (var _statement : elseBlock.getStatementList()) {
                _statement.accept(this);
            }
        }

        finishElement(statement);
    }

    @Override
    public void visitWhileStatement(@NotNull TIBasicWhileStatement statement) {
        startElement(statement);

        statement.getExpr().accept(this);
        addInstruction(new ConditionalGotoInstruction(getEndOffset(statement), new DfDoubleConstantType(0.0), statement));

        for (var _statement : statement.getStatementList()) {
            _statement.accept(this);
        }

        addInstruction(new GotoInstruction(getStartOffset(statement)));

        finishElement(statement);
    }

    @Override
    public void visitRepeatStatement(@NotNull TIBasicRepeatStatement statement) {
        startElement(statement);

        for (var _statement : statement.getStatementList()) {
            _statement.accept(this);
        }

        statement.getExpr().accept(this);
        addInstruction(new ConditionalGotoInstruction(getStartOffset(statement), new DfDoubleConstantType(0.0), statement));

        finishElement(statement);
    }

    @Override
    public void visitForStatement(@NotNull TIBasicForStatement statement) {
        // todo
    }

    @Override
    public void visitDelvarStatement(@NotNull TIBasicDelvarStatement statement) {
        // todo
    }

    @Override
    public void visitGotoStatement(@NotNull TIBasicGotoStatement statement) {
        if (statement.getGotoName() == null) return;
        var offset = new ControlFlow.DeferredOffset();
        gotoMap.put(statement.getGotoName().getText(), offset);
        addInstruction(new GotoInstruction(offset));
    }

    @Override
    public void visitLblStatement(@NotNull TIBasicLblStatement statement) {
        if (statement.getLblName() == null) return;
        labelMap.put(statement.getLblName().getText(), getInstructionCount());
    }

    @Override
    public void visitCommandStatement(@NotNull TIBasicCommandStatement statement) {
        // todo
    }

    @Override
    public void visitPrgmStatement(@NotNull TIBasicPrgmStatement statement) {
        // todo
    }

    @Override
    public void visitExprStatement(@NotNull TIBasicExprStatement statement) {
        startElement(statement);
        if (statement.getExpr() != null) {
            statement.getExpr().accept(this);
            addInstruction(new PopInstruction());
        }
        finishElement(statement);
    }

    @Override
    public void visitAssignmentStatement(@NotNull TIBasicAssignmentStatement statement) {
        startElement(statement);

        statement.getExpr().accept(this);
        if (statement.getAssignmentTarget() != null) {
            var targetPsi = statement.getAssignmentTarget().getFirstChild();
            if (targetPsi != null) {
                var name = targetPsi.getText();
                var descriptor = new SimpleVariableDescriptor(name);
                var variable = factory.getVarFactory().createVariableValue(descriptor);
                addInstruction(new AssignVariableInstruction(new TIBasicDfaAnchor(statement), variable));
//                addInstruction(new SimpleAssignmentInstruction(null, ansVariable));
            }
        }
        addInstruction(new PopInstruction());

        finishElement(statement);
    }

    @Override
    public void visitPlusExpr(@NotNull TIBasicPlusExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.PLUS));
    }

    @Override
    public void visitMinusExpr(@NotNull TIBasicMinusExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.MINUS));
    }

    @Override
    public void visitMulExpr(@NotNull TIBasicMulExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.TIMES));
    }

    @Override
    public void visitDivExpr(@NotNull TIBasicDivExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.DIVIDE));
    }

    @Override
    public void visitEqExpr(@NotNull TIBasicEqExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.EQ));
    }

    @Override
    public void visitNeExpr(@NotNull TIBasicNeExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.NE));
    }

    @Override
    public void visitGtExpr(@NotNull TIBasicGtExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.GT));
    }

    @Override
    public void visitGeExpr(@NotNull TIBasicGeExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.GE));
    }

    @Override
    public void visitLtExpr(@NotNull TIBasicLtExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.LT));
    }

    @Override
    public void visitLeExpr(@NotNull TIBasicLeExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() == 2)
            addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.LE));
    }

    @Override
    public void visitLiteralExpr(@NotNull TIBasicLiteralExpr expr) {
        var child = expr.getFirstChild().getNode().getElementType();
        if (child == TIBasicTypes.NUMBER) {
            var value = new BigDecimal(expr.getText()).doubleValue();
            var dfType = new DfDoubleConstantType(value);
            addInstruction(new PushValueInstruction(dfType, new TIBasicDfaAnchor(expr)));
        } else if (child == TIBasicTypes.SIMPLE_VARIABLE) {
            var name = expr.getText();
            var descriptor = new SimpleVariableDescriptor(name);
            var variable = factory.getVarFactory().createVariableValue(descriptor);
            addInstruction(new ReadVariableInstruction(new TIBasicDfaAnchor(expr), variable));
        } else if (child == TIBasicTypes.EXPR_FUNCTIONS_NO_ARGS) {
            var fname = expr.getText();
            DfType domain = null;
            if ("rand".equals(fname)) {
                domain = RAND_DOMAIN;
            } else if ("getKey".equals(fname)) {
                domain = GETKEY_DOMAIN;
            }
            var funcDescriptor = new ExpressionFunctionDescriptor(fname, domain);
            var funcVar = factory.getVarFactory().createVariableValue(funcDescriptor);
            addInstruction(new PushInstruction(funcVar, new TIBasicDfaAnchor(expr)));
        }
        // todo
    }

    private void addInstruction(Instruction i) {
        currentFlow.addInstruction(i);
    }

    private int getInstructionCount() {
        return currentFlow.getInstructionCount();
    }

    private ControlFlow.ControlFlowOffset getEndOffset(PsiElement element) {
        return currentFlow.getEndOffset(element);
    }

    private ControlFlow.ControlFlowOffset getStartOffset(PsiElement element) {
        return currentFlow.getStartOffset(element);
    }

    private void startElement(@NotNull PsiElement element) {
        currentFlow.startElement(element);
    }

    private void finishElement(@NotNull PsiElement element) {
        currentFlow.finishElement(element);
    }

    private void pushUnknown() {
        addInstruction(new PushValueInstruction(DfType.TOP, null));
    }

}
