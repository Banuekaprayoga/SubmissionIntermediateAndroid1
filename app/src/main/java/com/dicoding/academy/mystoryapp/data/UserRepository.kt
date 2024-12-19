package com.dicoding.academy.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.academy.mystoryapp.data.remote.response.DetailStoryResponse
import com.dicoding.academy.mystoryapp.data.remote.response.FileUploadResponse
import com.dicoding.academy.mystoryapp.data.remote.response.ListStoryItem
import com.dicoding.academy.mystoryapp.data.remote.response.ListStoryResponse
import com.dicoding.academy.mystoryapp.data.remote.response.Story
import com.dicoding.academy.mystoryapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService
) {
    fun uploadFile(file: MultipartBody.Part, desc: RequestBody): LiveData<Result<FileUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadImage(file, desc)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, FileUploadResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }
    fun getDetailStory(id: String): LiveData<Result<Story>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(id)
            val detailStory = response.story
            emit(Result.Success(detailStory))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, DetailStoryResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }
    fun getStoryList(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories()
            val storyList = response.listStory
            emit(Result.Success(storyList))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ListStoryResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }



    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}