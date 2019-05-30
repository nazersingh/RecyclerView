package com.nazer.recyclerview.ui.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.nazer.recyclerview.R
import com.nazer.recyclerview.manager.callbacks.GenericCallBack
import com.nazer.recyclerview.ui.activity.demo_list.MainActivity
import com.nazer.recyclerview.ui.dialogues.Dialogues
import com.nazer.recyclerview.utility.CommonUtils
import com.nazer.recyclerview.utility.PrintLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : AppCompatActivity() {

    private val ON_SUCCESS = 200
    private val ON_ERROR = 400
    private val ON_RESEND_VERIFICATION = 500
    private val ON_SESSION_EXPIRED = 1000

    private val TIME_INTERVAL: Long = 2000 // # milliseconds, desired time passed between two back presses.
    private var mBackPressed: Long = 0
    private var genricCallback: GenericCallBack<*>? = null
    private var mCompositeDisposable = CompositeDisposable()
    abstract fun setUpBinding()
    abstract fun setUpObservers()

    fun setTitle(title: String) = supportActionBar?.setTitle(title)


    fun setSubtitle(subtitle: String) = supportActionBar?.setSubtitle(subtitle)


    fun hideToolbar() = supportActionBar?.hide()

    fun showToolbar() = supportActionBar?.show()


    fun onAddFragment(container: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(container, fragment, fragment.javaClass.simpleName.toString())
        fragmentTransaction.addToBackStack(fragment.javaClass.simpleName.toString())
        fragmentTransaction.commitAllowingStateLoss()
        updateToollbarTitle(fragment)
    }


    fun onReplaceFragment(container: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(container, fragment, fragment.getTag())
        fragmentTransaction.addToBackStack(fragment.getTag())
        fragmentTransaction.commitAllowingStateLoss()
    }


    fun setCurrentFragmentCallBack(genricCallback: GenericCallBack<Any>) {
        this.genricCallback = genricCallback
    }

    fun popFragment() = supportFragmentManager.popBackStack()

    fun clearStack() = supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    override fun onBackPressed() {


        if (supportFragmentManager.backStackEntryCount > 1) {
            //            popFragment();
            super.onBackPressed()
            val ffragment = supportFragmentManager.findFragmentById(R.id.container)
//            ffragment.userVisibleHint = true
            updateToollbarTitle(ffragment!!)
        } else {

            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                mCompositeDisposable.clear()
                finish()
            } else {
                mBackPressed = System.currentTimeMillis()
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * ==================== is Internet connected
     */
    fun isInternetWorking(): Boolean = CommonUtils.isInternetWorking(this)

    fun showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    fun updateToollbarTitle(fragment: Fragment) {
//        when(fragment.tag)
//        {
//            fragment in LoginFragment->{}
//        }
        PrintLog.e("update Toolbar ", "" + fragment.tag)
    }

    fun showApiCallLoader() = Dialogues.showLoadingDialog(this)

    fun addViewDisposal(disposable: Disposable) = mCompositeDisposable.add(disposable)

    fun sessionExpired(message: String) = Dialogues
        .setMessage(message)
        .isCancelAble(false)
        .setPositiveButtonText(Dialogues.POSITIVE_BUTTON_OK)
        .showDialogueTextWithOneButton(this, object : Dialogues.DialgogueInterfaceOneButton<String> {
            override fun onOkClick(t: String) {
                logoutUserToLogin()
            }
        })
    fun logoutUser() = also {
        Dialogues.setMessage(resources.getString(R.string.logout_message))
            .isCancelAble(false)
            .setPositiveButtonText(Dialogues.POSITIVE_BUTTON_OK)
            .setNegativeButtonText(Dialogues.NEGATIVE_BUTTON_CANCEL)
            .showDialogueTextWithButton(this, object : Dialogues.DialgogueInterfaceTwoButton<String> {
                override fun onOkClick(t: String) = logoutUserToLogin()
                override fun onCancelClick(t: String) {}
            })
    }

    private fun logoutUserToLogin() {
//        getfirebaseAuthInstance().signOut()
        if(this is MainActivity)
        {
            clearStack()
//            addFragment(LoginStartUpFragment())
        }
    }

    fun checkApiSatusMessage(statusCode: Int, message: String) {
        when (statusCode) {
            ON_ERROR -> Dialogues.setMessage(message).showDialogueOnlyText(this)
            ON_RESEND_VERIFICATION -> Dialogues.setMessage(message).showDialogueOnlyText(this)
            ON_SESSION_EXPIRED -> sessionExpired(message)
        }
    }

    fun checkApiStatusFailed(error:Throwable)
    {

    }
    fun loge(msg: String) {
        Log.e(this.localClassName.toString(), msg)
    }

    fun loge(tag: String, msg: String) {
        Log.e(tag, msg)
    }
}