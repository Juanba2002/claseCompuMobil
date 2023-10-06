package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var numeroSecreto = Random.nextInt(1001)
    private var intentos=0
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text:EditText=findViewById(R.id.editTextText2)
        val spin:Spinner=findViewById(R.id.spinner)
        val but:Button=findViewById(R.id.button2)
        val but2:Button=findViewById(R.id.button3)
        val but3:Button=findViewById(R.id.button4)
        val but4:Button=findViewById(R.id.button5)
        val but5:Button=findViewById(R.id.button)
        val but6:Button=findViewById(R.id.button6)
        val but7:Button=findViewById(R.id.button7)
        val but8:Button=findViewById(R.id.button9)
        val but9:Button=findViewById(R.id.button10)
        val but10:Button=findViewById(R.id.button13)
        val but11:Button=findViewById(R.id.button14)
        val click:TextView=findViewById(R.id.textView8)
        val valor:EditText=findViewById(R.id.editTextText3)
        val porcen:EditText=findViewById(R.id.editTextText4)
        val intento:EditText=findViewById(R.id.editTextText5)
        val ganador:TextView=findViewById(R.id.textView12)
        var clickCount = 0
        spin.onItemSelectedListener=this

        but.setOnClickListener {
            val t=text.text.toString()
            Toast.makeText(this, t, Toast.LENGTH_SHORT).show()
        }
        but2.setOnClickListener {
            val valo=valor.text.toString()
            val p=porcen.text.toString()

            try {
                val num=valo.toFloat()
                val per=p.toFloat()/100
                val resultado=num*per
                Toast.makeText(this, "Resultado: $resultado", Toast.LENGTH_SHORT).show()

            }catch(e: NumberFormatException){
                Toast.makeText(this, "Numero invalido", Toast.LENGTH_SHORT).show()
            }
        }
        but3.setOnClickListener {
            clickCount++
            click.text="Clicks: $clickCount"
        }
        but4.setOnClickListener {
            val i=intento.text.toString()
            val i2=i.toInt()
            intentos++

            if(i2>numeroSecreto){
                ganador.text="Ingresa un numero mas pequeño"
            }else if(i2<numeroSecreto){
                ganador.text="Ingresa un numero mas grande"
            }else{
                ganador.text="Adivinaste, numero intentos: $intentos"
                reiniciar()
            }
        }
        but5.setOnClickListener {
            val selec=spin.selectedItem.toString()
            var intent= Intent(this,Pantalla2::class.java)
            intent.putExtra("texto",selec)
            startActivity(intent)
        }
        but6.setOnClickListener {
            var intent= Intent(this,Lista::class.java)
            startActivity(intent)
        }
        but7.setOnClickListener {
            var intent= Intent(this,Permisos::class.java)
            startActivity(intent)
        }
        but8.setOnClickListener {
            var intent= Intent(this,Localizacion::class.java)
            startActivity(intent)
        }
        but9.setOnClickListener {
            /*var intent= Intent(this,Mapa::class.java)
            startActivity(intent)


             */
        }
        but10.setOnClickListener {
            var intent= Intent(this,FireBase::class.java)
            startActivity(intent)
        }
        but11.setOnClickListener {
            myRef = database.getReference("message")
            myRef.setValue("Hello World")
        }
    }
    fun reiniciar(){
        numeroSecreto = Random.nextInt(1001)
        intentos=0
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedItem = resources.getStringArray(R.array.pais)[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}