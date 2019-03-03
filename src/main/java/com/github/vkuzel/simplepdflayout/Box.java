package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import com.github.vkuzel.simplepdflayout.geometry.Point;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

//import java.awt.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Box<T extends Box> implements ParentElement<T>, ChildElement {

    protected ParentElement parent;
    protected final List<ChildElement> children = new ArrayList<>();

    protected float x = 0;
    protected Float xPercent;
    protected float y = 0;
    protected Float yPercent;
    protected Float width;
    protected Float widthPercent;
    protected Float height;
    protected Float heightPercent;

    protected float paddingTop = 0;
    protected float paddingRight = 0;
    protected float paddingBottom = 0;
    protected float paddingLeft = 0;

    protected Line borderTop = new Line();
    protected Line borderRight = new Line();
    protected Line borderBottom = new Line();
    protected Line borderLeft = new Line();

    protected float marginTop = 0;
    protected float marginRight = 0;
    protected float marginLeft = 0;
    protected float marginBottom = 0;

    protected Color backgroundColor;

    @SuppressWarnings("unchecked")
    protected T getThis() {
        return (T) this;
    }

    public T addChild(ChildElement child) {
        return addChildren(Collections.singleton(child));
    }

    @Override
    public T addChildren(Collection<ChildElement> children) {
        for (ChildElement child : children) {
            if (child.getParent() != null) {
                throw new IllegalArgumentException("Child " + child.getClass().getSimpleName() + " already has a parent, cannot be put into another!");
            }
            child.setParent(this);
            this.children.add(child);
        }
        return getThis();
    }

    @Override
    public List<ChildElement> getChildren() {
        return children;
    }

    public T setParent(ParentElement parent) {
        this.parent = parent;
        return getThis();
    }

    public ParentElement getParent() {
        return this.parent;
    }

    public T setTopLeft(float x, float y) {
        setX(x).setY(y);
        return getThis();
    }

    public T setTopLeftPercent(float xPercent, float yPercent) {
        setXPercent(xPercent).setYPercent(yPercent);
        return getThis();
    }

    public T setX(float x) {
        this.x = x;
        this.xPercent = null;
        return getThis();
    }

    public T setXPercent(float xPercent) {
        this.xPercent = xPercent;
        return getThis();
    }

    public T setY(float y) {
        this.y = y;
        this.yPercent = null;
        return getThis();
    }

    public T setYPercent(float yPercent) {
        this.yPercent = yPercent;
        return getThis();
    }

    public T setDimension(float width, float height) {
        setWidth(width).setHeight(height);
        return getThis();
    }

    public T setDimensionPercent(float widthPercent, float heightPercent) {
        setWidthPercent(widthPercent).setHeightPercent(heightPercent);
        return getThis();
    }

    public T setWidth(float width) {
        this.width = width;
        this.widthPercent = null;
        return getThis();
    }

    public T setWidthPercent(float widthPercent) {
        this.widthPercent = widthPercent;
        return getThis();
    }

    public T setHeight(float height) {
        this.height = height;
        this.heightPercent = null;
        return getThis();
    }

    public T setHeightPercent(float heightPercent) {
        this.heightPercent = heightPercent;
        return getThis();
    }

    public T setMargin(float margin) {
        this.marginLeft = this.marginBottom = this.marginRight = this.marginTop = margin;
        return getThis();
    }

    public T setMarginTop(float marginTop) {
        this.marginTop = marginTop;
        return getThis();
    }

    public T setMarginRight(float marginRight) {
        this.marginRight = marginRight;
        return getThis();
    }

    public T setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
        return getThis();
    }

    public T setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
        return getThis();
    }

    public T setBorder(Line line) {
        validateNotNull(line);
        this.borderTop = line;
        this.borderRight = line;
        this.borderBottom = line;
        this.borderLeft = line;
        return getThis();
    }

    public T setBorderTop(Line line) {
        validateNotNull(line);
        this.borderTop = line;
        return getThis();
    }

    public T setBorderRight(Line line) {
        validateNotNull(line);
        this.borderRight = line;
        return getThis();
    }

    public T setBorderBottom(Line line) {
        validateNotNull(line);
        this.borderBottom = line;
        return getThis();
    }

    public T setBorderLeft(Line line) {
        validateNotNull(line);
        this.borderLeft = line;
        return getThis();
    }

    public T setPadding(float padding) {
        this.paddingLeft = this.paddingBottom = this.paddingRight = this.paddingTop = padding;
        return getThis();
    }

    public T setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
        return getThis();
    }

    public T setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
        return getThis();
    }

    public T setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
        return getThis();
    }

    public T setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
        return getThis();
    }

    public T setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return getThis();
    }

    public void draw(PDDocument document, PDPageContentStream contentStream) {
        validateParent();
        drawBackground(contentStream);
        drawBorders(contentStream);
        drawChildren(document, contentStream);
    }

    protected void drawBackground(PDPageContentStream contentStream) {
        if (backgroundColor == null) {
            return;
        }

        Point topLeft = calculateBackgroundTopLeft();
        Dimension dimension = calculateBackgroundDimension();
        Point bottomLeft = new Point(topLeft.getX(), topLeft.getY() + dimension.getHeight());
        Point pdfBottomLeft = convertPointToPdfCoordinates(bottomLeft);

        try {
            contentStream.addRect(pdfBottomLeft.getX(), pdfBottomLeft.getY(), dimension.getWidth(), dimension.getHeight());
            contentStream.setNonStrokingColor(backgroundColor);
            contentStream.fill();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void drawBorders(PDPageContentStream contentStream) {
        Point topLeft = calculateBorderTopLeft();
        Point bottomRight = calculateBorderBottomRight();
        Point topRight = new Point(bottomRight.getX(), topLeft.getY());
        Point bottomLeft = new Point(topLeft.getX(), bottomRight.getY());

        if (borderTop.getWidth() > 0) {
            Point topStart = new Point(topLeft.getX() - borderLeft.getWidth() / 2, topLeft.getY());
            Point topEnd = new Point(topRight.getX() + borderRight.getWidth() / 2, topRight.getY());
            drawLine(borderTop, topStart, topEnd, contentStream);
        }
        if (borderRight.getWidth() > 0) {
            Point rightStart = new Point(topRight.getX(), topRight.getY() - borderTop.getWidth() / 2);
            Point rightEnd = new Point(bottomRight.getX(), bottomRight.getY() + borderBottom.getWidth() / 2);
            drawLine(borderRight, rightStart, rightEnd, contentStream);
        }
        if (borderBottom.getWidth() > 0) {
            Point bottomStart = new Point(bottomRight.getX() + borderRight.getWidth() / 2, bottomRight.getY());
            Point bottomEnd = new Point(bottomLeft.getX() - borderLeft.getWidth() / 2, bottomLeft.getY());
            drawLine(borderBottom, bottomStart, bottomEnd, contentStream);
        }
        if (borderLeft.getWidth() > 0) {
            Point leftStart = new Point(bottomLeft.getX(), bottomLeft.getY() + borderBottom.getWidth() / 2);
            Point leftEnd = new Point(topLeft.getX(), topLeft.getY() - borderTop.getWidth() / 2);
            drawLine(borderLeft, leftStart, leftEnd, contentStream);
        }
    }

    protected void drawLine(Line line, Point start, Point end, PDPageContentStream contentStream) {
        Point pdfStart = convertPointToPdfCoordinates(start);
        Point pdfEnd = convertPointToPdfCoordinates(end);

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

    protected void drawChildren(PDDocument document, PDPageContentStream contentStream) {
        for (ChildElement child : children) {
            child.draw(document, contentStream);
        }
    }

    protected Point calculateTopLeft() {
        validateParent();
        Point parentOffset = parent.calculateContentTopLeft();
        Dimension parentContentDimension = parent.calculateContentDimension();
        float rx, ry;

        if (xPercent != null) {
            rx = parentOffset.getX() + xPercent * parentContentDimension.getWidth() / 100;
        } else {
            rx = parentOffset.getX() + x;
        }

        if (yPercent != null) {
            ry = parentOffset.getY() + yPercent * parentContentDimension.getHeight() / 100;
        } else {
            ry = parentOffset.getY() + y;
        }

        return new Point(rx, ry);
    }

    protected Dimension calculateDimension() {
        validateParent();
        Dimension parentContentDimension = parent.calculateContentDimension();
        float rWidth, rHeight;

        if (widthPercent != null) {
            rWidth = widthPercent * parentContentDimension.getWidth() / 100;
        } else if (width != null) {
            rWidth = width;
        } else {
            rWidth = parentContentDimension.getWidth();
        }

        if (heightPercent != null) {
            rHeight = heightPercent * parentContentDimension.getHeight() / 100;
        } else if (height != null) {
            rHeight = height;
        } else {
            rHeight = parentContentDimension.getHeight();
        }

        return new Dimension(rWidth, rHeight);
    }

    public Point calculateContentTopLeft() {
        Point topLeft = calculateTopLeft();
        return new Point(
                topLeft.getX() + marginLeft + borderLeft.getWidth() + paddingRight,
                topLeft.getY() + marginTop + borderTop.getWidth() + paddingTop
        );
    }

    public Dimension calculateContentDimension() {
        float horizontalBorderWidth = borderLeft.getWidth() + borderRight.getWidth();
        float verticalBorderWidth = borderTop.getWidth() + borderBottom.getWidth();
        Dimension dimension = calculateDimension();
        return new Dimension(
                dimension.getWidth() - marginLeft - marginRight - paddingLeft - paddingRight - horizontalBorderWidth,
                dimension.getHeight() - marginTop - marginBottom - paddingTop - paddingBottom - verticalBorderWidth
        );
    }

    protected Point calculateBackgroundTopLeft() {
        Point contentTopLeft = calculateContentTopLeft();
        return new Point(
                contentTopLeft.getX() - paddingLeft,
                contentTopLeft.getY() - paddingTop
        );
    }

    protected Dimension calculateBackgroundDimension() {
        Dimension contentDimension = calculateContentDimension();
        return new Dimension(
                contentDimension.getWidth() + paddingLeft + paddingRight,
                contentDimension.getHeight() + paddingTop + paddingBottom
        );
    }

    protected Point calculateBorderTopLeft() {
        Point topLeft = calculateTopLeft();
        return new Point(
                topLeft.getX() + marginLeft + borderLeft.getWidth() / 2,
                topLeft.getY() + marginTop + borderTop.getWidth() / 2
        );
    }

    protected Point calculateBorderBottomRight() {
        Point topLeft = calculateTopLeft();
        Dimension dimension = calculateDimension();
        return new Point(
                topLeft.getX() + dimension.getWidth() - marginRight - borderRight.getWidth() / 2,
                topLeft.getY() + dimension.getHeight() - marginBottom - borderBottom.getWidth() / 2
        );
    }

    @Override
    public Point convertPointToPdfCoordinates(Point point) {
        return parent.convertPointToPdfCoordinates(point);
    }

    protected void validateParent() {
        if (parent == null) {
            throw new IllegalStateException("Parent not found for element " + this.getClass().getSimpleName());
        }
    }

    protected void validateNotNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null!");
        }
    }

    public String toStringCoordinates() {
        Point topLeft = calculateTopLeft();
        Dimension dimension = calculateDimension();
        return "[x, y], [width, height] = " + topLeft + ", " + dimension +
                ", [pdfX, pdfY], [pdfWidth, pdfHeight] = " + convertPointToPdfCoordinates(topLeft) + ", " + dimension;
    }
}
