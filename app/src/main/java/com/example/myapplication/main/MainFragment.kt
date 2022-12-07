package com.example.myapplication.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var asteroidAdapter: AsteroidAdapter

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        )[MainViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

       val  binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        asteroidAdapter = AsteroidAdapter(OnClickItemListener { asteroid ->
            findNavController().navigate(
                MainFragmentDirections
                    .actionShowDetail(asteroid)
            )
        })
        val manager = LinearLayoutManager(activity)
        with(binding.asteroidRecycler) {
            adapter = asteroidAdapter
            layoutManager = manager
        }
        setHasOptionsMenu(true)

        viewModel.savedAsteroids.observe(viewLifecycleOwner) { asteroids ->
            if (asteroids.isNotEmpty()) {
                asteroidAdapter.submitList(asteroids)

                binding.statusLoadingWheel.visibility = View.GONE
                viewModel.getdatafromDatabase = true

            }
        }

        viewModel.refreshedData.observe(viewLifecycleOwner) { success ->
    if(success||viewModel.getdatafromDatabase)
            binding.statusLoadingWheel.visibility = View.GONE

        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_today_menu -> {
                viewModel.todayAsteroids.observe(viewLifecycleOwner) { asteroids ->
                    if (asteroids.isNotEmpty()) {
                        asteroidAdapter.submitList(asteroids)
                    }
                }
            }
            R.id.show_saved_menu -> {
                viewModel.savedAsteroids.observe(viewLifecycleOwner) { asteroids ->
                    if (asteroids.isNotEmpty()) {
                        asteroidAdapter.submitList(asteroids)
                    }
                }
            }
            R.id.show_all_menu -> {
                viewModel.WeekAsteroids.observe(viewLifecycleOwner) { asteroids ->
                    if (asteroids.isNotEmpty()) {
                        asteroidAdapter.submitList(asteroids)
                    }
                }
            }
        }
        return true
    }

}
