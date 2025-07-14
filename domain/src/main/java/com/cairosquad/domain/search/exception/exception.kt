package com.cairosquad.domain.search.exception

sealed class MovioException(message: String = "") : RuntimeException(message)

class NetworkException(message: String = "") : MovioException(message)

class InternetConnectionException(message: String = "") : MovioException(message)

class UnknownException(message: String = "") : MovioException(message)