package com.dejqit.detectoreventdemo.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dejqit.detectoreventdemo.R
import com.dejqit.detectoreventdemo.model.ServerContent
import kotlinx.android.synthetic.main.fragment_login_add.*

class LoginAddFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        saveButton.setOnClickListener {
            //            val serverName = it.findViewById<EditText>(R.id.serverName).toString()
//            val url = it.findViewById<EditText>(R.id.serverUrl).toString()
//            val username = it.findViewById<EditText>(R.id.userName).toString()
//            val password = it.findViewById<EditText>(R.id.password).toString()

            val item = ServerContent.LoginServerItem(name = "serverName",
                    base_url = "url",
                    username = "username",
                    password = "password",
                    description = ""
                    )

             arguments?.putString("addItem", "OK")
            it.findNavController().navigateUp()
        }
    }
}
