package com.smitch.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smitch.viewmodels.MyViewModel
import com.smitch.model.ScanResult

@Composable
fun HomeScreen(
    viewModel: MyViewModel,
    onScanClick: () -> Unit,
    onPublishClick: () -> Unit,
    onFindBLEClick: () -> Unit,
) {
    Column {
        Button(
            onClick = onPublishClick,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Publish")
        }
        Button(
            onClick = onScanClick,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Scan")
        }
        Button(
            onClick = onFindBLEClick,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Find BLE")
        }
        Divider(modifier = Modifier.padding(16.dp))
        LazyColumn {
            items(viewModel.scanResults) { result ->
                ScanResultItem(result = result)
            }
        }
    }
}

@Composable
fun ScanResultItem(result: ScanResult) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Service Name: ${result.serviceName}")
        Text(text = "Service Type: ${result.serviceType}")
        Text(text = "IP Address: ${result.ipAddress}")
        Text(text = "Port: ${result.port}")
    }
}
