package com.example.merabills.data;

@FunctionalInterface
public interface PaymentCallback {
    void onPaymentSaved(Payment payment);
}
