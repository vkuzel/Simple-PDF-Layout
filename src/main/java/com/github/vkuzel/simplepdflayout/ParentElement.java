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

    default P addBox(Consumer<Box> boxConfigurer) {
        return addChild(Box::new, boxConfigurer);
    }

    default P addImage(Consumer<Image> imageConfigurer) {
        return addChild(Image::new, imageConfigurer);
    }

    default P addTable(Consumer<Table> tableConfigurer) {
        return addChild(Table::new, tableConfigurer);
    }

    default P addText(Consumer<Text> textConfigurer) {
        return addChild(Text::new, textConfigurer);
    }

    <C extends ChildElement<C>> P addChild(Function<ParentElement<?>, C> childFactory, Consumer<C> childConfigurer);

    P removeChild(ChildElement<?> childElement);

    List<ChildElement<?>> getChildren();

    ChildElement<?> getPreviousChildTo(ChildElement<?> childElement);

    ChildElement<?> getNextChildTo(ChildElement<?> childElement);
}
