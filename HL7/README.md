Return to the <a href="https://project-herophilus.github.io/Project-Herophilus-Assets/" target="_blank">Main Page</a>

# HL7 Data Simulator
This component is intended to act as an HL7 client to enable connecting to HL7 MLLP Servers and process HL7 v2.x 
messages. While we eill explain this from the context of [iDaaS-Connect-HL7](https://github.com/Project-Herophilus/iDaaS-Connect/tree/main/iDaaS-Connect-HL7). 
When iDAAS-Connect-HL7 starts it creates a series of HL7 MLLP Server ports, a specific set of directories, and some servlet endpoints.  All of these
endpoints are in place to enable processing of HL7 v2x messages, and CCDA documents. Once these HL7 MLLP Server ports are
created then the HL7 Data Simulator(s) can connect to the specific HL7 MLLP specific port for the message type.

![Data Flow](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/images/iDaaS-Platform/DataFlow-HL7.png)

# Platform General Pre-Requisites
For all iDaaS design patterns it should be assumed that you will either install as part of this effort or need the
[following](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/PreRequisites.md).

## Cloning, Building and Running Solutions
Here is a consistent manner in the way you can clone, build and run this
[component](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/CloningBuildingRunningSolution.md).

# Detailed Implementation Guide
Please feel free to check out our [implementation guides](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/Platform-Content/ImplementationGuides/intro.md)
and provide us any feedback.

Benefits

- General way for analysts, developers and various other project resources to unit test HL7 v2 transactions
against systems.
- Very effective way to generally test one or more specific HL7 specific transactions.
- Simply mechanism to enable performance testing against a measure volume of HL7 transactions.

# Use Cases/Scenarios
The following are the specific use cases/scenarios that can be followwd to help implement 
the HL7 Data Simulator.

| Use Case                |
|-------------------------|
| [HL7 General Testing](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/Platform-Content/ImplementationGuides/DataSim-HL7.md) |



