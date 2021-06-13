package com.vashkpi.digitalretailgroup.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.gson.*
import com.vashkpi.digitalretailgroup.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.reflect.Type

fun Fragment.showMessage(messageResId: Int, btnTextResId: Int, btnListener: View.OnClickListener, duration: Int) {
    view?.let { v ->
        messageResId.let { msg ->
            Snackbar
                .make(v, msg, duration)
//                .apply {
//                    view.background = ContextCompat.getDrawable(view.context, R.drawable.notification_card_item_bgr)
//                    view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
//                }
                .setBackgroundTint(Color.parseColor("#4B685A"))
                .setAction(btnTextResId, btnListener)
                .show()
        }
    }
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.hideKeyboard() {
    val imm = getSystemService(context, InputMethodManager::class.java)
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

@SuppressLint("ClickableViewAccessibility")
fun View.changeAlphaOnTouch() {
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            v.alpha = 0.6f
        } else {
            v.alpha = 1f
        }
        false
    }
}

/**
 * Run Retrofit's blocking method in Dispatchers.IO
 * https://stackoverflow.com/questions/64190661/retrofit-response-errorbody-string-gives-me-warning-of-inappropriate-blocking
 */
@Suppress("BlockingMethodInNonBlockingContext")
suspend fun ResponseBody.stringSuspending(): String =
    withContext(Dispatchers.IO) { string() }

/**
 * Safely navigate to destinations, avoiding possible errors
 */
fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

/**
 * Safely navigate to destinations, avoiding possible errors
 */
fun NavController.safeNavigate(
    @IdRes currentDestinationId: Int,
    @IdRes id: Int,
    args: Bundle? = null
) {
    if (currentDestinationId == currentDestination?.id) {
        navigate(id, args)
    }
}

//Json utils

/**
 * Transform any object into JSONObject
 */
fun Any.toJsonObject(): JSONObject {
    return JSONObject(Gson().toJson(this))
}

//fun JsonArray.asArrayOfJsonPrimitiveStrings(): ArrayList<JsonPrimitive> {
//    val stringsArray = arrayListOf<JsonPrimitive>()
//    this.forEach {
//        stringsArray.add(JsonPrimitive(it.toString()))
//    }
//    return stringsArray
//}

fun JsonObject.getFirstElementName(): String {
    return this.keySet().first().toString()
}