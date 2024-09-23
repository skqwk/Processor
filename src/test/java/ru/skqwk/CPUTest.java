package ru.skqwk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CPUTest {

    @Test
    void inca() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.INCA, AddressingMode.DIRECT, 0).toChar()
        };

        CPU cpu = new CPU(new DataMemory(), new InstructionMemory(instructions));

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(1,  cpu.getAcc().value());
    }

    @Test
    void cmpWhenEqual() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.CMP, AddressingMode.IMMEDIATE, 204).toChar()
        };

        CPU cpu = new CPU(new DataMemory(), new InstructionMemory(instructions));
        cpu.getAcc().setValue(204);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(0,  cpu.getFlags().value());
    }

    @Test
    void cmpWhenEqualDirect() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.CMP, AddressingMode.DIRECT, 456).toChar()
        };

        DataMemory dataMemory = new DataMemory();
        dataMemory.getMemory()[456] = 204;

        CPU cpu = new CPU(dataMemory, new InstructionMemory(instructions));
        cpu.getAcc().setValue(204);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(0,  cpu.getFlags().value());
    }

    @Test
    void cmpWhenNonEqualDirect() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.CMP, AddressingMode.DIRECT, 456).toChar()
        };

        DataMemory dataMemory = new DataMemory();
        dataMemory.getMemory()[456] = 204;

        CPU cpu = new CPU(dataMemory, new InstructionMemory(instructions));
        cpu.getAcc().setValue(505);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(Flags.ZERO_FLAG,  cpu.getFlags().value());
    }

    @Test
    void jzWhenZero() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.JZ, AddressingMode.DIRECT, 12).toChar()
        };

        CPU cpu = new CPU(new DataMemory(), new InstructionMemory(instructions));
        cpu.getFlags().setValue(0);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(0,  cpu.getFlags().value());
    }

    @Test
    void jzWhenNonZero() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.JZ, AddressingMode.DIRECT, 12).toChar()
        };

        CPU cpu = new CPU(new DataMemory(), new InstructionMemory(instructions));
        cpu.getFlags().setValue(Flags.ZERO_FLAG);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(12,  cpu.getPc().value());
    }

    @Test
    void jzWhenNonZeroAndExecute() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.JZ, AddressingMode.DIRECT, 5).toChar(),
                Instruction.create(Command.INCA, AddressingMode.DIRECT, 0).toChar(),
                Instruction.create(Command.INCA, AddressingMode.DIRECT, 0).toChar(),
                Instruction.create(Command.INCA, AddressingMode.DIRECT, 0).toChar(),
                Instruction.create(Command.INCA, AddressingMode.DIRECT, 0).toChar(),
                Instruction.create(Command.LDA, AddressingMode.DIRECT, 17).toChar(),
        };

        DataMemory dataMemory = new DataMemory();
        dataMemory.getMemory()[17] = 88;
        CPU cpu = new CPU(dataMemory, new InstructionMemory(instructions));
        cpu.getFlags().setValue(Flags.ZERO_FLAG);

        // WHEN
        cpu.step();
        cpu.step();

        // THEN
        assertEquals(6,  cpu.getPc().value());
        assertEquals(88,  cpu.getAcc().value());
    }

    @Test
    void sta() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.STA, AddressingMode.DIRECT, 0).toChar()
        };

        DataMemory dataMemory = new DataMemory();

        CPU cpu = new CPU(dataMemory, new InstructionMemory(instructions));
        cpu.getAcc().setValue(12);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(12,  dataMemory.getMemory()[0]);
    }

    @Test
    void ldaImmediate() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.LDA, AddressingMode.IMMEDIATE, 456).toChar()
        };

        DataMemory dataMemory = new DataMemory();

        CPU cpu = new CPU(dataMemory, new InstructionMemory(instructions));

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(456,  cpu.getAcc().value());
    }

    @Test
    void ldaDirect() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.LDA, AddressingMode.DIRECT, 0).toChar()
        };

        DataMemory dataMemory = new DataMemory();
        dataMemory.getMemory()[0] = 7;

        CPU cpu = new CPU(dataMemory, new InstructionMemory(instructions));

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(7,  cpu.getAcc().value());
    }

    @Test
    void ldaRegisterA() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.LDA, AddressingMode.REGISTER, Registers.A).toChar()
        };

        CPU cpu = new CPU(new DataMemory(), new InstructionMemory(instructions));
        cpu.getA().setValue(9);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(9,  cpu.getAcc().value());
    }

    @Test
    void ldaRegisterB() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.LDA, AddressingMode.REGISTER, Registers.B).toChar()
        };

        CPU cpu = new CPU(new DataMemory(), new InstructionMemory(instructions));
        cpu.getB().setValue(99);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(99,  cpu.getAcc().value());
    }

    @Test
    void ldaRegisterC() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.LDA, AddressingMode.REGISTER, Registers.C).toChar()
        };

        CPU cpu = new CPU(new DataMemory(), new InstructionMemory(instructions));
        cpu.getC().setValue(86);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(86,  cpu.getAcc().value());
    }

    @Test
    void ldaRegisterIndirect() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.LDA, AddressingMode.REGISTER_INDIRECT, Registers.B).toChar()
        };

        DataMemory dataMemory = new DataMemory();
        dataMemory.getMemory()[2] = 124;
        CPU cpu = new CPU(dataMemory, new InstructionMemory(instructions));
        cpu.getB().setValue(2);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(124,  cpu.getAcc().value());
    }

    @Test
    void addRegisterIndirect() {
        // GIVEN
        char[] instructions = new char[] {
                Instruction.create(Command.ADD, AddressingMode.REGISTER_INDIRECT, Registers.C).toChar()
        };

        DataMemory dataMemory = new DataMemory();
        dataMemory.getMemory()[1] = 7;

        CPU cpu = new CPU(dataMemory, new InstructionMemory(instructions));
        cpu.getC().setValue(1);
        cpu.getAcc().setValue(12);

        // WHEN
        cpu.step();

        // THEN
        assertEquals(1,  cpu.getPc().value());
        assertEquals(19,  cpu.getAcc().value());
    }
}