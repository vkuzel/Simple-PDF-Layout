package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import com.github.vkuzel.simplepdflayout.geometry.Point;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoxTest {

    private Page root;
    private Box parent;
    private Box box;

    @Before
    public void init() {
        box = new Box();
        parent = new Box()
                .setPadding(10)
                .addChild(box);
        root = new Page()
                .addChild(parent);
    }

    @Test
    public void calculateTopLeft_absolute() {
        box.setTopLeft(10, 10);
        Point topLeft = box.calculateTopLeft();
        Assert.assertEquals(20, topLeft.getX(), 0.001);
        Assert.assertEquals(20, topLeft.getY(), 0.001);
    }

    @Test
    public void calculateTopLeft_percent() {
        box.setTopLeftPercent(50, 50);
        Point topLeft = box.calculateTopLeft();
        float horizontalMiddleOfPage = root.page.getWidth() / 2;
        float verticalMiddleOfPage = root.page.getHeight() / 2;
        Assert.assertEquals(horizontalMiddleOfPage, topLeft.getX(), 0.001);
        Assert.assertEquals(verticalMiddleOfPage, topLeft.getY(), 0.001);
    }

    @Test
    public void calculateDimension_default() {
        Dimension parentContentDimension = parent.calculateContentDimension();
        Dimension dimension = box.calculateDimension();
        Assert.assertEquals(parentContentDimension.getWidth(), dimension.getWidth(), 0.001);
        Assert.assertEquals(parentContentDimension.getHeight(), dimension.getHeight(), 0.001);
    }

    @Test
    public void calculateDimension_percent() {
        box.setDimensionPercent(50, 50);
        Dimension parentContentDimension = parent.calculateContentDimension();
        Dimension dimension = box.calculateDimension();
        Assert.assertEquals(parentContentDimension.getWidth() / 2, dimension.getWidth(), 0.001);
        Assert.assertEquals(parentContentDimension.getHeight() / 2, dimension.getHeight(), 0.001);
    }
}
