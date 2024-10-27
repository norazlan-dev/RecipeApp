package com.noradev.recipeapp.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.noradev.recipeapp.R
import com.noradev.recipeapp.data.db.AppDatabase
import com.noradev.recipeapp.data.model.Meal
import com.noradev.recipeapp.data.network.ApiService
import com.noradev.recipeapp.data.repository.MealRepository
import com.noradev.recipeapp.databinding.ActivityMainBinding
import com.noradev.recipeapp.ui.adapter.MealAdapter
import com.noradev.recipeapp.ui.decoration.GridSpacingItemDecoration
import com.noradev.recipeapp.ui.viewmodel.MealViewModel
import com.noradev.recipeapp.ui.viewmodel.MealViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var database: AppDatabase
    private lateinit var apiService: ApiService
    private lateinit var mealRepository: MealRepository
    private lateinit var mealViewModel: MealViewModel
    private lateinit var mealAdapter : MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNonView()
        initView()
    }

    private fun initNonView() {
        apiService = ApiService.create()
        database = AppDatabase.getInstance(applicationContext)
        mealRepository = MealRepository(database.mealDao(), apiService)
        mealViewModel = ViewModelProvider(this, MealViewModelFactory(mealRepository))[MealViewModel::class.java]

        mealViewModel.allMeals.observe(this) { meals ->
            if (meals != null) {
                mealAdapter.setMeals(meals.sortedByDescending { it.dateCreated })
            }
        }
    }

    private fun initView() {
        binding.btnGenerate.setOnClickListener {
            if (isOnline(this)) {
                mealViewModel.getRandomMealFromServer()
            } else {
                Toast.makeText(this, "Internet Unavailable", Toast.LENGTH_SHORT).show()
            }
        }

        binding.refresh.setOnRefreshListener {
            initNonView()
            binding.refresh.isRefreshing = false
        }

        binding.fabAdd.setOnClickListener {
            startActivity(
                MealFieldActivity.newIntent(
                    this@MainActivity,
                    "0",
                    "add"
                )
            )
        }

        initList()
    }

    private fun initList() {
        binding.rvList.layoutManager = GridLayoutManager(
            applicationContext,
            2
        )

        mealAdapter = MealAdapter()
        binding.rvList.adapter = mealAdapter

        mealAdapter.setOnItemClickListener(object : MealAdapter.OnItemsClickListener {
            override fun onItemClick(mealData: Meal) {
                startActivity(
                    MealActivity.newIntent(
                        this@MainActivity,
                        mealData.idMeal.toString(),
                        "view"
                    )
                )
            }
        })

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                mealAdapter.filter.filter(query)
                return false
            }
        })

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp_16)
        binding.rvList.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels))
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
}