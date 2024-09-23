package ru.skqwk;

import lombok.RequiredArgsConstructor;

/**
 * Память для инструкций
 */
@RequiredArgsConstructor
public class InstructionMemory implements Memory {
    private final char[] memory;

    @Override
    public char read(int address) {
        return this.memory[address];
    }

    @Override
    public void write(int address, char value) {
        this.memory[address] = value;
    }
}
