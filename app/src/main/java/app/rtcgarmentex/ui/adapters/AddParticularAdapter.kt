package app.rtcgarmentex.ui.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.rtcgarmentex.data.ParticularModel
import app.rtcgarmentex.databinding.RowAddParticularsBinding
import app.rtcgarmentex.ui.listeners.ParticularListener

class AddParticularAdapter(private val context: Context, private val listener: ParticularListener) : RecyclerView.Adapter<AddParticularAdapter.ViewHolder>() {
    private var items = mutableListOf<ParticularModel>()
    lateinit var mBinding: RowAddParticularsBinding

    init {
        items = ArrayList()
    }

    fun setData(data: ArrayList<ParticularModel>) {
        items.clear()
        items = data.toMutableList()
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = RowAddParticularsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = items[position]
        holder.onBind(item, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val itemBinding: RowAddParticularsBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun onBind(item: ParticularModel, position: Int) {
            if (item.particular.isNotEmpty()) {
                itemBinding.particularEt.setText(item.particular)
            } else {
                itemBinding.particularEt.setText("")
            }
            if (item.type.isNotEmpty()) {
                itemBinding.typeEt.setText(item.type)
            } else {
                itemBinding.typeEt.setText("")
            }
            if (item.qty == 0) {
                itemBinding.qtyEt.setText("")
            } else {
                itemBinding.qtyEt.setText(item.qty.toString())
            }
            if (item.amount == 0) {
                itemBinding.amountEt.setText("")
            } else {
                itemBinding.amountEt.setText(item.amount.toString())
            }
            itemBinding.particularEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        item.particular = p0.toString()
                        listener.updateParticular(position, item)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            itemBinding.typeEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        item.type = p0.toString()
                        listener.updateParticular(position, item)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            mBinding.qtyEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        item.qty = p0.toString().toInt()
                        listener.updateParticular(position, item)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            mBinding.amountEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        item.amount = p0.toString().toInt()
                        listener.updateParticular(position, item)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            if (position == 0) {
                itemBinding.deleteIv.visibility = INVISIBLE
            }
            itemBinding.deleteIv.setOnClickListener {
                listener.deleteParticular(position)
            }
        }
    }

}
