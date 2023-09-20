package com.aait.getak.utils;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.View;

import com.aait.getak.R;
import com.wang.avi.AVLoadingIndicatorView;


public final class DialogUtil {

    private DialogUtil() {
    }

    public static AVLoadingIndicatorView showProgressDialog(Context context, String message, boolean cancelable) {
      //  ProgressDialog dialog = new ProgressDialog(context);
        AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);

        avLoadingIndicatorView.setIndicator("BallSpinFadeLoaderIndicator");
        //dialog.setMessage(message);
        //dialog.setCancelable(cancelable);
        //dialog.show();
        avLoadingIndicatorView.show();
        return avLoadingIndicatorView;
    }

    public static AlertDialog showAlertDialog(Context context, String message, View view,
                                              DialogInterface.OnClickListener negativeClickListener) {
        // create the dialog builder & set message
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(message);

        dialogBuilder.setView(view);

        // check negative click listener
        if (negativeClickListener != null) {
            // not null
            // add negative click listener
            dialogBuilder.setNegativeButton(context.getString(R.string.yes), negativeClickListener);
        } else {
            // null
            // add new click listener to dismiss the dialog
            dialogBuilder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
        }
        // create and show the dialog

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        return dialog;
    }
    public static AlertDialog showProgressDialog(Context context) {
        // create the dialog builder & set message
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.loading_progress_layout);
        //dialogBuilder.setMessage(message);

        // create and show the dialog
        dialogBuilder.setCancelable(false);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        return dialog;
    }

    public static AlertDialog showconfirm(Context context, int tittle, String message, boolean cancble,
                                          DialogInterface.OnClickListener negativeClickListener, DialogInterface.OnClickListener positiveClickListener) {
        // create the dialog builder & set message
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(tittle);

        dialogBuilder.setMessage(message);

        // check negative click listener
        if (negativeClickListener != null) {
            // not null
            // add negative click listener
            dialogBuilder.setNegativeButton(context.getString(R.string.cancel), negativeClickListener);
        }
        if (positiveClickListener != null) {
            // not null
            // add negative click listener
            dialogBuilder.setPositiveButton(context.getString(R.string.continues), positiveClickListener);
        }
        // create and show the dialog
        dialogBuilder.setCancelable(cancble);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        return dialog;
    }

}
