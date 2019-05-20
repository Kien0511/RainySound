package com.example.rainysound;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class MyUtil {
    public static void loadImageFromUrl(Context context, ImageView imageView, String url) {

        Glide.with(context).load(url).apply(new RequestOptions().placeholder(R.color.colorAccent)
                .error(R.color.colorAccent)).into(imageView);
    }

    public static void loadImageFromUrl(Context context, ImageView imageView, int url) {

        Glide.with(context).load(url).apply(new RequestOptions().placeholder(R.color.colorAccent)
                .error(R.color.colorAccent)).into(imageView);
    }

    public static long parseTime(int hour, int minute)
    {
        return (hour*60*60 + minute*60)*1000;
    }

    public static String formatTime(long milis) {
        int hours = (int) ((milis / (1000 * 60 * 60)) % 24);
        int minutes = (int) ((milis / (1000 * 60)) % 60);
        int seconds = (int) (milis / 1000) % 60;

        String h;
        String m;
        String s;
        if(seconds < 10)
        {
            s = "0"+seconds;
        }
        else
        {
            s = seconds+"";
        }

        if(minutes < 10)
        {
            m = "0" + minutes;
        }
        else
        {
            m = minutes+"";
        }
        if(hours < 10)
        {
            h = "0"+hours;
        }
        else
        {
            h = hours + "";
        }
        return String.valueOf(h + ":" + m + ":" + s);
    }
}
