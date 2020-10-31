package com.github.vkuzel.simplepdflayout.renderer;

import com.github.vkuzel.simplepdflayout.calculator.CalculationContext;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class RenderingContext {

    private final PDDocument document;
    private final PDPageContentStream contentStream;
    private final CalculationContext calculationContext = new CalculationContext();

    public RenderingContext(PDDocument document, PDPageContentStream contentStream) {
        this.document = document;
        this.contentStream = contentStream;
    }

    public PDDocument getDocument() {
        return document;
    }

    public PDPageContentStream getContentStream() {
        return contentStream;
    }

    public CalculationContext getCalculationContext() {
        return calculationContext;
    }
}
