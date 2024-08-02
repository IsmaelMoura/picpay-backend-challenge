package com.moura.picpay.backend.challenge.domain.user

import com.moura.picpay.backend.challenge.IntegrationTest
import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.user.api.CreateUserRequest
import com.moura.picpay.backend.challenge.domain.user.api.FetchUsersQueryParametersRequest
import com.moura.picpay.backend.challenge.domain.user.api.create
import com.moura.picpay.backend.challenge.domain.user.api.randomList
import com.moura.picpay.backend.challenge.domain.user.persistence.UserRepository
import com.moura.picpay.backend.challenge.utils.randomEmailList
import com.moura.picpay.backend.challenge.utils.randomFullName
import com.moura.picpay.backend.challenge.utils.randomFullNameList
import io.azam.ulidj.ULID
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
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
            val request = CreateUserRequest.create()

            val user = underTest.createUser(request)

            user.id shouldNotBe null
            user.countrySpecificId shouldBe request.countrySpecificId
            user.fullName shouldBe request.fullName
            user.email shouldBe request.email
            user.password shouldBe request.password
            user.type shouldBe request.type
            user.balance shouldBe request.balance

            underTest.getById(user.id) shouldBeEqual user
        }

    @Test
    fun `should get user by id successfully`() =
        runTest {
            val user = underTest.createUser(CreateUserRequest.create())

            val result = underTest.getById(user.id)

            result shouldBeEqual user
        }

    @Test
    fun `should throw UserNotFound when user does not exist`() =
        runTest {
            val user = UserId.random()

            // should
            shouldThrow<PicPayException.UserNotFound> { underTest.getById(user) }
        }

    @Test
    fun `should update user correctly`() =
        runTest {
            val user = underTest.createUser(CreateUserRequest.create())
            val update =
                user.copy(
                    countrySpecificId = CountrySpecificId.random(),
                    fullName = randomAlphabetic(20),
                    email = randomAlphabetic(20),
                    password = ULID.random(),
                    type = UserType.entries.filterNot { it == user.type }.first(),
                    balance = BigDecimal.valueOf(200),
                )

            val result = underTest.updateUser(update)

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
            val user = underTest.createUser(CreateUserRequest.create())
            val update = user.copy(balance = BigDecimal.valueOf(200))

            delay(1000)
            val result = underTest.updateUser(update)

            userRepository
                .findById(result.id)
                .shouldNotBeNull {
                    createdAt.shouldNotBeNull() shouldBeBefore modifiedAt.shouldNotBeNull()
                }
        }

    @Test
    fun `should return all user successfully`() =
        runTest {
            CreateUserRequest.randomList().map { underTest.createUser(it) }

            val result = underTest.getAllUsers(FetchUsersQueryParametersRequest()).toList()

            result.map(User::id) shouldContainExactlyInAnyOrder userRepository.findAll().map { it.id }.toList()
        }

    @Test
    fun `should return users filtering by countrySpecificIds`() =
        runTest {
            val users =
                CountrySpecificId.randomList()
                    .map { CreateUserRequest.create(countrySpecificId = it) }
                    .map { underTest.createUser(it) }

            CreateUserRequest.randomList().map { underTest.createUser(it) }

            val result =
                underTest.getAllUsers(
                    request =
                        FetchUsersQueryParametersRequest(
                            countrySpecificIds = users.map(User::countrySpecificId),
                        ),
                ).toList()

            result shouldContainExactlyInAnyOrder users
        }

    @Test
    fun `should return users filtering by fullNames correctly`() =
        runTest {
            val users =
                String.randomFullNameList()
                    .map { CreateUserRequest.create(fullName = it) }
                    .map { underTest.createUser(it) }

            CreateUserRequest.randomList().map { underTest.createUser(it) }

            val result =
                underTest.getAllUsers(
                    request =
                        FetchUsersQueryParametersRequest(
                            fullNames = users.map(User::fullName),
                        ),
                ).toList()

            result shouldContainExactlyInAnyOrder users
        }

    @Test
    fun `should return users filtering by email correctly`() =
        runTest {
            val users =
                String.randomEmailList()
                    .map { CreateUserRequest.create(email = it) }
                    .map { underTest.createUser(it) }

            CreateUserRequest.randomList().map { underTest.createUser(it) }

            val result =
                underTest.getAllUsers(
                    request =
                        FetchUsersQueryParametersRequest(
                            emails = users.map(User::email),
                        ),
                ).toList()

            result shouldContainExactlyInAnyOrder users
        }

    @Test
    fun `should return users filtering by user type correctly`() =
        runTest {
            val userType = UserType.STANDARD
            val users =
                List(Random.nextInt(10, 20)) { userType }
                    .map { CreateUserRequest.create(type = it) }
                    .map { underTest.createUser(it) }

            CreateUserRequest.randomList().map { underTest.createUser(it.copy(type = UserType.MERCHANT)) }

            val result =
                underTest.getAllUsers(
                    request =
                        FetchUsersQueryParametersRequest(
                            type = userType,
                        ),
                ).toList()

            result shouldContainAll users
        }

    @Test
    fun `should return users filtering by countrySpecificId and fullName correctly`() =
        runTest {
            val repeatedFullNames = String.randomFullName()
            val users = CreateUserRequest.randomList().map { underTest.createUser(it.copy(fullName = repeatedFullNames)) }
            CreateUserRequest.randomList().map { underTest.createUser(it.copy(fullName = repeatedFullNames)) }

            val result =
                underTest.getAllUsers(
                    request =
                        FetchUsersQueryParametersRequest(
                            countrySpecificIds = users.map(User::countrySpecificId),
                            fullNames = users.map(User::fullName),
                        ),
                ).toList()

            result shouldContainExactlyInAnyOrder users
        }
}
