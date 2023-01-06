package app.rtcgarmentex.ui.adapters

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.rtcgarmentex.R
import app.rtcgarmentex.data.ParticularModel
import app.rtcgarmentex.data.response.CustomerResponse
import app.rtcgarmentex.databinding.RowAddParticularsBinding
import app.rtcgarmentex.ui.listeners.ParticularListener

class AddParticularAdapter(private val context: Context, private val listener: ParticularListener) : RecyclerView.Adapter<AddParticularAdapter.ViewHolder>() {
    private var items = mutableListOf<ParticularModel>()
    lateinit var mBinding: RowAddParticularsBinding
    private var particularItemList: ArrayList<String>? = null

    init {
        items = ArrayList()
    }

    fun setParticularItemList(items: ArrayList<String>) {
        particularItemList = items
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
            itemBinding.particularEt.setOnClickListener {
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.dialog_searchable_spinner)
                dialog.show()
                dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                val editText = dialog.findViewById<EditText>(R.id.edit_text)
                (dialog.findViewById<TextView>(R.id.title)).setText(context.getString(R.string.select_particular))
                val listView = dialog.findViewById<ListView>(R.id.list_view)
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, particularItemList!!)
                listView.adapter = adapter

                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        adapter.filter.filter(s)
                    }

                    override fun afterTextChanged(s: Editable) {}
                })

                listView.setOnItemClickListener { parent, view, pos, id ->
                    itemBinding.particularEt.setText(adapter.getItem(pos)) // The item that was clicked
                    item.particular = adapter.getItem(pos).toString()
                    listener.updateParticular(position, item)
                    dialog.dismiss()
                }
            }

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
