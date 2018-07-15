Config file context.

# For file location, use / instead of \ (e.g, C:/NuSMV/2.4.3/bin/NuSMV.exe)

# Tree View One
TreeViewable=yes

# if yes, dummy elements can be used to avoid errors during NuSMV verification
# if no, show users warning messages if less than two elements are used in an attribute
DummyEnabledForAttributesOn=yes
DummyEnabledForAttributesOn2=no

# NuSMV location
NUSMV="NuSMV/2.4.3/bin/NuSMV.exe"

# Fireeye location (This file is a jar file)
Fireeye="res/acts_cmd_beta_v2_r1.0.jar"
# Fireeye="res/fireeye_1.0b6_cmd.jar"

# XACML Template location (This file is a xml file).
XACMLTemplates="res/xacml.templates"

# NuSMV Template location
NUSMVTemplates="res/smv.templates"

# Process State number
ProcessStateNumber=10

# MultiLevel Security Policy Model State number
MultiLevelStateNumber=10
