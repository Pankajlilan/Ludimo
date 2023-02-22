package com.pankaj.ludimos.presentation

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.coroutineScope
import com.pankaj.ludimos.R
import com.pankaj.ludimos.databinding.ActivityMainBinding
import com.pankaj.ludimos.model.BallTracking
import kotlinx.coroutines.flow.collect


class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var ballTracking: BallTracking

    private lateinit var mCanvas: Canvas
    private lateinit var mBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.getBallTrackData(applicationContext)
        observeData()
    }

    private fun observeData() {
        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.getBallTrackLiveData.collect {
                ballTracking = it
                loadVideo()
            }
        }
    }

    private fun loadVideo() {
        binding.videoView.setVideoPath(ballTracking.source_url)
        binding.videoView.start()
        initStumpLine()
        implementListeners()
    }

    private fun initStumpLine() {
        // Created a Bitmap of 822 x 1920 size
        mBitmap =
            Bitmap.createBitmap(ballTracking.height, ballTracking.width, Bitmap.Config.ARGB_8888)

        val dimensionInDpWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            ballTracking.width.toFloat(),
            resources.displayMetrics
        ).toInt()
        val dimensionInDpHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            ballTracking.height.toFloat(),
            resources.displayMetrics
        ).toInt()

        binding.imageView.layoutParams.height = dimensionInDpHeight
        binding.imageView.layoutParams.width = dimensionInDpWidth
        binding.imageView.requestLayout()
        mCanvas = Canvas(mBitmap)
        binding.imageView.setImageBitmap(mBitmap)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun implementListeners() {
        binding.showStumpLineBtn.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    initStumpLine()
                    drawStumpLine()
                    binding.showStumpLineBtn.text = getString(R.string.hide_stump_line)
                }
                MotionEvent.ACTION_UP -> {
                    removeStumpLine()
                }
            }

            v?.onTouchEvent(event) ?: true
        }
    }

    private fun drawStumpLine() {
        // Paint is called, assume it as a paint bristle
        // with green paint and 10 as thickness
        val mPaint = Paint()
        mPaint.color = getColor(R.color.stump_line)
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 10F
        mPaint.isAntiAlias = true

        val path = Path()
        path.moveTo(0F, mBitmap.height / 2.717F)
        path.lineTo(
            ballTracking.stump_line[0] * mBitmap.width,
            ballTracking.stump_line[1] * mBitmap.height
        )
        path.lineTo(
            ballTracking.stump_line[2] * mBitmap.width,
            ballTracking.stump_line[3] * mBitmap.height
        )
        path.lineTo(
            ballTracking.stump_line[4] * mBitmap.width,
            ballTracking.stump_line[5] * mBitmap.height
        )
        path.lineTo(
            ballTracking.stump_line[6] * mBitmap.width,
            ballTracking.stump_line[7] * mBitmap.height
        )

        mCanvas.drawPath(path, mPaint)
        binding.imageView.setImageBitmap(mBitmap)
    }

    private fun removeStumpLine() {
        mBitmap.recycle()
        binding.imageView.setImageBitmap(mBitmap)
        binding.showStumpLineBtn.text = getString(R.string.show_stump_line)
    }
}