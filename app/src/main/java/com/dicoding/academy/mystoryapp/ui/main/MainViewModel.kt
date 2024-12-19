package com.dicoding.academy.mystoryapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.academy.mystoryapp.data.AuthRepository
import com.dicoding.academy.mystoryapp.data.UserRepository
import com.dicoding.academy.mystoryapp.data.local.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModelAuth(private val repository: AuthRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}
class MainViewModel(private val repository: UserRepository): ViewModel() {
    fun getStory() = repository.getStoryList()
}