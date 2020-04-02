package com.dejqit.detectoreventdemo.ui.login


import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dejqit.detectoreventdemo.R
import com.dejqit.detectoreventdemo.api.EventClient
import com.dejqit.detectoreventdemo.model.ServerContent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login_add.*

class LoginAddFragment : Fragment() {

    private lateinit var menu: Menu
    private lateinit var deleteDialog: AlertDialog
    private var modelIndex: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        modelIndex = arguments?.getInt("serverListModelIndex", -1)!!

        if(modelIndex < 0)
            activity?.title = "Add new server"
        else
            activity?.title = "Edit server"

        setHasOptionsMenu(true)

        activity?.let {
            val deleteDlg = AlertDialog.Builder(it)
                .setMessage("Delete this server?")
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    EventClient.ServerStorage.deleteServer(modelIndex)
                    findNavController().navigateUp()
                }
                .setNegativeButton(android.R.string.no) { _,_ -> }
            deleteDialog = deleteDlg.create()
        }

        return inflater.inflate(R.layout.fragment_login_add, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu!!

        inflater?.inflate(R.menu.menu_dialog_add_server, menu)

        menu.findItem(R.id.menu_button_save)?.isEnabled = false

        // When add mode, hide delete action
        if(modelIndex < 0) {
            menu.findItem(R.id.menu_button_delete)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_button_delete -> { onButtonDelete(); true }
            R.id.menu_button_save -> { onButtonSave(); true }
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
            .doOnNext { serverName?.error = if(it) "" else  getString(R.string.error_field_empty) }
            .distinctUntilChanged()

        val serverUrlValid = getTextEditObservable(serverUrl?.editText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { s -> Patterns.WEB_URL.matcher(s).matches() }
            .doOnNext { serverUrl?.error = if(it)  "" else  getString(R.string.error_field_url_invalid) }
            .distinctUntilChanged()

        val tryConnectButtonEnable = Observable.combineLatest(serverNameValid, serverUrlValid, BiFunction<Boolean?, Boolean?, Boolean> { b1, b2 -> b2 && b1 })
            .distinctUntilChanged()

        val d= tryConnectButtonEnable.subscribe {
                tryConnectButton.isEnabled = it
                menu.findItem(R.id.menu_button_save)?.isEnabled = it
            }

        tryConnectButton.setOnClickListener {
            serverConnectionLayout.visibility = View.VISIBLE
            serverConnectionProgressBar.visibility = View.VISIBLE
            serverConnectionLabel.text = getString(R.string.string_loading)

            try {
                val client = EventClient.createClient(getServerItem())
                ServerContent.getServerVersion(client) { isSuccess, version, error ->
                    serverConnectionProgressBar.visibility = View.INVISIBLE
                    if(isSuccess) {
                        serverConnectionLabel.text = version.version
                    } else {
                        serverConnectionLabel.text  = "Error: $error"
                    }
                }
            } catch (e: Exception) {
                serverConnectionProgressBar.visibility = View.INVISIBLE
                serverConnectionLabel.text  = "Error: $e"
            }
        }

        // When index is not negative, then edit fields
        if(modelIndex >= 0) {
            serverName?.editText?.setText(EventClient.ServerStorage.getServer(modelIndex).name)
            serverUrl?.editText?.setText(EventClient.ServerStorage.getServer(modelIndex).base_url)
            userName?.editText?.setText(EventClient.ServerStorage.getServer(modelIndex).username)
        }

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

    private fun getServerItem(): EventClient.LoginServerItem {
        val serverName = serverName.editText?.text.toString()
        val url = serverUrl.editText?.text.toString()
        val username = userName.editText?.text.toString()
        val password = password.editText?.text.toString()

        return EventClient.LoginServerItem(
            name = serverName,
            base_url = url,
            username = username,
            password = password
        )
    }

    private fun onButtonSave() {

        if(modelIndex >= 0)
            EventClient.ServerStorage.replaceServer(modelIndex, getServerItem())
        else
            EventClient.ServerStorage.addServer(getServerItem())

        findNavController().navigateUp()
    }

    private fun onButtonDelete() {
        deleteDialog.show()
    }

}