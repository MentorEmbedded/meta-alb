require conf/machine/include/s32-base.inc

IMAGE_FSTYPES += "cpio.gz"
IMAGE_FSTYPES:remove = "flashimage"

KERNEL_DEFCONFIG ?= "s32gen1_defconfig"

UBOOT_CONFIG = "cpio"

VIRTUAL-RUNTIME:dev_manager = "busybox-mdev"
VIRTUAL-RUNTIME:login_manager = "busybox"
VIRTUAL-RUNTIME:init_manager = "busybox"
VIRTUAL-RUNTIME:initscripts = "initscripts"
VIRTUAL-RUNTIME:keymaps = "keymaps"
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
DISTRO_FEATURES:append = " virtio "

SOC_FAMILY:append = ":gen1"
