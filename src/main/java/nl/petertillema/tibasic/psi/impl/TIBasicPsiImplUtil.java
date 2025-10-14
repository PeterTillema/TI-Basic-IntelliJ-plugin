package nl.petertillema.tibasic.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import nl.petertillema.tibasic.psi.TIBasicAssignmentTarget;
import nl.petertillema.tibasic.psi.TIBasicForIdentifier;
import nl.petertillema.tibasic.psi.TIBasicGotoName;
import nl.petertillema.tibasic.psi.TIBasicLblName;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.references.TIBasicLabelReference;
import nl.petertillema.tibasic.psi.references.TIBasicVariableReference;

public class TIBasicPsiImplUtil {

    public static String getName(TIBasicLblName element) {
        return element.getText();
    }

    public static PsiElement setName(TIBasicLblName element, String name) {
        return element;
    }

    public static PsiElement getNameIdentifier(TIBasicLblName element) {
        return element;
    }

    public static String getName(TIBasicAssignmentTarget element) {
        return element.getText();
    }

    public static PsiElement setName(TIBasicAssignmentTarget element, String name) {
        return element;
    }

    public static PsiElement getNameIdentifier(TIBasicAssignmentTarget element) {
        return element;
    }

    public static String getName(TIBasicForIdentifier element) {
        return element.getText();
    }

    public static PsiElement setName(TIBasicForIdentifier element, String name) {
        return element;
    }

    public static PsiElement getNameIdentifier(TIBasicForIdentifier element) {
        return element;
    }

    public static PsiReference[] getReferences(TIBasicGotoName element) {
        return new PsiReference[]{new TIBasicLabelReference(element, TextRange.from(0, element.getTextLength()))};
    }

    public static PsiReference[] getReferences(TIBasicLiteralExpr element) {
        var type = element.getFirstChild().getNode().getElementType();

        if (type == TIBasicTypes.SIMPLE_VARIABLE ||
                type == TIBasicTypes.ANS_VARIABLE ||
                type == TIBasicTypes.LIST_VARIABLE ||
                type == TIBasicTypes.EQUATION_VARIABLE_1 ||
                type == TIBasicTypes.EQUATION_VARIABLE_2 ||
                type == TIBasicTypes.EQUATION_VARIABLE_3 ||
                type == TIBasicTypes.EQUATION_VARIABLE_4 ||
                type == TIBasicTypes.STRING_VARIABLE ||
                type == TIBasicTypes.MATRIX_VARIABLE
        ) {
            return new PsiReference[]{new TIBasicVariableReference(element, TextRange.from(0, element.getTextLength()))};
        }

        return PsiReference.EMPTY_ARRAY;
    }

}
