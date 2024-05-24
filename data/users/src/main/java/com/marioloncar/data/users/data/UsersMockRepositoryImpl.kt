package com.marioloncar.data.users.data

import com.marioloncar.data.users.domain.UsersMockRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import java.util.UUID.randomUUID
import kotlin.random.Random

internal class UsersMockRepositoryImpl : UsersMockRepository {

    private var isFirstEmission = true

    // We would usually map API models to domain models here.
    // For the sake of simplicity and example we return a simple list.
    override fun fetchUsers(): Flow<List<String>> = flow {
        if (Random.nextDouble() < 0.10) { // 10% chance of error
            throw RuntimeException("Random error occurred.")
        } else {
            emit(randomUsers())
        }
    }
        .onEach {
            if (isFirstEmission) delay(1000)
            isFirstEmission = false
        }

    private fun randomUsers() = List(10) { "User ${randomUUID()}" }
}
