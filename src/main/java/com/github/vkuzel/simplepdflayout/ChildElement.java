package com.github.vkuzel.simplepdflayout;

public interface ChildElement<T extends ChildElement> extends Element {

    T setParent(ParentElement parent);

    ParentElement getParent();
}
