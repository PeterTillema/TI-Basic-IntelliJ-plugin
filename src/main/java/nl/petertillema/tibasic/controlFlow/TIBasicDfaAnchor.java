package nl.petertillema.tibasic.controlFlow;

import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.psi.PsiElement;

public record TIBasicDfaAnchor(PsiElement element) implements DfaAnchor {
}
