package co.edu.upb.mimercado.data

import java.security.MessageDigest
import java.util.UUID

object Passwords {
    fun newSalt(): String = UUID.randomUUID().toString()

    fun hash(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest("$salt:$password".toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun matches(password: String, salt: String, expectedHash: String): Boolean {
        return hash(password, salt) == expectedHash
    }
}

object Catalog {
    val units = listOf("Unidades", "kg", "g", "L")
    val categories = listOf(
        "Frutas y verduras",
        "Carnes",
        "Lácteos",
        "Panadería",
        "Bebidas",
        "Aseo",
        "Hogar",
        "Otros",
    )
    val icons = listOf(
        "cart" to "Carrito",
        "home" to "Hogar",
        "clean" to "Limpieza",
        "food" to "Comida",
    )
    val colors = listOf(
        "#2F7D4B",
        "#F2B84B",
        "#4A90D9",
        "#D9534F",
        "#9B59B6",
        "#E67E22",
    )

    fun emoji(category: String): String = when (category) {
        "Frutas y verduras" -> "🥦"
        "Carnes" -> "🥩"
        "Lácteos" -> "🥛"
        "Panadería" -> "🍞"
        "Bebidas" -> "🧃"
        "Aseo" -> "🧴"
        "Hogar" -> "🏠"
        else -> "📦"
    }
}
