package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.*;
import com.github.vkuzel.simplepdflayout.property.*;
import com.github.vkuzel.simplepdflayout.renderer.BackgroundRenderer;
import com.github.vkuzel.simplepdflayout.renderer.BorderRenderer;
import com.github.vkuzel.simplepdflayout.renderer.ChildrenRenderer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.HEIGHT;
import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.WIDTH;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.X;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.Y;

public final class Box implements ParentElement<Box>, ChildElement<Box>, ElementWithMargin, ElementWithBorder, ElementWithPadding {

    private final ParentElement<?> parentElement;
    private final ChildElementCollection<Box> children;

    private final ContentPositionCalculator xContentPositionCalculator;
    private final ContentPositionCalculator yContentPositionCalculator;
    private final ContentDimensionCalculator widthContentDimensionCalculator;
    private final ContentDimensionCalculator heightContentDimensionCalculator;

    private final BorderRenderer borderRenderer;
    private final BackgroundRenderer backgroundRenderer;
    private final ChildrenRenderer childrenRenderer;

    private PositionCalculator xPositionCalculator;
    private PositionCalculator yPositionCalculator;
    private DimensionCalculator widthDimensionCalculator;
    private DimensionCalculator heightDimensionCalculator;

    private Margin margin = null;
    private Border border = null;
    private Padding padding = null;

    private Color backgroundColor;

    public Box(ParentElement<?> parentElement) {
        this.parentElement = parentElement;
        this.children = new ChildElementCollection<>(this);

        this.xContentPositionCalculator = new ContentPositionCalculator(this, X);
        this.yContentPositionCalculator = new ContentPositionCalculator(this, Y);
        this.widthContentDimensionCalculator = new ContentDimensionCalculator(this, WIDTH);
        this.heightContentDimensionCalculator = new ContentDimensionCalculator(this, HEIGHT);

        this.backgroundRenderer = new BackgroundRenderer(this);
        this.borderRenderer = new BorderRenderer(this);
        this.childrenRenderer = new ChildrenRenderer(this);

        setTopLeft(0, 0);
        setWidthPercent(100);
        setHeightOfChildren();
    }

    public Box setTopLeft(float x, float y) {
        return setX(x).setY(y);
    }

    public Box setTopLeftPercent(float xPercent, float yPercent) {
        return setXPercent(xPercent).setYPercent(yPercent);
    }

    public Box setX(float x) {
        xPositionCalculator = new FixedPositionCalculator(parentElement, X, x);
        return this;
    }

    public Box setXPercent(float xPercent) {
        xPositionCalculator = new PercentOfParentPositionCalculator(parentElement, X, xPercent);
        return this;
    }

    public Box setY(float y) {
        yPositionCalculator = new FixedPositionCalculator(parentElement, Y, y);
        return this;
    }

    public Box setYPercent(float yPercent) {
        yPositionCalculator = new PercentOfParentPositionCalculator(parentElement, Y, yPercent);
        return this;
    }

    public Box setDimension(float width, float height) {
        return setWidth(width).setHeight(height);
    }

    public Box setDimensionPercent(float widthPercent, float heightPercent) {
        return setWidthPercent(widthPercent).setHeightPercent(heightPercent);
    }

    public Box setDimensionOfChildren() {
        return setWidthOfChildren().setHeightOfChildren();
    }

    public Box setWidth(float width) {
        widthDimensionCalculator = new FixedDimensionCalculator(width);
        return this;
    }

    public Box setWidthPercent(float widthPercent) {
        widthDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, WIDTH, widthPercent);
        return this;
    }

    public Box setWidthOfChildren() {
        widthDimensionCalculator = new SizeOfChildrenDimensionCalculator(this, WIDTH);
        return this;
    }

    public Box setHeight(float height) {
        heightDimensionCalculator = new FixedDimensionCalculator(height);
        return this;
    }

    public Box setHeightPercent(float heightPercent) {
        heightDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, HEIGHT, heightPercent);
        return this;
    }

    public Box setHeightOfChildren() {
        heightDimensionCalculator = new SizeOfChildrenDimensionCalculator(this, HEIGHT);
        return this;
    }

    public Box setHorizontalPosition(XPosition xPosition, Element positionElement) {
        xPositionCalculator = new RelativeToElementPositionCalculator(this, xPosition, null, positionElement);
        return this;
    }

    public Box setVerticalPosition(YPosition yPosition, Element positionElement) {
        yPositionCalculator = new RelativeToElementPositionCalculator(this, null, yPosition, positionElement);
        return this;
    }

    public Box setMargin(float margin) {
        return setMargin(new Margin(margin));
    }

    public Box setMargin(Margin margin) {
        this.margin = margin;
        return this;
    }

    @Override
    public Margin getMargin() {
        return margin;
    }

    public Box setBorder(Line line) {
        return setBorder(new Border(line));
    }

    public Box setBorder(Border border) {
        this.border = border;
        return this;
    }

    @Override
    public Border getBorder() {
        return border;
    }

    public Box setPadding(float padding) {
        return setPadding(new Padding(padding));
    }

    public Box setPadding(Padding padding) {
        this.padding = padding;
        return this;
    }

    @Override
    public Padding getPadding() {
        return padding;
    }

    public Box setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    public <C extends ChildElement<C>> Box addChild(Function<Box, C> childFactory, Consumer<C> childConfigurer) {
        return children.addChild(childFactory, childConfigurer);
    }

    @Override
    public List<ChildElement<?>> getChildren() {
        return children.getChildren();
    }

    @Override
    public ChildElement<?> getPreviousChildTo(ChildElement<?> childElement) {
        return children.getPreviousChildTo(childElement);
    }

    @Override
    public ChildElement<?> getNextChildTo(ChildElement<?> childElement) {
        return children.getNextChildTo(childElement);
    }

    @Override
    public ParentElement<?> getParent() {
        return parentElement;
    }

    @Override
    public void render(PDDocument document, PDPageContentStream contentStream) {
        backgroundRenderer.render(contentStream, backgroundColor);
        borderRenderer.render(contentStream);
        childrenRenderer.render(document, contentStream);
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
