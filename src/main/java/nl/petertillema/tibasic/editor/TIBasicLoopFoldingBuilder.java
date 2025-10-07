package nl.petertillema.tibasic.editor;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.psi.TIBasicEndBlock;
import nl.petertillema.tibasic.psi.TIBasicExpr;
import nl.petertillema.tibasic.psi.TIBasicFor;
import nl.petertillema.tibasic.psi.TIBasicIf;
import nl.petertillema.tibasic.psi.TIBasicRepeat;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.TIBasicWhile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class TIBasicLoopFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        var descriptors = new ArrayList<FoldingDescriptor>();

        root.accept(new TIBasicVisitor() {
            @Override
            public void visitFile(@NotNull PsiFile file) {
                file.acceptChildren(this);
            }

            @Override
            public void visitIf(@NotNull TIBasicIf o) {
                o.acceptChildren(this);

                if (o.getThen() != null) {
                    o.getThen().acceptChildren(this);

                    if (o.getThen().getElse() != null) {
                        o.getThen().getElse().acceptChildren(this);

                        // Add for "If" + "Then"
                        var ifStartOffset = o.getTextRange().getStartOffset();
                        var ifThenLength = o.getThen().getElse().getTextRange().getStartOffset() - o.getTextRange().getStartOffset() - 1;
                        var ifText = o.getText().substring(0, o.getThen().getTextRangeInParent().getStartOffset());

                        var descriptor = new FoldingDescriptor(
                                o.getNode(),
                                TextRange.from(ifStartOffset, ifThenLength),
                                null,
                                Collections.emptySet(),
                                false,
                                ifText + "...",
                                false);
                        descriptors.add(descriptor);

                        // Add for "Else"
                        this.addLoopDescriptor(o.getThen().getElse(), 4);
                    } else {
                        this.addLoopDescriptor(o, o.getExpr().getTextRangeInParent().getEndOffset());
                    }
                }
            }

            @Override
            public void visitRepeat(@NotNull TIBasicRepeat o) {
                o.acceptChildren(this);
                this.addLoopDescriptor(o, o.getExpr().getTextRangeInParent().getEndOffset());
            }

            @Override
            public void visitWhile(@NotNull TIBasicWhile o) {
                o.acceptChildren(this);
                this.addLoopDescriptor(o, o.getExpr().getTextRangeInParent().getEndOffset());
            }

            @Override
            public void visitFor(@NotNull TIBasicFor o) {
                o.acceptChildren(this);
                this.addLoopDescriptor(o, o.getExprList().getLast().getTextRangeInParent().getEndOffset());
            }

            private void addLoopDescriptor(PsiElement o, int textOffsetToCollapse) {
                var absoluteStartOffset = o.getTextRange().getStartOffset();
                var totalLength = o.getTextLength();
                var textToDisplay = o.getText().substring(0, textOffsetToCollapse);

                var descriptor = new FoldingDescriptor(
                        o.getNode(),
                        TextRange.from(absoluteStartOffset, totalLength),
                        null,
                        Collections.emptySet(),
                        false,
                        textToDisplay + "...",
                        false);
                descriptors.add(descriptor);
            }

            @Override
            public void visitThenBlock(@NotNull TIBasicThenBlock o) {
                o.acceptChildren(this);
            }

            @Override
            public void visitEndBlock(@NotNull TIBasicEndBlock o) {
                o.acceptChildren(this);
            }
        });

        return descriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
    }

    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode node) {
        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return true;
    }
}
