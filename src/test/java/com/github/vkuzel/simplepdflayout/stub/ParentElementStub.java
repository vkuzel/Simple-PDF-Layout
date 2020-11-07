package com.github.vkuzel.simplepdflayout.stub;

import com.github.vkuzel.simplepdflayout.ChildElement;
import com.github.vkuzel.simplepdflayout.ParentElement;
import com.github.vkuzel.simplepdflayout.calculator.CalculationContext;
import com.github.vkuzel.simplepdflayout.renderer.RenderingContext;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ParentElementStub implements ParentElement<ParentElementStub> {

    private final float contentX;

    private ParentElementStub(float contentX) {
        this.contentX = contentX;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public <C extends ChildElement<C>> ParentElementStub addChild(Function<ParentElement<?>, C> childFactory, Consumer<C> childConfigurer) {
        return null;
    }

    @Override
    public ParentElementStub removeChild(ChildElement<?> childElement) {
        return null;
    }

    @Override
    public List<ChildElement<?>> getChildren() {
        return null;
    }

    @Override
    public ChildElement<?> getPreviousChildTo(ChildElement<?> childElement) {
        return null;
    }

    @Override
    public ChildElement<?> getNextChildTo(ChildElement<?> childElement) {
        return null;
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
        return 0;
    }

    @Override
    public float calculateHeight(CalculationContext calculationContext) {
        return 0;
    }

    @Override
    public float calculateContentX(CalculationContext calculationContext) {
        return contentX;
    }

    @Override
    public float calculateContentY(CalculationContext calculationContext) {
        return 0;
    }

    @Override
    public float calculateContentWidth(CalculationContext calculationContext) {
        return 0;
    }

    @Override
    public float calculateContentHeight(CalculationContext calculationContext) {
        return 0;
    }

    @Override
    public void render(RenderingContext renderingContext) {

    }

    public static class Builder {

        private float contentX = 0;

        private Builder() {
        }

        public Builder setContentX(float contentX) {
            this.contentX = contentX;
            return this;
        }

        public ParentElementStub build() {
            return new ParentElementStub(contentX);
        }
    }
}
