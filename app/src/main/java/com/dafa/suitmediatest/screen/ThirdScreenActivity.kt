package com.dafa.suitmediatest.screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dafa.suitmediatest.adapter.UserAdapter
import com.dafa.suitmediatest.api.ApiConfig
import com.dafa.suitmediatest.databinding.ActivityThirdScreenBinding
import com.dafa.suitmediatest.response.DataItem
import com.dafa.suitmediatest.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdScreenBinding
    private lateinit var adapter: UserAdapter

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val perPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSwipeRefreshLayout()
        setupRecyclerView()
        customToolbar()
        fetchData(currentPage)
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        val recyclerView = binding.rvUser
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                android.os.Handler().postDelayed({
                    if (!isLoading && !isLastPage && totalItemCount >= lastVisibleItem) {
                        currentPage+1
                        fetchData(currentPage)
                    }
                },2000)
            }
        })

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItem) {
                val resultIntent = Intent()
                resultIntent.putExtra(SecondScreenActivity.SELECTED_USER_NAME, data.firstName + " " + data.lastName)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        })
    }

    private fun setupSwipeRefreshLayout() {
        binding.srl.setOnRefreshListener {
            currentPage = 1
            isLastPage = false
            adapter.clearData()
            fetchData(currentPage)
        }
    }

    private fun fetchData(page: Int) {
        isLoading = true
        binding.progressBar.visibility = View.VISIBLE
        val apiService = ApiConfig.getApiService().getUserData(page, perPage)
        apiService.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.data
                    responseBody?.let { newData ->
                        if (newData.size < perPage) {
                            isLastPage = true
                        } else {
                            // Append new data to your adapter
                            currentPage++
                        }
                        adapter.addData(newData)

                    }
                    if (adapter.itemCount == 0) {
                        binding.tvEmptyList.visibility = View.VISIBLE
                    } else {
                        binding.tvEmptyList.visibility = View.GONE
                    }
                    binding.srl.isRefreshing = false
                    isLoading = false
                    binding.progressBar.visibility = View.GONE
                }
                else {
                    Log.d("ThirdScreenActivity", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("ThirdScreenActivity", "onFailure: ${t.message}")
            }
        })
    }

    private fun customToolbar() {
        binding.toolbar.apply {
            tvToolbarName.setText("Third Screen")
            navBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}
