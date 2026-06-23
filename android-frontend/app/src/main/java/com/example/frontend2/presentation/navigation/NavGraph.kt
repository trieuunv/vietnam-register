package com.example.frontend2.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.frontend2.presentation.ui.appointmentCreate.AppointmentCreateScreen
import com.example.frontend2.presentation.ui.appointmentDetail.AppointmentDetailScreen
import com.example.frontend2.presentation.ui.appointmentManager.AppointmentManagerScreen
import com.example.frontend2.presentation.ui.citizenCard.CitizenCardScreen
import com.example.frontend2.presentation.ui.home.HomeScreen
import com.example.frontend2.presentation.ui.inspectionCenterDetail.InspectionCenterDetailScreen
import com.example.frontend2.presentation.ui.inspectionCenterLookUp.InspectionCenterLookUpScreen
import com.example.frontend2.presentation.ui.inspectionDetail.InspectionDetailScreen
import com.example.frontend2.presentation.ui.inspectionManager.InspectionManagerScreen
import com.example.frontend2.presentation.ui.login.LoginScreen
import com.example.frontend2.presentation.ui.profileDetail.ProfileDetailScreen
import com.example.frontend2.presentation.ui.register.RegisterScreen
import com.example.frontend2.presentation.ui.settingDetail.SettingDetailScreen
import com.example.frontend2.presentation.ui.vehicleEdit.VehicleEditScreen
import com.example.frontend2.presentation.ui.vehicleManager.VehicleManagerScreen
import com.example.frontend2.presentation.ui.vehicleRegister.VehicleRegisterScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(
                animationSpec = tween(600, easing = FastOutSlowInEasing),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            slideOutOfContainer(
                animationSpec = tween(600, easing = FastOutSlowInEasing),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
    ) {
        composable(
            route = Screen.Login.route,
            enterTransition = {
                if (initialState.destination.route == Screen.Register.route) {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(800)
                    )/* + fadeIn(animationSpec = tween(1200))*/
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (targetState.destination.route == Screen.Register.route) {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(800)
                    )/* + fadeOut(animationSpec = tween(1200))*/
                } else {
                    ExitTransition.None
                }
            },
            /*popEnterTransition = {
                if (initialState.destination.route == Screen.Register.route) {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(1200)
                    ) + fadeIn(animationSpec = tween(1200))
                } else {
                    EnterTransition.None
                }
            },
            popExitTransition = {
                if (targetState.destination.route == Screen.Register.route) {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(1200)
                    ) + fadeOut(animationSpec = tween(1200))
                } else {
                    ExitTransition.None
                }
            }*/
        ) {
            LoginScreen(navController)
        }

        composable(
            route = Screen.Register.route,
            enterTransition = {
                if (initialState.destination.route == Screen.Login.route) {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(600)
                    )/* + fadeIn(animationSpec = tween(1200))*/
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (targetState.destination.route == Screen.Login.route) {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(600)
                    )/* + fadeOut(animationSpec = tween(1200))*/
                } else {
                    ExitTransition.None
                }
            },
            /*popEnterTransition = {
                if (initialState.destination.route == Screen.Login.route) {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(1200)
                    ) + fadeIn(animationSpec = tween(1200))
                } else {
                    EnterTransition.None
                }
            },
            popExitTransition = {
                if (targetState.destination.route == Screen.Login.route) {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(1200)
                    ) + fadeOut(animationSpec = tween(1200))
                } else {
                    ExitTransition.None
                }
            }*/
        ) {
            RegisterScreen(navController)
        }

        composable(Screen.Home.route) {
            val homeNavController = rememberNavController()
            HomeScreen(navController, homeNavController)
        }

        composable(Screen.AppointmentManager.route) {
            AppointmentManagerScreen(navController)
        }

        composable(
            route = Screen.AppointmentCreate.route,
            arguments = listOf(
                navArgument("centerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val centerId = backStackEntry.arguments?.getString("centerId") ?: ""
            AppointmentCreateScreen(
                navController = navController,
                centerId = centerId
            )
        }

        composable(
            route = Screen.AppointmentDetail.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) {
            val appointmentId = it.arguments?.getString("appointmentId") ?: ""
            AppointmentDetailScreen(
                navController = navController,
                appointmentId = appointmentId
            )
        }

        composable(Screen.InspectionCenterLookUp.route) {
            InspectionCenterLookUpScreen(navController)
        }

        composable(
            route = Screen.InspectionCenterDetail.route,
            arguments = listOf(
                navArgument("inspectionCenterId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val inspectionCenterId = backStackEntry.arguments?.getString("inspectionCenterId") ?: ""
            InspectionCenterDetailScreen(
                navController = navController,
                centerId = inspectionCenterId
            )
        }

        composable(Screen.InspectionManager.route) {
            InspectionManagerScreen(navController)
        }

        composable(
            route = Screen.InspectionDetail.route,
            arguments = listOf(navArgument("inspectionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val inspectionId = backStackEntry.arguments?.getString("inspectionId") ?: ""
            InspectionDetailScreen(
                navController = navController,
                inspectionId = inspectionId
            )
        }

        composable(Screen.VehicleManager.route) {
            VehicleManagerScreen(navController)
        }

        composable(Screen.VehicleRegister.route) {
            VehicleRegisterScreen(navController)
        }

        composable(
            route = Screen.VehicleEdit.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) {
            VehicleEditScreen(navController = navController)
        }

        /*composable(Screen.ViolationLookup.route) {
            ViolationLookupScreen(navController)
        }*/

        composable(Screen.SettingDetail.route) {
            SettingDetailScreen(navController)
        }

        composable(Screen.ProfileDetail.route) {
            ProfileDetailScreen(navController)
        }

        composable(Screen.CitizenCard.route) {
            CitizenCardScreen(navController)
        }
    }
}