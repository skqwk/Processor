package ru.skqwk;

/**
 * Ячейка памяти
 */
public interface MemoryCell {
    /**
     * Возвращает значение
     */
    char read();

    /**
     * Устанавливает значение
     */
    void write(char value);
}
