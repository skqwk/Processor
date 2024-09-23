package ru.skqwk;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressingModeTest {

    @ParameterizedTest
    @MethodSource("ofValueDataProvider")
    void ofValue(int value, AddressingMode expected) {
        assertEquals(expected, AddressingMode.ofIntegerValue(value));
    }

    private static Stream<Arguments> ofValueDataProvider() {
        return Stream.of(
                Arguments.of(0, AddressingMode.IMMEDIATE),
                Arguments.of(1, AddressingMode.DIRECT),
                Arguments.of(2, AddressingMode.REGISTER),
                Arguments.of(3, AddressingMode.REGISTER_INDIRECT)
        );
    }
}