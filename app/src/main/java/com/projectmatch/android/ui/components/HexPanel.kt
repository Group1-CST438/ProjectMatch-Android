package com.projectmatch.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.projectmatch.android.ui.theme.PanelBorder
import com.projectmatch.android.ui.theme.PanelGlow1
import com.projectmatch.android.ui.theme.SurfaceDeep
import com.projectmatch.android.ui.theme.SurfacePrimary

fun hexPath(size: Size, cutPx: Float): Path = Path().apply {
    val w = size.width
    val h = size.height
    val c = cutPx.coerceAtMost(minOf(w, h) / 2f)
    moveTo(c, 0f)
    lineTo(w - c, 0f)
    lineTo(w, c)
    lineTo(w, h - c)
    lineTo(w - c, h)
    lineTo(c, h)
    lineTo(0f, h - c)
    lineTo(0f, c)
    close()
}

class HexShape(private val cutDp: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cutPx = with(density) { cutDp.toPx() }
        return Outline.Generic(hexPath(size, cutPx))
    }
}

@Composable
fun HexPanel(
    modifier: Modifier = Modifier,
    cut: Dp = 14.dp,
    borderWidth: Dp = 2.dp,
    borderColor: Color = PanelBorder,
    fill: Brush = Brush.linearGradient(listOf(SurfacePrimary, SurfaceDeep)),
    glowColor: Color = PanelGlow1,
    content: @Composable () -> Unit,
) {
    val outerShape = HexShape(cut)
    val innerCut = (cut - borderWidth).coerceAtLeast(0.dp)
    val innerShape = HexShape(innerCut)

    Box(
        modifier = modifier
            .drawBehind {
                // Glow shadow drawn behind the border
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        asFrameworkPaint().apply {
                            isAntiAlias = true
                            color = android.graphics.Color.TRANSPARENT
                            setShadowLayer(36f, 0f, 0f, glowColor.copy(alpha = 0.65f).toArgb())
                        }
                    }
                    canvas.drawPath(hexPath(size, cut.toPx()), paint)
                }
            }
            .clip(outerShape)
            .background(borderColor),
    ) {
        Box(
            modifier = Modifier
                .padding(borderWidth)
                .clip(innerShape)
                .background(fill),
        ) {
            content()
        }
    }
}

// Small hex-shaped container for chips/pills/buttons
@Composable
fun HexChip(
    modifier: Modifier = Modifier,
    cut: Dp = 4.dp,
    background: Color,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(HexShape(cut))
            .background(background),
    ) {
        content()
    }
}
