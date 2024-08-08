package com.example.helper.extras

import android.animation.TimeInterpolator
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TableLayout
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.view.updateMargins
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.Serializable


//-------------Coroutine----------------
/**
 * This is the Main thread coroutine scope for lifecycleOwner
 * @param block - code block to run on this thread
 */
fun LifecycleOwner.mainThread(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Main) { block() }
}

/**
 * This is the IO thread coroutine scope for lifecycleOwner
 * @param block - code block to run on this thread
 */
fun LifecycleOwner.ioThread(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) { block() }
}

/**
 * This is the Default thread coroutine scope for lifecycleOwner
 * @param block - code block to run on this thread
 */
fun LifecycleOwner.defaultThread(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Default) { block() }
}

/**
 * This is the Unconfined thread coroutine scope for lifecycleOwner
 * @param block - code block to run on this thread
 */
fun LifecycleOwner.unconfinedThread(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Unconfined) { block() }
}


/**
 * This is the Main thread coroutine scope for lifecycleOwner
 * @param block - code block to run on this thread
 */
fun ViewModel.mainThread(block: CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.Main) { block() }
}

/**
 * This is the IO thread coroutine scope for lifecycleOwner
 * @param block - code block to run on this thread
 */
fun ViewModel.ioThread(block: CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.IO) { block() }
}

/**
 * This is the Default thread coroutine scope for lifecycleOwner
 * @param block - code block to run on this thread
 */
fun ViewModel.defaultThread(block: CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.Default) { block() }
}

/**
 * This is the Unconfined thread coroutine scope for lifecycleOwner
 * @param block - code block to run on this thread
 */
fun ViewModel.unconfinedThread(block: CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.Unconfined) { block() }
}



//-------------Intent----------------
/**
 * To get serializable value from Intent
 * @param key - key to get value
 *
 * @return T - Serializable extended value
 */
inline fun <reified T: Serializable> Intent.getSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializableExtra(key, T::class.java)!!
    } else {
        this.getSerializableExtra(key) as? T
    }
}

/**
 * To put Bitmap in Intent
 * @param context - Context
 * @param key - key to put Bitmap
 * @param bitmap - Bitmap to put
 *
 * @return Intent object
 */
fun Intent.putBitmapExtra(context: Context, key: String, bitmap: Bitmap): Intent {
    val filename = "BMP_${System.currentTimeMillis()}.png"
    val stream: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

    //Cleanup
    stream.close()

    this.putExtra(key, filename)
    return this
}

/**
 * To get Bitmap from Intent
 * @param context - Context
 * @param key - key to get Bitmap from
 *
 * @return Bitmap if exist otherwise null
 */
fun Intent.getBitmapExtra(context: Context, key: String): Bitmap? {
    val bmp: Bitmap?
    val filename = this.getStringExtra(key)
    val `is`: FileInputStream = context.openFileInput(filename)
    bmp = BitmapFactory.decodeStream(`is`)
    `is`.close()
    return bmp
}



//-------------View----------------
/**
 * To set Alpha animation
 * @param alpha - alpha value
 * @param duration - animation duration
 * @param startDelay - delay before start
 * @param interpolator - animation interpolator style
 *
 * @return this View
 * @see startAnimate to start animation
 */
fun View.animateAlpha(alpha: Float, duration: Long = 300L, startDelay: Long = 0L, interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()): View {
    this.animate().apply {
        this.alpha(alpha)
        this.duration = duration
        this.startDelay = startDelay
        this.interpolator = interpolator
    }
    return this
}

/**
 * To set TranslationX animation
 * @param translationX - translationX value
 * @param duration - animation duration
 * @param startDelay - delay before start
 * @param interpolator - animation interpolator style
 *
 * @return this View
 * @see startAnimate to start animation
 */
fun View.animateTranslationX(translationX: Float, duration: Long = 300L, startDelay: Long = 0L, interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()): View {
    this.animate().apply {
        this.translationX(translationX)
        this.duration = duration
        this.startDelay = startDelay
        this.interpolator = interpolator
    }
    return this
}

/**
 * To set TranslationY animation
 * @param translationY - translationY value
 * @param duration - animation duration
 * @param startDelay - delay before start
 * @param interpolator - animation interpolator style
 *
 * @return this View
 * @see startAnimate to start animation
 */
fun View.animateTranslationY(translationY: Float, duration: Long = 300L, startDelay: Long = 0L, interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()): View {
    this.animate().apply {
        this.translationY(translationY)
        this.duration = duration
        this.startDelay = startDelay
        this.interpolator = interpolator
    }
    return this
}

/**
 * To set Rotation animation
 * @param rotation - Rotation value
 * @param duration - animation duration
 * @param startDelay - delay before start
 * @param interpolator - animation interpolator style
 *
 * @return this View
 * @see startAnimate to start animation
 */
fun View.animateRotation(rotation: Float, duration: Long = 300L, startDelay: Long = 0L, interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()): View {
    this.animate().apply {
        this.rotation(rotation)
        this.duration = duration
        this.startDelay = startDelay
        this.interpolator = interpolator
    }
    return this
}

/**
 * To set ScaleX animation
 * @param scaleX - scaleX value
 * @param duration - animation duration
 * @param startDelay - delay before start
 * @param interpolator - animation interpolator style
 *
 * @return this View
 * @see startAnimate to start animation
 */
fun View.animateScaleX(scaleX: Float, duration: Long = 300L, startDelay: Long = 0L, interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()): View {
    this.animate().apply {
        this.scaleX(scaleX)
        this.duration = duration
        this.startDelay = startDelay
        this.interpolator = interpolator
    }
    return this
}

/**
 * To set ScaleY animation
 * @param scaleY - scaleY value
 * @param duration - animation duration
 * @param startDelay - delay before start
 * @param interpolator - animation interpolator style
 *
 * @return this View
 * @see startAnimate to start animation
 */
fun View.animateScaleY(scaleY: Float, duration: Long = 300L, startDelay: Long = 0L, interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()): View {
    this.animate().apply {
        this.scaleY(scaleY)
        this.duration = duration
        this.startDelay = startDelay
        this.interpolator = interpolator
    }
    return this
}

/**
 * call to start animation set before
 */
fun View.startAnimate(): View {
    this.animate().apply {
        this.start()
    }
    return this
}

/**
 * To make View visible
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}
/**
 * To make View invisible
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}
/**
 * To make View gone
 */
fun View.gone() {
    this.visibility = View.GONE
}


/**
 * To set margin to View
 * @param all - margin all side
 */
fun View.setMargin(all: Int) {
    (this.layoutParams as? MarginLayoutParams)?.setMargins(all)
}

/**
 * To set margin to View
 * @param left - margin left
 * @param top - margin top
 * @param right - margin right
 * @param bottom - margin bottom
 */
fun View.setMargin(left: Int = this.marginLeft, top: Int = this.marginTop, right: Int = this.marginRight, bottom: Int = this.marginBottom) {
    (this.layoutParams as? MarginLayoutParams)?.updateMargins(left, top, right, bottom)
}


//-------------Context----------------
/**
 * To get Color
 */
fun Context.getResourceColor(@ColorRes color: Int): Int {
    return ResourcesCompat.getColor(this.resources, color, null)
}
/**
 * To get Drawable
 */
fun Context.getResourceDrawable(@DrawableRes drawable: Int): Drawable? {
    return ResourcesCompat.getDrawable(this.resources, drawable, null)
}
/**
 * To get Dimen
 */
fun Context.getResourceDimen(@DimenRes dimen: Int): Float {
    return ResourcesCompat.getFloat(this.resources, dimen)
}