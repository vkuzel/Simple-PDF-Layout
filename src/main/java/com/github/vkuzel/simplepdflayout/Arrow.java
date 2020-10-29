package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.Calculator;
import com.github.vkuzel.simplepdflayout.property.Line;
import com.github.vkuzel.simplepdflayout.property.Point;
import com.github.vkuzel.simplepdflayout.property.Vector;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.vkuzel.simplepdflayout.property.Line.Style.SOLID;
import static java.awt.Color.RED;

public final class Arrow implements ChildElement<Arrow> {

    private final ParentElement<?> parentElement;

    private float startX = 0;
    private float startY = 0;
    private Float endX;
    private Float endY;
    private Element startElement;
    private boolean startArrow = true;
    private boolean endArrow = false;
    private Line line = Line.of(3, SOLID, RED);

    Arrow(ParentElement<?> parentElement) {
        this.parentElement = parentElement;
    }

    public Arrow setStartPoint(Point point) {
        return setStartPosition(point.getX(), point.getY());
    }

    public Arrow setStartPosition(float x, float y) {
        this.startX = x;
        this.startY = y;
        return this;
    }

    public Arrow setEndPoint(Point point) {
        return setEndPosition(point.getX(), point.getY());
    }

    public Arrow setEndPosition(float x, float y) {
        this.endX = x;
        this.endY = y;
        return this;
    }

    public Arrow setStartElement(Element startElement) {
        this.startElement = startElement;
        return this;
    }

    public Arrow setStartArrow(boolean startArrow) {
        this.startArrow = startArrow;
        return this;
    }

    public Arrow setEndArrow(boolean endArrow) {
        this.endArrow = endArrow;
        return this;
    }

    public Arrow setLine(Line line) {
        this.line = line;
        return this;
    }

    @Override
    public ParentElement<?> getParent() {
        return parentElement;
    }

    @Override
    public void render(PDDocument document, PDPageContentStream contentStream) {
        try {
            Point start = Point.of(calculateStartX(new LinkedHashSet<>()), calculateStartY(new LinkedHashSet<>()));
            Point end = Point.of(calculateEndX(new LinkedHashSet<>()), calculateEndY(new LinkedHashSet<>()));
            Point pdfStart = parentElement.convertPointToPdfCoordinates(start);
            Point pdfEnd = parentElement.convertPointToPdfCoordinates(end);
            float arrowSize = line.getWidth() * 5.5f;

            if (startArrow) {
                Vector v = Vector.ofLineSegment(pdfStart, pdfEnd).normalize();
                Point p = Point.of(pdfStart.getX() + v.getX() * arrowSize, pdfStart.getY() + v.getY() * arrowSize);

                contentStream.setNonStrokingColor(line.getColor());
                contentStream.moveTo(pdfStart.getX(), pdfStart.getY());
                contentStream.lineTo(p.getX() + v.getY() * arrowSize / 2, p.getY() - v.getX() * arrowSize / 2);
                contentStream.lineTo(p.getX() - v.getY() * arrowSize / 2, p.getY() + v.getX() * arrowSize / 2);
                contentStream.closePath();
                contentStream.fill();

                pdfStart = p;
            }

            if (endArrow) {
                Vector v = Vector.ofLineSegment(pdfEnd, pdfStart).normalize();
                Point p = Point.of(pdfEnd.getX() + v.getX() * arrowSize, pdfEnd.getY() + v.getY() * arrowSize);

                contentStream.setNonStrokingColor(line.getColor());
                contentStream.moveTo(pdfEnd.getX(), pdfEnd.getY());
                contentStream.lineTo(p.getX() + v.getY() * arrowSize / 2, p.getY() - v.getX() * arrowSize / 2);
                contentStream.lineTo(p.getX() - v.getY() * arrowSize / 2, p.getY() + v.getX() * arrowSize / 2);
                contentStream.closePath();
                contentStream.fill();

                pdfEnd = p;
            }

            contentStream.setLineWidth(line.getWidth());
            contentStream.setStrokingColor(line.getColor());
            contentStream.setLineDashPattern(line.calculatePattern(), 0);
            contentStream.moveTo(pdfStart.getX(), pdfStart.getY());
            contentStream.lineTo(pdfEnd.getX(), pdfEnd.getY());
            contentStream.stroke();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private float calculateStartX(Set<Calculator> calculatorPath) {
        if (startElement != null) {
            float x = startElement.calculateX(new LinkedHashSet<>(calculatorPath));
            float width = startElement.calculateWidth(new LinkedHashSet<>(calculatorPath));
            return x + width;
        } else {
            return startX;
        }
    }

    private float calculateStartY(Set<Calculator> calculatorPath) {
        if (startElement != null) {
            float y = startElement.calculateY(new LinkedHashSet<>(calculatorPath));
            float height = startElement.calculateHeight(new LinkedHashSet<>(calculatorPath));
            return y + height;
        } else {
            return startY;
        }
    }

    private float calculateEndX(Set<Calculator> calculatorPath) {
        float startX = calculateStartX(new LinkedHashSet<>());
        float rx;
        if (endX == null) {
            float length = calculateLength();
            rx = startX + length;
            Element rootElement = getRootElement();
            float rootElementContentWidth = rootElement.calculateContentWidth(calculatorPath);
            if (rx > rootElementContentWidth) {
                rx = startX - length;
            }
        } else {
            rx = endX;
        }
        return rx;
    }

    private float calculateEndY(Set<Calculator> calculatorPath) {
        float startY = calculateStartY(new LinkedHashSet<>());
        float ry;
        if (endY == null) {
            float length = calculateLength();
            ry = startY + length;
            Element rootElement = getRootElement();
            float rootElementContentHeight = rootElement.calculateContentHeight(calculatorPath);
            if (ry > rootElementContentHeight) {
                ry = startY - length;
            }
        } else {
            ry = endY;
        }
        return ry;
    }

    private float calculateLength() {
        return line.getWidth() * 20;
    }

    private Element getRootElement() {
        ParentElement<?> parent = this.getParent();
        while (parent instanceof ChildElement) {
            parent = ((ChildElement<?>) parent).getParent();
        }
        return parent;
    }

    @Override
    public float calculateX(Set<Calculator> calculatorPath) {
        return Math.min(calculateStartX(calculatorPath), calculateEndX(calculatorPath));
    }

    @Override
    public float calculateY(Set<Calculator> calculatorPath) {
        return Math.min(calculateStartY(calculatorPath), calculateEndY(calculatorPath));
    }

    @Override
    public float calculateWidth(Set<Calculator> calculatorPath) {
        return Math.abs(calculateStartX(calculatorPath) - calculateEndX(calculatorPath));
    }

    @Override
    public float calculateHeight(Set<Calculator> calculatorPath) {
        return Math.abs(calculateStartY(calculatorPath) - calculateEndY(calculatorPath));
    }

    @Override
    public float calculateContentX(Set<Calculator> calculatorPath) {
        return calculateX(calculatorPath);
    }

    @Override
    public float calculateContentY(Set<Calculator> calculatorPath) {
        return calculateY(calculatorPath);
    }

    @Override
    public float calculateContentWidth(Set<Calculator> calculatorPath) {
        return calculateWidth(calculatorPath);
    }

    @Override
    public float calculateContentHeight(Set<Calculator> calculatorPath) {
        return calculateHeight(calculatorPath);
    }
}
