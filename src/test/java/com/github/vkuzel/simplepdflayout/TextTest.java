package com.github.vkuzel.simplepdflayout;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextTest {

    private static final Page ANY_PAGE = Page.a4();

    @Test
    void textIsProperlyWrapped() {
        // given
        float firstLineOffset = 30;
        float maxWidth = 60;
        Text text = new Text(ANY_PAGE)
                .setText("XXX XXX XXX XXX XXX XXX")
                .setFirstLineLeftOffset(firstLineOffset)
                .setLineMaxWidth(maxWidth);

        // when
        float width = text.calculateWidth(new HashSet<>());

        // then
        assertTrue(width <= maxWidth);
    }
}
