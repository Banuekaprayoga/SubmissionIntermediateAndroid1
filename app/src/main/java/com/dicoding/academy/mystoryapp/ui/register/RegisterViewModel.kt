package com.dicoding.academy.mystoryapp.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.academy.mystoryapp.data.AuthRepository

class RegisterViewModel(private val repository: AuthRepository): ViewModel() {
    fun register(name: String, email: String, password: String) = repository.registerUser(name, email, password)
}