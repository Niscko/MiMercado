package co.edu.upb.mimercado.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)],
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val salt: String,
)

@Entity(
    tableName = "lists",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("userId")],
)
data class ListEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val name: String,
    val icon: String,
    val color: String,
    val updatedAt: Long,
)

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = ListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("listId")],
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val listId: Long,
    val name: String,
    val quantity: Int,
    val unit: String,
    val category: String,
    val note: String,
    val priority: String,
    val bought: Boolean,
)

data class Product(
    val id: Long,
    val name: String,
    val quantity: Int,
    val unit: String,
    val category: String,
    val note: String,
    val priority: String,
    val bought: Boolean,
)

data class ShoppingList(
    val id: Long,
    val name: String,
    val icon: String,
    val color: String,
    val updatedAt: Long,
    val products: List<Product>,
) {
    val boughtCount: Int get() = products.count { it.bought }
    val totalCount: Int get() = products.size
    val progress: Float
        get() = if (totalCount == 0) 0f else boughtCount.toFloat() / totalCount
    val completed: Boolean get() = totalCount > 0 && boughtCount == totalCount
}

data class Settings(
    val showBoughtProducts: Boolean = true,
    val confirmBeforeDelete: Boolean = true,
    val sortByCategory: Boolean = true,
    val appearance: String = "light",
)

data class UiState(
    val ready: Boolean = false,
    val currentUser: UserEntity? = null,
    val lists: List<ShoppingList> = emptyList(),
    val settings: Settings = Settings(),
)
