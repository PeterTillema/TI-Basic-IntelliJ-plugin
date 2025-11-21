package nl.petertillema.tibasic.ide.CEmu;

import com.intellij.openapi.Disposable;
import com.intellij.ui.jcef.utils.JBCefLocalRequestHandler;
import com.intellij.ui.jcef.utils.JBCefStreamResourceHandler;

import java.util.Map;

public class CEmuRequestHandler extends JBCefLocalRequestHandler {

    private final Disposable parent;

    public CEmuRequestHandler(Disposable parent) {
        super("https", "localhost");
        this.parent = parent;
        addResourceHandler("CEmu.html", "/CEmu/CEmu.html", "text/html");
        addResourceHandler("WebCEmu_utils.js", "/CEmu/WebCEmu_utils.js", "text/javascript");
        addResourceHandler("WebCEmu.js", "/CEmu/WebCEmu.js", "text/javascript");
        addResourceHandler("WebCEmu.wasm", "/CEmu/WebCEmu.wasm", "application/wasm");
        addResourceHandler("emu_keypad_84pce.png", "/CEmu/emu_keypad_84pce.png", "application/png");
    }

    private void addResourceHandler(String path, String filename, String mimeType) {
        var stream = this.getClass().getResourceAsStream(filename);
        if (stream == null) return;
        createResource(path, () -> new JBCefStreamResourceHandler(stream, mimeType, parent, Map.of()));
    }

}
