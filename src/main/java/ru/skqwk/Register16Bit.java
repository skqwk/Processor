package ru.skqwk;

/**
 * Регистр
 */
public class Register16Bit {
    /**
     * Хранит 16 битов
     */
    private char value;

    public Register16Bit(int value) {
        this.value = (char) value;
    }

    public void incrementBy(int value) {
        this.value += value;
    }

    public char value() {
        return this.value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = (char) value;
    }
}
