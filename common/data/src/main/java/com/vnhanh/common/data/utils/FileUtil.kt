package com.vnhanh.common.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import com.vnhanh.common.data.extensions.closeSafe
import com.vnhanh.common.data.log.printDebugStackTrace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object FileUtil {
    suspend fun writeByteArrayToFile(context: Context, byteArray: ByteArray, fileName: String) : String? =
        withContext(Dispatchers.IO) {
            var fos: FileOutputStream? = null
            var path: String? = null
            try {
                val folder = getTopLevelSharedDir(context)
                val file = File(folder, fileName)

                if (file.exists()) {
                    withRetry(delayInMilliSecondsIfFailed = 300) {
                        file.delete()
                    }
                }
                file.createNewFile()

                fos = FileOutputStream(file)
                fos.write(byteArray)
                fos.close()

                path = file.path
            } catch (e: Exception) {
                e.printDebugStackTrace()
            } finally {
                fos.closeSafe()
            }

            return@withContext path
        }

    suspend fun writeStreamToFile(
        context: Context,
        inputStream: InputStream,
        fileName: String
    ) : String? =
        withContext(Dispatchers.IO) {
            var path: String? = null
            var fos: FileOutputStream? = null

            try {
                val folder = getTopLevelSharedDir(context)
                val outputFile = File(folder, fileName)

                if (outputFile.exists()) {
                    withRetry(delayInMilliSecondsIfFailed = 300) {
                        outputFile.delete()
                    }
                }

                outputFile.createNewFile()

                val buffer = ByteArray(1024)
                inputStream.read(buffer)
                inputStream.close()

                fos = FileOutputStream(outputFile).apply {
                    write(buffer)
                    close()
                }

                path = outputFile.path
            } catch (e: Exception) {
                e.printDebugStackTrace()
            } finally {
                inputStream.closeSafe()
                fos.closeSafe()
            }

            return@withContext path
        }

    /**
     * @return: result of deletion.
     */
    suspend fun String?.deleteFilePathSafe(retryNumber: Int = 2, delayIfFailedInMilliSeconds: Long = 300L) : Boolean {
        this ?: return false
        return try {
            val file = File(this)

            withRetry(
                retryNumber = retryNumber,
                delayInMilliSecondsIfFailed = delayIfFailedInMilliSeconds,
            ) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printDebugStackTrace()
            false
        }
    }

    /**
     * Get Folder or create if not exist and it should be available to write file
     */
    private fun getTopLevelSharedDir(context: Context) : File? {
        var dir: File? = null
        try {
            dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val topLevelSharedDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
                File(topLevelSharedDir)
            } else {
                File(Environment.getExternalStorageDirectory().toString())
            }

            // create dir if not exists
            dir = mkdirIfNotExist(dir)

            // try again if above try failed
            if (dir == null) {
                dir = context.filesDir
                dir = mkdirIfNotExist(dir)
            }
        } catch (e: Exception) {
            e.printDebugStackTrace()
        }

        return dir
    }

    private fun mkdirIfNotExist(dir: File?) : File? {
        dir ?: return null
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            val success = dir.mkdirs()
            if (!success) {
                return null
            }
        }

        return dir
    }

    /**
     * @param bitmap The Bitmap you want to save.
     * @param format Bitmap.CompressFormat can be PNG,JPEG or WEBP.
     * @param quality quality goes from 1 to 100. (Percentage).
     * @return path of output file if the Bitmap was saved successfully, null otherwise.
     */
    suspend fun saveBitmapToFile(
        context: Context,
        dir: File? = null,
        fileName: String,
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 100,
    ): String? = withContext(Dispatchers.IO) {
        val folder: File? = dir ?: run {
            getTopLevelSharedDir(context)
        }

        var fos: FileOutputStream? = null
        var outputFilePath: String? = null

        try {
            val imageFile = File(folder, fileName)
            if (imageFile.exists()) {
                withRetry(delayInMilliSecondsIfFailed = 300) {
                    imageFile.delete()
                }

                imageFile.createNewFile()
            }

            fos = FileOutputStream(imageFile)
            bitmap.compress(format, quality, fos)
            outputFilePath = imageFile.path
        } catch (e: IOException) {
            e.printDebugStackTrace()
        } finally {
            fos.closeSafe()
        }

        return@withContext outputFilePath
    }

    // Convert picture drawable to file, open it if you need
    // Caution: I did not test, please check before used
//    fun drawableToBitmap(pd: PictureDrawable): Bitmap {
//        val bm = Bitmap.createBitmap(pd.intrinsicWidth, pd.intrinsicHeight, Bitmap.Config.ARGB_8888)
//        val canvas: Canvas = Canvas(bm)
//        canvas.drawPicture(pd.picture)
//
//        return bm
//    }

}
