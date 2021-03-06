package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.*;
import com.github.vkuzel.simplepdflayout.property.*;
import com.github.vkuzel.simplepdflayout.renderer.BackgroundRenderer;
import com.github.vkuzel.simplepdflayout.renderer.BorderRenderer;
import com.github.vkuzel.simplepdflayout.renderer.ChildrenRenderer;
import com.github.vkuzel.simplepdflayout.renderer.RenderingContext;
import com.github.vkuzel.simplepdflayout.util.ChildElementCollection;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.HEIGHT;
import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.WIDTH;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.X;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.Y;

public final class Box implements ParentElement<Box>, ChildElement<Box>, ElementWithMargin, ElementWithBorder, ElementWithPadding, ElementWithBackground {

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

    Box(ParentElement<?> parentElement) {
        this.parentElement = parentElement;
        this.children = new ChildElementCollection<>(this);

        this.xContentPositionCalculator = new ContentPositionCalculator(this, X);
        this.yContentPositionCalculator = new ContentPositionCalculator(this, Y);
        this.widthContentDimensionCalculator = new ContentDimensionCalculator(this, WIDTH);
        this.heightContentDimensionCalculator = new ContentDimensionCalculator(this, HEIGHT);

        this.borderRenderer = new BorderRenderer(this);
        this.backgroundRenderer = new BackgroundRenderer(this);
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
        xPositionCalculator = new RelativeToElementPositionCalculator(parentElement, this, xPosition, null, positionElement);
        return this;
    }

    public Box setVerticalPosition(YPosition yPosition, Element positionElement) {
        yPositionCalculator = new RelativeToElementPositionCalculator(parentElement, this, null, yPosition, positionElement);
        return this;
    }

    public Box setMargin(float margin) {
        return setMargin(Margin.of(margin));
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
        return setBorder(Border.of(line));
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
        return setPadding(Padding.of(padding));
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
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public <C extends ChildElement<C>> Box addChild(Function<ParentElement<?>, C> childFactory, Consumer<C> childConfigurer) {
        return children.addChild(childFactory, childConfigurer);
    }

    @Override
    public Box removeChild(ChildElement<?> childElement) {
        return children.removeChild(childElement);
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
    public void render(RenderingContext renderingContext) {
        backgroundRenderer.render(renderingContext);
        borderRenderer.render(renderingContext);
        childrenRenderer.render(renderingContext);
    }

    @Override
    public float calculateX(CalculationContext calculationContext) {
        return xPositionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateY(CalculationContext calculationContext) {
        return yPositionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentX(CalculationContext calculationContext) {
        return xContentPositionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentY(CalculationContext calculationContext) {
        return yContentPositionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateWidth(CalculationContext calculationContext) {
        return widthDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateHeight(CalculationContext calculationContext) {
        return heightDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentWidth(CalculationContext calculationContext) {
        return widthContentDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentHeight(CalculationContext calculationContext) {
        return heightContentDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public String toString() {
        return "Box@" + Integer.toHexString(hashCode()) + "{" +
                "parentElement=" + getClassName(parentElement) +
                ", children.size=" + children.getChildren().size() +
                ", xPositionCalculator=" + getClassName(xPositionCalculator) +
                ", yPositionCalculator=" + getClassName(yPositionCalculator) +
                ", widthDimensionCalculator=" + getClassName(widthDimensionCalculator) +
                ", heightDimensionCalculator=" + getClassName(heightDimensionCalculator) +
                '}';
    }

    private String getClassName(Object obj) {
        if (obj != null) {
            return obj.getClass().getSimpleName();
        } else {
            return "";
        }
    }
}
