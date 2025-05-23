package toy.practice.androidtest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import toy.practice.androidtest.R
import toy.practice.androidtest.ui.components.WebPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // 임시 드로어 아이템
                NavigationDrawerItem(
                    label = { Text(stringResource(id = R.string.menu)) },
                    selected = false,
                    onClick = { /* TODO */ },
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.app_display_name)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = stringResource(id = R.string.menu_description),
                            )
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
