package com.dicoding.academy.mystoryapp.ui.detail

import androidx.lifecycle.ViewModel
import com.dicoding.academy.mystoryapp.data.UserRepository

class DetailViewModel(private val repository: UserRepository): ViewModel() {
    fun getDetailStory(id: String) = repository.getDetailStory(id)
}