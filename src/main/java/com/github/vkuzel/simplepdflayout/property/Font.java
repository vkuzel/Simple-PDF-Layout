package com.github.vkuzel.simplepdflayout.property;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

@FunctionalInterface
public interface Font {

    Font HELVETICA = () -> PDType1Font.HELVETICA;

    PDType1Font getPdType1Font();
}
