package com.wellcome.utils.permission

sealed class PermissionResult {

    val permission: String
        get() = permissions[0]

    abstract val permissions: List<String>

    data class Granted(override val permissions: List<String>) : PermissionResult()

    data class ShouldShowRationale(override val permissions: List<String>) : PermissionResult()

    data class Denied(override val permissions: List<String>) : PermissionResult()

    data class NeverAskAgain(override val permissions: List<String>) : PermissionResult()
}