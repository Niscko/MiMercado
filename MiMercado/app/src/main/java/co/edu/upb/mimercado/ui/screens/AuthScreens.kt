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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.upb.mimercado.MercadoViewModel
import co.edu.upb.mimercado.ui.components.LogoMark
import co.edu.upb.mimercado.ui.components.MercadoField
import co.edu.upb.mimercado.ui.components.PrimaryButton
import co.edu.upb.mimercado.ui.components.StrengthMeter
import co.edu.upb.mimercado.ui.components.emailError
import co.edu.upb.mimercado.ui.components.passwordIssues
import co.edu.upb.mimercado.ui.theme.Inter
import co.edu.upb.mimercado.ui.theme.Poppins
import co.edu.upb.mimercado.ui.theme.Yellow

@Composable
fun WelcomeScreen(onLogin: () -> Unit, onRegister: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(72.dp))
        LogoMark(size = 72.dp)
        Spacer(Modifier.height(16.dp))
        Text("MiMercado", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = MaterialTheme.colorScheme.onBackground)
        Text(
            "Organiza tus compras de forma sencilla",
            fontFamily = Inter,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(28.dp))
        WelcomeArt()
        Spacer(Modifier.height(22.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SoftChip("Listas organizadas")
            SoftChip("Por categorías")
        }
        Spacer(Modifier.height(8.dp))
        SoftChip("Progreso visual")
        Spacer(Modifier.weight(1f))
        PrimaryButton("Iniciar sesión", onClick = onLogin)
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                .clickable(onClick = onRegister),
            contentAlignment = Alignment.Center,
        ) {
            Text("Crear cuenta", color = MaterialTheme.colorScheme.primary, fontFamily = Poppins, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun WelcomeArt() {
    Box(modifier = Modifier.size(210.dp), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(168.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
        )
        Box(
            modifier = Modifier
                .size(width = 132.dp, height = 108.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Column(Modifier.padding(start = 18.dp, top = 28.dp)) {
                repeat(3) {
                    Box(
                        Modifier
                            .padding(bottom = 8.dp)
                            .width(if (it == 2) 48.dp else 72.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.85f)),
                    )
                }
            }
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(22.dp),
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-18).dp, y = 36.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(Yellow),
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 8.dp, y = 28.dp)
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Yellow),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.ShoppingCart, null, tint = Color(0xFF1F2A24), modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun SoftChip(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        color = MaterialTheme.colorScheme.primary,
        fontFamily = Inter,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
    )
}

@Composable
fun LoginScreen(
    viewModel: MercadoViewModel,
    onBack: () -> Unit,
    onRegister: () -> Unit,
    onRecover: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    AuthScaffold("Iniciar sesión", onBack) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LogoMark(size = 36.dp)
            Spacer(Modifier.width(8.dp))
            Text("MiMercado", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "Bienvenido de nuevo. Ingresa tus datos para continuar.",
            fontFamily = Inter,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
        )
        Spacer(Modifier.height(22.dp))
        MercadoField("Correo electrónico", email, { email = it; emailError = "" }, "andrea@email.com", emailError, keyboardType = androidx.compose.ui.text.input.KeyboardType.Email)
        Spacer(Modifier.height(14.dp))
        MercadoField("Contraseña", password, { password = it; passwordError = "" }, "••••••••", passwordError, password = true)
        TextButton(onClick = onRecover, modifier = Modifier.align(Alignment.End)) {
            Text("¿Olvidaste tu contraseña?", color = MaterialTheme.colorScheme.primary, fontFamily = Inter, fontSize = 13.sp)
        }
        PrimaryButton("Iniciar sesión") {
            val mail = emailError(email)
            val pass = if (password.isBlank()) "La contraseña es obligatoria" else null
            emailError = mail.orEmpty()
            passwordError = pass.orEmpty()
            if (mail == null && pass == null) {
                viewModel.login(email, password) { passwordError = it }
            }
        }
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.outline))
            Text("  o continúa con  ", fontFamily = Inter, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Box(Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.outline))
        }
        Spacer(Modifier.height(14.dp))
        Text(
            buildAnnotatedString {
                append("¿No tienes cuenta? ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)) {
                    append("Crear cuenta")
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = onRegister),
            fontFamily = Inter,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun RegisterScreen(viewModel: MercadoViewModel, onBack: () -> Unit, onLogin: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf("") }
    var mailError by remember { mutableStateOf("") }
    var passError by remember { mutableStateOf("") }
    var termsError by remember { mutableStateOf("") }
    var showTerms by remember { mutableStateOf(false) }

    AuthScaffold("Crear cuenta", onBack) {
        Text(
            "Crea tu cuenta para guardar las listas en este teléfono.",
            fontFamily = Inter,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(18.dp))
        MercadoField("Nombre completo", name, { name = it; nameError = "" }, "Andrea García", nameError)
        Spacer(Modifier.height(14.dp))
        MercadoField("Correo electrónico", email, { email = it; mailError = "" }, "andrea@email.com", mailError, keyboardType = androidx.compose.ui.text.input.KeyboardType.Email)
        Spacer(Modifier.height(14.dp))
        MercadoField("Contraseña", password, { password = it; passError = "" }, "Mínimo 6 caracteres", passError, password = true)
        StrengthMeter(password)
        Spacer(Modifier.height(14.dp))
        MercadoField("Confirmar contraseña", confirm, { confirm = it; passError = "" }, "Repite tu contraseña", password = true)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = accepted,
                onCheckedChange = { accepted = it; termsError = "" },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
            )
            Text(
                buildAnnotatedString {
                    append("Acepto los ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)) {
                        append("términos y condiciones")
                    }
                },
                modifier = Modifier.clickable { showTerms = true },
                fontFamily = Inter,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        if (termsError.isNotBlank()) {
            Text(termsError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, fontFamily = Inter)
        }
        Spacer(Modifier.height(12.dp))
        PrimaryButton("Crear cuenta") {
            nameError = if (name.isBlank()) "El nombre es obligatorio" else ""
            mailError = emailError(email).orEmpty()
            passError = passwordIssues(password, confirm).orEmpty()
            termsError = if (!accepted) "Debes aceptar los términos" else ""
            if (nameError.isBlank() && mailError.isBlank() && passError.isBlank() && termsError.isBlank()) {
                viewModel.register(name, email, password) { mailError = it }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            buildAnnotatedString {
                append("¿Ya tienes cuenta? ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)) {
                    append("Iniciar sesión")
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = onLogin),
            fontFamily = Inter,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    if (showTerms) {
        AlertDialog(
            onDismissRequest = { showTerms = false },
            title = { Text("Términos y condiciones", fontFamily = Poppins) },
            text = {
                Text(
                    "MiMercado guarda tus listas solo en este teléfono. No comparte datos con tiendas ni procesa pagos. Al crear la cuenta aceptas usar la aplicación para organizar tus compras.",
                    fontFamily = Inter,
                )
            },
            confirmButton = {
                TextButton(onClick = { accepted = true; showTerms = false }) {
                    Text("Aceptar", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTerms = false }) { Text("Cerrar") }
            },
        )
    }
}

@Composable
fun RecoverScreen(viewModel: MercadoViewModel, onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    AuthScaffold("Recuperar contraseña", onBack) {
        Text(
            "Escribe el correo de tu cuenta y elige una contraseña nueva. El cambio queda guardado en este teléfono.",
            fontFamily = Inter,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(18.dp))
        MercadoField("Correo electrónico", email, { email = it; error = "" }, "andrea@email.com", keyboardType = androidx.compose.ui.text.input.KeyboardType.Email)
        Spacer(Modifier.height(14.dp))
        MercadoField("Nueva contraseña", password, { password = it; error = "" }, "Mínimo 6 caracteres", password = true)
        StrengthMeter(password)
        Spacer(Modifier.height(14.dp))
        MercadoField("Confirmar contraseña", confirm, { confirm = it; error = "" }, "Repite tu contraseña", error, password = true)
        Spacer(Modifier.height(18.dp))
        PrimaryButton("Guardar contraseña") {
            val mail = emailError(email)
            val pass = passwordIssues(password, confirm)
            error = mail ?: pass.orEmpty()
            if (mail == null && pass == null) {
                viewModel.recover(email, password, onDone = onBack) { error = it }
            }
        }
    }
}

@Composable
private fun AuthScaffold(title: String, onBack: () -> Unit, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onSurface)
            }
            Text(title, fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        Spacer(Modifier.height(8.dp))
        content()
    }
}
