package com.github.vkuzel.simplepdflayout;

/**
 * Element can be added to ParentElement container.
 *
 * @see ParentElement
 */
public interface ChildElement<C extends ChildElement<C>> extends Element {

    ParentElement<?> getParent();

    default ChildElement<?> getPrevious() {
        return getParent().getPreviousChildTo(this);
    }

    default ChildElement<?> getNext() {
        return getParent().getNextChildTo(this);
    }
}
