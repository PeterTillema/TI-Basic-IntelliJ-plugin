package nl.petertillema.tibasic.editor.formatting;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public final class TIBasicCodeStyleSettings extends CustomCodeStyleSettings {

    public TIBasicCodeStyleSettings( @NotNull CodeStyleSettings container) {
        super("TIBasicCodeStyleSettings", container);
    }

}
