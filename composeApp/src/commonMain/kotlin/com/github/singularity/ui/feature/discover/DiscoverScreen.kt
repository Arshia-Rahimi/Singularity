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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.common.compose.getString
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.discover

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    navBack: () -> Unit,
) {
    val viewModel = koinViewModel<DiscoverViewModel>()
    val servers by viewModel.servers.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(Res.string.discover.getString()) },
                navigationIcon = {
                    IconButton(
                        onClick = navBack,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back),
                            contentDescription = Res.string.back.getString(),
                        )
                    }
                },
            )
        },
    ) { ip ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(ip)
                .padding(4.dp),
        ) {
            items(servers) {
                Column {
                    Text(it.ip)
                    Text(it.deviceName)
                    Text(it.deviceId)
                }
                Spacer(Modifier.fillMaxWidth().height(2.dp).background(Color.Red))
            }
        }
    }
}
