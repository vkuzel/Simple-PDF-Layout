package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.geometry.Dimension;
import com.github.vkuzel.simplepdflayout.geometry.Point;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

public class Image<T extends Image<T>> extends Box<T> {

    protected File imageFile;

    public T setImageFile(File imageFile) {
        this.imageFile= imageFile;
        return getThis();
    }

    @Override
    public void draw(PDDocument document, PDPageContentStream contentStream) {
        super.drawBackground(contentStream);
        drawImage(document, contentStream);
        super.drawBorders(contentStream);
        super.drawChildren(document, contentStream);
    }

    protected void drawImage(PDDocument document, PDPageContentStream contentStream) {
        if (!imageFile.exists()) {
            throw new IllegalStateException("Image file " + imageFile.getAbsolutePath() + " not found!");
        }

        Point topLeft = calculateContentTopLeft();
        Dimension dimension = calculateContentDimension();
        Point bottomLeft = new Point(topLeft.getX(), topLeft.getY() + dimension.getHeight());
        Point pdfBottomLeft = parent.convertPointToPdfCoordinates(bottomLeft);

        try {
            PDImageXObject image = PDImageXObject.createFromFileByContent(imageFile, document);
            contentStream.drawImage(image, pdfBottomLeft.getX(), pdfBottomLeft.getY(), dimension.getWidth(), dimension.getHeight());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
