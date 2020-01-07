package com.ccvn.flashcard_game.Common;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public final class DataBinder {

    @BindingAdapter("upload_path")
    public static void setUploadPath(ImageView imageView, String url){
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }

}
