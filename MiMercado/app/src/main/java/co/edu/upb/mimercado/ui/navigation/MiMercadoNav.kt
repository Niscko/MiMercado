package co.edu.upb.mimercado.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.edu.upb.mimercado.MercadoViewModel
import co.edu.upb.mimercado.data.UiState
import co.edu.upb.mimercado.ui.screens.CreditsScreen
import co.edu.upb.mimercado.ui.screens.HomeScreen
import co.edu.upb.mimercado.ui.screens.ListDetailScreen
import co.edu.upb.mimercado.ui.screens.ListsScreen
import co.edu.upb.mimercado.ui.screens.LoginScreen
import co.edu.upb.mimercado.ui.screens.NewListScreen
import co.edu.upb.mimercado.ui.screens.ProductDetailScreen
import co.edu.upb.mimercado.ui.screens.ProductFormScreen
import co.edu.upb.mimercado.ui.screens.RecoverScreen
import co.edu.upb.mimercado.ui.screens.RegisterScreen
import co.edu.upb.mimercado.ui.screens.SettingsScreen
import co.edu.upb.mimercado.ui.screens.WelcomeScreen
import co.edu.upb.mimercado.ui.theme.Inter

private object Routes {
    const val Welcome = "welcome"
    const val Login = "login"
    const val Register = "register"
    const val Recover = "recover"
    const val Home = "home"
    const val Lists = "lists"
    const val NewList = "new_list"
    const val Settings = "settings"
    const val Credits = "credits"
}

private data class Tab(val route: String, val label: String, val icon: ImageVector)

@Composable
fun MiMercadoNav(viewModel: MercadoViewModel, ui: UiState) {
    val navController = rememberNavController()
    val snackbars = remember { SnackbarHostState() }
    val tabs = listOf(
        Tab(Routes.Home, "Inicio", Icons.Filled.Home),
        Tab(Routes.Lists, "Listas", Icons.AutoMirrored.Filled.FormatListBulleted),
        Tab(Routes.Settings, "Configuración", Icons.Filled.Settings),
    )
    val backStack = navController.currentBackStackEntryAsState().value
    val current = backStack?.destination?.route
    val showBar = current in tabs.map { it.route }
    val start = if (ui.currentUser != null) Routes.Home else Routes.Welcome

    LaunchedEffect(viewModel) {
        viewModel.toasts.collect { snackbars.showSnackbar(it) }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbars) },
        bottomBar = {
            if (showBar) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    tabs.forEach { tab ->
                        val selected = current == tab.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = {
                                Text(tab.label, fontFamily = Inter, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = start,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.Welcome) {
                WelcomeScreen(
                    onLogin = { navController.navigate(Routes.Login) },
                    onRegister = { navController.navigate(Routes.Register) },
                )
            }
            composable(Routes.Login) {
                LoginScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onRegister = {
                        navController.navigate(Routes.Register) {
                            popUpTo(Routes.Login) { inclusive = true }
                        }
                    },
                    onRecover = { navController.navigate(Routes.Recover) },
                )
            }
            composable(Routes.Register) {
                RegisterScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onLogin = {
                        navController.navigate(Routes.Login) {
                            popUpTo(Routes.Register) { inclusive = true }
                        }
                    },
                )
            }
            composable(Routes.Recover) {
                RecoverScreen(viewModel, onBack = { navController.popBackStack() })
            }
            composable(Routes.Home) {
                HomeScreen(
                    ui = ui,
                    onOpenList = { navController.navigate("list/$it") },
                    onSeeAll = { navController.navigate(Routes.Lists) },
                    onNewList = { navController.navigate(Routes.NewList) },
                )
            }
            composable(Routes.Lists) {
                ListsScreen(
                    ui = ui,
                    viewModel = viewModel,
                    onOpenList = { navController.navigate("list/$it") },
                    onNewList = { navController.navigate(Routes.NewList) },
                )
            }
            composable(Routes.NewList) {
                NewListScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onCreated = { id ->
                        navController.navigate("list/$id") {
                            popUpTo(Routes.NewList) { inclusive = true }
                        }
                    },
                )
            }
            composable(
                route = "list/{listId}",
                arguments = listOf(navArgument("listId") { type = NavType.LongType }),
            ) { entry ->
                val listId = entry.arguments?.getLong("listId") ?: return@composable
                ListDetailScreen(
                    listId = listId,
                    ui = ui,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onAdd = { navController.navigate("list/$listId/product") },
                    onOpenProduct = { navController.navigate("list/$listId/product/$it") },
                )
            }
            composable(
                route = "list/{listId}/product?productId={productId}",
                arguments = listOf(
                    navArgument("listId") { type = NavType.LongType },
                    navArgument("productId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    },
                ),
            ) { entry ->
                val listId = entry.arguments?.getLong("listId") ?: return@composable
                val productId = entry.arguments?.getLong("productId") ?: -1L
                ProductFormScreen(
                    listId = listId,
                    productId = productId,
                    ui = ui,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(
                route = "list/{listId}/product/{productId}",
                arguments = listOf(
                    navArgument("listId") { type = NavType.LongType },
                    navArgument("productId") { type = NavType.LongType },
                ),
            ) { entry ->
                val listId = entry.arguments?.getLong("listId") ?: return@composable
                val productId = entry.arguments?.getLong("productId") ?: return@composable
                ProductDetailScreen(
                    listId = listId,
                    productId = productId,
                    ui = ui,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate("list/$listId/product?productId=$productId") },
                )
            }
            composable(Routes.Settings) {
                SettingsScreen(ui, viewModel, onCredits = { navController.navigate(Routes.Credits) })
            }
            composable(Routes.Credits) {
                CreditsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
