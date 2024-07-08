package com.bedirhan.resipy.view.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bedirhan.resipy.model.ResipyModel
import com.bedirhan.resipy.roomdb.ResipyDB
import com.bedirhan.resipy.service.ResipyAPIService
import com.bedirhan.resipy.util.SpecialSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel (application: Application):AndroidViewModel(application) {
    /*
        - Normal ViewModel yerine AndroidViewModel kullanma sebebimiz
        aslında sadece context yapısına erişebilmek
     */

    /*
        - view e etki edecek sürekli değişebilecek dataları MutableLiveData içerisinde
        tutuyoruz
     */

    val resipies = MutableLiveData<List<ResipyModel>>()
    val errorMessage = MutableLiveData<Boolean>()
    val isLoadingResipy = MutableLiveData<Boolean>()



    // veri çekmek için
    private val resipyApiService = ResipyAPIService()

    // son çekilen zaman için
    private val specialSharedPreferences = SpecialSharedPreferences(getApplication())


    private val timeUpdate = 10 * 60 * 1000* 1000 * 1000L

    /*
        - kaydedilme zamanına göre veriyi nereden alacağına bakıyor
     */
    fun refreshData(){
        val recordedTime = specialSharedPreferences.getRecordedTime()
        if (recordedTime != null && recordedTime != 0L && System.nanoTime()-recordedTime < timeUpdate){
            // 10 dakika geçmemiş o yüzden roomdan al
            getLocalResipies()
        }else{
            getRemoteResipies()
        }
    }

    fun getDataFromInternetDirectly(){
        getRemoteResipies()
    }

    private fun getLocalResipies(){
        isLoadingResipy.value = true

        viewModelScope.launch (Dispatchers.IO){
            val resipyList = ResipyDB(getApplication()).resipyDao().getAllResipies()
            withContext(Dispatchers.Main){
                showResipies(resipyList)
                Toast.makeText(getApplication(),"Local Resipies Uploaded",Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun getRemoteResipies(){
        isLoadingResipy.value = true
        /*
            - getData bir suspend fonksiyon, o yüzden scope ile erişmemiz lazım.
            Bunu da viewmodel e ait bir scope ile yapacağız.
            - Burada (Dispatchers.IO) u vermek zorunda değilsin...
         */
        viewModelScope.launch(Dispatchers.IO) {
            val resipiesList = resipyApiService.getData()
            withContext(Dispatchers.Main){
                isLoadingResipy.value = false
//                resipies.value = resipiesList
                Toast.makeText(getApplication(),"Remote Resipies Uploaded",Toast.LENGTH_LONG).show()

                uploadToRoom(resipiesList)
                // room a kaydedip daha sonradan ana ekranda roomdan çekip göstereceğiz
            }
        }
    }

    private fun showResipies(resipyList : List<ResipyModel>){
        resipies.value = resipyList
        errorMessage.value =false
        isLoadingResipy.value = false
    }

    private fun uploadToRoom(resipyList : ArrayList<ResipyModel>){
        viewModelScope.launch {
            val dao = ResipyDB(getApplication()).resipyDao()
            dao.deleteAllResipies()
            /*
                - resipyListteki her elemanı teker teker ekliyor
                - vararg bize uuid leri tek tek atamamızı istiyordu, long list yolluyodu
             */
            val uuid = dao.insertAll(*resipyList.toTypedArray())
            var i = 0
            while (i<uuid.size){
                resipyList[i].uuid = uuid[i].toInt()
                i+=1
            }
            showResipies(resipyList)
        }
        specialSharedPreferences.recordTime(System.nanoTime())
    }


}