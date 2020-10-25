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

    <C extends ChildElement<C>> P addChild(Function<ParentElement<?>, C> childFactory, Consumer<C> childConfigurer);

    P removeChild(ChildElement<?> childElement);

    List<ChildElement<?>> getChildren();

    ChildElement<?> getPreviousChildTo(ChildElement<?> childElement);

    ChildElement<?> getNextChildTo(ChildElement<?> childElement);
}
