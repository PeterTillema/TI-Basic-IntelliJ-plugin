package nl.petertillema.tibasic.structure;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import nl.petertillema.tibasic.psi.TIBasicElseBlock;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import nl.petertillema.tibasic.psi.impl.TIBasicElseBlockImpl;
import nl.petertillema.tibasic.psi.impl.TIBasicForStatementImpl;
import nl.petertillema.tibasic.psi.impl.TIBasicIfStatementImpl;
import nl.petertillema.tibasic.psi.impl.TIBasicRepeatStatementImpl;
import nl.petertillema.tibasic.psi.impl.TIBasicThenBlockImpl;
import nl.petertillema.tibasic.psi.impl.TIBasicWhileStatementImpl;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfAnyType;

public record TIBasicStructureViewElement(
        NavigatablePsiElement element) implements StructureViewTreeElement, SortableTreeElement {

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {
        element.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return element.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element.canNavigateToSource();
    }

    @Override
    public @NotNull String getAlphaSortKey() {
        var name = element.getName();
        return name != null ? name : "";
    }

    @Override
    public @NotNull ItemPresentation getPresentation() {
        var presentation = element.getPresentation();
        return presentation != null ? presentation : new PresentationData();
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        var elementToCheck = element;
        if (element instanceof TIBasicIfStatement statement && statement.getElseBlock() == null) {
            elementToCheck = (TIBasicThenBlockImpl) statement.getThenBlock();
        }
        var loopChildren = getChildrenOfAnyType(elementToCheck,
                TIBasicWhileStatement.class,
                TIBasicRepeatStatement.class,
                TIBasicForStatement.class,
                TIBasicIfStatement.class,
                TIBasicThenBlock.class,
                TIBasicElseBlock.class);

        return loopChildren.stream()
                .map(child -> switch (child) {
                    case TIBasicWhileStatement statement -> (TIBasicWhileStatementImpl) statement;
                    case TIBasicRepeatStatement statement -> (TIBasicRepeatStatementImpl) statement;
                    case TIBasicForStatement statement -> (TIBasicForStatementImpl) statement;
                    case TIBasicIfStatement statement -> (TIBasicIfStatementImpl) statement;
                    case TIBasicThenBlock statement -> (TIBasicThenBlockImpl) statement;
                    case TIBasicElseBlock statement -> (TIBasicElseBlockImpl) statement;
                    default -> throw new RuntimeException("Weird child found in StructureViewElement: " + child);
                })
                .map(TIBasicStructureViewElement::new)
                .toArray(TreeElement[]::new);
    }

}
