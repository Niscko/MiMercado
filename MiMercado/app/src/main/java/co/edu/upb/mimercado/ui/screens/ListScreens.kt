package co.edu.upb.mimercado.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.upb.mimercado.MercadoViewModel
import co.edu.upb.mimercado.data.Catalog
import co.edu.upb.mimercado.data.ShoppingList
import co.edu.upb.mimercado.data.UiState
import co.edu.upb.mimercado.ui.components.FilterPills
import co.edu.upb.mimercado.ui.components.ListSummaryCard
import co.edu.upb.mimercado.ui.components.MercadoField
import co.edu.upb.mimercado.ui.components.PrimaryButton
import co.edu.upb.mimercado.ui.components.SecondaryButton
import co.edu.upb.mimercado.ui.components.greeting
import co.edu.upb.mimercado.ui.components.listIcon
import co.edu.upb.mimercado.ui.components.parseHex
import co.edu.upb.mimercado.ui.theme.Inter
import co.edu.upb.mimercado.ui.theme.Poppins

@Composable
fun HomeScreen(ui: UiState, onOpenList: (Long) -> Unit, onSeeAll: () -> Unit, onNewList: () -> Unit) {
    val lists = ui.lists
    val products = lists.sumOf { it.totalCount }
    val bought = lists.sumOf { it.boughtCount }
    val pending = products - bought
    val featured = lists.take(3)
    val name = ui.currentUser?.name ?: ""

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp),
    ) {
        item {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                Text(greeting(name), fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)
                Text("Organiza tu compra de hoy", fontFamily = Inter, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(vertical = 16.dp),
                ) {
                    Stat("productos", products.toString(), Modifier.weight(1f))
                    Stat("pendientes", pending.toString(), Modifier.weight(1f))
                    Stat("comprados", bought.toString(), Modifier.weight(1f))
                }
            }
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Tus listas", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(
                    "Ver todas",
                    modifier = Modifier.clickable(onClick = onSeeAll),
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                )
            }
        }
        items(featured, key = { it.id }) { list ->
            Box(Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                ListSummaryCard(
                    name = list.name,
                    icon = list.icon,
                    colorHex = list.color,
                    bought = list.boughtCount,
                    total = list.totalCount,
                    updatedAt = list.updatedAt,
                    onClick = { onOpenList(list.id) },
                )
            }
        }
        item {
            Box(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                PrimaryButton("Nueva lista", onClick = onNewList)
            }
        }
    }
}

@Composable
private fun Stat(label: String, value: String, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
        Text(label, fontFamily = Inter, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ListsScreen(
    ui: UiState,
    viewModel: MercadoViewModel,
    onOpenList: (Long) -> Unit,
    onNewList: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("Todas") }
    var menuFor by remember { mutableStateOf<Long?>(null) }
    var pendingDelete by remember { mutableStateOf<ShoppingList?>(null) }

    val visible = ui.lists.filter { list ->
        val matches = list.name.contains(query, ignoreCase = true)
        val active = when (filter) {
            "Activas" -> !list.completed
            "Completadas" -> list.completed
            else -> true
        }
        matches && active
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            "Mis listas",
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp),
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
        )
        SearchField(query, { query = it }, "Buscar listas", Modifier.padding(horizontal = 20.dp))
        Spacer(Modifier.height(12.dp))
        Box(Modifier.padding(horizontal = 20.dp)) {
            FilterPills(listOf("Todas", "Activas", "Completadas"), filter) { filter = it }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (visible.isEmpty()) {
                item {
                    Text(
                        "No hay listas aquí",
                        modifier = Modifier.padding(top = 32.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = Inter,
                    )
                }
            }
            items(visible, key = { it.id }) { list ->
                ListSummaryCard(
                    name = list.name,
                    icon = list.icon,
                    colorHex = list.color,
                    bought = list.boughtCount,
                    total = list.totalCount,
                    updatedAt = list.updatedAt,
                    onClick = { onOpenList(list.id) },
                    trailing = {
                        Box {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "Acciones de la lista",
                                modifier = Modifier.clickable { menuFor = list.id },
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            DropdownMenu(expanded = menuFor == list.id, onDismissRequest = { menuFor = null }) {
                                DropdownMenuItem(
                                    text = { Text("Eliminar lista", color = MaterialTheme.colorScheme.error) },
                                    onClick = {
                                        menuFor = null
                                        if (ui.settings.confirmBeforeDelete) pendingDelete = list else viewModel.deleteList(list.id)
                                    },
                                )
                            }
                        }
                    },
                )
            }
        }
        Box(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            PrimaryButton("Nueva lista", onClick = onNewList)
        }
    }

    pendingDelete?.let { list ->
        ConfirmDialog(
            title = "¿Eliminar lista?",
            message = "Se borrará «${list.name}» y sus productos. Esta acción no se puede deshacer.",
            confirm = "Eliminar",
            onConfirm = {
                viewModel.deleteList(list.id)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null },
        )
    }
}

@Composable
fun NewListScreen(viewModel: MercadoViewModel, onBack: () -> Unit, onCreated: (Long) -> Unit) {
    var name by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("cart") }
    var color by remember { mutableStateOf(Catalog.colors.first()) }
    var error by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
    ) {
        ScreenTitle("Nueva lista", onBack)
        MercadoField("Nombre de la lista", name, { name = it; error = "" }, "Mercado semanal", error)
        Spacer(Modifier.height(18.dp))
        Text("Icono de lista", fontFamily = Inter, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Catalog.icons.forEach { (id, label) ->
                val selected = icon == id
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { icon = id }) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (selected) parseHex(color) else MaterialTheme.colorScheme.surfaceVariant)
                            .border(2.dp, if (selected) parseHex(color) else androidx.compose.ui.graphics.Color.Transparent, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            listIcon(id),
                            contentDescription = label,
                            tint = if (selected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(label, fontFamily = Inter, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        Text("Color", fontFamily = Inter, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Catalog.colors.forEach { hex ->
                val selected = color == hex
                Box(
                    modifier = Modifier
                        .size(if (selected) 34.dp else 28.dp)
                        .clip(CircleShape)
                        .background(parseHex(hex))
                        .border(2.dp, if (selected) MaterialTheme.colorScheme.onSurface else androidx.compose.ui.graphics.Color.Transparent, CircleShape)
                        .clickable { color = hex },
                )
            }
        }
        Spacer(Modifier.height(18.dp))
        Text("Vista previa", fontFamily = Inter, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        ListSummaryCard(
            name = name.ifBlank { "Nueva lista" },
            icon = icon,
            colorHex = color,
            bought = 0,
            total = 0,
            updatedAt = System.currentTimeMillis(),
            onClick = {},
        )
        Spacer(Modifier.weight(1f))
        PrimaryButton("Crear lista") {
            if (name.isBlank()) {
                error = "Escribe un nombre para la lista"
            } else {
                viewModel.addList(name, icon, color, onCreated)
            }
        }
        Spacer(Modifier.height(10.dp))
        SecondaryButton("Cancelar", onClick = onBack)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun SearchField(value: String, onValueChange: (String) -> Unit, placeholder: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = Inter,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(placeholder, fontFamily = Inter, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                inner()
            },
        )
    }
}

@Composable
fun ScreenTitle(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.material3.IconButton(onClick = onBack) {
            androidx.compose.material3.Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(title, fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
    }
}

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirm: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontFamily = Poppins, fontWeight = FontWeight.SemiBold) },
        text = { Text(message, fontFamily = Inter) },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onConfirm) {
                Text(confirm, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}
