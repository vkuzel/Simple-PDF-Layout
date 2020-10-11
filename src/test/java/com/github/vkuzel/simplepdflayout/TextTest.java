package com.github.vkuzel.simplepdflayout;

import org.junit.Assert;
import org.junit.Test;

public class TextTest {

    @Test
    public void textIsProperlyWrapped() {
        // given
        float firstLineOffset = 30;
        float maxWidth = 60;
        Text<?> text = new Text<>()
                .setText("XXX XXX XXX XXX XXX XXX")
                .setFirstLineLeftOffset(firstLineOffset)
                .setLineMaxWidth(maxWidth);

        // when
        float width = text.calculateTextWidth();

        // then
        Assert.assertTrue(width <= maxWidth);
    }
}
