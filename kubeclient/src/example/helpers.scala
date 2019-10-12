package openapigen

import org.http4s.Uri
import org.http4s.client.Client

case class ClientConfig[F[_]](httpClient: Client[F], baseApiUri: Uri)
