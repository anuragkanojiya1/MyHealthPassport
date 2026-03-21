package com.example.myhealthpassport.ui.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.Navigation.Screen
import com.example.myhealthpassport.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    navController: NavController,
    content: @Composable () -> Unit
) {

//    val navController = rememberNavController()
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
//            AnimatedVisibility(
//                visible = drawerState.isOpen,
//                enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
//                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
//            ) {
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

//                    NavigationDrawerItem(
//                        label = { Text(text = "Recommendation Agent", color = if (currentRoute == Screen.ChatPage.route) iconSelectedColor else iconUnselectedColor) },
//                        selected = currentRoute == Screen.ChatPage.route,
//                        icon = {
//                            Icon(
//                                imageVector = Icons.Default.AddCircle,
//                                contentDescription = "personal therapist",
//                                tint = if (currentRoute == Screen.ChatPage.route) iconSelectedColor else iconUnselectedColor
//                            )
//                        },
//                        onClick = {
//                            coroutineScope.launch { drawerState.close() }
//                            navController.navigate(Screen.ChatPage.route) {
//                                popUpTo(Screen.FlipAnimation.route) { inclusive = false }
//                            }
//                        },
//                        colors = NavigationDrawerItemDefaults.colors(
//                            selectedContainerColor = selectedItemColor,
//                            unselectedContainerColor = unselectedItemColor,
//                            unselectedTextColor = textPrimaryColor,
//                            selectedTextColor = textPrimaryColor,
//                            selectedIconColor = iconSelectedColor,
//                            unselectedIconColor = iconUnselectedColor
//                        ),
//                        modifier = Modifier.background(color = Color.White)
//                    )

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

            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                content()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {

    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.FlipAnimation.route),
        BottomNavItem("Health Charts", Icons.Default.Info, Screen.ChartScreen.route)
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 10.dp)
            .shadow(12.dp,
//                RoundedCornerShape(32.dp)
            )
//            .clip(RoundedCornerShape(32.dp))
            .background(gradient)
            .padding(vertical = 8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { item ->

                val selected = currentRoute == item.route

                val iconSize by animateDpAsState(
                    targetValue = if (selected) 30.dp else 24.dp,
                    label = ""
                )

                val textSize by animateFloatAsState(
                    targetValue = if (selected) 13f else 11f,
                    label = ""
                )

                val scale by animateFloatAsState(
                    targetValue = if (selected) 1.15f else 1f,
                    label = ""
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {

                            navController.navigate(item.route) {

                                popUpTo(Screen.FlipAnimation.route) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(horizontal = 22.dp, vertical = 6.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .scale(scale)
                            .clip(CircleShape)
                            .background(
                                if (selected)
                                    Color.White.copy(alpha = 0.25f)
                                else Color.Transparent
                            )
                            .padding(8.dp)
                    ) {

                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = Color.White,
                            modifier = Modifier.size(iconSize)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = item.label,
                        color = Color.White,
                        fontSize = textSize.sp
                    )
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
