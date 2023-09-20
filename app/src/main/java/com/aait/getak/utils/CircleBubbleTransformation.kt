package com.aait.getak.utils

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader

class CircleBubbleTransformation : com.squareup.picasso.Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)
        val r = size / 2f

        val output = Bitmap.createBitmap(size + triangleMargin, size + triangleMargin, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paintBorder = Paint()
        paintBorder.isAntiAlias = true
        paintBorder.color = Color.parseColor("#333030")
        paintBorder.strokeWidth = margin.toFloat()
        canvas.drawCircle(r, r, r - margin, paintBorder)

        val trianglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        trianglePaint.strokeWidth = 1f
        trianglePaint.color = Color.parseColor("#333030")
        trianglePaint.style = Paint.Style.FILL_AND_STROKE
        trianglePaint.isAntiAlias = true
        val triangle = Path()
        triangle.fillType = Path.FillType.EVEN_ODD
        triangle.moveTo((size - margin).toFloat(), (size / 2).toFloat())
        triangle.lineTo((size / 2).toFloat(), (size + triangleMargin).toFloat())
        triangle.lineTo(margin.toFloat(), (size / 2).toFloat())
        triangle.close()
        canvas.drawPath(triangle, trianglePaint)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        canvas.drawCircle(r, r, r - photoMargin, paint)

        if (source != output) {
            source.recycle()
        }

        return output
    }

    override fun key(): String {
        return "circlebubble"
    }

    companion object {
        private val photoMargin = 30
        private val margin = 20
        private val triangleMargin = 10
    }
}
