package com.sukajee.fetchlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sukajee.fetchlist.ui.theme.FetchListTheme
import com.sukajee.itemlist.presentation.ItemsScreenRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ItemsScreenRoot(
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}