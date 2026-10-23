package com.example.exp4

import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.exp4.databinding.ActivityInfoEditBinding

/**
 * 信息修改 Activity：修改玩家个人信息（仅保存在内存，不写入文件）
 */
class InfoEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.title_edit_info)

        fillWithCurrentInfo()

        binding.btnSave.setOnClickListener { saveInfo() }
        binding.btnCancel.setOnClickListener { finish() }
    }

    /** 用当前玩家信息回填表单 */
    private fun fillWithCurrentInfo() {
        val info = PlayerStore.info
        binding.etName.setText(info.name)
        binding.etStudentId.setText(info.studentId)
        binding.etPhone.setText(info.phone)
        binding.etEmail.setText(info.email)
        if (info.gender == getString(R.string.gender_female)) {
            binding.rbFemale.isChecked = true
        } else {
            binding.rbMale.isChecked = true
        }
        setChecked(binding.cbTravel, info.hobbies.contains(getString(R.string.hobby_travel)))
        setChecked(binding.cbReading, info.hobbies.contains(getString(R.string.hobby_reading)))
        setChecked(binding.cbGame, info.hobbies.contains(getString(R.string.hobby_game)))
        setChecked(binding.cbShopping, info.hobbies.contains(getString(R.string.hobby_shopping)))
    }

    @Suppress("ClickableViewAccessibility")
    private fun setChecked(box: CheckBox, checked: Boolean) {
        box.isChecked = checked
    }

    /** 保存修改后的信息到内存仓库并返回主界面 */
    private fun saveInfo() {
        val info = PlayerStore.info
        info.name = binding.etName.text.toString().trim()
        info.studentId = binding.etStudentId.text.toString().trim()
        info.phone = binding.etPhone.text.toString().trim()
        info.email = binding.etEmail.text.toString().trim()
        info.gender = if (binding.rbFemale.isChecked) {
            getString(R.string.gender_female)
        } else {
            getString(R.string.gender_male)
        }
        val hobbies = mutableListOf<String>()
        if (binding.cbTravel.isChecked) hobbies.add(getString(R.string.hobby_travel))
        if (binding.cbReading.isChecked) hobbies.add(getString(R.string.hobby_reading))
        if (binding.cbGame.isChecked) hobbies.add(getString(R.string.hobby_game))
        if (binding.cbShopping.isChecked) hobbies.add(getString(R.string.hobby_shopping))
        info.hobbies = hobbies

        Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show()
        finish()
    }
}
