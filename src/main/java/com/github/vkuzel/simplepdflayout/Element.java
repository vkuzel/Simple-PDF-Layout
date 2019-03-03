package com.github.vkuzel.simplepdflayout;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public interface Element {

    void draw(PDDocument document, PDPageContentStream contentStream);

    String toStringCoordinates();
}
