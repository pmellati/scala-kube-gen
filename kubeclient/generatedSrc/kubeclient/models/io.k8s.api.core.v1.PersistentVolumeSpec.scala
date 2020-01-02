package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PersistentVolumeSpec(
    azureFile: Option[AzureFilePersistentVolumeSource] = None,
    portworxVolume: Option[PortworxVolumeSource] = None,
    volumeMode: Option[String] = None,
    flocker: Option[FlockerVolumeSource] = None,
    local: Option[LocalVolumeSource] = None,
    gcePersistentDisk: Option[GCEPersistentDiskVolumeSource] = None,
    vsphereVolume: Option[VsphereVirtualDiskVolumeSource] = None,
    cephfs: Option[CephFSPersistentVolumeSource] = None,
    awsElasticBlockStore: Option[AWSElasticBlockStoreVolumeSource] = None,
    flexVolume: Option[FlexPersistentVolumeSource] = None,
    nfs: Option[NFSVolumeSource] = None,
    quobyte: Option[QuobyteVolumeSource] = None,
    storageos: Option[StorageOSPersistentVolumeSource] = None,
    capacity: Option[Map[String, Quantity]] = None,
    scaleIO: Option[ScaleIOPersistentVolumeSource] = None,
    mountOptions: Option[List[String]] = None,
    csi: Option[CSIPersistentVolumeSource] = None,
    accessModes: Option[List[String]] = None,
    nodeAffinity: Option[VolumeNodeAffinity] = None,
    azureDisk: Option[AzureDiskVolumeSource] = None,
    hostPath: Option[HostPathVolumeSource] = None,
    rbd: Option[RBDPersistentVolumeSource] = None,
    persistentVolumeReclaimPolicy: Option[String] = None,
    glusterfs: Option[GlusterfsPersistentVolumeSource] = None,
    claimRef: Option[ObjectReference] = None,
    fc: Option[FCVolumeSource] = None,
    storageClassName: Option[String] = None,
    photonPersistentDisk: Option[PhotonPersistentDiskVolumeSource] = None,
    iscsi: Option[ISCSIPersistentVolumeSource] = None,
    cinder: Option[CinderPersistentVolumeSource] = None
)

object PersistentVolumeSpec {
  implicit val `io.k8s.api.core.v1.PersistentVolumeSpec-Decoder`
      : Decoder[PersistentVolumeSpec] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PersistentVolumeSpec-Encoder`
      : Encoder[PersistentVolumeSpec] = deriveEncoder
}
