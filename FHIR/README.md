Return to the <a href="https://project-herophilus.github.io/Project-Herophilus-Assets/" target="_blank">Main Page</a>

# FHIR Data Simulator
This component is intended to act as an FHIR client to enable connecting to FHIR processing component/router or a FHIR Server. 
messages. While we eill explain this from the context of [iDaaS-Connect-FHIR](https://github.com/Project-Herophilus/iDaaS-Connect/tree/main/iDaaS-Connect-FHIR).
When iDAAS-Connect-FHIR starts it creates a series of endpoints. There is one specifically <your hostname>:<defined port>/idaas/fhirendpoint. 
Once these FHIR endpoint is created then the FHIR Data Simulator(s) can connect and send data in and based on the settings you have defined 
the rest of the platforms actions will be determined. 

*When setting up any FHIR resource for processing requires a header attribute named resourcename, this determines all the routing
and platform assets creation*

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

# Use Cases/Scenarios
The following are the specific use cases/scenarios that can be followwd to help implement
the FHIR Data Simulator.

| Use Case                                                                                                                                                |
|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| [FHIR General Testing](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/Platform-Content/ImplementationGuides/DataSim-FHIR.md) |

