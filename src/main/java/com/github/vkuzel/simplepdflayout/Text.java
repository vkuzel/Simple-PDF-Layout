package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import com.github.vkuzel.simplepdflayout.geometry.Point;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Text<T extends Text<T>> extends Box<T> {

    public enum Alignment {
        LEFT, CENTER, RIGHT
    }

    protected String text;
    protected PDFont font = PDType1Font.HELVETICA;
    protected float fontSize = 11;
    protected String linesSplitRegex = "\\r?\\n";
    protected float firstLineLeftOffset = 0;
    protected Float lineMaxWidth = null;
    protected String lineWrapRegex = " ";
    protected float lineHeight = 11.65f;
    protected Alignment alignment = Alignment.LEFT;
    protected Color color = Color.BLACK;

    public T setText(String text) {
        this.text = text;
        return getThis();
    }

    public T setFont(PDFont font) {
        this.font = font;
        return getThis();
    }

    public T setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return getThis();
    }

    public T setLineSplitRegex(String lineSplitRegex) {
        this.linesSplitRegex = lineSplitRegex;
        return getThis();
    }

    public T setFirstLineLeftOffset(float firstLineLeftOffset) {
        this.firstLineLeftOffset = firstLineLeftOffset;
        return getThis();
    }

    public T setLineMaxWidth(Float lineMaxWidth) {
        this.lineMaxWidth = lineMaxWidth;
        return getThis();
    }

    public T setLineWrapRegex(String lineWrapRegex) {
        this.lineWrapRegex = lineWrapRegex;
        return getThis();
    }

    public T setLineHeight(float lineHeight) {
        this.lineHeight = lineHeight;
        return getThis();
    }

    public T setAlignment(Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException();
        }
        this.alignment = alignment;
        return getThis();
    }

    public T setColor(Color color) {
        this.color = color;
        return getThis();
    }

    @Override
    public void draw(PDDocument document, PDPageContentStream contentStream) {
        super.drawBackground(contentStream);
        drawText(contentStream);
        super.drawBorders(contentStream);
        super.drawChildren(document, contentStream);
    }

    protected void drawText(PDPageContentStream contentStream) {
        Point contentTopLeft = calculateContentTopLeft();
        Dimension contentDimension = calculateContentDimension();
        List<String> lines = getLines();

        try {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if ("".equals(line)) {
                    continue;
                }

                float tx = contentTopLeft.getX();
                float ty = contentTopLeft.getY() + (i + 1) * lineHeight;
                if (alignment == Alignment.LEFT) {
                    tx += i == 0 ? firstLineLeftOffset : 0;
                } else {
                    float textWidth = calculateTextWidth(line);
                    if (alignment == Alignment.RIGHT) {
                        tx += contentDimension.getWidth() - textWidth;
                    } else if (alignment == Alignment.CENTER) {
                        tx += (contentDimension.getWidth() - textWidth) / 2;
                    }
                }

                Point textBottomLeft = new Point(tx, ty);
                Point pdfTextBottomLeft = parent.convertPointToPdfCoordinates(textBottomLeft);

                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(color);
                contentStream.newLineAtOffset(pdfTextBottomLeft.getX(), pdfTextBottomLeft.getY());
                contentStream.showText(line);
                contentStream.endText();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected List<String> getLines() {
        String[] potentialLines = text.split(linesSplitRegex);
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < potentialLines.length; i++) {
            String potentialLine = potentialLines[i];
            List<String> wrappedLines = wrapLine(potentialLine, i == 0);
            lines.addAll(wrappedLines);
        }
        return lines;
    }

    protected List<String> wrapLine(String text, boolean firstLine) {
        if (lineMaxWidth == null || calculateTextWidth(text) < lineMaxWidth) {
            return Collections.singletonList(text);
        }

        String[] tokens = text.split(lineWrapRegex);
        String buffer = "";
        List<String> lines = new ArrayList<>();
        boolean firstWrapLine = firstLine;

        for (String token : tokens) {
            String potentialBuffer = buffer + (buffer.isEmpty() ? "" : " ") + token;
            float bufferWidth = calculateTextWidth(potentialBuffer);
            float leftOffset = firstWrapLine && alignment == Alignment.LEFT ? firstLineLeftOffset : 0;
            if (bufferWidth + leftOffset > lineMaxWidth) {
                lines.add(buffer);
                buffer = token;
                firstWrapLine = false;
            } else {
                buffer = potentialBuffer;
            }
        }
        if (!buffer.isEmpty()) {
            lines.add(buffer);
        }

        return lines;
    }

    @Override
    protected Dimension calculateDimension() {
        Dimension dimension = super.calculateDimension();
        if (heightPercent == null && height == null) {
            float verticalMargin = marginTop + marginBottom;
            float verticalBorderWidth = borderTop.getWidth() + borderBottom.getWidth();
            float verticalPadding = paddingTop + paddingBottom;
            dimension = new Dimension(dimension.getWidth(), calculateTextHeight() + verticalMargin + verticalBorderWidth + verticalPadding);
        }
        return dimension;
    }

    public float calculateTextWidth() {
        float width = 0;
        List<String> lines = getLines();
        for (String line : lines) {
            float lineWidth = calculateTextWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        return width;
    }

    public float calculateTextHeight() {
        return getLines().size() * lineHeight;
    }

    protected float calculateTextWidth(String text) {
        try {
            return font.getStringWidth(text) / 1000f * fontSize;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
