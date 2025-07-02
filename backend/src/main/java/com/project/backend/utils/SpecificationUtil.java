package com.project.backend.utils;

import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;
import java.util.function.Supplier;

public class SpecificationUtil {
    public static boolean isValid(String value) {
        return value != null && !value.isBlank() && !value.equals("null");
    }

    public static boolean isValid(Boolean value) {
        return value != null;
    }

    public static <T, R> Specification<T> addSpecification(Specification<T> specification, Function<R, Specification<T>> addFunction, R value) {
        Specification<T> newSpec = addFunction.apply(value);
        return (specification == null) ? newSpec : specification.and(newSpec);
    }
    public static <T> Specification<T> addSpecification(Specification<T> specification, Supplier<Specification<T>> addFunction) {
        Specification<T> newSpec = addFunction.get();
        return (specification == null) ? newSpec : specification.and(newSpec);
    }
}
