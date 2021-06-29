package com.decompiler.util.output;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.decompiler.entities.Method;

public interface BytecodeDumpConsumer {
    interface Item {
        Method getMethod();
        /** return a map of BYTECODE LOCATION IN METHOD to LINE NUMBER.
         *  Note that this is ordered by BYTECODE LOCATION.
         **/
        NavigableMap<Integer, Integer> getBytecodeLocs();
    }

    void accept(Collection<Item> items);
}
