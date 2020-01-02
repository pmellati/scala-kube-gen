package kubeclient

import cats.Applicative
import cats.data.Kleisli
import cats.effect.Sync

import _root_.io.circe._

import openapigen.ClientConfig

import org.http4s._, circe._, client.Client

import kubeclient.io.k8s.api.authentication.v1beta1.TokenReview
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.APIResourceList

object AuthenticationV1beta1Api {
  private implicit def circeEncoderToHttp4sEntityEncoder[F[_]: Applicative, A: Encoder]
      : EntityEncoder[F, A] = jsonEncoderOf
  private implicit def circeDecoderToHttp4sEntityDecoder[F[_]: Sync, A: Decoder]
      : EntityDecoder[F, A] = jsonOf

  type Action[F[_], A] = Kleisli[F, ClientConfig[F], A]

  /** get available resources
    *

    */
  def getAuthenticationV1beta1APIResources[F[_]: Applicative: Sync]
      : Action[F, APIResourceList] = Kleisli { _config =>
    val _path = s"""/apis/authentication.k8s.io/v1beta1/"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[APIResourceList](_request)
  }

  /** create a TokenReview
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def createAuthenticationV1beta1TokenReview[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      pretty: Option[String] = None,
      body: TokenReview
  ): Action[F, TokenReview] = Kleisli { _config =>
    val _path = s"""/apis/authentication.k8s.io/v1beta1/tokenreviews"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[TokenReview](_request)
  }

}
