package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizgame.databinding.ActivityForgotPasswordBinding
import com.example.quizgame.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var forgotbinding : ActivityForgotPasswordBinding
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotbinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotbinding.root

        setContentView(view)
        forgotbinding.buttonReset.setOnClickListener {

            val userEmail = forgotbinding.editTextSignupForgotEmail.text.toString()

            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task->
                if(task.isSuccessful){

                    Toast.makeText(applicationContext,"We sent a reset email to your email address",Toast.LENGTH_SHORT).show()
                    finish()
                } else{
                    Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()


                }
            }
        }
    }
}