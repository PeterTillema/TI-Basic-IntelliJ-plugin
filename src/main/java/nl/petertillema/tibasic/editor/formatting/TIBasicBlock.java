package nl.petertillema.tibasic.editor.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import nl.petertillema.tibasic.psi.TIBasicEndBlock;
import nl.petertillema.tibasic.psi.TIBasicFor;
import nl.petertillema.tibasic.psi.TIBasicRepeat;
import nl.petertillema.tibasic.psi.TIBasicThen;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicWhile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TIBasicBlock extends AbstractBlock {

    protected TIBasicBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment) {
        super(node, wrap, alignment);
    }

    @Override
    protected List<Block> buildChildren() {
        var blocks = new ArrayList<Block>();
        ASTNode child = myNode.getFirstChildNode();
        while (child != null) {
            if (child.getElementType() != TokenType.WHITE_SPACE && child.getElementType() != TIBasicTypes.CRLF) {
                Block block = new TIBasicBlock(child, Wrap.createWrap(WrapType.NONE, false), null);
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    @Override
    public @Nullable Indent getIndent() {
        var e = myNode.getPsi();
        if (e.getParent() != null && (e.getParent() instanceof TIBasicEndBlock || e.getParent() instanceof TIBasicThenBlock)) {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }

    @Override
    protected @Nullable Indent getChildIndent() {
        var e = myNode.getPsi();
        if (e instanceof TIBasicThen ||
                e instanceof TIBasicThenBlock ||
                e instanceof TIBasicFor ||
                e instanceof TIBasicWhile ||
                e instanceof TIBasicRepeat ||
                e instanceof TIBasicEndBlock) {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }

    @Override
    public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return null;
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
