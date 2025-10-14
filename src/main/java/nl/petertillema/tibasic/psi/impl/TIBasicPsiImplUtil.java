package nl.petertillema.tibasic.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import nl.petertillema.tibasic.psi.TIBasicGotoName;
import nl.petertillema.tibasic.psi.TIBasicLblName;
import nl.petertillema.tibasic.psi.references.TIBasicLabelReference;

public class TIBasicPsiImplUtil {

    public static String getName(TIBasicLblName element) {
        return element.getText();
    }

    public static PsiElement setName(TIBasicLblName element, String name) {
        return element;
    }

    public static PsiElement getNameIdentifier(TIBasicLblName element) {
        System.out.println("getNameIdentifier");
        return element;
    }

    public static PsiReference[] getReferences(TIBasicGotoName element) {
        return new PsiReference[]{new TIBasicLabelReference(element, TextRange.from(0, element.getTextLength()))};
    }

}
