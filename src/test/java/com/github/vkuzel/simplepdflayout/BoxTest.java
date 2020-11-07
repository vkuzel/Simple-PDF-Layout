package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.property.Dimension;
import com.github.vkuzel.simplepdflayout.property.Point;
import com.github.vkuzel.simplepdflayout.property.YPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoxTest {

    private static final Dimension PAGE_DIMENSION = Dimension.of(100, 100);
    private static final float PAGE_PADDING = 10;
    private static final float PAGE_CONTENT_WIDTH = PAGE_DIMENSION.getWidth() - 2 * PAGE_PADDING;
    private static final float PAGE_CONTENT_HEIGHT = PAGE_DIMENSION.getHeight() - 2 * PAGE_PADDING;

    private Box box;

    @BeforeEach
    void init() {
        Page.of(PAGE_DIMENSION)
                .setPadding(PAGE_PADDING)
                .addBox(box -> this.box = box);
    }

    @Test
    void absoluteBoxPositionIsCorrectlyCalculated() {
        // given
        box.setTopLeft(10, 10);

        // when
        Point topLeft = box.calculateTopLeft();

        // then
        assertEquals(20, topLeft.getX(), 0.001);
        assertEquals(20, topLeft.getY(), 0.001);
    }

    @Test
    void fiftyPercentBoxPositionIsInTheMiddleOfPage() {
        // given
        box.setTopLeftPercent(50, 50);

        // when
        Point topLeft = box.calculateTopLeft();

        // then
        assertEquals(PAGE_DIMENSION.getWidth() / 2, topLeft.getX(), 0.001);
        assertEquals(PAGE_DIMENSION.getHeight() / 2, topLeft.getY(), 0.001);
    }

    @Test
    void hundredPercentBoxDimensionEqualsToPageContentDimension() {
        // given
        box.setDimensionPercent(100, 100);

        // when
        Dimension dimension = box.calculateDimension();

        // then
        assertEquals(PAGE_CONTENT_WIDTH, dimension.getWidth(), 0.001);
        assertEquals(PAGE_CONTENT_HEIGHT, dimension.getHeight(), 0.001);
    }

    @Test
    void fiftyPercentBoxDimensionIsHalfOfThePageContentSize() {
        // given
        box.setDimensionPercent(50, 50);

        // when
        Dimension dimension = box.calculateDimension();

        // then
        assertEquals(PAGE_CONTENT_WIDTH / 2, dimension.getWidth(), 0.001);
        assertEquals(PAGE_CONTENT_HEIGHT / 2, dimension.getHeight(), 0.001);
    }

    @Test
    void calculatePositionWithNestedBoxShouldNotFail() {
        // given
        box.addBox(ignored -> {
        });

        // when
        Point topLeft = box.calculateTopLeft();

        // then
        assertEquals(PAGE_PADDING, topLeft.getX(), 0.001);
        assertEquals(PAGE_PADDING, topLeft.getY(), 0.001);
    }

    @Test
    void cycleInDependenciesShouldThrowException() {
        // given
        AtomicReference<Box> first = new AtomicReference<>();
        AtomicReference<Box> second = new AtomicReference<>();
        box
                .addBox(first::set)
                .addBox(second::set);
        first.get().setVerticalPosition(YPosition.TO_TOP, first.get().getNext());
        second.get().setVerticalPosition(YPosition.TO_BOTTOM, second.get().getPrevious());

        // when, then
        assertThrows(Throwable.class, () -> box.calculateContentDimension());
    }
}
