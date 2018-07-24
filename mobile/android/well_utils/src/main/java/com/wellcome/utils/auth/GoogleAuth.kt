package com.wellcome.utils.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.wellcome.utils.R
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

class GoogleAuth(private val activity: AppCompatActivity) {
    private val googleClient by lazy {
        GoogleSignIn.getClient(activity,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(
                activity.getString(R.string.web_client_id)).build())
    }

    fun requestAuthDialog() = async(UI) {
        suspendCoroutine<AuthResult> { cont ->
            GoogleAuthFragment.cont = cont
            val intent = googleClient.signInIntent.apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
            activity.supportFragmentManager.apply {
                val oldFragment = findFragmentByTag(GoogleAuthFragment.TAG) as? GoogleAuthFragment?
                if (oldFragment != null) oldFragment.startActivityForResult(intent,
                    GoogleAuthFragment.GOOGLE_AUTH_CODE)
                else {
                    val fragment = GoogleAuthFragment()
                    beginTransaction().add(fragment, GoogleAuthFragment.TAG)
                            .commitAllowingStateLoss()
                    executePendingTransactions()
                    fragment.startActivityForResult(intent, GoogleAuthFragment.GOOGLE_AUTH_CODE)
                }
            }
        }
    }

    @SuppressLint("ValidFragment")
    private class GoogleAuthFragment : Fragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == GOOGLE_AUTH_CODE) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleResult(task)
            }
        }

        private fun handleResult(task: Task<GoogleSignInAccount>) {
            try {
                val account = task.getResult(ApiException::class.java)
                cont?.resume(AuthResult.ResultGranted(account))

            } catch (e: ApiException) {
                if (e.statusCode == 13) {
                    cont?.resume(AuthResult.ResultCanceled(e))
                } else {
                    cont?.resume(AuthResult.ResultApiException(e))
                }
            }
        }

        companion object {
            var cont: Continuation<AuthResult>? = null
            const val TAG = "GoogleAuthFragment"
            const val GOOGLE_AUTH_CODE = 102

        }
    }
}