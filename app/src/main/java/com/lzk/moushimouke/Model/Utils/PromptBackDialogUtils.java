package com.lzk.moushimouke.Model.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.lzk.moushimouke.R;

/**
 * Created by huqun on 2018/5/4.
 */

public class PromptBackDialogUtils {
    public void showUnEditDialog(final AppCompatActivity activity){
        final AlertDialog.Builder dialog=new AlertDialog.Builder(activity);
        dialog.setTitle(activity.getResources().getString(R.string.prompt));
        dialog.setMessage(activity.getResources().getString(R.string.are_you_sure_to_cancel_the_editor));
        dialog.setCancelable(true);
        dialog.setPositiveButton(activity.getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            activity.finish();
            }
        });
        dialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }
}
