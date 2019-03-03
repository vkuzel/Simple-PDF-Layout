package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import com.github.vkuzel.simplepdflayout.geometry.Point;

import java.util.Collection;
import java.util.List;

public interface ParentElement<T extends ParentElement> extends Element {

    T addChild(ChildElement child);

    T addChildren(Collection<ChildElement> children);

    List<ChildElement> getChildren();

    Point calculateContentTopLeft();

    Dimension calculateContentDimension();

    Point convertPointToPdfCoordinates(Point point);
}
