package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.*;
import com.github.vkuzel.simplepdflayout.property.Dimension;
import com.github.vkuzel.simplepdflayout.property.Padding;
import com.github.vkuzel.simplepdflayout.property.Point;
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

public final class Page implements ParentElement<Page>, ElementWithPadding {

    private final Dimension dimension;
    private final ChildElementCollection<Page> children;

    private final DimensionCalculator widthDimensionCalculator;
    private final DimensionCalculator heightDimensionCalculator;
    private final ContentPositionCalculator xContentPositionCalculator;
    private final ContentPositionCalculator yContentPositionCalculator;
    private final ContentDimensionCalculator widthContentDimensionCalculator;
    private final ContentDimensionCalculator heightContentDimensionCalculator;

    private final ChildrenRenderer childrenRenderer;

    private Padding padding = null;
    private boolean rainbow = false;

    private Page(Dimension dimension) {
        this.dimension = dimension;
        this.children = new ChildElementCollection<>(this);

        this.widthDimensionCalculator = new FixedDimensionCalculator(dimension.getWidth());
        this.heightDimensionCalculator = new FixedDimensionCalculator(dimension.getHeight());
        this.xContentPositionCalculator = new ContentPositionCalculator(this, X);
        this.yContentPositionCalculator = new ContentPositionCalculator(this, Y);
        this.widthContentDimensionCalculator = new ContentDimensionCalculator(this, WIDTH);
        this.heightContentDimensionCalculator = new ContentDimensionCalculator(this, HEIGHT);

        this.childrenRenderer = new ChildrenRenderer(this);
    }

    public static Page of(Dimension dimension) {
        return new Page(dimension);
    }

    public static Page a4() {
        return of(Dimension.a4());
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Page setPadding(float padding) {
        return setPadding(Padding.of(padding));
    }

    public Page setPadding(Padding padding) {
        this.padding = padding;
        return this;
    }

    @Override
    public Padding getPadding() {
        return padding;
    }

    public Page rainbow() {
        this.rainbow = true;
        return this;
    }

    @Override
    public <C extends ChildElement<C>> Page addChild(Function<ParentElement<?>, C> childFactory, Consumer<C> childConfigurer) {
        return children.addChild(childFactory, childConfigurer);
    }

    @Override
    public Page removeChild(ChildElement<?> childElement) {
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
    public void render(RenderingContext renderingContext) {
        if (rainbow) {
            setRainbowToChildrenRecursively(1, children.getChildren());
        }
        childrenRenderer.render(renderingContext);
    }

    private void setRainbowToChildrenRecursively(int level, List<ChildElement<?>> children) {
        for (int i = 0; i < children.size(); i++) {
            float mod = i % 6;
            float r = mod >= 3 ? 1f / level : 0;
            float g = mod % 2 == 0 ? 1f / level : 0;
            float b = mod <= 1 || mod >= 5 ? 1f / level : 0;

            ChildElement<?> child = children.get(i);

            if (child instanceof Box) {
                ((Box) child).setBackgroundColor(new Color(r, g, b));
            } else if (child instanceof Text) {
                ((Text) child).setBackgroundColor(new Color(r, g, b));
            }

            if (child instanceof ParentElement) {
                ParentElement<?> parent = (ParentElement<?>) child;
                setRainbowToChildrenRecursively(level + 1, parent.getChildren());
            }
        }
    }

    @Override
    public float calculateX(CalculationContext calculationContext) {
        return 0;
    }

    @Override
    public float calculateY(CalculationContext calculationContext) {
        return 0;
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
    public float calculateContentX(CalculationContext calculationContext) {
        return xContentPositionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentY(CalculationContext calculationContext) {
        return yContentPositionCalculator.calculate(calculationContext);
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
    public Point convertPointToPdfCoordinates(Point point) {
        return Point.of(point.getX(), dimension.getHeight() - point.getY());
    }
}
