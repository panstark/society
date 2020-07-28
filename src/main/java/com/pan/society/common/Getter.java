package com.pan.society.common;

@FunctionalInterface
public interface Getter<TObject, TValue> {
    TValue get(TObject object);
}
