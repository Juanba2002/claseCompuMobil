package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import android.widget.Button
import android.location.Location
import kotlin.math.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer
import java.util.Date
import org.json.JSONException


class Localizacion : AppCompatActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var longi: TextView? = null
    private var lati: TextView? = null
    private var dist: TextView? = null
    private var ubicacionFijaLatitud = 12.345 // Reemplaza con la latitud de tu ubicación fija
    private var ubicacionFijaLongitud = 67.890
    private lateinit var mLocationRequest: LocationRequest
    private val JSON_FILE_NAME = "locations.json"
    private var localizaciones = JSONArray()
    private var locationCallback: LocationCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localizacion)
        mLocationRequest = createLocationRequest()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
        longi = findViewById<TextView>(R.id.textView3) as TextView?
        lati = findViewById<TextView>(R.id.textView11) as TextView?
        dist=findViewById<TextView>(R.id.textView13) as TextView?
        val textViewLocations = findViewById<TextView>(R.id.textViewLocations)
        localizaciones = readJSONArrayFromFile(JSON_FILE_NAME)
        val jsonArray = readJSONArrayFromFile(JSON_FILE_NAME)
        textViewLocations.text = "Ubicaciones guardadas:\n${jsonArray.toString(4)}"

        if (ContextCompat.checkSelfPermission(this, fineLocationPermission) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, coarseLocationPermission) != PackageManager.PERMISSION_GRANTED) {

            // Mostrar explicación si es necesario (esto es opcional)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, fineLocationPermission) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, coarseLocationPermission)) {
                // Puedes mostrar una explicación aquí si lo deseas.
            }

            // Solicitar permisos de ubicación
            ActivityCompat.requestPermissions(this, arrayOf(fineLocationPermission, coarseLocationPermission), LOCALIZACION)
        } else {
            obtenerUbicacion()
        }
    }
    private fun obtenerUbicacion() {
        mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            Log.i("LOCATION", "onSuccess location")
            if (location != null) {
                val radius = 6371
                Log.i("LOCATION", "Longitud: " + location.longitude)
                Log.i("LOCATION", "Latitud: " + location.latitude)
                // Haz lo que necesites con la ubicación aquí.
                val lat1Rad = Math.toRadians(location.latitude)
                val lon1Rad = Math.toRadians(location.longitude)
                val lat2Rad = Math.toRadians(ubicacionFijaLatitud)
                val lon2Rad = Math.toRadians(ubicacionFijaLongitud)
                val dLat = lat2Rad - lat1Rad
                val dLon = lon2Rad - lon1Rad
                val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
                val c = radius*(2 * atan2(sqrt(a), sqrt(1 - a)))
                longi?.text=location.longitude.toString()
                dist?.text=c.toString()
                lati?.text=location.latitude.toString()
                writeJSONObject(location)
            }
        }
    }
    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
            .setInterval(10000)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        return locationRequest
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCALIZACION) {
            var fineLocationGranted = false
            var coarseLocationGranted = false
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        fineLocationGranted = true
                    }
                } else if (permission == Manifest.permission.ACCESS_COARSE_LOCATION) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        coarseLocationGranted = true
                    }
                }
            }

            if (fineLocationGranted && coarseLocationGranted) {
                Toast.makeText(this, "PERMISOS DE LOCALIZACIÓN DADOS", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "ALGUNO DE LOS PERMISOS DE LOCALIZACIÓN NO FUE DADO", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun writeJSONObject(location: Location) {
        localizaciones.put(MyLocation(Date(System.currentTimeMillis()), location.latitude, location.longitude).toJSON())
        var output: Writer?
        try {
            val file = File(baseContext.getExternalFilesDir(null), JSON_FILE_NAME)
            output = BufferedWriter(FileWriter(file))
            output.write(localizaciones.toString())
            output.close()
            Toast.makeText(applicationContext, "Location saved", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            // Maneja errores si la escritura falla
        }
    }



    private fun readJSONArrayFromFile(fileName: String): JSONArray {
        val file = File(baseContext.getExternalFilesDir(null), fileName)
        if (!file.exists()) {
            Log.i("LOCATION", "Ubicacion de archivo: $file no encontrado")
            return JSONArray()
        }
        val jsonString = file.readText()
        return JSONArray(jsonString)
    }


    companion object {
        const val LOCALIZACION = 1
    }
    class MyLocation(var fecha: Date, var latitud: Double, var longitud: Double) {
        fun toJSON(): JSONObject {
            val obj = JSONObject()
            try {
                obj.put("latitud", latitud)
                obj.put("longitud", longitud)
                obj.put("date", fecha)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return obj
        }
    }

}