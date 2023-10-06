package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.content.Intent
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class Imagen : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagen)
        val but:Button=findViewById(R.id.button8)
        but.setOnClickListener {
            openGallery()
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data

            // Carga la imagen seleccionada en el ImageView
            val imageView: ImageView = findViewById(R.id.imageView2)
            imageView.setImageURI(selectedImageUri)
        }
    }
}
