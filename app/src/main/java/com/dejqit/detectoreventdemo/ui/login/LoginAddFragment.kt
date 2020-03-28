package com.dejqit.detectoreventdemo.ui.login


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dejqit.detectoreventdemo.R
import com.dejqit.detectoreventdemo.model.ServerContent
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_login_add.*
import java.util.*
import kotlin.collections.ArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class LoginAddFragment : Fragment() {

    private lateinit var model: ArrayList<ServerContent.LoginServerItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = arguments?.getSerializable("viewModel") as ArrayList<ServerContent.LoginServerItem>

        activity?.title = "Add new server"
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_login_add, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_dialog_add_server, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_button_delete -> true
            R.id.menu_button_save -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // Server Name and URL validation
        val serverNameValid = getTextEditObservable(serverName?.editText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { s -> s.isNotBlank() }
            .doOnNext {if(it) serverName?.error = "" else serverName?.error = getString(R.string.error_field_empty)}
            .distinctUntilChanged()

        val serverUrlVaild = getTextEditObservable(serverUrl?.editText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { s -> Patterns.WEB_URL.matcher(s).matches() }
            .doOnNext {if(it) serverName?.error = "" else serverName?.error = getString(R.string.error_field_url_invalid)}
            .distinctUntilChanged()

        val tryConnectButtonEnable = Observable.zip(serverNameValid, serverUrlVaild, BiFunction { b1, b2 -> b1 && b2})
            .distinctUntilChanged()

        tryConnectButtonEnable
            .subscribe {
                Log.i("TEST", "FIRE")
            }

//        tryConnectButton.enab

//            tryConnectButton.setOnClickListener {}
        }

    private fun getTextEditObservable(textEdit: EditText?): Observable<String> {
        return Observable.create<String> { emitter ->
            val watcher = object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    emitter.onNext(s.toString())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }
            textEdit?.addTextChangedListener(watcher)
            emitter.setCancellable {
                textEdit?.removeTextChangedListener(watcher)
            }
        }
    }

    fun addServerItem() {
//        val serverName = this.serverName.text.toString()
//        val url = this.serverUrl.text.toString()
//        val username = this.userName.text.toString()
//        val password = this.password.text.toString()
//        var description = ""
//
//        val item = ServerContent.LoginServerItem(
//            name = serverName,
//            base_url = url,
//            username = username,
//            password = password,
//            description = description
//        )
//
//
//
//        model.add(item)
    }

}