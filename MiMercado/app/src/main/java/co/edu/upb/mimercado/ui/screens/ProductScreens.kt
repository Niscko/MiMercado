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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.upb.mimercado.MercadoViewModel
import co.edu.upb.mimercado.data.Catalog
import co.edu.upb.mimercado.data.Product
import co.edu.upb.mimercado.data.UiState
import co.edu.upb.mimercado.ui.components.FilterPills
import co.edu.upb.mimercado.ui.components.PrimaryButton
import co.edu.upb.mimercado.ui.components.ProgressLine
import co.edu.upb.mimercado.ui.components.SecondaryButton
import co.edu.upb.mimercado.ui.components.fieldColors
import co.edu.upb.mimercado.ui.components.parseHex
import co.edu.upb.mimercado.ui.theme.Inter
import co.edu.upb.mimercado.ui.theme.Poppins
import co.edu.upb.mimercado.ui.theme.Yellow

@Composable
fun ListDetailScreen(
    listId: Long,
    ui: UiState,
    viewModel: MercadoViewModel,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onOpenProduct: (Long) -> Unit,
) {
    val list = ui.lists.find { it.id == listId }
    var seen by remember { mutableStateOf(false) }
    LaunchedEffect(list?.id) {
        if (list != null) seen = true else if (seen) onBack()
    }
    if (list == null) return

    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("Todos") }
    val tint = parseHex(list.color)

    val visible = list.products.filter { product ->
        val text = product.name.contains(query, ignoreCase = true)
        val byFilter = when (filter) {
            "Pendientes" -> !product.bought
            "Comprados" -> product.bought
            else -> true
        }
        val hiddenBought = !ui.settings.showBoughtProducts && product.bought && filter != "Comprados"
        text && byFilter && !hiddenBought
    }
    val grouped = if (ui.settings.sortByCategory) {
        visible.groupBy { it.category }.toList()
    } else {
        listOf("Productos" to visible)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 88.dp),
        ) {
            item {
                ScreenTitle(list.name, onBack)
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "${list.boughtCount} de ${list.totalCount}",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = tint,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("productos", fontFamily = Inter, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    ProgressLine(list.progress, tint)
                    if (list.completed) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "¡Compra completada!",
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    SearchField(query, { query = it }, "Buscar producto")
                    Spacer(Modifier.height(12.dp))
                    FilterPills(listOf("Todos", "Pendientes", "Comprados"), filter) { filter = it }
                    Spacer(Modifier.height(8.dp))
                }
            }
            if (list.products.isEmpty()) {
                item {
                    Column(Modifier.padding(horizontal = 20.dp, vertical = 28.dp)) {
                        Text("Aún no tienes productos", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Spacer(Modifier.height(6.dp))
                        Text("Agrega el primer producto para empezar la compra.", fontFamily = Inter, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(16.dp))
                        PrimaryButton("Agregar producto", onClick = onAdd)
                    }
                }
            }
            grouped.forEach { (category, products) ->
                if (products.isEmpty()) return@forEach
                item {
                    Text(
                        if (ui.settings.sortByCategory) "${Catalog.emoji(category)}  $category" else category,
                        modifier = Modifier.padding(start = 20.dp, top = 14.dp, bottom = 6.dp),
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                items(products, key = { it.id }) { product ->
                    ProductRow(
                        product = product,
                        onToggle = { viewModel.toggleProduct(list.id, product) },
                        onOpen = { onOpenProduct(product.id) },
                    )
                }
            }
        }
        if (list.products.isNotEmpty()) {
            PrimaryButton(
                "Agregar producto",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                onClick = onAdd,
            )
        }
    }
}

@Composable
private fun ProductRow(product: Product, onToggle: () -> Unit, onOpen: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onOpen)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (product.bought) MaterialTheme.colorScheme.primary else Color.Transparent)
                .border(2.dp, if (product.bought) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, CircleShape)
                .clickable(onClick = onToggle),
            contentAlignment = Alignment.Center,
        ) {
            if (product.bought) {
                Icon(Icons.Filled.Check, contentDescription = "Comprado", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                product.name,
                fontFamily = Inter,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                textDecoration = if (product.bought) TextDecoration.LineThrough else null,
                color = if (product.bought) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                "${product.quantity} ${product.unit} · ${Catalog.emoji(product.category)} ${product.category}",
                fontFamily = Inter,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (product.priority == "important") {
            Text(
                "Importante",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Yellow.copy(alpha = 0.25f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color(0xFF8A5A00),
                fontFamily = Inter,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    listId: Long,
    productId: Long,
    ui: UiState,
    viewModel: MercadoViewModel,
    onBack: () -> Unit,
) {
    val existing = ui.lists.find { it.id == listId }?.products?.find { it.id == productId }
    val editing = existing != null
    var name by remember(existing?.id) { mutableStateOf(existing?.name.orEmpty()) }
    var quantity by remember(existing?.id) { mutableIntStateOf(existing?.quantity ?: 1) }
    var unit by remember(existing?.id) { mutableStateOf(existing?.unit ?: "Unidades") }
    var category by remember(existing?.id) { mutableStateOf(existing?.category ?: Catalog.categories.first()) }
    var note by remember(existing?.id) { mutableStateOf(existing?.note.orEmpty()) }
    var priority by remember(existing?.id) { mutableStateOf(existing?.priority ?: "normal") }
    var error by remember { mutableStateOf("") }
    var categoryOpen by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
    ) {
        ScreenTitle(if (editing) "Editar producto" else "Agregar producto", onBack)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; error = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre del producto") },
            singleLine = true,
            isError = error.isNotBlank(),
            supportingText = if (error.isNotBlank()) {
                { Text(error) }
            } else {
                null
            },
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
        )
        Spacer(Modifier.height(14.dp))
        Text("Cantidad", fontFamily = Inter, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            StepperButton(Icons.Filled.Remove, "Restar") { if (quantity > 1) quantity -= 1 }
            Text(
                quantity.toString(),
                modifier = Modifier.padding(horizontal = 18.dp),
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
            )
            StepperButton(Icons.Filled.Add, "Sumar") { quantity += 1 }
        }
        Spacer(Modifier.height(14.dp))
        Text("Unidad", fontFamily = Inter, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        FilterPills(Catalog.units, unit) { unit = it }
        Spacer(Modifier.height(14.dp))
        Text("Categoría", fontFamily = Inter, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(expanded = categoryOpen, onExpandedChange = { categoryOpen = it }) {
            OutlinedTextField(
                value = "${Catalog.emoji(category)}  $category",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryOpen) },
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
            )
            ExposedDropdownMenu(expanded = categoryOpen, onDismissRequest = { categoryOpen = false }) {
                Catalog.categories.forEach { option ->
                    DropdownMenuItem(
                        text = { Text("${Catalog.emoji(option)}  $option") },
                        onClick = {
                            category = option
                            categoryOpen = false
                        },
                    )
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nota (opcional)") },
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
        )
        Spacer(Modifier.height(14.dp))
        Text("Prioridad", fontFamily = Inter, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PriorityChip("Normal", priority == "normal") { priority = "normal" }
            PriorityChip("Importante", priority == "important") { priority = "important" }
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton(if (editing) "Guardar cambios" else "Agregar producto") {
            if (name.isBlank()) {
                error = "El nombre es obligatorio"
            } else {
                val product = Product(
                    id = existing?.id ?: 0,
                    name = name.trim(),
                    quantity = quantity,
                    unit = unit,
                    category = category,
                    note = note.trim(),
                    priority = priority,
                    bought = existing?.bought ?: false,
                )
                if (editing) viewModel.updateProduct(listId, product) else viewModel.addProduct(listId, product)
                onBack()
            }
        }
        Spacer(Modifier.height(10.dp))
        SecondaryButton("Cancelar", onClick = onBack)
    }
}

@Composable
private fun StepperButton(icon: androidx.compose.ui.graphics.vector.ImageVector, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = description, tint = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun PriorityChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
            .border(1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            if (text == "Importante") "⭐ $text" else text,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun ProductDetailScreen(
    listId: Long,
    productId: Long,
    ui: UiState,
    viewModel: MercadoViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
) {
    val product = ui.lists.find { it.id == listId }?.products?.find { it.id == productId }
    var confirm by remember { mutableStateOf(false) }
    var seen by remember { mutableStateOf(false) }
    LaunchedEffect(product?.id) {
        if (product != null) seen = true else if (seen) onBack()
    }
    if (product == null) return

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
    ) {
        ScreenTitle("Detalle del producto", onBack)
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(18.dp),
        ) {
            Text(Catalog.emoji(product.category), fontSize = 36.sp)
            Spacer(Modifier.height(8.dp))
            Text(product.name, fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
            Spacer(Modifier.height(12.dp))
            DetailLine("Cantidad", "${product.quantity} ${product.unit}")
            DetailLine("Categoría", product.category)
            DetailLine("Prioridad", if (product.priority == "important") "Importante" else "Normal")
            DetailLine("Estado", if (product.bought) "Comprado" else "Pendiente")
            if (product.note.isNotBlank()) DetailLine("Nota", product.note)
        }
        Spacer(Modifier.weight(1f))
        PrimaryButton("Editar producto", onClick = onEdit)
        Spacer(Modifier.height(10.dp))
        SecondaryButton("Eliminar producto") {
            if (ui.settings.confirmBeforeDelete) confirm = true else {
                viewModel.deleteProduct(listId, product.id)
            }
        }
        Spacer(Modifier.height(20.dp))
    }

    if (confirm) {
        ConfirmDialog(
            title = "¿Eliminar producto?",
            message = "Esta acción no se puede deshacer.",
            confirm = "Eliminar",
            onConfirm = {
                confirm = false
                viewModel.deleteProduct(listId, product.id)
            },
            onDismiss = { confirm = false },
        )
    }
}

@Composable
private fun DetailLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontFamily = Inter, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontFamily = Inter, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    }
}
