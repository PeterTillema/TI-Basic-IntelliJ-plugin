package nl.petertillema.tibasic.psi;

import com.intellij.psi.util.PsiTreeUtil;
import nl.petertillema.tibasic.language.TIBasicFile;

import java.util.ArrayList;
import java.util.List;

public class TIBasicUtil {

    public static List<TIBasicLblStatement> findLabels(TIBasicFile file, String key) {
        var labels = PsiTreeUtil.findChildrenOfType(file, TIBasicLblStatement.class);

        return labels.stream()
                .filter(label -> key.equals(label.getLblName().getText()))
                .toList();
    }

    public static List<TIBasicLblStatement> findLabels(TIBasicFile file) {
        var labels = PsiTreeUtil.findChildrenOfType(file, TIBasicLblStatement.class);
        return new ArrayList<>(labels);
    }

    public static List<TIBasicGotoStatement> findGotos(TIBasicFile file) {
        return List.of();
    }

}
