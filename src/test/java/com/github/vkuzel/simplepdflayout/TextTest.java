package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.CalculationContext;
import com.github.vkuzel.simplepdflayout.property.Margin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextTest {

    private static final Page ANY_PAGE = Page.a4();

    @Test
    void textIsCorrectlyWrapped() {
        // given
        float firstLineOffset = 30;
        float maxWidth = 60;
        Text text = new Text(ANY_PAGE)
                .setText("XXX XXX XXX XXX XXX XXX")
                .setFirstLineLeftOffset(firstLineOffset)
                .setLineMaxWidth(maxWidth);

        // when
        float width = text.calculateWidth(new CalculationContext());

        // then
        assertTrue(width <= maxWidth);
    }

    @Test
    void paragraphSpacingIsCorrectlyCalculated() {
        // given
        float lineHeight = 10;
        float paragraphSpacing = 5;
        Text text = new Text(ANY_PAGE)
                .setText("Paragraph #1\nParagraph #2")
                .setLineHeight(lineHeight)
                .setParagraphSpacing(paragraphSpacing);

        // when
        float height = text.calculateHeight(new CalculationContext());

        // then
        assertEquals(25, height);
    }

    @Test
    void boxWithMarginWrappingTextShouldHaveContentHeightEqualToTextHeight() {
        // given
        Box wrapper = new Box(ANY_PAGE)
                .setMargin(Margin.top(10))
                .addText(text -> text
                        .setLineHeight(20)
                        .setText("Whatever"));

        // when
        float wrapperContentHeight = wrapper.calculateContentHeight(new CalculationContext());

        // then
        assertEquals(20, wrapperContentHeight, 0.001);
    }
}
