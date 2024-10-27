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
            strMeal = "New Recipe",
            strDrinkAlternate = "",
            strCategory = "",
            strArea = "",
            strInstructions = "",
            strMealThumb = "",
            strTags = "",
            strYoutube = "",
            strIngredient1 = "",
            strIngredient2 = "",
            strIngredient3 = "",
            strIngredient4 = "",
            strIngredient5 = "",
            strIngredient6 = "",
            strIngredient7 = "",
            strIngredient8 = "",
            strIngredient9 = "",
            strIngredient10 = "",
            strIngredient11 = "",
            strIngredient12 = "",
            strIngredient13 = "",
            strIngredient14 = "",
            strIngredient15 = "",
            strIngredient16 = "",
            strIngredient17 = "",
            strIngredient18 = "",
            strIngredient19 = "",
            strIngredient20 = "",
            strMeasure1 = "",
            strMeasure2 = "",
            strMeasure3 = "",
            strMeasure4 = "",
            strMeasure5 = "",
            strMeasure6 = "",
            strMeasure7 = "",
            strMeasure8 = "",
            strMeasure9 = "",
            strMeasure10 = "",
            strMeasure11 = "",
            strMeasure12 = "",
            strMeasure13 = "",
            strMeasure14 = "",
            strMeasure15 = "",
            strMeasure16 = "",
            strMeasure17 = "",
            strMeasure18 = "",
            strMeasure19 = "",
            strMeasure20 = "",
            strSource = "",
            strImageSource = "",
            strCreativeCommonsConfirmed = "",
            dateModified = "",
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