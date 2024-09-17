package com.example.myhealthpassport.Health

import AgentScreen
import com.example.myhealthpassport.ViewModels.AgentViewModel
import FileUploadDownloadScreen
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
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
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.FlipAnimation
import com.example.myhealthpassport.Navigation.Screen
import com.example.myhealthpassport.R
import com.example.myhealthpassport.ViewModels.ChatViewModel
import com.example.myhealthpassport.ViewModels.FileViewModel
import com.example.myhealthpassport.ViewModels.HealthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun MainHealthActivity(navController: NavController){

    val context = LocalContext.current

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    val gradientx = Brush.horizontalGradient(
        colors = listOf(Color(0xFFDF1039),Color(0xFFDF1039))
    )

    Column(modifier = Modifier
        .fillMaxWidth()) {

        Text(text = "Life is all about being Healthy!", fontSize = 26.sp, modifier = Modifier
            .padding(30.dp),
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(top = 10.dp, bottom = 10.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Box {
                AnimatedPreloaderMainHealthActivity(modifier = Modifier
                    .size(400.dp, 350.dp)
                    .align(Alignment.Center)
                   // .scale(scaleX = 1.3f, scaleY = 1.6f)
                )
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()) {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.HealthInfo.route) },
                modifier = Modifier
                    .padding(30.dp)
                    .padding(0.dp, 10.dp)
                    .weight(1f)
                    .background(gradient, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Save your Data!")
            }
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.GetHealthInfo.route) },
                modifier = Modifier
                    .padding(30.dp)
                    .padding(0.dp, 10.dp)
                    .weight(1f)
                    .background(gradient, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Get your Data!")
            }
        }
        ExtendedFloatingActionButton(onClick = { navController.navigate(Screen.EmergencyContacts.route) },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
                .background(gradientx, shape = RoundedCornerShape(8.dp))
        ) {
            Text(text = "Emergency Helpline numbers")
        }
        ExtendedFloatingActionButton(onClick = { navController.navigate(Screen.HealthAiScreen.route) },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
                .background(gradientx, shape = RoundedCornerShape(8.dp))
        ) {
            Text(text = "Medical Certificate Analyser")
        }

        TextButton(onClick = { val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/myhealthpassport?igsh=YzljYTk1ODg3Zg=="))
                                context.startActivity(intent)
                             }, modifier = Modifier
            .padding(50.dp)
            .align(alignment = Alignment.CenterHorizontally)) {
            Text(text = "Community and Support", textDecoration = TextDecoration.Underline)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainHealthActivityPreview(){
    MainHealthActivity(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(navController: NavController) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedIcon = remember { mutableStateOf(Icons.Default.Home) }
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
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
                            .padding(8.dp)
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
                            text = "MyHealth Passport",
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
                        label = { Text(text = "Home", color = Color.Black) },
                        selected = selectedIcon.value == Icons.Default.Home,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "home"
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedIcon.value = Icons.Default.Home
                            navController.navigate(Screen.FlipAnimation.route) {
                                popUpTo(0)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.LightGray,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Health info", color = Color.Black) },
                        selected = selectedIcon.value == Icons.Default.Info,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "health info"
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedIcon.value = Icons.Default.Info
                            navController.navigate(Screen.HealthInfo.route) {
                                popUpTo(0)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.LightGray,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Get Health info", color = Color.Black) },
                        selected = selectedIcon.value == Icons.Default.Search,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "get health info"
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedIcon.value = Icons.Default.Search
                            navController.navigate(Screen.GetHealthInfo.route) {
                                popUpTo(0)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.LightGray,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Personal Therapist", color = Color.Black) },
                        selected = selectedIcon.value == Icons.Default.Send,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "personal therapist"
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedIcon.value = Icons.Default.Send
                            navController.navigate(Screen.ChatScreen.route) {
                                popUpTo(0)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.LightGray,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Emergency Contacts", color = Color.Black) },
                        selected = selectedIcon.value == Icons.Default.AccountCircle,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "emergency contacts"
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedIcon.value = Icons.Default.AccountCircle
                            navController.navigate(Screen.EmergencyContacts.route) {
                                popUpTo(0)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.LightGray,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Medical Certificate Analyser",
                                color = Color.Black
                            )
                        },
                        selected = selectedIcon.value == Icons.Default.Star,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "medical certificate analyser"
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedIcon.value = Icons.Default.Star
                            navController.navigate(Screen.HealthAiScreen.route) {
                                popUpTo(0)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.LightGray,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Agent", color = Color.Black) },
                        selected = selectedIcon.value == Icons.Default.Build,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "agent"
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedIcon.value = Icons.Default.Build
                            navController.navigate(Screen.AgentScreen.route) {
                                popUpTo(0)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.LightGray,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Cloud Storage", color = Color.Black) },
                        selected = selectedIcon.value == Icons.Default.Lock,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "cloud"
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedIcon.value = Icons.Default.Lock
                            navController.navigate(Screen.FileUploadDownloadScreen.route) {
                                popUpTo(0)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.LightGray,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black
                        ),
                        modifier = Modifier.background(color = Color.White)
                    )

                    Spacer(modifier = Modifier.background(color = Color.White).weight(0.4f))

                    NavigationDrawerItem(
                        label = { Text(text = "Exit and Sign Out", color = Color.Black) },
                        selected = selectedIcon.value == Icons.Default.ExitToApp,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "exit and sign out"
                            )
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedIcon.value = Icons.Default.ExitToApp
                            Firebase.auth.signOut()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.LightGray,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black
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
                        title = { Text(text = "MyHealth Passport") },
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
                    Divider(color = Color.Gray, thickness = 0.5.dp)
                }
            },
            modifier = Modifier.fillMaxSize().background(Color.White)
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.FlipAnimation.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screen.FlipAnimation.route){
                    FlipAnimation(navController = navController)
                }
                composable(Screen.HealthInfo.route) {
                    HealthInfo(
                        navController = navController,
                        healthViewModel = HealthViewModel()
                    )
                }
                composable(Screen.GetHealthInfo.route) {
                    GetHealthInfo(
                    navController = navController,
                    healthViewModel = HealthViewModel()
                    )
                }
                composable(Screen.HealthAiScreen.route) {
                    HealthAiScreen(navController = navController)
                }
                composable(Screen.EmergencyContacts.route) {
                    EmergencyContactsListPreview(navController = navController)
                }
                composable(Screen.ChatScreen.route){
                    ChatScreen(
                        navController = navController,
                        viewModel = ChatViewModel()
                    )
                }
                composable(Screen.AgentScreen.route){
                    AgentScreen(
                        navController = navController,
                        agentViewModel = AgentViewModel(),
                        healthViewModel = HealthViewModel()
                    )
                }
                composable(Screen.FileUploadDownloadScreen.route){
                    FileUploadDownloadScreen(navController, viewModel = FileViewModel())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationDrawerPreview(){
    NavigationDrawer(navController = rememberNavController())
}

