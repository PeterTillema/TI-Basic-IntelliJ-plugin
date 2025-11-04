package nl.petertillema.tibasic.syntax;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.util.NlsContexts;
import nl.petertillema.tibasic.language.TIBasicIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public final class TIBasicColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Braces and Operators//Operator sign", TIBasicSyntaxHighlighter.OPERATOR),
            new AttributesDescriptor("Braces and Operators//Parenthesis", TIBasicSyntaxHighlighter.PARENTHESES),
            new AttributesDescriptor("Braces and Operators//Braces", TIBasicSyntaxHighlighter.BRACES),
            new AttributesDescriptor("Braces and Operators//Brackets", TIBasicSyntaxHighlighter.BRACKETS),
            new AttributesDescriptor("Braces and Operators//Comma", TIBasicSyntaxHighlighter.COMMA),
            new AttributesDescriptor("Command", TIBasicSyntaxHighlighter.COMMAND),
            new AttributesDescriptor("Command//Program call", TIBasicSyntaxHighlighter.PRGM_CALL),
            new AttributesDescriptor("Comments//Line comment", TIBasicSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Functions", TIBasicSyntaxHighlighter.FUNCTION),
            new AttributesDescriptor("Number", TIBasicSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("String", TIBasicSyntaxHighlighter.STRING),
            new AttributesDescriptor("Variables//Ans", TIBasicSyntaxHighlighter.ANS_IDENTIFIER),
            new AttributesDescriptor("Variables//List", TIBasicSyntaxHighlighter.LIST_IDENTIFIER),
            new AttributesDescriptor("Variables//Equation", TIBasicSyntaxHighlighter.EQUATION_IDENTIFIER),
            new AttributesDescriptor("Variables//String", TIBasicSyntaxHighlighter.STRING_IDENTIFIER),
            new AttributesDescriptor("Variables//Normal", TIBasicSyntaxHighlighter.SIMPLE_IDENTIFIER),
            new AttributesDescriptor("Variables//Picture", TIBasicSyntaxHighlighter.PICTURE_IDENTIFIER),
            new AttributesDescriptor("Variables//Matrix", TIBasicSyntaxHighlighter. MATRIX_IDENTIFIER),
    };

    @Override
    public @NotNull Icon getIcon() {
        return TIBasicIcons.FILE;
    }

    @Override
    public @NotNull SyntaxHighlighter getHighlighter() {
        return new TIBasicSyntaxHighlighter();
    }

    @Override
    public @NonNls @NotNull String getDemoText() {
        return """
                // This is some sample TI-Basic code
                Ans->B
                Radian
                {1,2,B+3->L1
                "Hello World->Str0
                Lbl DE
                For(A,1,20,3
                    Pt-On(sin(7*A-B),40
                    Output(A,1,"TEST"
                End
                If A>3
                Goto DE
                """;
    }

    @Override
    public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @Override
    public @NotNull @NlsContexts.ConfigurableName String getDisplayName() {
        return "TI-Basic";
    }
}
