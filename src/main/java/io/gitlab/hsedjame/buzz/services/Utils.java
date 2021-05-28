package io.gitlab.hsedjame.buzz.services;

import java.util.function.Function;
import java.util.function.Supplier;

public class Utils {

    public static <T, R> R with(Supplier<T> supplier, Function<T, R>function) {
        return function.apply(supplier.get());
    }
}
