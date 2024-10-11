package com.example.merabills.data;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentRepository {
    private static PaymentRepository instance;
    private static final String FILE_NAME = "MeraBills.txt";
    private final Gson gson = new Gson();
    public static final String TAG = "MeraBills_Tag";

    public static synchronized PaymentRepository getInstance() {
        if (instance == null) {
            instance = new PaymentRepository();
        }
        return instance;
    }


    public List<Payment> readPayments(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            Payment[] payments = gson.fromJson(reader, Payment[].class);
            Log.d(TAG,"All Payments: "+gson.toJson(payments));
            return Arrays.asList(payments);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean savePayments(List<Payment> payments,Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);

        try (FileWriter writer = new FileWriter(file)) {
            String json = gson.toJson(payments);
            Log.d(TAG,"Saved Payment: "+json);
            writer.write(json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
