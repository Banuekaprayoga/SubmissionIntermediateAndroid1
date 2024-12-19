package com.dicoding.academy.mystoryapp.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.academy.mystoryapp.R
import com.dicoding.academy.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.academy.mystoryapp.ui.ViewModelFactory
import com.dicoding.academy.mystoryapp.ui.welcome.WelcomeActivity
import com.dicoding.academy.mystoryapp.data.Result
import com.dicoding.academy.mystoryapp.ui.ViewModelAuthFactory
import com.dicoding.academy.mystoryapp.ui.upload.UploadActivity

class MainActivity : AppCompatActivity() {
    private val viewModelAuth by viewModels<MainViewModelAuth> {
        ViewModelAuthFactory.getInstance(this)
    }
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModelAuth.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                Log.d(TAG, "onCreate: Login Failed")
                finish()
            } else {
                observeViewModel()
                setupRecyclerView()
                binding.fabAdd.setOnClickListener {
                    val intent = Intent(this@MainActivity, UploadActivity::class.java)
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
                }
                Log.d(TAG, "onCreate: Login Success")
            }
        }
        setupView()
    }

    private fun setupRecyclerView() {
        adapter = StoryAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = adapter

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.rvStory.addItemDecoration(itemDecoration)
    }

    private fun observeViewModel() {
        viewModel.getStory().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        val storyData = result.data
                        showLoading(false)
                        showToast(getString(R.string.success))
                        adapter.submitList(storyData)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading)  View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutMenu -> { viewModelAuth.logout() }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}