package com.github.vkuzel.simplepdflayout;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Element container can hold ChildElements.
 *
 * @see ChildElement
 */
public interface ParentElement<P extends ParentElement<P>> extends Element {

    <C extends ChildElement<C>> P addChild(Function<P, C> childFactory, Consumer<C> childConfigurer);

    List<ChildElement<?>> getChildren();

    ChildElement<?> getPreviousChildTo(ChildElement<?> childElement);

    ChildElement<?> getNextChildTo(ChildElement<?> childElement);
}
