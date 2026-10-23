package com.example.exp4

/**
 * 玩家基本信息（内存保存，无需持久化到文件）
 */
data class PlayerInfo(
    var name: String,
    var gender: String,
    var studentId: String,
    var phone: String,
    var email: String,
    var hobbies: MutableList<String>
)

/**
 * 全局内存数据仓库：保存玩家信息和游戏最高分，
 * 供 MainActivity / InfoEditActivity / CardGameActivity 之间共享。
 */
object PlayerStore {
    val info = PlayerInfo(
        name = "张三",
        gender = "男",
        studentId = "2023010101",
        phone = "13800000000",
        email = "zhangsan@example.com",
        hobbies = mutableListOf("旅游", "游戏")
    )

    var highScore = 0

    /** 更新最高分（只保留历史最高） */
    fun submitScore(score: Int) {
        if (score > highScore) {
            highScore = score
        }
    }
}
