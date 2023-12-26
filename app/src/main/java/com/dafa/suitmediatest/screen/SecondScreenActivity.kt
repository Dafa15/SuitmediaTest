package com.dafa.suitmediatest.screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dafa.suitmediatest.databinding.ActivitySecondScreenBinding

class SecondScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondScreenBinding

    private val REQUEST_CODE_THIRD_SCREEN = 123

    companion object {
        const val USER_NAME = "user_name"
        const val SELECTED_USER_NAME = "selected_user_name"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userName = intent.getStringExtra(USER_NAME)

        binding.apply {
            tvUserName.text = userName

            btnChooseUser.setOnClickListener {
                val intent = Intent(this@SecondScreenActivity, ThirdScreenActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_THIRD_SCREEN)
            }

        }
        customToolbar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_THIRD_SCREEN && resultCode == Activity.RESULT_OK) {
            val selectedUserName = data?.getStringExtra(SELECTED_USER_NAME)
            binding.tvSelectedUserName.text = selectedUserName
        }
    }

    private fun customToolbar() {
        binding.toolbar.apply {
            navBack.setOnClickListener {
                onBackPressed()
            }
            tvToolbarName.setText("Second Screen")
        }
    }
}