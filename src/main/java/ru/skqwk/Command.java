package ru.skqwk;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Command {
    /**
     * Выгрузить значение в аккумулятор
     * <br>
     * AC <- M[AD]
     */
    LDA(0b0000),

    /**
     * Выгрузить из аккумулятора в память
     * <br>
     * M[AD] <- AC
     */
    STA(0b0001),

    /**
     * Сложить
     * <br>
     * AC <- AC + M[AD]
     */
    ADD(0b0010),

    /**
     * Вычесть
     * <br>
     * AC <- AC - M[AD]
     */
    SUB(0b0011),

    /**
     * Конъюнкция
     * <br>
     * AC <- AC & M[AD]
     */
    AND(0b0100),

    /**
     * Дизъюнкция
     * <br>
     * AC <- AC | M[AD]
     */
    OR(0b0101),

    /**
     * XOR
     * <br>
     * AC <- AC xor M[AD]
     */
    XOR(0b0110),

    /**
     * Обратный код
     * <br>
     * AC <- !M[AD]
     */
    COM(0b0111),

    /**
     * Сравнить с аккумулятором
     * <br>
     * <ul>
     *     <li>AC == M[AD] => ZF := 0</li>
     *     <li>AC > M[AD] => CF := 0</li>
     *     <li>AC < M[AD] => CF := 1</li>
     * </ul>
     */
    CMP(0b1000),

    /**
     * Инкремент
     * <br>
     * AC <- M[AD] + 1
     */
    INC(0b1001),

    /**
     * Декремент
     * <br>
     * AC <- M[AD] - 1
     */
    DEC(0b1010),

    /**
     * Переход, если ZF != 0
     * <br>
     * PC <- AD
     */
    JZ(0b1011),

    /**
     * Очистка
     * <br>
     * AC <- 0
     */
    CLEA(0b1100),

    /**
     * Инкремент аккумулятора
     * <br>
     * AC <- AC + 1
     */
    INCA(0b1101),

    /**
     * Ввод
     * <br>
     * AC <- INPR
     */
    INP(0b1110),

    /**
     * Вывод
     * <br>
     * OUTR <- AC
     */
    OUT(0b1111);

    private final byte code;

    Command(int code) {
        this.code = (byte) code;
    }

    public static Command ofCode(int code) {
        return Arrays.stream(Command.values())
                .filter(command -> command.code == code)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Не найдена команда для кода - " + code));
    }
}
