package com.parkit.parkingsystem.util;

public class RoundUtil {
    public static double roundAt2Decimals(double value) {
        return Math.round(value * 100) / 100.0;
    }
}
