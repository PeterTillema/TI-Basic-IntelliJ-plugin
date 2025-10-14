package nl.petertillema.tibasic.run;

import com.intellij.execution.lineMarker.ExecutorAction;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.psi.PsiElement;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.language.TIBasicIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TIBasicRunLineMarkerContributor extends RunLineMarkerContributor {

    @Override
    public @Nullable Info getInfo(@NotNull PsiElement element) {
        if (!(element instanceof TIBasicFile)) return null;

        return new Info(TIBasicIcons.FILE, ExecutorAction.getActions());
    }

}
