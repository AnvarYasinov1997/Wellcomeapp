package wellcome.common.core

import android.util.Log

actual fun log(tag: String, message: String) {
    Log.e(tag, message)
}