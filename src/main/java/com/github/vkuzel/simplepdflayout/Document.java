package com.github.vkuzel.simplepdflayout;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

public final class Document {

    public static void renderPagesToFile(Pages pages, String fileName) {
        try (PDDocument pdDocument = new PDDocument()) {
            for (Page page : pages.getPages()) {
                renderPage(pdDocument, page);
            }
            pdDocument.save(fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void renderPageToFile(Page page, String fileName) {
        try (PDDocument pdDocument = new PDDocument()) {
            renderPage(pdDocument, page);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void renderPage(PDDocument pdDocument, Page page) throws IOException {
        PDPage pdPage = new PDPage(page.getDimension());
        pdDocument.addPage(pdPage);
        try (PDPageContentStream contentStream = new PDPageContentStream(pdDocument, pdPage)) {
            page.render(pdDocument, contentStream);
        }
    }
}
