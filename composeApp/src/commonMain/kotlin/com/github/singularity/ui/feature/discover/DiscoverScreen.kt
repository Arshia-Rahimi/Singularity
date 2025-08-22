package com.github.singularity.ui.feature.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.singularity.core.common.compose.collectToSnapShotStateList
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    navBack: () -> Unit,
) {
    val viewModel = koinViewModel<DiscoverViewModel>()
    val scope = rememberCoroutineScope()
    val devices = viewModel.devices.collectToSnapShotStateList(scope)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { ip ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(ip)
                .padding(4.dp),
        ) {
            items(devices) {
                Column {
                    Text(it.ip)
                    Text(it.deviceName)
                }
                Spacer(Modifier.fillMaxWidth().height(2.dp).background(Color.Red))
            }
        }
    }
}
