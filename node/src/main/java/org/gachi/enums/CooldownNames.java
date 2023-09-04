package org.gachi.enums;

public enum CooldownNames {
    DRINK_CUM_COOLDOWN(15),
    STEAL_SLAVE__COOLDOWN(120),
    GET_SLAVE_COOLDOWN(360);

    private final int value;

    CooldownNames(int value) {
        this.value = value;
    }

    public int getCooldownInMinutes(){
        return value;
    }
}
