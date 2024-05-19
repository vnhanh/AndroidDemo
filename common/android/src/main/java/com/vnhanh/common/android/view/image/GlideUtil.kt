package com.vnhanh.common.android.view.image

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


object GlideUtil {
    /**
     * @param url: might be String? or GlideUrl?
     */
    fun load(
        url: Any?,
        target: ImageView?,
        isCrossFade: Boolean = true,
        isCenterCrop: Boolean = true,
        @DrawableRes placeholderResId: Int?,
        @DrawableRes errorResId: Int? = placeholderResId,
        onLoadComplete: ((isSuccess: Boolean) -> Unit)? = null,
    ) {
        val context = target?.context ?: return

        var builder: RequestBuilder<Drawable> = Glide.with(context)
            .load(url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadComplete?.invoke(false)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadComplete?.invoke(true)

                    return false
                }
            })
        if (placeholderResId != null) {
            builder = builder.placeholder(placeholderResId)
        }
        if (errorResId != null) {
            builder = builder.error(errorResId)
        }
        if (isCenterCrop) builder = builder.centerCrop()
        if (isCrossFade) builder = builder.transition(withCrossFade())

        builder.into(target)
    }
}
