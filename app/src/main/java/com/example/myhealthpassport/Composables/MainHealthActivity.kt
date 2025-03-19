package com.example.myhealthpassport.Composables

import AgentScreen
import com.example.myhealthpassport.ViewModels.AgentViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.FlipAnimation
import com.example.myhealthpassport.Navigation.Screen
import com.example.myhealthpassport.R
import com.example.myhealthpassport.ViewModels.HealthViewModel
import com.example.myhealthpassport.aicompanion.ChatPage
import com.example.myhealthpassport.aicompanion.ChatViewModel
import com.example.myhealthpassport.graph.ChartScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(navController: NavController) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val selectedIcon = remember { mutableStateOf(Icons.Default.Home) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val drawerBackgroundColor = Color(0xFFE3F2FD)
    val selectedItemColor = Color(0xFFB2EBF2)
    val unselectedItemColor = Color.White
    val textPrimaryColor = Color(0xFF1A237E)
    val textUnselectedColor = Color.Black
    val iconSelectedColor = Color(0xFF0288D1)
    val iconUnselectedColor = Color(0xFF37474F)

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            AnimatedVisibility(
                visible = drawerState.isOpen,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
            ) {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.8f)) {
                Box(
                    modifier = Modifier
                        .background(gradient)
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(vertical = 8.dp)
                            .padding(start = 4.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.medicalcheck),
                            contentDescription = "logo",
                            modifier = Modifier.clip(shape = RoundedCornerShape(88.dp))
                                .size(80.dp)
                        )
                        Text(
                            text = "MyHealthPassport",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                                .padding(8.dp)
                        )
                    }
                }

                Column(modifier = Modifier.background(color = Color.White)) {

                    NavigationDrawerItem(
                        label = { Text(text = "Home", color = if (currentRoute == Screen.FlipAnimation.route) iconSelectedColor else iconUnselectedColor) },
                        selected = currentRoute == Screen.FlipAnimation.route,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "home",
                                tint = if (currentRoute == Screen.FlipAnimation.route) iconSelectedColor else iconUnselectedColor
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate(Screen.FlipAnimation.route) {
                                popUpTo(Screen.FlipAnimation.route) { inclusive = false }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = selectedItemColor,
                            unselectedContainerColor = unselectedItemColor,
                            unselectedTextColor = textPrimaryColor,
                            selectedTextColor = textPrimaryColor,
                            selectedIconColor = iconSelectedColor,
                            unselectedIconColor = iconUnselectedColor
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Agent", color = if (currentRoute == Screen.AgentScreen.route) iconSelectedColor else iconUnselectedColor) },
                        selected = currentRoute == Screen.AgentScreen.route,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "agent",
                                tint = if (currentRoute == Screen.AgentScreen.route) iconSelectedColor else iconUnselectedColor
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate(Screen.AgentScreen.route) {
                                popUpTo(Screen.FlipAnimation.route) { inclusive = false }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = selectedItemColor,
                            unselectedContainerColor = unselectedItemColor,
                            unselectedTextColor = textPrimaryColor,
                            selectedTextColor = textPrimaryColor,
                            selectedIconColor = iconSelectedColor,
                            unselectedIconColor = iconUnselectedColor
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Emergency Contacts", color = if (currentRoute == Screen.EmergencyContacts.route) iconSelectedColor else iconUnselectedColor) },
                        selected = currentRoute == Screen.EmergencyContacts.route,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "emergency contacts",
                                tint = if (currentRoute == Screen.EmergencyContacts.route) iconSelectedColor else iconUnselectedColor
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate(Screen.EmergencyContacts.route) {
                                popUpTo(Screen.FlipAnimation.route) { inclusive = false }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = selectedItemColor,
                            unselectedContainerColor = unselectedItemColor,
                            unselectedTextColor = textPrimaryColor,
                            selectedTextColor = textPrimaryColor,
                            selectedIconColor = iconSelectedColor,
                            unselectedIconColor = iconUnselectedColor
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Health info", color = if (currentRoute == Screen.HealthInfo.route) iconSelectedColor else iconUnselectedColor) },
                        selected = currentRoute == Screen.HealthInfo.route,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "health info",
                                tint = if (currentRoute == Screen.HealthInfo.route) iconSelectedColor else iconUnselectedColor
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate(Screen.HealthInfo.route) {
                                popUpTo(Screen.FlipAnimation.route) { inclusive = false }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = selectedItemColor,
                            unselectedContainerColor = unselectedItemColor,
                            unselectedTextColor = textPrimaryColor,
                            selectedTextColor = textPrimaryColor,
                            selectedIconColor = iconSelectedColor,
                            unselectedIconColor = iconUnselectedColor
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Get Health info", color = if (currentRoute == Screen.GetHealthInfo.route) iconSelectedColor else iconUnselectedColor) },
                        selected = currentRoute == Screen.GetHealthInfo.route,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "get health info",
                                tint = if (currentRoute == Screen.GetHealthInfo.route) iconSelectedColor else iconUnselectedColor
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate(Screen.GetHealthInfo.route) {
                                popUpTo(Screen.FlipAnimation.route) { inclusive = false }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = selectedItemColor,
                            unselectedContainerColor = unselectedItemColor,
                            unselectedTextColor = textPrimaryColor,
                            selectedTextColor = textPrimaryColor,
                            selectedIconColor = iconSelectedColor,
                            unselectedIconColor = iconUnselectedColor
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "AI Symptom Checker", color = if (currentRoute == Screen.ChatPage.route) iconSelectedColor else iconUnselectedColor) },
                        selected = currentRoute == Screen.ChatPage.route,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "personal therapist",
                                tint = if (currentRoute == Screen.ChatPage.route) iconSelectedColor else iconUnselectedColor
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate(Screen.ChatPage.route) {
                                popUpTo(Screen.FlipAnimation.route) { inclusive = false }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = selectedItemColor,
                            unselectedContainerColor = unselectedItemColor,
                            unselectedTextColor = textPrimaryColor,
                            selectedTextColor = textPrimaryColor,
                            selectedIconColor = iconSelectedColor,
                            unselectedIconColor = iconUnselectedColor
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Medical Report Analyser",
                                color = if (currentRoute == Screen.HealthAiScreen.route) iconSelectedColor else iconUnselectedColor
                            )
                        },
                        selected = currentRoute == Screen.HealthAiScreen.route,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "medical report analyser",
                                tint = if (currentRoute == Screen.HealthAiScreen.route) iconSelectedColor else iconUnselectedColor
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate(Screen.HealthAiScreen.route) {
                                popUpTo(Screen.FlipAnimation.route) { inclusive = false }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = selectedItemColor,
                            unselectedContainerColor = unselectedItemColor,
                            unselectedTextColor = textPrimaryColor,
                            selectedTextColor = textPrimaryColor,
                            selectedIconColor = iconSelectedColor,
                            unselectedIconColor = iconUnselectedColor
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    Spacer(modifier = Modifier.background(color = Color.White).weight(0.4f))

                    NavigationDrawerItem(
                        label = { Text(text = "Exit and Sign Out", color = Color.Black) },
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
                                popUpTo(Screen.FlipAnimation.route) { inclusive = false }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = selectedItemColor,
                            unselectedContainerColor = unselectedItemColor,
                            unselectedTextColor = textPrimaryColor,
                            selectedTextColor = textPrimaryColor,
                            selectedIconColor = iconSelectedColor,
                            unselectedIconColor = iconUnselectedColor
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    Row(
                        modifier = Modifier.background(color = Color.White).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.instagram.com/myhealthpassport?igsh=YzljYTk1ODg3Zg==")
                                )
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically)
                        ) {
                            Text(
                                text = "Community and Support",
                                textDecoration = TextDecoration.Underline,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }
        }
//            TextButton(onClick = { val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/myhealthpassport?igsh=YzljYTk1ODg3Zg=="))
//                context.startActivity(intent)
//            }, modifier = Modifier
//                .padding(50.dp)) {
//                Text(text = "Community and Support", textDecoration = TextDecoration.Underline)
//            }
        }
    ) {
        Scaffold(
            topBar = {
                Column {
                        TopAppBar(
                            title = { Text(text = "MyHealthPassport") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.White,
                                titleContentColor = Color.Black,
                                navigationIconContentColor = Color.White
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch { drawerState.open() }
                                    },
                                    colors = IconButtonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.Black,
                                        disabledContentColor = Color.Black,
                                        disabledContainerColor = Color.Transparent
                                    )
                                ) {
                                    Icon(Icons.Rounded.Menu, contentDescription = "MenuButton")
                                }
                            }
                        )
                        Divider(color = Color.LightGray, thickness = 0.6.dp)
                    }
            },
            bottomBar = {
                    BottomNavigationBar(navController)
            },
            modifier = Modifier.fillMaxSize().background(Color.White)
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.FlipAnimation.route,
                Modifier.padding(innerPadding)
            ) {

                    composable(Screen.FlipAnimation.route,
                            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
                        FlipAnimation(navController = navController)
                    }
                    composable(Screen.HealthInfo.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
                        HealthInfo(
                            navController = navController,
                            healthViewModel = HealthViewModel()
                        )
                    }
                    composable(Screen.GetHealthInfo.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
                        GetHealthInfo(
                            navController = navController,
                            healthViewModel = HealthViewModel()
                        )
                    }
                    composable(Screen.HealthAiScreen.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
                        HealthAiScreen(navController = navController)
                    }
                    composable(Screen.EmergencyContacts.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
                        EmergencyContactsListPreview(navController = navController)
                    }
                    composable(Screen.ChatPage.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
                        ChatPage(
                            navController,
                            context,
                            ChatViewModel(context)
                        )
                    }
                    composable(Screen.AgentScreen.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
                        AgentScreen(
                            navController = navController,
                            agentViewModel = AgentViewModel(),
                            healthViewModel = HealthViewModel()
                        )
                    }
                    composable(Screen.ChartScreen.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
                        ) {
                        ChartScreen(navController)
                    }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.FlipAnimation.route),
        BottomNavItem("Health Info", Icons.Default.Info, Screen.ChartScreen.route)
    )
    var selectedItem = remember { mutableStateOf(0) }

    NavigationBar(containerColor = Color(0xFFF0F1F2)) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selectedItem.value == index) Color(0xFFF0F1F2) else Color.Black
                        )
                       },
                label = { Text(text = item.label,
                    color = Color.Black) },
                selected = selectedItem.value == index,
                onClick = {
                    selectedItem.value = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)


@Preview(showBackground = true)
@Composable
fun NavigationDrawerPreview(){
    NavigationDrawer(navController = rememberNavController())
}
