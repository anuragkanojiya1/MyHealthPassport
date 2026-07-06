package com.example.myhealthpassport.ui.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myhealthpassport.R
import com.example.myhealthpassport.data.database.EmergencyContactDatabase
import com.example.myhealthpassport.data.database.EmergencyContactRepository
import com.example.myhealthpassport.data.database.EmergencyContactViewModel
import com.example.myhealthpassport.data.database.EmergencyContactViewModelFactory
import com.example.myhealthpassport.data.database.Entity
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark
import com.example.myhealthpassport.ui.theme.MyHealthPassportTheme
import androidx.core.net.toUri

@Composable
fun EmergencyContactsScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { EmergencyContactDatabase.getDatabase(context) }
    val dao = remember { db.contactDao() }
    val repository = remember { EmergencyContactRepository(dao) }

    val viewModel: EmergencyContactViewModel = viewModel(
        factory = EmergencyContactViewModelFactory(repository)
    )
    val emergencyContacts by viewModel.contacts.collectAsState()

    val contacts = listOf(
        EmergencyContact("Police", "100"),
        EmergencyContact("Ambulance", "102")
    )

    EmergencyContactsList(
        standardContacts = contacts,
        savedContacts = emergencyContacts,
        onAddContact = { name, phone -> viewModel.addContact(name, phone) },
        onDeleteContact = { viewModel.removeContact(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsList(
    standardContacts: List<EmergencyContact>,
    savedContacts: List<Entity>,
    onAddContact: (String, String) -> Unit,
    onDeleteContact: (Entity) -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val gradient = Brush.horizontalGradient(
        colors = listOf(HealthBlue, HealthBlueDark)
    )

    val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            Image(
                painter = backgroundPainter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.35f),
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 88.dp) // Adjusted for the dynamic Add button
            ) {
                Text(
                    text = "Emergency Contacts",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        Text(
                            text = "Standard Services",
                            style = MaterialTheme.typography.labelLarge,
                            color = HealthBlueDark,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    items(standardContacts) { contact ->
                        EmergencyContactRow(contact)
                    }

                    if (savedContacts.isNotEmpty()) {
                        item {
                            Text(
                                text = "My Saved Contacts",
                                style = MaterialTheme.typography.labelLarge,
                                color = HealthBlueDark,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                            )
                        }

                        items(savedContacts) { contactEntity ->
                            EmergencyContactRow(
                                contact = EmergencyContact(contactEntity.name, contactEntity.phone),
                                onDelete = {
                                    onDeleteContact(contactEntity)
                                }
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom) {
                Box(modifier = Modifier.imePadding()) {
                    AddContactSection(
                        name = name,
                        onNameChange = { name = it },
                        phone = phone,
                        onPhoneChange = { phone = it },
                        onAddClick = {
                            if (name.isNotBlank() && phone.isNotBlank()) {
                                onAddContact(name, phone)
                                name = ""
                                phone = ""
                            }
                        },
                        gradient = gradient
                    )
                }
            }
        }
    }
}

@Composable
fun EmergencyContactRow(
    contact: EmergencyContact,
    onDelete: (() -> Unit)? = null
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Call,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                }

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:${contact.phoneNumber}".toUri()
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HealthBlue),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "CALL",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AddContactSection(
    name: String,
    onNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    onAddClick: () -> Unit,
    gradient: Brush
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.End
    ) {
        if (isExpanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Add New Contact",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        label = { Text("Contact Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HealthBlue,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = HealthBlue
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = onPhoneChange,
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HealthBlue,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = HealthBlue
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            onAddClick()
                            isExpanded = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(gradient, RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("SAVE CONTACT",
                            color = Color.White,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        FilledIconButton(
            onClick = { isExpanded = !isExpanded },
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent,
                contentColor = Color.White),
            shape = CircleShape,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .sizeIn(minWidth = 56.dp, minHeight = 56.dp)
                .fillMaxWidth(0.15f)
                .aspectRatio(1f)
                .background(gradient, CircleShape)
                .shadow(elevation = 0.dp, shape = CircleShape),
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = "Toggle Add Contact",
                modifier = Modifier.size(26.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmergencyContactsScreenPreview() {
    MyHealthPassportTheme {
        EmergencyContactsList(
            standardContacts = listOf(
                EmergencyContact("Police", "100"),
                EmergencyContact("Ambulance", "102")
            ),
            savedContacts = listOf(
                Entity(id = 1, name = "Family Doctor", phone = "555-0199"),
                Entity(id = 2, name = "Local Clinic", phone = "555-0123")
            ),
            onAddContact = { _, _ -> },
            onDeleteContact = {}
        )
    }
}
