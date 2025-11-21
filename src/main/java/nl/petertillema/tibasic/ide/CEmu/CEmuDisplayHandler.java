package nl.petertillema.tibasic.ide.CEmu;

import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefDisplayHandler;

public class CEmuDisplayHandler implements CefDisplayHandler {

    @Override
    public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
    }

    @Override
    public void onTitleChange(CefBrowser browser, String title) {
    }

    @Override
    public void onFullscreenModeChange(CefBrowser browser, boolean fullscreen) {
    }

    @Override
    public boolean onTooltip(CefBrowser browser, String text) {
        return false;
    }

    @Override
    public void onStatusMessage(CefBrowser browser, String value) {
    }

    @Override
    public boolean onConsoleMessage(CefBrowser browser, CefSettings.LogSeverity level, String message, String source, int line) {
        System.out.println("=== CONSOLE === " + message);
        return false;
    }

    @Override
    public boolean onCursorChange(CefBrowser browser, int cursorType) {
        return false;
    }

}
