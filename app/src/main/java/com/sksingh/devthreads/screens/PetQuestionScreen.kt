package com.sksingh. devthreads.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sksingh.devthreads.data.FirebaseService

import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetQuestionScreen(navController: NavController, petName: String) {
    val service = remember { FirebaseService() }
    val coroutineScope = rememberCoroutineScope()
    var questions by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }
    val answers = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            questions = service.getQuestionsForPet(petName)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Check: $petName") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
//            Image(
//                painter = painterResource(id = R.drawable.background),
//                contentDescription = "Background",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                questions.forEach { (category, qs) ->
                    Text(text = category, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    qs.forEach { question ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = question, modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = answers[question] == true,
                                onCheckedChange = { answers[question] = it }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val correctAnswers = answers.values.count { it }
                        val totalQuestions = questions.values.sumOf { it.size }
                        val scorePercentage = if (totalQuestions > 0) (correctAnswers.toFloat() / totalQuestions * 100) else 0f
                        navController.navigate("result/${petName}/${String.format("%.1f", scorePercentage)}")
                    },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}