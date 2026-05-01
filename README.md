# Triathlon Competition Management - Cross-Platform (gRPC)

##  Project Overview
This project demonstrates a **cross-platform distributed architecture** for managing triathlon event results. To ensure seamless communication between different programming languages, the system utilizes **gRPC** (Google Remote Procedure Call), allowing a high-performance exchange of structured data.

The architecture consists of:
*   **Server**: Developed in **Java**, handling business logic and data persistence.
*   **Client**: Developed in **C# (WPF)**, providing a responsive interface for referees.

##  Key Features & Distributed Logic

###  gRPC Integration 
*   **Cross-Language Interoperability**: The system uses gRPC to bridge the Java server and C# client, enabling them to share the same service definition via `.proto` files.
*   **Protocol Buffers (Protobuf)**: Data is serialized using Protobuf, ensuring smaller payload sizes and faster communication compared to text-based protocols like JSON or XML.
*   **Strict Typing**: Service contracts are strictly defined, reducing errors in data exchange between the Java and .NET environments.

###  Security & Authentication
*   **Encrypted Credentials**: Secure login for referees. Passwords are encrypted before storage to protect sensitive information across the network.
*   **Authorized Access**: Only authenticated referees can access their assigned trials and modify scores.

###  Real-Time Synchronization
*   **Automated Updates**: The system ensures that all clients are notified of score changes in real-time. This is achieved through gRPC's streaming capabilities or a complementary notification mechanism.
*   **Consistency**: Data integrity is maintained across the distributed system, ensuring all referees view the same live rankings.

##  Technical Architecture
The solution is organized into a modular structure to maintain a clean separation of concerns:

### 1. Java Server (The Engine)
*   **Persistence**: Uses a hybrid approach with **Hibernate 6** for core entities and **JDBC** for event data.
*   **gRPC Server**: Hosted in Java, processing requests from the C# client according to the defined service contracts.

### 2. C# Client (The Interface)
*   **WPF UI**: A modern desktop interface following the **MVC/MVVM** pattern.
*   **gRPC Proxy**: The client communicates with the Java server through a generated gRPC stub, abstracting the complexity of network calls.

##  Repository Structure
*   **`Protos/`**: The `.proto` files defining the gRPC services and messages shared by both platforms.
*   **`JavaServer/`**: The server-side implementation (Hibernate, gRPC Service, Logging).
*   **`CSharpClient/`**: The desktop application (WPF, gRPC Stub, UI Logic).

## 💻 Execution Instructions
1.  **Generate Code**: Ensure gRPC tools generate the necessary classes from the `.proto` files for both Java and C#.
2.  **Start Java Server**: Run the server to begin listening for gRPC calls on the configured port.
3.  **Start C# Client**: Run the WPF application and log in as a referee to manage competition trials.

---
*Developed for the "Distributed Cross-Platform Applications" Assignment - 2026*
