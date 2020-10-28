package com.github.vkuzel.simplepdflayout.property;

import org.apache.pdfbox.pdmodel.font.PDFont;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

@FunctionalInterface
public interface Font {

    PDFont getPdFont();

    static Font helvetica() {
        return () -> HELVETICA;
    }
}
