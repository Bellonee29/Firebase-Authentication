package com.bellonee.model

import com.bellonee.enums.EngineType
import com.bellonee.enums.FuelType
import com.bellonee.enums.VehicleType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Vehicle (
    val id: Int,
    val vehicleType: VehicleType,
    val industryModelCode: String,
    val engineNumber: String, // inline value class
    val engineType: EngineType, // make as enum type
    val fuelType: FuelType, // Make as enum type
    @Contextual
    val firstRegistrationDate: LocalDateTime, // convert to date format
    val numberOfSeats: Int,
    val acFitted: String,
    val colour:String,
    val bodyType: String,
    val vehicleYear: String,
    val models: String,
    val series: String,
    val marketPrice: Double, // this should be Double
    val displaySupported: String,
)
