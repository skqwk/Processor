package ru.skqwk;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstructionTest {
    @ParameterizedTest
    @MethodSource("ofData")
    void of(int code, String expected) {
        // WHEN
        String actualResult = Instruction.of(code).toString();

        // THEN
        assertEquals(expected, actualResult);
    }

    private static Stream<Arguments> ofData() {
        return Stream.of(
                Arguments.of(0b1111_0000_0000_0000, "OUT IMM 0000000000000000"),
                Arguments.of(0b0001_1010_1111_0000, "STA REG 0000001011110000")
        );
    }

    @ParameterizedTest
    @MethodSource("toCharData")
    void toChar(Instruction instruction, int expected) {
        // WHEN
        char actualResult = instruction.toChar();

        // THEN
        assertEquals(expected, actualResult);
    }

    private static Stream<Arguments> toCharData() {
        return Stream.of(
                Arguments.of(Instruction.create(Command.OUT, AddressingMode.IMMEDIATE, 0), 0b1111_0000_0000_0000),
                Arguments.of(Instruction.create(Command.STA, AddressingMode.REGISTER, 0b1011110000), 0b0001_1010_1111_0000)
        );
    }
}