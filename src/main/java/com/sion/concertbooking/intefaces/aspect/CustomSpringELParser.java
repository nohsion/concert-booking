package com.sion.concertbooking.intefaces.aspect;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomSpringELParser {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    public static Object getDynamicValue(String[] parameters, Object[] args, String key) {
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameters.length; i++) {
            context.setVariable(parameters[i], args[i]);
        }

        return PARSER.parseExpression(key).getValue(context, Object.class);
    }

    public static Object[] getDynamicValues(String[] parameters, Object[] args, String[] keys) {
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameters.length; i++) {
            context.setVariable(parameters[i], args[i]);
        }

        return Arrays.stream(keys)
                .map(PARSER::parseExpression)
                .map(expression -> expression.getValue(context, Object.class))
                .filter(Objects::nonNull)
                .flatMap(value -> {
                    if (value instanceof Collection<?> collection) {
                        return collection.stream();
                    }
                    if (value instanceof Object[] array) {
                        return Arrays.stream(array);
                    }
                    return Stream.of(value);
                })
                .toArray();
    }
}
