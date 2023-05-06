package dev.abdujabbor.jetpackcomposepermissionhandling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dev.abdujabbor.jetpackcomposepermissionhandling.ui.theme.JetPackComposePermissionHandlingTheme

@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackComposePermissionHandlingTheme {
                val permissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.CAMERA,
                    )
                )

                val lifeSyvleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifeSyvleOwner, effect = {
                    val observer = LifecycleEventObserver { source, event ->
                        if (event==Lifecycle.Event.ON_RESUME){
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }
                    lifeSyvleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifeSyvleOwner.lifecycle.removeObserver(observer)
                    }
                })



                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    permissionState.permissions.forEach { perm ->

                        when (perm.permission) {
                            android.Manifest.permission.CAMERA -> {
                                when {
                                    perm.hasPermission -> {
                                        Text(text = "Camera permission accepted")

                                    }

                                    perm.shouldShowRationale -> {
                                        Text(text = "Camera permission is  needed to accsees  the camera ")

                                    }

                                    perm.isPermanentlyDenied() -> {
                                        Text(text = "Camera permission was permenantly denied You canenable it in the app settings ")

                                    }
                                }
                            }

                            android.Manifest.permission.RECORD_AUDIO -> {
                                when {
                                    perm.hasPermission -> {
                                        Text(text = "Record audio permission accepted")

                                    }

                                    perm.shouldShowRationale -> {
                                        Text(text = "Record audio permission is  needed to accsees  the camera ")

                                    }

                                    perm.isPermanentlyDenied() -> {
                                        Text(text = "Record audio permission was permenantly denied You canenable it in the app settings ")

                                    }
                                }
                            }
                        }

                    }

                }
            }
        }
    }
}



