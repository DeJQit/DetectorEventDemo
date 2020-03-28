package com.dejqit.detectoreventdemo.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.dejqit.detectoreventdemo.R
import com.dejqit.detectoreventdemo.adapter.LoginListAdapter
import com.dejqit.detectoreventdemo.model.ServerContent
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var adapter: LoginListAdapter
    private var model = ArrayList<ServerContent.LoginServerItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = LoginListAdapter(model)
        activity?.title = getString(R.string.app_name)

        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Setup adapter
        serverList.layoutManager = LinearLayoutManager(activity)
        serverList.adapter = adapter

        // On click handler
        addServerButton.setOnClickListener(Navigation.createNavigateOnClickListener(
                R.id.action_loginFragment_to_loginAddFragment,
                bundleOf(Pair("viewModel", model))
        ))


    }


}
