package com.dafa.suitmediatest.screen

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.dafa.suitmediatest.R
import com.dafa.suitmediatest.databinding.ActivityFirstscreenBinding

class FirstScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFirstscreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            btnCheck.setOnClickListener {
                val palindrome = tePalindrome.text.toString().lowercase()
                val message = if (palindrome.isNullOrBlank()) {
                    "Input is empty"
                } else if (isPalindrome(palindrome)) {
                    "isPalindrome"
                } else {
                    "not palindrome"
                }
                AlertDialog.Builder(this@FirstScreenActivity).apply {
                    setMessage(message)
                    create()
                    show()
                }
            }

            teName.hint = getString(R.string.name)
            tePalindrome.hint = getString(R.string.palindrome)

            val onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                val editText = view as? EditText
                if (hasFocus) {
                    editText?.hint = null
                } else {
                    if (editText?.text.isNullOrEmpty()) {
                        editText?.hint = editText?.tag as? CharSequence
                    }
                }
            }
            teName.onFocusChangeListener = onFocusChangeListener
            teName.tag = getString(R.string.name)

            tePalindrome.onFocusChangeListener = onFocusChangeListener
            tePalindrome.tag = getString(R.string.palindrome)

            btnNext.setOnClickListener {
                val name = binding.teName.text.toString()
                val intent = Intent(this@FirstScreenActivity, SecondScreenActivity::class.java)
                intent.putExtra(SecondScreenActivity.USER_NAME, name)
                startActivity(intent)
            }
        }
    }

    private fun isPalindrome(str: String): Boolean {
        val cleanStr = str.replace("[^a-zA-Z0-9]".toRegex(), "")
        return cleanStr == cleanStr.reversed()
    }

}