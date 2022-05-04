package com.example.weatherapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastDetailsFragment : Fragment() {
    private val args by navArgs<ForecastDetailsFragmentArgs>()

    @SuppressLint("UnrememberedMutableState")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setContent {
                Column(horizontalAlignment = CenterHorizontally, verticalArrangement = Center) {
                    val weather = args.dayForecast.weather.firstOrNull()?.icon
                    val url = "https://openweathermap.org/img/wn/${weather}@2x.png"
                    weather.let {
                        val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)
                        Glide.with(LocalContext.current)
                            .asBitmap()
                            .load(url)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    bitmapState.value = resource
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {

                                }

                            })
                        val image = bitmapState.value
                        image?.let { img ->
                            Image(
                                bitmap = img.asImageBitmap(),
                                contentDescription = "weather icon",
                                modifier = Modifier
                                    .requiredWidth(67.dp)
                                    .requiredHeight(67.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Text(text = "Day: " + args.dayForecast.temp.day.toInt().toString() + "°")
                    Text(text = "High: " + args.dayForecast.temp.max.toInt().toString() + "°")
                    Text(text = "Low: " + args.dayForecast.temp.min.toInt().toString() + "°")
                    Text(text = "Humidity: " + args.dayForecast.humidity.toString() + "%")
                    Text(text = "Pressure: " + args.dayForecast.pressure.toInt().toString() + "hPa")
                    Text(text = "Wind Speed: " + args.dayForecast.speed.toString() + "mph")
                    Text(text = "" + args.dayForecast.weather.firstOrNull()?.description)
                }
            }
        }
    }

}