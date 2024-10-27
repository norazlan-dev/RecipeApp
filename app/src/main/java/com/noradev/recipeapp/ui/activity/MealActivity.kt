package com.noradev.recipeapp.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.noradev.recipeapp.data.db.AppDatabase
import com.noradev.recipeapp.data.network.ApiService
import com.noradev.recipeapp.data.repository.MealRepository
import com.noradev.recipeapp.databinding.ActivityMealBinding
import com.noradev.recipeapp.ui.utils.loadImageFromUrl
import com.noradev.recipeapp.ui.viewmodel.MealViewModel
import com.noradev.recipeapp.ui.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMealBinding
    private lateinit var database: AppDatabase
    private lateinit var apiService: ApiService
    private lateinit var mealRepository: MealRepository
    private lateinit var mealViewModel: MealViewModel
    private var isSubmenuOpen = false
    private var mealId: String = ""
    private var mode: String = ""

    companion object {
        private const val MEAL_ID = "MEAL_ID"
        private const val MODE = "MODE"

        fun newIntent(context: Context, mealId: String, mode: String): Intent {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra(MEAL_ID, mealId)
            intent.putExtra(MODE, mode)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        mealId = intent.getStringExtra(MEAL_ID).toString()
        mode = intent.getStringExtra(MODE).toString()
        setContentView(binding.root)

        initNonView()
        initView()
    }

    private fun initNonView() {
        apiService = ApiService.create()
        database = AppDatabase.getInstance(applicationContext)
        mealRepository = MealRepository(database.mealDao(), apiService)
        mealViewModel = ViewModelProvider(this, MealViewModelFactory(mealRepository))[MealViewModel::class.java]
    }

    private fun initView() {
        initGetMeal()

        binding.fabMenu.setOnClickListener {
            toggleSubmenu()
        }

        binding.svHead.viewTreeObserver.addOnScrollChangedListener {
            checkImageViewVisibility()
        }
    }

    private fun toggleSubmenu() {
        val spacing = 8 * resources.displayMetrics.density

        if (isSubmenuOpen) {
            binding.fabDelete.animate()
                .translationY(0f)
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    binding.fabDelete.visibility = View.GONE
                }

            binding.fabEdit.animate()
                .translationY(0f)
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    binding.fabEdit.visibility = View.GONE
                }
        } else {
            binding.fabDelete.apply {
                visibility = View.VISIBLE
                alpha = 0f
                translationY = 0f
                animate()
                    .translationY(-150f - spacing)
                    .alpha(1f)
                    .setDuration(200)
                    .start()
            }

            binding.fabEdit.apply {
                visibility = View.VISIBLE
                alpha = 0f
                translationY = 0f
                animate()
                    .translationY(-300f - 2 * spacing)
                    .alpha(1f)
                    .setDuration(200)
                    .start()
            }
        }
        isSubmenuOpen = !isSubmenuOpen
    }

    private fun initGetMeal() {
        mealViewModel.getMealFromId(mealId.toInt()).observe(this) { meal ->
            if (meal != null) {
                meal.strMealThumb?.let { binding.ivMeal.loadImageFromUrl(it) }
                binding.tvHead.text = meal.strMeal
                binding.tvHiddenHead.text = meal.strMeal
                binding.tvInstructions.text = meal.strInstructions

                val ingredients = arrayOf(
                    meal.strIngredient1,
                    meal.strIngredient2,
                    meal.strIngredient3,
                    meal.strIngredient4,
                    meal.strIngredient5,
                    meal.strIngredient6,
                    meal.strIngredient7,
                    meal.strIngredient8,
                    meal.strIngredient9,
                    meal.strIngredient10,
                    meal.strIngredient11,
                    meal.strIngredient12,
                    meal.strIngredient13,
                    meal.strIngredient14,
                    meal.strIngredient15,
                    meal.strIngredient16,
                    meal.strIngredient17,
                    meal.strIngredient18,
                    meal.strIngredient19,
                    meal.strIngredient20
                )

                val measurements = arrayOf(
                    meal.strMeasure1,
                    meal.strMeasure2,
                    meal.strMeasure3,
                    meal.strMeasure4,
                    meal.strMeasure5,
                    meal.strMeasure6,
                    meal.strMeasure7,
                    meal.strMeasure8,
                    meal.strMeasure9,
                    meal.strMeasure10,
                    meal.strMeasure11,
                    meal.strMeasure12,
                    meal.strMeasure13,
                    meal.strMeasure14,
                    meal.strMeasure15,
                    meal.strMeasure16,
                    meal.strMeasure17,
                    meal.strMeasure18,
                    meal.strMeasure19,
                    meal.strMeasure20
                )

                val result = StringBuilder()
                for (i in ingredients.indices) {
                    if (ingredients[i]?.isNotEmpty() == true) {
                        val measurementWithSpace = if (measurements[i]?.endsWith(" ") == true) {
                            measurements[i]
                        } else {
                            "${measurements[i]} "
                        }
                        result.append("$measurementWithSpace${ingredients[i]}\n")
                    }
                }

                binding.tvIngredients.text = result

                binding.ivVideo.setOnClickListener {
                    meal.strYoutube?.let { it1 -> openLink(it1) }
                }

                if (meal.strYoutube.isNullOrBlank()) {
                    binding.ivVideo.visibility = View.INVISIBLE
                }

                binding.ivSite.setOnClickListener {
                    meal.strSource?.let { it1 -> openLink(it1) }
                }

                if (meal.strSource.isNullOrBlank()) {
                    binding.ivSite.visibility = View.INVISIBLE
                }

                binding.fabDelete.setOnClickListener {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(title)
                        .setMessage("Delete This Recipe?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            finish()
                            mealViewModel.deleteMeal(meal)
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                    builder.create().show()
                }

                binding.fabEdit.setOnClickListener {
                    startActivity(
                        MealFieldActivity.newIntent(
                            this@MealActivity,
                            meal.idMeal.toString(),
                            "edit"
                        )
                    )
                }
            }
        }
    }

    private fun checkImageViewVisibility() {
        val location = IntArray(2)
        binding.ivMeal.getLocationOnScreen(location)

        val scrollViewLocation = IntArray(2)
        binding.main.getLocationOnScreen(scrollViewLocation)

        val scrollViewBottom = scrollViewLocation[1] + binding.main.height
        val isCompletelyHidden = location[1] + binding.ivMeal.height <= scrollViewLocation[1] || location[1] >= scrollViewBottom

        binding.tvHiddenHead.visibility = if (isCompletelyHidden) View.VISIBLE else View.GONE
    }

    private fun openLink(url: String) {
        if (url.isNotBlank()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Unavailable", Toast.LENGTH_SHORT).show()
        }
    }
}