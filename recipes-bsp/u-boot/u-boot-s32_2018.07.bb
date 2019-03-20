require recipes-bsp/u-boot/u-boot.inc

inherit fsl-u-boot-localversion

DESCRIPTION = "U-boot provided by NXP with focus on S32 chipsets"
PROVIDES = "virtual/bootloader u-boot"

LICENSE = "GPLv2 & BSD-3-Clause & BSD-2-Clause & LGPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = " \
    file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://Licenses/bsd-2-clause.txt;md5=6a31f076f5773aabd8ff86191ad6fdd5 \
    file://Licenses/bsd-3-clause.txt;md5=4a1190eac56a9db675d58ebe86eaf50c \
    file://Licenses/lgpl-2.0.txt;md5=5f30f0716dfdd0d91eb439ebec522ec2 \
    file://Licenses/lgpl-2.1.txt;md5=4fbd65380cdd255951079008b364516c \
"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "libgcc virtual/${TARGET_PREFIX}gcc dtc-native bc-native"

SRC_URI = "git://source.codeaurora.org/external/autobsps32/u-boot;protocol=https;branch=alb/master"

SRCREV = "33111da4309c04921ce592ee3c2a571f54f33402"

SCMVERSION = "y"
LOCALVERSION = ""

KERNEL_SINGLE_CORE ??= "0"

# Support for generating default environment
SRC_URI += " \
    file://0001-env-Add-Makefile-rule-to-generate-default-environment-2018.patch \
"

# Force booting linux single core on demand
SRC_URI_append_gen1 = "${@oe.utils.conditional("KERNEL_SINGLE_CORE", "0", "", " file://0001-s32gen1-Force-kernel-to-boot-single-core.patch ", d)}"

# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
                 HOSTLDFLAGS="${BUILD_LDFLAGS}" \
                 HOSTSTRIP=true'

PACKAGE_ARCH = "${MACHINE_ARCH}"

USRC ?= ""
S = '${@oe.utils.conditional("USRC", "", "${WORKDIR}/git", "${USRC}", d)}'

do_compile_append() {
    unset i j k
    for config in ${UBOOT_MACHINE}; do
        i=`expr $i + 1`;
        for type in ${UBOOT_CONFIG}; do
            j=`expr $j + 1`;
            for binary in ${UBOOT_BINARIES}; do
                k=`expr $k + 1`
                if [ $j -eq $i ] && [ $k -eq $i ]; then
                    # Generate an u-boot image which can be flashed and booted via QSPI
                    if [ "qspi" = "${type}" ];then
                        cp -r ${S}/tools/s32v234-qspi/ ${B}/${config}/tools/s32v234-qspi/
                        cd ${B}/${config}/tools/s32v234-qspi/
                        oe_runmake
                        mv -f ${B}/${config}/${binary} ${B}/${config}/${binary}.nospi
                        cp ${B}/${config}/tools/s32v234-qspi/${binary}.qspi ${B}/${config}/${binary}
                        cp ${B}/${config}/${binary} ${B}/${config}/u-boot-${type}.${UBOOT_SUFFIX}
                        cd -
                    fi
                    cp ${config}/${binary} ${config}/u-boot-${type}-${PV}-${PR}.${UBOOT_SUFFIX}
                fi
            done
            unset k
        done
        unset j
    done
    unset i
}

ENV_STAGE_DIR = "${datadir}/env"

do_install_append() {
    unset i j
    mkdir -p ${D}${ENV_STAGE_DIR}
    # we should have one config
    for config in ${UBOOT_MACHINE}; do
        i=`expr $i + 1`;
        unset j
        for type in ${UBOOT_CONFIG}; do
            j=`expr $j + 1`;
            if [ $j -eq $i ]; then
                # remove any empty lines which might break the environment
                sed '/^[[:space:]]*$/d' -i ${B}/${config}/env-default.txt
                # install our environment file to usr/share to have it staged by yocto
                install ${B}/${config}/env-default.txt ${D}${ENV_STAGE_DIR}/u-boot-default-flashenv${type}.txt
            fi
        done
    done
}

FILES_${PN} += "${ENV_STAGE_DIR}"

COMPATIBLE_MACHINE = "s32"
