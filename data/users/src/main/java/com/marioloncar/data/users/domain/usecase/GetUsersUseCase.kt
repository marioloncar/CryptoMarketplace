package com.marioloncar.data.users.domain.usecase

import com.marioloncar.data.users.domain.UsersMockRepository
import kotlinx.coroutines.flow.Flow

class GetUsersUseCase(
    private val usersMockRepository: UsersMockRepository,
) {

    operator fun invoke(): Flow<List<String>> = usersMockRepository.fetchUsers()
}
