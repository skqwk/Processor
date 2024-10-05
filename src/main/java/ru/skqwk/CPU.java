package ru.skqwk;

import lombok.Getter;

import java.io.IOException;

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
        executeWrapped(instruction);
    }

    private void executeWrapped(Instruction instruction) {
        try {
            execute(instruction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void execute(Instruction instruction) throws IOException {
        log(instruction);
        Operation operation = instruction.getOperation();
        AddressingMode addressingMode = operation.getAddressingMode();
        Command commandCode = operation.getCommandCode();
        int address = instruction.getAddress();

        MemoryCell cell = defineMemoryCell(addressingMode, address);
        switch (commandCode) {
            case LDA -> acc.setValue(cell.read());
            case STA -> cell.write(acc.value());
            case ADD -> acc.setValue(acc.value() + cell.read());
            case SUB -> acc.setValue(acc.value() - cell.read());
            case AND -> acc.setValue(acc.value() & cell.read());
            case OR -> acc.setValue(acc.value() | cell.read());
            case XOR -> acc.setValue(acc.value() ^ cell.read());
            case COM -> acc.setValue(~ cell.read());
            case CMP -> flags.setValue(acc.value() == cell.read() ? 0 : Flags.ZERO_FLAG);
            case INC -> acc.setValue(cell.read() + 1);
            case DEC -> acc.setValue(cell.read() - 1);
            case JZ -> {
                int result = flags.value() & Flags.ZERO_FLAG;
                if (result != 0) {
                    pc.setValue(address);
                }
            }
            case CLEA -> acc.setValue(0);
            case INCA -> acc.setValue(acc.value() + 1);
            case INP -> acc.setValue(System.in.read());
            case OUT -> System.out.println((int) acc.value());
        }
    }

    private void log(Instruction instruction) {
        System.out.printf("Execute instruction - %s\n", instruction);
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
