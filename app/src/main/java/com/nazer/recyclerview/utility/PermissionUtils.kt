package com.racestake.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import java.util.*

class PermissionUtils(context: Context, callback: PermissionResultCallback)
{

    internal var context:Context=context
    internal var current_activity: Activity=context as Activity

    var permissionResultCallback: PermissionResultCallback=callback

    internal var permission_list = ArrayList<String>()
    internal var listPermissionsNeeded = ArrayList<String>()

    internal var dialog_content = ""
//    var REQUEST_PERMISSION_CAMERA_AND_GALLERY: Int = 113
    var REQUEST_PERMISSION_CODE: Int = 0
//
//    fun PermissionUtils(context: Context, callback: PermissionResultCallback){
//        this.context = context
//        this.current_activity = context as Activity
//        permissionResultCallback = callback
//    }

//    companion object {
//        fun REQUEST_PERMISSION_CAMERA_AND_GALLERY() : Int = 113
//    }
//

    /**
     * Check the API Level & Permission
     *
     * @param permissions
     * @param dialog_content
     * @param request_code
     */
    fun check_permission(permissions: ArrayList<String>, dialog_content: String, request_code: Int) {
        this.permission_list = permissions
        this.dialog_content = dialog_content
        this.REQUEST_PERMISSION_CODE = request_code
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions(permissions, request_code)) {
                permissionResultCallback.PermissionGranted(request_code)
                Log.e("all permissions", "granted")
                Log.e("proceed", "to callback")
            }
        } else {
            permissionResultCallback.PermissionGranted(request_code)
            Log.e("all permissions", "granted")
            Log.e("proceed", "to callback")
        }
    }

    /**
     * Check and request the Permissions
     *
     * @param permissions
     * @param request_code
     * @return
     */

    private fun checkAndRequestPermissions(permissions: ArrayList<String>, request_code: Int): Boolean {

        if (permissions.size > 0) {
            listPermissionsNeeded = ArrayList()

            for (i in permissions.indices) {
                val hasPermission = ContextCompat.checkSelfPermission(current_activity!!, permissions[i])

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions[i])
                }

            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(current_activity!!, listPermissionsNeeded.toTypedArray(), request_code)
                return false
            }
        }

        return true
    }

    /**
     *
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.e("onRequestPermission"," "+grantResults.size)
//        when (requestCode) {
        if (requestCode==REQUEST_PERMISSION_CODE) {
            if (grantResults.size > 0) {
                val perms = HashMap<String, Int>()

                for (i in permissions.indices) {
                    perms[permissions[i]] = grantResults[i]
                }

                val pending_permissions = ArrayList<String>()

                for (i in listPermissionsNeeded.indices) {
                    if (perms[listPermissionsNeeded[i]] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(current_activity!!, listPermissionsNeeded[i]))
                            pending_permissions.add(listPermissionsNeeded[i])
                        else {
                            Log.e("Go to settings", "and enable permissions")
                            permissionResultCallback.NeverAskAgain(REQUEST_PERMISSION_CODE)
                            Toast.makeText(current_activity, "Go to settings and enable permissions", Toast.LENGTH_LONG).show()
                            return
                        }
                    }

                }

                if (pending_permissions.size > 0) {
                    showMessageOKCancel(dialog_content,
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> check_permission(permission_list, dialog_content, REQUEST_PERMISSION_CODE)
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                        Log.e("permisson", "not fully given")
                                        if (permission_list.size == pending_permissions.size)
                                            permissionResultCallback.PermissionDenied(REQUEST_PERMISSION_CODE)
                                        else
                                            permissionResultCallback.PartialPermissionGranted(REQUEST_PERMISSION_CODE, pending_permissions)
                                    }
                                }
                            })

                } else {
                    Log.e("all", "permissions granted")
                    Log.e("proceed", "to next step")
                    permissionResultCallback.PermissionGranted(REQUEST_PERMISSION_CODE)

                }

            }

        }
    }

    /**
     * Explain why the app needs permissions
     *
     * @param message
     * @param okListener
     */
    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(current_activity!!)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
    }

    interface PermissionResultCallback {
        fun PermissionGranted(request_code: Int)
        fun PartialPermissionGranted(request_code: Int, granted_permissions: ArrayList<String>)
        fun PermissionDenied(request_code: Int)
        fun NeverAskAgain(request_code: Int)
    }
    interface CommonPermissionResultCallback {
        fun PermissionGranted(request_code: Int)
    }
}