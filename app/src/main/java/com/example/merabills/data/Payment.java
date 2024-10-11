package com.example.merabills.data;

import androidx.annotation.NonNull;
import java.util.Objects;

public class Payment {

    private final String paymentMode;
    private final double amount;
    private final String provider;
    private final String reference;

    public Payment(String paymentMode, double amount, String provider, String reference) {
        this.paymentMode = paymentMode;
        this.amount = amount;
        this.provider = provider;
        this.reference = reference;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return Double.compare(getAmount(), payment.getAmount()) == 0 && Objects.equals(getPaymentMode(), payment.getPaymentMode()) && Objects.equals(getProvider(), payment.getProvider()) && Objects.equals(getReference(), payment.getReference());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPaymentMode(), getAmount(), getProvider(), getReference());
    }

    public String getProvider() {
        return provider;
    }

    public String getReference() {
        return reference;
    }

    @NonNull
    @Override
    public String toString() {
        return paymentMode + ": Rs." + amount;
    }
}

