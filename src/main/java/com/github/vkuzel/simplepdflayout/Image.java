package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.*;
import com.github.vkuzel.simplepdflayout.property.Dimension;
import com.github.vkuzel.simplepdflayout.property.Point;
import com.github.vkuzel.simplepdflayout.property.*;
import com.github.vkuzel.simplepdflayout.renderer.BackgroundRenderer;
import com.github.vkuzel.simplepdflayout.renderer.BorderRenderer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.HEIGHT;
import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.WIDTH;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.X;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.Y;

public final class Image implements ChildElement<Image>, ElementWithMargin, ElementWithBorder, ElementWithPadding, ElementWithBackground {

    private final ParentElement<?> parentElement;

    private final ContentPositionCalculator xContentPositionCalculator;
    private final ContentPositionCalculator yContentPositionCalculator;
    private final ContentDimensionCalculator widthContentDimensionCalculator;
    private final ContentDimensionCalculator heightContentDimensionCalculator;

    private final BorderRenderer borderRenderer;
    private final BackgroundRenderer backgroundRenderer;

    private PositionCalculator xPositionCalculator;
    private PositionCalculator yPositionCalculator;
    private DimensionCalculator widthDimensionCalculator;
    private DimensionCalculator heightDimensionCalculator;

    private Margin margin = null;
    private Border border = null;
    private Padding padding = null;

    private Color backgroundColor;
    private File imageFile;

    Image(ParentElement<?> parentElement) {
        this.parentElement = parentElement;

        this.xContentPositionCalculator = new ContentPositionCalculator(this, X);
        this.yContentPositionCalculator = new ContentPositionCalculator(this, Y);
        this.widthContentDimensionCalculator = new ContentDimensionCalculator(this, WIDTH);
        this.heightContentDimensionCalculator = new ContentDimensionCalculator(this, HEIGHT);

        this.borderRenderer = new BorderRenderer(this);
        this.backgroundRenderer = new BackgroundRenderer(this);

        setTopLeft(0, 0);
        setDimensionPercent(100, 100);
    }

    public Image setTopLeft(float x, float y) {
        return setX(x).setY(y);
    }

    public Image setTopLeftPercent(float xPercent, float yPercent) {
        return setXPercent(xPercent).setYPercent(yPercent);
    }

    public Image setX(float x) {
        xPositionCalculator = new FixedPositionCalculator(parentElement, X, x);
        return this;
    }

    public Image setXPercent(float xPercent) {
        xPositionCalculator = new PercentOfParentPositionCalculator(parentElement, X, xPercent);
        return this;
    }

    public Image setY(float y) {
        yPositionCalculator = new FixedPositionCalculator(parentElement, Y, y);
        return this;
    }

    public Image setYPercent(float yPercent) {
        yPositionCalculator = new PercentOfParentPositionCalculator(parentElement, Y, yPercent);
        return this;
    }

    public Image setDimension(float width, float height) {
        return setWidth(width).setHeight(height);
    }

    public Image setDimensionPercent(float widthPercent, float heightPercent) {
        return setWidthPercent(widthPercent).setHeightPercent(heightPercent);
    }

    public Image setWidth(float width) {
        widthDimensionCalculator = new FixedDimensionCalculator(width);
        return this;
    }

    public Image setWidthPercent(float widthPercent) {
        widthDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, WIDTH, widthPercent);
        return this;
    }

    public Image setHeight(float height) {
        heightDimensionCalculator = new FixedDimensionCalculator(height);
        return this;
    }

    public Image setHeightPercent(float heightPercent) {
        heightDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, HEIGHT, heightPercent);
        return this;
    }

    public Image setHorizontalPosition(XPosition xPosition, Element positionElement) {
        xPositionCalculator = new RelativeToElementPositionCalculator(parentElement, this, xPosition, null, positionElement);
        return this;
    }

    public Image setVerticalPosition(YPosition yPosition, Element positionElement) {
        yPositionCalculator = new RelativeToElementPositionCalculator(parentElement, this, null, yPosition, positionElement);
        return this;
    }

    public Image setMargin(float margin) {
        return setMargin(Margin.of(margin));
    }

    public Image setMargin(Margin margin) {
        this.margin = margin;
        return this;
    }

    @Override
    public Margin getMargin() {
        return margin;
    }

    public Image setBorder(Line line) {
        return setBorder(Border.of(line));
    }

    public Image setBorder(Border border) {
        this.border = border;
        return this;
    }

    @Override
    public Border getBorder() {
        return border;
    }

    public Image setPadding(float padding) {
        return setPadding(Padding.of(padding));
    }

    public Image setPadding(Padding padding) {
        this.padding = padding;
        return this;
    }

    @Override
    public Padding getPadding() {
        return padding;
    }

    public Image setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Image setImageFile(File imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    @Override
    public ParentElement<?> getParent() {
        return parentElement;
    }

    @Override
    public void render(PDDocument document, PDPageContentStream contentStream) {
        backgroundRenderer.render(contentStream);
        renderImage(document, contentStream);
        borderRenderer.render(contentStream);
    }

    private void renderImage(PDDocument document, PDPageContentStream contentStream) {
        if (!imageFile.exists()) {
            throw new IllegalStateException("Image file " + imageFile.getAbsolutePath() + " not found!");
        }

        Point topLeft = calculateContentTopLeft();
        Dimension dimension = calculateContentDimension();
        Point bottomLeft = Point.of(topLeft.getX(), topLeft.getY() + dimension.getHeight());
        Point pdfBottomLeft = convertPointToPdfCoordinates(bottomLeft);

        try {
            PDImageXObject image = PDImageXObject.createFromFileByContent(imageFile, document);
            contentStream.drawImage(image, pdfBottomLeft.getX(), pdfBottomLeft.getY(), dimension.getWidth(), dimension.getHeight());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public float calculateX(Set<Calculator> calculatorPath) {
        return xPositionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateY(Set<Calculator> calculatorPath) {
        return yPositionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateContentX(Set<Calculator> calculatorPath) {
        return xContentPositionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateContentY(Set<Calculator> calculatorPath) {
        return yContentPositionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateWidth(Set<Calculator> calculatorPath) {
        return widthDimensionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateHeight(Set<Calculator> calculatorPath) {
        return heightDimensionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateContentWidth(Set<Calculator> calculatorPath) {
        return widthContentDimensionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateContentHeight(Set<Calculator> calculatorPath) {
        return heightContentDimensionCalculator.calculate(calculatorPath);
    }
}
