package com.example.exp4

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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

    /** 创建选项菜单（“关于”项） */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /** 点击菜单后弹出关于对话框 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                showAboutDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog() {
        val message = resources.getStringArray(R.array.about_message_lines).joinToString("\n")
        AlertDialog.Builder(this)
            .setTitle(R.string.about_title)
            .setMessage(message)
            .setPositiveButton(R.string.btn_ok, null)
            .show()
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
