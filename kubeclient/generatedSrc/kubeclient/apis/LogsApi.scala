package kubeclient

import cats.Applicative
import cats.data.Kleisli
import cats.effect.Sync

import _root_.io.circe._

import openapigen.ClientConfig

import org.http4s._, circe._, client.Client

object LogsApi {
  private implicit def circeEncoderToHttp4sEntityEncoder[F[_]: Applicative, A: Encoder]
      : EntityEncoder[F, A] = jsonEncoderOf
  private implicit def circeDecoderToHttp4sEntityDecoder[F[_]: Sync, A: Decoder]
      : EntityDecoder[F, A] = jsonOf

  type Action[F[_], A] = Kleisli[F, ClientConfig[F], A]

  /** null
    *

    */
  def logFileListHandler[F[_]: Applicative: Sync]: Action[F, Unit] = Kleisli {
    _config =>
      val _path = s"""/logs/"""
      val _uri  = _config.baseApiUri.withPath(_path)

      val _uriWithQueryParams = _uri

      val _method = Method.GET

      val _request = Request[F](
        method = _method,
        uri = _uriWithQueryParams
      )

      _config.httpClient.expect[Unit](_request)
  }

  /** null
    *
    *  @param logpath path to the log
    */
  def logFileHandler[F[_]: Applicative: Sync](
      logpath: String
  ): Action[F, Unit] = Kleisli { _config =>
    val _path = s"""/logs/${logpath}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Unit](_request)
  }

}
