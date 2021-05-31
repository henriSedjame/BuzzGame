package io.gitlab.hsedjame.buzz.services;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Utils {

    public static <T, R> R applyWith(Supplier<T> supplier, Function<T, R>function) {
        return function.apply(supplier.get());
    }

    public static <T> void  consumeWith(Supplier<T> supplier, Consumer<T> consumer) {
        consumer.accept(supplier.get());
    }
}
