package kubeclient

import cats.Applicative
import cats.data.Kleisli
import cats.effect.Sync

import _root_.io.circe._

import openapigen.ClientConfig

import org.http4s._, circe._, client.Client

import kubeclient.io.k8s.api.authorization.v1.LocalSubjectAccessReview
import kubeclient.io.k8s.api.authorization.v1.SelfSubjectAccessReview
import kubeclient.io.k8s.api.authorization.v1.SelfSubjectRulesReview
import kubeclient.io.k8s.api.authorization.v1.SubjectAccessReview
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.APIResourceList

object AuthorizationV1Api {
  private implicit def circeEncoderToHttp4sEntityEncoder[F[_]: Applicative, A: Encoder]
      : EntityEncoder[F, A] = jsonEncoderOf
  private implicit def circeDecoderToHttp4sEntityDecoder[F[_]: Sync, A: Decoder]
      : EntityDecoder[F, A] = jsonOf

  type Action[F[_], A] = Kleisli[F, ClientConfig[F], A]

  /** get available resources
    *

    */
  def getAuthorizationV1APIResources[F[_]: Applicative: Sync]
      : Action[F, APIResourceList] = Kleisli { _config =>
    val _path = s"""/apis/authorization.k8s.io/v1/"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[APIResourceList](_request)
  }

  /** create a LocalSubjectAccessReview
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def createAuthorizationV1NamespacedLocalSubjectAccessReview[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      namespace: String,
      pretty: Option[String] = None,
      body: LocalSubjectAccessReview
  ): Action[F, LocalSubjectAccessReview] = Kleisli { _config =>
    val _path =
      s"""/apis/authorization.k8s.io/v1/namespaces/${namespace}/localsubjectaccessreviews"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[LocalSubjectAccessReview](_request)
  }

  /** create a SelfSubjectAccessReview
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def createAuthorizationV1SelfSubjectAccessReview[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      pretty: Option[String] = None,
      body: SelfSubjectAccessReview
  ): Action[F, SelfSubjectAccessReview] = Kleisli { _config =>
    val _path = s"""/apis/authorization.k8s.io/v1/selfsubjectaccessreviews"""
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

    _config.httpClient.expect[SelfSubjectAccessReview](_request)
  }

  /** create a SelfSubjectRulesReview
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def createAuthorizationV1SelfSubjectRulesReview[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      pretty: Option[String] = None,
      body: SelfSubjectRulesReview
  ): Action[F, SelfSubjectRulesReview] = Kleisli { _config =>
    val _path = s"""/apis/authorization.k8s.io/v1/selfsubjectrulesreviews"""
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

    _config.httpClient.expect[SelfSubjectRulesReview](_request)
  }

  /** create a SubjectAccessReview
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def createAuthorizationV1SubjectAccessReview[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      pretty: Option[String] = None,
      body: SubjectAccessReview
  ): Action[F, SubjectAccessReview] = Kleisli { _config =>
    val _path = s"""/apis/authorization.k8s.io/v1/subjectaccessreviews"""
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

    _config.httpClient.expect[SubjectAccessReview](_request)
  }

}
