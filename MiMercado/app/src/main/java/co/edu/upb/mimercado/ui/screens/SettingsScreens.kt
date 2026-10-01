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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.upb.mimercado.MercadoViewModel
import co.edu.upb.mimercado.data.Settings
import co.edu.upb.mimercado.data.UiState
import co.edu.upb.mimercado.ui.components.LogoMark
import co.edu.upb.mimercado.ui.components.SectionLabel
import co.edu.upb.mimercado.ui.components.initials
import co.edu.upb.mimercado.ui.theme.Inter
import co.edu.upb.mimercado.ui.theme.Poppins

@Composable
fun SettingsScreen(ui: UiState, viewModel: MercadoViewModel, onCredits: () -> Unit) {
    val settings = ui.settings
    val user = ui.currentUser
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
    ) {
        Text(
            "Configuración",
            modifier = Modifier.padding(top = 20.dp, bottom = 16.dp),
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Text(initials(user?.name.orEmpty()), color = MaterialTheme.colorScheme.onPrimary, fontFamily = Poppins, fontWeight = FontWeight.SemiBold)
            }
            Column(Modifier.padding(start = 12.dp)) {
                Text(user?.name.orEmpty(), fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(user?.email.orEmpty(), fontFamily = Inter, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(18.dp))
        SectionLabel("Apariencia")
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        ) {
            listOf("light" to "Claro", "dark" to "Oscuro", "system" to "Sistema").forEach { (value, label) ->
                val selected = settings.appearance == value
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { viewModel.updateSettings(settings.copy(appearance = value)) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        label,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                    )
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        SectionLabel("Preferencias")
        Spacer(Modifier.height(4.dp))
        CardBlock {
            SwitchRow("Mostrar productos comprados", settings.showBoughtProducts) {
                viewModel.updateSettings(settings.copy(showBoughtProducts = it))
            }
            SwitchRow("Confirmar antes de eliminar", settings.confirmBeforeDelete) {
                viewModel.updateSettings(settings.copy(confirmBeforeDelete = it))
            }
            SwitchRow("Ordenar productos por categoría", settings.sortByCategory) {
                viewModel.updateSettings(settings.copy(sortByCategory = it))
            }
        }
        Spacer(Modifier.height(16.dp))
        CardBlock {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onCredits)
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                androidx.compose.material3.Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Créditos", modifier = Modifier.padding(start = 10.dp).weight(1f), fontFamily = Inter, fontWeight = FontWeight.Medium)
                androidx.compose.material3.Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Versión", fontFamily = Inter, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("1.0.0", fontFamily = Inter, fontWeight = FontWeight.Medium)
            }
        }
        Spacer(Modifier.height(18.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(16.dp))
                .clickable { viewModel.logout() },
            contentAlignment = Alignment.Center,
        ) {
            Text("Cerrar sesión", color = MaterialTheme.colorScheme.error, fontFamily = Poppins, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun CardBlock(content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) { content() }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, modifier = Modifier.weight(1f), fontFamily = Inter, fontSize = 14.sp)
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = Color.White,
            ),
        )
    }
}

@Composable
fun CreditsScreen(onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxWidth()) { ScreenTitle("Créditos", onBack) }
        LogoMark(size = 72.dp)
        Spacer(Modifier.height(12.dp))
        Text("MiMercado", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 26.sp)
        Text("Versión 1.0.0", fontFamily = Inter, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(18.dp))
        CreditCard(
            "Proyecto" to "App de organización de listas de mercado",
            "Universidad" to "Universidad Pontificia Bolivariana",
            "Programa" to "Ingeniería de Sistemas",
            "Asignatura" to "Aplicaciones Móviles",
        )
        Spacer(Modifier.height(16.dp))
        Box(Modifier.fillMaxWidth()) { SectionLabel("Integrantes") }
        Spacer(Modifier.height(8.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Member("Josue Pino")
            Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.surfaceVariant))
            Member("Nicolás Agudelo")
        }
        Spacer(Modifier.height(18.dp))
        Text("Desarrollado con Kotlin y Jetpack Compose", fontFamily = Inter, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun CreditCard(vararg rows: Pair<String, String>) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        rows.forEach { (label, value) ->
            Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Text(label, fontFamily = Inter, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(value, fontFamily = Inter, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun Member(name: String) {
    Text(
        name,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
    )
}
