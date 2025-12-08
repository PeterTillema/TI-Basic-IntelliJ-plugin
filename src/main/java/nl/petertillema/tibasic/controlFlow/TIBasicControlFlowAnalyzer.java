package nl.petertillema.tibasic.controlFlow;

import com.intellij.codeInspection.dataFlow.lang.ir.ConditionalGotoInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.ControlFlow;
import com.intellij.codeInspection.dataFlow.lang.ir.DupInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.FlushVariableInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.GotoInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.Instruction;
import com.intellij.codeInspection.dataFlow.lang.ir.PopInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.PushInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.PushValueInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.SwapInstruction;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import nl.petertillema.tibasic.controlFlow.descriptor.ListElementDescriptor;
import nl.petertillema.tibasic.controlFlow.descriptor.TIBasicVariableDescriptor;
import nl.petertillema.tibasic.controlFlow.instruction.AssignVariableInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.BooleanBinaryInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.CommandInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.FlushVariablesInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.FunctionInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.GetListElementInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.MultipleGotoInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.NumericBinaryInstruction;
import nl.petertillema.tibasic.controlFlow.instruction.NumericUnaryInstruction;
import nl.petertillema.tibasic.controlFlow.type.BinaryOperator;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import nl.petertillema.tibasic.controlFlow.type.DfMatrixType;
import nl.petertillema.tibasic.controlFlow.type.UnaryOperator;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import nl.petertillema.tibasic.psi.TIBasicAndExpr;
import nl.petertillema.tibasic.psi.TIBasicAnonymousList;
import nl.petertillema.tibasic.psi.TIBasicAnonymousMatrix;
import nl.petertillema.tibasic.psi.TIBasicAnonymousMatrixRow;
import nl.petertillema.tibasic.psi.TIBasicAsmStatement;
import nl.petertillema.tibasic.psi.TIBasicAssignmentStatement;
import nl.petertillema.tibasic.psi.TIBasicAssignmentTarget;
import nl.petertillema.tibasic.psi.TIBasicCommandStatement;
import nl.petertillema.tibasic.psi.TIBasicCustomList;
import nl.petertillema.tibasic.psi.TIBasicDegreeExpr;
import nl.petertillema.tibasic.psi.TIBasicDelvarStatement;
import nl.petertillema.tibasic.psi.TIBasicDispStatement;
import nl.petertillema.tibasic.psi.TIBasicDivExpr;
import nl.petertillema.tibasic.psi.TIBasicElseBlock;
import nl.petertillema.tibasic.psi.TIBasicEqExpr;
import nl.petertillema.tibasic.psi.TIBasicExpr;
import nl.petertillema.tibasic.psi.TIBasicExprStatement;
import nl.petertillema.tibasic.psi.TIBasicFactorialExpr;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicFuncExpr;
import nl.petertillema.tibasic.psi.TIBasicFuncOptionalExpr;
import nl.petertillema.tibasic.psi.TIBasicGeExpr;
import nl.petertillema.tibasic.psi.TIBasicGotoStatement;
import nl.petertillema.tibasic.psi.TIBasicGtExpr;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicImpliedMulExpr;
import nl.petertillema.tibasic.psi.TIBasicInverseExpr;
import nl.petertillema.tibasic.psi.TIBasicIsDsStatement;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.TIBasicLeExpr;
import nl.petertillema.tibasic.psi.TIBasicListIndex;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import nl.petertillema.tibasic.psi.TIBasicLtExpr;
import nl.petertillema.tibasic.psi.TIBasicMatrixIndex;
import nl.petertillema.tibasic.psi.TIBasicMenuOption;
import nl.petertillema.tibasic.psi.TIBasicMenuStatement;
import nl.petertillema.tibasic.psi.TIBasicMinusExpr;
import nl.petertillema.tibasic.psi.TIBasicMulExpr;
import nl.petertillema.tibasic.psi.TIBasicNcrExpr;
import nl.petertillema.tibasic.psi.TIBasicNeExpr;
import nl.petertillema.tibasic.psi.TIBasicNegationExpr;
import nl.petertillema.tibasic.psi.TIBasicNprExpr;
import nl.petertillema.tibasic.psi.TIBasicOrExpr;
import nl.petertillema.tibasic.psi.TIBasicParenExpr;
import nl.petertillema.tibasic.psi.TIBasicPlotStatement;
import nl.petertillema.tibasic.psi.TIBasicPlusExpr;
import nl.petertillema.tibasic.psi.TIBasicPow2Expr;
import nl.petertillema.tibasic.psi.TIBasicPow3Expr;
import nl.petertillema.tibasic.psi.TIBasicPowExpr;
import nl.petertillema.tibasic.psi.TIBasicPrgmStatement;
import nl.petertillema.tibasic.psi.TIBasicRadianExpr;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicStatement;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicTransposeExpr;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import nl.petertillema.tibasic.psi.TIBasicXorExpr;
import nl.petertillema.tibasic.psi.TIBasicXrootExpr;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.petertillema.tibasic.TIBasicPaletteColors.TIBASIC_COLOR_NUMS;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.numToString;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.FULL_RANGE;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;
import static nl.petertillema.tibasic.controlFlow.type.FunctionRangeSetDomain.GETKEY_DOMAIN;
import static nl.petertillema.tibasic.controlFlow.type.FunctionRangeSetDomain.RAND_DOMAIN;

public class TIBasicControlFlowAnalyzer extends TIBasicVisitor {

    private final DfaValueFactory factory;
    private final PsiElement psiBlock;
    private final Map<String, Integer> labelMap = new HashMap<>();
    private final List<Pair<String, ControlFlow.DeferredOffset>> gotoMap = new ArrayList<>();
    private ControlFlow currentFlow;
    private final DfaVariableValue ansVariable;

    public TIBasicControlFlowAnalyzer(@NotNull DfaValueFactory factory, @NotNull PsiElement psiBlock) {
        this.factory = factory;
        this.psiBlock = psiBlock;
        this.ansVariable = factory.getVarFactory().createVariableValue(new TIBasicVariableDescriptor("Ans"));
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
        for (Pair<String, ControlFlow.DeferredOffset> gotoPair : gotoMap) {
            String gotoLabel = gotoPair.getFirst();
            Integer foundLabel = labelMap.get(gotoLabel);
            if (foundLabel != null) {
                gotoPair.getSecond().setOffset(foundLabel);
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

        TIBasicExpr condition = statement.getExpr();
        if (condition != null) {
            statement.getExpr().accept(this);
        } else {
            pushUnknown();
        }

        TIBasicThenBlock thenBlock = statement.getThenBlock();
        TIBasicElseBlock elseBlock = statement.getElseBlock();
        TIBasicStatement singleStatement = statement.getStatement();

        // Single statement
        if (thenBlock == null && singleStatement != null) {
            addInstruction(new ConditionalGotoInstruction(getEndOffset(statement), fromValue(BigDecimal.ZERO), statement));
            singleStatement.accept(this);
        }

        // Then block
        if (thenBlock != null && elseBlock == null) {
            addInstruction(new ConditionalGotoInstruction(getEndOffset(statement), fromValue(BigDecimal.ZERO), statement));
            visitThenBlock(thenBlock);
        }

        // Then-Else block
        if (thenBlock != null && elseBlock != null) {
            addInstruction(new ConditionalGotoInstruction(getStartOffset(elseBlock), fromValue(BigDecimal.ZERO), statement));
            visitThenBlock(thenBlock);
            addInstruction(new GotoInstruction(getEndOffset(statement)));

            visitElseBlock(elseBlock);
        }

        finishElement(statement);
    }

    @Override
    public void visitThenBlock(@NotNull TIBasicThenBlock thenBlock) {
        startElement(thenBlock);

        for (TIBasicStatement statement : thenBlock.getStatementList()) {
            statement.accept(this);
        }

        finishElement(thenBlock);
    }

    @Override
    public void visitElseBlock(@NotNull TIBasicElseBlock elseBlock) {
        startElement(elseBlock);

        for (TIBasicStatement statement : elseBlock.getStatementList()) {
            statement.accept(this);
        }

        finishElement(elseBlock);
    }

    @Override
    public void visitWhileStatement(@NotNull TIBasicWhileStatement statement) {
        startElement(statement);

        TIBasicExpr condition = statement.getExpr();
        if (condition != null) {
            statement.getExpr().accept(this);
        } else {
            pushUnknown();
        }
        addInstruction(new ConditionalGotoInstruction(getEndOffset(statement), fromValue(BigDecimal.ZERO), statement));

        for (TIBasicStatement _statement : statement.getStatementList()) {
            _statement.accept(this);
        }

        addInstruction(new GotoInstruction(getStartOffset(statement)));

        finishElement(statement);
    }

    @Override
    public void visitRepeatStatement(@NotNull TIBasicRepeatStatement statement) {
        startElement(statement);

        for (TIBasicStatement _statement : statement.getStatementList()) {
            _statement.accept(this);
        }

        TIBasicExpr condition = statement.getExpr();
        if (condition != null) {
            statement.getExpr().accept(this);
        } else {
            pushUnknown();
        }
        addInstruction(new ConditionalGotoInstruction(getStartOffset(statement), fromValue(BigDecimal.ZERO), statement));

        finishElement(statement);
    }

    @Override
    public void visitForStatement(@NotNull TIBasicForStatement statement) {
        // todo
    }

    @Override
    public void visitIsDsStatement(@NotNull TIBasicIsDsStatement statement) {
        startElement(statement);

        // If the expression exists, the target variable certainly exists
        if (statement.getExpr() != null) {
            PsiElement is_ds = statement.getFirstChild();
            DfaVariableValue variable = createVariableFromElement(is_ds.getNextSibling().getNextSibling());

            addInstruction(new PushInstruction(variable, null));
            addInstruction(new PushValueInstruction(fromValue(BigDecimal.ONE)));
            addInstruction(new NumericBinaryInstruction(null, is_ds.getNode().getElementType() == TIBasicTypes.IS ? BinaryOperator.PLUS : BinaryOperator.MINUS));
            addInstruction(new PushInstruction(variable, null));
            addInstruction(new AssignVariableInstruction(null));
            statement.getExpr().accept(this);
            addInstruction(new BooleanBinaryInstruction(null, is_ds.getNode().getElementType() == TIBasicTypes.IS ? RelationType.LE : RelationType.GE));
        } else {
            pushUnknown();
        }

        addInstruction(new ConditionalGotoInstruction(getEndOffset(statement), fromValue(BigDecimal.ZERO)));

        if (statement.getStatement() != null) {
            statement.getStatement().accept(this);
        }

        finishElement(statement);
    }

    @Override
    public void visitDelvarStatement(@NotNull TIBasicDelvarStatement statement) {
        PsiElement child = statement.getFirstChild();
        while (child != null) {
            IElementType type = child.getNode().getElementType();
            if (type == TIBasicTypes.SIMPLE_VARIABLE) {
                DfaVariableValue variable = createVariableFromElement(child);
                addInstruction(new FlushVariableInstruction(variable));
            }
            // todo
            child = child.getNextSibling();
        }

        if (statement.getStatement() != null) {
            statement.getStatement().accept(this);
        }
    }

    @Override
    public void visitGotoStatement(@NotNull TIBasicGotoStatement statement) {
        if (statement.getGotoName() == null) return;
        ControlFlow.DeferredOffset offset = new ControlFlow.DeferredOffset();
        gotoMap.add(new Pair<>(statement.getGotoName().getText(), offset));
        addInstruction(new GotoInstruction(offset));
    }

    @Override
    public void visitLblStatement(@NotNull TIBasicLblStatement statement) {
        if (statement.getLblName() == null) return;
        labelMap.put(statement.getLblName().getText(), getInstructionCount());
    }

    @Override
    public void visitCommandStatement(@NotNull TIBasicCommandStatement statement) {
        statement.acceptChildren(this);
        addInstruction(new CommandInstruction(statement.getFirstChild().getText(), statement.getExprList().size()));
    }

    @Override
    public void visitPrgmStatement(@NotNull TIBasicPrgmStatement statement) {
        addInstruction(new FlushVariablesInstruction(v -> true));
    }

    @Override
    public void visitMenuStatement(@NotNull TIBasicMenuStatement statement) {
        ArrayList<ControlFlow.ControlFlowOffset> offsets = new ArrayList<>();

        for (TIBasicMenuOption option : statement.getMenuOptionList()) {
            if (option.getGotoName() == null) continue;
            ControlFlow.DeferredOffset offset = new ControlFlow.DeferredOffset();
            gotoMap.add(new Pair<>(option.getGotoName().getText(), offset));
            offsets.add(offset);
        }
        addInstruction(new MultipleGotoInstruction(offsets));
    }

    @Override
    public void visitPlotStatement(@NotNull TIBasicPlotStatement statement) {
        statement.acceptChildren(this);
    }

    @Override
    public void visitDispStatement(@NotNull TIBasicDispStatement statement) {
        statement.acceptChildren(this);
    }

    @Override
    public void visitAsmStatement(@NotNull TIBasicAsmStatement statement) {
        addInstruction(new FlushVariablesInstruction(v -> true));
    }

    @Override
    public void visitExprStatement(@NotNull TIBasicExprStatement statement) {
        startElement(statement);
        if (statement.getExpr() != null) {
            statement.getExpr().accept(this);
            addInstruction(new PushInstruction(ansVariable, null));
            addInstruction(new AssignVariableInstruction(null));
            addInstruction(new PopInstruction());
        }
        finishElement(statement);
    }

    @Override
    public void visitAssignmentStatement(@NotNull TIBasicAssignmentStatement statement) {
        startElement(statement);

        statement.getExpr().accept(this);
        TIBasicAssignmentTarget target = statement.getAssignmentTarget();
        if (target != null) {
            addInstructionsForLiteral(target, PsiTreeUtil.getChildrenOfTypeAsList(target, TIBasicExpr.class));
            addInstruction(new AssignVariableInstruction(new TIBasicDfaAnchor(target)));
        }
        addInstruction(new PushInstruction(ansVariable, null));
        addInstruction(new AssignVariableInstruction(null));
        addInstruction(new PopInstruction());

        finishElement(statement);
    }

    @Override
    public void visitPlusExpr(@NotNull TIBasicPlusExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.PLUS));
    }

    @Override
    public void visitMinusExpr(@NotNull TIBasicMinusExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.MINUS));
    }

    @Override
    public void visitMulExpr(@NotNull TIBasicMulExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.TIMES));
    }

    @Override
    public void visitDivExpr(@NotNull TIBasicDivExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.DIVIDE));
    }

    @Override
    public void visitImpliedMulExpr(@NotNull TIBasicImpliedMulExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.TIMES));
    }

    @Override
    public void visitEqExpr(@NotNull TIBasicEqExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.EQ));
    }

    @Override
    public void visitNeExpr(@NotNull TIBasicNeExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.NE));
    }

    @Override
    public void visitGtExpr(@NotNull TIBasicGtExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.GT));
    }

    @Override
    public void visitGeExpr(@NotNull TIBasicGeExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.GE));
    }

    @Override
    public void visitLtExpr(@NotNull TIBasicLtExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.LT));
    }

    @Override
    public void visitLeExpr(@NotNull TIBasicLeExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new BooleanBinaryInstruction(new TIBasicDfaAnchor(expr), RelationType.LE));
    }

    @Override
    public void visitNegationExpr(@NotNull TIBasicNegationExpr expr) {
        if (expr.getExpr() != null) {
            expr.getExpr().accept(this);
        } else {
            pushUnknown();
        }
        addInstruction(new NumericUnaryInstruction(new TIBasicDfaAnchor(expr), UnaryOperator.TRANSPOSE));
    }

    @Override
    public void visitPowExpr(@NotNull TIBasicPowExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.POW));
    }

    @Override
    public void visitXrootExpr(@NotNull TIBasicXrootExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        // A xroot B = B^(1/A)
        addInstruction(new SwapInstruction());
        addInstruction(new PushValueInstruction(fromValue(BigDecimal.ONE)));
        addInstruction(new SwapInstruction());
        addInstruction(new NumericBinaryInstruction(null, BinaryOperator.DIVIDE));
        addInstruction(new NumericBinaryInstruction(null, BinaryOperator.POW));
    }

    @Override
    public void visitAndExpr(@NotNull TIBasicAndExpr expr) {
        // todo
    }

    @Override
    public void visitOrExpr(@NotNull TIBasicOrExpr expr) {
        // todo
    }

    @Override
    public void visitXorExpr(@NotNull TIBasicXorExpr expr) {
        // todo
    }

    @Override
    public void visitNprExpr(@NotNull TIBasicNprExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new FunctionInstruction("nPr", expr.getExprList().size(), new TIBasicDfaAnchor(expr)));
    }

    @Override
    public void visitNcrExpr(@NotNull TIBasicNcrExpr expr) {
        expr.acceptChildren(this);
        if (expr.getExprList().size() != 2) pushUnknown();
        addInstruction(new FunctionInstruction("nPr", expr.getExprList().size(), new TIBasicDfaAnchor(expr)));
    }

    @Override
    public void visitRadianExpr(@NotNull TIBasicRadianExpr expr) {
        expr.getExpr().accept(this);
        addInstruction(new NumericUnaryInstruction(new TIBasicDfaAnchor(expr), UnaryOperator.TO_RADIAN));
    }

    @Override
    public void visitDegreeExpr(@NotNull TIBasicDegreeExpr expr) {
        expr.getExpr().accept(this);
        addInstruction(new NumericUnaryInstruction(new TIBasicDfaAnchor(expr), UnaryOperator.TO_DEGREE));
    }

    @Override
    public void visitInverseExpr(@NotNull TIBasicInverseExpr expr) {
        expr.getExpr().accept(this);
        addInstruction(new PushValueInstruction(fromValue(BigDecimal.ONE)));
        addInstruction(new SwapInstruction());
        addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.DIVIDE));
    }

    @Override
    public void visitPow2Expr(@NotNull TIBasicPow2Expr expr) {
        expr.getExpr().accept(this);
        addInstruction(new DupInstruction());
        addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.TIMES));
    }

    @Override
    public void visitTransposeExpr(@NotNull TIBasicTransposeExpr expr) {
        expr.getExpr().accept(this);
        addInstruction(new NumericUnaryInstruction(new TIBasicDfaAnchor(expr), UnaryOperator.TRANSPOSE));
    }

    @Override
    public void visitPow3Expr(@NotNull TIBasicPow3Expr expr) {
        expr.getExpr().accept(this);
        addInstruction(new DupInstruction());
        addInstruction(new DupInstruction());
        addInstruction(new NumericBinaryInstruction(null, BinaryOperator.TIMES));
        addInstruction(new NumericBinaryInstruction(new TIBasicDfaAnchor(expr), BinaryOperator.TIMES));
    }

    @Override
    public void visitFactorialExpr(@NotNull TIBasicFactorialExpr expr) {
        expr.getExpr().accept(this);
        addInstruction(new NumericUnaryInstruction(new TIBasicDfaAnchor(expr), UnaryOperator.FACTORIAL));
    }

    @Override
    public void visitFuncExpr(@NotNull TIBasicFuncExpr expr) {
        expr.acceptChildren(this);
        addInstruction(new FunctionInstruction(expr.getFirstChild().getText(), expr.getExprList().size(), new TIBasicDfaAnchor(expr)));
    }

    @Override
    public void visitFuncOptionalExpr(@NotNull TIBasicFuncOptionalExpr expr) {
        expr.acceptChildren(this);
        addInstruction(new FunctionInstruction(expr.getFirstChild().getText(), expr.getExprList().size(), new TIBasicDfaAnchor(expr)));
    }

    @Override
    public void visitParenExpr(@NotNull TIBasicParenExpr expr) {
        if (expr.getExpr() != null) {
            expr.getExpr().accept(this);
        } else {
            pushUnknown();
        }
    }

    @Override
    public void visitLiteralExpr(@NotNull TIBasicLiteralExpr expr) {
        addInstructionsForLiteral(expr, PsiTreeUtil.getChildrenOfTypeAsList(expr, TIBasicExpr.class));
    }

    private void addInstructionsForLiteral(PsiElement element, List<TIBasicExpr> expressions) {
        PsiElement child = element.getFirstChild();
        IElementType childType = child.getNode().getElementType();

        // List index
        if (child instanceof TIBasicListIndex listIndex) {
            DfaVariableValue var = factory.getVarFactory().createVariableValue(new TIBasicVariableDescriptor(listIndex.getFirstChild().getText()));
            addInstruction(new PushInstruction(var, new TIBasicDfaAnchor(listIndex.getFirstChild())));

            if (listIndex.getExpr() != null) {
                listIndex.getExpr().accept(this);
            } else {
                pushUnknown();
            }
            addInstruction(new GetListElementInstruction(new TIBasicDfaAnchor(child)));
        }

        // List
        else if (child instanceof TIBasicCustomList || childType == TIBasicTypes.LIST_VARIABLE || childType == TIBasicTypes.ANS_VARIABLE) {
            DfaVariableValue var = factory.getVarFactory().createVariableValue(new TIBasicVariableDescriptor(child.getText()));
            addInstruction(new PushInstruction(var, new TIBasicDfaAnchor(child)));
            if (child.getNextSibling() != null && child.getNextSibling().getNode().getElementType() == TIBasicTypes.LPAREN) {
                if (expressions.isEmpty()) {
                    pushUnknown();
                } else {
                    expressions.getFirst().accept(this);
                }
                addInstruction(new GetListElementInstruction(new TIBasicDfaAnchor(child)));
            }
        }

        // Matrix index
        else if (child instanceof TIBasicMatrixIndex matrixIndex) {
            DfaVariableValue var = factory.getVarFactory().createVariableValue(new TIBasicVariableDescriptor(matrixIndex.getFirstChild().getText()));
            addInstruction(new PushInstruction(var, new TIBasicDfaAnchor(matrixIndex.getFirstChild())));

            if (!matrixIndex.getExprList().isEmpty()) {
                matrixIndex.getExprList().getFirst().accept(this);
            } else {
                pushUnknown();
            }
            addInstruction(new GetListElementInstruction(new TIBasicDfaAnchor(child)));

            if (matrixIndex.getExprList().size() > 1) {
                matrixIndex.getExprList().get(1).accept(this);
            } else {
                pushUnknown();
            }
            addInstruction(new GetListElementInstruction(new TIBasicDfaAnchor(child)));
        }

        // Matrix
        else if (childType == TIBasicTypes.MATRIX_VARIABLE) {
            DfaVariableValue var = factory.getVarFactory().createVariableValue(new TIBasicVariableDescriptor(child.getText()));
            addInstruction(new PushInstruction(var, new TIBasicDfaAnchor(child)));

            if (child.getNextSibling() != null && child.getNextSibling().getNode().getElementType() == TIBasicTypes.LPAREN) {
                if (expressions.isEmpty()) {
                    pushUnknown();
                } else {
                    expressions.getFirst().accept(this);
                }
                addInstruction(new GetListElementInstruction(new TIBasicDfaAnchor(child)));

                if (expressions.size() < 2) {
                    pushUnknown();
                } else {
                    expressions.get(1).accept(this);
                }
                addInstruction(new GetListElementInstruction(new TIBasicDfaAnchor(child)));
            }
        }

        // Anonymous list
        else if (child instanceof TIBasicAnonymousList anonymousList) {
            DfaVariableValue tempListVar = createTempVariable();

            addInstruction(new PushValueInstruction(new DfListType()));
            addInstruction(new PushInstruction(tempListVar, null));
            addInstruction(new AssignVariableInstruction(null));
            addInstruction(new PopInstruction());

            int idx = 1;
            for (TIBasicExpr expr : anonymousList.getExprList()) {
                expr.accept(this);
                ListElementDescriptor descriptor = new ListElementDescriptor(idx);
                DfaVariableValue listElemVar = factory.getVarFactory().createVariableValue(descriptor, tempListVar);
                addInstruction(new PushInstruction(listElemVar, new TIBasicDfaAnchor(expr)));
                addInstruction(new AssignVariableInstruction(null));
                addInstruction(new PopInstruction());
                idx++;
            }

            addInstruction(new PushInstruction(tempListVar, new TIBasicDfaAnchor(child)));
        }

        // Anonymous matrix
        else if (child instanceof TIBasicAnonymousMatrix anonymousMatrix) {
            DfaVariableValue tempMatrixVar = createTempVariable();

            addInstruction(new PushValueInstruction(new DfMatrixType()));
            addInstruction(new PushInstruction(tempMatrixVar, null));
            addInstruction(new AssignVariableInstruction(null));
            addInstruction(new PopInstruction());

            int rowNr = 1;
            for (TIBasicAnonymousMatrixRow row : anonymousMatrix.getAnonymousMatrixRowList()) {
                ListElementDescriptor rowDescriptor = new ListElementDescriptor(rowNr);
                DfaVariableValue tempMatrixRowVar = factory.getVarFactory().createVariableValue(rowDescriptor, tempMatrixVar);

                addInstruction(new PushValueInstruction(new DfListType()));
                addInstruction(new PushInstruction(tempMatrixRowVar, null));
                addInstruction(new AssignVariableInstruction(null));
                addInstruction(new PopInstruction());

                int colNr = 1;
                for (TIBasicExpr expr : row.getExprList()) {
                    expr.accept(this);
                    ListElementDescriptor colDescriptor = new ListElementDescriptor(colNr);
                    DfaVariableValue listElemVar = factory.getVarFactory().createVariableValue(colDescriptor, tempMatrixRowVar);
                    addInstruction(new PushInstruction(listElemVar, new TIBasicDfaAnchor(expr)));
                    addInstruction(new AssignVariableInstruction(null));
                    addInstruction(new PopInstruction());
                    colNr++;
                }

                rowNr++;
            }

            addInstruction(new PushInstruction(tempMatrixVar, new TIBasicDfaAnchor(child)));
        }

        // Number, math variables
        else if (childType == TIBasicTypes.NUMBER || childType == TIBasicTypes.MATH_VARIABLE) {
            String text = child.getText();
            String num;
            if (text.equals("pi") || text.equals("π")) num = "3.1415926535898";
            else if (text.equals("[e]") || text.equals("\uD835\uDC52")) num = "2.7182818284590";
            else num = element.getText();

            DfType numberType = fromValue(numToString(num));
            addInstruction(new PushValueInstruction(numberType, new TIBasicDfaAnchor(child)));
        }

        // Expression function
        else if (childType == TIBasicTypes.EXPR_FUNCTIONS_NO_ARGS) {
            String fname = child.getText();
            BigDecimalRangeSet domain;
            if ("rand".equals(fname)) {
                domain = RAND_DOMAIN;
            } else if ("getKey".equals(fname)) {
                domain = GETKEY_DOMAIN;
            } else {
                domain = FULL_RANGE;
            }
            addInstruction(new PushValueInstruction(fromRange(domain), new TIBasicDfaAnchor(child)));
        }

        // Color variable
        else if (childType == TIBasicTypes.COLOR_VARIABLE) {
            DfType colorType = fromValue(BigDecimal.valueOf(TIBASIC_COLOR_NUMS.get(child.getText())));
            addInstruction(new PushValueInstruction(colorType, new TIBasicDfaAnchor(child)));
        }

        // String
        else if (childType == TIBasicTypes.STRING) {
            // todo
            pushUnknown();
        }

        // All the other simple variables: EQUATION_VARIABLE, STRING_VARIABLE, SIMPLE_VARIABLE, WINDOW_VARIABLE
        else {
            DfaVariableValue var = factory.getVarFactory().createVariableValue(new TIBasicVariableDescriptor(child.getText()));
            addInstruction(new PushInstruction(var, new TIBasicDfaAnchor(child)));
        }
    }

    private DfaVariableValue createVariableFromElement(PsiElement element) {
        String name = element.getText();
        return factory.getVarFactory().createVariableValue(new TIBasicVariableDescriptor(name));
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

    private DfaVariableValue createTempVariable() {
        return currentFlow.createTempVariable(DfType.TOP);
    }

    private void startElement(@NotNull PsiElement element) {
        currentFlow.startElement(element);
    }

    private void finishElement(@NotNull PsiElement element) {
        currentFlow.finishElement(element);
        if (element instanceof TIBasicStatement) {
            addInstruction(new FlushVariablesInstruction(v -> v.getDescriptor() instanceof ControlFlow.Synthetic));
        }
    }

    private void pushUnknown() {
        addInstruction(new PushValueInstruction(DfType.TOP, null));
    }

}
