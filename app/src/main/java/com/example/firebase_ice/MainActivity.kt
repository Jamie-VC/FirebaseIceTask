package com.example.firebase_ice

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity()
{
    private lateinit var rootNode: FirebaseDatabase
    private lateinit var userreference: DatabaseReference
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))
        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listView = findViewById(R.id.lsOutput)
        rootNode= FirebaseDatabase.getInstance()
        userreference = rootNode.getReference("users")

        val dc =DataClass("Jamie", "Pizza")
        userreference.child(dc.name.toString()).setValue(dc)

        val list = ArrayList<String>()
        val adapter = ArrayAdapter<String>(this,R.layout.listitems,list)
        listView.adapter=adapter

        userreference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                list.clear()
                for(snapshot1 in snapshot.children)
                {
                    val dc2 = snapshot1.getValue(DataClass::class.java)
                    val txt = "Name is ${dc2?.name}, Favourite Food: ${dc2?.FavouriteFood}"
                    txt?.let { list.add(it)}
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError)
            {

            }
        })

    }
}