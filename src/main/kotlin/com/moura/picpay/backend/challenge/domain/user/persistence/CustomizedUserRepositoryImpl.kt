package com.moura.picpay.backend.challenge.domain.user.persistence

import com.moura.picpay.backend.challenge.domain.user.api.FetchUsersQueryParametersRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.CriteriaDefinition
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.isEqual
import org.springframework.data.relational.core.query.isIn
import org.springframework.stereotype.Component

@Component
class CustomizedUserRepositoryImpl(
    private val template: R2dbcEntityTemplate,
) : CustomizedUserRepository {
    override fun fetchAllUsers(request: FetchUsersQueryParametersRequest): Flow<UserEntity> {
        return template.select(Query.query(createCriteria(request)), UserEntity::class.java).asFlow()
    }

    private fun createCriteria(request: FetchUsersQueryParametersRequest): CriteriaDefinition {
        return buildSet {
            request.countrySpecificIds
                ?.let { ids ->
                    add(Criteria.where(COUNTRY_SPECIFIC_ID_COLUMN_NAME).isIn(ids))
                }
            request.fullNames
                ?.filter { it.isNotBlank() }
                ?.map { name ->
                    Criteria.where(FULL_NAME_COLUMN_NAME)
                        .like(name + LIKE_OPERATOR)
                        .ignoreCase(true)
                }
                ?.combineWithOr()
                ?.let(::add)
            request.emails?.let { emails ->
                add(Criteria.where(EMAIL_COLUMN_NAME).isIn(emails))
            }
            request.type?.let { type ->
                add(Criteria.where(TYPE_COLUMN_NAME).isEqual(type))
            }
        }.combineWithAnd()
    }

    private fun Collection<Criteria>.combineWithOr(): Criteria {
        return reduceOrNull { combined, criteria -> combined.or(criteria) } ?: Criteria.empty()
    }

    private fun Collection<Criteria>.combineWithAnd(): Criteria {
        return reduceOrNull { combined, criteria -> combined.and(criteria) } ?: Criteria.empty()
    }

    private companion object {
        const val COUNTRY_SPECIFIC_ID_COLUMN_NAME = "countrySpecificId"
        const val FULL_NAME_COLUMN_NAME = "fullName"
        const val EMAIL_COLUMN_NAME = "email"
        const val TYPE_COLUMN_NAME = "type"

        const val LIKE_OPERATOR = "%"
    }
}
