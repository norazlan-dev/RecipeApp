package com.noradev.recipeapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.noradev.recipeapp.data.model.Meal
import com.noradev.recipeapp.data.repository.MealRepository
import kotlinx.coroutines.launch

class MealViewModel(private val repository: MealRepository) : ViewModel() {
    val allMeals: LiveData<List<Meal>> = repository.getMeals()
    var meal: LiveData<Meal>? = null

    fun getRandomMealFromServer() {
        viewModelScope.launch {
            repository.getRandomMeal()
        }
    }

    fun getMealFromId(mealId: Int) {
        meal = repository.getMealFromId(mealId)
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            repository.insertMeal(meal)
        }
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