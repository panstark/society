package com.pan.society.Common;

@FunctionalInterface
public interface Getter<TObject, TValue> {
    TValue get(TObject object);
}
