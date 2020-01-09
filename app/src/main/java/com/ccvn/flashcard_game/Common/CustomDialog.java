package com.ccvn.flashcard_game.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.views.GameActivity;
import com.ccvn.flashcard_game.views.HighScoreFragment;

import java.text.DecimalFormat;

public class CustomDialog {
    private Dialog dialog;

    public Dialog showProgressBarDialog(Context context) {
        return showProgressBarDialog(context, null);
    }

    public Dialog showProgressBarDialog(Context context, CharSequence title) {
        return showProgressBarDialog(context, title, false);
    }

    public Dialog showProgressBarDialog(Context context, CharSequence title, boolean cancelable) {
        return showProgressBarDialog(context, title, cancelable, null);
    }

    public Dialog showProgressBarDialog(Context context, CharSequence title, boolean cancelable,
                       DialogInterface.OnCancelListener cancelListener) {
        LayoutInflater inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflator.inflate(R.layout.progress_bar, null);
        if(title != null) {
            final TextView tv = (TextView) view.findViewById(R.id.title);
            tv.setText(title);
        }

        dialog = new Dialog(context, R.style.Widget_Flashcard_ProgressbarDialog);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();

        return dialog;
    }

    public Dialog getProgressBarDialog() {
        return dialog;
    }

    public Dialog showScoreDialog(final Context context,double score){

        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflator.inflate(R.layout.score_dialog, null);
        final TextView mScore = view.findViewById(R.id.dialog_score);
        TextView mConfirm = view.findViewById(R.id.tv_comfirm);

        DecimalFormat f =  new DecimalFormat("##.##");
        mScore.setText("Your score: " + f.format(score));
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FragmentManager fragmentManager =((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new HighScoreFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }

}
