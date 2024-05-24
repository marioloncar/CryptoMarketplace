package com.marioloncar.data.users.domain

import kotlinx.coroutines.flow.Flow

interface UsersMockRepository {

    fun fetchUsers(): Flow<List<String>>
}
