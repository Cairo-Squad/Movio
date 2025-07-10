package com.cairosquad.repository.common.exceptions

class UnknownException(message: String = "") : RuntimeException(message)

class ServerException(message: String = "") : RuntimeException(message)

open class ApiException(message: String = "") : RuntimeException(message)

class RequestTimeoutException(message: String = "") : ApiException(message)

class TooManyRequestsException(message: String = "") : ApiException(message)

class UnauthorizedException(message: String = "") : RuntimeException(message)