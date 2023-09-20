package com.aait.getak.ui.activities.core

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.chat_model.ChatModel
import com.aait.getak.models.chat_model.Data
import com.aait.getak.models.chat_model.Message
import com.aait.getak.network.remote_db.SocketConnection
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.item_bottom_chat.*
import kotlinx.android.synthetic.main.toolbar_normal.*
import org.json.JSONObject

class ChatActivity : ParentActivity() {


    private fun setActions() {
        iv_back.setOnClickListener {
            onBackPressed()
        }
        iv_send.setOnClickListener {
            if (editTextMessage.text.isNotBlank()){
                val msg=Message(content = editTextMessage.text.toString(),sender = "you",user_id = myId,type = "text",date = "الآن")
                allMsgs.add(msg)
                mAdapter.swapData(allMsgs)
                recycler_msg.scrollToPosition(allMsgs.lastIndex)
                Log.e("msgs",allMsgs.toString())
                emitSocketMessage("text",editTextMessage.text.toString())
                editTextMessage.text.clear()
            }
        }
    }

    private fun emitSocketMessage(type:String,msg:String?=null,file:String?=null) {
        Log.e("socket","send")
        val jsonObject = JSONObject()
        jsonObject.put("sender_id", myId.toString())
        jsonObject.put("receiver_id", mData?.seconduser!!.id.toString())
        jsonObject.put("conversation_id", conv_id)
        jsonObject.put("time_zone", mPrefs?.user?.time_zone)
        jsonObject.put("content", msg ?: file)
        jsonObject.put("type", type)
        Log.e("addUser","added")
        Log.e("msgs",jsonObject.toString())
        SocketConnection.addEvent("sendmessage",jsonObject)
    }

    private fun connectSocket() {
        SocketConnection.mSocket.let {
            it!!.connect()
                .on("message") {data->
                    if (SocketConnection.mSocket!!.connected()) {
                        Log.e("sig",data.toString())
                        val json = data[0]
                        val jsonData=json as JSONObject
                        Log.e("sig",jsonData.toString())
                        conv_id=jsonData["conversation_id"].toString().toInt()
                        val sender_id=jsonData["sender_id"].toString().toInt()
                        var content=jsonData["content"].toString()
                        val type=jsonData["type"].toString()

                        val msg=Message(mData?.seconduser!!.avatar,content,"now","true","seconduser",type,sender_id,mData?.seconduser!!.name)
                        allMsgs.add(msg)
                        runOnUiThread {
                            Runnable {
                            }.run{
                                mAdapter.swapData(allMsgs)
                                recycler_msg.scrollToPosition(allMsgs.lastIndex)

                            }
                        }

                    }

                }
        }
    }

    private fun sendRequest() {
        repo.chat(conv_id.toString(),"Bearer "+mPrefs?.user!!.token!!).subscribeWithLoading({showProgressDialog()},{handleChat(it)},{handleError(it.message)},{})
    }

    private fun handleError(message: String?) {
        hideProgressDialog()
        toasty(message!!,2)
    }

    private fun handleChat(response: ChatModel) {
            hideProgressDialog()
            if (response.value=="1"){
                mData = response.data
                allMsgs=mData?.messages as MutableList<Message>
                mAdapter.swapData(allMsgs)
                recycler_msg.scrollToPosition(allMsgs.lastIndex)
        }
    }

    private fun setRec() {
        recycler_msg.layoutManager= LinearLayoutManager(this)
        recycler_msg.adapter=mAdapter
        recycler_msg.setHasFixedSize(true)
        recycler_msg.setItemViewCacheSize(20)
        recycler_msg.isDrawingCacheEnabled = true
        recycler_msg.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

    }

    private var mData: Data?=null
    private var myId: Int=0
    private var conv_id: Int=0
    private val mAdapter = ChatAdapter()
    private  var allMsgs: MutableList<Message> = mutableListOf()

    override fun onStart() {
        super.onStart()
        addSocketConnection()
    }
    override val layoutResource: Int
        get() = R.layout.activity_chat

    override fun onStop() {
        super.onStop()
        SocketConnection.cancelEvent("addUser")
        SocketConnection.cancelEvent("message")
        SocketConnection.addEvent("exitChat",JSONObject())
        SocketConnection.onClose()

    }

    override fun init() {
        title_toolbar.text=getText(R.string.chat)
        conv_id=intent.getIntExtra("conv_id",0)
        setRec()
        sendRequest()
        connectSocket()
        setActions()
    }

    private fun addSocketConnection() {
        myId=mPrefs!!.user?.id!!.toInt()
        val jsonObject = JSONObject()
        jsonObject.put("conversation", conv_id)
        jsonObject.put("client", myId.toString())
        Log.e("addUser","added")
        SocketConnection.addEvent("adduser",jsonObject)
    }

}