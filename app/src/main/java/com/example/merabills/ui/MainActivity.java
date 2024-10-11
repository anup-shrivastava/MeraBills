package com.example.merabills.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.merabills.data.Payment;
import com.example.merabills.R;
import com.example.merabills.databinding.ActivityMainBinding;
import com.google.android.material.chip.Chip;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PaymentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        initializer();
        observeViewModel();
    }


    private void initializer() {
        viewModel.loadPayments(this);
        binding.btnAdd.setOnClickListener(view -> showAddPaymentBottomSheet());
        binding.btnSave.setOnClickListener(view -> viewModel.savePayments(this));
    }

    private void observeViewModel() {
        viewModel.paymentsLiveData.observe(this, this::updatePaymentChips);
        viewModel.message.observe(this, msg -> Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show());
    }

    private void showAddPaymentBottomSheet() {
        viewModel.getAvailablePaymentTypes().observe(this, availablePaymentTypes -> {
            if (availablePaymentTypes == null || availablePaymentTypes.isEmpty()) {
                Toast.makeText(this, this.getString(R.string.no_payment_modes_available_please_delete_payment_to_add_new), Toast.LENGTH_SHORT).show();
            } else {
                PaymentDialog paymentDialog = new PaymentDialog(availablePaymentTypes, payment -> viewModel.addPayment(payment));
                paymentDialog.show(getSupportFragmentManager(), paymentDialog.getTag());
            }
        });
    }

    private void updatePaymentChips(List<Payment> paymentList) {
        double totalAmt = 0.0;
        binding.chipContainer.removeAllViews();
        for (Payment payment : paymentList) {
            totalAmt +=payment.getAmount();
            Chip chip = getChip(payment);
            binding.chipContainer.addView(chip);
        }
        binding.tvTotalAmount.setText(getString(R.string.total_amount) + String.format("%.2f", totalAmt));
        if (paymentList.isEmpty()) {
            binding.tvTotalAmount.setText(getString(R.string.total_amount) + "0.00");
        }
    }

    @NonNull
    private Chip getChip(Payment payment) {
        Chip chip = new Chip(this);
        String chipText = payment.getPaymentMode() + " - ₹ " + payment.getAmount();
        chip.setText(chipText);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(view -> viewModel.deletePayment(payment));
        return chip;
    }
}