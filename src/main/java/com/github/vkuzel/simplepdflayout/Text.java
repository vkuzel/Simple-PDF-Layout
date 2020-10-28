package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.*;
import com.github.vkuzel.simplepdflayout.property.Font;
import com.github.vkuzel.simplepdflayout.property.Point;
import com.github.vkuzel.simplepdflayout.property.*;
import com.github.vkuzel.simplepdflayout.renderer.BackgroundRenderer;
import com.github.vkuzel.simplepdflayout.renderer.BorderRenderer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.HEIGHT;
import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.WIDTH;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.X;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.Y;

public final class Text implements ChildElement<Text>, ElementWithMargin, ElementWithBorder, ElementWithPadding, ElementWithBackground {

    public enum Alignment {
        LEFT, CENTER, RIGHT
    }

    private final ParentElement<?> parentElement;

    private final ContentPositionCalculator xContentPositionCalculator;
    private final ContentPositionCalculator yContentPositionCalculator;
    private final ContentDimensionCalculator widthContentDimensionCalculator;
    private final ContentDimensionCalculator heightContentDimensionCalculator;

    private final BorderRenderer borderRenderer;
    private final BackgroundRenderer backgroundRenderer;

    private PositionCalculator xPositionCalculator;
    private PositionCalculator yPositionCalculator;
    private DimensionCalculator widthDimensionCalculator;
    private DimensionCalculator heightDimensionCalculator;

    private Margin margin = null;
    private Border border = null;
    private Padding padding = null;

    private String text;
    private Font font = Font.helvetica();
    private float fontSize = 11;
    private String linesSplitRegex = "\\r?\\n";
    private float firstLineLeftOffset = 0;
    private Float lineMaxWidth = null;
    private String lineWrapRegex = " ";
    private float lineHeight = 11.65f;
    private Alignment alignment = Alignment.LEFT;
    private Color color = Color.BLACK;
    private Color backgroundColor;

    Text(ParentElement<?> parentElement) {
        this.parentElement = parentElement;

        this.xContentPositionCalculator = new ContentPositionCalculator(this, X);
        this.yContentPositionCalculator = new ContentPositionCalculator(this, Y);
        this.widthContentDimensionCalculator = new ContentDimensionCalculator(this, WIDTH);
        this.heightContentDimensionCalculator = new ContentDimensionCalculator(this, HEIGHT);

        this.backgroundRenderer = new BackgroundRenderer(this);
        this.borderRenderer = new BorderRenderer(this);

        setTopLeft(0, 0);
        setWidthOfText();
        setHeight(lineHeight);
    }

    public Text setTopLeft(float x, float y) {
        return setX(x).setY(y);
    }

    public Text setTopLeftPercent(float xPercent, float yPercent) {
        return setXPercent(xPercent).setYPercent(yPercent);
    }

    public Text setX(float x) {
        xPositionCalculator = new FixedPositionCalculator(parentElement, X, x);
        return this;
    }

    public Text setXPercent(float xPercent) {
        xPositionCalculator = new PercentOfParentPositionCalculator(parentElement, X, xPercent);
        return this;
    }

    public Text setY(float y) {
        yPositionCalculator = new FixedPositionCalculator(parentElement, Y, y);
        return this;
    }

    public Text setYPercent(float yPercent) {
        yPositionCalculator = new PercentOfParentPositionCalculator(parentElement, Y, yPercent);
        return this;
    }

    public Text setDimension(float width, float height) {
        return setWidth(width).setHeight(height);
    }

    public Text setDimensionPercent(float widthPercent, float heightPercent) {
        return setWidthPercent(widthPercent).setHeightPercent(heightPercent);
    }

    public Text setWidth(float width) {
        widthDimensionCalculator = new FixedDimensionCalculator(width);
        return this;
    }

    public Text setWidthPercent(float widthPercent) {
        widthDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, WIDTH, widthPercent);
        return this;
    }

    public Text setWidthOfText() {
        widthDimensionCalculator = new TextWidthDimensionCalculator();
        return this;
    }

    public Text setHeight(float height) {
        heightDimensionCalculator = new FixedDimensionCalculator(height);
        return this;
    }

    public Text setHeightPercent(float heightPercent) {
        heightDimensionCalculator = new PercentOfParentContentDimensionCalculator(parentElement, HEIGHT, heightPercent);
        return this;
    }

    public Text setHorizontalPosition(XPosition xPosition, Element positionElement) {
        xPositionCalculator = new RelativeToElementPositionCalculator(this, xPosition, null, positionElement);
        return this;
    }

    public Text setVerticalPosition(YPosition yPosition, Element positionElement) {
        yPositionCalculator = new RelativeToElementPositionCalculator(this, null, yPosition, positionElement);
        return this;
    }

    public Text setMargin(float margin) {
        return setMargin(Margin.of(margin));
    }

    public Text setMargin(Margin margin) {
        this.margin = margin;
        return this;
    }

    @Override
    public Margin getMargin() {
        return margin;
    }

    public Text setBorder(Line line) {
        return setBorder(Border.of(line));
    }

    public Text setBorder(Border border) {
        this.border = border;
        return this;
    }

    @Override
    public Border getBorder() {
        return border;
    }

    public Text setPadding(float padding) {
        return setPadding(Padding.of(padding));
    }

    public Text setPadding(Padding padding) {
        this.padding = padding;
        return this;
    }

    @Override
    public Padding getPadding() {
        return padding;
    }


    public Text setText(String text) {
        this.text = text;
        return this;
    }

    public Text setFont(Font font) {
        this.font = font;
        return this;
    }

    public Text setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public Text setLineSplitRegex(String linesSplitRegex) {
        this.linesSplitRegex = linesSplitRegex;
        return this;
    }

    public Text setFirstLineLeftOffset(float firstLineLeftOffset) {
        this.firstLineLeftOffset = firstLineLeftOffset;
        return this;
    }

    public Text setLineMaxWidth(float lineMaxWidth) {
        this.lineMaxWidth = lineMaxWidth;
        return this;
    }

    public Text setLineHeight(float lineHeight) {
        this.lineHeight = lineHeight;
        return this;
    }

    public Text setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public Text setColor(Color color) {
        this.color = color;
        return this;
    }

    public Text setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public ParentElement<?> getParent() {
        return parentElement;
    }

    @Override
    public void render(PDDocument document, PDPageContentStream contentStream) {
        backgroundRenderer.render(contentStream);
        borderRenderer.render(contentStream);
        renderText(contentStream);
    }

    private void renderText(PDPageContentStream contentStream) {
        Point contentTopLeft = calculateContentTopLeft();
        float width = calculateWidth(new HashSet<>());
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
                        tx += width - textWidth;
                    } else if (alignment == Alignment.CENTER) {
                        tx += (width - textWidth) / 2;
                    }
                }

                Point textBottomLeft = Point.of(tx, ty);
                Point pdfTextBottomLeft = parentElement.convertPointToPdfCoordinates(textBottomLeft);

                contentStream.beginText();
                contentStream.setFont(font.getPdFont(), fontSize);
                contentStream.setNonStrokingColor(color);
                contentStream.newLineAtOffset(pdfTextBottomLeft.getX(), pdfTextBottomLeft.getY());
                contentStream.showText(line);
                contentStream.endText();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<String> getLines() {
        String[] potentialLines = text.split(linesSplitRegex);
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < potentialLines.length; i++) {
            String potentialLine = potentialLines[i];
            List<String> wrappedLines = wrapLine(potentialLine, i == 0);
            lines.addAll(wrappedLines);
        }
        return lines;
    }

    private List<String> wrapLine(String text, boolean firstLine) {
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

    private float calculateTextWidth(String text) {
        try {
            PDFont pdFont = font.getPdFont();
            return pdFont.getStringWidth(text) / 1000f * fontSize;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
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
    public float calculateContentX(Set<Calculator> calculatorPath) {
        return xContentPositionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateContentY(Set<Calculator> calculatorPath) {
        return yContentPositionCalculator.calculate(calculatorPath);
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
    public float calculateContentWidth(Set<Calculator> calculatorPath) {
        return widthContentDimensionCalculator.calculate(calculatorPath);
    }

    @Override
    public float calculateContentHeight(Set<Calculator> calculatorPath) {
        return heightContentDimensionCalculator.calculate(calculatorPath);
    }

    private class TextWidthDimensionCalculator implements DimensionCalculator {

        @Override
        public float calculate(Set<Calculator> calculatorPath) {
            validatePath(calculatorPath);
            float width = 0;
            for (String line : Text.this.getLines()) {
                float lineWidth = Text.this.calculateTextWidth(line);
                if (lineWidth > width) {
                    width = lineWidth;
                }
            }
            return width;
        }
    }
}
