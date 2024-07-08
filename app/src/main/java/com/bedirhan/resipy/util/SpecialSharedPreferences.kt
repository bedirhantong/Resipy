package com.bedirhan.resipy.util

import android.content.Context
import android.content.SharedPreferences


class SpecialSharedPreferences {
    companion object{

        private val LAST_MODIFY_DATE = "time"

        private var sharedPreferences:SharedPreferences?=null

        @Volatile
        private var instance : SpecialSharedPreferences? = null

        private val lock = Any()


        /*
            - bir nesne oluşturulduğunda arka planda invoke metodu çalışır ve
            instance degeri
            1- null ise :createDatabase ile yeni bir Database oluşturacak
            2- null değilse : senkronize bir şekilde bu instance değerine
            erişmeye çalışır
         */
        operator fun invoke(context: Context) = instance?: synchronized(lock){
            instance ?: createSpecialSharedPreferences(context).also {
                instance=it
            }
        }

        private fun createSpecialSharedPreferences(context: Context):SpecialSharedPreferences{
            sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return SpecialSharedPreferences()
        }

    }

    fun recordTime(time:Long){
        sharedPreferences?.edit()?.putLong(LAST_MODIFY_DATE,time)?.apply()
    }

    fun getRecordedTime() = sharedPreferences?.getLong(LAST_MODIFY_DATE,0)
}