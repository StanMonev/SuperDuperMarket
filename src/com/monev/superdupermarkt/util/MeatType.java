package com.monev.superdupermarkt.util;

public enum MeatType {
    POULTRY(2, 10),
    BEEF(5, 10),
    PORK(5, 10),
    VENISON(5, 10),
    VEAL(5, 10),
    LAMB(5, 10);

    private final int freshDays;
    private final int vacuumedDays;

    MeatType(int freshDays, int vacuumedDays) {
        this.freshDays = freshDays;
        this.vacuumedDays = vacuumedDays;
    }

    public int getFreshDays() {
        return freshDays;
    }

    public int getVacuumedDays() {
        return vacuumedDays;
    }
}
