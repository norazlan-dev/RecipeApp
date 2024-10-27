package com.noradev.recipeapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.noradev.recipeapp.data.model.Meal
import com.noradev.recipeapp.databinding.RvMealBinding
import com.noradev.recipeapp.ui.utils.loadImageFromUrl
import java.util.Locale

class MealAdapter : RecyclerView.Adapter<MealAdapter.ViewHolder>(),
    Filterable {
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
        holder.bd.tvName.text = mealListFilter[position].strMeal
        mealListFilter[position].strMealThumb?.let { holder.bd.ivMeal.loadImageFromUrl(it) }
        if (mealListFilter[position].strMealThumb.isNullOrBlank()) {
            holder.bd.ivMeal.setImageDrawable(null)
        }

        holder.itemView.setOnClickListener {
            this.onItemsClickListener.onItemClick(
                mealListFilter[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return mealListFilter.size
    }

    fun setMeals(mealList: List<Meal>){
        this.mealList = mealList.toMutableList()
        this.mealListFilter = mealList.toMutableList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                mealListSearch = if (charSearch.isEmpty()) {
                    mealList
                } else {
                    val resultList = ArrayList<Meal>()
                    for (row in mealList) {
                        if (row.strMeal?.lowercase(Locale.ROOT)?.contains(charSearch.lowercase(Locale.ROOT)) == true) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = mealListSearch
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let {
                    it.values?.let { values ->
                        mealListFilter = values as ArrayList<Meal>
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}