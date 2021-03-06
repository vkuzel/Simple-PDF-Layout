package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.*;
import com.github.vkuzel.simplepdflayout.property.*;
import com.github.vkuzel.simplepdflayout.renderer.BorderRenderer;
import com.github.vkuzel.simplepdflayout.renderer.ChildrenRenderer;
import com.github.vkuzel.simplepdflayout.renderer.RenderingContext;
import com.github.vkuzel.simplepdflayout.util.ChildElementCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.HEIGHT;
import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.WIDTH;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.X;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.Y;
import static com.github.vkuzel.simplepdflayout.property.Line.Style.SOLID;
import static java.awt.Color.GRAY;
import static java.util.Collections.emptyList;

public final class Table implements ParentElement<Table>, ChildElement<Table>, ElementWithBorder, ElementWithMargin {

    private final ParentElement<?> parentElement;
    private final ChildElementCollection<Table> children;

    private final ContentPositionCalculator xContentPositionCalculator;
    private final ContentPositionCalculator yContentPositionCalculator;
    private final ContentDimensionCalculator widthContentDimensionCalculator;
    private final ContentDimensionCalculator heightContentDimensionCalculator;

    private final BorderRenderer borderRenderer;
    private final ChildrenRenderer childrenRenderer;

    private PositionCalculator xPositionCalculator;
    private PositionCalculator yPositionCalculator;
    private DimensionCalculator widthDimensionCalculator;
    private DimensionCalculator heightDimensionCalculator;

    private Margin margin = null;
    private Border border = null;

    private TableCellConfigurer cellConfigurer;
    private List<List<String>> data = emptyList();

    private boolean cellsGenerated = false;

    Table(ParentElement<?> parentElement) {
        this.parentElement = parentElement;
        this.children = new ChildElementCollection<>(this);

        this.xContentPositionCalculator = new ContentPositionCalculator(this, X);
        this.yContentPositionCalculator = new ContentPositionCalculator(this, Y);
        this.widthContentDimensionCalculator = new ContentDimensionCalculator(this, WIDTH);
        this.heightContentDimensionCalculator = new ContentDimensionCalculator(this, HEIGHT);

        this.borderRenderer = new BorderRenderer(this);
        this.childrenRenderer = new ChildrenRenderer(this);

        setTopLeft(0, 0);
        setWidthPercent(100);
        setHeightOfChildren();
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
        ensureCellsAreNotGenerated();
        widthDimensionCalculator = new FixedDimensionCalculator(width);
        return this;
    }

    public Table setWidthPercent(float widthPercent) {
        ensureCellsAreNotGenerated();
        widthDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, WIDTH, widthPercent);
        return this;
    }

    public Table setWidthOfChildren() {
        widthDimensionCalculator = new SizeOfChildrenDimensionCalculator(this, WIDTH);
        return this;
    }

    public Table setHeight(float height) {
        ensureCellsAreNotGenerated();
        heightDimensionCalculator = new FixedDimensionCalculator(height);
        return this;
    }

    public Table setHeightPercent(float heightPercent) {
        ensureCellsAreNotGenerated();
        heightDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, HEIGHT, heightPercent);
        return this;
    }

    public Table setHeightOfChildren() {
        heightDimensionCalculator = new SizeOfChildrenDimensionCalculator(this, HEIGHT);
        return this;
    }

    public Table setHorizontalPosition(XPosition xPosition, Element positionElement) {
        xPositionCalculator = new RelativeToElementPositionCalculator(parentElement, this, xPosition, null, positionElement);
        return this;
    }

    public Table setVerticalPosition(YPosition yPosition, Element positionElement) {
        yPositionCalculator = new RelativeToElementPositionCalculator(parentElement, this, null, yPosition, positionElement);
        return this;
    }

    public Table setMargin(float margin) {
        return setMargin(Margin.of(margin));
    }

    public Table setMargin(Margin margin) {
        this.margin = margin;
        return this;
    }

    @Override
    public Margin getMargin() {
        return margin;
    }

    public Table setBorder(Line line) {
        return setBorder(Border.of(line));
    }

    public Table setBorder(Border border) {
        this.border = border;
        return this;
    }

    @Override
    public Border getBorder() {
        return border;
    }

    public Table setCellConfigurer(TableCellConfigurer cellConfigurer) {
        ensureCellsAreNotGenerated();
        this.cellConfigurer = cellConfigurer;
        return this;
    }

    public Table setData(List<List<String>> rowsColumnsValues) {
        ensureCellsAreNotGenerated();
        this.data = rowsColumnsValues;
        return this;
    }

    @Override
    public void render(RenderingContext renderingContext) {
        generateCells(renderingContext.getCalculationContext());
        borderRenderer.render(renderingContext);
        childrenRenderer.render(renderingContext);
    }

    private void generateCells(CalculationContext calculationContext) {
        if (cellsGenerated) {
            return;
        }
        cellsGenerated = true;

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

                addText(cell -> {
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

                    float cellWidth = cell.calculateWidth(calculationContext);
                    float cellHeight = cell.calculateHeight(calculationContext);
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
        Line line = Line.of(1, SOLID, GRAY);
        Line top = Line.of();
        Line left = Line.of();
        if (row == 1) {
            top = line;
        }
        if (column == 1) {
            left = line;
        }
        cell.setBorder(Border.of(top, line, line, left));
    }

    private void ensureCellsAreNotGenerated() {
        if (cellsGenerated) {
            throw new IllegalStateException("Table cells are generated, modification is not allowed!");
        }
    }

    @Override
    public ParentElement<?> getParent() {
        return parentElement;
    }

    @Override
    public <C extends ChildElement<C>> Table addChild(Function<ParentElement<?>, C> childFactory, Consumer<C> childConfigurer) {
        return children.addChild(childFactory, childConfigurer);
    }

    @Override
    public Table removeChild(ChildElement<?> childElement) {
        return children.removeChild(childElement);
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
    public float calculateX(CalculationContext calculationContext) {
        return xPositionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateY(CalculationContext calculationContext) {
        return yPositionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentX(CalculationContext calculationContext) {
        return xContentPositionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentY(CalculationContext calculationContext) {
        return yContentPositionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateWidth(CalculationContext calculationContext) {
        generateCells(calculationContext);
        return widthDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateHeight(CalculationContext calculationContext) {
        generateCells(calculationContext);
        return heightDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentWidth(CalculationContext calculationContext) {
        generateCells(calculationContext);
        return widthContentDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentHeight(CalculationContext calculationContext) {
        generateCells(calculationContext);
        return heightContentDimensionCalculator.calculate(calculationContext);
    }

    @FunctionalInterface
    public interface TableCellConfigurer {

        void configure(Table table, Text cell, int noOfRows, int noOfColumns, int row, int column);
    }

    private static class Offset {
        private float x = 0;
        private float y = 0;
        private float rowHeight = 0;
    }
}
