package com.yk.memo.ui.main.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class ConfirmDialogFragment extends DialogFragment {
    private static final String TAG = "ConfirmDialogFragment";

    public void show(FragmentManager fm) {
        show(fm, TAG);
    }

    public static ConfirmDialogFragment newInstance() {
        return new ConfirmDialogFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onConfirmListener = (OnConfirmListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否一键打包分享")
                .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onConfirmListener != null) {
                            onConfirmListener.onPositiveClick();
                        }
                    }
                })
                .setNegativeButton("不分享", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onConfirmListener != null) {
                            onConfirmListener.onNegativeClick();
                        }
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onConfirmListener != null) {
                            onConfirmListener.onNeutralClick();
                        }
                    }
                });
        return builder.create();
    }

    private OnConfirmListener onConfirmListener;

    public interface OnConfirmListener {
        void onPositiveClick();

        void onNegativeClick();

        void onNeutralClick();
    }
}
