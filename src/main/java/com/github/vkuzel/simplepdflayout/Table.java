package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.*;
import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import com.github.vkuzel.simplepdflayout.property.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.HEIGHT;
import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.WIDTH;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.X;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.Y;

public final class Table implements ParentElement<Table>, ChildElement<Table> {

    private final ParentElement<?> parentElement;
    private final ChildElementCollection<Table> children;

    private PositionCalculator xPositionCalculator;
    private PositionCalculator yPositionCalculator;
    private DimensionCalculator widthDimensionCalculator;
    private DimensionCalculator heightDimensionCalculator;

    private TableCellConfigurer cellConfigurer;
    private List<List<String>> data;

    public Table(ParentElement<?> parentElement) {
        this.parentElement = parentElement;
        this.children = new ChildElementCollection<>(this);

        setTopLeft(0, 0);
        setWidthPercent(100).setHeightOfChildren();
        setCellConfigurer(this::configureCell);
    }

    public Table setTopLeft(float x, float y) {
        return setX(x).setY(y);
    }

    public Table setTopLeftPercent(float xPercent, float yPercent) {
        return setXPercent(xPercent).setYPercent(yPercent);
    }

    public Table setX(float x) {
        xPositionCalculator = new FixedPositionCalculator(parentElement, X, x);
        return this;
    }

    public Table setXPercent(float xPercent) {
        xPositionCalculator = new PercentOfParentPositionCalculator(parentElement, X, xPercent);
        return this;
    }

    public Table setY(float y) {
        yPositionCalculator = new FixedPositionCalculator(parentElement, Y, y);
        return this;
    }

    public Table setYPercent(float yPercent) {
        yPositionCalculator = new PercentOfParentPositionCalculator(parentElement, Y, yPercent);
        return this;
    }

    public Table setDimension(float width, float height) {
        return setWidth(width).setHeight(height);
    }

    public Table setDimensionPercent(float widthPercent, float heightPercent) {
        return setWidthPercent(widthPercent).setHeightPercent(heightPercent);
    }

    public Table setWidth(float width) {
        widthDimensionCalculator = new FixedDimensionCalculator(width);
        return this;
    }

    public Table setWidthPercent(float widthPercent) {
        widthDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, WIDTH, widthPercent);
        return this;
    }

    public Table setWidthOfChildren() {
        widthDimensionCalculator = new SizeOfChildrenDimensionCalculator(this, WIDTH);
        return this;
    }

    public Table setHeight(float height) {
        heightDimensionCalculator = new FixedDimensionCalculator(height);
        return this;
    }

    public Table setHeightPercent(float heightPercent) {
        heightDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, HEIGHT, heightPercent);
        return this;
    }

    public Table setHeightOfChildren() {
        heightDimensionCalculator = new SizeOfChildrenDimensionCalculator(this, HEIGHT);
        return this;
    }

    public Table setHorizontalPosition(XPosition xPosition, Element positionElement) {
        xPositionCalculator = new RelativeToElementPositionCalculator(this, xPosition, null, positionElement);
        return this;
    }

    public Table setVerticalPosition(YPosition yPosition, Element positionElement) {
        yPositionCalculator = new RelativeToElementPositionCalculator(this, null, yPosition, positionElement);
        return this;
    }

    public Table setCellConfigurer(TableCellConfigurer cellConfigurer) {
        this.cellConfigurer = cellConfigurer;
        return this;
    }

    public Table setData(List<List<String>> rowsColumnsValues) {
        this.data = rowsColumnsValues;
        return this;
    }

    @Override
    public void render(PDDocument document, PDPageContentStream contentStream) {
        createCells();
    }

    private void createCells() {
        int noOfRows = data.size();
        int noOfColumns = data.isEmpty() || data.get(0).isEmpty() ? 0 : data.get(0).size();

        Offset offset = new Offset();
        offset.y = 0;

        for (int row = 0; row < data.size(); row++) {
            List<Text> rowCells = new ArrayList<>(noOfColumns);
            offset.x = 0;
            offset.rowHeight = 0;

            for (int column = 0; column < data.get(row).size(); column++) {
                String value = data.get(row).get(column);
                int cellRow = row + 1;
                int cellColumn = column + 1;

                addChild(Text::new, cell -> {
                    cell
                            .setWidthPercent(100f / noOfColumns)
                            .setPadding(2)
                            .setText(value);
                    // If the height of a table is absolute all cells should
                    // be stretched accordingly.
                    if (!(heightDimensionCalculator instanceof SizeOfChildrenDimensionCalculator)) {
                        cell.setHeightPercent(100f / noOfRows);
                    }

                    cellConfigurer.configure(this, cell, noOfRows, noOfColumns, cellRow, cellColumn);

                    float cellX = offset.x;
                    float cellY = offset.y;

                    float cellWidth = cell.calculateWidth(new HashSet<>());
                    float cellHeight = cell.calculateHeight(new HashSet<>());
                    offset.x += cellWidth;
                    offset.rowHeight = Math.max(offset.rowHeight, cellHeight);

                    cell.setTopLeft(cellX, cellY);
                    rowCells.add(cell);
                });
            }

            for (Text cell : rowCells) {
                Dimension cellDimension = cell.calculateDimension();
                if (cellDimension.getHeight() < offset.rowHeight) {
                    cell.setHeight(offset.rowHeight);
                }
            }

            offset.y += offset.rowHeight;
        }
    }

    private void configureCell(Table table, Text cell, int noOfRows, int noOfColumns, int row, int column) {
        Line line = new Line().setWidth(1).setColor(Color.GRAY);
        Line top = new Line();
        Line left = new Line();
        if (row == 1) {
            top = line;
        }
        if (column == 1) {
            left = line;
        }
        cell.setBorder(new Border(top, line, line, left));
    }

    @Override
    public ParentElement<?> getParent() {
        return parentElement;
    }

    @Override
    public <C extends ChildElement<C>> Table addChild(Function<Table, C> childFactory, Consumer<C> childConfigurer) {
        return children.addChild(childFactory, childConfigurer);
    }

    @Override
    public List<ChildElement<?>> getChildren() {
        return children.getChildren();
    }

    @Override
    public ChildElement<?> getPreviousChildTo(ChildElement<?> childElement) {
        return children.getPreviousChildTo(childElement);
    }

    @Override
    public ChildElement<?> getNextChildTo(ChildElement<?> childElement) {
        return children.getNextChildTo(childElement);
    }

    @Override
    public float calculateX(Set<Calculator> calculatorPath) {
        return xPositionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateY(Set<Calculator> calculatorPath) {
        return yPositionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateWidth(Set<Calculator> calculatorPath) {
        return widthDimensionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateHeight(Set<Calculator> calculatorPath) {
        return heightDimensionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateContentX(Set<Calculator> calculatorPath) {
        return calculateX(calculatorPath);
    }

    @Override
    public float calculateContentY(Set<Calculator> calculatorPath) {
        return calculateY(calculatorPath);
    }

    @Override
    public float calculateContentWidth(Set<Calculator> calculatorPath) {
        return calculateWidth(calculatorPath);
    }

    @Override
    public float calculateContentHeight(Set<Calculator> calculatorPath) {
        return calculateHeight(calculatorPath);
    }

    public interface TableCellConfigurer {

        void configure(Table table, Text cell, int noOfRows, int noOfColumns, int row, int column);
    }

    private static class Offset {
        private float x = 0;
        private float y = 0;
        private float rowHeight = 0;
    }
}
