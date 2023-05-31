package com.example.p2

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class ProductsAdapter(private val clickListener: (Product) -> Unit, private val longClickListener: (Product) -> Unit): ListAdapter<Product, ProductsAdapter.ProductViewHolder>(ProductComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)

        holder.productRowContainer.setOnClickListener {
            clickListener(current)
        }

        holder.productRowContainer.setOnLongClickListener {
            longClickListener(current)
            true
        }
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photo: ShapeableImageView = itemView.findViewById(R.id.photo)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val adres: TextView = itemView.findViewById(R.id.adres)

        val productRowContainer : CardView = itemView.findViewById(R.id.taskRowContainer)

        fun bind(product: Product) {
            val photoBitmap = BitmapFactory.decodeByteArray(product.photo, 0, product.photo!!.size)
            photo.setImageBitmap(photoBitmap)
            name.text = product.name
            adres.text = product.location
        }

        companion object {
            fun create(parent: ViewGroup): ProductViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.simple_card, parent, false)
                return ProductViewHolder(view)
            }
        }
    }

    class ProductComparator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }
    }
}