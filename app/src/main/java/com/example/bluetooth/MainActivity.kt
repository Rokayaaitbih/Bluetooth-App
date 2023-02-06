package com.example.bluetooth

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import com.example.bluetooth.R
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.example.bluetooth.MainActivity
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       val  mStatusBlueTv = findViewById<TextView>(R.id.statusBluetoothTv)
        val mPairedTv = findViewById<TextView>(R.id.pairedTv)
        val mBlueIv = findViewById<ImageView>(R.id.bluetoothIv)
        val mOnBtn = findViewById<Button>(R.id.onBtn)
        val mOffBtn = findViewById<Button>(R.id.offBtn)
        val mDiscoverBtn = findViewById<Button>(R.id.discoverableBtn)
        val mPairedBtn = findViewById<Button>(R.id.pairedBtn)

       val mBlueAdapter = BluetoothAdapter.getDefaultAdapter()


        if (mBlueAdapter == null) {
            mStatusBlueTv.setText("Bluetooth is not available")
        } else {
            mStatusBlueTv.setText("Bluetooth is available")
        }


        if (mBlueAdapter.isEnabled()) {
            mBlueIv.setImageResource(R.drawable.ic_action_on)
        } else {
            mBlueIv.setImageResource(R.drawable.ic_action_off)
        }


        mOnBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (!mBlueAdapter.isEnabled()) {
                    showToast("Turning On Bluetooth...")

                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    startActivityForResult(intent, REQUEST_ENABLE_BT)
                } else {
                    showToast("Bluetooth is already on")
                }
            }
        })

        mDiscoverBtn.setOnClickListener(View.OnClickListener {
            if (!mBlueAdapter.isDiscovering()) {
                showToast("Making Your Device Discoverable")
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                startActivityForResult(intent, REQUEST_DISCOVER_BT)
            }
        })

        mOffBtn.setOnClickListener(View.OnClickListener {
            if (mBlueAdapter.isEnabled()) {
                mBlueAdapter.disable()
                showToast("Turning Bluetooth Off")
                mBlueIv.setImageResource(R.drawable.ic_action_off)
            } else {
                showToast("Bluetooth is already off")
            }
        })

        mPairedBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (mBlueAdapter.isEnabled()) {
                    mPairedTv.setText("Paired Devices")
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    val devices = mBlueAdapter.getBondedDevices()
                    for (device in devices) {
                        mPairedTv.append(
                            """
    
    Device: ${device.name}, $device
    """.trimIndent()
                        )
                    }
                } else {

                    showToast("Turn on bluetooth to get paired devices")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ENABLE_BT -> if (resultCode == RESULT_OK) {

                mBlueIv!!.setImageResource(R.drawable.ic_action_on)
                showToast("Bluetooth is on")
            } else {

                showToast("could't on bluetooth")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 0
        private const val REQUEST_DISCOVER_BT = 1
    }
}