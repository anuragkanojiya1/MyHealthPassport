package com.example.myhealthpassport.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.Navigation.Screen
import com.example.myhealthpassport.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(600)
            _isLoading.value = false
        }
    }
}

@Composable
fun SplashScreen(navController: NavController, mainViewModel: MainViewModel = viewModel()) {
    val isLoading by mainViewModel.isLoading.collectAsState()

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    if(currentUser==null){
    LaunchedEffect(key1 = isLoading) {
        if (!isLoading) {
            navController.navigate(Screen.Login.route) {
                popUpTo("splash") { inclusive = true }
            }
        }
        }
    } else{
        LaunchedEffect(key1 = isLoading) {
            if (!isLoading) {
                navController.navigate(Screen.NavigationDrawer.route) {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().background(color = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().background(color = Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.medicalcheck),
                contentDescription = "Movie Galaxy",
                modifier = Modifier.size(128.dp).scale(2f,2f)
                    .clip(shape = RoundedCornerShape(45.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    com.example.myhealthpassport.Composables.SplashScreen(navController = rememberNavController(), mainViewModel = MainViewModel())
}