package com.cairosquad.domain.search.exception

open class MovioException(message: String = "") : RuntimeException(message)

open class NetworkException(message: String = "") : MovioException(message)

class StoreDataException(message: String = "") : MovioException(message)

class NoInternetException(message: String = "") : NetworkException(message)

class UnknownException(message: String = "") : MovioException(message)