package com.dejqit.detectoreventdemo.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dejqit.detectoreventdemo.adapter.EventListAdapter
import com.dejqit.detectoreventdemo.databinding.FragmentEventBinding
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : Fragment() {

    //private lateinit var eventViewModel: EventViewModel
    private var serverId: Int = 0
    private lateinit var fragmentEventBinding: FragmentEventBinding
    private lateinit var adapter: EventListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        serverId = arguments?.getInt("selectedServer")!!

        fragmentEventBinding = FragmentEventBinding.inflate(inflater, container, false).apply {
            val model = ViewModelProviders.of(this@EventFragment).get(EventViewModel::class.java)
            model.serverId = serverId
            eventModelView = model
            lifecycleOwner = viewLifecycleOwner
        }


        return fragmentEventBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentEventBinding.eventModelView?.fetchEventList()

        // Setup adapter
        adapter = EventListAdapter(fragmentEventBinding.eventModelView!!, serverId)
        val layoutManager = LinearLayoutManager(activity)
        list.layoutManager = layoutManager
//        list.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))
        list.adapter = adapter

        // Setup observes
        fragmentEventBinding.eventModelView?.listEvent?.observe(viewLifecycleOwner, Observer {
            adapter.updateEventList(it)
        })
//        fragmentEventBinding.eventModelView?.text?.observe(viewLifecycleOwner, Observer {
//            activity?.message = it
//        })
    }

}
