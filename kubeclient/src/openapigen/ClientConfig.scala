package openapigen

import org.http4s.Uri
import org.http4s.client.Client

// TODO: Should be made available via a library that this project would depend on.
case class ClientConfig[F[_]](httpClient: Client[F], baseApiUri: Uri)

