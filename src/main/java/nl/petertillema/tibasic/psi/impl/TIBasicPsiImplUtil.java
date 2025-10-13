package nl.petertillema.tibasic.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import nl.petertillema.tibasic.psi.TIBasicGotoStatement;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.references.TIBasicLabelReference;

public class TIBasicPsiImplUtil {

    public static String getName(TIBasicLblStatement element) {
        var labelNameNode = element.getNode().findChildByType(TIBasicTypes.LBL_NAME);
        return labelNameNode != null ? labelNameNode.getText() : null;
    }

    public static PsiElement setName(TIBasicLblStatement element, String name) {
        return element;
    }

    public static PsiElement getNameIdentifier(TIBasicLblStatement element) {
        var labelNameNode = element.getNode().findChildByType(TIBasicTypes.LBL_NAME);
        return labelNameNode != null ? labelNameNode.getPsi() : null;
    }

    public static PsiReference[] getReferences(TIBasicGotoStatement element) {
        return new PsiReference[]{new TIBasicLabelReference(element, element.getLblName().getTextRangeInParent())};
    }

}
