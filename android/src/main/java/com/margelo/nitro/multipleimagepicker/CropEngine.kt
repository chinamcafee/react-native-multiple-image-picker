package com.margelo.nitro.multipleimagepicker


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.config.CustomIntentKey
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.engine.CropFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnMediaEditInterceptListener
import com.luck.picture.lib.utils.DateUtils
import com.margelo.nitro.core.Promise.Companion.resolved
import com.margelo.nitro.multipleimagepicker.ImageLoaderUtils.assertValidRequest
import com.margelo.nitro.multipleimagepicker.editor.dialog.PictureEditorCallback
import com.margelo.nitro.multipleimagepicker.editor.dialog.PictureEditorDialog
import com.margelo.nitro.multipleimagepicker.utils.getBitmapPathFromUri
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCrop.Options
import com.yalantis.ucrop.UCrop.RESULT_ERROR
import com.yalantis.ucrop.UCropImageEngine
import java.io.File

class CropImageEngine : UCropImageEngine {
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (!assertValidRequest(context)) {
            return
        }
        Glide.with(context).load(url).override(180, 180).into(imageView)
    }

    override fun loadImage(
        context: Context,
        url: Uri,
        maxWidth: Int,
        maxHeight: Int,
        call: UCropImageEngine.OnCallbackListener<Bitmap>
    ) {
        Glide.with(context).asBitmap().load(url).override(maxWidth, maxHeight)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap, transition: Transition<in Bitmap?>?
                ) {
                    call.onCall(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    call.onCall(null)
                }
            })
    }
}

class CropEngine(cropOption: Options) : CropFileEngine {
    private val options: Options = cropOption

    override fun onStartCrop(
        fragment: Fragment,
        srcUri: Uri?,
        destinationUri: Uri?,
        dataSource: ArrayList<String?>?,
        requestCode: Int
    ) {
        println("no engine!!!")
    }
}

class MediaEditInterceptListener(private val sandboxPath: String, private val options: UCrop.Options) : OnMediaEditInterceptListener {
    override fun onStartMediaEdit(fragment: Fragment, currentLocalMedia: LocalMedia, requestCode: Int) {
        val activity = fragment.activity
        
        if (activity is AppCompatActivity) {
            val currentEditPath = currentLocalMedia.availablePath
            val picUri = if (PictureMimeType.isContent(currentEditPath)) {
                Uri.parse(currentEditPath)
            } else {
                Uri.fromFile(File(currentEditPath))
            }
            
            // 可以尝试获取实际文件路径
            val realPath = if (picUri.scheme == "content") {
                activity.getBitmapPathFromUri(picUri) ?: currentEditPath
            } else {
                currentEditPath
            }
            
            PictureEditorDialog.newInstance()
                .setBitmapPathOrUri(realPath, picUri)
                .setPictureEditorCallback(object : PictureEditorCallback {
                    override fun onFinish(path: String, uri: Uri) {
                        // 创建Intent并设置结果
                        val intent = Intent()
                        // 将编辑后的路径保存在MediaStore.EXTRA_OUTPUT中
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        
                        // 如果有其他自定义数据需要返回
                        // intent.putExtra(CustomIntentKey.EXTRA_CUSTOM_EXTRA_DATA, 自定义数据)
                        
                        // 更新LocalMedia对象的相关属性
                        currentLocalMedia.isCut = true
                        currentLocalMedia.cutPath = path
                        currentLocalMedia.cropImageWidth = 500 // 根据实际编辑后的尺寸设置
                        currentLocalMedia.cropImageHeight = 500 // 根据实际编辑后的尺寸设置

                        println("===== onFinish ======extra data is set======")
                        // 直接调用fragment的onActivityResult方法，模拟返回结果
                        // 这样可以避免关闭整个Activity
                        fragment.onActivityResult(requestCode, Activity.RESULT_OK, intent)
                        
                        // PictureEditorDialog会自动关闭，不需要额外调用dismiss
                    }
                })
                .show(activity)
        }
    }
}


fun getSandboxPath(context: Context): String {
    val externalFilesDir: File? = context.getExternalFilesDir("")
    val customFile = File(externalFilesDir?.absolutePath, "Sandbox")
    if (!customFile.exists()) {
        customFile.mkdirs()
    }
    return customFile.absolutePath + File.separator
}


