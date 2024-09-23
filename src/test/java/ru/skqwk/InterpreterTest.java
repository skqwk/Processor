package ru.skqwk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class InterpreterTest {
    private final Interpreter interpreter = new Interpreter();

    @ParameterizedTest
    @MethodSource("parseDataProvider")
    void parse(String raw, String expected) {
        Assertions.assertEquals(expected, interpreter.parse(Collections.singletonList(raw)).get(0).toString());
    }

    private static Stream<Arguments> parseDataProvider() {
        return Stream.of(
                Arguments.of("LDA ?0", "LDA DIR 0000000000000000"),
                Arguments.of("STA /A", "STA REG 0000000000000001"),
                Arguments.of("ADD ?1", "ADD DIR 0000000000000001"),
                Arguments.of("STA /B", "STA REG 0000000000000010"),
                Arguments.of("CLEA", "CLEA DIR 0000000000001111"),
                Arguments.of("ADD @A", "ADD RIN 0000000000000001"),

                Arguments.of("STA /C", "STA REG 0000000000000100"),
                Arguments.of("INC /A", "INC REG 0000000000000001"),
                Arguments.of("CMP /B", "CMP REG 0000000000000010"),
                Arguments.of("LDA /C", "LDA REG 0000000000000100"),
                Arguments.of("JZ &5", "JZ IMM 0000000000000101"),
                Arguments.of("OUT", "OUT DIR 0000000000001111"),

                Arguments.of("STA /A", "STA REG 0000000000000001")
        );
    }

    @ParameterizedTest
    @MethodSource("parseWithStampsDataProvider")
    void parseWithStamps(List<String> raw, List<String> expected) {
        // GIVEN | WHEN
        List<Instruction> parsed = interpreter.parse(raw);

        // THEN
        List<String> result = parsed.stream().map(Instruction::toString).collect(Collectors.toList());
        for (int i = 0; i < result.size(); i++) {
            Assertions.assertEquals(expected.get(i), result.get(i));
        }
    }

    private static Stream<Arguments> parseWithStampsDataProvider() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                "LDA ?0",
                                "STA /A",
                                "LOOP: ADD @A",
                                "STA /C",
                                "JZ LOOP"
                        ),
                        List.of(
                                "LDA DIR 0000000000000000",
                                "STA REG 0000000000000001",
                                "ADD RIN 0000000000000001",
                                "STA REG 0000000000000100",
                                "JZ IMM 0000000000000010"
                        )
                ),

                Arguments.of(
                        List.of(
                                "LDA ?0",
                                "JZ &4",
                                "STA /A",
                                "STA /C",
                                "LOOP: ADD @A"
                        ),
                        List.of(
                                "LDA DIR 0000000000000000",
                                "JZ IMM 0000000000000100",
                                "STA REG 0000000000000001",
                                "STA REG 0000000000000100",
                                "ADD RIN 0000000000000001"
                        )
                )
        );
    }
}