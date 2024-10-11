package com.example.merabills.data;

import androidx.annotation.NonNull;

public enum PaymentType {
    CASH("Cash"),
    BANK_TRANSFER("Bank Transfer"),
    CREDIT_CARD("Credit Card");

    private final String displayName;

    PaymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @NonNull
    @Override
    public String toString() {
        return displayName;
    }

    public static PaymentType fromDisplayName(String displayName) {
        for (PaymentType type : PaymentType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with display name " + displayName);
    }
}
