package com.github.vkuzel.simplepdflayout.util;

import com.github.vkuzel.simplepdflayout.Element;

import java.util.function.Function;

public final class Utils {

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
