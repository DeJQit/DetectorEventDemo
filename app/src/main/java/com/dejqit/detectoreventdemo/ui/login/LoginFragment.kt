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
import com.dejqit.detectoreventdemo.databinding.LoginFragmentBinding
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

//    private lateinit var viewModel: LoginViewModel
    private lateinit var fragmentLoginBinding: LoginFragmentBinding
    private lateinit var adapter: LoginListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel =
        fragmentLoginBinding  = LoginFragmentBinding.inflate(inflater, container, false).apply {
//            loginViewModel = ViewModelProviders.of(this@LoginFragment).get(LoginViewModel::class.java)
            loginViewModel = LoginViewModel()
            lifecycleOwner = viewLifecycleOwner
        }
//        return  fragmentLoginBinding.root
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = fragmentLoginBinding.loginViewModel!!

        viewModel.fetchOneItem()

        // Setup adapter
        serverList.layoutManager = LinearLayoutManager(activity)
        serverList.adapter = LoginListAdapter(viewModel)

        // Setup model observer
        viewModel.serverList.observe(viewLifecycleOwner, Observer {
            adapter.updateServerList(it)
        })

        // On click handler
        addServerButton.setOnClickListener(Navigation.createNavigateOnClickListener(
                R.id.action_loginFragment_to_loginAddFragment,
                bundleOf(Pair("viewModel"," viewModel"))
        ))


    }


}
