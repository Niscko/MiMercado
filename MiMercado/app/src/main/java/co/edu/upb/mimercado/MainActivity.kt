package co.edu.upb.mimercado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.upb.mimercado.ui.components.LogoMark
import co.edu.upb.mimercado.ui.navigation.MiMercadoNav
import co.edu.upb.mimercado.ui.theme.MiMercadoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = (application as MiMercadoApplication).repository
        setContent {
            val viewModel: MercadoViewModel = viewModel(factory = MercadoViewModel.factory(repository))
            val ui by viewModel.ui.collectAsState()
            val dark = when (ui.settings.appearance) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }
            MiMercadoTheme(dark = dark) {
                if (!ui.ready) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center,
                    ) {
                        LogoMark(size = 72.dp)
                    }
                } else {
                    key(ui.currentUser?.id ?: 0L) {
                        MiMercadoNav(viewModel, ui)
                    }
                }
            }
        }
    }
}
