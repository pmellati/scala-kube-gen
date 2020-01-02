package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Volume(
    secret: Option[SecretVolumeSource] = None,
    azureFile: Option[AzureFileVolumeSource] = None,
    glusterfs: Option[GlusterfsVolumeSource] = None,
    flocker: Option[FlockerVolumeSource] = None,
    fc: Option[FCVolumeSource] = None,
    photonPersistentDisk: Option[PhotonPersistentDiskVolumeSource] = None,
    gcePersistentDisk: Option[GCEPersistentDiskVolumeSource] = None,
    gitRepo: Option[GitRepoVolumeSource] = None,
    cephfs: Option[CephFSVolumeSource] = None,
    awsElasticBlockStore: Option[AWSElasticBlockStoreVolumeSource] = None,
    downwardAPI: Option[DownwardAPIVolumeSource] = None,
    flexVolume: Option[FlexVolumeSource] = None,
    quobyte: Option[QuobyteVolumeSource] = None,
    storageos: Option[StorageOSVolumeSource] = None,
    scaleIO: Option[ScaleIOVolumeSource] = None,
    csi: Option[CSIVolumeSource] = None,
    name: String,
    azureDisk: Option[AzureDiskVolumeSource] = None,
    hostPath: Option[HostPathVolumeSource] = None,
    rbd: Option[RBDVolumeSource] = None,
    configMap: Option[ConfigMapVolumeSource] = None,
    portworxVolume: Option[PortworxVolumeSource] = None,
    persistentVolumeClaim: Option[PersistentVolumeClaimVolumeSource] = None,
    vsphereVolume: Option[VsphereVirtualDiskVolumeSource] = None,
    projected: Option[ProjectedVolumeSource] = None,
    iscsi: Option[ISCSIVolumeSource] = None,
    cinder: Option[CinderVolumeSource] = None,
    emptyDir: Option[EmptyDirVolumeSource] = None,
    nfs: Option[NFSVolumeSource] = None
)

object Volume {
  implicit val `io.k8s.api.core.v1.Volume-Decoder`: Decoder[Volume] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Volume-Encoder`: Encoder[Volume] =
    deriveEncoder
}
