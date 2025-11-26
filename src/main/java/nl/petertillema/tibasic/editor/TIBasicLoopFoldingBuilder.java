package nl.petertillema.tibasic.editor;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import nl.petertillema.tibasic.psi.visitors.TIBasicCommandRecursiveVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TIBasicLoopFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        root.accept(new TIBasicCommandRecursiveVisitor() {

            @Override
            public void visitIfStatement(@NotNull TIBasicIfStatement o) {
                super.visitIfStatement(o);

                if (o.getThenBlock() != null) {
                    if (o.getElseBlock() != null) {
                        // Add for "If" + "Then"
                        int ifStartOffset = o.getTextRange().getStartOffset();
                        int ifThenLength = o.getThenBlock().getTextRangeInParent().getEndOffset() - 1;
                        String ifText = o.getText().substring(0, o.getExpr().getTextRangeInParent().getEndOffset());

                        FoldingDescriptor descriptor = new FoldingDescriptor(
                                o.getNode(),
                                TextRange.from(ifStartOffset, ifThenLength),
                                null,
                                Collections.emptySet(),
                                false,
                                ifText + "...",
                                Boolean.FALSE);
                        descriptors.add(descriptor);

                        // Add for "Else"
                        this.addLoopDescriptor(o.getElseBlock(), 4);
                    } else {
                        // Only add for "If" + "Then"
                        this.addLoopDescriptor(o, o.getExpr().getTextRangeInParent().getEndOffset());
                    }
                }
            }

            @Override
            public void visitRepeatStatement(@NotNull TIBasicRepeatStatement o) {
                super.visitRepeatStatement(o);
                this.addLoopDescriptor(o, o.getExpr() == null ? 7 : o.getExpr().getTextRangeInParent().getEndOffset());
            }

            @Override
            public void visitWhileStatement(@NotNull TIBasicWhileStatement o) {
                super.visitWhileStatement(o);
                this.addLoopDescriptor(o, o.getExpr() == null ? 6 : o.getExpr().getTextRangeInParent().getEndOffset());
            }

            @Override
            public void visitForStatement(@NotNull TIBasicForStatement o) {
                super.visitForStatement(o);
                this.addLoopDescriptor(o, o.getForInitializer().getTextRangeInParent().getEndOffset());
            }

            private void addLoopDescriptor(PsiElement o, int textOffsetToCollapse) {
                int absoluteStartOffset = o.getTextRange().getStartOffset();
                int totalLength = o.getTextLength();
                String textToDisplay = o.getText().substring(0, textOffsetToCollapse);

                FoldingDescriptor descriptor = new FoldingDescriptor(
                        o.getNode(),
                        TextRange.from(absoluteStartOffset, totalLength),
                        null,
                        Collections.emptySet(),
                        false,
                        textToDisplay + "...",
                        Boolean.FALSE);
                descriptors.add(descriptor);
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
