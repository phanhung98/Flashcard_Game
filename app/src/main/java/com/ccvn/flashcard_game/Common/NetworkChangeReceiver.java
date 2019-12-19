package com.ccvn.flashcard_game.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

@SuppressWarnings("ALL")
public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String NETWORK_CHANGE_ACTION = "com.ccvn.flashcard_game.Common.NetworkChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(isOnline(context)){
            sendInternalBroadcast(context, "Internet Connected");
        }else {
            sendInternalBroadcast(context, "Internet Not Connected");
        }
    }
    /**
     * This method is responsible to send status by internal broadcast
     *
     * @param context
     * @param status
     * */
    private void sendInternalBroadcast(Context context, String status) {

        try {
            Intent intent = new Intent();
            intent.putExtra("status", status);
            intent.setAction(NETWORK_CHANGE_ACTION);
            context.sendBroadcast(intent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    /**
     * Check if network available or not
     *
     * @param context
     * */
    public static boolean isOnline(Context context) {

        boolean isOnline = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isOnline = (networkInfo != null && networkInfo.isConnected());
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return isOnline;
    }
}
