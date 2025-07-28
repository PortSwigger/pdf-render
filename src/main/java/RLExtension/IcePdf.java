package RLExtension;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.ViewerPropertiesManager;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;

public class IcePdf {

    private SwingController controller;
    private JPanel viewerComponentPanel;
    private boolean openDocument = false;

    public IcePdf() {
        buildPanel();
    }

    public void buildPanel() {
        controller = new SwingController();

        // Configure viewer properties to disable annotation tools
        ViewerPropertiesManager properties = ViewerPropertiesManager.getInstance();
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION, false);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_FORMS, false);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_UTILITYPANE_ANNOTATION, false);

        // Build the viewer panel with the updated properties
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        viewerComponentPanel = factory.buildViewerPanel();

        // Disable annotation callback
        controller.getDocumentViewController().setAnnotationCallback(null);
    }

    public Component getComponent() {
        return viewerComponentPanel;
    }

    public void setPdfContent(byte[] respBytes, int bodyOffset, String description, String url) {
        if (openDocument) {
            controller.closeDocument();
            openDocument = false;
        }

        controller.openDocument(respBytes, bodyOffset, respBytes.length - bodyOffset, description, url);
        openDocument = true;

        // Use integer value for page fit mode
        controller.setPageFitMode(1, true); // 1 corresponds to PAGE_FIT_WINDOW_WIDTH
    }

    public void setFullScreenMode() {
        viewerComponentPanel.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        viewerComponentPanel.setVisible(true); // Ensure visibility
        viewerComponentPanel.repaint(); // Force repaint
    }

    public void loadPdf(byte[] pdfBytes) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes)) {
            controller.openDocument(inputStream, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
