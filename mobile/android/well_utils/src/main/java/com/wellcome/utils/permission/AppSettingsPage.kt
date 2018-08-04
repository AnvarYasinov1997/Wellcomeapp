package com.wellcome.utils.permission

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

class AppSettingsPage(private val activity: AppCompatActivity) {

    fun requestSettingsPage() = async(UI) {
        suspendCoroutine<Unit> { cont ->
            AppSettingsFragment.cont = cont
            with(activity.supportFragmentManager) {
                val oldFragment =
                    findFragmentByTag(AppSettingsFragment.TAG) as? AppSettingsFragment?
                if (oldFragment == null) {
                    val fragment = AppSettingsFragment()
                    beginTransaction().add(fragment, AppSettingsFragment.TAG)
                            .commitAllowingStateLoss()
                    executePendingTransactions()
                    fragment.requestPage(activity)
                } else oldFragment.requestPage(activity)
            }
        }
    }

    @SuppressLint("ValidFragment")
    private class AppSettingsFragment : Fragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true
        }

        fun requestPage(activity: AppCompatActivity) {
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", activity.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
            startActivityForResult(intent, REQUEST_CODE)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == REQUEST_CODE) {
                cont?.resume(Unit)
            }
        }

        companion object {
            const val TAG = "AppSettingsFragment"
            var cont: Continuation<Unit>? = null
            private const val REQUEST_CODE = 101
        }
    }
}