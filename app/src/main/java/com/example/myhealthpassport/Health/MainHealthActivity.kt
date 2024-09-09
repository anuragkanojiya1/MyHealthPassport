package com.example.myhealthpassport.Health

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
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
import com.example.myhealthpassport.FlipAnimation1
import com.example.myhealthpassport.Navigation.Screen
import com.example.myhealthpassport.ViewModels.HealthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    val navigationController = rememberNavController()
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
                    Text(text = "MyHealth Passport", color = Color.White)
                }

                NavigationDrawerItem(
                    label = { Text(text = "Health info", color = Color.Black) },
                    selected = selectedIcon.value == Icons.Default.Home,
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "health info") },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        selectedIcon.value = Icons.Default.Home
                        navigationController.navigate(Screen.HealthInfo.route) {
                            popUpTo(0)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Get Health info", color = Color.Black) },
                    selected = selectedIcon.value == Icons.Default.LocationOn,
                    icon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = "get health info") },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        selectedIcon.value = Icons.Default.LocationOn
                        navigationController.navigate(Screen.GetHealthInfo.route) {
                            popUpTo(0)
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text(text = "Health AI screen", color = Color.Black) },
                    selected = selectedIcon.value == Icons.Default.ShoppingCart,
                    icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "health ai screen") },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        selectedIcon.value = Icons.Default.ShoppingCart
                        navigationController.navigate(Screen.HealthAiScreen.route) {
                            popUpTo(0)
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text(text = "Emergency Contacts", color = Color.Black) },
                    selected = selectedIcon.value == Icons.Default.DateRange,
                    icon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "emergency contacts") },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        selectedIcon.value = Icons.Default.DateRange
                        navigationController.navigate(Screen.EmergencyContacts.route) {
                            popUpTo(0)
                        }
                    }
                )
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
                            containerColor = Color.Transparent,
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
                navController = navigationController,
                startDestination = Screen.FlipAnimation.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screen.FlipAnimation.route){
                    FlipAnimation(navController = navController)
                }
                composable(Screen.HealthInfo.route) {
                    HealthInfo(
                        navController = navigationController,
                        healthViewModel = HealthViewModel()
                    )
                }
                composable(Screen.GetHealthInfo.route) {
                    GetHealthInfo(
                    navController = navigationController,
                    healthViewModel = HealthViewModel()
                    )
                }
                composable(Screen.HealthAiScreen.route) {
                    HealthAiScreen(navController = navigationController)
                }
                composable(Screen.EmergencyContacts.route) {
                    EmergencyContactsListPreview(navController = navigationController)
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

