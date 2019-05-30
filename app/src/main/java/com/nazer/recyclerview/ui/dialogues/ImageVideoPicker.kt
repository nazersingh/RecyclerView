package com.nazer.recyclerview.ui.dialogues

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object ImageVideoPicker {
    var TAG="ImageVideoPicker";
    var mPlayer: MediaPlayer? = null

    val IMAGE_GALLERY = 1
    val IMAGE_CAMERA = 2

    val VIDEO_GALLERY = 3
    val VIDEO_CAMERA = 4

    val AUDIO_GALLERY = 5
    val AUDIO_RECORD = 6
    val APP_DIRECTORY = "/MyProject"
    var recorderCallBack: AudioRecorderCallBack? = null

    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: String? = null

    /**
     * Open Video and Image
     */
    fun OpenDiaLogueImageVideo(activity: Activity) {
        Dialogues.setPositiveButtonText("Image")
            .setNegativeButtonText("Video")
            .setMessage("Select Option")
            .showGalleryPickerDialog(activity)
    }


    /**
     * Image Picker
     */
    fun startActivityForImagePicker(activity: Activity) {
        Dialogues.setPositiveButtonText("Gallery")
            .setNegativeButtonText("Camera")
            .showImagePickerDialog(activity)
    }

    /**
     * Video Picker
     */
    fun startActivityForVideoPicker(activity: Activity) {
        Dialogues.setPositiveButtonText("Gallery")
            .setNegativeButtonText("Camera")
            .showVideoPickerDialog(activity)
    }

    /**
     * Audio Picker dialogue
     */
    fun startActivityForAudioPicker(activity: Activity, recorderCallBack: AudioRecorderCallBack) {
        this.recorderCallBack = recorderCallBack
        Dialogues.setPositiveButtonText("Gallery")
            .setNegativeButtonText("Record")
            .showAudioPickerDialog(activity)
    }

    /**
     * Audio from gallery
     */
    fun startActivityAudioFromGallary(activity: Activity) {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, AUDIO_GALLERY)
    }

    /**
     * Audio From Recorded
     */
    fun startActivityAudioFromRecorded(activity: Activity) {
//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp"

        outputFile = getProjectDirectory().toString()+"/"+Calendar.getInstance().getTimeInMillis().toString()+".3gp"
        mediaRecorder=getMediaRecorderAndStart()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
        mediaRecorder?.setOutputFile(outputFile)
        mediaRecorder?.prepare()
        mediaRecorder?.start()
        recorderCallBack?.onRecordingSatrt()
        /**
         * stop audio recorder on 10 seconds
         */
        Handler().postDelayed(Runnable {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            recorderCallBack?.onRecordingStop(Uri.fromFile(File(outputFile)))
        }, 10000)
    }

    /**
     * Gallery Image
     */
    fun startActivityPhotoFromGallary(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, IMAGE_GALLERY)
    }



    /***
     * Camera Image capture
     */
    fun startActivityPhotoFromCamera(activity: Activity) {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        activity.startActivityForResult(intent, IMAGE_CAMERA)
    }

    /**
     * Gallery Video
     */
    fun startActivityVideoFromGallary(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, VIDEO_GALLERY)
    }

    /**
     * Camera Video capture
     */
    fun startActivityVideoFromCamera(activity: Activity) {
//        val videoUri =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordingVideo.mp4"
        val videoUri =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordingVideo.mp4"
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
//        val videoUri = Uri.fromFile(mediaFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        activity.startActivityForResult(intent, VIDEO_CAMERA)
    }


    /**
     * Media recorder used for audio recording and video recording
     */
    fun getMediaRecorderAndStart(): MediaRecorder {
        mediaRecorder = MediaRecorder()
        return mediaRecorder as MediaRecorder
    }


    /**
     * get Bitmap from Activity Result
     */
    fun getBitmapFromActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?): Bitmap? {
        if (requestCode == IMAGE_GALLERY) {
            if (data != null) {
                val contentURI = data.data
                return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), contentURI)
            }
        } else if (requestCode == IMAGE_CAMERA) {
            return data!!.extras!!.get("data") as Bitmap
        }
        return null
    }

    /**
     * Directory to save app data
     */
    fun getProjectDirectory(): File
    {
        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + APP_DIRECTORY)
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        return wallpaperDirectory
    }


    /**
     * Save image
     */
    fun saveImageAndGetPath(context: Context, myBitmap: Bitmap?): String {
        val bytes = ByteArrayOutputStream()
        myBitmap?.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val ProjectDirectory = getProjectDirectory()
        // have the object build the directory structure, if needed.
//        if (!wallpaperDirectory.exists()) {
//            wallpaperDirectory.mkdirs()
//        }

        try {
            val f = File(ProjectDirectory, Calendar.getInstance().getTimeInMillis().toString() + ".jpg")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(f.getPath()), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())
            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }


    /**
     * get Audio from Activity Result
     */
    fun getAudioFromActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?): Uri? {

        if (requestCode == AUDIO_GALLERY) {
            if (data != null) {
                return data.data
            }
        }
        return null
    }

    /**
     * get Video from Activity Result
     */
    fun getVideoFromActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?): Uri? {

        if (requestCode == VIDEO_GALLERY) {
            if (data != null) {
                val selectedImageUri = data.data
                return selectedImageUri
            }

        } else if (requestCode == VIDEO_CAMERA) {
            return null
        }
        return null
    }


    /**
     * get RealPathFromUri
     */
    fun getRealPathFromUri(context: Context, contentUri: Uri?): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            return cursor!!.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
    }


    interface AudioRecorderCallBack {
        fun onRecordingSatrt()
        fun onRecordingStop(uri: Uri)
    }


    /**
     * Play Audio Player
     */

    fun playAudio(audioPath: Uri, context: Context) {
        // Even you can refer resource in res/raw directory
        //Uri myUri = Uri.parse("android.resource://com.prgguru.example/" + R.raw.hosannatamil);
        Log.e(TAG, " audio " + audioPath)
        stopAudio()
        if (mPlayer == null)
            mPlayer = MediaPlayer()
        mPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mPlayer!!.setDataSource(context, audioPath)
        } catch (e: IllegalArgumentException) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        } catch (e: SecurityException) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        } catch (e: IllegalStateException) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            mPlayer!!.prepare()
        } catch (e: IllegalStateException) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show()
        }

        mPlayer!!.start()
    }

    fun stopAudio() {
        if (mPlayer != null && mPlayer!!.isPlaying()) {
            mPlayer!!.stop()
            mPlayer!!.release()
            mPlayer=null
        }
    }

    fun pauseAudio()
    {
        if (mPlayer != null && mPlayer!!.isPlaying()) {
            mPlayer!!.pause()
        }
    }

}