package com.vashkpi.digitalretailgroup.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.installations.FirebaseInstallations
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import timber.log.Timber
import java.util.*


fun Context.getDeviceId(): String {
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
}

fun Fragment.showMessage(messageResId: Int, btnTextResId: Int, btnListener: View.OnClickListener, duration: Int, callBack: Snackbar.Callback) {
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
                .addCallback(callBack)
                .show()
        }
    }
}

fun Fragment.showErrorMessage(message: String) {
    view?.let { v ->
        message.let { msg ->
            Snackbar
                .make(v, msg, 5000)
//                .apply {
//                    view.background = ContextCompat.getDrawable(view.context, R.drawable.notification_card_item_bgr)
//                    view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
//                }
                //.setBackgroundTint(Color.parseColor("#4B685A"))
                //.setAction(btnTextResId, btnListener)
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
    currentDestination?.getAction(direction.actionId)?.run {
        //navigate(direction)

        navigate(direction)
    } ?: kotlin.run{
        Timber.d("Unable to navigate")
    }
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

fun Fragment.safeOpenUrlInBrowser(url: String) {
    try {
        val intent =
            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
    catch (t: Throwable) {
        Timber.e(t)
        showErrorMessage(t.message.toString())
    }
}

fun Fragment.safeOpenCallDialer(phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }
    catch (t: Throwable) {
        Timber.e(t)
        showErrorMessage(t.message.toString())
    }
}

fun Fragment.safeOpenAddressInMaps(address: String) {
    try {
        val intent =
            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MAPS)
        intent.data = Uri.parse("geo:0,0?q=$address")
        startActivity(intent)
    }
    catch (t: Throwable) {
        Timber.e(t)
        if (t is android.content.ActivityNotFoundException) {
            this.safeOpenAddressInBrowser(address)
        }
        else {
            showErrorMessage(t.message.toString())
        }
    }
}

fun Fragment.safeOpenAddressInBrowser(address: String) {

    try {
//        val intent =
//            Intent.makeMainSelectorActivity(Intent.ACTION_WEB_SEARCH, Intent.CATEGORY_APP_BROWSER)
//        //intent.data = Uri.parse("geo:0,0?q=$address")
//        intent.putExtra(SearchManager.QUERY, address)
//        startActivity(intent)

        val uri = Uri.parse("http://www.google.com/?q=$address")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    catch (t: Throwable) {
        Timber.e(t)
        showErrorMessage(t.message.toString())
    }
}

fun String.capitalizeWords(): String = split(" ").map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
    Locale.getDefault()) else it.toString() } }.joinToString(" ")

fun obtainFcmToken(onSuccess: (String) -> Unit,
                   onError: () -> Unit) {
    FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            task.result?.let { result ->
                onSuccess(result.token)
            } ?: run {
                onError()
            }
        }
        else {
            onError()
        }
    }
}