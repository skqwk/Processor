package ru.skqwk;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class MemoryCellImpl implements MemoryCell {
    private final Supplier<Character> reader;
    private final Consumer<Character> writer;

    @Override
    public char read() {
        return reader.get();
    }

    @Override
    public void write(char value) {
        writer.accept(value);
    }
}
