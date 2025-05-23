package toy.practice.androidtest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import toy.practice.androidtest.config.AppConfig
import toy.practice.androidtest.ui.components.WebPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                // 임시 드로어 아이템
                NavigationDrawerItem(
                    selected = false,
                    onClick = { /* TODO */ },
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    // AppConfig에서 앱 이름 가져오기
                    title = { Text(AppConfig.appDisplayName) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                )
            },
        ) { paddingValues ->
            WebPage(
                url = "https://www.google.com",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
            )
        }
    }
}
