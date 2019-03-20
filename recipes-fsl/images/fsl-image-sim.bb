# Copyright 2018 NXP
#
# The minimal rootfs with basic packages for boot on VDK
#

require recipes-fsl/images/fsl-image-base.bb

# Support for Inter-Process(or) Communication over Shared Memory (ipc-shm) under Linux
IMAGE_INSTALL_append_s32g275sim = " ipc-shm "
IMAGE_INSTALL_append_s32v344sim = " ipc-shm "
IMAGE_INSTALL_append_s32r45xsim = " ipc-shm "

IMAGE_INSTALL_append_gen1 ?= "${@bb.utils.contains('DISTRO_FEATURES', 'gmac', ' gmac ', '', d)}"
