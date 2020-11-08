package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.*;
import com.github.vkuzel.simplepdflayout.property.Font;
import com.github.vkuzel.simplepdflayout.property.Point;
import com.github.vkuzel.simplepdflayout.property.*;
import com.github.vkuzel.simplepdflayout.renderer.BackgroundRenderer;
import com.github.vkuzel.simplepdflayout.renderer.BorderRenderer;
import com.github.vkuzel.simplepdflayout.renderer.RenderingContext;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.HEIGHT;
import static com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement.WIDTH;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.X;
import static com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis.Y;
import static com.github.vkuzel.simplepdflayout.util.Utils.sumHorizontalMarginBorderPadding;
import static com.github.vkuzel.simplepdflayout.util.Utils.sumVerticalMarginBorderPadding;

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
    private String paragraphsSplitRegex = "\\r?\\n";
    private float paragraphSpacing = 8;
    private float firstLineLeftOffset = 0;
    private Float lineMaxWidth = null;
    private String lineWrapString = " ";
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
        setHeightOfText();
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

    public Text setHeightOfText() {
        heightDimensionCalculator = new TextHeightDimensionCalculator();
        return this;
    }

    public Text setHorizontalPosition(XPosition xPosition, Element positionElement) {
        xPositionCalculator = new RelativeToElementPositionCalculator(parentElement, this, xPosition, null, positionElement);
        return this;
    }

    public Text setVerticalPosition(YPosition yPosition, Element positionElement) {
        yPositionCalculator = new RelativeToElementPositionCalculator(parentElement, this, null, yPosition, positionElement);
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

    public Text setParagraphsSplitRegex(String paragraphsSplitRegex) {
        this.paragraphsSplitRegex = paragraphsSplitRegex;
        return this;
    }

    public Text setParagraphSpacing(float paragraphSpacing) {
        this.paragraphSpacing = paragraphSpacing;
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
    public void render(RenderingContext renderingContext) {
        backgroundRenderer.render(renderingContext);
        borderRenderer.render(renderingContext);
        renderText(renderingContext);
    }

    private void renderText(RenderingContext renderingContext) {
        Point contentTopLeft = calculateContentTopLeft();
        float width = calculateContentWidth(renderingContext.getCalculationContext());
        List<TextLine> lines = getLines();

        float ty = contentTopLeft.getY();

        for (TextLine line : lines) {
            String text = line.getText();

            float tx = contentTopLeft.getX() + line.getLeftOffset();
            ty += lineHeight + line.getMarginTop();
            if (alignment == Alignment.RIGHT) {
                float textWidth = calculateTextWidth(line.getText());
                tx += width - textWidth;
            } else if (alignment == Alignment.CENTER) {
                float textWidth = calculateTextWidth(line.getText());
                tx += (width - textWidth) / 2;
            }

            if ("".equals(text)) {
                continue;
            }

            try {
                Point textBottomLeft = Point.of(tx, ty);
                Point pdfTextBottomLeft = parentElement.convertPointToPdfCoordinates(textBottomLeft);

                PDPageContentStream contentStream = renderingContext.getContentStream();
                contentStream.beginText();
                contentStream.setFont(font.getPdFont(), fontSize);
                contentStream.setNonStrokingColor(color);
                contentStream.newLineAtOffset(pdfTextBottomLeft.getX(), pdfTextBottomLeft.getY());
                contentStream.showText(text);
                contentStream.endText();
            } catch (IOException e) {
                throw new IllegalStateException("Line " + text + " cannot be rendered!", e);
            }
        }
    }

    private List<TextLine> getLines() {
        boolean firstLine = true;
        float marginTop = 0;
        List<TextLine> lines = new ArrayList<>();
        for (String paragraph : text.split(paragraphsSplitRegex)) {
            for (String text : wrapLines(paragraph, firstLine)) {
                float leftOffset = firstLine ? firstLineLeftOffset : 0;
                lines.add(new TextLine(text, marginTop, leftOffset));
                firstLine = false;
                marginTop = 0;
            }
            marginTop = paragraphSpacing;
        }
        return lines;
    }

    private List<String> wrapLines(String text, boolean firstLine) {
        float textWidth = calculateTextWidth(text);
        if (firstLine) {
            textWidth += firstLineLeftOffset;
        }

        if (lineMaxWidth == null || textWidth < lineMaxWidth) {
            return Collections.singletonList(text);
        }

        List<String> tokens = tokenize(text);
        String buffer = "";
        List<String> lines = new ArrayList<>();
        boolean firstWrapLine = firstLine;

        for (String token : tokens) {
            String potentialBuffer = buffer + token;
            float bufferWidth = calculateTextWidth(potentialBuffer);
            float leftOffset = firstWrapLine && alignment == Alignment.LEFT ? firstLineLeftOffset : 0;
            if (bufferWidth + leftOffset > lineMaxWidth) {
                lines.add(buffer);
                if (!token.equals(lineWrapString)) {
                    buffer = token;
                } else {
                    buffer = "";
                }
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

    private List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        int index, previousIndex = 0;
        while ((index = text.indexOf(lineWrapString, previousIndex)) >= 0) {
            if (previousIndex < index) {
                tokens.add(text.substring(previousIndex, index));
            }
            tokens.add(lineWrapString);
            previousIndex = index + lineWrapString.length();
        }
        if (previousIndex < text.length()) {
            tokens.add(text.substring(previousIndex));
        }
        return tokens;
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
        return widthDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateHeight(CalculationContext calculationContext) {
        return heightDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentWidth(CalculationContext calculationContext) {
        return widthContentDimensionCalculator.calculate(calculationContext);
    }

    @Override
    public float calculateContentHeight(CalculationContext calculationContext) {
        return heightContentDimensionCalculator.calculate(calculationContext);
    }

    private class TextWidthDimensionCalculator implements DimensionCalculator {

        @Override
        public float calculate(CalculationContext calculationContext) {
            float width = 0;
            for (TextLine line : Text.this.getLines()) {
                float lineWidth = Text.this.calculateTextWidth(line.getText()) + line.getLeftOffset();
                if (lineWidth > width) {
                    width = lineWidth;
                }
            }
            width += sumHorizontalMarginBorderPadding(Text.this);
            return width;
        }
    }

    private class TextHeightDimensionCalculator implements DimensionCalculator {

        @Override
        public float calculate(CalculationContext calculationContext) {
            float height = 0;
            for (TextLine line : getLines()) {
                height += lineHeight + line.getMarginTop();
            }
            height += sumVerticalMarginBorderPadding(Text.this);
            return height;
        }
    }

    private static class TextLine {

        private final String text;
        private final float marginTop;
        private final float leftOffset;

        public TextLine(String text, float marginTop, float leftOffset) {
            this.text = text;
            this.marginTop = marginTop;
            this.leftOffset = leftOffset;
        }

        public String getText() {
            return text;
        }

        public float getMarginTop() {
            return marginTop;
        }

        public float getLeftOffset() {
            return leftOffset;
        }
    }
}
