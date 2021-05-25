package com.example.shopmanagement.ui.sales.product


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmanagement.R
import com.example.shopmanagement.data.models.SalesProductModel
import com.example.shopmanagement.databinding.ProductSelectedLayoutBinding

class AdapterSelectedProduct(private val list : ArrayList<SalesProductModel>,
                             private val listener: IQuantityChangeListener)
    : RecyclerView.Adapter<AdapterSelectedProduct.ProductViewHolder>() {

    class ProductViewHolder(
        private val binding: ProductSelectedLayoutBinding,
        private val listener: IQuantityChangeListener
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(product: SalesProductModel, position: Int){

            binding.product = product
            binding.idNo.text = "${position+1}"
            binding.price.setText(product.price.toString())

            binding.quantityEditText.addTextChangedListener {
                if(binding.quantity.editText?.text.toString().isEmpty()){
                    binding.totalPriceTv.setText("${1 * binding.price.text.toString().toDouble()}")
                }else {

                    binding.totalPriceTv.setText("${binding.quantity.editText?.text.toString().toDouble() * binding.price.text.toString().toDouble()}")
                    listener.updateProduct(position,binding.quantity.editText?.text.toString().toDouble(),binding.totalPriceTv.text.toString().toDouble(),binding.price.text.toString().toDouble())
                }
            }

            binding.incrementQuantityBtn.setOnClickListener {
                var quantity = binding.quantityEditText.text.toString().toDouble()
                binding.quantityEditText.setText("${quantity + 1.0}")
//                listener.incrementQuantity(product)
            }

            binding.decrementQuantityBtn.setOnClickListener {
                var quantity = binding.quantityEditText.text.toString().toDouble()
                if (quantity > 1) {
                    binding.quantityEditText.setText("${quantity - 1.0}")
//                    listener.decrementProduct(product.id)
                }
            }

            binding.price.addTextChangedListener {
                if(binding.price.text.toString().isEmpty()){
                    binding.totalPriceTv.setText("${binding.quantity.editText?.text.toString().toDouble() * product.price}")
                }
                else{
                    binding.totalPriceTv.setText("${binding.quantity.editText?.text.toString().toDouble() * binding.price.text.toString().toDouble()}")
                    listener.updateProduct(
                        position,
                        binding.quantity.editText?.text.toString().toDouble(),
                        binding.totalPriceTv.text.toString().toDouble(),
                        binding.price.text.toString().toDouble()
                    )
                }

            }

            binding.deleteProduct.setOnClickListener {
                listener.deleteProduct(product)
            }

            binding.root.setOnClickListener {
                listener.showWeightDialog(product,position,binding)
            }

            binding.quantityEditText.setText("${product.quantity}")

            binding.totalPriceTv.setText("${String.format("%.2f",binding.quantity.editText?.text.toString().toDouble() * binding.price.text.toString().toDouble()).toDouble()}")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterSelectedProduct.ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ProductSelectedLayoutBinding>(layoutInflater, R.layout.product_selected_layout,parent,false)
        return ProductViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: AdapterSelectedProduct.ProductViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int = list.size
}