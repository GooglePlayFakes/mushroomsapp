package ru.topbun.mushrooms.presentation.theme.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import com.caverock.androidsvg.SVG

@Composable
fun rememberSvgPainter(svg: String, size: Size? = null): Painter {
    return remember(svg) {
        val svgObj = SVG.getFromString(svg)
        object : Painter() {
            override val intrinsicSize: Size
                get() = size ?: Size(svgObj.documentWidth, svgObj.documentHeight)

            override fun DrawScope.onDraw() {
                val canvas = drawContext.canvas.nativeCanvas
                svgObj.renderToCanvas(canvas)
            }
        }
    }
}