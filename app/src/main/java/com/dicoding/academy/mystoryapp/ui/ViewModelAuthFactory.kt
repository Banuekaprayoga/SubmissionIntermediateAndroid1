package com.dicoding.academy.mystoryapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.academy.mystoryapp.data.AuthRepository
import com.dicoding.academy.mystoryapp.di.Injection
import com.dicoding.academy.mystoryapp.ui.login.LoginViewModel
import com.dicoding.academy.mystoryapp.ui.main.MainViewModelAuth
import com.dicoding.academy.mystoryapp.ui.register.RegisterViewModel

class ViewModelAuthFactory(private val repository: AuthRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModelAuth::class.java) -> {
                MainViewModelAuth(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: ViewModelAuthFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelAuthFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelAuthFactory::class.java) {
                    INSTANCE = ViewModelAuthFactory(Injection.authRepository(context))
                }
            }
            return INSTANCE as ViewModelAuthFactory
        }
    }
}