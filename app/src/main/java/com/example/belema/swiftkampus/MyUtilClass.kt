package com.example.belema.swiftkampus

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AlertDialog
import org.json.JSONObject
import retrofit2.Response

/**
 * Created by belema on 9/20/17.
 */

object MyUtilClass {

    fun checkNetworkConnection(context:Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        var activeNetwork: NetworkInfo? = null
        try {
            activeNetwork = cm.activeNetworkInfo
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting

    }

    fun showNoInternetMessage(context: Context?) {
        if (context != null) {
            AlertDialog.Builder(context)
                    .setTitle("Network Connection")
                    .setMessage("Sorry, you are not connected to the internet")
                    .setNegativeButton("Close", null)
                    .show()
        }
    }

    fun <T> showErrorMessage(context: Context?, response: Response<T>){
        if (context != null && response.errorBody() != null){
            val errorMessage = JSONObject(response.errorBody()?.string())
            AlertDialog.Builder(context)
                    .setMessage(errorMessage.getString("message"))
                    .setNegativeButton("Close", null)
                    .show()
        }
    }

}
