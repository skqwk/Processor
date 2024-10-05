package ru.skqwk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ru.skqwk.AddressingMode.*;
import static ru.skqwk.Command.*;
import static ru.skqwk.Registers.*;

public class ArraySumTest {
    @Test
    void arraySumTest() {
        // GIVEN
        DataMemory dataMemory = new DataMemory();
        dataMemory.getMemory()[0] = 6; // Размер массива
        dataMemory.getMemory()[1] = 10; // Адрес первого элемента массива
        // Инициализация массива
        dataMemory.getMemory()[10] = 10;
        dataMemory.getMemory()[11] = 5;
        dataMemory.getMemory()[12] = 3;
        dataMemory.getMemory()[13] = 7;
        dataMemory.getMemory()[14] = 8;
        dataMemory.getMemory()[15] = 7;

        char[] instructions = new char[]{
                Instruction.create(LDA, DIRECT, 1).toChar(),
                Instruction.create(STA, REGISTER, A).toChar(),
                Instruction.create(ADD, DIRECT, 0).toChar(),
                Instruction.create(STA, REGISTER, B).toChar(),
                Instruction.createImplied(CLEA).toChar(),
                Instruction.create(ADD, REGISTER_INDIRECT, A).toChar(),
                Instruction.create(STA, REGISTER, C).toChar(),
                Instruction.create(INC, REGISTER, A).toChar(),
                Instruction.create(STA, REGISTER, A).toChar(),
                Instruction.create(CMP, REGISTER, B).toChar(),
                Instruction.create(LDA, REGISTER, C).toChar(),
                Instruction.create(JZ, IMMEDIATE, 5).toChar(),
                Instruction.createImplied(OUT).toChar(),
        };

        CPU cpu = new CPU(dataMemory, new InstructionMemory(instructions));

        // WHEN
        while (cpu.getPc().value() != instructions.length) {
            cpu.step();
        }

        // THEN
        Assertions.assertEquals(40, cpu.getAcc().value());
    }

    @Test
    void arraySumTestWithByteCode() {
        // GIVEN
        DataMemory dataMemory = new DataMemory();
        dataMemory.getMemory()[0] = 5; // Размер массива
        dataMemory.getMemory()[1] = 10; // Адрес первого элемента массива
        // Инициализация массива
        dataMemory.getMemory()[10] = 10;
        dataMemory.getMemory()[11] = 5;
        dataMemory.getMemory()[12] = 3;
        dataMemory.getMemory()[13] = 7;
        dataMemory.getMemory()[14] = 8;

        List<String> instructions = List.of(
                "LDA ?1",
                "STA /A",
                "ADD ?0",
                "STA /B",
                "CLEA",
                "LOOP123: ADD @A",
                "STA /C",
                "INC /A",
                "STA /A",
                "CMP /B",
                "LDA /C",
                "JZ LOOP123",
                "OUT"
        );

        Interpreter interpreter = new Interpreter();

        List<Instruction> parsed = interpreter.parse(instructions);
        char[] byteInstructions = new char[instructions.size()];
        for (int i = 0; i < parsed.size(); i++) {
            byteInstructions[i] = parsed.get(i).toChar();
        }

        CPU cpu = new CPU(dataMemory, new InstructionMemory(byteInstructions));

        // WHEN
        while (cpu.getPc().value() != byteInstructions.length) {
            cpu.step();
        }

        // THEN
        Assertions.assertEquals(33, cpu.getAcc().value());
    }
}
