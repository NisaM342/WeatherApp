package com.example.weatherapp

import android.os.Bundle
import android.view.View
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
    @Inject lateinit var viewModel: CurrentConditionsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrentConditionBinding.bind(view)
        binding.forecastBtn.setOnClickListener {
            val action= CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(args.zipCode, args.latitude, args.longitude)
            findNavController().navigate(action)
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.currentConditions.observe(this) { currentConditions ->
            bindData(currentConditions)
        }
        try {
            if(args.zipCode.length == 5 && args.zipCode.all { it.isDigit() }) {
                viewModel.loadData(args.zipCode)
            } else {
                viewModel.loadData(args.latitude, args.longitude)
            }
        } catch (exception: HttpException) {
            ErrorDialogFragment().show(childFragmentManager, "")
        }



    }

    private fun bindData(currentConditions: CurrentConditions) {
        binding.cityName.text = currentConditions.name
        binding.temperature.text = getString(R.string.temperature, currentConditions.main.temp.toInt())
        binding.feelLike.text = getString(R.string.feelLike, currentConditions.main.feelsLike.toInt())
        binding.lowTemp.text = getString(R.string.lowTemp, currentConditions.main.tempMin.toInt())
        binding.highTemp.text = getString(R.string.highTemp, currentConditions.main.tempMax.toInt())
        binding.humidity.text = getString(R.string.humidity, currentConditions.main.humidity.toInt())
        binding.pressure.text = getString(R.string.pressure, currentConditions.main.pressure.toInt())

        val weather = currentConditions.weather.firstOrNull()
        weather?.let {
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${it.icon}@2x.png")
                .into(binding.weatherIcon)
        }

    }
}