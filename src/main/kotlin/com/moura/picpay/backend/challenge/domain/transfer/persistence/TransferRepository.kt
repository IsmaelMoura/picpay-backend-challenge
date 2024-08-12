package com.moura.picpay.backend.challenge.domain.transfer.persistence

import com.moura.picpay.backend.challenge.domain.transfer.model.TransferId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TransferRepository : CoroutineCrudRepository<TransferEntity, TransferId>
