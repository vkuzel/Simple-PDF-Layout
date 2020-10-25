package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.Calculator;
import com.github.vkuzel.simplepdflayout.property.Padding;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.apache.pdfbox.pdmodel.common.PDRectangle.A4;

public final class Pages implements ParentElement<Pages>, ElementWithPadding {

    private final PDRectangle dimension;
    private final List<Page> pages = new ArrayList<>();

    private Padding padding = null;

    public Pages(PDRectangle dimension) {
        this.dimension = dimension;
    }

    public static Pages a4() {
        return new Pages(A4);
    }

    @Override
    public Padding getPadding() {
        return padding;
    }

    public Pages setPadding(float padding) {
        return setPadding(new Padding(padding));
    }

    public Pages setPadding(Padding padding) {
        this.padding = padding;
        return this;
    }

    private Page getCurrentPage() {
        return !pages.isEmpty() ? pages.get(pages.size() - 1) : null;
    }

    public List<Page> getPages() {
        return Collections.unmodifiableList(pages);
    }

    @Override
    public void render(PDDocument document, PDPageContentStream contentStream) {
        for (Page page : pages) {
            page.render(document, contentStream);
        }
    }

    @Override
    public <C extends ChildElement<C>> Pages addChild(Function<ParentElement<?>, C> childFactory, Consumer<C> childConfigurer) {
        Page currentPage = getCurrentPage();
        if (currentPage == null) {
            currentPage = createPage();
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
        Page page = new Page(dimension)
                .setPadding(padding);
        pages.add(page);
        return page;
    }

    private float calculateChildrenHeight(Page page) {
        float height = 0;
        for (ChildElement<?> child : page.getChildren()) {
            height += child.calculateHeight(new HashSet<>());
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
    public float calculateX(Set<Calculator> calculatorPath) {
        return getCurrentPage().calculateX(calculatorPath);
    }

    @Override
    public float calculateY(Set<Calculator> calculatorPath) {
        return getCurrentPage().calculateY(calculatorPath);
    }

    @Override
    public float calculateWidth(Set<Calculator> calculatorPath) {
        return getCurrentPage().calculateWidth(calculatorPath);
    }

    @Override
    public float calculateHeight(Set<Calculator> calculatorPath) {
        return getCurrentPage().calculateHeight(calculatorPath);
    }

    @Override
    public float calculateContentX(Set<Calculator> calculatorPath) {
        return getCurrentPage().calculateContentX(calculatorPath);
    }

    @Override
    public float calculateContentY(Set<Calculator> calculatorPath) {
        return getCurrentPage().calculateContentY(calculatorPath);
    }

    @Override
    public float calculateContentWidth(Set<Calculator> calculatorPath) {
        return getCurrentPage().calculateWidth(calculatorPath);
    }

    @Override
    public float calculateContentHeight(Set<Calculator> calculatorPath) {
        return getCurrentPage().calculateContentHeight(calculatorPath);
    }
}
