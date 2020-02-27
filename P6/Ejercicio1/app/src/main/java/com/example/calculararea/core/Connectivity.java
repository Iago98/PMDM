package com.example.calculararea.core;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity extends Activity {

    Context mContext;
    public Connectivity(Context mContext) {
        this.mContext = mContext;
    }
    public boolean isConnected() {


    ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    boolean isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting();
    return isConnected;
    }


    public boolean isWiFi(){
        boolean isWiFi=false;
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
try{
         isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    return  isWiFi;
    }catch (Exception e){

}

        return  isWiFi;
    }


}
