package com.github.vkuzel.simplepdflayout;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Element container can hold ChildElements.
 *
 * @see ChildElement
 */
public interface ParentElement<P extends ParentElement<P>> extends Element {

    default P addArrow(Consumer<Arrow> arrowConfigurer) {
        return addChild(Arrow::new, arrowConfigurer);
    }

    @SuppressWarnings("unchecked")
    default P addArrowIf(boolean condition, Consumer<Arrow> arrowConfigurer) {
        return condition ? addArrow(arrowConfigurer) : (P) this;
    }

    default P addBox(Consumer<Box> boxConfigurer) {
        return addChild(Box::new, boxConfigurer);
    }

    @SuppressWarnings("unchecked")
    default P addBoxIf(boolean condition, Consumer<Box> boxConfigurer) {
        return condition ? addBox(boxConfigurer) : (P) this;
    }

    default P addImage(Consumer<Image> imageConfigurer) {
        return addChild(Image::new, imageConfigurer);
    }

    @SuppressWarnings("unchecked")
    default P addImageIf(boolean condition, Consumer<Image> imageConfigurer) {
        return condition ? addImage(imageConfigurer) : (P) this;
    }

    default P addTable(Consumer<Table> tableConfigurer) {
        return addChild(Table::new, tableConfigurer);
    }

    @SuppressWarnings("unchecked")
    default P addTableIf(boolean condition, Consumer<Table> tableConfigurer) {
        return condition ? addTable(tableConfigurer) : (P) this;
    }

    default P addText(Consumer<Text> textConfigurer) {
        return addChild(Text::new, textConfigurer);
    }

    @SuppressWarnings("unchecked")
    default P addTextIf(boolean condition, Consumer<Text> textConfigurer) {
        return condition ? addText(textConfigurer) : (P) this;
    }

    <C extends ChildElement<C>> P addChild(Function<ParentElement<?>, C> childFactory, Consumer<C> childConfigurer);

    P removeChild(ChildElement<?> childElement);

    List<ChildElement<?>> getChildren();

    ChildElement<?> getPreviousChildTo(ChildElement<?> childElement);

    ChildElement<?> getNextChildTo(ChildElement<?> childElement);
}
