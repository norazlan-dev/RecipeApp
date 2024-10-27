package com.noradev.recipeapp.data.model

import com.google.gson.annotations.SerializedName

data class Root (
    @SerializedName("meals" ) var meals : ArrayList<Meal> = arrayListOf()
)
