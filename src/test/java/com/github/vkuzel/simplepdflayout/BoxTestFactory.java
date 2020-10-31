package com.github.vkuzel.simplepdflayout;

import java.util.concurrent.atomic.AtomicReference;

public class BoxTestFactory {

    public static Box emptyBox() {
        AtomicReference<Box> reference = new AtomicReference<>();
        Page.a4().addBox(reference::set);
        return reference.get();
    }
}
