package com.example.merabills.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.merabills.data.PaymentType;
import com.example.merabills.R;
import com.example.merabills.data.Payment;
import com.example.merabills.data.PaymentCallback;
import com.example.merabills.databinding.PaymentDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.List;
import java.util.Objects;

public class PaymentDialog extends BottomSheetDialogFragment {

    private PaymentDialogBinding binding;
    private Context mContext;
    private final PaymentCallback resultCallback;
    List<PaymentType> availablePaymentTypes;

    public PaymentDialog(List<PaymentType> availablePaymentTypes,PaymentCallback resultCallback) {
        this.availablePaymentTypes = availablePaymentTypes;
        this.resultCallback = resultCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PaymentDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = requireContext();
        initializer();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            View bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });

        return dialog;
    }

    private void initializer() {
        ArrayAdapter<PaymentType> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, availablePaymentTypes);
        AutoCompleteTextView paymentModeView = (AutoCompleteTextView) binding.etPaymentMode.getEditText();
        Objects.requireNonNull(paymentModeView).setAdapter(adapter);
        paymentModeView.setOnItemClickListener((parent, view, position, id) -> {
            PaymentType selectedPaymentType = (PaymentType) parent.getItemAtPosition(position);
            handlePaymentTypeSelection(selectedPaymentType);
        });

        binding.btnSave.setOnClickListener(view -> {
            String amountText = Objects.requireNonNull(binding.etAmount.getEditText()).getText().toString().trim();
            if (validate(amountText)) {
                Payment payment = new Payment(
                        binding.etPaymentMode.getEditText().getText().toString(),
                        Double.parseDouble(amountText),
                        Objects.requireNonNull(binding.etProvider.getEditText()).getText().toString(),
                        Objects.requireNonNull(binding.etReference.getEditText()).getText().toString()
                );
                resultCallback.onPaymentSaved(payment);
                dismiss();
            }
        });
        binding.btnClose.setOnClickListener(view -> dismiss());
    }

    private boolean validate(String amountText) {
        if (amountText.isEmpty()) {
            Toast.makeText(mContext, mContext.getString(R.string.please_enter_amount), Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            Toast.makeText(mContext, mContext.getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Objects.requireNonNull(binding.etPaymentMode.getEditText()).getText() == null || binding.etPaymentMode.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(mContext, mContext.getString(R.string.please_select_payment_mode), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void handlePaymentTypeSelection(PaymentType selectedPaymentType) {
        binding.linearLay.setVisibility(selectedPaymentType == PaymentType.CASH ? View.GONE : View.VISIBLE);
    }
}