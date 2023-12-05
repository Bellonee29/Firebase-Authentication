package com.bellonee.entity

import com.bellonee.data.tables.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EntityUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntityUser>(Users)

    var fullName by Users.fullName
    var email by Users.email
    var password by Users.password
    var createAt by Users.createAt


}