package ru.skqwk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Operation {
    /**
     * Код команды
     */
    private final Command commandCode;

    /**
     * Тип адресации
     */
    private final AddressingMode addressingMode;
}
