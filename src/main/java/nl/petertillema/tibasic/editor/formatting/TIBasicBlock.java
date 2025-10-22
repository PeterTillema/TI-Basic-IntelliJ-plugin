package nl.petertillema.tibasic.editor.formatting;

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.TokenType;
import nl.petertillema.tibasic.psi.TIBasicElseStatement;
import nl.petertillema.tibasic.psi.TIBasicEndBlock;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicThenStatement;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TIBasicBlock implements ASTBlock {

    private final ASTNode node;
    private final Indent indent;
    private final SpacingBuilder spacingBuilder;
    private List<TIBasicBlock> subBlocks = null;

    public TIBasicBlock(@NotNull ASTNode node, @NotNull Indent indent, SpacingBuilder spacingBuilder) {
        this.node = node;
        this.indent = indent;
        this.spacingBuilder = spacingBuilder;
    }

    @Override
    public @NotNull ASTNode getNode() {
        return this.node;
    }

    @Override
    public @NotNull TextRange getTextRange() {
        return this.node.getTextRange();
    }

    @Override
    public @NotNull @Unmodifiable List<Block> getSubBlocks() {
        if (this.subBlocks == null) {
            this.subBlocks = this.buildSubBlocks();
        }

        return Collections.unmodifiableList(this.subBlocks);
    }

    private List<TIBasicBlock> buildSubBlocks() {
        var childBlocks = new ArrayList<TIBasicBlock>();
        var children = this.node.getChildren(null);
        var psi = this.node.getPsi();

        for (var child : children) {
            if (child.getElementType() != TokenType.WHITE_SPACE && child.getElementType() != TIBasicTypes.CRLF) {
                var childIndent = Indent.getNoneIndent();
                if (psi instanceof TIBasicThenBlock || psi instanceof TIBasicEndBlock) {
                    childIndent = Indent.getNormalIndent();
                }

                var newBlock = new TIBasicBlock(child, childIndent, this.spacingBuilder);
                childBlocks.add(newBlock);
            }
        }

        return childBlocks;
    }

    @Override
    public @Nullable Wrap getWrap() {
        return null;
    }

    @Override
    public @NotNull Indent getIndent() {
        return this.indent;
    }

    @Override
    public @Nullable Alignment getAlignment() {
        return null;
    }

    @Override
    public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return this.spacingBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
        var psi = this.node.getPsi();
        var childIndent = Indent.getNoneIndent();

        if (psi instanceof TIBasicForStatement ||
                psi instanceof TIBasicWhileStatement ||
                psi instanceof TIBasicRepeatStatement ||
                psi instanceof TIBasicThenStatement ||
                psi instanceof TIBasicElseStatement) {
            childIndent = Indent.getNormalIndent();
        }
        return new ChildAttributes(childIndent, null);
    }

    @Override
    public boolean isIncomplete() {
        var psi = this.node.getPsi();
        if (psi instanceof TIBasicWhileStatement ||
                psi instanceof TIBasicRepeatStatement ||
                psi instanceof TIBasicForStatement ||
                psi instanceof TIBasicElseStatement) {
            return !psi.getText().endsWith("End");
        }
        if (psi instanceof TIBasicThenStatement) {
            return !(psi.getText().endsWith("End") || psi.getText().endsWith("Else"));
        }

        return this.isIncomplete(this.node);
    }

    private boolean isIncomplete(ASTNode node) {
        ASTNode lastChild = node == null ? null : node.getLastChildNode();
        while (lastChild != null && (lastChild.getElementType() == TIBasicTypes.CRLF || lastChild.getElementType() == TokenType.WHITE_SPACE)) {
            lastChild = lastChild.getTreePrev();
        }
        if (lastChild == null) return false;
        if (lastChild.getElementType() == TokenType.ERROR_ELEMENT) return true;
        return isIncomplete(lastChild);
    }

    @Override
    public boolean isLeaf() {
        return this.node.getFirstChildNode() == null;
    }
}
