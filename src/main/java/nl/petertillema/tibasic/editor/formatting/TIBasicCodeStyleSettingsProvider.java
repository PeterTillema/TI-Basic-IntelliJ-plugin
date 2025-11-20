package nl.petertillema.tibasic.editor.formatting;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.lang.Language;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.codeStyle.CodeStyleConfigurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import nl.petertillema.tibasic.language.TIBasicLanguage;
import org.jetbrains.annotations.NotNull;

public final class TIBasicCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

    @Override
    public @NotNull Language getLanguage() {
        return TIBasicLanguage.INSTANCE;
    }

    @Override
    public @NotNull CustomCodeStyleSettings createCustomSettings(@NotNull CodeStyleSettings settings) {
        return new TIBasicCodeStyleSettings(settings);
    }

    @Override
    public @NlsContexts.ConfigurableName @NotNull String getConfigurableDisplayName() {
        return "TI-Basic";
    }

    @Override
    public @NotNull CodeStyleConfigurable createConfigurable(@NotNull CodeStyleSettings settings, @NotNull CodeStyleSettings modelSettings) {
        return new CodeStyleAbstractConfigurable(settings, modelSettings, this.getConfigurableDisplayName()) {
            @Override
            protected @NotNull CodeStyleAbstractPanel createPanel(@NotNull CodeStyleSettings settings) {
                return new TIBasicCodeStyleMainPanel(getCurrentSettings(), settings);
            }
        };
    }

    private static class TIBasicCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
        public TIBasicCodeStyleMainPanel(CodeStyleSettings currentSettings, @NotNull CodeStyleSettings settings) {
            super(TIBasicLanguage.INSTANCE, currentSettings, settings);
        }
    }

}
