package nl.petertillema.tibasic.ide.CEmu;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import org.jetbrains.annotations.NotNull;

public class CEmuToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        if (!JBCefApp.isSupported()) return;

        var browser = new JBCefBrowser();
        browser.getJBCefClient().getCefClient().addDialogHandler(new CEmuDialogHandler());
        browser.getJBCefClient().getCefClient().addDisplayHandler(new CEmuDisplayHandler());
        browser.getJBCefClient().addRequestHandler(new CEmuRequestHandler(toolWindow.getContentManager()), browser.getCefBrowser());
        browser.loadURL("https://localhost/CEmu.html");

        var content = ContentFactory.getInstance().createContent(browser.getComponent(), null, false);
        toolWindow.getContentManager().addContent(content);
    }

}
