package com.example.myapplication

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.example.myapplication.main.AsteroidAdapter

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    if (pictureOfDay?.mediaType == "image") {
        pictureOfDay.url.let {
            Picasso.with(imageView.context).load(pictureOfDay.url).fit()
                .centerCrop()
                .into(imageView)
        }
    }
}
@BindingAdapter("pictureOfDay")
fun bindTodayImage(imageView: ImageView, pictureOfDay: PictureOfDay) {
    val context = imageView.context
    if (pictureOfDay.mediaType == "image") {
        Picasso.with(context).load(pictureOfDay.url).centerCrop().into(imageView)
        imageView.contentDescription = context.getString(
            R.string.nasa_picture_of_day_content_description_format,
            pictureOfDay.title
        )
    } else {
        Picasso.with(context).load(R.drawable.placeholder_picture_of_day).into(imageView)
        imageView.contentDescription = context.getString(
            R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet
        )
    }
}


@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription=imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription=imageView.context.getString(R.string.normal_asteroid_image)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription=imageView.context.getString(R.string.hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription=imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityUnitText")
fun bindTextViewTovelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}



