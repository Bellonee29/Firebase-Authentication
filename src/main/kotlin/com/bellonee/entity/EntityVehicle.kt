package com.bellonee.entity

import com.bellonee.data.tables.Vehicles
import com.bellonee.enums.EngineType
import com.bellonee.enums.FuelType
import com.bellonee.enums.VehicleType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

class EntityVehicle(id: EntityID<Int>) : IntEntity(id)  {
    companion object: IntEntityClass<EntityVehicle>(Vehicles)
    val vehicleType by Vehicles.vehicleType
    val industryModelCode by Vehicles.industryModelCode
    val engineNumber by Vehicles.engineNumber
    val engineType by Vehicles.engineType
    val fuelType by Vehicles.fuelType
    val firstRegistrationDate by Vehicles.firstRegistrationDate // convert to date format
    val numberOfSeats by Vehicles.numberOfSeats
    val acFitted by Vehicles.acFitted
    val colour by Vehicles.colour
    val bodyType by Vehicles.bodyType
    val vehicleYear by Vehicles.vehicleYear
    val models by Vehicles.models
    val series by Vehicles.series
    val marketPrice by Vehicles.marketPrice// this should be Double
    val displaySupported by Vehicles.displaysSupported
}