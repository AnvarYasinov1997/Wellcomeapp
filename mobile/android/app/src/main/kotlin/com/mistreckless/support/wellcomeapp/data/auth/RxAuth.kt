package com.mistreckless.support.wellcomeapp.data.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.mistreckless.support.wellcomeapp.R
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject

class RxAuth(private val activity: AppCompatActivity) {

    private val googleClient by lazy {
        GoogleSignIn.getClient(
            activity, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(activity.getString(R.string.web_client_id))
                .build()
        )
    }

    fun authWithGoogle(): Single<GoogleSignInAccount> {
        val fragment = GoogleAuthFragment()
        val intent = googleClient.signInIntent
        with(activity.supportFragmentManager) {
            if (findFragmentByTag(GOOGLE_AUTH_TAG) == null) {
                beginTransaction()
                    .add(fragment, GOOGLE_AUTH_TAG)
                    .commitAllowingStateLoss()
                executePendingTransactions()
                fragment.startActivityForResult(intent, RC_SIGN_IN)
            }
        }
        return googleSubject
    }

    class GoogleAuthFragment : Fragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == RC_SIGN_IN) {
                if (resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    handleResult(task)
                } else googleSubject.onError(Exception("cancel"))
            }
        }

        private fun handleResult(task: Task<GoogleSignInAccount>) {
            try {
                val account = task.getResult(ApiException::class.java)
                googleSubject.onSuccess(account)

            } catch (e: ApiException) {
                googleSubject.onError(e)
            }
        }

    }

    companion object {
        private const val RC_SIGN_IN = 88
        private const val GOOGLE_AUTH_TAG = "google_auth_fragment"
        private val googleSubject by lazy { SingleSubject.create<GoogleSignInAccount>() }
    }
}