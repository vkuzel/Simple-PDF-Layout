package com.github.vkuzel.simplepdflayout.property;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public final class Font {

    private final PDFont pdFont;

    private Font(PDFont pdFont) {
        this.pdFont = pdFont;
    }

    public static Font loadFromFile(PDDocument pdDocument, File file) {
        try {
            PDFont pdFont = PDType0Font.load(pdDocument, file);
            return new Font(pdFont);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Font loadFromResource(PDDocument pdDocument, String resource) {
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            PDFont pdFont = PDType0Font.load(pdDocument, inputStream);
            return new Font(pdFont);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Font helvetica() {
        return new Font(HELVETICA);
    }

    public PDFont getPdFont() {
        return pdFont;
    }
}
