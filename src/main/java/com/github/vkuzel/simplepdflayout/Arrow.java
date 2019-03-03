package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import com.github.vkuzel.simplepdflayout.geometry.Point;
import com.github.vkuzel.simplepdflayout.geometry.Vector;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

public class Arrow<T extends Arrow> implements ChildElement<Arrow> {

    protected ParentElement parent;
    protected float startX = 0;
    protected float startY = 0;
    protected Float endX;
    protected Float endY;
    protected Box startElement;
    protected boolean startArrow = true;
    protected boolean endArrow = false;
    protected Line line = new Line().setWidth(3).setColor(Color.RED);

    @SuppressWarnings("unchecked")
    protected T getThis() {
        return (T) this;
    }

    @Override
    public Arrow setParent(ParentElement parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public ParentElement getParent() {
        return parent;
    }

    public T setStartPoint(Point point) {
        return setStartPosition(point.getX(), point.getY());
    }

    public T setStartPosition(float x, float y) {
        this.startX = x;
        this.startY = y;
        return getThis();
    }

    public T setEndPoint(Point point) {
        return setEndPosition(point.getX(), point.getY());
    }

    public T setEndPosition(float x, float y) {
        this.endX = x;
        this.endY = y;
        return getThis();
    }

    public T setStartElement(Box startElement) {
        this.startElement = startElement;
        return getThis();
    }

    public T setStartArrow(boolean startArrow) {
        this.startArrow = startArrow;
        return getThis();
    }

    public T setEndArrow(boolean endArrow) {
        this.endArrow = endArrow;
        return getThis();
    }

    public T setLine(Line line) {
        this.line = line;
        return getThis();
    }

    @Override
    public void draw(PDDocument document, PDPageContentStream contentStream) {
        try {
            Point pdfStart = parent.convertPointToPdfCoordinates(calculateStart());
            Point pdfEnd = parent.convertPointToPdfCoordinates(calculateEnd());
            float arrowSize = line.getWidth() * 5.5f;

            if (startArrow) {
                Vector v = Vector.ofLineSegment(pdfStart, pdfEnd).normalize();
                Point p = new Point(pdfStart.getX() + v.getX() * arrowSize, pdfStart.getY() + v.getY() * arrowSize);

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
                Point p = new Point(pdfEnd.getX() + v.getX() * arrowSize, pdfEnd.getY() + v.getY() * arrowSize);

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

    protected Point calculateStart() {
        if (startElement != null) {
            Point topLeft = startElement.calculateTopLeft();
            Dimension dimension = startElement.calculateDimension();
            return new Point(topLeft.getX() + dimension.getWidth(), topLeft.getY() + dimension.getHeight());
        } else {
            return new Point(startX, startY);
        }
    }

    protected Point calculateEnd() {
        Point start = calculateStart();

        Dimension pageDimension = null;
        Float length = null;
        if (endX == null || endY == null) {
            pageDimension = getRootElement().calculateContentDimension();
            length = line.getWidth() * 20;
        }

        float rx, ry;

        if (endX == null) {
            rx = start.getX() + length;
            if (rx > pageDimension.getWidth()) {
                rx = start.getX() - length;
            }
        } else {
            rx = endX;
        }

        if (endY == null) {
            ry = start.getY() + length;
            if (ry > pageDimension.getHeight()) {
                ry = start.getY() - length;
            }
        } else {
            ry = endY;
        }

        return new Point(rx, ry);
    }

    protected ParentElement getRootElement() {
        ParentElement parent = this.getParent();
        while (parent instanceof ChildElement) {
            parent = ((ChildElement) parent).getParent();
        }
        return parent;
    }

    public String toStringCoordinates() {
        Point start = calculateStart();
        Point end = calculateEnd();
        return "[startX, startY], [endX, endY] = " + start + ", " + end +
                ", [pdfStartX, pdfStartY], [pdfEndX, pdfEndY] = " + parent.convertPointToPdfCoordinates(start) + ", " + parent.convertPointToPdfCoordinates(end);
    }
}
