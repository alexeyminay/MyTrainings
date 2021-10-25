package com.alesno.mytrainings.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alesno.mytrainings.di.AppComponent
import com.alesno.mytrainings.navigation.factories.TrainingHistoryStoreFactories
import com.alesno.mytrainings.navigation.factories.TrainingListStoreFactory
import com.alesno.mytrainings.navigation.factories.TrainingStoreFactory
import com.alexey.minay.feature_training.di.TrainingComponent
import com.alexey.minay.feature_training.view.TrainingScreen
import com.alexey.minay.feature_training_history.ui.TrainingHistory
import com.alexey.minay.feature_training_list.view.TrainingListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Destination = Destination.Home(Destination.HomeItem.TRAINING_LIST),
    appComponent: AppComponent
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.routePart
    ) {
        navigation(
            route = startDestination.routePart,
            startDestination = startDestination.route
        ) {
            composable(
                route = Destination.Home(Destination.HomeItem.TRAINING_LIST).route,
            ) {
                val store = TrainingListStoreFactory.create(appComponent)

                TrainingListScreen(
                    store = store,
                    startTraining = { trainingInfoId ->
                        val route = Destination.Training(trainingTypeId = trainingInfoId).route
                        navController.navigate(route)
                    }
                )
            }

            composable(
                route = Destination.Home(Destination.HomeItem.HISTORY).route
            ) {
                val store = TrainingHistoryStoreFactories.create(appComponent)

                TrainingHistory(
                    store = store,
                    editTraining = { trainingId ->
                        val route = Destination.Training(trainingId = trainingId).route
                        navController.navigate(route)

                    }
                )
            }
        }

        composable(
            route = Destination.Training().route
        ) { backStackEntry ->
            val store = TrainingStoreFactory.create(
                appComponent = appComponent,
                arguments = backStackEntry.arguments
            )

            TrainingScreen(
                store = store,
                onBackPressed = {
                    navController.popBackStack()
                    TrainingComponent.release()
                }
            )
        }
    }
}
