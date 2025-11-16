package com.example.apppoketeam.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- NOVO TEMA DARK ROXO ---
private val DarkColorScheme = darkColorScheme(
  primary = PrimaryPurpleDark,      // Roxo vibrante como cor principal
  secondary = SecondaryPurpleDark,  // Roxo mais escuro como cor secundária
  tertiary = Pink80,                // Mantemos um toque de rosa, se quiser
  background = BackgroundDark,      // Fundo muito escuro
  surface = SurfaceDark,            // Superfícies do card e outros elementos
  error = ErrorRed,                 // Cor para mensagens de erro

  onPrimary = Color.White,          // Texto sobre a cor principal
  onSecondary = Color.White,        // Texto sobre a cor secundária
  onTertiary = Color.White,         // Texto sobre a cor terciária
  onBackground = Color.White,       // Texto sobre o fundo
  onSurface = Color.White           // Texto sobre as superfícies
)

// O tema Light não será usado por enquanto, mas é bom ter uma definição
private val LightColorScheme = lightColorScheme(
  primary = PrimaryPurpleLight,
  secondary = SecondaryPurpleLight,
  tertiary = Pink40,
  background = BackgroundLight,
  surface = SurfaceLight,
  error = ErrorRed,

  onPrimary = Color.White,
  onSecondary = Color.Black,
  onTertiary = Color.Black,
  onBackground = Color.Black,
  onSurface = Color.Black
)

@Composable
fun AppPokeTeamTheme(
  // Forçamos o 'darkTheme' a ser 'true' para o tema roxo escuro
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.primary.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
