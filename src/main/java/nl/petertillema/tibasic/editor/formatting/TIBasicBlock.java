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
import com.intellij.psi.formatter.FormatterUtil;
import nl.petertillema.tibasic.psi.TIBasicStatement;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

import static nl.petertillema.tibasic.syntax.TIBasicTokenSets.LOOPS;

public final class TIBasicBlock implements ASTBlock {

    private final ASTNode node;
    private final Indent indent;
    private final SpacingBuilder spacingBuilder;
    private List<Block> subBlocks = null;

    public TIBasicBlock(@NotNull ASTNode node, @NotNull Indent indent, SpacingBuilder spacingBuilder) {
        this.node = node;
        this.indent = indent;
        this.spacingBuilder = spacingBuilder;
    }

    @Override
    public @NotNull ASTNode getNode() {
        return node;
    }

    @Override
    public @NotNull TextRange getTextRange() {
        return node.getTextRange();
    }

    @Override
    public @NotNull @Unmodifiable List<Block> getSubBlocks() {
        if (subBlocks == null) {
            var children = node.getChildren(null);
            subBlocks = new ArrayList<>(children.length);
            for (var child : children) {
                if (isWhitespaceOrEmpty(child)) continue;
                subBlocks.add(makeSubBlock(child));
            }
        }
        return subBlocks;
    }

    private Block makeSubBlock(ASTNode childNode) {
        var childIndent = Indent.getNoneIndent();

        // The second check is necessary to only match children which belong to the actual statements in the loop,
        // and not the normal nodes. For example, "WHILE" is a child node from the "WHILE_STATEMENT", but should
        // logically not be indented. A JsonBlock works kinda the same but only needs to check against the opening
        // and closing braces/brackets.
        if (LOOPS.contains(node.getElementType()) && childNode.getPsi() instanceof TIBasicStatement) {
            childIndent = Indent.getNormalIndent();
        }
        return new TIBasicBlock(childNode, childIndent, spacingBuilder);
    }

    @Override
    public @Nullable Wrap getWrap() {
        return null;
    }

    @Override
    public @NotNull Indent getIndent() {
        return indent;
    }

    @Override
    public @Nullable Alignment getAlignment() {
        return null;
    }

    @Override
    public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
        if (LOOPS.contains(node.getElementType())) {
            return new ChildAttributes(Indent.getNormalIndent(), null);
        }
        return new ChildAttributes(Indent.getNoneIndent(), null);
    }

    @Override
    public boolean isIncomplete() {
        if (node.getElementType() == TIBasicTypes.THEN_STATEMENT) {
            var lastChildElementType = node.getLastChildNode().getElementType();
            return lastChildElementType != TIBasicTypes.ELSE && lastChildElementType != TIBasicTypes.END;
        }

        if (LOOPS.contains(node.getElementType())) {
            return node.getLastChildNode().getElementType() != TIBasicTypes.END;
        }

        return FormatterUtil.isIncomplete(node);
    }

    @Override
    public boolean isLeaf() {
        return node.getFirstChildNode() == null;
    }

    private static boolean isWhitespaceOrEmpty(ASTNode node) {
        return node.getElementType() == TokenType.WHITE_SPACE || node.getElementType() == TIBasicTypes.CRLF || node.getTextLength() == 0;
    }
}
