package com.cairosquad.repository.utils.exception

sealed class DataSourceException(override val message: String = "") : RuntimeException(message)

// General errors
class UnknownDataSourceException(override val message: String = "") : DataSourceException(message)
class ServerException(override val message: String = "") : DataSourceException(message)
class NoInternetException(override val message: String = "") : DataSourceException(message)
class EmptyResponseException(override val message: String = "Empty response body") : DataSourceException(message)
class JsonParsingException(override val message: String = "Failed to parse response") : DataSourceException(message)

// API-specific
sealed class ApiException(override val message: String = "") : DataSourceException(message)
class RequestTimeoutException(override val message: String = "") : ApiException(message)
class TooManyRequestsException(override val message: String = "") : ApiException(message)
class UnauthorizedException(override val message: String = "") : ApiException(message)
class ForbiddenException(override val message: String = "") : ApiException(message)
class NotFoundException(override val message: String = "") : ApiException(message)
class ConflictException(override val message: String = "") : ApiException(message)
class BadRequestException(override val message: String = "") : ApiException(message)
