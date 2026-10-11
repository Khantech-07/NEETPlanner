package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NeetDarkColorScheme = darkColorScheme(
    primary = PrimaryCyan,
    onPrimary = Color(0xFF00363D),
    primaryContainer = PhysicsCyanContainer,
    onPrimaryContainer = PrimaryCyan,
    secondary = SecondaryMint,
    onSecondary = Color(0xFF003822),
    secondaryContainer = BiologyMintContainer,
    onSecondaryContainer = SecondaryMint,
    tertiary = TertiaryCoral,
    onTertiary = Color(0xFF45190E),
    tertiaryContainer = ChemistryCoralContainer,
    onTertiaryContainer = TertiaryCoral,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkOutline,
    outlineVariant = Color(0xFF1E2B45)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeetDarkColorScheme,
        typography = Typography,
        content = content
    )
}
