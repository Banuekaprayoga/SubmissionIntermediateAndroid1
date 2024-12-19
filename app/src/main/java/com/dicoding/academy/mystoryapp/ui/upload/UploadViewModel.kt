package com.dicoding.academy.mystoryapp.ui.upload

import androidx.lifecycle.ViewModel
import com.dicoding.academy.mystoryapp.data.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody


class UploadViewModel(private val repository: UserRepository): ViewModel() {
    fun uploadFile(file: MultipartBody.Part, desc: RequestBody) = repository.uploadFile(file, desc)
}