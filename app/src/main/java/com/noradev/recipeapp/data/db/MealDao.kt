package com.noradev.recipeapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noradev.recipeapp.data.model.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM meal_table")
    fun getAllMeals(): LiveData<List<Meal>>

    @Query("SELECT * FROM meal_table WHERE idMeal LIKE :idMeal LIMIT 1")
    fun getMealFromId(idMeal: Int): LiveData<Meal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal): Long

    @Delete
    suspend fun delete(meal: Meal)
}