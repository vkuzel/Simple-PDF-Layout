package com.github.vkuzel.simplepdflayout;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;

import static com.github.vkuzel.simplepdflayout.property.YPosition.TO_BOTTOM;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PagesTest {

    private static final PDRectangle PAGE_DIMENSION = new PDRectangle(100, 100);
    private static final float PAGE_PADDING = 20;
    private static final float BOX_HEIGHT = 30;

    @Test
    void addBoxCreatesNewPageIfCurrentIsAlreadyFull() {
        // given
        Pages pages = new Pages(PAGE_DIMENSION)
                .setPadding(PAGE_PADDING);

        // when
        pages
                .addBox(box -> box
                        .setHeight(BOX_HEIGHT))
                .addBox(box -> box
                        .setHeight(BOX_HEIGHT)
                        .setVerticalPosition(TO_BOTTOM, box.getPrevious()))
                .addBox(box -> box
                        .setHeight(BOX_HEIGHT)
                        .setVerticalPosition(TO_BOTTOM, box.getPrevious()));

        // then
        assertEquals(2, pages.getPages().size());
    }
}
