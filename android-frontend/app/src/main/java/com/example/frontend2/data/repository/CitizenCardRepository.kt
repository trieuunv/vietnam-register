package com.example.frontend2.data.repository

import com.example.frontend2.data.network.datasource.CitizenCardDataSource
import com.example.frontend2.data.network.dto.request.CitizenCardRequest
import com.example.frontend2.util.Resource
import javax.inject.Inject

class CitizenCardRepository @Inject constructor(
    private val citizenCardDataSource: CitizenCardDataSource
) {

    suspend fun citizenCard(citizenCardRequest: CitizenCardRequest): Resource<Boolean> {
        val citizenCardResponse = citizenCardDataSource.citizenCard(citizenCardRequest)

        return when (citizenCardResponse.success) {
            true -> {
                Resource.Success(true)
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        citizenCardResponse.message
                            ?: "Không thể cập nhật thông tin, vui lòng thử lại sau"
                    ),
                    errorData = citizenCardResponse.errors
                )
            }
        }
    }
}
