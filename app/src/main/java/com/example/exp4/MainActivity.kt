package com.example.exp4

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.exp4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnEditInfo.setOnClickListener {
            startActivity(Intent(this, InfoEditActivity::class.java))
        }
        binding.btnCardGame.setOnClickListener {
            startActivity(Intent(this, CardGameActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        displayPlayerInfo()
    }

    /** 从内存仓库读取玩家信息并刷新界面 */
    private fun displayPlayerInfo() {
        val info = PlayerStore.info
        title = "${info.name}+${info.studentId}"
        binding.tvName.text = info.name
        binding.tvGender.text = info.gender
        binding.tvStudentId.text = info.studentId
        binding.tvPhone.text = info.phone
        binding.tvEmail.text = info.email
        binding.tvHobbies.text = info.hobbies.joinToString("、")
        binding.tvHighScore.text = PlayerStore.highScore.toString()
    }
}
