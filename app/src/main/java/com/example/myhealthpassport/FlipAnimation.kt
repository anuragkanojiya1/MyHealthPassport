package com.example.myhealthpassport

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.Navigation.Screen

@Preview
@Composable
fun FlipAnimation1() {
    var isCardFlipped by remember { mutableStateOf(false) }
    val animDuration = 900
    val zAxisDistance = 10f //distance between camera and Card

    val frontColor by animateColorAsState(
        targetValue = if (isCardFlipped) Color(0xFF789FFF) else Color(0xFF282A31),
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    // rotate Y-axis with animation
    val rotateCardY by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    // text animation
    val textAlpha by animateFloatAsState(
        targetValue = if (isCardFlipped) 0f else 1f,
        tween(durationMillis = 1500),
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp, 300.dp)
                .graphicsLayer {
                    rotationY = rotateCardY
                    cameraDistance = zAxisDistance
                }
                .clip(RoundedCornerShape(24.dp))
                .clickable { isCardFlipped = !isCardFlipped }
                .background(frontColor)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier.graphicsLayer {
                alpha = if (isCardFlipped) 1f - textAlpha else textAlpha
            },
            text = if (isCardFlipped) "Reveal" else "Hide",
            color = Color.Black,
        )
    }
}


@Composable
fun FlipAnimation(navController: NavController) {
    var isCardFlipped by remember { mutableStateOf(false) }
    val animDuration = 1000
    val zAxisDistance = 10f
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    val rotateCardY by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    val frontImageAlpha by animateFloatAsState(
        targetValue = if (isCardFlipped) 0f else 1f,
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    val backImageAlpha by animateFloatAsState(
        targetValue = if (isCardFlipped) 1f else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (isCardFlipped) 0f else 1f,
        tween(durationMillis = 1500),
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).align(Alignment.CenterHorizontally)) {
            Box(
                modifier = Modifier
                    .size(250.dp, 260.dp)
                    .graphicsLayer {
                        rotationY = rotateCardY
                        cameraDistance = zAxisDistance
                    }
                    .clip(RoundedCornerShape(24.dp))
                    .padding(top = 8.dp)
                    .clickable { isCardFlipped = !isCardFlipped }
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.handsmedicaldoctor),
                    contentDescription = "Front Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(alpha = frontImageAlpha),
                    contentScale = ContentScale.FillBounds
                )

                Image(
                    painter = painterResource(id = R.drawable.onlinedoctor),
                    contentDescription = "Back Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(alpha = backImageAlpha)
                        .scale(-1f, 1f),
                    contentScale = ContentScale.FillBounds
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            if (isCardFlipped) {
                OutlinedButton(
                    onClick = {
                        navController.navigate(Screen.HealthInfo.route)
                    },
                    border = BorderStroke(1.dp, color = Color(0xFF1E88E5)),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text(text = "Save Data", color = Color.Black)
                }
            } else OutlinedButton(
                onClick = {
                    navController.navigate(Screen.GetHealthInfo.route)
                },
                border = BorderStroke(1.dp, color = Color(0xFF1E88E5)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Get Data", color = Color.Black)
            }

            Text(
                text = "Emergency Contacts",
                fontWeight = FontWeight.W400,
                color = Color.Black,
                fontSize = 28.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 24.dp)
                    .padding(start = 8.dp)
            )
            Row(modifier = Modifier.align(Alignment.Start).padding(start = 8.dp)) {

                Text(
                    text = AnnotatedString("Show Emergency Contacts"),
                    modifier = Modifier
                        .padding(top = 12.dp),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                )

                IconButton(
                    onClick = { navController.navigate(Screen.EmergencyContacts.route) },
                    modifier = Modifier.padding(top = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "emergency contacts",
                        tint = Color.Black
                    )
                }
            }

//            Row(
//                modifier = Modifier
//                    .align(Alignment.Start)
//                    .align(Alignment.CenterHorizontally)
//                    .padding(start = 8.dp)
//            ) {
//                Text(
//                    text = AnnotatedString("Store your data in Cloud"),
//                    fontWeight = FontWeight.W400,
//                    color = Color.Black,
//                    fontSize = 28.sp,
//                    modifier = Modifier
//                        .padding(top = 20.dp),
//                )
//                IconButton(
//                    onClick = {
//                        navController.navigate(Screen.ChartScreen.route)
//                    },
//                    modifier = Modifier.padding(top = 12.dp, start = 8.dp)
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.cloudservice),
//                        contentDescription = "cloud",
//                        tint = Color.Black
//                    )
//                }
//            }

            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 4.dp)
                    .padding(bottom = 8.dp)
            ) {

                Column(modifier = Modifier.padding(top = 4.dp).padding(start = 4.dp).weight(0.5f)) {
                    ElevatedCard(
                        onClick = { navController.navigate(Screen.ChatPage.route) },
                        modifier = Modifier
                            .padding(top = 16.dp, end = 4.dp, bottom = 8.dp)
                            .background(color = Color.White)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.picturedoctorwithstethoscope),
                            contentDescription = "Doctor",
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.Start)
                                .background(Color.White)
                        )
                    }
                    Text(
                        text = "AI Symptom Checker",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 12.sp
                    )
                }
                Column(modifier = Modifier.padding(top = 4.dp).padding(end = 4.dp).weight(0.5f)) {
                    ElevatedCard(
                        onClick = { navController.navigate(Screen.HealthAiScreen.route) },
                        modifier = Modifier
                            .padding(top = 16.dp, start = 4.dp, bottom = 8.dp)
                            .background(color = Color.White)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.groupmedicalstaff),
                            contentDescription = "MedicalStaff",
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.Start),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Text(
                        text = "Medical Report Analyser",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 12.sp
                    )
                }
            }

//        Text(
//            modifier = Modifier.graphicsLayer {
//                alpha = if (isCardFlipped) 1f - textAlpha else textAlpha
//            },
//            text = if (isCardFlipped) "Reveal" else "Hide",
//            color = Color.Black,
//        )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlipAnimation1Preview(){
    FlipAnimation(navController = rememberNavController())
}