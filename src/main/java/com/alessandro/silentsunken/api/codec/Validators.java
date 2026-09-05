package com.alessandro.silentsunken.api.codec;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.mojang.serialization.DataResult;

import java.util.List;

@NotNullParams
public class Validators {
    public static <VALUE> DataResult<List<VALUE>> listWithSize(List<VALUE> value, int expectedSize) {
        return !value.isEmpty() ? DataResult.success(value) : DataResult.error(() -> "Excpected list size of " + expectedSize + ", but got " + value.size());
    }

    public static <VALUE> DataResult<List<VALUE>> nonEmptyList(List<VALUE> value) {
        return !value.isEmpty() ? DataResult.success(value) : DataResult.error(() -> "List must not be empty");
    }

    public static DataResult<String> nonBlankString(String value) {
        return !value.isBlank() ? DataResult.success(value) : DataResult.error(() -> "String value must not be empty");
    }
}
