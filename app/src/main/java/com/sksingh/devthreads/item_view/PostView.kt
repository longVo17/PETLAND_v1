package com.sksingh.devthreads.item_view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.sksingh.devthreads.R
import com.sksingh.devthreads.models.ThreadModel
import com.sksingh.devthreads.models.UserModel
import com.sksingh.devthreads.navigations.Routes
import com.sksingh.devthreads.screens.PetCommunity
import com.sksingh.devthreads.ui.theme.myfont2
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)

     val com1=  PetCommunity("Cat Lovers", "😺", 1523, "https://res.cloudinary.com/dstpzjcom/image/upload/v1744251174/DACS3/z5ymszfhhgdjcyzmgbji.jpg")
   //  val com2 = PetCommunity("Dog Paradise", "🐶", 2390,"https://th.bing.com/th/id/OIP.vUP_4HvL1iQY1NQd9IQ9cAHaFj?rs=1&pid=ImgDetMain")

@Composable
fun PostItem(
        thread: ThreadModel,
        users: UserModel,
        navHostController: NavHostController,
        userId: String
) {
        val context = LocalContext.current

        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF121212))
                        .padding(12.dp)
        ) {
                // Subreddit-style Header
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                        painter = rememberAsyncImagePainter(model = com1.urlAvatar),
                                        contentDescription = "Profile",
                                        modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .clickable {
                                                        val route = Routes.OtherUser.routes.replace("{data}", users.uid!!)
                                                        navHostController.navigate(route)
                                                },
                                        contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                        text = "r/ ${com1.name}  •  ${thread.timezone}",
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                )
                        }

                        Text(
                                text = "Join",
                                color = Color.Blue,
                                fontSize = 14.sp,
                                modifier = Modifier
                                        .background(Color(0xFF1A1A1A), shape = RoundedCornerShape(10.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                        .clickable {
                                                Toast.makeText(context, "Joined!", Toast.LENGTH_SHORT).show()
                                        }
                        )
                }



                // Header with avatar and timestamp
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                ) {
                        Spacer(modifier = Modifier.height(60.dp))
                        Image(
                                painter = rememberAsyncImagePainter(model = users.imageUrl),
                                contentDescription = "Profile",
                                modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                                val route = Routes.OtherUser.routes.replace("{data}", users.uid!!)
                                                navHostController.navigate(route)
                                        },
                                contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                                Text(text = users.username, color = Color.White, fontSize = 14.sp)
                                //Text(text = thread.timezone, color = Color.Gray, fontSize = 12.sp)
                        }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Thread content
                Text(text = thread.thread, color = Color.White, fontSize = 16.sp)

                // Image (if exists)
                if (thread.image.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),
                                shape = RoundedCornerShape(10.dp)
                        ) {
                                Image(
                                        painter = rememberAsyncImagePainter(model = thread.image),
                                        contentDescription = "Post Image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                )
                        }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Buttons: like, comment, save
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        var heartColor by remember { mutableStateOf(Color.White) }
                        var saveColor by remember { mutableStateOf(Color.White) }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                        painter = painterResource(id = R.drawable.heart),
                                        contentDescription = "Like",
                                        modifier = Modifier
                                                .size(20.dp)
                                                .clickable {
                                                        heartColor =
                                                                if (heartColor == Color.White) Color.Red else Color.White
                                                },
                                        colorFilter = ColorFilter.tint(heartColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("1.2k", color = Color.White, fontSize = 14.sp)

                                Spacer(modifier = Modifier.width(16.dp))
                                Image(
                                        painter = painterResource(id = R.drawable.comment),
                                        contentDescription = "Comment",
                                        modifier = Modifier.size(20.dp),
                                        colorFilter = ColorFilter.tint(Color.White)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("32", color = Color.White, fontSize = 14.sp)
                        }

                        Image(
                                painter = painterResource(id = R.drawable.save),
                                contentDescription = "Save",
                                modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                                saveColor =
                                                        if (saveColor == Color.White) {
                                                                Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                                                                Color.Green
                                                        } else Color.White
                                        },
                                colorFilter = ColorFilter.tint(saveColor)
                        )
                }
        }
}
