package com.project.backend.utils;

import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;
import java.util.function.Supplier;

public class SpecificationUtil {
    public static boolean isValid(String value) {
        return value != null && !value.isBlank() && !value.equals("null");
    }

    public static <T> Specification<T> addSpecification(Specification<T> specification, Function<String, Specification<T>> addFunction, String value) {
        Specification<T> newSpec = addFunction.apply(value);
        return (specification == null) ? newSpec : specification.and(newSpec);
    }
    public static <T> Specification<T> addSpecification(Specification<T> specification, Supplier<Specification<T>> addFunction) {
        Specification<T> newSpec = addFunction.get();
        return (specification == null) ? newSpec : specification.and(newSpec);
    }
}
