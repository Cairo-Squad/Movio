package com.cairosquad.repository.common.exception

sealed class DataSourceException(override val message: String = "") : RuntimeException(message)

class UnknownDataSourceException(override val message: String = "") : DataSourceException(message)
class ServerException(override val message: String = "") : DataSourceException(message)
sealed class ApiException(override val message: String = "") : DataSourceException(message)
class RequestTimeoutException(override val message: String = "") : ApiException(message)
class TooManyRequestsException(override val message: String = "") : ApiException(message)
class UnauthorizedException(override val message: String = "") : DataSourceException(message)