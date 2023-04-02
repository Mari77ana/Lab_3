package com.example.lab_3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lab_3.databinding.FragmentRegisterBinding
import com.google.firebase.database.*

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var db : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater,container,false)
        val view = binding.root



        db = FirebaseDatabase
            .getInstance("https://lab-3-8ee48-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users") // är en rot som man pushar till "users", USER-TABLE, som @Entity i Room
        val btnRegisterUser = binding.btnRegisterUser
        val etRegisterUsername = binding.btnRegisterUser
        val etRegisterUserPassword = binding.etRegisterUserPassword


        btnRegisterUser.setOnClickListener {
            val regUsername = etRegisterUsername.text.toString()
            val regUserPassword = etRegisterUserPassword.text.toString()
            val newUser = User(regUsername,regUserPassword)

            if (regUsername.isNotEmpty() && regUserPassword.isNotEmpty()){
                db.orderByChild("regUsername").equalTo(regUsername)
                    .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                       if(snapshot.exists()){
                           Toast.makeText(context,"Username already exist!", Toast.LENGTH_LONG).show()
                       }
                        else{
                            db.push()
                                .setValue(newUser)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Succeeded", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context,"Something went wrong $it", Toast.LENGTH_LONG).show()
                        // it står för exception
                    }

                })

            }

            else{
                Toast.makeText(context,"Fill in all fields please!", Toast.LENGTH_LONG).show()
            }

        }



        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}