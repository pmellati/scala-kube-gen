package openapigen.write.api

import io.swagger.models.{HttpMethod, Operation}

case class OperationWithMeta(operation: Operation, path: String, method: HttpMethod)