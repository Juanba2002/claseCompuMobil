package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class Permisos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permisos)
        val ima:ImageButton=findViewById(R.id.imageButton)
        ima.setOnClickListener {
            var intent= Intent(this,Imagen::class.java)
            startActivity(intent)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMARA)
        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMARA) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.CAMERA) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "PERMISO CAMARA DADO", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "PERMISO DE CMARA NO DADO", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    companion object {
        const val CAMARA=1
        }

}
