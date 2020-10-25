package com.github.vkuzel.simplepdflayout;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class TextTest {

    private static final Page ANY_PAGE = Page.a4();

    @Test
    public void textIsProperlyWrapped() {
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
        Assert.assertTrue(width <= maxWidth);
    }
}
