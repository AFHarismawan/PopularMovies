package com.harismawan.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.harismawan.popularmovies.config.Constants;

public class Utils {

    public static APIHelper getAPIHelper() {
        return RetrofitClient.getClient(Constants.BASE_URL).create(APIHelper.class);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
