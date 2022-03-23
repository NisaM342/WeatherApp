package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.FragmentCurrentConditionBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionsFragment : Fragment() {
    @Inject
    lateinit var viewModel: CurrentConditionsViewModel
    private lateinit var binding: FragmentCurrentConditionBinding
    private lateinit var myZip: String
    private val args: CurrentConditionsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_condition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCurrentConditionBinding.bind(view)

        myZip = args.zipCode

        binding.forecastBtn.setOnClickListener {navigationToForecast()
        }
    }

    override fun onResume() {

        super.onResume()
        viewModel.currentConditions.observe(this)
        {
                currentConditions ->
            bindData(currentConditions)
        }

        try
        {
            viewModel.loadData(myZip)
        }
        catch (e: HttpException)
        {

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


    private fun navigationToForecast()
    {
        val action = CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(myZip)
        findNavController().navigate(action)
    }
}