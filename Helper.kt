package com.example.helper.extras

import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//-------------Coroutine----------------
/**
 * This is the Main thread coroutine scope to use anywhere
 * @param block -> code block to run on this thread
 */
fun mainThread(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main).launch { block() }
}

/**
 * This is the IO thread coroutine scope to use anywhere
 * @param block -> code block to run on this thread
 */
fun ioThread(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO).launch { block() }
}

/**
 * This is the Default thread coroutine scope to use anywhere
 * @param block -> code block to run on this thread
 */
fun defaultThread(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Default).launch { block() }
}

/**
 * This is the Unconfined thread coroutine scope to use anywhere
 * @param block -> code block to run on this thread
 */
fun unconfinedThread(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Unconfined).launch { block() }
}


/**
 * This is the Main thread coroutine scope to use inside other coroutine scope
 * @param block -> code block to run on this thread
 */
suspend fun innerMainThread(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.Main) { block() }
}

/**
 * This is the IO thread coroutine scope to use inside other coroutine scope
 * @param block -> code block to run on this thread
 */
suspend fun innerIOThread(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.IO) { block() }
}

/**
 * This is the Default thread coroutine scope to use inside other coroutine scope
 * @param block -> code block to run on this thread
 */
suspend fun innerDefaultThread(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.Default) { block() }
}

/**
 * This is the Unconfined thread coroutine scope to use inside other coroutine scope
 * @param block -> code block to run on this thread
 */
suspend fun innerUnconfinedThread(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.Unconfined) { block() }
}



//-------------Api levels(android versions)----------------
/**
 * To run block of code on above given api level
 * @param apiLevel -> the api level to check
 * @param block -> block of code to run if api level is above given api level
 * @param elseBlock -> block of code to run if api level is not above given api level
 */
fun aboveApi(apiLevel: Int, elseBlock: () -> Unit = {}, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > apiLevel) {
        block()
    } else {
        elseBlock()
    }
}

/**
 * To run block of code on above or given api level
 * @param apiLevel -> the api level to check
 * @param block -> block of code to run if api level is above or same as given api level
 * @param elseBlock -> block of code to run if api level is not above or not same as given api level
 */
fun onOrAboveApi(apiLevel: Int, elseBlock: () -> Unit = {}, block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= apiLevel) {
        block()
    } else {
        elseBlock()
    }
}

/**
 * To run block of code on below given api level
 * @param apiLevel -> the api level to check
 * @param block -> block of code to run if api level is below given api level
 * @param elseBlock -> block of code to run if api level is not below given api level
 */
fun belowApi(apiLevel: Int, elseBlock: () -> Unit = {}, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < apiLevel) {
        block()
    } else {
        elseBlock()
    }
}

/**
 * To run block of code on below or given api level
 * @param apiLevel -> the api level to check
 * @param block -> block of code to run if api level is below or same as given api level
 * @param elseBlock -> block of code to run if api level is not below or not same as given api level
 */
fun onOrBelowApi(apiLevel: Int, elseBlock: () -> Unit = {}, block: () -> Unit) {
    if (Build.VERSION.SDK_INT <= apiLevel) {
        block()
    } else {
        elseBlock()
    }
}

/**
 * To run block of code on given api level
 * @param apiLevel -> the api level to check
 * @param block -> block of code to run if api level is same as given api level
 * @param elseBlock -> block of code to run if api level is not same as given api level
 */
fun onApi(apiLevel: Int, elseBlock: () -> Unit = {}, block: () -> Unit) {
    if (Build.VERSION.SDK_INT == apiLevel) {
        block()
    } else {
        elseBlock()
    }
}

/**
 * To run block of code on given api levels
 * @param apiLevels -> the api levels to check
 * @param block -> block of code to run if api level is in given api levels
 * @param elseBlock -> block of code to run if api level is not in given api levels
 */
fun onApis(vararg apiLevels: Int, elseBlock: () -> Unit = {}, block: () -> Unit) {
    if (apiLevels.contains(Build.VERSION.SDK_INT)) {
        block()
    } else {
        elseBlock()
    }
}

/**
 * To run block of code of a particular api levels
 * @param levelsAndBlocks -> pair of api level and block of code to run on that api level
 * @param elseBlock -> if api level is not in given api levels
 */
fun whenApiLevel(vararg levelsAndBlocks: Pair<Int, () -> Unit>, elseBlock: () -> Unit = {}) {
    if (levelsAndBlocks.map { it.first }.contains(Build.VERSION.SDK_INT)) {
        levelsAndBlocks.find { it.first == Build.VERSION.SDK_INT }?.second?.invoke()
    } else {
        elseBlock()
    }
}
