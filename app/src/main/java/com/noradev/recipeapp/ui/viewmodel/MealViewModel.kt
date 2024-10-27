package com.noradev.recipeapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.noradev.recipeapp.data.model.Meal
import com.noradev.recipeapp.data.repository.MealRepository
import kotlinx.coroutines.launch

class MealViewModel(private val repository: MealRepository) : ViewModel() {
    val allMeals: LiveData<List<Meal>> = repository.getMeals()

    fun getRandomMealFromServer() {
        viewModelScope.launch {
            repository.getRandomMeal()
        }
    }

    fun getMealFromId(mealId: Int): LiveData<Meal> {
        return repository.getMealFromId(mealId)
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
        }
    }

    fun insertMeal(meal: Meal): LiveData<Long> {
        val result = MutableLiveData<Long>()
        viewModelScope.launch {
            val id = repository.insertMeal(meal)
            result.postValue(id)
        }
        return result
    }

    fun createEmptyMeal(): Meal {
        return Meal(
            idMeal = 0,
            strMeal = null,
            strDrinkAlternate = null,
            strCategory = null,
            strArea = null,
            strInstructions = null,
            strMealThumb = null,
            strTags = null,
            strYoutube = null,
            strIngredient1 = null,
            strIngredient2 = null,
            strIngredient3 = null,
            strIngredient4 = null,
            strIngredient5 = null,
            strIngredient6 = null,
            strIngredient7 = null,
            strIngredient8 = null,
            strIngredient9 = null,
            strIngredient10 = null,
            strIngredient11 = null,
            strIngredient12 = null,
            strIngredient13 = null,
            strIngredient14 = null,
            strIngredient15 = null,
            strIngredient16 = null,
            strIngredient17 = null,
            strIngredient18 = null,
            strIngredient19 = null,
            strIngredient20 = null,
            strMeasure1 = null,
            strMeasure2 = null,
            strMeasure3 = null,
            strMeasure4 = null,
            strMeasure5 = null,
            strMeasure6 = null,
            strMeasure7 = null,
            strMeasure8 = null,
            strMeasure9 = null,
            strMeasure10 = null,
            strMeasure11 = null,
            strMeasure12 = null,
            strMeasure13 = null,
            strMeasure14 = null,
            strMeasure15 = null,
            strMeasure16 = null,
            strMeasure17 = null,
            strMeasure18 = null,
            strMeasure19 = null,
            strMeasure20 = null,
            strSource = null,
            strImageSource = null,
            strCreativeCommonsConfirmed = null,
            dateModified = null,
            dateCreated = System.currentTimeMillis()
        )
    }
}

class MealViewModelFactory(private val repository: MealRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            return MealViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}