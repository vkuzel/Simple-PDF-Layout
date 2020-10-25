# Simple PDF Library

[![](https://jitpack.io/v/vkuzel/Simple-PDF-Layout.svg)](https://jitpack.io/#vkuzel/Simple-PDF-Layout)

A library which makes it easier to manually create a simple PDF document with [Apache PDFBox](https://pdfbox.apache.org).

## Features

* HTML-like element model.
* Reversed Y-axis so a point (0, 0) is at the top-left corner of the page.
* Few basic elements for drawing boxes, texts, tables and arrows.

## Getting started

1. Include the library into your project.

    ```kotlin
    // build.gradle.kts
    repositories {
        maven("https://jitpack.id")
    }
    
    dependencies {
        compile("com.github.vkuzel:Simple-PDF-Layout:2.0.0")
    }
    ```

2. Use it

    ```java
    try (PDDocument document = new PDDocument()) {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
    
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Create a text somewhere at the page and align it to the right in a box of width 100.
            Text text = new Text<>().setTopLeft(100, 100).setWidth(100).setText("Hello World!").setAlignment(Text.Alignment.RIGHT);
            // Add an arrow pointing to text.
            Arrow arrow = new Arrow().setStartElement(text);
            // Create parent element, add text and arrow to it and draw it.
            new Page<>().addChild(text).addChild(arrow).draw(document, contentStream);
        }
    
        document.save("hello-world.pdf");
    }
    ```

## Box model

Following image was drawn by `BoxModelDocumentTest.generateBoxModelDocumentTest()` method in this project.

![Document box model](document-box-model.png)
