package com.wikiart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CategorySpinnerAdapter<T : CategoryItem>(
    context: Context,
    private val categories: Array<T>
) : ArrayAdapter<T>(context, R.layout.spinner_category_item, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_category_item, parent, false)
        bind(view, categories[position])
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_category_item, parent, false)
        bind(view, categories[position])
        return view
    }

    private fun bind(view: View, category: T) {
        val text = view.findViewById<TextView>(android.R.id.text1)
        val icon = view.findViewById<ImageView>(R.id.iconView)
        text.text = context.getString(category.nameRes())
        icon.setImageResource(category.iconRes())
    }
}
