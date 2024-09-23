package ru.skqwk;

/**
 * Память
 */
public interface Memory {
    /**
     * Прочитать 16 бит по адресу {@code address}
     */
    char read(int address);

    /**
     * Записать 16 бит {@code value} по адресу {@code address}
     */
    void write(int address, char value);
}
