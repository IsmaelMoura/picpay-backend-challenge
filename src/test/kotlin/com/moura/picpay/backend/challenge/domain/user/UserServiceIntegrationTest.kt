package com.moura.picpay.backend.challenge.domain.user

import com.moura.picpay.backend.challenge.IntegrationTest
import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.user.api.CreateUserRequest
import com.moura.picpay.backend.challenge.domain.user.persistence.UserRepository
import io.azam.ulidj.ULID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import kotlin.random.Random

class UserServiceIntegrationTest : IntegrationTest() {
    @Autowired
    private lateinit var underTest: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should create user successfully`() =
        runTest {
            // given
            val request = CreateUserRequest.create()

            // when
            val user = underTest.createUser(request)

            // then
            user.id shouldNotBe null
            user.countrySpecificId shouldBe request.countrySpecificId
            user.fullName shouldBe request.fullName
            user.email shouldBe request.email
            user.password shouldBe request.password
            user.type shouldBe request.type
            user.balance shouldBe request.balance

            userRepository.findById(user.id).shouldNotBeNull()
        }

    @Test
    fun `should get user by id successfully`() =
        runTest {
            // given
            val user = underTest.createUser(CreateUserRequest.create())

            // when
            val result = underTest.getById(user.id)

            // then
            result shouldBeEqual user
        }

    @Test
    fun `should throw UserNotFound when user does not exist`() =
        runTest {
            // given
            val user = UserId.random()

            // when
            // should
            shouldThrow<PicPayException.UserNotFound> { underTest.getById(user) }
        }

    @Test
    fun `should update user correctly`() =
        runTest {
            // given
            val user = underTest.createUser(CreateUserRequest.create())
            val update =
                user.copy(
                    countrySpecificId = ULID.random(),
                    fullName = randomAlphabetic(20),
                    email = randomAlphabetic(20),
                    password = ULID.random(),
                    type = UserType.entries.filterNot { it == user.type }.first(),
                    balance = BigDecimal.valueOf(200),
                )

            // when
            val result = underTest.updateUser(update)

            // then
            result shouldNotBeEqual user
            result.id shouldBeEqual user.id shouldBeEqual update.id
            result.countrySpecificId shouldBe update.countrySpecificId
            result.fullName shouldBe update.fullName
            result.email shouldBe update.email
            result.password shouldBe update.password
            result.type shouldBe update.type
            result.balance shouldBe update.balance

            userRepository
                .findById(result.id)
                .shouldNotBeNull {
                    createdAt.shouldNotBeNull()
                    modifiedAt.shouldNotBeNull()
                }
        }

    @Test
    fun `should update modifiedAt when update user correctly`() =
        runTest {
            // given
            val user = underTest.createUser(CreateUserRequest.create())
            val update = user.copy(balance = BigDecimal.valueOf(200))

            // when
            delay(1000)
            val result = underTest.updateUser(update)

            // then
            userRepository
                .findById(result.id)
                .shouldNotBeNull {
                    createdAt.shouldNotBeNull() shouldBeBefore modifiedAt.shouldNotBeNull()
                }
        }

    @Test
    fun `should return all user successfully`() =
        runTest {
            // given
            val ids =
                List(Random.nextInt(10, 20)) { CreateUserRequest.create() }
                    .map { underTest.createUser(it).id }

            // when
            val result = underTest.getAllUsers().toList()

            result.map(User::id).shouldContainAll(ids)
        }

    @Test
    fun `should return empty flow when there are no users`() =
        runTest {
            // given
            userRepository.deleteAll()

            // when
            val result = underTest.getAllUsers().toList()

            // then
            result.shouldBeEmpty()
        }
}
