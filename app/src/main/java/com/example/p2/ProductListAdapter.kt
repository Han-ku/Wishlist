package com.example.p2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ProductListAdapter(context: Context, entries: List<Product>, private val clickListener: (Product) -> Unit, private val longClickListener: (Product) -> Unit)
    : RecyclerView.Adapter<ProductListAdapter.ListViewHolder>() {

    var context: Context? = null
    var entries: List<Product> = java.util.ArrayList()

    init {
        this.context = context
        this.entries = entries
    }

    fun updateProductList(products: List<Product>) {
        this.entries = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.simple_card, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val product: Product = entries[position]
        holder.productName.text = product.name
        holder.productLocation.text = product.location
        holder.productDescription.text = product.description

        holder.productRowContainer.setOnClickListener {
            clickListener(product)
        }

        holder.productRowContainer.setOnLongClickListener {
            longClickListener(product)
            true
        }
    }

    override fun getItemCount() =  entries.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productName: TextView
        var productLocation: TextView
        var productDescription: TextView

        var productRowContainer: CardView

        init {
            productName = itemView.findViewById(R.id.name)
            productLocation = itemView.findViewById(R.id.adres)
            productDescription = itemView.findViewById(R.id.description)

            productRowContainer = itemView.findViewById(R.id.taskRowContainer)
        }
    }
}