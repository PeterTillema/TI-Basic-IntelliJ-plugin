package nl.petertillema.tibasic.psi.impl;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import nl.petertillema.tibasic.psi.TIBasicAssignmentTarget;
import nl.petertillema.tibasic.psi.TIBasicElseBlock;
import nl.petertillema.tibasic.psi.TIBasicForIdentifier;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicGotoName;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicLblName;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicThenBlock;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import nl.petertillema.tibasic.psi.references.TIBasicLabelReference;
import nl.petertillema.tibasic.psi.references.TIBasicVariableReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static nl.petertillema.tibasic.psi.TIBasicUtil.getTextUntilNewline;

public final class TIBasicPsiImplUtil {

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
        IElementType type = element.getFirstChild().getNode().getElementType();

        if (type == TIBasicTypes.SIMPLE_VARIABLE ||
                type == TIBasicTypes.ANS_VARIABLE ||
                type == TIBasicTypes.LIST_VARIABLE ||
                type == TIBasicTypes.EQUATION_VARIABLE ||
                type == TIBasicTypes.STRING_VARIABLE ||
                type == TIBasicTypes.MATRIX_VARIABLE
        ) {
            return new PsiReference[]{new TIBasicVariableReference(element, TextRange.from(0, element.getTextLength()))};
        }

        return PsiReference.EMPTY_ARRAY;
    }

    public static ItemPresentation getPresentation(TIBasicWhileStatement statement) {
        return new ItemPresentation() {
            @Override
            public @NlsSafe @NotNull String getPresentableText() {
                return getTextUntilNewline(statement);
            }

            @Override
            public @Nullable Icon getIcon(boolean unused) {
                return null;
            }
        };
    }

    public static ItemPresentation getPresentation(TIBasicRepeatStatement statement) {
        return new ItemPresentation() {
            @Override
            public @NlsSafe @NotNull String getPresentableText() {
                return getTextUntilNewline(statement);
            }

            @Override
            public @Nullable Icon getIcon(boolean unused) {
                return null;
            }
        };
    }

    public static ItemPresentation getPresentation(TIBasicForStatement statement) {
        return new ItemPresentation() {
            @Override
            public @NlsSafe @NotNull String getPresentableText() {
                return getTextUntilNewline(statement);
            }

            @Override
            public @Nullable Icon getIcon(boolean unused) {
                return null;
            }
        };
    }

    public static ItemPresentation getPresentation(TIBasicIfStatement statement) {
        return new ItemPresentation() {
            @Override
            public @NlsSafe @NotNull String getPresentableText() {
                return getTextUntilNewline(statement);
            }

            @Override
            public @Nullable Icon getIcon(boolean unused) {
                return null;
            }
        };
    }

    public static ItemPresentation getPresentation(TIBasicThenBlock statement) {
        return new ItemPresentation() {
            @Override
            public @NlsSafe @NotNull String getPresentableText() {
                return "Then";
            }

            @Override
            public @Nullable Icon getIcon(boolean unused) {
                return null;
            }
        };
    }

    public static ItemPresentation getPresentation(TIBasicElseBlock statement) {
        return new ItemPresentation() {
            @Override
            public @NlsSafe @NotNull String getPresentableText() {
                return "Else";
            }

            @Override
            public @Nullable Icon getIcon(boolean unused) {
                return null;
            }
        };
    }

}
