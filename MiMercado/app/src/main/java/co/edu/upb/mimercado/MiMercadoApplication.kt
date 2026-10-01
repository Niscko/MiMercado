package co.edu.upb.mimercado

import android.app.Application
import co.edu.upb.mimercado.data.AppDatabase
import co.edu.upb.mimercado.data.MercadoRepository

class MiMercadoApplication : Application() {
    lateinit var repository: MercadoRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = MercadoRepository(this, AppDatabase.get(this))
    }
}
