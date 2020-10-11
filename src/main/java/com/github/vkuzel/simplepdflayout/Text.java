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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Text<T extends Text<T>> extends Box<T> {

    public enum Alignment {
        LEFT, CENTER, RIGHT
    }

    protected List<String> lines;
    protected PDFont font = PDType1Font.HELVETICA;
    protected float fontSize = 11;
    protected float lineHeight = 11.65f;
    protected Alignment alignment;
    protected Color color = Color.BLACK;

    public T setText(String text) {
        this.lines = Arrays.asList(text.split("\\r?\\n"));
        return getThis();
    }

    /**
     * Sets a text and splits it by space character to multiple lines if it is
     * longer than maxWidth value.
     *
     * @param text Text to print.
     * @param maxWidth Points.
     * @return This instance.
     */
    public T setAndWrapText(String text, float maxWidth) {
        float textWidth = calculateTextWidth(text);
        if (textWidth < maxWidth) {
            this.lines = Collections.singletonList(text);
        } else {
            String[] tokens = text.split(" ");
            String buffer = "";
            List<String> lines = new ArrayList<>();
            for (String token : tokens) {
                String potentialBuffer = buffer + (buffer.isEmpty() ? "" : " ") + token;
                float bufferWidth = calculateTextWidth(potentialBuffer);
                if (bufferWidth > maxWidth) {
                    lines.add(buffer);
                    buffer = token;
                } else {
                    buffer = potentialBuffer;
                }
            }
            if (!buffer.isEmpty()) {
                lines.add(buffer);
            }
            this.lines = lines;
        }
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

    public T setLineHeight(float lineHeight) {
        this.lineHeight = lineHeight;
        return getThis();
    }

    public T setAlignment(Alignment alignment) {
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

        try {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if ("".equals(line)) {
                    continue;
                }

                float tx = contentTopLeft.getX();
                float ty = contentTopLeft.getY() + (i + 1) * lineHeight;
                if (alignment != Alignment.LEFT) {
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
        for (String line : lines) {
            float lineWidth = calculateTextWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        return width;
    }

    public float calculateTextHeight() {
        return lines.size() * lineHeight;
    }

    protected float calculateTextWidth(String text) {
        try {
            return font.getStringWidth(text) / 1000f * fontSize;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
