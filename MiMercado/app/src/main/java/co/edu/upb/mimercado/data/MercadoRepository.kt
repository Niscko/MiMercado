package co.edu.upb.mimercado.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("mimercado")

class MercadoRepository(
    private val context: Context,
    private val db: AppDatabase,
) {
    private val sessionKey = longPreferencesKey("session_user_id")
    private val showBoughtKey = booleanPreferencesKey("show_bought")
    private val confirmDeleteKey = booleanPreferencesKey("confirm_delete")
    private val sortCategoryKey = booleanPreferencesKey("sort_category")
    private val appearanceKey = stringPreferencesKey("appearance")

    val sessionUserId: Flow<Long?> = context.dataStore.data.map { prefs ->
        prefs[sessionKey]
    }

    val settings: Flow<Settings> = context.dataStore.data.map { prefs ->
        Settings(
            showBoughtProducts = prefs[showBoughtKey] ?: true,
            confirmBeforeDelete = prefs[confirmDeleteKey] ?: true,
            sortByCategory = prefs[sortCategoryKey] ?: true,
            appearance = prefs[appearanceKey] ?: "light",
        )
    }

    suspend fun ensureSeeded() {
        seedDemoAccount(db)
    }

    suspend fun currentUser(): UserEntity? {
        val id = sessionUserId.first() ?: return null
        return db.userDao().findById(id)
    }

    fun lists(userId: Long): Flow<List<ShoppingList>> {
        val listsFlow = db.listDao().observe(userId)
        val productsFlow = db.productDao().observeByUser(userId)
        return combine(listsFlow, productsFlow) { lists, products ->
            val byList = products.groupBy { it.listId }
            lists.map { list ->
                ShoppingList(
                    id = list.id,
                    name = list.name,
                    icon = list.icon,
                    color = list.color,
                    updatedAt = list.updatedAt,
                    products = byList[list.id].orEmpty().map { it.toProduct() },
                )
            }
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        val normalized = email.trim().lowercase()
        if (db.userDao().findByEmail(normalized) != null) {
            return Result.failure(IllegalStateException("Ya existe una cuenta con ese correo"))
        }
        val salt = Passwords.newSalt()
        val id = db.userDao().insert(
            UserEntity(
                name = name.trim(),
                email = normalized,
                passwordHash = Passwords.hash(password, salt),
                salt = salt,
            ),
        )
        saveSession(id)
        return Result.success(Unit)
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        val user = db.userDao().findByEmail(email.trim().lowercase())
            ?: return Result.failure(IllegalArgumentException("Correo o contraseña incorrectos"))
        if (!Passwords.matches(password, user.salt, user.passwordHash)) {
            return Result.failure(IllegalArgumentException("Correo o contraseña incorrectos"))
        }
        saveSession(user.id)
        return Result.success(Unit)
    }

    suspend fun recover(email: String, newPassword: String): Result<Unit> {
        val user = db.userDao().findByEmail(email.trim().lowercase())
            ?: return Result.failure(IllegalArgumentException("No hay una cuenta con ese correo"))
        val salt = Passwords.newSalt()
        db.userDao().update(
            user.copy(salt = salt, passwordHash = Passwords.hash(newPassword, salt)),
        )
        return Result.success(Unit)
    }

    suspend fun logout() {
        context.dataStore.edit { it.remove(sessionKey) }
    }

    suspend fun addList(userId: Long, name: String, icon: String, color: String): Long {
        return db.listDao().insert(
            ListEntity(
                userId = userId,
                name = name.trim(),
                icon = icon,
                color = color,
                updatedAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun deleteList(listId: Long) {
        db.listDao().delete(listId)
    }

    suspend fun addProduct(listId: Long, product: Product) {
        db.productDao().insert(product.toEntity(listId))
        db.listDao().touch(listId, System.currentTimeMillis())
    }

    suspend fun updateProduct(listId: Long, product: Product) {
        db.productDao().update(product.toEntity(listId))
        db.listDao().touch(listId, System.currentTimeMillis())
    }

    suspend fun toggleProduct(listId: Long, product: Product) {
        db.productDao().update(product.copy(bought = !product.bought).toEntity(listId))
        db.listDao().touch(listId, System.currentTimeMillis())
    }

    suspend fun deleteProduct(listId: Long, productId: Long) {
        db.productDao().delete(productId)
        db.listDao().touch(listId, System.currentTimeMillis())
    }

    suspend fun updateSettings(settings: Settings) {
        context.dataStore.edit { prefs ->
            prefs[showBoughtKey] = settings.showBoughtProducts
            prefs[confirmDeleteKey] = settings.confirmBeforeDelete
            prefs[sortCategoryKey] = settings.sortByCategory
            prefs[appearanceKey] = settings.appearance
        }
    }

    private suspend fun saveSession(userId: Long) {
        context.dataStore.edit { it[sessionKey] = userId }
    }
}

private fun ProductEntity.toProduct() = Product(
    id = id,
    name = name,
    quantity = quantity,
    unit = unit,
    category = category,
    note = note,
    priority = priority,
    bought = bought,
)

private fun Product.toEntity(listId: Long) = ProductEntity(
    id = id,
    listId = listId,
    name = name,
    quantity = quantity,
    unit = unit,
    category = category,
    note = note,
    priority = priority,
    bought = bought,
)
