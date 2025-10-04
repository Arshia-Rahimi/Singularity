package com.github.singularity.ui.feature.log

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.feature.settings.SettingsIntent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.logs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    navBack: () -> Unit,
) {
    val viewModel = koinViewModel<LogViewModel>()
    val log by viewModel.logStream.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var isFirstScroll by remember { mutableStateOf(true) }

    LaunchedEffect(log) {
        if (isFirstScroll) {
            scrollState.scrollTo(scrollState.maxValue)
            isFirstScroll = false
        } else {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(Res.string.logs.getString()) },
                navigationIcon = {
                    IconButton(
                        onClick = navBack,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back),
                            contentDescription = Res.string.back.getString(),
                        )
                    }
                }
            )
        }
    ) { ip ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip)
                .padding(4.dp),
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                text = log,
                fontSize = 8.sp,
                lineHeight = TextUnit(10f, TextUnitType.Sp),
            )
        }
    }
}
