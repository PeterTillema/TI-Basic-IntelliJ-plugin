package nl.petertillema.tibasic.psi.visitors;

import com.intellij.psi.PsiElement;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import org.jetbrains.annotations.NotNull;

public class TIBasicFullRecursiveVisitor extends TIBasicVisitor {

    @Override
    public void visitElement(@NotNull PsiElement element) {
        super.visitElement(element);
        element.acceptChildren(this);
    }
}
