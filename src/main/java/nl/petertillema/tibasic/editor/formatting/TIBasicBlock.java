package nl.petertillema.tibasic.editor.formatting;

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.TokenType;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicElse;
import nl.petertillema.tibasic.psi.TIBasicEndBlock;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicThen;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TIBasicBlock implements ASTBlock {

    private final TIBasicBlock parent;
    private final ASTNode node;
    private final Indent indent;
    private List<TIBasicBlock> subBlocks = null;

    public TIBasicBlock(@Nullable TIBasicBlock parent, @NotNull ASTNode node, @NotNull Indent indent) {
        this.parent = parent;
        this.node = node;
        this.indent = indent;
    }

    @Override
    public @Nullable ASTNode getNode() {
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

                var newBlock = new TIBasicBlock(this, child, childIndent);
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
    public @Nullable Indent getIndent() {
        return this.indent;
    }

    @Override
    public @Nullable Alignment getAlignment() {
        return null;
    }

    @Override
    public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return null;
    }

    @Override
    public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
        var psi = this.node.getPsi();
        if (newChildIndex > 0 && psi instanceof TIBasicFile) {
            return ChildAttributes.DELEGATE_TO_PREV_CHILD;
        }

        var childIndent = Indent.getNoneIndent();

        if (psi instanceof TIBasicForStatement ||
                psi instanceof TIBasicThen ||
                psi instanceof TIBasicElse ||
                psi instanceof TIBasicWhileStatement ||
                psi instanceof TIBasicRepeatStatement ||
                psi instanceof TIBasicEndBlock) {
            childIndent = Indent.getNormalIndent();
        }
        return new ChildAttributes(childIndent, null);
    }

    @Override
    public boolean isIncomplete() {
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
