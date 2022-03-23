package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject
@AndroidEntryPoint

class ForecastFragment : Fragment() {

    @Inject
    lateinit var viewModel: ForecastViewModel
    private lateinit var binding: FragmentForecastBinding
    private lateinit var myZip : String
    private val args: ForecastFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated( view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForecastBinding.bind(view)

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        myZip = args.zipCode
    }

    override fun onResume() {
        super.onResume()
        viewModel.mutateForecast.observe(this) { forecast ->
            bindData(forecast)
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

    private fun bindData(forecast: Forecast) {
        binding.recyclerView.adapter = MyAdapter(forecast.list)
    }

}