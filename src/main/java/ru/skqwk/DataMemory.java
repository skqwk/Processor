package ru.skqwk;

import lombok.Getter;

/**
 * Память для данных
 */
@Getter
public class DataMemory implements Memory {
    public static final int SIZE = 1024;

    private final char[] memory = new char[SIZE];

    @Override
    public char read(int address) {
        return this.memory[address];
    }

    @Override
    public void write(int address, char value) {
        this.memory[address] = value;
    }
}
