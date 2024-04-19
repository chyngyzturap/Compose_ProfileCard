package com.pharos.composeprofilecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pharos.composeprofilecard.ui.theme.MyTheme
import com.pharos.composeprofilecard.ui.theme.LightGreen200
import com.pharos.composeprofilecard.ui.theme.LightRed200

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen(userProfiles: List<UserProfile> = userProfileList) {
        Scaffold(topBar = { AppBar() }) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                LazyColumn() {
                    items(userProfiles) { profile ->
                        ProfileCard(userProfile = profile)
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppBar() {
        TopAppBar(
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "App Bar",
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            },
            title = { Text(text = "Chat App") },
            colors = TopAppBarDefaults.topAppBarColors(Color.White)
        )
    }

    @Composable
    fun ProfileCard(userProfile: UserProfile) {
        Card(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                userProfile.run {
                    ProfilePicture(photoUrl, userProfile.isActive)
                    ProfileContent(name, isActive)
                }

            }
        }
    }

    @Composable
    fun ProfileContent(name: String, isActive: Boolean) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.alpha(if (isActive) 1f else 0.5f)
            )
            Text(
                text = if (isActive) "active now" else "offline",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(if (isActive) 1f else 0.5f)
            )

        }
    }

    @Composable
    fun ProfilePicture(
        photoUrl: String,
        isActive: Boolean
    ) {
        Card(
            shape = CircleShape,
            border = BorderStroke(
                width = 2.dp,
                color = if (isActive) LightGreen200 else LightRed200
            ),
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Profile Photo",
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Crop
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Preview() {
        MyTheme {
            MainScreen()
        }
    }
}