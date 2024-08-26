package com.example.helper.extras

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Interpolator
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TableLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.animation.ArgbEvaluatorCompat
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Date
import kotlin.math.max


//-------------Coroutine----------------
/**
 * This is the Main thread coroutine scope for lifecycleOwner
 * @param block -> code block to run on this thread
 */
fun LifecycleOwner.lifeCycleMainThread(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Main) { block() }
}

/**
 * This is the IO thread coroutine scope for lifecycleOwner
 * @param block -> code block to run on this thread
 */
fun LifecycleOwner.lifeCycleIoThread(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) { block() }
}

/**
 * This is the Default thread coroutine scope for lifecycleOwner
 * @param block -> code block to run on this thread
 */
fun LifecycleOwner.lifeCycleDefaultThread(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Default) { block() }
}

/**
 * This is the Unconfined thread coroutine scope for lifecycleOwner
 * @param block -> code block to run on this thread
 */
fun LifecycleOwner.lifeCycleUnconfinedThread(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Unconfined) { block() }
}


/**
 * This is the Main thread coroutine scope for lifecycleOwner
 * @param block -> code block to run on this thread
 */
fun ViewModel.viewModelMainThread(block: CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.Main) { block() }
}

/**
 * This is the IO thread coroutine scope for lifecycleOwner
 * @param block -> code block to run on this thread
 */
fun ViewModel.viewModelIoThread(block: CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.IO) { block() }
}

/**
 * This is the Default thread coroutine scope for lifecycleOwner
 * @param block -> code block to run on this thread
 */
fun ViewModel.viewModelDefaultThread(block: CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.Default) { block() }
}

/**
 * This is the Unconfined thread coroutine scope for lifecycleOwner
 * @param block -> code block to run on this thread
 */
fun ViewModel.viewModelUnconfinedThread(block: CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.Unconfined) { block() }
}

/**
 * This is the ui thread for Activity
 * @param block -> code block to run on this thread
 */
fun Activity.uiThread(block: () -> Unit) {
    runOnUiThread { block() }
}



//-------------Intent----------------
/**
 * To get serializable value from Intent
 * @param key -> key to get value
 *
 * @return T -> Serializable extended value
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
 * @param context -> Context
 * @param key -> key to put Bitmap
 * @param bitmap -> Bitmap to put
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
 * @param context -> Context
 * @param key -> key to get Bitmap from
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
 * @param alpha -> alpha value
 * @param duration -> animation duration
 * @param startDelay -> delay before start
 * @param interpolator -> animation interpolator style
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
 * @param translationX -> translationX value
 * @param duration -> animation duration
 * @param startDelay -> delay before start
 * @param interpolator -> animation interpolator style
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
 * @param translationY -> translationY value
 * @param duration -> animation duration
 * @param startDelay -> delay before start
 * @param interpolator -> animation interpolator style
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
 * @param rotation -> Rotation value
 * @param duration -> animation duration
 * @param startDelay -> delay before start
 * @param interpolator -> animation interpolator style
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
 * @param scaleX -> scaleX value
 * @param duration -> animation duration
 * @param startDelay -> delay before start
 * @param interpolator -> animation interpolator style
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
 * @param scaleY -> scaleY value
 * @param duration -> animation duration
 * @param startDelay -> delay before start
 * @param interpolator -> animation interpolator style
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

val View.isVisible: Boolean
    get() {
        return this.visibility == View.VISIBLE
    }

val View.isGone: Boolean
    get() {
        return this.visibility == View.GONE
    }

val View.isInvisible: Boolean
    get() {
        return this.visibility == View.INVISIBLE
    }


/**
 * To set margin to View
 * @param all -> margin all side
 */
fun View.setMargin(all: Int) {
    (this.layoutParams as? MarginLayoutParams)?.setMargins(all)
}

/**
 * To set margin to View
 * @param left -> margin left
 * @param top -> margin top
 * @param right -> margin right
 * @param bottom -> margin bottom
 */
fun View.setMargin(left: Int = this.marginLeft, top: Int = this.marginTop, right: Int = this.marginRight, bottom: Int = this.marginBottom) {
    (this.layoutParams as? MarginLayoutParams)?.updateMargins(left, top, right, bottom)
}

fun View.setHeight(newValue: Int) {
    val params = layoutParams
    params?.let {
        params.height = newValue
        layoutParams = params
    }
}

fun View.setWidth(newValue: Int) {
    val params = layoutParams
    params?.let {
        params.width = newValue
        layoutParams = params
    }
}

fun View.setSize(width: Int, height: Int) {
    val params = layoutParams
    params?.let {
        params.width = width
        params.height = height
        layoutParams = params
    }
}

/**
 *  View as bitmap.
 */
fun View.getBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    canvas.save()
    return bitmap
}

/**
 * Aligns to left of the parent in relative layout
 */
fun View.alignParentStart() {
    val params = layoutParams as? RelativeLayout.LayoutParams

    params?.apply {
        addRule(RelativeLayout.ALIGN_PARENT_START)
    }

}

/**
 * Aligns to right of the parent in relative layout
 */
fun View.alignParentEnd() {
    val params = layoutParams as? RelativeLayout.LayoutParams

    params?.apply {
        addRule(RelativeLayout.ALIGN_PARENT_END)
    }

}


/**
 * Aligns in the center of the parent in relative layout
 */
fun View.centerInParent() {
    val params = layoutParams as? RelativeLayout.LayoutParams

    params?.apply {
        addRule(RelativeLayout.CENTER_IN_PARENT)
    }

}

/**
 * Aligns in the center horizontal of the parent in relative layout
 */
fun View.centerHorizontal() {
    val params = layoutParams as? RelativeLayout.LayoutParams

    params?.apply {
        addRule(RelativeLayout.CENTER_HORIZONTAL)
    }

}

/**
 * Aligns in the center vertical of the parent in relative layout
 */
fun View.centerVertical() {
    val params = layoutParams as? RelativeLayout.LayoutParams

    params?.apply {
        addRule(RelativeLayout.CENTER_VERTICAL)
    }

}

/**
 * Set weight to view in LinearLayout
 */
fun View.setWeight(weight: Float) {
    val params = layoutParams as? LinearLayout.LayoutParams

    params?.apply {
        layoutParams = LinearLayout.LayoutParams(width, height, weight)
    }
}

fun View.setOnScaleClickListener(onClick: (View) -> Unit) {
    var isPressed = false

    val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat("scaleX", 0.9f),
        PropertyValuesHolder.ofFloat("scaleY", 0.9f)
    )
    scaleDown.duration = 100

    val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat("scaleX", 1f),
        PropertyValuesHolder.ofFloat("scaleY", 1f)
    )
    scaleUp.duration = 100

    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                scaleDown.start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isPressed && isPressedInsideView(event)) {
                    performClick()
                    onClick(this)

                    scaleUp.start()
                    isPressed = false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isPressed && !isPressedInsideView(event)) {
                    isPressed = false
                    scaleUp.start()
                }
            }
        }
        true
    }
}

private fun View.isPressedInsideView(event: MotionEvent): Boolean {
    val rect = Rect()
    getGlobalVisibleRect(rect)
    return rect.contains(event.rawX.toInt(), event.rawY.toInt())
}

fun View.visibilityChangeListener(onVisibility: (isVisible: Boolean) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener {
        onVisibility(isVisible)
    }
}

fun View.createCircularReveal(
    revealDuration: Long = 1500L,
    interpolator: TimeInterpolator = FastOutSlowInInterpolator(),
    centerX: Int = width / 2,
    centerY: Int = height / 2,
    showAtEnd: Boolean = true,
    isReverse: Boolean = false
): Animator {

    val radius = max(width, height).toFloat()
    val startRadius = if (isReverse.not()) 0f else radius
    val finalRadius = if (isReverse.not()) radius else 0f

    val animator =
        ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, finalRadius).apply {
            this.interpolator = interpolator
            duration = revealDuration
            doOnEnd {
                if (showAtEnd) visible() else gone()
            }
            start()
        }
    return animator
}

fun View.createColoredCircularReveal(
    revealDuration: Long = 1500L,
    interpolator: TimeInterpolator = FastOutSlowInInterpolator(),
    centerX: Int = width / 2,
    centerY: Int = height / 2,
    @ColorRes startColor: Int,
    @ColorRes endColor: Int,
    showAtEnd: Boolean = true,
    isReverse: Boolean = false
): Animator {

    val radius = max(width, height).toFloat()
    val startRadius = if (isReverse.not()) 0f else radius
    val finalRadius = if (isReverse.not()) radius else 0f

    val animator =
        ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, finalRadius).apply {
            this.interpolator = interpolator
            duration = revealDuration
            doOnEnd {
                if (showAtEnd) visible() else gone()
            }
            start()
        }


    val oldBackground = background
    ValueAnimator().apply {
        setIntValues(ContextCompat.getColor(context, startColor), ContextCompat.getColor(context, endColor))
        setEvaluator(ArgbEvaluatorCompat())
        addUpdateListener { valueAnimator -> setBackgroundColor((valueAnimator.animatedValue as Int)) }
        doOnEnd { background = oldBackground }
        duration = revealDuration

        start()
    }
    return animator
}

@TargetApi(value = Build.VERSION_CODES.M)
fun View.setSelectableItemForeground() {
    foreground = ContextCompat.getDrawable(context, TypedValue().also { context.theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true) }.resourceId)
}

fun View.setSelectableItemBackground() {
    setBackgroundResource(TypedValue().also { context.theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true) }.resourceId)
}

@TargetApi(value = Build.VERSION_CODES.M)
fun View.setSelectableItemForegroundBorderless() {
    foreground = ContextCompat.getDrawable(context, TypedValue().also { context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, it, true) }.resourceId)
}

fun View.setSelectableItemBackgroundBorderless() {
    setBackgroundResource(TypedValue().also { context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, it, true) }.resourceId)
}

/**
 * Applys given func to all child views
 */
inline fun ViewGroup.eachChild(func: (view: View) -> Unit) {
    for (i in 0 until childCount) {
        func(getChildAt(i))
    }
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



//-------------Activity----------------
/**
 * Hide keyboard for an activity.
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

/**
 * Show keyboard for an activity.
 * @param toFocus -> View to show keyboard on
 */
fun Activity.showKeyboard(toFocus: View) {
    toFocus.requestFocus()
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(toFocus, InputMethodManager.SHOW_IMPLICIT);
//    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/**
 *  get display size in pixels
 */
fun Activity.getDisplaySizePixels(): Point {
    val display = this.windowManager.defaultDisplay
    return DisplayMetrics()
        .apply {
            display.getRealMetrics(this)
        }.let {
            Point(it.widthPixels, it.heightPixels)
        }
}

/**
 * Set Status Bar Color
 */
fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

/**
 * Set Navigation Bar Color
 */
fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    window.navigationBarColor = color
}

/**
 * Set Navigation Bar Divider Color if Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
 */
@RequiresApi(api = Build.VERSION_CODES.P)
fun Activity.setNavigationBarDividerColor(@ColorInt color: Int) {
    window.navigationBarDividerColor = color
}

/**
 *  Makes the activity enter fullscreen mode.
 */
@UiThread
fun Activity.enterFullScreenMode() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}


/**
 * Makes the Activity exit fullscreen mode.
 */
@UiThread
fun Activity.exitFullScreenMode() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
}

/**
 * Hide navigation buttons
 */
@SuppressLint("ObsoleteSdkInt")
fun Activity.hideBottomBar() {
    if (Build.VERSION.SDK_INT < 19) { // lower api
        val v = this.window.decorView
        v.systemUiVisibility = View.GONE
    } else {
        //for new api versions.
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }
}

/**
 * Show navigation buttons
 */
@SuppressLint("ObsoleteSdkInt")
fun Activity.showBottomBar() {
    if (Build.VERSION.SDK_INT < 19) { // lower api
        val v = this.window.decorView
        v.systemUiVisibility = View.VISIBLE
    } else {
        //for new api versions.
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
        decorView.systemUiVisibility = uiOptions
    }
}

/**
 * Lock screen orientation
 */
fun Activity.lockOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
}

/**
 * Lock screen orientation Portrait
 */
@SuppressLint("SourceLockedOrientationActivity")
fun Activity.lockOrientationPortrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

/**
 * Lock screen orientation Horizontal
 */
fun Activity.lockOrientationHorizontal() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

/**
 * Unlock screen orientation
 */
fun Activity.unlockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
}

/**
 * Get screenShort as bitmap
 */
fun Activity.screenShot(removeStatusBar: Boolean = false): Bitmap? {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)

    val bmp = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.RGB_565)
    val canvas = Canvas(bmp)
    window.decorView.draw(canvas)


    return if (removeStatusBar) {
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight = rect.top
        Bitmap.createBitmap(
            bmp,
            0,
            statusBarHeight,
            dm.widthPixels,
            dm.heightPixels - statusBarHeight
        )
    } else {
        Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels)
    }
}

/**
 * Check that the device has permission for picture in picture mode or not
 */
fun Activity.hasPipPermission(): Boolean {
    val appOps = getSystemService(AppOpsManager::class.java)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps?.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                android.os.Process.myUid(),
                packageName
            ) == AppOpsManager.MODE_ALLOWED
        } else {
            appOps?.checkOpNoThrow(
                AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                android.os.Process.myUid(),
                packageName
            ) == AppOpsManager.MODE_ALLOWED
        }
    } else {
        return try {
            packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
        } catch (e: Exception) {
            false
        }
    }
}


//-------------Binding----------------
/**
 * To get viewBinding in Activity
 */
inline fun <reified T : ViewBinding> Activity.viewBinding(
    parent: ViewGroup? = null,
    attachToParent: Boolean = false,
): Lazy<T> {
    return object : Lazy<T> {

        private var cached: T? = null

        override val value: T
            get() {
                if (cached == null) {
                    cached = T::class.java.getDeclaredMethod(
                        "inflate", LayoutInflater::class.java,
                        ViewGroup::class.java, Boolean::class.java
                    ).invoke(null, layoutInflater, parent, attachToParent) as T
                }
                return cached!!
            }

        override fun isInitialized() = cached != null
    }
}

/**
 * To get viewBinding in Fragment
 */
inline fun <reified T : ViewBinding> Fragment.viewBinding(
    parent: ViewGroup? = null,
    attachToParent: Boolean = false,
): Lazy<T> {
    return object : Lazy<T> {

        private var cached: T? = null

        override val value: T
            get() {
                if (cached == null) {
                    cached = T::class.java.getDeclaredMethod(
                        "inflate", LayoutInflater::class.java,
                        ViewGroup::class.java, Boolean::class.java
                    ).invoke(null, layoutInflater, parent, attachToParent) as T
                }
                return cached!!
            }

        override fun isInitialized() = cached != null
    }
}

/**
 * To get viewBinding using Context
 */
inline fun <reified T : ViewBinding> Context.viewBinding(
    parent: ViewGroup? = null,
    attachToParent: Boolean = false,
): Lazy<T> {
    return object : Lazy<T> {

        private var cached: T? = null

        override val value: T
            get() {
                if (cached == null) {
                    cached = T::class.java.getDeclaredMethod(
                        "inflate", LayoutInflater::class.java,
                        ViewGroup::class.java, Boolean::class.java
                    ).invoke(null, LayoutInflater.from(this@viewBinding), parent, attachToParent) as T
                }
                return cached!!
            }

        override fun isInitialized() = cached != null
    }
}



//-------------Collections----------------
/**
 * Moves the given **T** item to the specified index
 */
fun <T> MutableList<T>.move(item: T, newIndex: Int) {
    val currentIndex = indexOf(item)
    if (currentIndex < 0) return
    removeAt(currentIndex)
    add(newIndex, item)
}

/**
 * Moves the given item at the `oldIndex` to the `newIndex`
 */
fun <T> MutableList<T>.moveAt(oldIndex: Int, newIndex: Int) {
    val item = this[oldIndex]
    removeAt(oldIndex)
    if (oldIndex > newIndex)
        add(newIndex, item)
    else
        add(newIndex - 1, item)
}

/**
 * Check that index is in bound or not
 */
private fun <T> List<T>.isInBounds(index: Int): Boolean {
    return index in 0..lastIndex
}

/**
 * Swaps the two items
 */
fun <T> List<T>.swap(i: Int, j: Int): List<T> {
    if (isInBounds(i) && isInBounds(j)) {
        Collections.swap(this, i, j)
    }
    return this
}

/**
 * Swaps the two items
 */
fun <T> MutableList<T>.swap(itemOne: T, itemTwo: T) = swap(indexOf(itemOne), indexOf(itemTwo))

/**
 * do something if Collection contains item
 */
fun <T> Collection<T>.doIfContained(item: T, func: T.() -> Unit): Boolean {
    if (contains(item)) {
        item.func()
        return true
    }
    return false
}

/**
 * add this value in given lists
 */
fun <T> T.addTo(vararg lists: MutableList<T>) {
    lists.fold(Unit) { _, list ->
        list.add(this)
    }
}

/**
 * remove this value from given lists
 */
fun <T> T.removeFrom(vararg lists: MutableList<T>) {
    lists.fold(Unit) { _, list ->
        list.remove(this)
    }
}

/**
 * remove this value from give values if given prediction is true
 */
fun <T> T.removeFrom(vararg lists: MutableList<T>, predicate: (T, T) -> Boolean) {
    lists.fold(Unit) { _, list ->
        list.removeAt(list.indexOfFirst { predicate(this, it) })
    }
}

/**
 * remove all this value from give lists
 */
fun <T> T.removeAllFrom(vararg lists: MutableList<T>) {
    lists.fold(Unit) { _, list ->
        list.removeAll { it == this }
    }
}

/**
 * remove all this value from given lists if prediction in true
 */
fun <T> T.removeAllFrom(vararg lists: MutableList<T>, predicate: (T, T) -> Boolean) {
    lists.fold(Unit) { _, list ->
        list.removeAll { predicate(this, it) }
    }
}

/**
 * return count of this value in given list
 */
fun <T> T.countIn(list: MutableList<T>) = list.count { it == this }

/**
 * Returns true if an element matching the given [predicate] was found.
 */
inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean {
    for (element in this) if (predicate(element)) return true
    return false
}

/**
 * Remove duplicate values
 */
inline fun <reified T> Collection<T>.removeDuplicates(): MutableList<T> {
    return this.toSet().toMutableList()
}


//-------------Date/Time----------------
fun Date.millisecondsSince(date: Date) = (time - date.time)
fun Date.secondsSince(date: Date): Double = millisecondsSince(date) / 1000.0
fun Date.minutesSince(date: Date): Double = secondsSince(date) / 60
fun Date.hoursSince(date: Date): Double = minutesSince(date) / 60
fun Date.daysSince(date: Date): Double = hoursSince(date) / 24
fun Date.weeksSince(date: Date): Double = daysSince(date) / 7
fun Date.monthsSince(date: Date): Double = weeksSince(date) / 4
fun Date.yearsSince(date: Date): Double = monthsSince(date) / 12

/**
 * Gives [Calendar] object from Date
 */
inline val Date.calendar: Calendar
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar
    }


/**
* Gets  Year directly from [Calendar] Object
*/
inline val Calendar.year: Int
    get() = get(Calendar.YEAR)

/**
 * Gets value of DayOfMonth from [Calendar] Object
 */
inline val Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)

/**
 * Gets value of Month from [Calendar] Object
 */
inline val Calendar.month: Int
    get() = get(Calendar.MONTH)

/**
 * Gets value of Hour from [Calendar] Object
 */
inline val Calendar.hour: Int
    get() = get(Calendar.HOUR)

/**
 * Gets value of HourOfDay from [Calendar] Object
 */
inline val Calendar.hourOfDay: Int
    get() = get(Calendar.HOUR_OF_DAY)

/**
 * Gets value of Minute from [Calendar] Object
 */
inline val Calendar.minute: Int
    get() = get(Calendar.MINUTE)

/**
 * Gets value of Second from [Calendar] Object
 */
inline val Calendar.second: Int
    get() = get(Calendar.SECOND)

/**
 * Gets value of DayOfMonth from [Date] Object
 */
inline val Date.yearFromCalendar: Int
    get() = calendar.year

/**
 * Gets value of DayOfMonth from [Date] Object
 */
inline val Date.dayOfMonth: Int
    get() = calendar.dayOfMonth

/**
 * Gets value of Month from [Date] Object
 */
inline val Date.monthFromCalendar: Int
    get() = calendar.month

/**
 * Gets value of Hour from [Date] Object
 */
inline val Date.hour: Int
    get() = calendar.hour

/**
 * Gets value of HourOfDay from [Date] Object
 */
inline val Date.hourOfDay: Int
    get() = calendar.hourOfDay

/**
 * Gets value of Minute from [Date] Object
 */
inline val Date.minute: Int
    get() = calendar.minute


/**
 * Gets value of Second from [Date] Object
 */
inline val Date.second: Int
    get() = calendar.second

/**
 * Converts current date to proper provided format
 */
fun Date.convertTo(format: String): String? {
    var dateStr: String? = null
    val df = SimpleDateFormat(format)
    try {
        dateStr = df.format(this)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    return dateStr
}

fun Date.isFuture(): Boolean {
    return !Date().before(this)
}

fun Date.isPast(): Boolean {
    return Date().before(this)
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isTomorrow(): Boolean {
    return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}


//-------------File----------------
fun File.name() = this.nameWithExtension().substringBeforeLast(".")

fun File.nameWithExtension() = this.absolutePath.substringAfterLast("/")

fun File.extension() = this.absolutePath.substringAfterLast(".")


//-------------Gson----------------
fun Any.toJson(): String = Gson().toJson(this)

inline fun <reified T : Any> String.toData(): T = Gson().fromJson(this, T::class.java)


//-------------Bitmap----------------
fun Bitmap.toOutputStream(
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): OutputStream {
    val stream = ByteArrayOutputStream()
    compress(compressFormat, quality, stream)
    return stream
}

fun Bitmap.toInputStream(
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): InputStream {
    val stream = ByteArrayOutputStream()
    compress(compressFormat, quality, stream)
    val byteArray = stream.toByteArray()
    return ByteArrayInputStream(byteArray)
}

@RequiresApi(26)
fun Bitmap.toIcon(): Icon = Icon.createWithBitmap(this)

fun Bitmap.toDrawable(resources: Resources) = BitmapDrawable(resources, this)

fun Bitmap.toRoundCorner(radius: Float): Bitmap? {
    val width = this.width
    val height = this.height
    val bitmap = Bitmap.createBitmap(width, height, this.config)
    val paint = Paint()
    val canvas = Canvas(bitmap)
    val rect = Rect(0, 0, width, height)

    paint.isAntiAlias = true
    canvas.drawRoundRect(RectF(rect), radius, radius, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)

    return bitmap
}

/**
 * Method to save Bitmap to specified file path.
 */
fun Bitmap.saveAsFile(path: String, format: Bitmap.CompressFormat, quality: Int) {
    require(quality in 0..100) { "quality must be in 0 to 100" }
    val f = File(path)
    if (!f.exists()) {
        f.createNewFile()
    }
    val stream = FileOutputStream(f)
    compress(format, quality, stream)
    stream.flush()
    stream.close()
}

fun Bitmap.flipHorizontally(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(1.0f, -1.0f)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.flipVertically(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(-1.0f, 1.0f)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

/**
 * Rotates a bitmap, creating a new bitmap.  Beware of memory allocations.
 */
fun Bitmap.rotate(degrees: Int): Bitmap {
    val matrix = Matrix()

    matrix.postRotate(degrees.toFloat())

    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}
