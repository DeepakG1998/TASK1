package com.blogspot.devofandroid.task1

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_floating_widget.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit


//http://apis.nexdha.com:8000/api/test
//parameter name is parameter
//need to give header
//https://johncodeos.com/how-to-parse-json-with-retrofit-converters-using-kotlin/
//Authorization:token  18a753a70886b74099aef9b51f3e4d769ee75e5b
class MainActivity : AppCompatActivity() {

    companion object {
        private const val DRAW_OVERLAYS_PERMISSION_REQUEST_CODE = 666
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_submit.setOnClickListener {
            startFloatingWidgetMaybe()
            rawJSON()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DRAW_OVERLAYS_PERMISSION_REQUEST_CODE && isDrawOverlaysAllowed()) {
            Toast.makeText(this, "Granted permissions for drawing over apps", Toast.LENGTH_SHORT)
                .show()
            startFloatingWidgetMaybe()
        }
    }

    override fun onDestroy() {
        stopService(Intent(this, FloatingWidgetService::class.java))
        Toast.makeText(this, "Stopped floating widget", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    private fun startFloatingWidgetMaybe() {
        if (isDrawOverlaysAllowed()) {
            startService(Intent(this@MainActivity, FloatingWidgetService::class.java))
            return
        }

        requestForDrawingOverAppsPermission()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestForDrawingOverAppsPermission() {
        val intent =
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivityForResult(intent, DRAW_OVERLAYS_PERMISSION_REQUEST_CODE)
    }

    private fun isDrawOverlaysAllowed(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)


    private fun rawJSON() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apis.nexdha.com")
            .build()


        val service = retrofit.create(ApiService::class.java)
        val textEnter = text_enter.text.toString()
        val requestBody = textEnter.toRequestBody("parameter/form-data".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.createEmployee(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val items = response.body()
                    if (items != null) {
                        val fetchedToken = items.toString()
                        last_text.text = fetchedToken
                        Log.e("RETROFIT_PASS", response.code().toString())

                    } else {

                        Log.e("RETROFIT_ERROR", response.code().toString())

                    }

                }

            }

        }


    }
}