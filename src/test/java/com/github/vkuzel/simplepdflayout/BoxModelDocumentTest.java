package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.property.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.github.vkuzel.simplepdflayout.Text.Alignment.CENTER;
import static java.awt.Color.*;

public class BoxModelDocumentTest {

    private static final Line LINE = Line.of().withWidth(1).withColor(BLACK);
    private static final Line BORDER_TOP_BOTTOM = Line.of().withWidth(25).withColor(CYAN);
    private static final Line BORDER_LEFT_RIGHT = Line.of().withWidth(50).withColor(CYAN);

    @Disabled
    @Test
    void generateBoxModelDocumentTest() {
        Page page = Page.a4()
                .addBox(box -> box
                        .setTopLeft(97, 100)
                        .setDimension(400, 200)
                        .setMargin(Margin.of(25, 50, 25, 50))
                        .setBorder(Border.of(BORDER_TOP_BOTTOM, BORDER_LEFT_RIGHT, BORDER_TOP_BOTTOM, BORDER_LEFT_RIGHT))
                        .setPadding(Padding.of(25, 50, 25, 50))
                        .addBox(box1 -> box1
                                .setDimensionPercent(100, 100)
                                .setBackgroundColor(PINK))
                )
                .addBox(this::createTopLeftPointer)
                .addBox(this::createAxes)
                .addBox(box1 -> createHorizontalDimension(box1, Point.of(247, 300), Point.of(347, 350), "content width"))
                .addBox(box1 -> createHorizontalDimension(box1, Point.of(97, 300), Point.of(147, 350), "margin"))
                .addBox(box1 -> createHorizontalDimension(box1, Point.of(147, 300), Point.of(197, 350), "border"))
                .addBox(box1 -> createHorizontalDimension(box1, Point.of(197, 300), Point.of(247, 350), "padding"))
                .addBox(box1 -> createHorizontalDimension(box1, Point.of(197, 300), Point.of(397, 390), "background width"))
                .addBox(box1 -> createHorizontalDimension(box1, Point.of(97, 300), Point.of(497, 430), "width"))
                .addText(text -> text
                        .setTopLeftPercent(0, 90)
                        .setText("Generated by " + this.getClass().getSimpleName())
                        .setAlignment(CENTER));

        Document.renderPageToFile(page, "document-box-model.pdf");
    }

    private void createTopLeftPointer(Box box) {
        Point topLeft = Point.of(97, 100);

        box
                .addText(text -> text
                        .setTopLeft(60, 50)
                        .setText("top-left"))
                .addArrow(arrow -> arrow
                        .setStartPoint(topLeft)
                        .setEndPosition(80, 70)
                        .setLine(LINE));
    }

    private void createAxes(Box box) {
        Point topLeft = Point.of(20, 20);
        Point topRight = Point.of(60, topLeft.getX());
        Point bottomLeft = Point.of(topLeft.getX(), 60);

        box
                .addArrow(arrow -> arrow
                        .setStartPoint(topRight)
                        .setEndPoint(topLeft)
                        .setLine(LINE))
                .addArrow(arrow -> arrow
                        .setStartPoint(bottomLeft)
                        .setEndPoint(topLeft)
                        .setLine(LINE))
                .addText(text -> text
                        .setTopLeft(topRight.getX() + 3, topRight.getY() - 8)
                        .setText("x"))
                .addText(text -> text
                        .setTopLeft(bottomLeft.getX() - 3, bottomLeft.getY())
                        .setText("y"));
    }

    private void createHorizontalDimension(Box box, Point topLeft, Point bottomRight, String text) {
        Point topRight = Point.of(bottomRight.getX(), topLeft.getY());
        Point bottomLeft = Point.of(topLeft.getX(), bottomRight.getY());

        box
                .addArrow(arrow -> arrow
                        .setStartPoint(bottomLeft)
                        .setEndPoint(bottomRight)
                        .setEndArrow(true)
                        .setLine(LINE))
                .addArrow(arrow -> arrow
                        .setStartPoint(topLeft)
                        .setEndPoint(bottomLeft)
                        .setStartArrow(false)
                        .setLine(LINE))
                .addArrow(arrow -> arrow
                        .setStartPoint(topRight)
                        .setEndPoint(bottomRight)
                        .setStartArrow(false)
                        .setLine(LINE))
                .addText(text1 -> text1
                        .setTopLeft(bottomLeft.getX(), bottomLeft.getY() - 18)
                        .setWidth(bottomRight.getX() - bottomLeft.getX())
                        .setText(text)
                        .setAlignment(CENTER));
    }
}
