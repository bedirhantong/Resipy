package com.bedirhan.resipy.service

import com.bedirhan.resipy.model.ResipyModel
import retrofit2.http.GET

interface ResipyAPI {

    // Base url -> https://raw.githubusercontent.com/

    // Endpoint -> atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json
    /*
        - suspend ile coroutine kullanacağız. Bu sayede istediğimiz zaman
        duraklatıp, devam ettirilebilen fonksiyondur
            - suspend fonksiyonları,
            1- suspend fonksiyonlar içinde
            2- coroutine kapsamı içerisinde çağırabilirsin.
     */
    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")
    suspend fun getResipies():ArrayList<ResipyModel>

}