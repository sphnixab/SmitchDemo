package com.smitch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.smitch.screens.HomeScreen
import com.smitch.viewmodels.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(
                viewModel = viewModel,
                onScanClick = { viewModel.scan() },
                onPublishClick = { viewModel.publish() },
                onFindBLEClick = { viewModel.findBLE() },
            )
        }
    }
}