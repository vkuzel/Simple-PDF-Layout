package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import com.github.vkuzel.simplepdflayout.geometry.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoxTest {

    private static final Dimension PAGE_DIMENSION = new Dimension(100, 100);
    private static final float PAGE_PADDING = 10;
    private static final Dimension PAGE_CONTENT_DIMENSION = new Dimension(
            PAGE_DIMENSION.getWidth() - 2 * PAGE_PADDING,
            PAGE_DIMENSION.getHeight() - 2 * PAGE_PADDING
    );

    private Page page;
    private Box box;

    @BeforeEach
    public void init() {
        page = new Page(PAGE_DIMENSION)
                .setPadding(PAGE_PADDING)
                .addChild(Box::new, box -> this.box = box);
    }

    @Test
    public void absoluteBoxPositionIsCorrectlyCalculated() {
        // given
        box.setTopLeft(10, 10);

        // when
        Point topLeft = box.calculateTopLeft();

        // then
        assertEquals(20, topLeft.getX(), 0.001);
        assertEquals(20, topLeft.getY(), 0.001);
    }

    @Test
    public void fiftyPercentBoxPositionIsInTheMiddleOfPage() {
        // given
        box.setTopLeftPercent(50, 50);

        // when
        Point topLeft = box.calculateTopLeft();

        // then
        assertEquals(PAGE_DIMENSION.getWidth() / 2, topLeft.getX(), 0.001);
        assertEquals(PAGE_DIMENSION.getHeight() / 2, topLeft.getY(), 0.001);
    }

    @Test
    public void hundredPercentBoxDimensionEqualsToPageContentDimension() {
        // given
        box.setDimensionPercent(100, 100);

        // when
        Dimension dimension = box.calculateDimension();

        // then
        assertEquals(PAGE_CONTENT_DIMENSION.getWidth(), dimension.getWidth(), 0.001);
        assertEquals(PAGE_CONTENT_DIMENSION.getHeight(), dimension.getHeight(), 0.001);
    }

    @Test
    public void fiftyPercentBoxDimensionIsHalfOfThePageContentSize() {
        // given
        box.setDimensionPercent(50, 50);

        // when
        Dimension dimension = box.calculateDimension();

        // then
        assertEquals(PAGE_CONTENT_DIMENSION.getWidth() / 2, dimension.getWidth(), 0.001);
        assertEquals(PAGE_CONTENT_DIMENSION.getHeight() / 2, dimension.getHeight(), 0.001);
    }
}
