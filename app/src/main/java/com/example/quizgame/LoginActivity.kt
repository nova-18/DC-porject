package com.example.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.quizgame.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding:ActivityLoginBinding
    val auth = FirebaseAuth.getInstance()
    lateinit var googleSignInClient : GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)
        val textofGoogleButton = loginBinding.buttonGoogleSignin.getChildAt(0) as TextView
        textofGoogleButton.text = "Continue with Google"
        textofGoogleButton.setTextColor(Color.BLACK)
        textofGoogleButton.textSize = 12F
        registerActivityforGoogleSignIn()
        loginBinding.buttonLogin.setOnClickListener {
            val userEmail = loginBinding.editTextLoginEmail.text.toString()
            val userPassword = loginBinding.editTextLoginPassword.text.toString()
            signInUser(userEmail,userPassword)
        }
        loginBinding.buttonGoogleSignin.setOnClickListener {
            signInGoogle()
        }
        loginBinding.buttonSignup.setOnClickListener {

            val intent1 = Intent(this,SignupActivity::class.java)
            startActivity(intent1)
        }
        loginBinding.buttonForgotPassword.setOnClickListener {
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }


    }
    fun signInUser(userEmail:String,userPassword:String){
        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener { task->

            if(task.isSuccessful){
                Toast.makeText(applicationContext,"Welcome to Quizgame",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
            }else{

                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }

    }

    override  fun onStart(){
        super.onStart()
        val user = auth.currentUser
        if (user != null){
            Toast.makeText(applicationContext,"Welcome to Quizgame",Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signInGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("761627872998-b9iedhpaivkgvgc94v7vbai9bpc1bfkj.apps.googleusercontent.com")
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        signIn()



    }

    private fun signIn(){

        val signInIntent :Intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)



    }

    private fun registerActivityforGoogleSignIn(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback{result->
                val resultCode=result.resultCode
                val data = result.data

                if(resultCode == RESULT_OK && data != null){

                    val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseSignInWithGoogle(task)
                }


            })
    }

    private fun firebaseSignInWithGoogle(Task: Task<GoogleSignInAccount>){

        try {
            val account : GoogleSignInAccount = Task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext,"Welcome to quiz game",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }catch (e : ApiException){
            Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }
    }


    private fun firebaseGoogleAccount(account : GoogleSignInAccount){

        val authCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task->

            if(task.isSuccessful){

                //val user = auth.currentUser


            } else{

            }
        }
    }
}