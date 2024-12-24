package com.example.dz16contentprovider

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dz16contentprovider.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import android.Manifest
import android.util.Log
import androidx.core.os.bundleOf

class MainActivity : AppCompatActivity(),MessengerContact,CallContact {
    private lateinit var binding: ActivityMainBinding
    private var contactModelList: MutableList<ContactModel>? = null
    private var scrollPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Тулбар
        var toolbarMain = binding.toolbarMain
        setSupportActionBar(toolbarMain)
        title = " Мои контакты"
        toolbarMain.subtitle = "  Версия 1.Главная страница"
        toolbarMain.setLogo(R.drawable.pleer)

        val layoutManager = LinearLayoutManager(this)
        binding.contactsRv.layoutManager = layoutManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionContacts.launch(Manifest.permission.READ_CONTACTS)
        } else {
            getContact()
        }
    }

    @SuppressLint("Range")
    private fun getContact() {
        contactModelList = mutableListOf()
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phone =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contactModel = ContactModel(name, phone)
            Log.d("Con", "getContact: $contactModel")
            contactModelList?.add(contactModel)
        }
        phones.close()
        binding.contactsRv.adapter = ContactAdapter(contactModelList!!)
    }

    private val permissionContacts = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    )
    { isGranted ->
        if (isGranted) {
            Snackbar.make(
                binding.root,
                "Разрешение на чтение контактов предоставлено.",
                Snackbar.LENGTH_LONG
            ).show()
            getContact()
        } else {
            Snackbar.make(binding.root, "В разрешениях отказано.", Snackbar.LENGTH_LONG)
                .setTextColor(Color.RED)
                .show()
        }
    }

    private val permissionPhoneCallSMS = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    )
    { isGranted ->
        if (isGranted) {
            Snackbar.make(binding.root, "Разрешения предоставлены.", Snackbar.LENGTH_LONG).show()
            getContact()
        } else {
            Snackbar.make(binding.root, "В разрешениях отказано.", Snackbar.LENGTH_LONG)
                .setTextColor(Color.RED)
                .show()
        }
    }


    override fun call(phone: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionPhoneCallSMS.launch(Manifest.permission.CALL_PHONE)
        } else {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phone")
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        scrollPosition = (binding.contactsRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onResume() {
        super.onResume()
        (binding.contactsRv.layoutManager as LinearLayoutManager).scrollToPosition(scrollPosition)
    }


    override fun createMessage(phone: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionPhoneCallSMS.launch(Manifest.permission.SEND_SMS)
        } else {
            val intent = Intent(this@MainActivity, MessengerActivity::class.java)
            intent.putExtras(bundleOf("phone" to phone))
            startActivity(intent)
        }
    }


    //Меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.infoMenuMain -> {
                Toast.makeText(applicationContext, "Автор Ефремов О.В. Создан 24.12.2024",
                    Toast.LENGTH_LONG).show()
            }
            R.id.exitMenuMain ->{
                Toast.makeText(applicationContext, "Работа приложения завершена",
                    Toast.LENGTH_LONG).show()
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}