package com.cairosquad.domain.exception

sealed class MovioException(message: String = "") : RuntimeException(message)

class NetworkException(message: String = "") : MovioException(message)

class InternetConnectionException(message: String = "") : MovioException(message)

class UnknownException(message: String = "") : MovioException(message)

class DomainEmptyResponseException(message: String = "") : MovioException(message)

class DomainJsonParsingException(message: String = "") : MovioException(message)

class DUnauthorizedException(message: String = "") : MovioException(message)



