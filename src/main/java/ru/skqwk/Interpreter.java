package ru.skqwk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {
    private static final String DELITEMER = ": ";

    private final Map<String, Integer> stamps = new HashMap<>();

    private Instruction parse(String instruction, int index) {
        if (instruction.contains(DELITEMER)) {
            String[] splitted = instruction.split(DELITEMER);
            return parse(splitted[1]);
        }

        return parse(instruction);
    }

    private Instruction parse(String instruction) {
        if (instruction.startsWith(Command.CLEA.name())) {
            return Instruction.createImplied(Command.CLEA);
        }
        if (instruction.startsWith(Command.INCA.name())) {
            return Instruction.createImplied(Command.CLEA);
        }
        if (instruction.startsWith(Command.OUT.name())) {
            return Instruction.createImplied(Command.OUT);
        }

        String[] parts = instruction.split(" ");
        Command command = Command.valueOf(parts[0]);

        String addressRaw = parts[1];
        String mode = addressRaw.substring(0, 1);
        AddressingMode addressingMode = AddressingMode.ofSymbol(mode);

        String address = addressRaw.substring(1);
        int numericAddress = getAddress(address, addressingMode);

        return Instruction.create(command, addressingMode, numericAddress);
    }

    public List<Instruction> parse(List<String> instructions) {
        List<String> mutableInstructions = new ArrayList<>(instructions);
        preProcessWithDelimeter(mutableInstructions);
        preProcessWithJmp(mutableInstructions);

        List<Instruction> parsed = new ArrayList<>();
        for (int i = 0; i < mutableInstructions.size(); i++) {
            parsed.add(parse(mutableInstructions.get(i), i));
        }
        return parsed;
    }

    private void preProcessWithDelimeter(List<String> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            String instruction = instructions.get(i);
            if (instruction.contains(DELITEMER)) {
                String[] splitted = instruction.split(DELITEMER);
                stamps.put(splitted[0], i);
            }
        }
    }

    private void preProcessWithJmp(List<String> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            String instruction = instructions.get(i);
            if (instruction.startsWith(Command.JZ.name())) {
                String[] parts = instruction.split(" ");
                Command command = Command.valueOf(parts[0]);

                String addressRaw = parts[1];
                String mode = addressRaw.substring(0, 1);
                AddressingMode addressingMode = AddressingMode.ofSymbol(mode);
                if (addressingMode == null && command == Command.JZ) {
                    instructions.set(i, String.format("%s %s%s", Command.JZ, AddressingMode.IMMEDIATE.getSymbol(), stamps.get(addressRaw)));
                }
            }
        }
    }

    private int getAddress(String address, AddressingMode addressingMode) {
        return switch (addressingMode) {
            case DIRECT, IMMEDIATE -> Integer.parseInt(address);
            case REGISTER, REGISTER_INDIRECT -> Registers.of(address);
        };
    }
}
