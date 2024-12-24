package com.example.dz16contentprovider

import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dz16contentprovider.databinding.ActivityMessengerBinding

class MessengerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessengerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Тулбар
        var toolbarMain = binding.toolbarMessAct
        setSupportActionBar(toolbarMain)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = " Мои контакты"
        toolbarMain.subtitle = "  Страница отправки сообщения"
        toolbarMain.setLogo(R.drawable.pleer)

// ОБработка кнопки отправить
        val phone = intent.extras?.getString("phone")
        binding.addresseeTv.text = "${binding.addresseeTv.text} $phone"
        binding.sendBtn.setOnClickListener {
            try {
                val smsManager: SmsManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    smsManager = applicationContext.getSystemService<SmsManager>(SmsManager::class.java)
                } else {
                    smsManager = SmsManager.getDefault()
                }
                smsManager.sendTextMessage(phone,
                    null,
                    binding.textEt.text.toString(),
                    null,
                    null)
                Toast.makeText(applicationContext, "Сообщение отправлено", Toast.LENGTH_LONG).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Пожалуйста, введите все данные..."+e.message.toString(),
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}




