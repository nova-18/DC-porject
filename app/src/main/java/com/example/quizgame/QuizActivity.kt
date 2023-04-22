package com.example.quizgame

import android.content.Intent
import android.graphics.Color
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.quizgame.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    val database = FirebaseDatabase.getInstance("https://quizgame-e8806-default-rtdb.asia-southeast1.firebasedatabase.app")
    val databaseReference = database.reference.child("questions")
    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 0
    var userAnswer = ""
    var userCorrect = 0
    var userWrong = 0

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val responseRef = database.reference.child("scores")
    val questionsnum = HashSet<Int>()
    var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        setContentView(view)

        do{
            val number = Random.nextInt(1,11)
            Log.d("number",number.toString())
            questionsnum.add(number)
        }while(questionsnum.size < 5)

        Log.d("myTag1", questionsnum.toString())
        gameLogic()

        quizBinding.buttonNext.setOnClickListener {
            Log.d("myTag2", "This is my message")
            gameLogic()

        }
        quizBinding.buttonFinish.setOnClickListener {
            endActivity()
        }
        quizBinding.textViewA.setOnClickListener {
            userAnswer = "a"
            if(correctAnswer == userAnswer){

                quizBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                quizBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()

            }

            disableClickableOFOptions()
            sendData(quizBinding.textViewQuestion.text.toString(),quizBinding.textViewA.text.toString())
        }
        quizBinding.textViewB.setOnClickListener {
            userAnswer = "b"
            if(correctAnswer == userAnswer){

                quizBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                quizBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOFOptions()
            sendData(quizBinding.textViewQuestion.text.toString(),quizBinding.textViewB.text.toString())
        }
        quizBinding.textViewC.setOnClickListener {
            userAnswer = "c"
            if(correctAnswer == userAnswer){

                quizBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                quizBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOFOptions()
            sendData(quizBinding.textViewQuestion.text.toString(),quizBinding.textViewC.text.toString())
        }
        quizBinding.textViewD.setOnClickListener {
            userAnswer = "d"
            if(correctAnswer == userAnswer){

                quizBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text = userCorrect.toString()
            }else{
                quizBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOFOptions()
            sendData(quizBinding.textViewQuestion.text.toString(),quizBinding.textViewC.text.toString())
        }

    }
    private fun gameLogic(){
        //Log.d("myTag3", "This is my message")
        restoreOption()
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                questionCount = snapshot.childrenCount.toInt()
                //createHashSet(questionCount)
                //Log.d("myTag4", "This is my message")
                if(questionNumber < questionsnum.size){
                    question = snapshot.child(questionsnum.elementAt(questionNumber).toString()).child("q").value.toString()
                    answerA = snapshot.child(questionsnum.elementAt(questionNumber).toString()).child("a").value.toString()
                    answerB = snapshot.child(questionsnum.elementAt(questionNumber).toString()).child("b").value.toString()
                    answerC = snapshot.child(questionsnum.elementAt(questionNumber).toString()).child("c").value.toString()
                    answerD = snapshot.child(questionsnum.elementAt(questionNumber).toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questionsnum.elementAt(questionNumber).toString()).child("answer").value.toString()

                    quizBinding.textViewQuestion.text = question
                    quizBinding.textViewA.text = answerA
                    quizBinding.textViewB.text = answerB
                    quizBinding.textViewC.text = answerC
                    quizBinding.textViewD.text = answerD
                    quizBinding.progressBarQuiz.visibility = View.INVISIBLE
                    quizBinding.linearLayoutinfo.visibility = View.VISIBLE
                    quizBinding.linearLayoutQuestion.visibility = View.VISIBLE
                    quizBinding.linearLayoutButtons.visibility = View.VISIBLE

                }else{
                    Toast.makeText(applicationContext,"You have answered all the questions",Toast.LENGTH_SHORT).show()
                }
                questionNumber++

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()

            }
        })

    }

    fun findAnswer(){

        when(correctAnswer){

            "a" -> quizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.textViewD.setBackgroundColor(Color.GREEN)

        }
    }

    fun disableClickableOFOptions(){

        quizBinding.textViewA.isClickable = false
        quizBinding.textViewB.isClickable = false
        quizBinding.textViewC.isClickable = false
        quizBinding.textViewD.isClickable = false

    }

    fun restoreOption(){

        quizBinding.textViewA.setBackgroundColor(Color.WHITE)
        quizBinding.textViewB.setBackgroundColor(Color.WHITE)
        quizBinding.textViewC.setBackgroundColor(Color.WHITE)
        quizBinding.textViewD.setBackgroundColor(Color.WHITE)

        quizBinding.textViewA.isClickable = true
        quizBinding.textViewB.isClickable = true
        quizBinding.textViewC.isClickable = true
        quizBinding.textViewD.isClickable = true

    }

    fun sendData(ques: String,Response : String){


        user?.let{

            val userUID = it.uid
            //responseRef.child(userUID).child("Questions").child("Response").setValue(Response)
            responseRef.child(userUID).child("Questions").child(ques).setValue(Response).addOnSuccessListener {
                Toast.makeText(applicationContext,"User response stored",Toast.LENGTH_SHORT).show()
                //val intent = Intent(this@QuizActivity,MainActivity::class.java)
                //startActivity(intent)
                //finish()

            }
        }

    }

    fun endActivity(){
        val intent = Intent(this@QuizActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    //fun decodeData(correctAnswer : String): String {
        //var out = ""
        //if(correctAnswer == "a"){
          //  out = quizBinding.textViewA.text.toString()
        //}
        //if(correctAnswer == "b"){
          //  out = quizBinding.textViewB.text.toString()
        //}
        //if(correctAnswer == "c"){
          //  out = quizBinding.textViewC.text.toString()
        //}
        //if(correctAnswer == "d"){
          //  out = quizBinding.textViewD.text.toString()
        //}
      //  return out
    //}

    //fun createHashSet(count : Int){
      //  Log.d("flag 1", flag.toString())
        //if(flag == 0){
          //  do{
            //    val number = Random.nextInt(1,count + 1)
              //  Log.d("number",number.toString())
                //questionsnum.add(number)
            //}while(questionsnum.size < 5)
            //flag = 1
        //}
        //Log.d("flag 2", flag.toString())
    //}
}