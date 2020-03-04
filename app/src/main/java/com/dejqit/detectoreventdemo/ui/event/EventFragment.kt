package com.dejqit.detectoreventdemo.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dejqit.detectoreventdemo.adapter.EventListAdapter
import com.dejqit.detectoreventdemo.databinding.FragmentEventBinding
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : Fragment() {

    //private lateinit var eventViewModel: EventViewModel
    private lateinit var fragmentEventBinding: FragmentEventBinding
    private lateinit var adapter: EventListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentEventBinding = FragmentEventBinding.inflate(inflater, container, false).apply {
            eventModelView = ViewModelProviders.of(this@EventFragment).get(EventViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return fragmentEventBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentEventBinding.eventModelView?.fetchEventList()

        // Setup adapter
        adapter = EventListAdapter(fragmentEventBinding.eventModelView!!)
        val layoutManager = LinearLayoutManager(activity)
        list.layoutManager = layoutManager
        list.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))
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
