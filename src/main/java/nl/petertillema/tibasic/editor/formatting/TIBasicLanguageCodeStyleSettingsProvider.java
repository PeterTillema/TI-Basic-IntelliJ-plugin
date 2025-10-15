package nl.petertillema.tibasic.editor.formatting;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import nl.petertillema.tibasic.language.TIBasicLanguage;
import org.jetbrains.annotations.NotNull;

public final class TIBasicLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

    @Override
    public @NotNull Language getLanguage() {
        return TIBasicLanguage.INSTANCE;
    }

    @Override
    public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
        if (settingsType == SettingsType.INDENT_SETTINGS) {
            consumer.showStandardOptions("INDENT_SIZE");
        }
    }

    @Override
    public @NotNull IndentOptionsEditor getIndentOptionsEditor() {
        return new IndentOptionsEditor(this);
    }

    @Override
    public @NotNull String getCodeSample(@NotNull SettingsType settingsType) {
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
                Goto DE""";
    }

}
