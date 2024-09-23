package ru.skqwk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Instruction {
    /**
     * Размер всех инструкций:
     * <ul>
     *     <li>4 бита на код команды</li>
     *     <li>2 бита на тип адресации</li>
     *     <li>10 на адрес в памяти</li>
     * </ul>
     *
     * Т.к. все регистры 16 битов - инструкция целиком помещается в регистр
     */
    private static final int SIZE = 1;

    public static final int ADDRESS_SIZE = 10;
    public static final int COMMAND_CODE_SIZE = 4;
    public static final int TWO_BYTES_MINUS_COMMAND_CODE_SIZE = 12;

    /**
     * Операция, к-ю необходимо выполнить
     */
    private final Operation operation;

    /**
     * Адрес, по к-му лежат данные
     */
    private final int address;

    public int size() {
        return SIZE;
    }

    public static Instruction of(int value) {
        return Instruction.of((char) value);
    }

    public static Instruction of(char value) {
        int code = value >> TWO_BYTES_MINUS_COMMAND_CODE_SIZE;
        Command commandCode = Command.ofCode(code);

        int modeValue = (value & 0b0000_1100_0000_0000) >> ADDRESS_SIZE;
        Operation operation = new Operation(commandCode, AddressingMode.ofIntegerValue(modeValue));

        int address = value & 0b0000_0011_1111_1111;
        return new Instruction(operation, address);
    }

    public static Instruction create(Command commandMode, AddressingMode addressingMode, int address) {
        return new Instruction(new Operation(commandMode, addressingMode), address);
    }

    /**
     * Поскольку часть команд не подразумевает использование какой-либо адресации и работает только с {@code аккумулятором}
     * есть отдельный метод, в котором передаются константные значения для типа адресации и значение адреса.
     * <br>
     * Можно использовать его для следующих команд:
     * <ul>
     *      <li>{@link Command#INP}</li>
     *      <li>{@link Command#INCA}</li>
     *      <li>{@link Command#CLEA}</li>
     *      <li>{@link Command#OUT}</li>
     * </ul>
     */
    public static Instruction createImplied(Command commandMode) {
        return Instruction.create(commandMode, AddressingMode.DIRECT, 0xF);
    }

    public char toChar() {
        Operation operation = this.getOperation();
        byte commandByteCode = operation.getCommandCode().getCode();
        int addressingModeValue = operation.getAddressingMode().getValue();

        char result = (char) (commandByteCode << TWO_BYTES_MINUS_COMMAND_CODE_SIZE);
        result = (char) (result | (addressingModeValue << ADDRESS_SIZE));
        result = (char) (result | address);

        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s",
                operation.getCommandCode().name(),
                operation.getAddressingMode().getShortName(),
                String.format("%16s", Integer.toBinaryString(address)).replace(" ", "0"));
    }
}
