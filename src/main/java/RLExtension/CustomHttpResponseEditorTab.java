package RLExtension;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;

import java.awt.*;

public class CustomHttpResponseEditorTab implements ExtensionProvidedHttpResponseEditor {
    MontoyaApi api;
    Logging logging;
    EditorCreationContext creationContext;
    IcePdf propertyPanel;

    public CustomHttpResponseEditorTab(MontoyaApi api, EditorCreationContext creationContext) {
        this.api = api;
        this.propertyPanel = new IcePdf();
        this.creationContext = creationContext;

        api.userInterface().applyThemeToComponent(propertyPanel.getComponent());
    }

    @Override
    public HttpResponse getResponse() {
        return null;
    }

    @Override
    public void setRequestResponse(HttpRequestResponse httpRequestResponse) {
        if (httpRequestResponse == null || !httpRequestResponse.hasResponse()) {
            return; // No response available
        }

        // Get the response body directly
        byte[] pdfContent = httpRequestResponse.response().body().getBytes();

        // Load the PDF content into IcePdf
        try {
            propertyPanel.loadPdf(pdfContent);
        } catch (Exception e) {
            api.logging().logToError("Failed to load PDF: " + e.getMessage());
        }
    }

    @Override
    public boolean isEnabledFor(HttpRequestResponse httpRequestResponse) {
        // Check if the response exists
        if (!httpRequestResponse.hasResponse()) {
            return false;
        }

        // Check if the response body starts with the PDF signature
        byte[] bodyBytes = httpRequestResponse.response().body().getBytes();
        return PdfUtil.isPdfFile(bodyBytes, 0);
    }

    @Override
    public String caption() {
        return "PDF";
    }

    @Override
    public Component uiComponent() {
        return propertyPanel.getComponent();
    }

    @Override
    public Selection selectedData() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }
}
