package com.sendhur.cameraandphotopicker

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _list = MutableLiveData<List<Uri>>()
    val list: LiveData<List<Uri>> get() = _list
    var deletedPosition = -1
    fun setData(newList: List<Uri>) {
        val currentList = _list.value.orEmpty().toMutableList()
        currentList.addAll(newList)
        _list.value = currentList
    }

    fun deleteData() {
        val currentList = _list.value.orEmpty().toMutableList()
        if (deletedPosition < currentList.size) {
            currentList.removeAt(deletedPosition)
            deletedPosition = -1
        }
        _list.value = currentList
    }
}