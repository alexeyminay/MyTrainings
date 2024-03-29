package com.alexey.minay.feature_training.domain

import com.alexey.minay.core_training.TrainingId
import com.alexey.minay.core_training.TrainingTypeId
import java.util.*

data class Training(
    val id: TrainingId,
    val trainingTypeId: TrainingTypeId,
    val title: String,
    val date: Date,
    val exercises: List<TrainingExercise>
) {
    companion object {
        fun default(
            trainingTypeId: TrainingTypeId?,
            trainingId: TrainingId?
        ) = Training(
            id = trainingId ?: TrainingId(-1),
            trainingTypeId = trainingTypeId ?: TrainingTypeId(-1),
            title = "",
            date = Date(),
            exercises = emptyList()
        )
    }
}