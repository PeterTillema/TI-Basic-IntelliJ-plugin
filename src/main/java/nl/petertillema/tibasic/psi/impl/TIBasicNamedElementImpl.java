package nl.petertillema.tibasic.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class TIBasicNamedElementImpl extends ASTWrapperPsiElement {

    public TIBasicNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }

}
