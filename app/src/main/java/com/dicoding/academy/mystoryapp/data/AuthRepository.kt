package com.dicoding.academy.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.academy.mystoryapp.data.local.pref.UserModel
import com.dicoding.academy.mystoryapp.data.local.pref.UserPreference
import com.dicoding.academy.mystoryapp.data.remote.response.LoginResponse
import com.dicoding.academy.mystoryapp.data.remote.response.RegisterResponse
import com.dicoding.academy.mystoryapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    fun registerUser(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }

    }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            saveSession(
                UserModel(
                    name = response.loginResult?.name ?: "",
                    email = email,
                    password = password,
                    token = response.loginResult?.token ?: "",
                    isLogin = true
                )
            )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(userPreference, apiService)
            }.also { instance = it }
    }
}