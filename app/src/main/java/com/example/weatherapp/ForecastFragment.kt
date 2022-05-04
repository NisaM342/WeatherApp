package com.example.weatherapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {
    private val args by navArgs<ForecastFragmentArgs>()
    private lateinit var binding: FragmentForecastBinding
    @Inject lateinit var viewModel: ForecastViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForecastBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this) {
                forecast -> bindData(forecast)
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

    private fun bindData(foreCast: Forecast) {
        binding.recyclerView.adapter = MyAdapter(foreCast.list)
        var adapter = MyAdapter(foreCast.list)
        binding.recyclerView.adapter = adapter
        adapter.setOnDayClickListener(object : MyAdapter.OnDayListener {
            override fun onDayClick(index: Int) {
                val action =
                    ForecastFragmentDirections.actionForecastFragmentToForecastDetailsFragment(
                        viewModel.forecast.value!!.list[index]
                    )
                findNavController().navigate(action)
            }

        })
    }

}