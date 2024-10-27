package com.noradev.recipeapp.data.repository

import androidx.lifecycle.LiveData
import com.noradev.recipeapp.data.db.MealDao
import com.noradev.recipeapp.data.model.Meal
import com.noradev.recipeapp.data.network.ApiService

class MealRepository(private val mealDao: MealDao, private val apiService: ApiService) {
    fun getMeals(): LiveData<List<Meal>> {
        return mealDao.getAllMeals()
    }

    fun getMealFromId(mealId: Int): LiveData<Meal> {
        return mealDao.getMealFromId(mealId)
    }

    suspend fun getRandomMeal() {
        val remoteMeal = apiService.getRandomMeal()
        val meal = remoteMeal.meals[0]
        meal.dateCreated = System.currentTimeMillis()
        mealDao.insertMeal(meal)
    }

    suspend fun insertMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    suspend fun deleteMeal(meal: Meal) {
        mealDao.delete(meal)
    }
}
