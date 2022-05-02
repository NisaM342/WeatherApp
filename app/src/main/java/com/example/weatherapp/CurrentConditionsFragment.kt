package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.FragmentCurrentConditionBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionsFragment : Fragment(R.layout.fragment_current_condition) {
    private val args by navArgs<CurrentConditionsFragmentArgs>()
    private lateinit var binding: FragmentCurrentConditionBinding

    @Inject
    lateinit var viewModel: CurrentConditionsViewModel
    private lateinit var weatherService: NotificationService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherService = NotificationService()
        binding = FragmentCurrentConditionBinding.bind(view)
        binding.forecastBtn.setOnClickListener {
            val action =
                CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(
                    args.zipCode,
                    args.latitude,
                    args.longitude
                )
            findNavController().navigate(action)
        }
        requireActivity().registerReceiver(
            updateNotification,
            IntentFilter(Constants.TIMER_UPDATED)
        )
    }


    override fun onResume() {
        super.onResume()
        viewModel.currentConditions.observe(this) { currentConditions ->
            bindData(currentConditions)
        }
        try {
            if (args.zipCode.length == 5 && args.zipCode.all { it.isDigit() }) {
                viewModel.loadData(args.zipCode)
            } else {
                viewModel.loadData(args.latitude, args.longitude)
                sendNotification()
            }
        } catch (exception: HttpException) {
            if (exception.code() == 404)
                ErrorDialogFragment().show(childFragmentManager, "")
        }


    }

    private fun bindData(currentConditions: CurrentConditions) {
        binding.cityName.text = currentConditions.name
        binding.temperature.text =
            getString(R.string.temperature, currentConditions.main.temp.toInt())
        binding.feelLike.text =
            getString(R.string.feelLike, currentConditions.main.feelsLike.toInt())
        binding.lowTemp.text = getString(R.string.lowTemp, currentConditions.main.tempMin.toInt())
        binding.highTemp.text = getString(R.string.highTemp, currentConditions.main.tempMax.toInt())
        binding.humidity.text =
            getString(R.string.humidity, currentConditions.main.humidity.toInt())
        binding.pressure.text =
            getString(R.string.pressure, currentConditions.main.pressure.toInt())

        val weather = currentConditions.weather.firstOrNull()
        weather?.let {
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${it.icon}@2x.png")
                .into(binding.weatherIcon)
        }

    }


    private fun sendNotification() {
        val builder = NotificationCompat.Builder(
            requireContext(),
            Constants.NOTIFICATION_CHANNEL_ID
        )
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.sun)
            .setContentTitle(
                getString(
                    R.string.notify_location,
                    viewModel.currentConditions.value?.name
                )
            )
            .setContentText(
                getString(
                    R.string.notify_currentTemp,
                    viewModel.currentConditions.value?.main?.temp?.toInt()
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(requireContext())) {
            notify(1, builder.build())
        }
    }

    private val updateNotification: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val builder = NotificationCompat.Builder(
                requireContext(),
                Constants.NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(
                    getString(
                        R.string.notify_location,
                        viewModel.currentConditions.value?.name
                    )
                )
                .setContentText(
                    getString(
                        R.string.notify_currentTemp,
                        viewModel.currentConditions.value?.main?.temp?.toInt()
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getMainActivityPendingIntent())
            with(NotificationManagerCompat.from(requireContext())) {
                notify(1, builder.build())
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        requireContext(), 0, Intent(requireContext(), MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT
        }, PendingIntent.FLAG_UPDATE_CURRENT
    )


}