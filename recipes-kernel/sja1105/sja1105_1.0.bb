# Copyright 2017-2020 NXP

SUMMARY = "Add support for SJA1105 switch for S32V234EVB, BB Mini, S32G-PROCEVB-S plus S32GRV-PLATEVB and S32G-VNP-RDB"
LICENSE = "GPLv2+ & MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit module

SRC_URI = "git://source.codeaurora.org/external/autobsps32/sja1105x;branch=alb/master;protocol=https"
SRCREV = "8569b7eccbf55f83b516425ac2bf75e928f29afc"

KERNEL_NAME = "${PREFERRED_PROVIDER_virtual/kernel}"
KERNEL_VER = '${@d.getVar("PREFERRED_VERSION_${KERNEL_NAME}",True)}'

# Customizations for some kernel versions
OTHER_KERNEL_INCLUDE = "sja1105-append-${KERNEL_VER}.inc"

include ${OTHER_KERNEL_INCLUDE}

S = "${WORKDIR}/git"
DESTDIR = "${D}"
EXTRA_OEMAKE_append = " INSTALL_DIR=${DESTDIR} KERNELDIR=${KBUILD_OUTPUT} MYCOMPILER=${CROSS_COMPILE}gcc "
EXTRA_OEMAKE_append_s32v234evb = " MYPLATFORM=evb "
EXTRA_OEMAKE_append_s32v234bbmini = " MYPLATFORM=bbmini "
EXTRA_OEMAKE_append_s32g274aevb = " MYPLATFORM=gplat "
EXTRA_OEMAKE_append_s32g274ardb = " MYPLATFORM=rdb "

KERNEL_MODULE_AUTOLOAD += "sja1105pqrs"

FILES_${PN} += "${base_libdir}/*"
FILES_${PN} += "${sysconfdir}/modules-load.d/*"

PROVIDES = "kernel-module-sja1105pqrs${KERNEL_MODULE_PACKAGE_SUFFIX}"
RPROVIDES_${PN} = "kernel-module-sja1105pqrs${KERNEL_MODULE_PACKAGE_SUFFIX}"

COMPATIBLE_MACHINE = "s32v234evb|s32v234bbmini|s32g274aevb|s32g274ardb"
INHIBIT_PACKAGE_STRIP = "1"

DEPENDS_append = " coreutils-native"
