package com.bedirhan.resipy.view.detailPage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bedirhan.resipy.model.ResipyModel
import com.bedirhan.resipy.roomdb.ResipyDB
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) :AndroidViewModel(application){
    val resipyData = MutableLiveData<ResipyModel>()

    fun getResipyFromLocalDb(uuid:Int){
        viewModelScope.launch {
            val dao = ResipyDB(getApplication()).resipyDao()
            val resipy = dao.getResipyById(uuid)
            resipyData.value = resipy
        }
    }
}