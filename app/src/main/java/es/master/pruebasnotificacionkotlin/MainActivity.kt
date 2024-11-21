package es.master.pruebasnotificacionkotlin

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import es.master.pruebasnotificacionkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Usamos `lateinit` para inicializar `binding` posteriormente
    private lateinit var binding: ActivityMainBinding

    // Variables constantes para el canal de notificación y su configuración
    companion object {
        private const val CHANNEL_NAME = "noti1" // Nombre del canal de notificación
        private const val CHANNEL_ID = "channel1" // ID único para el canal
        private const val NOTIFICATION_ID = 1 // ID para identificar notificaciones específicas
        private const val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH // Nivel de importancia para el canal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Configuración para un diseño inmersivo

        // Inicializamos el View Binding para inflar la vista
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajustar el padding de la vista principal para evitar conflictos con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración del botón para mostrar la notificación
        binding.btnNotificar.setOnClickListener {
            createNotificationChannel() // Crear el canal de notificación
            showNotification() // Mostrar la notificación
        }
    }

    /**
     * Crea un canal de notificación si no existe.
     */
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE).apply {
            description = "Canal para notificaciones de prueba"
            enableVibration(true) // Habilita la vibración para las notificaciones
        }

        // Registrar el canal en el NotificationManager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    /**
     * Muestra una notificación básica.
     */
    private fun showNotification() {
        // Construir la notificación
        val notificationBuilder = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ícono de la notificación
            .setContentTitle("Notificación") // Título de la notificación
            .setContentText("Esto es una notificación de prueba") // Texto de la notificación

        // Comprobar y solicitar el permiso para notificaciones si no está otorgado
        if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
        }
        // Mostrar la notificación usando NotificationManagerCompat
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    /**
     * Habilita el diseño de borde a borde.
     */
    private fun enableEdgeToEdge() {
        // Configuración para un diseño inmersivo (si es necesario)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}