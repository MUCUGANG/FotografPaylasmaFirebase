package com.example.fotografpaylasmafirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fotografpaylasmafirebase.databinding.ActivityFotografPaylasmaBinding
import com.example.fotografpaylasmafirebase.databinding.ActivityFotografPaylasmaBinding.inflate
import com.example.fotografpaylasmafirebase.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso


class HaberRecyclerAdapter(val postlist : ArrayList<Post>) : RecyclerView.Adapter<HaberRecyclerAdapter.PostHolder>(){
    class PostHolder(val itemBinding : RecyclerRowBinding) :
        RecyclerView.ViewHolder(itemBinding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RecyclerRowBinding.inflate(inflater,parent,false)

        return PostHolder(itemBinding)
    }

    override fun getItemCount(): Int {
     return postlist.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemBinding.recyclerRowKullaniciEmail.text = postlist[position].kullaniciEmail
        holder.itemBinding.recyclerRowKullaniciYorumu.text = postlist[position].kullaniciYorum
        Picasso.get().load(postlist[position].gorselUrl).into(holder.itemBinding.recyclerRowImageview)
        //picasso kütüphanesşnş kullanarak görüntüyü rahat bir şekilde çektik
    // implemantion aldık bu githubdan pıicasso için
    // https://github.com/square/picasso
    }

}