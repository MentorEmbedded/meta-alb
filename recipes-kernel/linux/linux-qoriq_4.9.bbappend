FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

COMMONSRC_URI = " \
	file://0001-kernel-Added-BB-Mini-device-tree.patch \
	file://0001-gpio-workaround-for-gpio-interrupts.patch \
\
	file://0001-cache-export-function-__dma_flush_area.patch \
	file://0002-dtb-add-shared-mem-block-as-reserved-memory-node.patch \
	file://0001-vnet-Fixed-up-cache-flush-to-use-kernel-4.9-API.patch \
\
    file://blueboxconfig \
    file://enablepktgen.cfg \
    file://iptables.cfg \
    file://iso9660.cfg \
    file://enableusbcan.cfg \
    file://enableusbftdi.cfg \
    file://containers.config \
    file://docker.cfg \
    file://lxc.cfg \
"

SRC_URI_append_ls2 += " \
    ${COMMONSRC_URI} \
    file://ls2blueboxconfig \
    file://enablegfxhwls2.cfg \
    file://dpaa2qdma.cfg \
"

# add sources for virtual ethernet over PCIe
SRC_URI_append_ls2084abbmini += " \
    git://source.codeaurora.org/external/autobsps32/vnet;protocol=https;branch=alb/master;name=vnet;destsuffix=git/drivers/net/vnet \
    file://0001-vnet-Add-initial-support-to-build-driver-in-kernel.patch \
    file://vnet_ls2.cfg \
    file://0001-vnet-Added-support-for-S32V-with-encryption-enabled.patch \
"
SRCREV_vnet = "0a4dfebd961466f43e45047078a0c0e433a4470c"

# Optional to simplify Ethernet debug
#SRC_URI_append_ls2 += " \
#    file://dpaa2debugfs.cfg \
#"

SRC_URI_append_t4bluebox += " \
    ${COMMONSRC_URI} \
    file://t4blueboxconfig \
    file://enablegfxhwt4.cfg \
"

# Note how our lxc.cfg comes *AFTER* containers.config to add to it
COMMONDELTA_KERNEL_DEFCONFIG = "enablepktgen.cfg iptables.cfg iso9660.cfg enableusbcan.cfg containers.config lxc.cfg"
DELTA_KERNEL_DEFCONFIG_append_ls2 = " ls2blueboxconfig dpaa2qdma.cfg dpaa2debugfs.cfg ${COMMONDELTA_KERNEL_DEFCONFIG}"
DELTA_KERNEL_DEFCONFIG_append_ls2084abbmini = " vnet_ls2.cfg"

#DELTA_KERNEL_DEFCONFIG_append_ls2 = " enablegfxhwls2.cfg"
DELTA_KERNEL_DEFCONFIG_append_t4bluebox = " t4blueboxconfig ${COMMONDELTA_KERNEL_DEFCONFIG}"
#DELTA_KERNEL_DEFCONFIG_append_t4bluebox = " enablegfxhwt4.cfg"
DELTA_KERNEL_DEFCONFIG_append_ls1043ardb = " ${COMMONDELTA_KERNEL_DEFCONFIG}"
DELTA_KERNEL_DEFCONFIG_append += "${@bb.utils.contains('DISTRO_FEATURES', 'docker', 'docker.cfg', '', d)}"