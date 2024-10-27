package com.noradev.recipeapp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.noradev.recipeapp.R
import com.noradev.recipeapp.data.db.AppDatabase
import com.noradev.recipeapp.data.model.Meal
import com.noradev.recipeapp.data.network.ApiService
import com.noradev.recipeapp.data.repository.MealRepository
import com.noradev.recipeapp.databinding.ActivityMealFieldBinding
import com.noradev.recipeapp.ui.viewmodel.MealViewModel
import com.noradev.recipeapp.ui.viewmodel.MealViewModelFactory

class MealFieldActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMealFieldBinding
    private lateinit var database: AppDatabase
    private lateinit var apiService: ApiService
    private lateinit var mealRepository: MealRepository
    private lateinit var mealViewModel: MealViewModel
    private var mealId: String = ""
    private var mode: String = ""

    private var ingredientCount = 0
    private val maxItems = 20
    private val measureEditTexts = mutableListOf<TextInputEditText>()
    private val ingredientEditTexts = mutableListOf<TextInputEditText>()

    companion object {
        private const val MEAL_ID = "MEAL_ID"
        private const val MODE = "MODE"

        fun newIntent(context: Context, mealId: String, mode: String): Intent {
            val intent = Intent(context, MealFieldActivity::class.java)
            intent.putExtra(MEAL_ID, mealId)
            intent.putExtra(MODE, mode)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealFieldBinding.inflate(layoutInflater)
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
        binding.ivBack.setOnClickListener { finish() }
        binding.fabAdd.setOnClickListener { addIngredientItem() }
    }

    private fun initGetMeal() {
        if (mode == "add") {
            binding.tvHead.text = getString(R.string.add_meal)
            val newMeal = mealViewModel.createEmptyMeal()
            mealViewModel.insertMeal(newMeal).observe(this) { generatedId ->
                mealViewModel.getMealFromId(generatedId.toInt()).observe(this) { meal ->
                    updateFields(meal)
                }
            }
        } else {
            binding.tvHead.text = getString(R.string.edit_meal)
            mealViewModel.getMealFromId(mealId.toInt()).observe(this) { meal ->
                updateFields(meal)
            }
        }
    }

    private fun updateFields(meal: Meal?) {
        if (meal != null) {
            binding.tvStrMeal.setText(meal.strMeal)
            binding.tvStrInstructions.setText(meal.strInstructions)
            binding.tvStrMealThumb.setText(meal.strMealThumb)

            val measures = mutableListOf(
                meal.strMeasure1, meal.strMeasure2, meal.strMeasure3,
                meal.strMeasure4, meal.strMeasure5, meal.strMeasure6,
                meal.strMeasure7, meal.strMeasure8, meal.strMeasure9,
                meal.strMeasure10, meal.strMeasure11, meal.strMeasure12,
                meal.strMeasure13, meal.strMeasure14, meal.strMeasure15,
                meal.strMeasure16, meal.strMeasure17, meal.strMeasure18,
                meal.strMeasure19, meal.strMeasure20
            )

            val ingredients = mutableListOf(
                meal.strIngredient1, meal.strIngredient2, meal.strIngredient3,
                meal.strIngredient4, meal.strIngredient5, meal.strIngredient6,
                meal.strIngredient7, meal.strIngredient8, meal.strIngredient9,
                meal.strIngredient10, meal.strIngredient11, meal.strIngredient12,
                meal.strIngredient13, meal.strIngredient14, meal.strIngredient15,
                meal.strIngredient16, meal.strIngredient17, meal.strIngredient18,
                meal.strIngredient19, meal.strIngredient20
            )

            measures.forEachIndexed { index, measure ->
                if (measure?.isNotBlank() == true) {
                    addIngredientItem()
                    measureEditTexts[index].setText(measure)
                    ingredientEditTexts[index].setText(ingredients[index])
                }
            }

            binding.fabSave.setOnClickListener {
                initSave(meal, measures, ingredients)
            }
        }
    }

    private fun addIngredientItem() {
        if (ingredientCount > 0) {
            val lastMeasureEditText = measureEditTexts.lastOrNull()
            val lastIngredientEditText = ingredientEditTexts.lastOrNull()

            if (lastMeasureEditText?.text.isNullOrEmpty() || lastIngredientEditText?.text.isNullOrEmpty()) {
                return
            }
        }

        if (ingredientCount >= maxItems) {
            return
        }

        val itemLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            isBaselineAligned = false
        }

        val measureInputLayout = TextInputLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            ).apply { marginEnd = resources.getDimensionPixelSize(R.dimen.dp_8) }
            hint = "Measurement ${ingredientCount + 1}"
            endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        }
        val measureEditText = TextInputEditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            inputType = InputType.TYPE_CLASS_TEXT
            imeOptions = EditorInfo.IME_ACTION_NEXT
        }
        measureInputLayout.addView(measureEditText)

        val ingredientInputLayout = TextInputLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            )
            hint = "Ingredient ${ingredientCount + 1}"
            endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        }
        val ingredientEditText = TextInputEditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            inputType = InputType.TYPE_CLASS_TEXT
            imeOptions = EditorInfo.IME_ACTION_NEXT
        }
        ingredientInputLayout.addView(ingredientEditText)

        itemLayout.addView(measureInputLayout)
        itemLayout.addView(ingredientInputLayout)
        binding.llContainer.addView(itemLayout)

        measureEditTexts.add(measureEditText)
        ingredientEditTexts.add(ingredientEditText)

        ingredientCount++

        if (ingredientCount >= maxItems) {
            binding.fabAdd.visibility = View.GONE
        }
    }

    private fun initSave(meal: Meal, measures: MutableList<String?>, ingredients: MutableList<String?>) {
        meal.strMeal = binding.tvStrMeal.text.toString()
        meal.strInstructions = binding.tvStrInstructions.text.toString()
        meal.strMealThumb = binding.tvStrMealThumb.text.toString()

        measureEditTexts.forEachIndexed { index, measureEditText ->
            val measureText = measureEditText.text.toString()
            val ingredientText = ingredientEditTexts[index].text.toString()

            measures[index] = measureText
            ingredients[index] = ingredientText
        }

        for (i in measures.indices) {
            when (i) {
                0 -> meal.strMeasure1 = measures[i]
                1 -> meal.strMeasure2 = measures[i]
                2 -> meal.strMeasure3 = measures[i]
                3 -> meal.strMeasure4 = measures[i]
                4 -> meal.strMeasure5 = measures[i]
                5 -> meal.strMeasure6 = measures[i]
                6 -> meal.strMeasure7 = measures[i]
                7 -> meal.strMeasure8 = measures[i]
                8 -> meal.strMeasure9 = measures[i]
                9 -> meal.strMeasure10 = measures[i]
                10 -> meal.strMeasure11 = measures[i]
                11 -> meal.strMeasure12 = measures[i]
                12 -> meal.strMeasure13 = measures[i]
                13 -> meal.strMeasure14 = measures[i]
                14 -> meal.strMeasure15 = measures[i]
                15 -> meal.strMeasure16 = measures[i]
                16 -> meal.strMeasure17 = measures[i]
                17 -> meal.strMeasure18 = measures[i]
                18 -> meal.strMeasure19 = measures[i]
                19 -> meal.strMeasure20 = measures[i]
            }

            when (i) {
                0 -> meal.strIngredient1 = ingredients[i]
                1 -> meal.strIngredient2 = ingredients[i]
                2 -> meal.strIngredient3 = ingredients[i]
                3 -> meal.strIngredient4 = ingredients[i]
                4 -> meal.strIngredient5 = ingredients[i]
                5 -> meal.strIngredient6 = ingredients[i]
                6 -> meal.strIngredient7 = ingredients[i]
                7 -> meal.strIngredient8 = ingredients[i]
                8 -> meal.strIngredient9 = ingredients[i]
                9 -> meal.strIngredient10 = ingredients[i]
                10 -> meal.strIngredient11 = ingredients[i]
                11 -> meal.strIngredient12 = ingredients[i]
                12 -> meal.strIngredient13 = ingredients[i]
                13 -> meal.strIngredient14 = ingredients[i]
                14 -> meal.strIngredient15 = ingredients[i]
                15 -> meal.strIngredient16 = ingredients[i]
                16 -> meal.strIngredient17 = ingredients[i]
                17 -> meal.strIngredient18 = ingredients[i]
                18 -> meal.strIngredient19 = ingredients[i]
                19 -> meal.strIngredient20 = ingredients[i]
            }
        }

        mealViewModel.insertMeal(meal)
        finish()
    }
}