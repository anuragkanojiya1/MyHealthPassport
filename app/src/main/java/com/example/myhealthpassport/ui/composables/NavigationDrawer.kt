package com.example.myhealthpassport.ui.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.myhealthpassport.ui.navigation.Screen
import com.example.myhealthpassport.R
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val gradient = Brush.horizontalGradient(
        colors = listOf(HealthBlue, HealthBlueDark)
    )

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.85f),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ) {
                // Header section with relative height
                Box(
                    modifier = Modifier
                        .background(gradient)
                        .fillMaxWidth()
                        .heightIn(min = 140.dp, max = 180.dp)
                        .aspectRatio(1.8f) // Dynamic height based on width
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                  Firebase.auth.currentUser?.photoUrl ?: R.drawable.medicalcheck
                            ),
                            contentDescription = "logo",
                            modifier = Modifier
                                .sizeIn(maxWidth = 120.dp, maxHeight = 120.dp)
                                .fillMaxWidth(0.8f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                        )
                        Spacer(modifier = Modifier.heightIn(max = 16.dp))
                        Text(
                            text = Firebase.auth.currentUser?.displayName.toString() ?: "MyHealthPassport",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(vertical = 12.dp)
                ) {
                    val items = listOf(
                        DrawerItem("Home", Icons.Rounded.Home, Screen.FlipAnimation.route),
                        DrawerItem("Health Agent", Icons.Rounded.AutoAwesome, Screen.AgentScreen.route),
                        DrawerItem("Emergency Contacts", Icons.Rounded.ContactPhone, Screen.EmergencyContacts.route),
                        DrawerItem("Health Info", Icons.Rounded.HealthAndSafety, Screen.HealthInfo.route),
                        DrawerItem("Get Health Info", Icons.Rounded.Search, Screen.GetHealthInfo.route),
                        DrawerItem("Medical Report Analyser", Icons.Rounded.Analytics, Screen.HealthAiScreen.route),
                        DrawerItem("Settings", Icons.Rounded.Settings, Screen.SettingsScreen.route)
                    )

                    items.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationDrawerItem(
                            label = { 
                                Text(
                                    text = item.label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ) 
                            },
                            selected = isSelected,
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                unselectedContainerColor = Color.Transparent,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Community and Support", fontWeight = FontWeight.Medium) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Public,
                                contentDescription = "community and support",
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://www.x.com/mHealthPassport".toUri()
                            )
                            context.startActivity(intent)
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            unselectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Exit and Sign Out", fontWeight = FontWeight.Medium) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "exit and sign out",
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            Firebase.auth.signOut()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            unselectedTextColor = MaterialTheme.colorScheme.error,
                            unselectedIconColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Text(
                                text = "MyHealthPassport",
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        navigationIcon = {
                            IconButton(
                                onClick = { coroutineScope.launch { drawerState.open() } }
                            ) {
                                Icon(Icons.Rounded.Menu, contentDescription = "MenuButton")
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier,
                        thickness = 0.25.dp,
                        color = Color(0xFFDCDBDB)
                    )
                }
            },
            bottomBar = {
                BottomNavigationBar(navController)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                content()
            }
        }
    }
}

private data class DrawerItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Rounded.Home, Screen.FlipAnimation.route),
        BottomNavItem("Health Charts", Icons.Rounded.Analytics, Screen.ChartScreen.route)
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val gradient = Brush.horizontalGradient(
        colors = listOf(HealthBlue, HealthBlueDark)
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 12.dp,
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .navigationBarsPadding()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val selected = currentRoute == item.route
                    val scale by animateFloatAsState(if (selected) 1.15f else 1f, label = "scale")

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .scale(scale)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (selected) Color.White else Color.White.copy(alpha = 0.65f),
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.label,
                            color = if (selected) Color.White else Color.White.copy(alpha = 0.65f),
                            fontSize = 13.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@Preview(showBackground = true)
@Composable
fun NavigationDrawerPreview(){
    NavigationDrawer(navController = rememberNavController(), content = {})
}
