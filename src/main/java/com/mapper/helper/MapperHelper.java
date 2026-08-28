package com.mapper.helper;

import com.exceptions.common.NullMapperInputException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class MapperHelper {

    private MapperHelper() {
    }

    public static void checkIfMapperInputIsNull(Object... objects) {

        boolean hasNull = Arrays.stream(objects).anyMatch(Objects::isNull);

        if (hasNull) {

            throw new NullMapperInputException();
        }
    }

    public static <T, R> List<R> mapList(List<T> sourceList, Function<T, R> mapper) {

        checkIfMapperInputIsNull(sourceList);

        return sourceList.stream().map(mapper).collect(Collectors.toList());
    }

    public static LocalDateTime truncateToMinute(LocalDateTime localDateTime) {

        return localDateTime != null ? localDateTime.withSecond(0) : null;
    }

    public static <T> T orDefault(T value, T defaultValue) {

        return Optional.ofNullable(value).orElse(defaultValue);
    }
}
