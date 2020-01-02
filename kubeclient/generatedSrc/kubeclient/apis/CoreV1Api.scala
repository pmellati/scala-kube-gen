package kubeclient

import cats.Applicative
import cats.data.Kleisli
import cats.effect.Sync

import _root_.io.circe._

import openapigen.ClientConfig

import org.http4s._, circe._, client.Client

import kubeclient.io.k8s.api.authentication.v1.TokenRequest
import kubeclient.io.k8s.api.autoscaling.v1.Scale
import kubeclient.io.k8s.api.core.v1.Binding
import kubeclient.io.k8s.api.core.v1.ComponentStatus
import kubeclient.io.k8s.api.core.v1.ComponentStatusList
import kubeclient.io.k8s.api.core.v1.ConfigMap
import kubeclient.io.k8s.api.core.v1.ConfigMapList
import kubeclient.io.k8s.api.core.v1.Endpoints
import kubeclient.io.k8s.api.core.v1.EndpointsList
import kubeclient.io.k8s.api.core.v1.Event
import kubeclient.io.k8s.api.core.v1.EventList
import kubeclient.io.k8s.api.core.v1.LimitRange
import kubeclient.io.k8s.api.core.v1.LimitRangeList
import kubeclient.io.k8s.api.core.v1.Namespace
import kubeclient.io.k8s.api.core.v1.NamespaceList
import kubeclient.io.k8s.api.core.v1.Node
import kubeclient.io.k8s.api.core.v1.NodeList
import kubeclient.io.k8s.api.core.v1.PersistentVolume
import kubeclient.io.k8s.api.core.v1.PersistentVolumeClaim
import kubeclient.io.k8s.api.core.v1.PersistentVolumeClaimList
import kubeclient.io.k8s.api.core.v1.PersistentVolumeList
import kubeclient.io.k8s.api.core.v1.Pod
import kubeclient.io.k8s.api.core.v1.PodList
import kubeclient.io.k8s.api.core.v1.PodTemplate
import kubeclient.io.k8s.api.core.v1.PodTemplateList
import kubeclient.io.k8s.api.core.v1.ReplicationController
import kubeclient.io.k8s.api.core.v1.ReplicationControllerList
import kubeclient.io.k8s.api.core.v1.ResourceQuota
import kubeclient.io.k8s.api.core.v1.ResourceQuotaList
import kubeclient.io.k8s.api.core.v1.Secret
import kubeclient.io.k8s.api.core.v1.SecretList
import kubeclient.io.k8s.api.core.v1.Service
import kubeclient.io.k8s.api.core.v1.ServiceAccount
import kubeclient.io.k8s.api.core.v1.ServiceAccountList
import kubeclient.io.k8s.api.core.v1.ServiceList
import kubeclient.io.k8s.api.policy.v1beta1.Eviction
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.APIResourceList
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.DeleteOptions
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Patch
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Status
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.WatchEvent

object CoreV1Api {
  private implicit def circeEncoderToHttp4sEntityEncoder[F[_]: Applicative, A: Encoder]
      : EntityEncoder[F, A] = jsonEncoderOf
  private implicit def circeDecoderToHttp4sEntityDecoder[F[_]: Sync, A: Decoder]
      : EntityDecoder[F, A] = jsonOf

  type Action[F[_], A] = Kleisli[F, ClientConfig[F], A]

  /** get available resources
    *

    */
  def getCoreV1APIResources[F[_]: Applicative: Sync]
      : Action[F, APIResourceList] = Kleisli { _config =>
    val _path = s"""/api/v1/"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[APIResourceList](_request)
  }

  /** list objects of kind ComponentStatus
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1ComponentStatus[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ComponentStatusList] = Kleisli { _config =>
    val _path = s"""/api/v1/componentstatuses"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ComponentStatusList](_request)
  }

  /** read the specified ComponentStatus
    *
    *  @param name name of the ComponentStatus
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1ComponentStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None
  ): Action[F, ComponentStatus] = Kleisli { _config =>
    val _path = s"""/api/v1/componentstatuses/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ComponentStatus](_request)
  }

  /** list or watch objects of kind ConfigMap
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1ConfigMapForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ConfigMapList] = Kleisli { _config =>
    val _path = s"""/api/v1/configmaps"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ConfigMapList](_request)
  }

  /** list or watch objects of kind Endpoints
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1EndpointsForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, EndpointsList] = Kleisli { _config =>
    val _path = s"""/api/v1/endpoints"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[EndpointsList](_request)
  }

  /** list or watch objects of kind Event
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1EventForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, EventList] = Kleisli { _config =>
    val _path = s"""/api/v1/events"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[EventList](_request)
  }

  /** list or watch objects of kind LimitRange
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1LimitRangeForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, LimitRangeList] = Kleisli { _config =>
    val _path = s"""/api/v1/limitranges"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[LimitRangeList](_request)
  }

  /** list or watch objects of kind Namespace
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1Namespace[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, NamespaceList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[NamespaceList](_request)
  }

  /** create a Namespace
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1Namespace[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      body: Namespace,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Namespace] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Namespace](_request)
  }

  /** create a Binding
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def createCoreV1NamespacedBinding[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      namespace: String,
      pretty: Option[String] = None,
      body: Binding
  ): Action[F, Binding] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/bindings"""
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

    _config.httpClient.expect[Binding](_request)
  }

  /** list or watch objects of kind ConfigMap
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedConfigMap[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ConfigMapList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/configmaps"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ConfigMapList](_request)
  }

  /** create a ConfigMap
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedConfigMap[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: ConfigMap,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ConfigMap] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/configmaps"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ConfigMap](_request)
  }

  /** delete collection of ConfigMap
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedConfigMap[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/configmaps"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified ConfigMap
    *
    *  @param name name of the ConfigMap
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedConfigMap[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, ConfigMap] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/configmaps/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ConfigMap](_request)
  }

  /** replace the specified ConfigMap
    *
    *  @param name name of the ConfigMap
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedConfigMap[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: ConfigMap,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ConfigMap] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/configmaps/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ConfigMap](_request)
  }

  /** delete a ConfigMap
    *
    *  @param name name of the ConfigMap
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedConfigMap[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/configmaps/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified ConfigMap
    *
    *  @param name name of the ConfigMap
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedConfigMap[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ConfigMap] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/configmaps/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ConfigMap](_request)
  }

  /** list or watch objects of kind Endpoints
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedEndpoints[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, EndpointsList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/endpoints"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[EndpointsList](_request)
  }

  /** create Endpoints
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedEndpoints[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: Endpoints,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Endpoints] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/endpoints"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Endpoints](_request)
  }

  /** delete collection of Endpoints
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedEndpoints[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/endpoints"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified Endpoints
    *
    *  @param name name of the Endpoints
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedEndpoints[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, Endpoints] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/endpoints/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Endpoints](_request)
  }

  /** replace the specified Endpoints
    *
    *  @param name name of the Endpoints
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedEndpoints[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Endpoints,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Endpoints] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/endpoints/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Endpoints](_request)
  }

  /** delete Endpoints
    *
    *  @param name name of the Endpoints
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedEndpoints[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/endpoints/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified Endpoints
    *
    *  @param name name of the Endpoints
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedEndpoints[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Endpoints] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/endpoints/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Endpoints](_request)
  }

  /** list or watch objects of kind Event
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedEvent[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, EventList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/events"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[EventList](_request)
  }

  /** create an Event
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedEvent[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: Event,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Event] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/events"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Event](_request)
  }

  /** delete collection of Event
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedEvent[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/events"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified Event
    *
    *  @param name name of the Event
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedEvent[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, Event] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/events/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Event](_request)
  }

  /** replace the specified Event
    *
    *  @param name name of the Event
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedEvent[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Event,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Event] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/events/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Event](_request)
  }

  /** delete an Event
    *
    *  @param name name of the Event
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedEvent[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/events/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified Event
    *
    *  @param name name of the Event
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedEvent[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Event] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/events/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Event](_request)
  }

  /** list or watch objects of kind LimitRange
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedLimitRange[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, LimitRangeList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/limitranges"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[LimitRangeList](_request)
  }

  /** create a LimitRange
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedLimitRange[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: LimitRange,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, LimitRange] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/limitranges"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[LimitRange](_request)
  }

  /** delete collection of LimitRange
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedLimitRange[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/limitranges"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified LimitRange
    *
    *  @param name name of the LimitRange
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedLimitRange[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, LimitRange] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/limitranges/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[LimitRange](_request)
  }

  /** replace the specified LimitRange
    *
    *  @param name name of the LimitRange
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedLimitRange[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: LimitRange,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, LimitRange] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/limitranges/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[LimitRange](_request)
  }

  /** delete a LimitRange
    *
    *  @param name name of the LimitRange
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedLimitRange[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/limitranges/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified LimitRange
    *
    *  @param name name of the LimitRange
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedLimitRange[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, LimitRange] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/limitranges/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[LimitRange](_request)
  }

  /** list or watch objects of kind PersistentVolumeClaim
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedPersistentVolumeClaim[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, PersistentVolumeClaimList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PersistentVolumeClaimList](_request)
  }

  /** create a PersistentVolumeClaim
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedPersistentVolumeClaim[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: PersistentVolumeClaim,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, PersistentVolumeClaim] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolumeClaim](_request)
  }

  /** delete collection of PersistentVolumeClaim
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedPersistentVolumeClaim[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified PersistentVolumeClaim
    *
    *  @param name name of the PersistentVolumeClaim
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedPersistentVolumeClaim[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, PersistentVolumeClaim] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PersistentVolumeClaim](_request)
  }

  /** replace the specified PersistentVolumeClaim
    *
    *  @param name name of the PersistentVolumeClaim
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedPersistentVolumeClaim[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: PersistentVolumeClaim,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, PersistentVolumeClaim] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolumeClaim](_request)
  }

  /** delete a PersistentVolumeClaim
    *
    *  @param name name of the PersistentVolumeClaim
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedPersistentVolumeClaim[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified PersistentVolumeClaim
    *
    *  @param name name of the PersistentVolumeClaim
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedPersistentVolumeClaim[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, PersistentVolumeClaim] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolumeClaim](_request)
  }

  /** read status of the specified PersistentVolumeClaim
    *
    *  @param name name of the PersistentVolumeClaim
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1NamespacedPersistentVolumeClaimStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, PersistentVolumeClaim] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PersistentVolumeClaim](_request)
  }

  /** replace status of the specified PersistentVolumeClaim
    *
    *  @param name name of the PersistentVolumeClaim
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedPersistentVolumeClaimStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: PersistentVolumeClaim,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, PersistentVolumeClaim] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolumeClaim](_request)
  }

  /** partially update status of the specified PersistentVolumeClaim
    *
    *  @param name name of the PersistentVolumeClaim
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedPersistentVolumeClaimStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, PersistentVolumeClaim] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/persistentvolumeclaims/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolumeClaim](_request)
  }

  /** list or watch objects of kind Pod
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedPod[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, PodList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PodList](_request)
  }

  /** create a Pod
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedPod[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: Pod,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Pod] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Pod](_request)
  }

  /** delete collection of Pod
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedPod[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified Pod
    *
    *  @param name name of the Pod
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedPod[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, Pod] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Pod](_request)
  }

  /** replace the specified Pod
    *
    *  @param name name of the Pod
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedPod[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Pod,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Pod] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Pod](_request)
  }

  /** delete a Pod
    *
    *  @param name name of the Pod
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedPod[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified Pod
    *
    *  @param name name of the Pod
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedPod[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Pod] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Pod](_request)
  }

  /** connect GET requests to attach of Pod
    *
    *  @param container The container in which to execute the command. Defaults to only container if there is only one container in the pod.
    *  @param name name of the PodAttachOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param stderr Stderr if true indicates that stderr is to be redirected for the attach call. Defaults to true.
    *  @param stdin Stdin if true, redirects the standard input stream of the pod for this call. Defaults to false.
    *  @param stdout Stdout if true indicates that stdout is to be redirected for the attach call. Defaults to true.
    *  @param tty TTY if true indicates that a tty will be allocated for the attach call. This is passed through the container runtime so the tty is allocated on the worker node by the container runtime. Defaults to false.
    */
  def connectCoreV1GetNamespacedPodAttach[F[_]: Applicative: Sync](
      container: Option[String] = None,
      name: String,
      namespace: String,
      stderr: Option[Boolean] = None,
      stdin: Option[Boolean] = None,
      stdout: Option[Boolean] = None,
      tty: Option[Boolean] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/attach"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("container", container)
      .withOptionQueryParam("stderr", stderr)
      .withOptionQueryParam("stdin", stdin)
      .withOptionQueryParam("stdout", stdout)
      .withOptionQueryParam("tty", tty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect POST requests to attach of Pod
    *
    *  @param container The container in which to execute the command. Defaults to only container if there is only one container in the pod.
    *  @param name name of the PodAttachOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param stderr Stderr if true indicates that stderr is to be redirected for the attach call. Defaults to true.
    *  @param stdin Stdin if true, redirects the standard input stream of the pod for this call. Defaults to false.
    *  @param stdout Stdout if true indicates that stdout is to be redirected for the attach call. Defaults to true.
    *  @param tty TTY if true indicates that a tty will be allocated for the attach call. This is passed through the container runtime so the tty is allocated on the worker node by the container runtime. Defaults to false.
    */
  def connectCoreV1PostNamespacedPodAttach[F[_]: Applicative: Sync](
      container: Option[String] = None,
      name: String,
      namespace: String,
      stderr: Option[Boolean] = None,
      stdin: Option[Boolean] = None,
      stdout: Option[Boolean] = None,
      tty: Option[Boolean] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/attach"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("container", container)
      .withOptionQueryParam("stderr", stderr)
      .withOptionQueryParam("stdin", stdin)
      .withOptionQueryParam("stdout", stdout)
      .withOptionQueryParam("tty", tty)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** create binding of a Pod
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param name name of the Binding
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def createCoreV1NamespacedPodBinding[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Binding
  ): Action[F, Binding] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/binding"""
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

    _config.httpClient.expect[Binding](_request)
  }

  /** create eviction of a Pod
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param name name of the Eviction
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def createCoreV1NamespacedPodEviction[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Eviction
  ): Action[F, Eviction] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/eviction"""
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

    _config.httpClient.expect[Eviction](_request)
  }

  /** connect GET requests to exec of Pod
    *
    *  @param command Command is the remote command to execute. argv array. Not executed within a shell.
    *  @param container Container in which to execute the command. Defaults to only container if there is only one container in the pod.
    *  @param name name of the PodExecOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param stderr Redirect the standard error stream of the pod for this call. Defaults to true.
    *  @param stdin Redirect the standard input stream of the pod for this call. Defaults to false.
    *  @param stdout Redirect the standard output stream of the pod for this call. Defaults to true.
    *  @param tty TTY if true indicates that a tty will be allocated for the exec call. Defaults to false.
    */
  def connectCoreV1GetNamespacedPodExec[F[_]: Applicative: Sync](
      command: Option[String] = None,
      container: Option[String] = None,
      name: String,
      namespace: String,
      stderr: Option[Boolean] = None,
      stdin: Option[Boolean] = None,
      stdout: Option[Boolean] = None,
      tty: Option[Boolean] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/exec"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("command", command)
      .withOptionQueryParam("container", container)
      .withOptionQueryParam("stderr", stderr)
      .withOptionQueryParam("stdin", stdin)
      .withOptionQueryParam("stdout", stdout)
      .withOptionQueryParam("tty", tty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect POST requests to exec of Pod
    *
    *  @param command Command is the remote command to execute. argv array. Not executed within a shell.
    *  @param container Container in which to execute the command. Defaults to only container if there is only one container in the pod.
    *  @param name name of the PodExecOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param stderr Redirect the standard error stream of the pod for this call. Defaults to true.
    *  @param stdin Redirect the standard input stream of the pod for this call. Defaults to false.
    *  @param stdout Redirect the standard output stream of the pod for this call. Defaults to true.
    *  @param tty TTY if true indicates that a tty will be allocated for the exec call. Defaults to false.
    */
  def connectCoreV1PostNamespacedPodExec[F[_]: Applicative: Sync](
      command: Option[String] = None,
      container: Option[String] = None,
      name: String,
      namespace: String,
      stderr: Option[Boolean] = None,
      stdin: Option[Boolean] = None,
      stdout: Option[Boolean] = None,
      tty: Option[Boolean] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/exec"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("command", command)
      .withOptionQueryParam("container", container)
      .withOptionQueryParam("stderr", stderr)
      .withOptionQueryParam("stdin", stdin)
      .withOptionQueryParam("stdout", stdout)
      .withOptionQueryParam("tty", tty)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** read log of the specified Pod
    *
    *  @param container The container for which to stream logs. Defaults to only container if there is one container in the pod.
    *  @param follow Follow the log stream of the pod. Defaults to false.
    *  @param limitBytes If set, the number of bytes to read from the server before terminating the log output. This may not display a complete final line of logging, and may return slightly more or slightly less than the specified limit.
    *  @param name name of the Pod
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param previous Return previous terminated container logs. Defaults to false.
    *  @param sinceSeconds A relative time in seconds before the current time from which to show logs. If this value precedes the time a pod was started, only logs since the pod start will be returned. If this value is in the future, no logs will be returned. Only one of sinceSeconds or sinceTime may be specified.
    *  @param tailLines If set, the number of lines from the end of the logs to show. If not specified, logs are shown from the creation of the container or sinceSeconds or sinceTime
    *  @param timestamps If true, add an RFC3339 or RFC3339Nano timestamp at the beginning of every line of log output. Defaults to false.
    */
  def readCoreV1NamespacedPodLog[F[_]: Applicative: Sync](
      container: Option[String] = None,
      follow: Option[Boolean] = None,
      limitBytes: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      previous: Option[Boolean] = None,
      sinceSeconds: Option[Int] = None,
      tailLines: Option[Int] = None,
      timestamps: Option[Boolean] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/log"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("container", container)
      .withOptionQueryParam("follow", follow)
      .withOptionQueryParam("limitBytes", limitBytes)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("previous", previous)
      .withOptionQueryParam("sinceSeconds", sinceSeconds)
      .withOptionQueryParam("tailLines", tailLines)
      .withOptionQueryParam("timestamps", timestamps)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect GET requests to portforward of Pod
    *
    *  @param name name of the PodPortForwardOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param ports List of ports to forward Required when using WebSockets
    */
  def connectCoreV1GetNamespacedPodPortforward[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      ports: Option[Int] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/portforward"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("ports", ports)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect POST requests to portforward of Pod
    *
    *  @param name name of the PodPortForwardOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param ports List of ports to forward Required when using WebSockets
    */
  def connectCoreV1PostNamespacedPodPortforward[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      ports: Option[Int] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/portforward"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("ports", ports)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect GET requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1GetNamespacedPodProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PUT requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1PutNamespacedPodProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect POST requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1PostNamespacedPodProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect DELETE requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1DeleteNamespacedPodProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PATCH requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1PatchNamespacedPodProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect HEAD requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1HeadNamespacedPodProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.HEAD

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect OPTIONS requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1OptionsNamespacedPodProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.OPTIONS

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect GET requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1GetNamespacedPodProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PUT requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1PutNamespacedPodProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect POST requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1PostNamespacedPodProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect DELETE requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1DeleteNamespacedPodProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PATCH requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1PatchNamespacedPodProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect HEAD requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1HeadNamespacedPodProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.HEAD

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect OPTIONS requests to proxy of Pod
    *
    *  @param name name of the PodProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to pod.
    */
  def connectCoreV1OptionsNamespacedPodProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/pods/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.OPTIONS

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** read status of the specified Pod
    *
    *  @param name name of the Pod
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1NamespacedPodStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, Pod] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Pod](_request)
  }

  /** replace status of the specified Pod
    *
    *  @param name name of the Pod
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedPodStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Pod,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Pod] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Pod](_request)
  }

  /** partially update status of the specified Pod
    *
    *  @param name name of the Pod
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedPodStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Pod] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/pods/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Pod](_request)
  }

  /** list or watch objects of kind PodTemplate
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedPodTemplate[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, PodTemplateList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/podtemplates"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PodTemplateList](_request)
  }

  /** create a PodTemplate
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedPodTemplate[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: PodTemplate,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, PodTemplate] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/podtemplates"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PodTemplate](_request)
  }

  /** delete collection of PodTemplate
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedPodTemplate[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/podtemplates"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified PodTemplate
    *
    *  @param name name of the PodTemplate
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedPodTemplate[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, PodTemplate] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/podtemplates/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PodTemplate](_request)
  }

  /** replace the specified PodTemplate
    *
    *  @param name name of the PodTemplate
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedPodTemplate[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: PodTemplate,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, PodTemplate] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/podtemplates/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PodTemplate](_request)
  }

  /** delete a PodTemplate
    *
    *  @param name name of the PodTemplate
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedPodTemplate[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/podtemplates/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified PodTemplate
    *
    *  @param name name of the PodTemplate
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedPodTemplate[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, PodTemplate] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/podtemplates/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PodTemplate](_request)
  }

  /** list or watch objects of kind ReplicationController
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedReplicationController[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ReplicationControllerList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/replicationcontrollers"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ReplicationControllerList](_request)
  }

  /** create a ReplicationController
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedReplicationController[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: ReplicationController,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ReplicationController] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/replicationcontrollers"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ReplicationController](_request)
  }

  /** delete collection of ReplicationController
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedReplicationController[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/replicationcontrollers"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified ReplicationController
    *
    *  @param name name of the ReplicationController
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedReplicationController[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, ReplicationController] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ReplicationController](_request)
  }

  /** replace the specified ReplicationController
    *
    *  @param name name of the ReplicationController
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedReplicationController[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: ReplicationController,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ReplicationController] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ReplicationController](_request)
  }

  /** delete a ReplicationController
    *
    *  @param name name of the ReplicationController
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedReplicationController[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified ReplicationController
    *
    *  @param name name of the ReplicationController
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedReplicationController[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ReplicationController] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ReplicationController](_request)
  }

  /** read scale of the specified ReplicationController
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1NamespacedReplicationControllerScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}/scale"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Scale](_request)
  }

  /** replace scale of the specified ReplicationController
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedReplicationControllerScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Scale,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}/scale"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Scale](_request)
  }

  /** partially update scale of the specified ReplicationController
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedReplicationControllerScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}/scale"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Scale](_request)
  }

  /** read status of the specified ReplicationController
    *
    *  @param name name of the ReplicationController
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1NamespacedReplicationControllerStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, ReplicationController] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ReplicationController](_request)
  }

  /** replace status of the specified ReplicationController
    *
    *  @param name name of the ReplicationController
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedReplicationControllerStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: ReplicationController,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ReplicationController] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ReplicationController](_request)
  }

  /** partially update status of the specified ReplicationController
    *
    *  @param name name of the ReplicationController
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedReplicationControllerStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ReplicationController] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/replicationcontrollers/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ReplicationController](_request)
  }

  /** list or watch objects of kind ResourceQuota
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedResourceQuota[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ResourceQuotaList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/resourcequotas"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ResourceQuotaList](_request)
  }

  /** create a ResourceQuota
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedResourceQuota[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: ResourceQuota,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ResourceQuota] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/resourcequotas"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ResourceQuota](_request)
  }

  /** delete collection of ResourceQuota
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedResourceQuota[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/resourcequotas"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified ResourceQuota
    *
    *  @param name name of the ResourceQuota
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedResourceQuota[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, ResourceQuota] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/resourcequotas/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ResourceQuota](_request)
  }

  /** replace the specified ResourceQuota
    *
    *  @param name name of the ResourceQuota
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedResourceQuota[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: ResourceQuota,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ResourceQuota] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/resourcequotas/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ResourceQuota](_request)
  }

  /** delete a ResourceQuota
    *
    *  @param name name of the ResourceQuota
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedResourceQuota[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/resourcequotas/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified ResourceQuota
    *
    *  @param name name of the ResourceQuota
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedResourceQuota[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ResourceQuota] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/resourcequotas/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ResourceQuota](_request)
  }

  /** read status of the specified ResourceQuota
    *
    *  @param name name of the ResourceQuota
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1NamespacedResourceQuotaStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, ResourceQuota] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/resourcequotas/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ResourceQuota](_request)
  }

  /** replace status of the specified ResourceQuota
    *
    *  @param name name of the ResourceQuota
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedResourceQuotaStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: ResourceQuota,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ResourceQuota] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/resourcequotas/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ResourceQuota](_request)
  }

  /** partially update status of the specified ResourceQuota
    *
    *  @param name name of the ResourceQuota
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedResourceQuotaStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ResourceQuota] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/resourcequotas/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ResourceQuota](_request)
  }

  /** list or watch objects of kind Secret
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedSecret[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, SecretList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/secrets"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[SecretList](_request)
  }

  /** create a Secret
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedSecret[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: Secret,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Secret] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/secrets"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Secret](_request)
  }

  /** delete collection of Secret
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedSecret[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/secrets"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified Secret
    *
    *  @param name name of the Secret
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedSecret[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, Secret] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/secrets/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Secret](_request)
  }

  /** replace the specified Secret
    *
    *  @param name name of the Secret
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedSecret[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Secret,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Secret] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/secrets/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Secret](_request)
  }

  /** delete a Secret
    *
    *  @param name name of the Secret
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedSecret[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/secrets/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified Secret
    *
    *  @param name name of the Secret
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedSecret[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Secret] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/secrets/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Secret](_request)
  }

  /** list or watch objects of kind ServiceAccount
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedServiceAccount[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ServiceAccountList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/serviceaccounts"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ServiceAccountList](_request)
  }

  /** create a ServiceAccount
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedServiceAccount[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: ServiceAccount,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ServiceAccount] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/serviceaccounts"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ServiceAccount](_request)
  }

  /** delete collection of ServiceAccount
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNamespacedServiceAccount[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/serviceaccounts"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified ServiceAccount
    *
    *  @param name name of the ServiceAccount
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedServiceAccount[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, ServiceAccount] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/serviceaccounts/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ServiceAccount](_request)
  }

  /** replace the specified ServiceAccount
    *
    *  @param name name of the ServiceAccount
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedServiceAccount[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: ServiceAccount,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ServiceAccount] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/serviceaccounts/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ServiceAccount](_request)
  }

  /** delete a ServiceAccount
    *
    *  @param name name of the ServiceAccount
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedServiceAccount[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/serviceaccounts/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified ServiceAccount
    *
    *  @param name name of the ServiceAccount
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedServiceAccount[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ServiceAccount] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/serviceaccounts/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ServiceAccount](_request)
  }

  /** create token of a ServiceAccount
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param name name of the TokenRequest
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def createCoreV1NamespacedServiceAccountToken[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: TokenRequest
  ): Action[F, TokenRequest] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/serviceaccounts/${name}/token"""
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

    _config.httpClient.expect[TokenRequest](_request)
  }

  /** list or watch objects of kind Service
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1NamespacedService[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ServiceList] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ServiceList](_request)
  }

  /** create a Service
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1NamespacedService[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: Service,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Service] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Service](_request)
  }

  /** read the specified Service
    *
    *  @param name name of the Service
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1NamespacedService[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, Service] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Service](_request)
  }

  /** replace the specified Service
    *
    *  @param name name of the Service
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedService[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Service,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Service] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Service](_request)
  }

  /** delete a Service
    *
    *  @param name name of the Service
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1NamespacedService[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified Service
    *
    *  @param name name of the Service
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedService[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Service] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Service](_request)
  }

  /** connect GET requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1GetNamespacedServiceProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PUT requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1PutNamespacedServiceProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect POST requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1PostNamespacedServiceProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect DELETE requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1DeleteNamespacedServiceProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PATCH requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1PatchNamespacedServiceProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect HEAD requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1HeadNamespacedServiceProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.HEAD

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect OPTIONS requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1OptionsNamespacedServiceProxy[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.OPTIONS

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect GET requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1GetNamespacedServiceProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/services/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PUT requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1PutNamespacedServiceProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/services/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect POST requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1PostNamespacedServiceProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/services/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect DELETE requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1DeleteNamespacedServiceProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/services/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PATCH requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1PatchNamespacedServiceProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/services/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect HEAD requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1HeadNamespacedServiceProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/services/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.HEAD

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect OPTIONS requests to proxy of Service
    *
    *  @param name name of the ServiceProxyOptions
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param path path to the resource
    *  @param path_2 Path is the part of URLs that include service endpoints, suffixes, and parameters to use for the current proxy request to service. For example, the whole request URL is http://localhost/api/v1/namespaces/kube-system/services/elasticsearch-logging/_search?q=user:kimchy. Path is _search?q=user:kimchy.
    */
  def connectCoreV1OptionsNamespacedServiceProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path =
      s"""/api/v1/namespaces/${namespace}/services/${name}/proxy/${path}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.OPTIONS

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** read status of the specified Service
    *
    *  @param name name of the Service
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1NamespacedServiceStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, Service] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Service](_request)
  }

  /** replace status of the specified Service
    *
    *  @param name name of the Service
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespacedServiceStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Service,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Service] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Service](_request)
  }

  /** partially update status of the specified Service
    *
    *  @param name name of the Service
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespacedServiceStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Service] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${namespace}/services/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Service](_request)
  }

  /** read the specified Namespace
    *
    *  @param name name of the Namespace
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1Namespace[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, Namespace] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Namespace](_request)
  }

  /** replace the specified Namespace
    *
    *  @param name name of the Namespace
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1Namespace[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Namespace,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Namespace] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Namespace](_request)
  }

  /** delete a Namespace
    *
    *  @param name name of the Namespace
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1Namespace[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Namespace] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Namespace](_request)
  }

  /** partially update the specified Namespace
    *
    *  @param name name of the Namespace
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1Namespace[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Namespace] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Namespace](_request)
  }

  /** replace finalize of the specified Namespace
    *
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    *  @param name name of the Namespace
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    */
  def replaceCoreV1NamespaceFinalize[F[_]: Applicative: Sync](
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      name: String,
      pretty: Option[String] = None,
      body: Namespace
  ): Action[F, Namespace] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${name}/finalize"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Namespace](_request)
  }

  /** read status of the specified Namespace
    *
    *  @param name name of the Namespace
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1NamespaceStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None
  ): Action[F, Namespace] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Namespace](_request)
  }

  /** replace status of the specified Namespace
    *
    *  @param name name of the Namespace
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NamespaceStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Namespace,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Namespace] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Namespace](_request)
  }

  /** partially update status of the specified Namespace
    *
    *  @param name name of the Namespace
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NamespaceStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Namespace] = Kleisli { _config =>
    val _path = s"""/api/v1/namespaces/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Namespace](_request)
  }

  /** list or watch objects of kind Node
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1Node[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, NodeList] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[NodeList](_request)
  }

  /** create a Node
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1Node[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      body: Node,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Node] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Node](_request)
  }

  /** delete collection of Node
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionNode[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified Node
    *
    *  @param name name of the Node
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1Node[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, Node] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Node](_request)
  }

  /** replace the specified Node
    *
    *  @param name name of the Node
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1Node[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Node,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Node] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Node](_request)
  }

  /** delete a Node
    *
    *  @param name name of the Node
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1Node[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified Node
    *
    *  @param name name of the Node
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1Node[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Node] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Node](_request)
  }

  /** connect GET requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1GetNodeProxy[F[_]: Applicative: Sync](
      name: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PUT requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1PutNodeProxy[F[_]: Applicative: Sync](
      name: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect POST requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1PostNodeProxy[F[_]: Applicative: Sync](
      name: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect DELETE requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1DeleteNodeProxy[F[_]: Applicative: Sync](
      name: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PATCH requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1PatchNodeProxy[F[_]: Applicative: Sync](
      name: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect HEAD requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1HeadNodeProxy[F[_]: Applicative: Sync](
      name: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.HEAD

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect OPTIONS requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1OptionsNodeProxy[F[_]: Applicative: Sync](
      name: String,
      path: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path)

    val _method = Method.OPTIONS

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect GET requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1GetNodeProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy/${path}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PUT requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1PutNodeProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy/${path}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect POST requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1PostNodeProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy/${path}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect DELETE requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1DeleteNodeProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy/${path}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect PATCH requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1PatchNodeProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy/${path}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect HEAD requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1HeadNodeProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy/${path}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.HEAD

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** connect OPTIONS requests to proxy of Node
    *
    *  @param name name of the NodeProxyOptions
    *  @param path path to the resource
    *  @param path_2 Path is the URL path to use for the current proxy request to node.
    */
  def connectCoreV1OptionsNodeProxyWithPath[F[_]: Applicative: Sync](
      name: String,
      path: String,
      path_2: Option[String] = None
  ): Action[F, String] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/proxy/${path}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("path", path_2)

    val _method = Method.OPTIONS

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[String](_request)
  }

  /** read status of the specified Node
    *
    *  @param name name of the Node
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1NodeStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None
  ): Action[F, Node] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Node](_request)
  }

  /** replace status of the specified Node
    *
    *  @param name name of the Node
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1NodeStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Node,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Node] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Node](_request)
  }

  /** partially update status of the specified Node
    *
    *  @param name name of the Node
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1NodeStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Node] = Kleisli { _config =>
    val _path = s"""/api/v1/nodes/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Node](_request)
  }

  /** list or watch objects of kind PersistentVolumeClaim
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1PersistentVolumeClaimForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, PersistentVolumeClaimList] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumeclaims"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PersistentVolumeClaimList](_request)
  }

  /** list or watch objects of kind PersistentVolume
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1PersistentVolume[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, PersistentVolumeList] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PersistentVolumeList](_request)
  }

  /** create a PersistentVolume
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createCoreV1PersistentVolume[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      body: PersistentVolume,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, PersistentVolume] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolume](_request)
  }

  /** delete collection of PersistentVolume
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param body null
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def deleteCoreV1CollectionPersistentVolume[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      body: Option[DeleteOptions] = None,
      continue: Option[String] = None,
      dryRun: Option[String] = None,
      fieldSelector: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** read the specified PersistentVolume
    *
    *  @param name name of the PersistentVolume
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readCoreV1PersistentVolume[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, PersistentVolume] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("exact", exact)
      .withOptionQueryParam("export", export)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PersistentVolume](_request)
  }

  /** replace the specified PersistentVolume
    *
    *  @param name name of the PersistentVolume
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1PersistentVolume[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: PersistentVolume,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, PersistentVolume] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolume](_request)
  }

  /** delete a PersistentVolume
    *
    *  @param name name of the PersistentVolume
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteCoreV1PersistentVolume[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("gracePeriodSeconds", gracePeriodSeconds)
      .withOptionQueryParam("orphanDependents", orphanDependents)
      .withOptionQueryParam("propagationPolicy", propagationPolicy)

    val _method = Method.DELETE

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Status](_request)
  }

  /** partially update the specified PersistentVolume
    *
    *  @param name name of the PersistentVolume
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1PersistentVolume[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, PersistentVolume] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolume](_request)
  }

  /** read status of the specified PersistentVolume
    *
    *  @param name name of the PersistentVolume
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readCoreV1PersistentVolumeStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None
  ): Action[F, PersistentVolume] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PersistentVolume](_request)
  }

  /** replace status of the specified PersistentVolume
    *
    *  @param name name of the PersistentVolume
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceCoreV1PersistentVolumeStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: PersistentVolume,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, PersistentVolume] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.PUT

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolume](_request)
  }

  /** partially update status of the specified PersistentVolume
    *
    *  @param name name of the PersistentVolume
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchCoreV1PersistentVolumeStatus[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, PersistentVolume] = Kleisli { _config =>
    val _path = s"""/api/v1/persistentvolumes/${name}/status"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)
      .withOptionQueryParam("force", force)

    val _method = Method.PATCH

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[PersistentVolume](_request)
  }

  /** list or watch objects of kind Pod
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1PodForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, PodList] = Kleisli { _config =>
    val _path = s"""/api/v1/pods"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PodList](_request)
  }

  /** list or watch objects of kind PodTemplate
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1PodTemplateForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, PodTemplateList] = Kleisli { _config =>
    val _path = s"""/api/v1/podtemplates"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[PodTemplateList](_request)
  }

  /** list or watch objects of kind ReplicationController
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1ReplicationControllerForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ReplicationControllerList] = Kleisli { _config =>
    val _path = s"""/api/v1/replicationcontrollers"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ReplicationControllerList](_request)
  }

  /** list or watch objects of kind ResourceQuota
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1ResourceQuotaForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ResourceQuotaList] = Kleisli { _config =>
    val _path = s"""/api/v1/resourcequotas"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ResourceQuotaList](_request)
  }

  /** list or watch objects of kind Secret
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1SecretForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, SecretList] = Kleisli { _config =>
    val _path = s"""/api/v1/secrets"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[SecretList](_request)
  }

  /** list or watch objects of kind ServiceAccount
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1ServiceAccountForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ServiceAccountList] = Kleisli { _config =>
    val _path = s"""/api/v1/serviceaccounts"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ServiceAccountList](_request)
  }

  /** list or watch objects of kind Service
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def listCoreV1ServiceForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ServiceList] = Kleisli { _config =>
    val _path = s"""/api/v1/services"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ServiceList](_request)
  }

  /** watch individual changes to a list of ConfigMap. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1ConfigMapListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/configmaps"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Endpoints. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1EndpointsListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/endpoints"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Event. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1EventListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/events"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of LimitRange. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1LimitRangeListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/limitranges"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Namespace. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespaceList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of ConfigMap. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedConfigMapList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/configmaps"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind ConfigMap. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the ConfigMap
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedConfigMap[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/configmaps/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Endpoints. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedEndpointsList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/endpoints"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind Endpoints. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the Endpoints
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedEndpoints[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/endpoints/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Event. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedEventList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/events"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind Event. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the Event
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedEvent[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/events/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of LimitRange. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedLimitRangeList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/limitranges"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind LimitRange. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the LimitRange
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedLimitRange[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/limitranges/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of PersistentVolumeClaim. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedPersistentVolumeClaimList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path =
      s"""/api/v1/watch/namespaces/${namespace}/persistentvolumeclaims"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind PersistentVolumeClaim. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the PersistentVolumeClaim
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedPersistentVolumeClaim[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path =
      s"""/api/v1/watch/namespaces/${namespace}/persistentvolumeclaims/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Pod. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedPodList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/pods"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind Pod. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the Pod
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedPod[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/pods/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of PodTemplate. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedPodTemplateList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/podtemplates"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind PodTemplate. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the PodTemplate
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedPodTemplate[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path =
      s"""/api/v1/watch/namespaces/${namespace}/podtemplates/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of ReplicationController. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedReplicationControllerList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path =
      s"""/api/v1/watch/namespaces/${namespace}/replicationcontrollers"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind ReplicationController. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the ReplicationController
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedReplicationController[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path =
      s"""/api/v1/watch/namespaces/${namespace}/replicationcontrollers/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of ResourceQuota. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedResourceQuotaList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/resourcequotas"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind ResourceQuota. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the ResourceQuota
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedResourceQuota[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path =
      s"""/api/v1/watch/namespaces/${namespace}/resourcequotas/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Secret. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedSecretList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/secrets"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind Secret. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the Secret
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedSecret[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/secrets/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of ServiceAccount. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedServiceAccountList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/serviceaccounts"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind ServiceAccount. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the ServiceAccount
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedServiceAccount[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path =
      s"""/api/v1/watch/namespaces/${namespace}/serviceaccounts/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Service. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedServiceList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/services"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind Service. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the Service
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NamespacedService[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${namespace}/services/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind Namespace. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the Namespace
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1Namespace[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/namespaces/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Node. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1NodeList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/nodes"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind Node. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the Node
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1Node[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/nodes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of PersistentVolumeClaim. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1PersistentVolumeClaimListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/persistentvolumeclaims"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of PersistentVolume. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1PersistentVolumeList[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/persistentvolumes"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch changes to an object of kind PersistentVolume. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the PersistentVolume
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1PersistentVolume[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      name: String,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/persistentvolumes/${name}"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Pod. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1PodListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/pods"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of PodTemplate. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1PodTemplateListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/podtemplates"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of ReplicationController. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1ReplicationControllerListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/replicationcontrollers"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of ResourceQuota. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1ResourceQuotaListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/resourcequotas"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Secret. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1SecretListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/secrets"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of ServiceAccount. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1ServiceAccountListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/serviceaccounts"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

  /** watch individual changes to a list of Service. deprecated: use the 'watch' parameter with a list operation instead.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchCoreV1ServiceListForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, WatchEvent] = Kleisli { _config =>
    val _path = s"""/api/v1/watch/services"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("allowWatchBookmarks", allowWatchBookmarks)
      .withOptionQueryParam("continue", continue)
      .withOptionQueryParam("fieldSelector", fieldSelector)
      .withOptionQueryParam("labelSelector", labelSelector)
      .withOptionQueryParam("limit", limit)
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("resourceVersion", resourceVersion)
      .withOptionQueryParam("timeoutSeconds", timeoutSeconds)
      .withOptionQueryParam("watch", watch)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[WatchEvent](_request)
  }

}
