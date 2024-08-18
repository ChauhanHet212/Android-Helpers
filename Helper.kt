package com.example.helper.extras

import android.os.Build
import android.webkit.MimeTypeMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


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


//-------------File----------------
/**
 * Get mime type of file
 *
 * @param path -> path of file
 * @return application/'fileType' like **application/pdf**
 */
fun getMimeType(path: String): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(path)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

/**
 * Get mime type of file
 *
 * @param file -> file
 * @return application/'fileType' like **application/pdf**
 */
fun getMimeType(file: File): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

/**
 * Create zip of files
 *
 * @param files -> list of file paths to make a zip
 * @param file ->  where it is stored
 * @return file/folder path on successful process
 */
fun createZip(files: List<String>, file: File, bufferSize: Int = 2048): String? {
    try {
        var origin: BufferedInputStream
        val dest = FileOutputStream(file)
        val out = ZipOutputStream(
            BufferedOutputStream(
                dest)
        )

        val data = ByteArray(bufferSize)
        for (i in files.indices) {
            val fi = FileInputStream(files[i])
            origin = BufferedInputStream(fi, bufferSize)

            val entry = ZipEntry(files[i].substring(
                files[i].lastIndexOf("/") + 1))
            out.putNextEntry(entry)
            val count: Int = origin.read(data, 0, bufferSize)

            while (count != -1) {
                out.write(data, 0, count)
            }
            origin.close()
        }

        out.close()
        return file.toString()
    } catch (ignored: Exception) {
        ignored.printStackTrace()
    }

    return null
}
