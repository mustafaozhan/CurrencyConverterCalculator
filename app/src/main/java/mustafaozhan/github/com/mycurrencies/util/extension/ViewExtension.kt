/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.util.extension

import android.content.Context
import android.widget.ImageView
import mustafaozhan.github.com.mycurrencies.R
import timber.log.Timber
import java.io.FileNotFoundException
import java.util.Locale

fun ImageView.setBackgroundByName(name: String) =
    setImageResource(context.getImageResourceByName(name))

fun Context.getImageResourceByName(name: String): Int = try {
    resources.getIdentifier(
        name.toLowerCase(Locale.getDefault()).replace("try", "tryy"),
        "drawable",
        packageName
    )
} catch (e: FileNotFoundException) {
    Timber.w(e)
    R.drawable.transparent
}