package ru.skqwk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Типы адресации
 */
@Getter
@RequiredArgsConstructor
public enum AddressingMode {
    /**
     * Непосредственная.<br>
     * LDA &30 - загрузить в аккумулятор слово "30", в команде содержится сам операнд, загрузка его из памяти не требуется
     */
    IMMEDIATE("Непосредственная", "IMM", "&", 0b00),

    /**
     * Прямая.<br>
     * LDA ?30 - Загрузить в аккумулятор слово из ячейки с адресом 30
     */
    DIRECT("Прямая", "DIR", "?", 0b01),

    /**
     * Регистровая.<br>
     * LDA /A - Загрузить в аккумулятор слово из регистра A
     */
    REGISTER("Регистровая", "REG", "/", 0b10),

    /**
     * Косвенно-регистровая<br>
     * LDA @30 - загрузить в аккумулятор слово из ячейки, адрес которой находится в ячейке 30, в команде содержится адрес адреса операнда
     */
    REGISTER_INDIRECT("Косвенно-регистровая" , "RIN", "@", 0b11);

    /**
     * Название
     */
    private final String name;

    /**
     * Краткое название (для логов)
     */
    private final String shortName;

    /**
     * Обозначение
     */
    private final String symbol;

    /**
     * Значения битов
     */
    private final int value;

    public static AddressingMode ofIntegerValue(int value) {
        return Arrays.stream(AddressingMode.values())
                .filter(mode -> mode.value == value)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Не найден тип адресации для значения - " + value));
    }

    public static AddressingMode ofSymbol(String symbol) {
        return Arrays.stream(AddressingMode.values())
                .filter(mode -> symbol.equals(mode.getSymbol()))
                .findFirst()
                .orElse(null);
    }
}
