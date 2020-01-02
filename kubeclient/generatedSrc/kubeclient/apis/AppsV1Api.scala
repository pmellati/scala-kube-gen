package kubeclient

import cats.Applicative
import cats.data.Kleisli
import cats.effect.Sync

import _root_.io.circe._

import openapigen.ClientConfig

import org.http4s._, circe._, client.Client

import kubeclient.io.k8s.api.apps.v1.ControllerRevision
import kubeclient.io.k8s.api.apps.v1.ControllerRevisionList
import kubeclient.io.k8s.api.apps.v1.DaemonSet
import kubeclient.io.k8s.api.apps.v1.DaemonSetList
import kubeclient.io.k8s.api.apps.v1.Deployment
import kubeclient.io.k8s.api.apps.v1.DeploymentList
import kubeclient.io.k8s.api.apps.v1.ReplicaSet
import kubeclient.io.k8s.api.apps.v1.ReplicaSetList
import kubeclient.io.k8s.api.apps.v1.StatefulSet
import kubeclient.io.k8s.api.apps.v1.StatefulSetList
import kubeclient.io.k8s.api.autoscaling.v1.Scale
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.APIResourceList
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.DeleteOptions
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Patch
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Status
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.WatchEvent

object AppsV1Api {
  private implicit def circeEncoderToHttp4sEntityEncoder[F[_]: Applicative, A: Encoder]
      : EntityEncoder[F, A] = jsonEncoderOf
  private implicit def circeDecoderToHttp4sEntityDecoder[F[_]: Sync, A: Decoder]
      : EntityDecoder[F, A] = jsonOf

  type Action[F[_], A] = Kleisli[F, ClientConfig[F], A]

  /** get available resources
    *

    */
  def getAppsV1APIResources[F[_]: Applicative: Sync]
      : Action[F, APIResourceList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[APIResourceList](_request)
  }

  /** list or watch objects of kind ControllerRevision
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
  def listAppsV1ControllerRevisionForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ControllerRevisionList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/controllerrevisions"""
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

    _config.httpClient.expect[ControllerRevisionList](_request)
  }

  /** list or watch objects of kind DaemonSet
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
  def listAppsV1DaemonSetForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, DaemonSetList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/daemonsets"""
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

    _config.httpClient.expect[DaemonSetList](_request)
  }

  /** list or watch objects of kind Deployment
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
  def listAppsV1DeploymentForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, DeploymentList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/deployments"""
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

    _config.httpClient.expect[DeploymentList](_request)
  }

  /** list or watch objects of kind ControllerRevision
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
  def listAppsV1NamespacedControllerRevision[F[_]: Applicative: Sync](
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
  ): Action[F, ControllerRevisionList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/controllerrevisions"""
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

    _config.httpClient.expect[ControllerRevisionList](_request)
  }

  /** create a ControllerRevision
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createAppsV1NamespacedControllerRevision[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: ControllerRevision,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ControllerRevision] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/controllerrevisions"""
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

    _config.httpClient.expect[ControllerRevision](_request)
  }

  /** delete collection of ControllerRevision
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
  def deleteAppsV1CollectionNamespacedControllerRevision[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/controllerrevisions"""
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

  /** read the specified ControllerRevision
    *
    *  @param name name of the ControllerRevision
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readAppsV1NamespacedControllerRevision[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, ControllerRevision] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/controllerrevisions/${name}"""
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

    _config.httpClient.expect[ControllerRevision](_request)
  }

  /** replace the specified ControllerRevision
    *
    *  @param name name of the ControllerRevision
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedControllerRevision[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: ControllerRevision,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ControllerRevision] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/controllerrevisions/${name}"""
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

    _config.httpClient.expect[ControllerRevision](_request)
  }

  /** delete a ControllerRevision
    *
    *  @param name name of the ControllerRevision
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteAppsV1NamespacedControllerRevision[F[_]: Applicative: Sync](
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
      s"""/apis/apps/v1/namespaces/${namespace}/controllerrevisions/${name}"""
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

  /** partially update the specified ControllerRevision
    *
    *  @param name name of the ControllerRevision
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedControllerRevision[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ControllerRevision] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/controllerrevisions/${name}"""
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

    _config.httpClient.expect[ControllerRevision](_request)
  }

  /** list or watch objects of kind DaemonSet
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
  def listAppsV1NamespacedDaemonSet[F[_]: Applicative: Sync](
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
  ): Action[F, DaemonSetList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/daemonsets"""
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

    _config.httpClient.expect[DaemonSetList](_request)
  }

  /** create a DaemonSet
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createAppsV1NamespacedDaemonSet[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: DaemonSet,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, DaemonSet] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/daemonsets"""
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

    _config.httpClient.expect[DaemonSet](_request)
  }

  /** delete collection of DaemonSet
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
  def deleteAppsV1CollectionNamespacedDaemonSet[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/daemonsets"""
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

  /** read the specified DaemonSet
    *
    *  @param name name of the DaemonSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readAppsV1NamespacedDaemonSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, DaemonSet] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/daemonsets/${name}"""
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

    _config.httpClient.expect[DaemonSet](_request)
  }

  /** replace the specified DaemonSet
    *
    *  @param name name of the DaemonSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedDaemonSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: DaemonSet,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, DaemonSet] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/daemonsets/${name}"""
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

    _config.httpClient.expect[DaemonSet](_request)
  }

  /** delete a DaemonSet
    *
    *  @param name name of the DaemonSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteAppsV1NamespacedDaemonSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/daemonsets/${name}"""
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

  /** partially update the specified DaemonSet
    *
    *  @param name name of the DaemonSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedDaemonSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, DaemonSet] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/daemonsets/${name}"""
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

    _config.httpClient.expect[DaemonSet](_request)
  }

  /** read status of the specified DaemonSet
    *
    *  @param name name of the DaemonSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readAppsV1NamespacedDaemonSetStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, DaemonSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/daemonsets/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[DaemonSet](_request)
  }

  /** replace status of the specified DaemonSet
    *
    *  @param name name of the DaemonSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedDaemonSetStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: DaemonSet,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, DaemonSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/daemonsets/${name}/status"""
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

    _config.httpClient.expect[DaemonSet](_request)
  }

  /** partially update status of the specified DaemonSet
    *
    *  @param name name of the DaemonSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedDaemonSetStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, DaemonSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/daemonsets/${name}/status"""
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

    _config.httpClient.expect[DaemonSet](_request)
  }

  /** list or watch objects of kind Deployment
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
  def listAppsV1NamespacedDeployment[F[_]: Applicative: Sync](
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
  ): Action[F, DeploymentList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/deployments"""
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

    _config.httpClient.expect[DeploymentList](_request)
  }

  /** create a Deployment
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createAppsV1NamespacedDeployment[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: Deployment,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Deployment] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/deployments"""
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

    _config.httpClient.expect[Deployment](_request)
  }

  /** delete collection of Deployment
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
  def deleteAppsV1CollectionNamespacedDeployment[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/deployments"""
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

  /** read the specified Deployment
    *
    *  @param name name of the Deployment
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readAppsV1NamespacedDeployment[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, Deployment] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}"""
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

    _config.httpClient.expect[Deployment](_request)
  }

  /** replace the specified Deployment
    *
    *  @param name name of the Deployment
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedDeployment[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Deployment,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Deployment] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}"""
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

    _config.httpClient.expect[Deployment](_request)
  }

  /** delete a Deployment
    *
    *  @param name name of the Deployment
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteAppsV1NamespacedDeployment[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}"""
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

  /** partially update the specified Deployment
    *
    *  @param name name of the Deployment
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedDeployment[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Deployment] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}"""
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

    _config.httpClient.expect[Deployment](_request)
  }

  /** read scale of the specified Deployment
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readAppsV1NamespacedDeploymentScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}/scale"""
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

  /** replace scale of the specified Deployment
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedDeploymentScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Scale,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}/scale"""
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

  /** partially update scale of the specified Deployment
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedDeploymentScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}/scale"""
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

  /** read status of the specified Deployment
    *
    *  @param name name of the Deployment
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readAppsV1NamespacedDeploymentStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, Deployment] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Deployment](_request)
  }

  /** replace status of the specified Deployment
    *
    *  @param name name of the Deployment
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedDeploymentStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Deployment,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Deployment] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}/status"""
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

    _config.httpClient.expect[Deployment](_request)
  }

  /** partially update status of the specified Deployment
    *
    *  @param name name of the Deployment
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedDeploymentStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Deployment] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/deployments/${name}/status"""
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

    _config.httpClient.expect[Deployment](_request)
  }

  /** list or watch objects of kind ReplicaSet
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
  def listAppsV1NamespacedReplicaSet[F[_]: Applicative: Sync](
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
  ): Action[F, ReplicaSetList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/replicasets"""
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

    _config.httpClient.expect[ReplicaSetList](_request)
  }

  /** create a ReplicaSet
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createAppsV1NamespacedReplicaSet[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: ReplicaSet,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ReplicaSet] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/replicasets"""
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

    _config.httpClient.expect[ReplicaSet](_request)
  }

  /** delete collection of ReplicaSet
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
  def deleteAppsV1CollectionNamespacedReplicaSet[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/replicasets"""
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

  /** read the specified ReplicaSet
    *
    *  @param name name of the ReplicaSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readAppsV1NamespacedReplicaSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, ReplicaSet] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}"""
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

    _config.httpClient.expect[ReplicaSet](_request)
  }

  /** replace the specified ReplicaSet
    *
    *  @param name name of the ReplicaSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedReplicaSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: ReplicaSet,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ReplicaSet] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}"""
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

    _config.httpClient.expect[ReplicaSet](_request)
  }

  /** delete a ReplicaSet
    *
    *  @param name name of the ReplicaSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteAppsV1NamespacedReplicaSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}"""
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

  /** partially update the specified ReplicaSet
    *
    *  @param name name of the ReplicaSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedReplicaSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ReplicaSet] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}"""
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

    _config.httpClient.expect[ReplicaSet](_request)
  }

  /** read scale of the specified ReplicaSet
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readAppsV1NamespacedReplicaSetScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}/scale"""
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

  /** replace scale of the specified ReplicaSet
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedReplicaSetScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Scale,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}/scale"""
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

  /** partially update scale of the specified ReplicaSet
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedReplicaSetScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}/scale"""
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

  /** read status of the specified ReplicaSet
    *
    *  @param name name of the ReplicaSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readAppsV1NamespacedReplicaSetStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, ReplicaSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ReplicaSet](_request)
  }

  /** replace status of the specified ReplicaSet
    *
    *  @param name name of the ReplicaSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedReplicaSetStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: ReplicaSet,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ReplicaSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}/status"""
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

    _config.httpClient.expect[ReplicaSet](_request)
  }

  /** partially update status of the specified ReplicaSet
    *
    *  @param name name of the ReplicaSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedReplicaSetStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ReplicaSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/replicasets/${name}/status"""
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

    _config.httpClient.expect[ReplicaSet](_request)
  }

  /** list or watch objects of kind StatefulSet
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
  def listAppsV1NamespacedStatefulSet[F[_]: Applicative: Sync](
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
  ): Action[F, StatefulSetList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/statefulsets"""
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

    _config.httpClient.expect[StatefulSetList](_request)
  }

  /** create a StatefulSet
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createAppsV1NamespacedStatefulSet[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: StatefulSet,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, StatefulSet] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/statefulsets"""
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

    _config.httpClient.expect[StatefulSet](_request)
  }

  /** delete collection of StatefulSet
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
  def deleteAppsV1CollectionNamespacedStatefulSet[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/namespaces/${namespace}/statefulsets"""
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

  /** read the specified StatefulSet
    *
    *  @param name name of the StatefulSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param exact Should the export be exact.  Exact export maintains cluster-specific fields like 'Namespace'. Deprecated. Planned for removal in 1.18.
    *  @param export Should this value be exported.  Export strips fields that a user can not specify. Deprecated. Planned for removal in 1.18.
    */
  def readAppsV1NamespacedStatefulSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      exact: Option[Boolean] = None,
      export: Option[Boolean] = None
  ): Action[F, StatefulSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}"""
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

    _config.httpClient.expect[StatefulSet](_request)
  }

  /** replace the specified StatefulSet
    *
    *  @param name name of the StatefulSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedStatefulSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: StatefulSet,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, StatefulSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}"""
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

    _config.httpClient.expect[StatefulSet](_request)
  }

  /** delete a StatefulSet
    *
    *  @param name name of the StatefulSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteAppsV1NamespacedStatefulSet[F[_]: Applicative: Sync](
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
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}"""
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

  /** partially update the specified StatefulSet
    *
    *  @param name name of the StatefulSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedStatefulSet[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, StatefulSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}"""
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

    _config.httpClient.expect[StatefulSet](_request)
  }

  /** read scale of the specified StatefulSet
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readAppsV1NamespacedStatefulSetScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}/scale"""
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

  /** replace scale of the specified StatefulSet
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedStatefulSetScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Scale,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}/scale"""
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

  /** partially update scale of the specified StatefulSet
    *
    *  @param name name of the Scale
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedStatefulSetScale[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Scale] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}/scale"""
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

  /** read status of the specified StatefulSet
    *
    *  @param name name of the StatefulSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readAppsV1NamespacedStatefulSetStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, StatefulSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}/status"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[StatefulSet](_request)
  }

  /** replace status of the specified StatefulSet
    *
    *  @param name name of the StatefulSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceAppsV1NamespacedStatefulSetStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: StatefulSet,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, StatefulSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}/status"""
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

    _config.httpClient.expect[StatefulSet](_request)
  }

  /** partially update status of the specified StatefulSet
    *
    *  @param name name of the StatefulSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchAppsV1NamespacedStatefulSetStatus[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, StatefulSet] = Kleisli { _config =>
    val _path =
      s"""/apis/apps/v1/namespaces/${namespace}/statefulsets/${name}/status"""
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

    _config.httpClient.expect[StatefulSet](_request)
  }

  /** list or watch objects of kind ReplicaSet
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
  def listAppsV1ReplicaSetForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ReplicaSetList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/replicasets"""
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

    _config.httpClient.expect[ReplicaSetList](_request)
  }

  /** list or watch objects of kind StatefulSet
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
  def listAppsV1StatefulSetForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, StatefulSetList] = Kleisli { _config =>
    val _path = s"""/apis/apps/v1/statefulsets"""
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

    _config.httpClient.expect[StatefulSetList](_request)
  }

  /** watch individual changes to a list of ControllerRevision. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1ControllerRevisionListForAllNamespaces[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/watch/controllerrevisions"""
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

  /** watch individual changes to a list of DaemonSet. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1DaemonSetListForAllNamespaces[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/watch/daemonsets"""
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

  /** watch individual changes to a list of Deployment. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1DeploymentListForAllNamespaces[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/watch/deployments"""
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

  /** watch individual changes to a list of ControllerRevision. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1NamespacedControllerRevisionList[F[_]: Applicative: Sync](
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
      s"""/apis/apps/v1/watch/namespaces/${namespace}/controllerrevisions"""
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

  /** watch changes to an object of kind ControllerRevision. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the ControllerRevision
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchAppsV1NamespacedControllerRevision[F[_]: Applicative: Sync](
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
      s"""/apis/apps/v1/watch/namespaces/${namespace}/controllerrevisions/${name}"""
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

  /** watch individual changes to a list of DaemonSet. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1NamespacedDaemonSetList[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/watch/namespaces/${namespace}/daemonsets"""
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

  /** watch changes to an object of kind DaemonSet. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the DaemonSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchAppsV1NamespacedDaemonSet[F[_]: Applicative: Sync](
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
      s"""/apis/apps/v1/watch/namespaces/${namespace}/daemonsets/${name}"""
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

  /** watch individual changes to a list of Deployment. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1NamespacedDeploymentList[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/watch/namespaces/${namespace}/deployments"""
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

  /** watch changes to an object of kind Deployment. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the Deployment
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchAppsV1NamespacedDeployment[F[_]: Applicative: Sync](
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
      s"""/apis/apps/v1/watch/namespaces/${namespace}/deployments/${name}"""
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

  /** watch individual changes to a list of ReplicaSet. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1NamespacedReplicaSetList[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/watch/namespaces/${namespace}/replicasets"""
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

  /** watch changes to an object of kind ReplicaSet. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the ReplicaSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchAppsV1NamespacedReplicaSet[F[_]: Applicative: Sync](
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
      s"""/apis/apps/v1/watch/namespaces/${namespace}/replicasets/${name}"""
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

  /** watch individual changes to a list of StatefulSet. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1NamespacedStatefulSetList[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/watch/namespaces/${namespace}/statefulsets"""
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

  /** watch changes to an object of kind StatefulSet. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the StatefulSet
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchAppsV1NamespacedStatefulSet[F[_]: Applicative: Sync](
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
      s"""/apis/apps/v1/watch/namespaces/${namespace}/statefulsets/${name}"""
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

  /** watch individual changes to a list of ReplicaSet. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1ReplicaSetListForAllNamespaces[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/watch/replicasets"""
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

  /** watch individual changes to a list of StatefulSet. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchAppsV1StatefulSetListForAllNamespaces[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/apps/v1/watch/statefulsets"""
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
