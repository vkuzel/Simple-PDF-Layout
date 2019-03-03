package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import com.github.vkuzel.simplepdflayout.geometry.Point;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Page<T extends Page<T>> implements ParentElement<T> {

    protected PDRectangle page = PDRectangle.A4;
    protected final List<ChildElement> children = new ArrayList<>();
    protected boolean rainbow = false;

    @SuppressWarnings("unchecked")
    protected T getThis() {
        return (T) this;
    }

    public T setPage(PDRectangle page) {
        this.page = page;
        return getThis();
    }

    public T rainbow() {
        this.rainbow = true;
        return getThis();
    }

    @Override
    public T addChild(ChildElement child) {
        return addChildren(Collections.singleton(child));
    }

    @Override
    public T addChildren(Collection<ChildElement> children) {
        for (ChildElement child : children) {
            if (child.getParent() != null) {
                throw new IllegalArgumentException("Child " + child.getClass().getSimpleName() + " already has a parent, cannot be put into another!");
            }
            child.setParent(this);
            this.children.add(child);
        }
        return getThis();
    }

    @Override
    public List<ChildElement> getChildren() {
        return children;
    }

    @Override
    public void draw(PDDocument document, PDPageContentStream contentStream) {
        if (rainbow) {
            setRainbowToChildrenRecursively(1, children);
        }

        for (ChildElement child : children) {
            child.draw(document, contentStream);
        }
    }

    @SuppressWarnings("unchecked")
    protected void setRainbowToChildrenRecursively(int level, List<ChildElement> children) {
        for (int i = 0; i < children.size(); i++) {
            ChildElement child = children.get(i);
            if (child instanceof Box) {
                Box box = (Box) child;
                float mod = i % 6;
                float r = mod >= 3 ? 1f / level : 0;
                float g = mod % 2 == 0 ? 1f / level : 0;
                float b = mod <= 1 || mod >= 5 ? 1f / level : 0;
                box.setBackgroundColor(new Color(r, g, b));
            }
            if (child instanceof ParentElement) {
                ParentElement parent = (ParentElement) child;
                setRainbowToChildrenRecursively(level + 1, parent.getChildren());
            }
        }
    }

    @Override
    public Point calculateContentTopLeft() {
        return new Point(0, 0);
    }

    @Override
    public Dimension calculateContentDimension() {
        return new Dimension(page.getWidth(), page.getHeight());
    }

    @Override
    public Point convertPointToPdfCoordinates(Point point) {
        return new Point(point.getX(), page.getHeight() - point.getY());
    }

    @Override
    public String toStringCoordinates() {
        return "[width, height] = " + calculateContentDimension();
    }
}
