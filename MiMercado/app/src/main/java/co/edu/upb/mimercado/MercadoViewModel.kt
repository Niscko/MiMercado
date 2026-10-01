package co.edu.upb.mimercado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import co.edu.upb.mimercado.data.MercadoRepository
import co.edu.upb.mimercado.data.Product
import co.edu.upb.mimercado.data.Settings
import co.edu.upb.mimercado.data.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MercadoViewModel(
    private val repository: MercadoRepository,
) : ViewModel() {
    private val _ui = MutableStateFlow(UiState())
    val ui: StateFlow<UiState> = _ui

    private val _toasts = MutableSharedFlow<String>()
    val toasts: SharedFlow<String> = _toasts

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            combine(repository.sessionUserId, repository.settings) { userId, settings ->
                userId to settings
            }.collectLatest { (userId, settings) ->
                if (userId == null) {
                    _ui.value = UiState(ready = true, settings = settings)
                } else {
                    val user = repository.currentUser()
                    repository.lists(userId).collect { lists ->
                        _ui.value = UiState(
                            ready = true,
                            currentUser = user,
                            lists = lists,
                            settings = settings,
                        )
                    }
                }
            }
        }
    }

    fun register(name: String, email: String, password: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            repository.register(name, email, password)
                .onFailure { onError(it.message ?: "No se pudo crear la cuenta") }
        }
    }

    fun login(email: String, password: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            repository.login(email, password)
                .onFailure { onError(it.message ?: "No se pudo iniciar sesión") }
        }
    }

    fun recover(email: String, password: String, onDone: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            repository.recover(email, password)
                .onSuccess {
                    toast("Contraseña actualizada")
                    onDone()
                }
                .onFailure { onError(it.message ?: "No se pudo actualizar la contraseña") }
        }
    }

    fun logout() {
        viewModelScope.launch { repository.logout() }
    }

    fun addList(name: String, icon: String, color: String, onCreated: (Long) -> Unit) {
        val userId = _ui.value.currentUser?.id ?: return
        viewModelScope.launch {
            val id = repository.addList(userId, name, icon, color)
            toast("Lista creada")
            onCreated(id)
        }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch {
            repository.deleteList(listId)
            toast("Lista eliminada")
        }
    }

    fun addProduct(listId: Long, product: Product) {
        viewModelScope.launch {
            repository.addProduct(listId, product)
            toast("Producto agregado")
        }
    }

    fun updateProduct(listId: Long, product: Product) {
        viewModelScope.launch {
            repository.updateProduct(listId, product)
            toast("Cambios guardados")
        }
    }

    fun toggleProduct(listId: Long, product: Product) {
        viewModelScope.launch { repository.toggleProduct(listId, product) }
    }

    fun deleteProduct(listId: Long, productId: Long) {
        viewModelScope.launch {
            repository.deleteProduct(listId, productId)
            toast("Producto eliminado")
        }
    }

    fun updateSettings(settings: Settings) {
        viewModelScope.launch { repository.updateSettings(settings) }
    }

    private fun toast(message: String) {
        viewModelScope.launch { _toasts.emit(message) }
    }

    companion object {
        fun factory(repository: MercadoRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MercadoViewModel(repository) as T
            }
        }
    }
}
