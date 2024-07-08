package com.bedirhan.resipy.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bedirhan.resipy.model.ResipyModel

@Dao
interface ResipyDao {
    /*
        - eger bir parametre vararg olarak tanımlanırsa o
        paramtereden istediğimiz sayıda girebiliriz
     */
    @Insert
    suspend fun insertAll(vararg resipyModel: ResipyModel) : List<Long>
    // Ekledigi resipy lerin id lerini long olarak donuyor


    @Query("SELECT * FROM resipymodel")
    suspend fun getAllResipies() : List<ResipyModel>


    @Query("SELECT * FROM resipymodel WHERE uuid=:resipyId")
    suspend fun getResipyById(resipyId:Int) : ResipyModel


    @Query("DELETE FROM resipymodel")
    suspend fun deleteAllResipies()
}