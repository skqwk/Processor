package ru.skqwk;

/**
 * Описание основных регистров
 */
public class Registers {
    public static final int A = 0b001;
    public static final int B = 0b010;
    public static final int C = 0b100;

    public static int of(String address) {
        if ("A".equals(address)) {
            return A;
        } else if ("B".equals(address)) {
            return B;
        } else if ("C".equals(address)) {
            return C;
        }

        throw new RuntimeException("Неизвестный адрес регистра - " + address);
    }
}
