package co.edu.upb.mimercado.data

private data class SeedProduct(
    val name: String,
    val quantity: Int,
    val unit: String,
    val category: String,
    val note: String = "",
    val priority: String = "normal",
    val bought: Boolean = false,
)

private data class SeedList(
    val name: String,
    val icon: String,
    val color: String,
    val ageMillis: Long,
    val products: List<SeedProduct>,
)

private val sampleLists = listOf(
    SeedList(
        name = "Mercado semanal",
        icon = "cart",
        color = "#2F7D4B",
        ageMillis = 0,
        products = listOf(
            SeedProduct("Tomates", 4, "Unidades", "Frutas y verduras", note = "Para ensalada"),
            SeedProduct("Banano", 6, "Unidades", "Frutas y verduras", bought = true),
            SeedProduct("Cebolla", 2, "Unidades", "Frutas y verduras"),
            SeedProduct("Leche", 2, "L", "Lácteos", priority = "important"),
            SeedProduct("Queso", 1, "Unidades", "Lácteos", bought = true),
            SeedProduct("Jabón", 2, "Unidades", "Aseo"),
            SeedProduct("Pollo", 1, "kg", "Carnes", priority = "important"),
            SeedProduct("Pan", 1, "Unidades", "Panadería", bought = true),
            SeedProduct("Jugo de naranja", 1, "L", "Bebidas"),
            SeedProduct("Arroz", 1, "kg", "Otros"),
            SeedProduct("Aceite", 1, "L", "Hogar", bought = true),
            SeedProduct("Pasta", 2, "Unidades", "Otros"),
        ),
    ),
    SeedList(
        name = "Compra para la cena",
        icon = "food",
        color = "#F2B84B",
        ageMillis = 86_400_000,
        products = listOf(
            SeedProduct("Carne molida", 500, "g", "Carnes", priority = "important"),
            SeedProduct("Espagueti", 2, "Unidades", "Otros"),
            SeedProduct("Salsa de tomate", 1, "Unidades", "Otros"),
            SeedProduct("Queso parmesano", 1, "Unidades", "Lácteos"),
            SeedProduct("Vino tinto", 1, "Unidades", "Bebidas"),
        ),
    ),
    SeedList(
        name = "Artículos de aseo",
        icon = "clean",
        color = "#4A90D9",
        ageMillis = 172_800_000,
        products = listOf(
            SeedProduct("Shampoo", 1, "Unidades", "Aseo", bought = true),
            SeedProduct("Acondicionador", 1, "Unidades", "Aseo", bought = true),
            SeedProduct("Crema dental", 2, "Unidades", "Aseo", bought = true),
            SeedProduct("Papel higiénico", 6, "Unidades", "Aseo", priority = "important", bought = true),
            SeedProduct("Jabón de manos", 2, "Unidades", "Aseo", bought = true),
            SeedProduct("Desodorante", 1, "Unidades", "Aseo", bought = true),
            SeedProduct("Toallas húmedas", 1, "Unidades", "Aseo", bought = true),
        ),
    ),
    SeedList(
        name = "Cumpleaños familiar",
        icon = "food",
        color = "#D9534F",
        ageMillis = 259_200_000,
        products = listOf(
            SeedProduct("Torta de chocolate", 1, "Unidades", "Panadería", priority = "important"),
            SeedProduct("Gaseosas", 6, "Unidades", "Bebidas", bought = true),
            SeedProduct("Globos", 20, "Unidades", "Hogar"),
            SeedProduct("Velas", 1, "Unidades", "Hogar"),
            SeedProduct("Platos desechables", 1, "Unidades", "Hogar", bought = true),
        ),
    ),
)

suspend fun seedDemoAccount(db: AppDatabase) {
    if (db.userDao().count() > 0) return
    val salt = Passwords.newSalt()
    val now = System.currentTimeMillis()
    val userId = db.userDao().insert(
        UserEntity(
            name = "Andrea García",
            email = "andrea@email.com",
            passwordHash = Passwords.hash("mercado123", salt),
            salt = salt,
        ),
    )
    sampleLists.forEach { sample ->
        val listId = db.listDao().insert(
            ListEntity(
                userId = userId,
                name = sample.name,
                icon = sample.icon,
                color = sample.color,
                updatedAt = now - sample.ageMillis,
            ),
        )
        sample.products.forEach { product ->
            db.productDao().insert(
                ProductEntity(
                    listId = listId,
                    name = product.name,
                    quantity = product.quantity,
                    unit = product.unit,
                    category = product.category,
                    note = product.note,
                    priority = product.priority,
                    bought = product.bought,
                ),
            )
        }
    }
}
