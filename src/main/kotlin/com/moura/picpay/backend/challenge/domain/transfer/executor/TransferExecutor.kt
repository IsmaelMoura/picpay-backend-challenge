package com.moura.picpay.backend.challenge.domain.transfer.executor

import com.moura.picpay.backend.challenge.domain.transfer.model.TransferId
import com.moura.picpay.backend.challenge.infrastructure.http.transfer.api.TransferRequest

interface TransferExecutor {
    suspend fun execute(request: TransferRequest): Result<TransferId>
}
