package com.bedirhan.resipy.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bedirhan.resipy.model.ResipyModel
import kotlin.concurrent.Volatile

@Database(entities = [ResipyModel::class], version = 1)
abstract class ResipyDB: RoomDatabase() {
    abstract fun resipyDao(): ResipyDao

    /*
        - Data Racing : farklı yerlerden db ye erişip onu aynı anda
        değiştirmek isterlerse sorun yaşarlar bu sorunu sadece tek bir
        zamanda bir tanesi yazıyorken diğeri sadece okuyabilsin ile yapacağız.
     */

    companion object{
        @Volatile
        private var instance : ResipyDB? = null

        private val lock = Any()


        /*
            - bir nesne oluşturulduğunda arka planda invoke metodu çalışır ve
            instance degeri
            1- null ise :createDatabase ile yeni bir Database oluşturacak
            2- null değilse : senkronize bir şekilde bu instance değerine
            erişmeye çalışır
         */
        operator fun invoke(context: Context) = instance?: synchronized(lock){
            instance ?: createDatabase(context).also {
                instance=it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ResipyDB::class.java,
            "ResipyDB"
        ).build()
    }
}
