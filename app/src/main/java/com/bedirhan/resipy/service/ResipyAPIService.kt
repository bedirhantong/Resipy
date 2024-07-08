package com.bedirhan.resipy.service

import com.bedirhan.resipy.model.ResipyModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResipyAPIService {

    /*
        - addConverterFactory : gelecek olan veri hangi türde gelecek bize?
        ona göre işlem yapacak. Json geleceği için biz GsonConverterFactory
        kullanacağız.
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ResipyAPI::class.java)




    suspend fun getData() : ArrayList<ResipyModel>{
        return retrofit.getResipies()
    }
}