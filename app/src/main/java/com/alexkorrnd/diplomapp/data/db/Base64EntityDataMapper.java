package com.alexkorrnd.diplomapp.data.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.alexkorrnd.diplomapp.data.BaseEntityMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class Base64EntityDataMapper<T> extends BaseEntityMapper<String, List<T>> {
    private final String TAG = getClass().getSimpleName();

    @Nullable
    protected abstract T read(@NonNull DataInputStream inputStream) throws IOException;

    @Nullable
    @Override
    public List<T> from(String entity) {
        final byte[] bytes = Base64.decode(entity.getBytes(), Base64.NO_WRAP);
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(new ByteArrayInputStream(bytes));
            final int size = inputStream.readInt();
            final List<T> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                list.add(read(inputStream));
            }
            return list;
        } catch (IOException e) {
            Log.e(TAG, "Failed to convert entity");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    protected abstract void write(@NonNull DataOutputStream outputStream, @NonNull T item) throws IOException;

    @Nullable
    @Override
    public String to(List<T> model) {
        DataOutputStream outputStream = null;
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            outputStream = new DataOutputStream(byteArrayOutputStream);
            final int size = model.size();
            outputStream.writeInt(size);
            for (int i = 0; i < size; i++) {
                final T item = model.get(i);
                write(outputStream, item);
            }
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
        } catch (IOException e) {
            Log.e(TAG, "Failed to convert data");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }
}
