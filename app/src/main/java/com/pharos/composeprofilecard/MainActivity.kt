package com.pharos.composeprofilecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.pharos.composeprofilecard.ui.theme.MyTheme
import com.pharos.composeprofilecard.ui.theme.LightGreen200
import com.pharos.composeprofilecard.ui.theme.LightRed200

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                UsersApplication()
            }
        }
    }

    @Composable
    fun UsersApplication(userProfiles: List<UserProfile> = userProfileList) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "users_list") {
            composable("users_list") {
                UserListScreen(userProfiles, navController)
            }
            composable(
                route = "user_details/{userId}",
                arguments = listOf(navArgument("userId") {
                    type = NavType.IntType
                })
            ) { navStackBackEntry ->
                UserProfileDetailsScreen(
                    navStackBackEntry.arguments!!.getInt("userId"),
                    navController
                )
            }
        }
    }

    @Composable
    fun UserListScreen(userProfiles: List<UserProfile>, navController: NavHostController?) {
        Scaffold(
            topBar = { AppBar("Chat App", Icons.Default.Home) {} }) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                LazyColumn {
                    items(userProfiles) { profile ->
                        ProfileCard(userProfile = profile) {
                            navController?.navigate("user_details/${profile.id}")
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppBar(title: String, icon: ImageVector, clickAction: () -> Unit) {
        TopAppBar(
            navigationIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "App Bar",
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable { clickAction.invoke() },
                )
            },
            title = { Text(text = title) },
            colors = TopAppBarDefaults.topAppBarColors(Color.White)
        )
    }

    @Composable
    fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit) {
        Card(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .clickable { clickAction.invoke() },
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                userProfile.run {
                    ProfilePicture(photoUrl, userProfile.isActive, 72.dp)
                    ProfileContent(name, isActive, Start)
                }

            }
        }
    }

    @Composable
    fun ProfileContent(name: String, isActive: Boolean, alignment: Alignment.Horizontal) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = alignment
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .alpha(if (isActive) 1f else 0.5f)
            )
            Text(
                text = if (isActive) "active now" else "offline",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .alpha(if (isActive) 1f else 0.5f)
            )

        }
    }

    @Composable
    fun ProfilePicture(
        photoUrl: String,
        isActive: Boolean,
        imageSize: Dp
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
                modifier = Modifier.size(imageSize),
                contentScale = ContentScale.Crop
            )
        }
    }

    @Composable
    fun UserProfileDetailsScreen(userId: Int, navController: NavHostController?) {
        val userProfile = userProfileList.first { it.id == userId }
        Scaffold(topBar = {
            AppBar(
                userProfile.name,
                Icons.Default.ArrowBack
            ) {
                navController?.navigateUp()
            }
        }) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                Column(
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    userProfile.run {
                        ProfilePicture(photoUrl, userProfile.isActive, 240.dp)
                        ProfileContent(name, isActive, CenterHorizontally)
                    }

                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun UserProfileDetailsPreview() {
        MyTheme {
            UserProfileDetailsScreen(1, null)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun UserListPreview() {
        MyTheme {
            UserListScreen(userProfileList, null)
        }
    }
}