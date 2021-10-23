package com.alesno.mytrainings.navigation

import androidx.annotation.DrawableRes
import com.alesno.mytrainings.R
import com.alexey.minay.core_training.TrainingTypeId
import com.alexey.minay.core_utils.asString

sealed class Destination(
    private var args: List<String> = emptyList(),
    val routePart: String
) {

    open val route: String get() = routePart + args.asString { "/$it" }

    class Training(trainingTypeId: TrainingTypeId? = null) : Destination(
        routePart = "training",
        args = listOf(trainingTypeId?.value?.toString() ?: "{$KEY_TRAINING_INFO_ID}")
    ) {
        companion object {
            const val KEY_TRAINING_INFO_ID = "training_info_id"
        }
    }

    class Home(val item: HomeItem) : Destination(routePart = "home") {
        override val route: String
            get() = routePart + "/" + item.routePart
    }

    enum class HomeItem(
        val routePart: String,
        @DrawableRes val iconRes: Int,
        val title: String
        ) {
        TRAINING_LIST("training list", R.drawable.ic_home, "Тренировки"),
        HISTORY("history", R.drawable.ic_history, "История")
    }

}