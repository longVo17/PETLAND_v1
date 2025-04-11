package com.sksingh.devthreads.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.sksingh.devthreads.screens.AddThreads
import com.sksingh.devthreads.screens.BottomNav
import com.sksingh.devthreads.screens.Home
import com.sksingh.devthreads.screens.Login
import com.sksingh.devthreads.screens.Notification
import com.sksingh.devthreads.screens.OtherUser
import com.sksingh.devthreads.screens.PetQuestionScreen
import com.sksingh.devthreads.screens.Profile
import com.sksingh.devthreads.screens.Register
import com.sksingh.devthreads.screens.ResultScreen
import com.sksingh.devthreads.screens.Search
import com.sksingh.devthreads.screens.Splash

@Composable
fun NavGraph(navController: NavHostController){

    NavHost(navController = navController, startDestination = Routes.Splash.routes ){

        composable(Routes.Splash.routes){
            Splash(navController)
        }
        composable(Routes.Home.routes){
            Home(navController)
        }
        composable(Routes.AddThreads.routes){
                AddThreads(navController)
        }

        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }
        composable(Routes.Notification.routes){
            Notification()
        }

        composable(Routes.Profile.routes){
            Profile(navController)
        }

        composable(Routes.Search.routes){
            Search(navController)
        }
        composable(Routes.Login.routes){
            Login(navController)
        }

        composable(Routes.Register.routes){
            Register(navController)
        }

        composable(Routes.OtherUser.routes){
            val data = it.arguments!!.getString("data")
            OtherUser(navController, data!!)
        }
        composable(
            route = "question/{petName}",
            arguments = listOf(navArgument("petName") { type = NavType.StringType })
        ) { backStackEntry ->
            val petName = backStackEntry.arguments?.getString("petName")
            petName?.let {
                PetQuestionScreen(navController = navController, petName = it)
            }
        }
        composable(
            route = "result/{petName}/{score}",
            arguments = listOf(
                navArgument("petName") { type = NavType.StringType },
                navArgument("score") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val petName = backStackEntry.arguments?.getString("petName")
            val score = backStackEntry.arguments?.getString("score")
            if (petName != null && score != null) {
                ResultScreen(navController = navController, petName = petName, score = score)
            }
        }


    }
}