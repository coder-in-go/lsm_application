# Physician Portal Application - Architecture Diagram

![Physician Portal Architecture](https://raw.githubusercontent.com/github-copilot-assets/diagrams/main/physician-portal-architecture.png)

---

## ER Diagram (Entities & Relationships)

![Physician Portal ER Diagram](https://raw.githubusercontent.com/github-copilot-assets/diagrams/main/physician-portal-er.png)

---

## Flow Diagram (Specimen Pickup Assignment)

![Specimen Pickup Assignment Flow](https://raw.githubusercontent.com/github-copilot-assets/diagrams/main/specimen-pickup-assignment-flow.png)

---

## ER Diagram (Mermaid)

```mermaid
erDiagram
    PHYSICIAN ||--o{ SPECIMEN_PICKUP_REQUEST : creates
    PHYSICIAN ||--o{ PHYSICIAN_WORKSTATION : assigned_to
    PHYSICIAN_WORKSTATION }o--|| WORKSTATION : contains
    VEHICLE ||--o{ SPECIMEN_PICKUP_REQUEST : assigned_to
    SPECIMEN_PICKUP_REQUEST }o--|| WORKSTATION : at_location

    PHYSICIAN {
        UUID id
        String name
        ...
    }
    VEHICLE {
        UUID id
        String registrationNumber
        ...
    }
    WORKSTATION {
        UUID id
        String address
        String landmark
        ...
    }
    SPECIMEN_PICKUP_REQUEST {
        UUID id
        LocalDateTime createdAt
        ...
    }
    PHYSICIAN_WORKSTATION {
        physician_id
        workstation_id
    }
```

---

## Flow Diagram (Mermaid)

```mermaid
flowchart TD
    A[Create Specimen Pickup Request] --> B[Assign Physician]
    B --> C[Find Available Vehicle]
    C --> D[Optimize Route]
    D --> E[Assign Vehicle to Request]
    E --> F[Send Notification]
    F --> G[Pickup Complete]
```

---

## Application Architecture (Mermaid)

```mermaid
flowchart LR
    subgraph Backend
        A1[Spring Boot REST API]
        A2[PostgreSQL]
    end
    subgraph Frontend
        B1[Angular App]
    end
    B1 <-->|HTTP/JSON| A1
    A1 <-->|JPA| A2
```

---

> For custom diagrams, you can use tools like draw.io, Lucidchart, or PlantUML. If you want a PlantUML diagram, let me know!
