package nl.petertillema.tibasic.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.psi.TIBasicElseBlock;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TIBasicStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {

    public TIBasicStructureViewModel(@NotNull PsiFile psiFile, @Nullable Editor editor) {
        super(psiFile, editor, new TIBasicStructureViewElement(psiFile));
    }

    @Override
    public Sorter @NotNull [] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement structureViewTreeElement) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement structureViewTreeElement) {
        return false;
    }

    @Override
    protected Class<?> @NotNull [] getSuitableClasses() {
        return new Class[]{
                TIBasicRepeatStatement.class,
                TIBasicWhileStatement.class,
                TIBasicForStatement.class,
                TIBasicIfStatement.class,
                TIBasicThenBlock.class,
                TIBasicElseBlock.class
        };
    }
}
