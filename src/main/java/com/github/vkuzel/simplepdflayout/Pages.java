package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.CalculationContext;
import com.github.vkuzel.simplepdflayout.property.Dimension;
import com.github.vkuzel.simplepdflayout.property.Padding;
import com.github.vkuzel.simplepdflayout.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Pages implements ParentElement<Pages>, ElementWithPadding {

    private final Dimension dimension;
    private final List<Page> pages = new ArrayList<>();

    private Padding padding = null;

    private Pages(Dimension dimension) {
        this.dimension = dimension;
    }

    public static Pages of(Dimension dimension) {
        return new Pages(dimension);
    }

    public static Pages a4() {
        return of(Dimension.a4());
    }

    @Override
    public Padding getPadding() {
        return padding;
    }

    public Pages setPadding(float padding) {
        return setPadding(Padding.of(padding));
    }

    public Pages setPadding(Padding padding) {
        this.padding = padding;
        return this;
    }

    private Page getCurrentPage() {
        if (!pages.isEmpty()) {
            return pages.get(pages.size() - 1);
        } else {
            throw new IllegalStateException("Empty pages!");
        }
    }

    public List<Page> getPages() {
        return Collections.unmodifiableList(pages);
    }

    @Override
    public void render(RenderingContext renderingContext) {
        for (Page page : pages) {
            page.render(renderingContext);
        }
    }

    @Override
    public <C extends ChildElement<C>> Pages addChild(Function<ParentElement<?>, C> childFactory, Consumer<C> childConfigurer) {
        Page currentPage;
        if (pages.isEmpty()) {
            currentPage = createPage();
        } else {
            currentPage = getCurrentPage();
        }
        currentPage.addChild(childFactory, childConfigurer);

        float pageChildrenHeight = calculateChildrenHeight(currentPage);
        float pageMaxContentHeight = calculateMaxContentHeight();
        if (pageChildrenHeight > pageMaxContentHeight) {
            currentPage.removeChild(currentPage.getChildren().get(currentPage.getChildren().size() - 1));
            createPage().addChild(childFactory, childConfigurer);
        }

        return this;
    }

    private Page createPage() {
        Page page = Page.of(dimension)
                .setPadding(padding);
        pages.add(page);
        return page;
    }

    private float calculateChildrenHeight(Page page) {
        CalculationContext calculationContext = new CalculationContext();
        float height = 0;
        for (ChildElement<?> child : page.getChildren()) {
            height += child.calculateHeight(calculationContext);
        }
        return height;
    }

    private float calculateMaxContentHeight() {
        float height = dimension.getHeight();
        if (padding != null) {
            height -= padding.getTop() + padding.getBottom();
        }
        return height;
    }

    @Override
    public Pages removeChild(ChildElement<?> childElement) {
        getCurrentPage().removeChild(childElement);
        return this;
    }

    @Override
    public List<ChildElement<?>> getChildren() {
        return getCurrentPage().getChildren();
    }

    @Override
    public ChildElement<?> getPreviousChildTo(ChildElement<?> childElement) {
        return getCurrentPage().getPreviousChildTo(childElement);
    }

    @Override
    public ChildElement<?> getNextChildTo(ChildElement<?> childElement) {
        return getCurrentPage().getNextChildTo(childElement);
    }

    @Override
    public float calculateX(CalculationContext calculationContext) {
        return getCurrentPage().calculateX(calculationContext);
    }

    @Override
    public float calculateY(CalculationContext calculationContext) {
        return getCurrentPage().calculateY(calculationContext);
    }

    @Override
    public float calculateWidth(CalculationContext calculationContext) {
        return getCurrentPage().calculateWidth(calculationContext);
    }

    @Override
    public float calculateHeight(CalculationContext calculationContext) {
        return getCurrentPage().calculateHeight(calculationContext);
    }

    @Override
    public float calculateContentX(CalculationContext calculationContext) {
        return getCurrentPage().calculateContentX(calculationContext);
    }

    @Override
    public float calculateContentY(CalculationContext calculationContext) {
        return getCurrentPage().calculateContentY(calculationContext);
    }

    @Override
    public float calculateContentWidth(CalculationContext calculationContext) {
        return getCurrentPage().calculateWidth(calculationContext);
    }

    @Override
    public float calculateContentHeight(CalculationContext calculationContext) {
        return getCurrentPage().calculateContentHeight(calculationContext);
    }
}
