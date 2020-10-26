package com.github.vkuzel.simplepdflayout.renderer;

import com.github.vkuzel.simplepdflayout.ElementWithBorder;
import com.github.vkuzel.simplepdflayout.ElementWithMargin;
import com.github.vkuzel.simplepdflayout.property.Border;
import com.github.vkuzel.simplepdflayout.property.Line;
import com.github.vkuzel.simplepdflayout.property.Margin;
import com.github.vkuzel.simplepdflayout.property.Point;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.HashSet;
import java.util.function.Function;

import static com.github.vkuzel.simplepdflayout.util.Utils.getValue;

public final class BorderRenderer {

    private final ElementWithBorder element;

    public BorderRenderer(ElementWithBorder element) {
        this.element = element;
    }

    public void render(PDPageContentStream contentStream) {
        Point topLeft = Point.of(calculateTopLeftX(), calculateTopLeftY());
        Point bottomRight = Point.of(calculateBottomRightX(), calculateBottomRightY());
        Point topRight = Point.of(bottomRight.getX(), topLeft.getY());
        Point bottomLeft = Point.of(topLeft.getX(), bottomRight.getY());

        Line borderTop = getLine(Border::getTop);
        Line borderRight = getLine(Border::getRight);
        Line borderBottom = getLine(Border::getBottom);
        Line borderLeft = getLine(Border::getLeft);

        if (borderTop != null && borderTop.getWidth() > 0) {
            Point topStart = Point.of(topLeft.getX() - getWidth(Border::getLeft) / 2, topLeft.getY());
            Point topEnd = Point.of(topRight.getX() + getWidth(Border::getRight) / 2, topRight.getY());
            renderLine(borderTop, topStart, topEnd, contentStream);
        }
        if (borderRight != null && borderRight.getWidth() > 0) {
            Point rightStart = Point.of(topRight.getX(), topRight.getY() - getWidth(Border::getTop) / 2);
            Point rightEnd = Point.of(bottomRight.getX(), bottomRight.getY() + getWidth(Border::getBottom) / 2);
            renderLine(borderRight, rightStart, rightEnd, contentStream);
        }
        if (borderBottom != null && borderBottom.getWidth() > 0) {
            Point bottomStart = Point.of(bottomRight.getX() + getWidth(Border::getRight) / 2, bottomRight.getY());
            Point bottomEnd = Point.of(bottomLeft.getX() - getWidth(Border::getLeft) / 2, bottomLeft.getY());
            renderLine(borderBottom, bottomStart, bottomEnd, contentStream);
        }
        if (borderLeft != null && borderLeft.getWidth() > 0) {
            Point leftStart = Point.of(bottomLeft.getX(), bottomLeft.getY() + getWidth(Border::getBottom) / 2);
            Point leftEnd = Point.of(topLeft.getX(), topLeft.getY() - getWidth(Border::getTop) / 2);
            renderLine(borderLeft, leftStart, leftEnd, contentStream);
        }
    }

    private void renderLine(Line line, Point start, Point end, PDPageContentStream contentStream) {
        Point pdfStart = element.convertPointToPdfCoordinates(start);
        Point pdfEnd = element.convertPointToPdfCoordinates(end);

        try {
            contentStream.setLineWidth(line.getWidth());
            contentStream.setLineDashPattern(line.calculatePattern(), 0);
            contentStream.setStrokingColor(line.getColor());
            contentStream.moveTo(pdfStart.getX(), pdfStart.getY());
            contentStream.lineTo(pdfEnd.getX(), pdfEnd.getY());
            contentStream.stroke();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private float calculateTopLeftX() {
        float x = element.calculateX(new HashSet<>());
        x += getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getLeft);
        x += getWidth(Border::getLeft) / 2;
        return x;
    }

    private float calculateTopLeftY() {
        float y = element.calculateY(new HashSet<>());
        y += getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getTop);
        y += getWidth(Border::getTop) / 2;
        return y;
    }

    private float calculateBottomRightX() {
        float x = element.calculateX(new HashSet<>());
        x += element.calculateWidth(new HashSet<>());
        x -= getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getRight);
        x -= getWidth(Border::getRight) / 2;
        return x;
    }

    private float calculateBottomRightY() {
        float y = element.calculateY(new HashSet<>());
        y += element.calculateHeight(new HashSet<>());
        y -= getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getBottom);
        y -= getWidth(Border::getBottom) / 2;
        return y;
    }

    private float getWidth(Function<Border, Line> mapToLine) {
        Line line = getLine(mapToLine);
        if (line == null) {
            return 0;
        }
        return line.getWidth();
    }

    private Line getLine(Function<Border, Line> mapToLine) {
        Border border = element.getBorder();
        if (border == null) {
            return null;
        }
        return mapToLine.apply(border);
    }
}
