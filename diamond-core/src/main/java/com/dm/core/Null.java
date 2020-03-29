package com.dm.core;

/**
 * @author zy
 */
public class Null {

    private static Null instance = new Null();

    /**
     * Get null.
     *
     * @return the null
     */
    public static Null get() {
        return instance;
    }

    private Null() {
    }

    @Override
    public String toString() {
        return "NULL";
    }

}
