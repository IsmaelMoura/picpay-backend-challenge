rootProject.name = "picpay-backend-challenge"

include(":picpay-notification-processor")
include(":picpay-protobuf")
include(":picpay-transfer-manager")

project(":picpay-notification-processor").projectDir = file("components/picpay-notification-processor")
project(":picpay-protobuf").projectDir = file("components/picpay-protobuf")
project(":picpay-transfer-manager").projectDir = file("components/picpay-transfer-manager")
