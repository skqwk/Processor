package ru.skqwk;

import lombok.Getter;

@Getter
public class CPU {
    /**
     * Память с данными
     */
    private final Memory dataMemory;

    /**
     * Память с инструкциями (в гарвардской архитектуре она отдельно)
     */
    private final Memory instructionMemory;

    /**
     * Счетчик инструкций
     */
    private final Register16Bit pc;

    /**
     * Аккумулятор
     */
    private final Register16Bit acc;

    /**
     * Регистр флагов
     */
    private final Register16Bit flags;

    /**
     * Зарезервированные регистры
     */
    private final Register16Bit a;
    private final Register16Bit b;
    private final Register16Bit c;

    public CPU(Memory dataMemory, Memory instructionMemory) {
        this.dataMemory = dataMemory;
        this.instructionMemory = instructionMemory;

        this.pc = new Register16Bit(0x0000);
        this.acc = new Register16Bit(0x0000);
        this.flags = new Register16Bit(0x0000);

        this.a = new Register16Bit(0x0000);
        this.b = new Register16Bit(0x0000);
        this.c = new Register16Bit(0x0000);
    }

    public void step() {
        Instruction instruction = readNextInstruction();
        pc.incrementBy(instruction.size());
        execute(instruction);
    }

    private void execute(Instruction instruction) {
        Operation operation = instruction.getOperation();
        AddressingMode addressingMode = operation.getAddressingMode();
        Command commandCode = operation.getCommandCode();
        int address = instruction.getAddress();

        MemoryCell cell = defineMemoryCell(addressingMode, address);

        System.out.printf("Execute instruction - %s\n", instruction);

        switch (commandCode) {
            case LDA -> acc.setValue(cell.read());
            case STA -> cell.write(acc.value());
            case ADD -> {
                int result = acc.value() + cell.read();
                acc.setValue(result);
            }
            case SUB -> {
                int result = acc.value() - cell.read();
                acc.setValue(result);
            }
            case AND -> {
                int result = acc.value() & cell.read();
                acc.setValue(result);
            }
            case OR -> {
                int result = acc.value() | cell.read();
                acc.setValue(result);
            }
            case XOR -> throw new RuntimeException();
            case COM -> throw new RuntimeException();
            case CMP -> {
                char read = cell.read();
                if (acc.value() == read) {
                    flags.setValue(0);
                } else {
                    flags.setValue(Flags.ZERO_FLAG);
                }
            }
            case INC -> acc.setValue(cell.read() + 1);
            case DEC -> acc.setValue(cell.read() - 1);
            case JZ -> {
                int result = flags.value() & Flags.ZERO_FLAG;
                if (result != 0) {
                    // TODO: или тоже читать из cell.read() ?
                    pc.setValue(address);
                }
            }
            case CLEA -> acc.setValue(0);
            case INCA -> acc.setValue(acc.value() + 1);
            case INP -> throw new RuntimeException();
            case OUT -> System.out.println((int) acc.value());
        }
    }

    /**
     * Определить ячейку для записи/чтения информации по адресу и типу адресации
     */
    private MemoryCell defineMemoryCell(AddressingMode addressingMode, int address) {
        return switch (addressingMode) {
            // По факту нужно добавить ещё один тип адресации, но свободных битов больше нет и в целом так норм
            // case IMPLIED -> null;
            case IMMEDIATE -> new MemoryCellImpl(() -> (char) address, (value) -> {});
            case DIRECT -> new MemoryCellImpl(() -> dataMemory.read(address), (value) -> dataMemory.write(address, value));
            case REGISTER -> defineRegister(address);
            case REGISTER_INDIRECT -> new MemoryCellImpl(() -> dataMemory.read(defineRegister(address).read()), (value) -> {});
        };
    }

    private MemoryCell defineRegister(int address) {
        return switch (address) {
            case Registers.A -> new MemoryCellImpl(a::value, a::setValue);
            case Registers.B -> new MemoryCellImpl(b::value, b::setValue);
            case Registers.C -> new MemoryCellImpl(c::value, c::setValue);
            default -> throw new IllegalStateException("Unexpected value: " + address);
        };
    }

    private Instruction readNextInstruction() {
        char value = instructionMemory.read(pc.value());
        return Instruction.of(value);
    }
}
