package com.github.vkuzel.simplepdflayout;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.IOException;

public final class Document {

    public static void renderPageToFile(Page page, String fileName) {
        try (PDDocument pdDocument = new PDDocument()) {
            PDPage pdPage = new PDPage(PDRectangle.A4);
            pdDocument.addPage(pdPage);
            try (PDPageContentStream contentStream = new PDPageContentStream(pdDocument, pdPage)) {
                page.render(pdDocument, contentStream);
            }
            pdDocument.save(fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
