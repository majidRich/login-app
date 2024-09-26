package com.example.a283_loginappexample.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.a283_loginappexample.androidWrapper.ActivityUtils
import com.example.a283_loginappexample.databinding.ActivityMainBinding
import com.example.a283_loginappexample.remote.RetrofitService
import com.example.a283_loginappexample.remote.dataModel.DefaultModel
import com.example.a283_loginappexample.remote.dataModel.GetApiModel
import com.example.a283_loginappexample.remote.ext.ErrorUtils
import com.example.a283_loginappexample.ui.HomeActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewMainActivity(private val context: Context, private val finished: ActivityUtils) {

    val binding = ActivityMainBinding.inflate(LayoutInflater.from(context))

    @SuppressLint("SetTextI18n")
    fun onClickHandler(androidId: String, error: String) {

        binding.btnSend.setOnClickListener {

            val email = binding.edtInputEmail.text.toString()

            if (email.isEmpty()) {
                binding.textInputEmail.error = error
                return@setOnClickListener
            }

            binding.textInputEmail.error = null
            hideKeyboard()
            sendCodeInEmail(email)

            binding.btnSend.visibility = View.INVISIBLE
            binding.textInputEmail.visibility = View.INVISIBLE

            binding.txtSendEmail.visibility = View.VISIBLE
            binding.textInputCode.visibility = View.VISIBLE
            binding.btnConfirm.visibility = View.VISIBLE
            binding.txtWrong.visibility = View.VISIBLE

            binding.txtSendEmail.text = "send code to email : $email"
        }

        binding.txtWrong.setOnClickListener {

            binding.btnSend.visibility = View.VISIBLE
            binding.textInputEmail.visibility = View.VISIBLE

            binding.txtSendEmail.visibility = View.INVISIBLE
            binding.textInputCode.visibility = View.INVISIBLE
            binding.btnConfirm.visibility = View.INVISIBLE
            binding.txtWrong.visibility = View.INVISIBLE

        }

        binding.btnConfirm.setOnClickListener {
            val email = binding.edtInputEmail.text.toString()
            val code = binding.edtCode.text.toString()
            if (code.length < 6) {
                binding.textInputCode.error = "کد نباید کتر از 6 رفم باشد"
                return@setOnClickListener
            }
            binding.textInputCode.error = null

            verifyCode(androidId, code, email)
        }

    }

    private fun sendCodeInEmail(email: String) {

        val service = RetrofitService.service

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.sendCode(email)
            try {
                if (response.isSuccessful) {
                    val data = response.body() as DefaultModel
                    launch(Dispatchers.Main) {
                        Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    launch(Dispatchers.Main) {
                        Toast.makeText(context, ErrorUtils.getError(response), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.i("SERVER_ERROR", e.message.toString())
            }
        }

    }

    private fun verifyCode(androidId: String, code: String, email: String) {

        val service = RetrofitService.service

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.verifyCode(androidId, code, email)

            try {
                if (response.isSuccessful) {
                    launch(Dispatchers.Main) {
                        val data = response.body() as GetApiModel
                        data.api
                        Toast.makeText(context, "لاگین شما موفقیت آمیز بود", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.putExtra("email", email)
                        context.startActivity(intent)
                        finished.finished()
                    }
                } else {
                    launch(Dispatchers.Main) {
                        Toast.makeText(context, ErrorUtils.getError(response), Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Log.i("SERVER_ERROR", e.message.toString())
            }
        }

    }

    private fun isValidEmail(error: String, editText: TextInputEditText): Boolean {

        val email = binding.edtInputEmail.text.toString().trim()
        if (email.isEmpty()) {
            editText.error = error
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText.error = "ساختار ایمیل صحیح نیست"
            return false
        }

        editText.error = null
        return true

    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}