package com.dicoding.academy.mystoryapp.di

import android.content.Context
import com.dicoding.academy.mystoryapp.data.AuthRepository
import com.dicoding.academy.mystoryapp.data.UserRepository
import com.dicoding.academy.mystoryapp.data.local.pref.UserPreference
import com.dicoding.academy.mystoryapp.data.local.pref.dataStore
import com.dicoding.academy.mystoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService)
    }

    fun authRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return AuthRepository.getInstance(pref, apiService)
    }
}