package com.github.vkuzel.simplepdflayout;

import org.junit.Assert;
import org.junit.Test;

public class TextTest {

    @Test
    public void textIsProperlyWrapped() {
        // given
        float maxWidth = 60;
        Text<?> text = new Text<>()
                .setAndWrapText("XXX XXX XXX XXX XXX XXX", maxWidth);

        // when
        float width = text.calculateTextWidth();

        // then
        Assert.assertTrue(width <= maxWidth);
    }
}
