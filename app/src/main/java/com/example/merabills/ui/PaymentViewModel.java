package com.example.merabills.ui;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.merabills.data.Payment;
import com.example.merabills.data.PaymentRepository;
import com.example.merabills.data.PaymentType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PaymentViewModel extends ViewModel {
    PaymentRepository repository = PaymentRepository.getInstance();
    public final MutableLiveData<List<Payment>> paymentsLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> message = new MutableLiveData<>();

    private void runInBackground(Runnable task) {
        new Thread(task).start();
    }


    public LiveData<List<PaymentType>> getAvailablePaymentTypes() {
        List<PaymentType> allPaymentTypes = new ArrayList<>(Arrays.asList(PaymentType.values()));
        List<Payment> currentPayments = paymentsLiveData.getValue();
        if (currentPayments != null) {
            for (Payment payment : currentPayments) {
                PaymentType selectedType = PaymentType.fromDisplayName(payment.getPaymentMode());
                allPaymentTypes.remove(selectedType);
            }
        }
        return new MutableLiveData<>(allPaymentTypes);
    }

    public void loadPayments(Context context) {
        try {
            runInBackground(() -> {
                List<Payment> payments = repository.readPayments(context);
                paymentsLiveData.postValue(payments);
            });
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void savePayments(Context context) {
        try {
            List<Payment> payments = paymentsLiveData.getValue();
            if (payments != null) {
                runInBackground(() -> {
                    if (repository.savePayments(payments, context)) {
                        message.postValue("Saved Successfully");
                    } else {
                        message.postValue("Failed to save");
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addPayment(Payment payment) {
        try {
            ArrayList<Payment> currentPayments = new ArrayList<>(Objects.requireNonNull(paymentsLiveData.getValue()));
            currentPayments.add(payment);
            runInBackground(() -> paymentsLiveData.postValue(currentPayments));
        } catch (Exception ex) {
            message.postValue("Something went wrong");
            ex.printStackTrace();
        }
    }

    public void deletePayment(Payment payment) {
        try {
            ArrayList<Payment> currentPayments = new ArrayList<>(Objects.requireNonNull(paymentsLiveData.getValue()));
            if (currentPayments.remove(payment)) {
                runInBackground(() -> {
                    paymentsLiveData.postValue(new ArrayList<>(currentPayments));
                    message.postValue("Payment Deleted");
                });
            } else {
                message.postValue("Payment Not Found");
            }
        } catch (Exception ex) {
            message.postValue("Payment Not Found");
            ex.printStackTrace();
        }
    }
}
