package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.property.XPosition;
import com.github.vkuzel.simplepdflayout.stub.ParentElementStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RelativeToElementPositionCalculatorTest {

    @Test
    void calculateFallsBackToParentElementIfNoPositionElementIsSet() {
        // given
        ParentElementStub parentElement = ParentElementStub.builder().setContentX(100).build();
        RelativeToElementPositionCalculator calculator = new RelativeToElementPositionCalculator(parentElement, null, XPosition.TO_LEFT, null, null);

        // when
        float position = calculator.calculate(new CalculationContext());

        // then
        Assertions.assertEquals(100, position, 0.0001);
    }
}
