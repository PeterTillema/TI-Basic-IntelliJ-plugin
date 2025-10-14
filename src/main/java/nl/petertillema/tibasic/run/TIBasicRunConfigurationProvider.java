package nl.petertillema.tibasic.run;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import nl.petertillema.tibasic.language.TIBasicFile;
import org.jetbrains.annotations.NotNull;

public class TIBasicRunConfigurationProvider extends LazyRunConfigurationProducer<TIBasicRunConfiguration> {

    @Override
    public @NotNull ConfigurationFactory getConfigurationFactory() {
        return new TIBasicConfigurationFactory(new TIBasicConfigurationType());
    }

    @Override
    protected boolean setupConfigurationFromContext(@NotNull TIBasicRunConfiguration configuration, @NotNull ConfigurationContext context, @NotNull Ref<PsiElement> sourceElement) {
        var psiLocation = context.getPsiLocation();
        if (psiLocation == null || !(psiLocation.getContainingFile() instanceof TIBasicFile)) return false;

        var extension = psiLocation.getContainingFile().getVirtualFile().getExtension();
        if (extension == null) extension = "";

        var inputPath = psiLocation.getContainingFile().getVirtualFile().getPath();
        var outputPath = inputPath.substring(0, inputPath.length() - extension.length()) + ".8xp";

        configuration.setInputPathField(inputPath);
        configuration.setOutputPathField(outputPath);
        configuration.setGeneratedName();

        return true;
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull TIBasicRunConfiguration configuration, @NotNull ConfigurationContext context) {
        System.out.println("isConfigurationFromContext");
        return false;
    }
}
