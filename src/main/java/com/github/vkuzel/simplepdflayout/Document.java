package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.property.Dimension;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

public final class Document {

    public static byte[] renderPagesToByteArray(Function<PDDocument, Pages> pagesBuilder) {
        try (PDDocument pdDocument = new PDDocument()) {
            Pages pages = pagesBuilder.apply(pdDocument);
            for (Page page : pages.getPages()) {
                renderPage(pdDocument, page);
            }
            return saveToByteArray(pdDocument);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] renderPageToByteArray(Function<PDDocument, Page> pageBuilder) {
        try (PDDocument pdDocument = new PDDocument()) {
            Page page = pageBuilder.apply(pdDocument);
            renderPage(pdDocument, page);
            return saveToByteArray(pdDocument);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void renderPagesToFile(Pages pages, String fileName) {
        try (PDDocument pdDocument = new PDDocument()) {
            for (Page page : pages.getPages()) {
                renderPage(pdDocument, page);
            }
            saveToFile(pdDocument, fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void renderPageToFile(Page page, String fileName) {
        try (PDDocument pdDocument = new PDDocument()) {
            renderPage(pdDocument, page);
            saveToFile(pdDocument, fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void renderPage(PDDocument pdDocument, Page page) throws IOException {
        Dimension dimension = page.getDimension();
        PDPage pdPage = new PDPage(dimension.toPdRectangle());
        pdDocument.addPage(pdPage);
        try (PDPageContentStream contentStream = new PDPageContentStream(pdDocument, pdPage)) {
            page.render(pdDocument, contentStream);
        }
    }

    private static byte[] saveToByteArray(PDDocument pdDocument) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdDocument.save(outputStream);
        return outputStream.toByteArray();
    }

    public static void saveToFile(PDDocument pdDocument, String fileName) throws IOException {
        pdDocument.save(fileName);
    }
}
