package com.apdallahy3.accenturetask.base

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apdallahy3.accenturetask.R
import com.apdallahy3.accenturetask.utils.KeyboardUtils

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    private var confirmMessageDialog: AlertDialog? = null
    private var messageDialog: AlertDialog? = null
    lateinit var binding: T
    lateinit var loading: AlertDialog
    private var isCancelable = false
    var isCheckCountInBackPress = true

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    @LayoutRes
    open fun getLayoutIdLoading(): Int = -1

    open fun getThemResId(): Int = -1

    protected abstract fun updateUI(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        initDialog()
        updateUI(savedInstanceState)
    }


    protected open fun performDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutRes())
        binding.executePendingBindings()
    }


    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 1 && isCheckCountInBackPress) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Init dialog loading
     */
    private fun initDialog() {
        val builder: AlertDialog.Builder = if (getThemResId() != -1)
            AlertDialog.Builder(this, getThemResId()) else AlertDialog.Builder(this)

        builder.setCancelable(isCancelable)
        builder.setView(getLayoutIdLoading())
        loading = builder.create()
    }

    /**
     * Show dialog loading
     */
    open fun showDialog() {
        runOnUiThread {
            if (!loading.isShowing) {
                loading.show()
            }
        }
    }


    /**
     * Hide dialog loading
     */
    open fun hideDialog() {
        runOnUiThread {
            if (loading.isShowing) {
                loading.dismiss()
            }
        }
    }

    /**
     * Set cancelable dialog
     */
    fun setCancelableDialog(isCancelable: Boolean) {
        this.isCancelable = isCancelable
    }

    fun hideKeyboard() {
        KeyboardUtils.hideKeyboard(this)
    }

    fun hideKeyboardOutSide(view: View) {
        KeyboardUtils.hideKeyBoardWhenClickOutSide(view, this)
    }

    fun hideKeyboardOutSideText(view: View) {
        KeyboardUtils.hideKeyBoardWhenClickOutSideText(view, this)
    }


    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        rcv: RecyclerView,
        adapter: RecyclerView.Adapter<VH>
    ) {
        rcv.setHasFixedSize(true)
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        rcv: RecyclerView, adapter:
        RecyclerView.Adapter<VH>,
        isHasFixedSize: Boolean,
        isNestedScrollingEnabled: Boolean
    ) {
        rcv.setHasFixedSize(isHasFixedSize)
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter
        rcv.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        rcv: RecyclerView,
        adapter: RecyclerView.Adapter<VH>,
        isNestedScrollingEnabled: Boolean
    ) {
        rcv.setHasFixedSize(true)
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter
        rcv.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    open fun clearAllBackStack() {
        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }


    fun addFragment(fragment: Fragment, id: Int, addToBackStack: Boolean) {

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        if (addToBackStack)
            transaction.addToBackStack(fragment.javaClass.simpleName)

        transaction.add(id, fragment, fragment.javaClass.simpleName)
        transaction.commit()

    }


    fun replaceFragment(fragment: Fragment, id: Int, addToBackStack: Boolean) {

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()


        if (addToBackStack)
            transaction.addToBackStack(fragment.javaClass.canonicalName)

        transaction.replace(id, fragment, fragment.javaClass.canonicalName)
        transaction.commit()
    }

    /**
     * show message dialog
     */
    fun showMessagDialog(message: String) {

        runOnUiThread {

            if (messageDialog?.isShowing == true) return@runOnUiThread

            val builder: AlertDialog.Builder = if (getThemResId() != -1)
                AlertDialog.Builder(this, getThemResId()) else AlertDialog.Builder(this)

            builder.setCancelable(isCancelable)
            builder.setMessage(message)

            //Yes Action
            builder.setPositiveButton(
                R.string.yes,
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })

            messageDialog = builder.create()

            messageDialog?.show()
        }
    }

    /**
     * Show confirm message dialog loading
     */
    fun showConfirmMessagDialog(
        message: String,
        yesText: Int,
        noText: Int,
        yesAction: () -> Unit,
        noAction: () -> Unit
    ) {

        runOnUiThread {

            if (confirmMessageDialog?.isShowing == true) return@runOnUiThread


            val builder: AlertDialog.Builder = if (getThemResId() != -1)
                AlertDialog.Builder(this, getThemResId()) else AlertDialog.Builder(this)

            builder.setCancelable(isCancelable)
            builder.setMessage(message)

            //Yes Action
            builder.setPositiveButton(
                yesText,
                DialogInterface.OnClickListener { dialog, _ ->
                    yesAction.invoke()
                    dialog.dismiss()
                })

            //No Action
            builder.setNegativeButton(
                noText,
                DialogInterface.OnClickListener { dialog, which ->
                    noAction.invoke()
                    dialog.dismiss()
                })
            confirmMessageDialog = builder.create()

            confirmMessageDialog?.show()
        }
    }

    open fun loadUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)

    }



    open fun <T> setAdapter(list: ArrayList<T>): ArrayAdapter<T> {
        return ArrayAdapter<T>(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            list // Array
        )
    }
}

