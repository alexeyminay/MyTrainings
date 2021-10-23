package com.alesno.mytrainings.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alesno.mytrainings.HistoryScreen
import com.alesno.mytrainings.di.AppComponent
import com.alexey.minay.core_training.TrainingTypeId
import com.alexey.minay.feature_training.di.ITrainingDependencies
import com.alexey.minay.feature_training.di.TrainingComponent
import com.alexey.minay.feature_training.presentation.TrainingStore
import com.alexey.minay.feature_training.view.TrainingScreen
import com.alexey.minay.feature_training_list.di.TrainingListComponent
import com.alexey.minay.feature_training_list.di.TrainingListDependency
import com.alexey.minay.feature_training_list.presentation.TrainingListStore
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
                val trainingListComponent = remember {
                    val trainingListDependency = object : TrainingListDependency {
                        override fun provideTrainingListDao() =
                            appComponent.appDatabase.getTrainingListDao()

                        override fun provideCoroutineDispatchersProvider() =
                            appComponent.coroutineDispatchersProvider

                    }
                    TrainingListComponent.initAndGet(trainingListDependency)
                }
                val store = viewModel<TrainingListStore>(
                    factory = trainingListComponent.trainingListStoreProvider
                )
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
                HistoryScreen()
            }
        }

        composable(
            route = Destination.Training().route
        ) {
            val trainingComponent = remember {
                val trainingInfoId =
                    it.arguments?.getString(Destination.Training.KEY_TRAINING_INFO_ID)
                val trainingDependencies = object : ITrainingDependencies {
                    override fun provideTrainingInfoId() = TrainingTypeId(trainingInfoId!!.toLong())
                    override fun provideTrainingDao() = appComponent.appDatabase.getTrainingDao()
                }
                TrainingComponent.initAndGet(trainingDependencies)
            }
            val store = viewModel<TrainingStore>(
                factory = trainingComponent.trainingStoreProvider
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