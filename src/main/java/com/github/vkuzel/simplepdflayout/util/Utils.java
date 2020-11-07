package com.github.vkuzel.simplepdflayout.util;

import com.github.vkuzel.simplepdflayout.Element;
import com.github.vkuzel.simplepdflayout.ElementWithBorder;
import com.github.vkuzel.simplepdflayout.ElementWithMargin;
import com.github.vkuzel.simplepdflayout.ElementWithPadding;
import com.github.vkuzel.simplepdflayout.property.Margin;
import com.github.vkuzel.simplepdflayout.property.Padding;

import java.util.function.Function;

public final class Utils {

    public static float sumHorizontalMarginBorderPadding(Element element) {
        return sumRightMarginBorderPadding(element) + sumLeftMarginBorderPadding(element);
    }

    public static float sumVerticalMarginBorderPadding(Element element) {
        return sumTopMarginBorderPadding(element) + sumBottomMarginBorderPadding(element);
    }

    public static float sumTopMarginBorderPadding(Element element) {
        float dimension = 0;
        dimension += getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getTop);
        dimension += getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getTop().getWidth());
        dimension += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getTop);
        return dimension;
    }

    public static float sumRightMarginBorderPadding(Element element) {
        float dimension = 0;
        dimension += getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getRight);
        dimension += getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getRight().getWidth());
        dimension += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getRight);
        return dimension;

    }

    public static float sumBottomMarginBorderPadding(Element element) {
        float dimension = 0;
        dimension += getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getBottom);
        dimension += getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getBottom().getWidth());
        dimension += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getBottom);
        return dimension;
    }

    public static float sumLeftMarginBorderPadding(Element element) {
        float dimension = 0;
        dimension += getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getLeft);
        dimension += getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getLeft().getWidth());
        dimension += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getLeft);
        return dimension;
    }

    @SuppressWarnings("unchecked")
    public static <T, P> float getValue(Element element, Class<T> elementType, Function<T, P> mapToProperty, Function<P, Float> mapToValue) {
        if (!elementType.isInstance(element)) {
            return 0;
        }
        P property = mapToProperty.apply((T) element);
        if (property == null) {
            return 0;
        }
        return mapToValue.apply(property);
    }
}
