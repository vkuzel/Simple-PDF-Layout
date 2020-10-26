package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.Calculator;
import com.github.vkuzel.simplepdflayout.property.Dimension;
import com.github.vkuzel.simplepdflayout.property.Point;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.util.HashSet;
import java.util.Set;

public interface Element {

    float calculateX(Set<Calculator> calculatorPath);

    float calculateY(Set<Calculator> calculatorPath);

    default Point calculateTopLeft() {
        float x = calculateX(new HashSet<>());
        float y = calculateY(new HashSet<>());
        return Point.of(x, y);
    }

    float calculateWidth(Set<Calculator> calculatorPath);

    float calculateHeight(Set<Calculator> calculatorPath);

    default Dimension calculateDimension() {
        float width = calculateWidth(new HashSet<>());
        float height = calculateHeight(new HashSet<>());
        return Dimension.of(width, height);
    }

    float calculateContentX(Set<Calculator> calculatorPath);

    float calculateContentY(Set<Calculator> calculatorPath);

    default Point calculateContentTopLeft() {
        float x = calculateContentX(new HashSet<>());
        float y = calculateContentY(new HashSet<>());
        return Point.of(x, y);
    }

    float calculateContentWidth(Set<Calculator> calculatorPath);

    float calculateContentHeight(Set<Calculator> calculatorPath);

    default Dimension calculateContentDimension() {
        float width = calculateContentWidth(new HashSet<>());
        float height = calculateContentHeight(new HashSet<>());
        return Dimension.of(width, height);
    }

    void render(PDDocument document, PDPageContentStream contentStream);

    default Point convertPointToPdfCoordinates(Point point) {
        if (this instanceof ChildElement) {
            return ((ChildElement<?>) this).getParent().convertPointToPdfCoordinates(point);
        } else {
            throw new UnsupportedOperationException("Not implemented!");
        }
    }
}
