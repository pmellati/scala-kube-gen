package kubegen;

import io.swagger.parser.SwaggerParser
import io.swagger.models.Swagger

object Main {
  def main(args: Array[String]) {
    val swagger = new SwaggerParser().read("/Users/pouria/Documents/kube-openapi-spec.json")
    println(s"Got swagger:\n\n$swagger")
  }
}