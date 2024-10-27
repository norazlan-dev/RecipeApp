package com.noradev.recipeapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noradev.recipeapp.data.model.Meal
import com.noradev.recipeapp.databinding.RvMealBinding
import com.noradev.recipeapp.ui.utils.loadImageFromUrl

class MealAdapter(private val context: Context): RecyclerView.Adapter<MealAdapter.ViewHolder>() {
    private var mealList: MutableList<Meal> = ArrayList()
    private var mealListFilter: MutableList<Meal> = ArrayList()
    private var mealListSearch: MutableList<Meal> = ArrayList()
    private lateinit var onItemsClickListener: OnItemsClickListener

    interface OnItemsClickListener {
        fun onItemClick(
            mealData: Meal
        )
    }

    fun setOnItemClickListener(onItemsClickListener: OnItemsClickListener) {
        this.onItemsClickListener = onItemsClickListener
    }

    class ViewHolder(binding: RvMealBinding): RecyclerView.ViewHolder(binding.root){
        val bd = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvMealBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bd.tvName.text = mealList[position].strMeal
        mealList[position].strMealThumb?.let { holder.bd.ivMeal.loadImageFromUrl(it) }

        holder.itemView.setOnClickListener {
            this.onItemsClickListener.onItemClick(
                mealList[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    fun setMeals(mealList: List<Meal>){
        this.mealList = mealList.toMutableList()
        notifyDataSetChanged()
    }
}