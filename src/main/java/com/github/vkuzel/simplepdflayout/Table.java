package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Table<T extends Table<T>> extends Box<T> {

    protected CellDecorator cellDecorator = new CellDecorator();
    protected List<List<String>> data;

    public T setCellDecorator(CellDecorator cellDecorator) {
        this.cellDecorator = cellDecorator;
        return getThis();
    }

    public T setData(List<List<String>> rowsColumnsValues) {
        this.data = rowsColumnsValues;
        return getThis();
    }

    @Override
    public void draw(PDDocument document, PDPageContentStream contentStream) {
        createCells();
        super.draw(document, contentStream);
    }

    protected void createCells() {
        int noOfRows = data.size();
        int noOfColumns = data.isEmpty() || data.get(0).isEmpty() ? 0 : data.get(0).size();

        float rowsHeight = 0;

        for (int row = 0; row < data.size(); row++) {
            List<Text> cells = new ArrayList<>(noOfColumns);
            float offsetX = 0;
            float rowHeight = 0;

            for (int column = 0; column < data.get(row).size(); column++) {
                String value = data.get(row).get(column);
                Text cell = cellDecorator.createCell(this, noOfRows, noOfColumns).setText(value);
                this.addChild(cell);
                cellDecorator.decorate(noOfRows, noOfColumns, row + 1, column + 1, cell);

                float cellX = offsetX;
                float cellY = rowsHeight;

                Dimension cellDimension = cell.calculateDimension();
                offsetX += cellDimension.getWidth();
                rowHeight = Math.max(rowHeight, cellDimension.getHeight());

                cell.setTopLeft(cellX, cellY);
                cells.add(cell);
            }

            for (Text cell : cells) {
                Dimension cellDimension = cell.calculateDimension();
                if (cellDimension.getHeight() < rowHeight) {
                    cell.setHeight(rowHeight);
                }
            }

            rowsHeight += rowHeight;
        }

        if (height == null && heightPercent == null) {
            float verticalBorderWidth = borderTop.getWidth() + borderBottom.getWidth();
            this.height = rowsHeight + marginTop + marginBottom + verticalBorderWidth + paddingTop + paddingBottom;
        }
    }

    public static class CellDecorator {

        public Text createCell(Table table, int notOfRows, int noOfColumns) {
            Text text = new Text<>()
                    .setWidthPercent(100f / noOfColumns)
                    .setPadding(2);
            if (table.height != null || table.heightPercent != null) {
                text.setHeightPercent(100f / notOfRows);
            }
            return text;
        }

        public void decorate(int notOfRows, int noOfColumns, int row, int column, Text cell) {
            Line border = new Line().setWidth(1).setColor(Color.GRAY);
            if (row == 1) {
                cell.setBorderTop(border);
            }
            if (column == 1) {
                cell.setBorderLeft(border);
            }
            cell.setBorderRight(border).setBorderBottom(border);
        }
    }
}
