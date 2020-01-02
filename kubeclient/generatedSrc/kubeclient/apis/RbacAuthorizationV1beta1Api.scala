package kubeclient

import cats.Applicative
import cats.data.Kleisli
import cats.effect.Sync

import _root_.io.circe._

import openapigen.ClientConfig

import org.http4s._, circe._, client.Client

import kubeclient.io.k8s.api.rbac.v1beta1.ClusterRole
import kubeclient.io.k8s.api.rbac.v1beta1.ClusterRoleBinding
import kubeclient.io.k8s.api.rbac.v1beta1.ClusterRoleBindingList
import kubeclient.io.k8s.api.rbac.v1beta1.ClusterRoleList
import kubeclient.io.k8s.api.rbac.v1beta1.Role
import kubeclient.io.k8s.api.rbac.v1beta1.RoleBinding
import kubeclient.io.k8s.api.rbac.v1beta1.RoleBindingList
import kubeclient.io.k8s.api.rbac.v1beta1.RoleList
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.APIResourceList
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.DeleteOptions
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Patch
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Status
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.WatchEvent

object RbacAuthorizationV1beta1Api {
  private implicit def circeEncoderToHttp4sEntityEncoder[F[_]: Applicative, A: Encoder]
      : EntityEncoder[F, A] = jsonEncoderOf
  private implicit def circeDecoderToHttp4sEntityDecoder[F[_]: Sync, A: Decoder]
      : EntityDecoder[F, A] = jsonOf

  type Action[F[_], A] = Kleisli[F, ClientConfig[F], A]

  /** get available resources
    *

    */
  def getRbacAuthorizationV1beta1APIResources[F[_]: Applicative: Sync]
      : Action[F, APIResourceList] = Kleisli { _config =>
    val _path = s"""/apis/rbac.authorization.k8s.io/v1beta1/"""
    val _uri  = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[APIResourceList](_request)
  }

  /** list or watch objects of kind ClusterRoleBinding
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
  def listRbacAuthorizationV1beta1ClusterRoleBinding[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ClusterRoleBindingList] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterrolebindings"""
    val _uri = _config.baseApiUri.withPath(_path)

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

    _config.httpClient.expect[ClusterRoleBindingList](_request)
  }

  /** create a ClusterRoleBinding
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createRbacAuthorizationV1beta1ClusterRoleBinding[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      body: ClusterRoleBinding,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ClusterRoleBinding] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterrolebindings"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[ClusterRoleBinding](_request)
  }

  /** delete collection of ClusterRoleBinding
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
  def deleteRbacAuthorizationV1beta1CollectionClusterRoleBinding[F[_]: Applicative: Sync](
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
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterrolebindings"""
    val _uri = _config.baseApiUri.withPath(_path)

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

  /** read the specified ClusterRoleBinding
    *
    *  @param name name of the ClusterRoleBinding
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readRbacAuthorizationV1beta1ClusterRoleBinding[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None
  ): Action[F, ClusterRoleBinding] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterrolebindings/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ClusterRoleBinding](_request)
  }

  /** replace the specified ClusterRoleBinding
    *
    *  @param name name of the ClusterRoleBinding
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceRbacAuthorizationV1beta1ClusterRoleBinding[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: ClusterRoleBinding,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ClusterRoleBinding] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterrolebindings/${name}"""
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

    _config.httpClient.expect[ClusterRoleBinding](_request)
  }

  /** delete a ClusterRoleBinding
    *
    *  @param name name of the ClusterRoleBinding
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteRbacAuthorizationV1beta1ClusterRoleBinding[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterrolebindings/${name}"""
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

  /** partially update the specified ClusterRoleBinding
    *
    *  @param name name of the ClusterRoleBinding
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchRbacAuthorizationV1beta1ClusterRoleBinding[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ClusterRoleBinding] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterrolebindings/${name}"""
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

    _config.httpClient.expect[ClusterRoleBinding](_request)
  }

  /** list or watch objects of kind ClusterRole
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
  def listRbacAuthorizationV1beta1ClusterRole[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, ClusterRoleList] = Kleisli { _config =>
    val _path = s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterroles"""
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

    _config.httpClient.expect[ClusterRoleList](_request)
  }

  /** create a ClusterRole
    *
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createRbacAuthorizationV1beta1ClusterRole[F[_]: Applicative: Sync](
      pretty: Option[String] = None,
      body: ClusterRole,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ClusterRole] = Kleisli { _config =>
    val _path = s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterroles"""
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

    _config.httpClient.expect[ClusterRole](_request)
  }

  /** delete collection of ClusterRole
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
  def deleteRbacAuthorizationV1beta1CollectionClusterRole[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterroles"""
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

  /** read the specified ClusterRole
    *
    *  @param name name of the ClusterRole
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readRbacAuthorizationV1beta1ClusterRole[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None
  ): Action[F, ClusterRole] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterroles/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[ClusterRole](_request)
  }

  /** replace the specified ClusterRole
    *
    *  @param name name of the ClusterRole
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceRbacAuthorizationV1beta1ClusterRole[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: ClusterRole,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, ClusterRole] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterroles/${name}"""
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

    _config.httpClient.expect[ClusterRole](_request)
  }

  /** delete a ClusterRole
    *
    *  @param name name of the ClusterRole
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteRbacAuthorizationV1beta1ClusterRole[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Option[DeleteOptions] = None,
      dryRun: Option[String] = None,
      gracePeriodSeconds: Option[Int] = None,
      orphanDependents: Option[Boolean] = None,
      propagationPolicy: Option[String] = None
  ): Action[F, Status] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterroles/${name}"""
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

  /** partially update the specified ClusterRole
    *
    *  @param name name of the ClusterRole
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchRbacAuthorizationV1beta1ClusterRole[F[_]: Applicative: Sync](
      name: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, ClusterRole] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/clusterroles/${name}"""
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

    _config.httpClient.expect[ClusterRole](_request)
  }

  /** list or watch objects of kind RoleBinding
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
  def listRbacAuthorizationV1beta1NamespacedRoleBinding[F[_]: Applicative: Sync](
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
  ): Action[F, RoleBindingList] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/rolebindings"""
    val _uri = _config.baseApiUri.withPath(_path)

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

    _config.httpClient.expect[RoleBindingList](_request)
  }

  /** create a RoleBinding
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createRbacAuthorizationV1beta1NamespacedRoleBinding[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: RoleBinding,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, RoleBinding] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/rolebindings"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[RoleBinding](_request)
  }

  /** delete collection of RoleBinding
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
  def deleteRbacAuthorizationV1beta1CollectionNamespacedRoleBinding[F[_]: Applicative: Sync](
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
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/rolebindings"""
    val _uri = _config.baseApiUri.withPath(_path)

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

  /** read the specified RoleBinding
    *
    *  @param name name of the RoleBinding
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readRbacAuthorizationV1beta1NamespacedRoleBinding[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, RoleBinding] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/rolebindings/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[RoleBinding](_request)
  }

  /** replace the specified RoleBinding
    *
    *  @param name name of the RoleBinding
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceRbacAuthorizationV1beta1NamespacedRoleBinding[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: RoleBinding,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, RoleBinding] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/rolebindings/${name}"""
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

    _config.httpClient.expect[RoleBinding](_request)
  }

  /** delete a RoleBinding
    *
    *  @param name name of the RoleBinding
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteRbacAuthorizationV1beta1NamespacedRoleBinding[F[_]: Applicative: Sync](
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
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/rolebindings/${name}"""
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

  /** partially update the specified RoleBinding
    *
    *  @param name name of the RoleBinding
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchRbacAuthorizationV1beta1NamespacedRoleBinding[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, RoleBinding] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/rolebindings/${name}"""
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

    _config.httpClient.expect[RoleBinding](_request)
  }

  /** list or watch objects of kind Role
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
  def listRbacAuthorizationV1beta1NamespacedRole[F[_]: Applicative: Sync](
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
  ): Action[F, RoleList] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/roles"""
    val _uri = _config.baseApiUri.withPath(_path)

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

    _config.httpClient.expect[RoleList](_request)
  }

  /** create a Role
    *
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def createRbacAuthorizationV1beta1NamespacedRole[F[_]: Applicative: Sync](
      namespace: String,
      pretty: Option[String] = None,
      body: Role,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Role] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/roles"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)
      .withOptionQueryParam("dryRun", dryRun)
      .withOptionQueryParam("fieldManager", fieldManager)

    val _method = Method.POST

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    ).withEntity(body)

    _config.httpClient.expect[Role](_request)
  }

  /** delete collection of Role
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
  def deleteRbacAuthorizationV1beta1CollectionNamespacedRole[F[_]: Applicative: Sync](
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
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/roles"""
    val _uri = _config.baseApiUri.withPath(_path)

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

  /** read the specified Role
    *
    *  @param name name of the Role
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    */
  def readRbacAuthorizationV1beta1NamespacedRole[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None
  ): Action[F, Role] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/roles/${name}"""
    val _uri = _config.baseApiUri.withPath(_path)

    val _uriWithQueryParams = _uri
      .withOptionQueryParam("pretty", pretty)

    val _method = Method.GET

    val _request = Request[F](
      method = _method,
      uri = _uriWithQueryParams
    )

    _config.httpClient.expect[Role](_request)
  }

  /** replace the specified Role
    *
    *  @param name name of the Role
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint.
    */
  def replaceRbacAuthorizationV1beta1NamespacedRole[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Role,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None
  ): Action[F, Role] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/roles/${name}"""
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

    _config.httpClient.expect[Role](_request)
  }

  /** delete a Role
    *
    *  @param name name of the Role
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param gracePeriodSeconds The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
    *  @param orphanDependents Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the "orphan" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
    *  @param propagationPolicy Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
    */
  def deleteRbacAuthorizationV1beta1NamespacedRole[F[_]: Applicative: Sync](
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
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/roles/${name}"""
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

  /** partially update the specified Role
    *
    *  @param name name of the Role
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param body null
    *  @param dryRun When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
    *  @param fieldManager fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
    *  @param force Force is going to "force" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
    */
  def patchRbacAuthorizationV1beta1NamespacedRole[F[_]: Applicative: Sync](
      name: String,
      namespace: String,
      pretty: Option[String] = None,
      body: Patch,
      dryRun: Option[String] = None,
      fieldManager: Option[String] = None,
      force: Option[Boolean] = None
  ): Action[F, Role] = Kleisli { _config =>
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/namespaces/${namespace}/roles/${name}"""
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

    _config.httpClient.expect[Role](_request)
  }

  /** list or watch objects of kind RoleBinding
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
  def listRbacAuthorizationV1beta1RoleBindingForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, RoleBindingList] = Kleisli { _config =>
    val _path = s"""/apis/rbac.authorization.k8s.io/v1beta1/rolebindings"""
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

    _config.httpClient.expect[RoleBindingList](_request)
  }

  /** list or watch objects of kind Role
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
  def listRbacAuthorizationV1beta1RoleForAllNamespaces[F[_]: Applicative: Sync](
      allowWatchBookmarks: Option[Boolean] = None,
      continue: Option[String] = None,
      fieldSelector: Option[String] = None,
      labelSelector: Option[String] = None,
      limit: Option[Int] = None,
      pretty: Option[String] = None,
      resourceVersion: Option[String] = None,
      timeoutSeconds: Option[Int] = None,
      watch: Option[Boolean] = None
  ): Action[F, RoleList] = Kleisli { _config =>
    val _path = s"""/apis/rbac.authorization.k8s.io/v1beta1/roles"""
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

    _config.httpClient.expect[RoleList](_request)
  }

  /** watch individual changes to a list of ClusterRoleBinding. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchRbacAuthorizationV1beta1ClusterRoleBindingList[F[_]: Applicative: Sync](
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
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/clusterrolebindings"""
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

  /** watch changes to an object of kind ClusterRoleBinding. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the ClusterRoleBinding
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchRbacAuthorizationV1beta1ClusterRoleBinding[F[_]: Applicative: Sync](
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
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/clusterrolebindings/${name}"""
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

  /** watch individual changes to a list of ClusterRole. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchRbacAuthorizationV1beta1ClusterRoleList[F[_]: Applicative: Sync](
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
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/clusterroles"""
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

  /** watch changes to an object of kind ClusterRole. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the ClusterRole
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchRbacAuthorizationV1beta1ClusterRole[F[_]: Applicative: Sync](
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
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/clusterroles/${name}"""
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

  /** watch individual changes to a list of RoleBinding. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchRbacAuthorizationV1beta1NamespacedRoleBindingList[F[_]: Applicative: Sync](
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
      s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/namespaces/${namespace}/rolebindings"""
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

  /** watch changes to an object of kind RoleBinding. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the RoleBinding
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchRbacAuthorizationV1beta1NamespacedRoleBinding[F[_]: Applicative: Sync](
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
      s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/namespaces/${namespace}/rolebindings/${name}"""
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

  /** watch individual changes to a list of Role. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchRbacAuthorizationV1beta1NamespacedRoleList[F[_]: Applicative: Sync](
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
      s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/namespaces/${namespace}/roles"""
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

  /** watch changes to an object of kind Role. deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter.
    *
    *  @param allowWatchBookmarks allowWatchBookmarks requests watch events with type "BOOKMARK". Servers that do not implement bookmarks may ignore this flag and bookmarks are sent at the server's discretion. Clients should not assume bookmarks are returned at any specific interval, nor may they assume the server will send any BOOKMARK event during a session. If this is not a watch, this field is ignored. If the feature gate WatchBookmarks is not enabled in apiserver, this field is ignored.

This field is beta.
    *  @param continue The continue option should be set when retrieving more results from the server. Since this value is server defined, clients may only use the continue value from a previous query result with identical query parameters (except for the value of continue) and the server may reject a continue value it does not recognize. If the specified continue value is no longer valid whether due to expiration (generally five to fifteen minutes) or a configuration change on the server, the server will respond with a 410 ResourceExpired error together with a continue token. If the client needs a consistent list, it must restart their list without the continue field. Otherwise, the client may send another list request with the token received with the 410 error, the server will respond with a list starting from the next key, but from the latest snapshot, which is inconsistent from the previous list results - objects that are created, modified, or deleted after the first list request will be included in the response, as long as their keys are after the "next key".

This field is not supported when watch is true. Clients may start a watch from the last resourceVersion value returned by the server and not miss any modifications.
    *  @param fieldSelector A selector to restrict the list of returned objects by their fields. Defaults to everything.
    *  @param labelSelector A selector to restrict the list of returned objects by their labels. Defaults to everything.
    *  @param limit limit is a maximum number of responses to return for a list call. If more items exist, the server will set the `continue` field on the list metadata to a value that can be used with the same initial query to retrieve the next set of results. Setting a limit may return fewer than the requested amount of items (up to zero items) in the event all requested objects are filtered out and clients should only use the presence of the continue field to determine whether more results are available. Servers may choose not to support the limit argument and will return all of the available results. If limit is specified and the continue field is empty, clients may assume that no more results are available. This field is not supported if watch is true.

The server guarantees that the objects returned when using continue will be identical to issuing a single list call without a limit - that is, no objects created, modified, or deleted after the first request is issued will be included in any subsequent continued requests. This is sometimes referred to as a consistent snapshot, and ensures that a client that is using limit to receive smaller chunks of a very large result can ensure they see all possible objects. If objects are updated during a chunked list the version of the object that was present at the time the first list result was calculated is returned.
    *  @param name name of the Role
    *  @param namespace object name and auth scope, such as for teams and projects
    *  @param pretty If 'true', then the output is pretty printed.
    *  @param resourceVersion When specified with a watch call, shows changes that occur after that particular version of a resource. Defaults to changes from the beginning of history. When specified for list: - if unset, then the result is returned from remote storage based on quorum-read flag; - if it's 0, then we simply return what we currently have in cache, no guarantee; - if set to non zero, then the result is at least as fresh as given rv.
    *  @param timeoutSeconds Timeout for the list/watch call. This limits the duration of the call, regardless of any activity or inactivity.
    *  @param watch Watch for changes to the described resources and return them as a stream of add, update, and remove notifications. Specify resourceVersion.
    */
  def watchRbacAuthorizationV1beta1NamespacedRole[F[_]: Applicative: Sync](
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
      s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/namespaces/${namespace}/roles/${name}"""
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

  /** watch individual changes to a list of RoleBinding. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchRbacAuthorizationV1beta1RoleBindingListForAllNamespaces[F[_]: Applicative: Sync](
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
    val _path =
      s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/rolebindings"""
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

  /** watch individual changes to a list of Role. deprecated: use the 'watch' parameter with a list operation instead.
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
  def watchRbacAuthorizationV1beta1RoleListForAllNamespaces[F[_]: Applicative: Sync](
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
    val _path = s"""/apis/rbac.authorization.k8s.io/v1beta1/watch/roles"""
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
