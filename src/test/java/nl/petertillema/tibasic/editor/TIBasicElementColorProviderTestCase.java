package nl.petertillema.tibasic.editor;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.util.ui.ColorIcon;
import nl.petertillema.tibasic.TIBasicPaletteColors;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TIBasicElementColorProviderTestCase extends BasePlatformTestCase {

    @Override
    protected @NonNls @NotNull String getTestDataPath() {
        return "src/test/testData/editor/";
    }

    public void testColorProviderGutter() {
        myFixture.configureByFile("ColorProviderSampleCode.basic");
        var gutters = myFixture.findAllGutters();

        assertEquals(2, gutters.size());

        var icon1 = assertInstanceOf(gutters.get(0).getIcon(), ColorIcon.class);
        assertEquals(TIBasicPaletteColors.ORANGE, icon1.getIconColor());
        var icon2 = assertInstanceOf(gutters.get(1).getIcon(), ColorIcon.class);
        assertEquals(TIBasicPaletteColors.BLACK, icon2.getIconColor());
    }

}
