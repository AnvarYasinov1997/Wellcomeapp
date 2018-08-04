package com.wellcome.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import kotlin.coroutines.experimental.Continuation

class RationaleDialog(private val fragmentManager: FragmentManager) {

    suspend fun request(title: String, okTitle: String) = async {
        suspendCancellableCoroutine<Boolean> { cont ->
            RationaleDialogFragment.cont = cont
            val dialog =
                fragmentManager.findFragmentByTag(RationaleDialogFragment.TAG) as? DialogFragment
            if (dialog == null) RationaleDialogFragment.newInstance(title, okTitle).show(
                fragmentManager,
                RationaleDialogFragment.TAG)

        }
    }

    @SuppressLint("ValidFragment")
    private class RationaleDialogFragment : DialogFragment() {
        private val title by lazy {
            arguments!!.getString(TITLE_KEY)!!
        }
        private val okTitle by lazy {
            arguments!!.getString(OK_KEY)!!
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(activity!!).setCancelable(false).setTitle(title)
                    .setPositiveButton(okTitle) { _, _ ->
                        cont.resume(true)
                    }.create()
        }

        companion object {
            lateinit var cont: Continuation<Boolean>
            const val TAG = "RationaleDialog"
            const val TITLE_KEY = "title_key"
            const val OK_KEY = "ok_key"
            fun newInstance(title: String, okTitle: String): RationaleDialogFragment =
                RationaleDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(TITLE_KEY, title)
                        putString(OK_KEY, okTitle)
                    }
                }
        }
    }

}
