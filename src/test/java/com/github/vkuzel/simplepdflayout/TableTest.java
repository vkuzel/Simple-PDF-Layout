package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.CalculationContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableTest {

    private static final Page ANY_PAGE = Page.a4();
    private static final List<List<String>> ANY_DATA = singletonList(singletonList("Test"));

    @Test
    void tableCellsShouldBeAlreadyGeneratedWhenHeightCalculationIsInvoked() {
        // given
        Table table = new Table(ANY_PAGE).setData(ANY_DATA);

        // when
        float height = table.calculateHeight(new CalculationContext());

        // then
        assertTrue(height > 0);
    }

    @Test
    void tableCannotBeModifiedIfCellsAreGenerated() {
        // given
        Table table = new Table(ANY_PAGE).setData(ANY_DATA);

        // when ... generates cells
        table.calculateContentHeight(new CalculationContext());

        // then
        assertThrows(IllegalStateException.class, () -> table.setData(ANY_DATA));
    }
}
