package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.Box;
import com.github.vkuzel.simplepdflayout.property.Border;
import com.github.vkuzel.simplepdflayout.property.Line;
import com.github.vkuzel.simplepdflayout.property.Margin;
import com.github.vkuzel.simplepdflayout.property.Padding;
import org.junit.jupiter.api.Test;

import static com.github.vkuzel.simplepdflayout.BoxTestFactory.emptyBox;
import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.HEIGHT;
import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.WIDTH;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SizeOfChildrenDimensionCalculatorTest {

    @Test
    void calculatedWidthIncludesParentElementsMarginAndBorderAndPadding() {
        // given
        Box box = emptyBox()
                .setMargin(Margin.right(10).withLeft(10))
                .setBorder(Border.right(Line.width(10)).withLeft(Line.width(10)))
                .setPadding(Padding.right(10).withLeft(10));
        SizeOfChildrenDimensionCalculator calculator = new SizeOfChildrenDimensionCalculator(box, WIDTH);

        // when
        float width = calculator.calculate(new CalculationContext());

        // then
        assertEquals(60, width, 0.001);
    }

    @Test
    void calculatedHeightIncludesParentElementsMarginAndBorderAndPadding() {
        // given
        Box box = emptyBox()
                .setMargin(Margin.top(10).withBottom(10))
                .setBorder(Border.top(Line.width(10)).withBottom(Line.width(10)))
                .setPadding(Padding.top(10).withBottom(10));
        SizeOfChildrenDimensionCalculator calculator = new SizeOfChildrenDimensionCalculator(box, HEIGHT);

        // when
        float height = calculator.calculate(new CalculationContext());

        // then
        assertEquals(60, height, 0.001);
    }
}
