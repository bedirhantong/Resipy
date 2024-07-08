package com.bedirhan.resipy.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bedirhan.resipy.databinding.HomeRecyclerviewRowBinding
import com.bedirhan.resipy.model.ResipyModel
import com.bedirhan.resipy.util.customizedPlaceHolder
import com.bedirhan.resipy.util.downloadImage

class ResipyRecyclerAdapter(private val onClickResipy : (resipId : Int)->Unit)
    : RecyclerView.Adapter<ResipyRecyclerAdapter.ResipyViewHolder>() {

    private var resipyList: List<ResipyModel> = arrayListOf()


    inner class ResipyViewHolder(
      private val binding : HomeRecyclerviewRowBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(resipyModel: ResipyModel){
            binding.name.text = resipyModel.isim
            binding.caloryId.text = resipyModel.kalori
            binding.homeRecyclerviewRowImage.downloadImage(
                resipyModel.gorsel, customizedPlaceHolder(itemView.context)
            )
            binding.root.setOnClickListener{
                onClickResipy.invoke(resipyModel.uuid)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResipyViewHolder {
        // inflate : xml ile bir layoutu bağlamayı amaçlar
        val binding = HomeRecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        // bizden binding yapmamızı istiyor
        return ResipyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return resipyList.size
    }

    override fun onBindViewHolder(holder: ResipyViewHolder, position: Int) {
        holder.bind(
            resipyList[position]
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list : List<ResipyModel>){
        resipyList = list
        notifyDataSetChanged()
    }
}