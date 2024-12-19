package com.dicoding.academy.mystoryapp.ui.login

import androidx.lifecycle.ViewModel
import com.dicoding.academy.mystoryapp.data.AuthRepository

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    fun loginUser(email: String, password: String) = repository.loginUser(email, password)
}