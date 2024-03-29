package me.digi.testimplementation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import me.digi.sdk.DMEPullClient
import me.digi.sdk.utilities.crypto.DMECryptoUtilities
import me.digi.sdk.entities.DMEPullClientConfiguration
import me.digi.sdk.interapp.DMEAppCommunicator
import kotlinx.android.synthetic.main.activity_main.*
import me.digi.sdk.DMEPushClient
import me.digi.sdk.entities.DMEPushClientConfiguration
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    lateinit var pushClient: DMEPushClient
    lateinit var client: DMEPullClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        doPB()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        DMEAppCommunicator.getSharedInstance().onActivityResult(requestCode, resultCode, data)
    }

    fun doPB() {
        val pk = DMECryptoUtilities(applicationContext).privateKeyHexFrom(
            applicationContext.getString(R.string.digime_p12_filename),
            applicationContext.getString(R.string.digime_p12_password)
        )
        val cfg = DMEPushClientConfiguration(
            applicationContext.getString(R.string.digime_application_id),
            applicationContext.getString(R.string.digime_contract_id)
        )
        cfg.baseUrl = "https://api.test06.devdigi.me/"
        pushClient = DMEPushClient(applicationContext, cfg)

        launchBtn.setOnClickListener {
            pushClient.createPostbox(this) { dmePostbox, error ->
                if (dmePostbox != null) {
                    Log.i("DME", "Postbox Created: $dmePostbox")
                }
                else {
                    Log.i("DME", "Postbox Create Error: $error")
                }
            }
        }
    }

    fun doCA() {
        val pk = DMECryptoUtilities(applicationContext).privateKeyHexFrom(
            applicationContext.getString(R.string.digime_p12_filename),
            applicationContext.getString(R.string.digime_p12_password)
        )
        val cfg = DMEPullClientConfiguration(
            applicationContext.getString(R.string.digime_application_id),
            applicationContext.getString(R.string.digime_contract_id),
            pk
        )
        client = DMEPullClient(applicationContext, cfg)

        launchBtn.setOnClickListener {
            client.authorize(this) { session, error ->
                session?.let {
                    client.getFileList { fileIds, error ->

                        fileIds?.orEmpty()?.forEach {
                            client.getSessionData(it) { file, error ->

                                if (file != null) {
                                    Log.i("DME", "File Received: ${String(file.content, StandardCharsets.UTF_8)}")
                                }
                                else {
                                    Log.i("DME", "File Download Error: $error")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
