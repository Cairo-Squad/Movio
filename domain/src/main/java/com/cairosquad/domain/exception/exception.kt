package com.cairosquad.domain.exception

sealed class MovioException(message: String = "") : RuntimeException(message)

class NetworkException(message: String = "") : MovioException(message)

class InternetConnectionException(message: String = "") : MovioException(message)

class UnknownException(message: String = "") : MovioException(message)

class NoDataException(message: String = "") : MovioException(message)

class ParsingException(message: String = "") : MovioException(message)

class UnauthorizedActionException(message: String = "") : MovioException(message)
