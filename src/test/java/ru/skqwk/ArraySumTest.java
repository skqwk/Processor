package ru.skqwk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        char[] instructions = new char[] {
                Instruction.create(Command.LDA, AddressingMode.DIRECT, 1).toChar(),
                Instruction.create(Command.STA, AddressingMode.REGISTER, Registers.A).toChar(),
                Instruction.create(Command.ADD, AddressingMode.DIRECT, 0).toChar(),
                Instruction.create(Command.STA, AddressingMode.REGISTER, Registers.B).toChar(),
                Instruction.createImplied(Command.CLEA).toChar(),
                Instruction.create(Command.ADD, AddressingMode.REGISTER_INDIRECT, Registers.A).toChar(),
                Instruction.create(Command.STA, AddressingMode.REGISTER, Registers.C).toChar(),
                Instruction.create(Command.INC, AddressingMode.REGISTER, Registers.A).toChar(),
                Instruction.create(Command.STA, AddressingMode.REGISTER, Registers.A).toChar(),
                Instruction.create(Command.CMP, AddressingMode.REGISTER, Registers.B).toChar(),
                Instruction.create(Command.LDA, AddressingMode.REGISTER, Registers.C).toChar(),
                Instruction.create(Command.JZ, AddressingMode.IMMEDIATE, 5).toChar(),
                Instruction.createImplied(Command.OUT).toChar(),
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
