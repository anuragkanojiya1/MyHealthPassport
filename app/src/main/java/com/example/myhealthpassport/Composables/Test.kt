package com.example.myhealthpassport.Composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myhealthpassport.ViewModels.HealthViewModel

@Composable
fun Test(){
    val context = LocalContext.current
    val medicalIDs = remember { mutableStateListOf<String>() }
    val healthViewModel: HealthViewModel = viewModel()

    LaunchedEffect(Unit) {
        healthViewModel.fetchMedicalIDs(context) { ids ->
            medicalIDs.clear()
            medicalIDs.addAll(ids)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Medical IDs",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        medicalIDs.forEach { id ->
            Text(
                text = id,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(12.dp),
                color = Color.Black
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun t(){
    Test()
}