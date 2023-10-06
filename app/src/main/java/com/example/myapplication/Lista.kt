package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import org.json.JSONArray
import java.io.IOException
import org.json.JSONObject
import java.io.InputStream

class Lista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        val json = JSONObject(loadJASONFromAsset())
        val paisesJSONArray=json.getJSONArray("paises")
        var paisnombre= ArrayList<String>()
        val l:ListView=findViewById(R.id.Lista1)
        for (i in 0  until  paisesJSONArray.length()){
            val jsonObject = paisesJSONArray.getJSONObject(i)
            val nombrePais = jsonObject.getString("nombre_pais")
            paisnombre.add(nombrePais)
        }
        val adapter1= ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,paisnombre)
        l.adapter=adapter1
            /*
        Cada elemento de la lista se vuelve un boton que va a llevar a otra pantalla y se obtiene
        el dato del boton que se presiono para dar la info del pais

        list.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val paisElegido = parent.getItemAtPosition(position) as String
            val intent = Intent(this, Info::class.java)
            intent.putExtra("paisElegido", paisElegido)
            startActivity(intent)
        }
             */

    }
    fun loadJASONFromAsset(): String? {
        var json: String? = null
        try {
            val istream: InputStream = assets.open("paises.json")
            val size:Int = istream.available()
            val buffer = ByteArray(size)
            istream.read(buffer)
            istream.close()
            json = buffer.toString(Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}