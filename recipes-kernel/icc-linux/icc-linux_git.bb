SUMMARY = "ICC Linux for s32v234 - A5x (Linux) to A5x (Linux) over PCIe"

require icc-linux.inc
SRCREV = "5abc7e8e87532bb1603ee84511b299b104489fde"

EXTRA_OEMAKE_ls2080abluebox = "CONFIG=RC"
EXTRA_OEMAKE_ls2084abluebox = "CONFIG=RC"
EXTRA_OEMAKE_ls2084abbmini = "CONFIG=RC"
EXTRA_OEMAKE_s32v234pcie = "CONFIG=EP"
EXTRA_OEMAKE_s32v234bbmini = "CONFIG=EP"
EXTRA_OEMAKE_s32v234evb = "CONFIG=RC"
EXTRA_OEMAKE_s32v234evb28899 = "CONFIG=RC"

COMPATIBLE_MACHINE = "ls2080abluebox|ls2084abluebox|ls2084abbmini|s32v234pcie|s32v234bbmini|s32v234evb.*"
