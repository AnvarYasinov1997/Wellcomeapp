package com.wellcome.utils.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException

sealed class AuthResult {
    data class ResultApiException(val exception: ApiException) : AuthResult()
    data class ResultCanceled(val exception: ApiException) : AuthResult()
    data class ResultGranted(val signInAccount: GoogleSignInAccount) : AuthResult()
}
