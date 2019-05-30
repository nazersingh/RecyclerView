package com.nazer.recyclerview.utility

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import com.nazer.recyclerview.R
import java.text.SimpleDateFormat
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.nazer.recyclerview.manager.callbacks.onTextChangeCallback
import java.util.*

object CommonUtils {

    fun isInternetWorking(context: Context):Boolean
    {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getTimestamp(): String {
        return SimpleDateFormat(Constants.TIMESTAMP_FORMAT, Locale.US).format(Date())
    }



//    fun openPlayStoreForApp(context: Context) {
//        val appPackageName = context.packageName
//        try {
//            context.startActivity(Intent(Intent.ACTION_VIEW,
//                Uri.parse(context
//                    .resources
//                    .getString(R.string.app_market_link) + appPackageName)))
//        } catch (e: android.content.ActivityNotFoundException) {
//            context.startActivity(Intent(Intent.ACTION_VIEW,
//                Uri.parse(context
//                    .resources
//                    .getString(R.string.app_google_play_store_link) + appPackageName)))
//        }
//
//    }
    fun onEditTextType(onTextChangeCallback: onTextChangeCallback): TextWatcher
    {
        return  object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onTextChangeCallback.afterTextChange(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        }
    }
    /**
     * Load Image from Url
     */
    fun loadPic(context: Context, file: String, abc: ImageView, progressBar: ProgressBar, id: Int) {
        try {
//            PrintLog.e("LoadPic ", "" + file);
            progressBar.visibility = View.VISIBLE
            Glide.with(context).asBitmap().load(file).apply(RequestOptions.centerCropTransform())
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: com.bumptech.glide.request.target.Target<Bitmap>, isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        abc.setImageResource(id)
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap, model: Any, target: com.bumptech.glide.request.target.Target<Bitmap>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(abc)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

