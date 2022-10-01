Return to the <a href="https://project-herophilus.github.io/Project-Herophilus-Assets/" target="_blank">Main Page</a>

# HL7 Data Simulator

The HL7 Data Simulator is specifically designed to enable testing of vendors HL7 capabilities. The HL7 Data Simulator is
designed as an HL7 client that can process data from files, kafka topics or API endpoints. Beyond just using it for 
general purpose simple testing it can also be configured for performance testing against HL7 servers.

Systems that process HL7 data typically leverage an HL7 MLLP Servers, this enables it to process HL7 v2.x 
messages. We eill explain this from the context of [iDaaS-Connect-HL7](https://github.com/Project-Herophilus/iDaaS-Connect/tree/main/iDaaS-Connect-HL7). 
When iDAAS-Connect-HL7 starts it creates a series of connections to process data. Specifically for HL7 near real time transactions it has several MLLP Server ports defined, Once the platform starts all these HL7 MLLP Server ports are
created and ready to process data. When these actiond occur the HL7 Data Simulator(s) can connect to the specific HL7 MLLP specific port for the message type.

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

# Benefits

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



