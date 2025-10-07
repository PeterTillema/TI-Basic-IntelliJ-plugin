package nl.petertillema.tibasic.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import nl.petertillema.tibasic.psi.TIBasicGoto;
import nl.petertillema.tibasic.psi.TIBasicLbl;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.references.TIBasicLabelReference;

public class TIBasicPsiImplUtil {

    public static String getName(TIBasicLbl element) {
        var labelNameNode = element.getNode().findChildByType(TIBasicTypes.LBL_NAME);
        return labelNameNode != null ? labelNameNode.getText() : null;
    }

    public static PsiElement setName(TIBasicLbl element, String name) {
        System.out.println("setName");
        return element;
    }

    public static PsiElement getNameIdentifier(TIBasicLbl element) {
        var labelNameNode = element.getNode().findChildByType(TIBasicTypes.LBL_NAME);
        return labelNameNode != null ? labelNameNode.getPsi() : null;
    }

    public static PsiReference[] getReferences(TIBasicGoto element) {
        return new PsiReference[]{new TIBasicLabelReference(element, element.getLblName().getTextRangeInParent())};
    }

}
