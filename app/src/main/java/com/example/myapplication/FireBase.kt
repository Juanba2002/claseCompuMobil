package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FireBase : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private lateinit var email: EditText
    private lateinit var password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base)
        val but:Button=findViewById(R.id.button11)
        auth = Firebase.auth
        email = findViewById(R.id.editTextTextEmailAddress)
        password = findViewById(R.id.editTextTextPassword)
        but.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            signInUser(emailText,passwordText)
        }

    }
    private fun signInUser(email: String, password: String){
        if(validateForm()){
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
// Sign in success, update UI
                        Log.d(TAG, "signInWithEmail:success:")
                        val user = auth.currentUser
                        updateUI(auth.currentUser)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
    }
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, Autenticado::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)
        } else {
            email.setText("")
            password.setText("")
        }
    }
    private fun validateForm(): Boolean {
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        var valid = true
        if (TextUtils.isEmpty(emailText)) {
            email.error="Required."
            valid = false
        } else {
            email.error= null
        }
        if (TextUtils.isEmpty(passwordText)) {
            password.error= "Required."
            valid = false
        } else {
            password.error = null
        }
        return valid
    }
    companion object {
        private const val TAG = "FireBase"
    }
}