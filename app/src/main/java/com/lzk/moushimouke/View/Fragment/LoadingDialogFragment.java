package com.lzk.moushimouke.View.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lzk.moushimouke.R;

/**
 * Created by huqun on 2018/5/2.
 */

public class LoadingDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        AlertDialog dialog=builder.create();
        /*dialog.setCancelable(false);此处设置无效，需在使用dialog fragment的地方使用。*/
        dialog.getWindow().setDimAmount(0f);
        dialog.show();
        /*setContentView会覆盖全部，setView()只会覆盖AlertDialog的Title与Button之间的那部分
        * setContentView在show()之后调用*/
        dialog.getWindow().setContentView(R.layout.fragment_loading_dialog);
        dialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
        params.width=200;
        params.height=200;
        dialog.getWindow().setAttributes(params);
        return dialog;
    }
}
