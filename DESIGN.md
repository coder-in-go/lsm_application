# Physician Portal Application Design Document

## Overview

The Physician Portal is a Spring Boot and Angular-based web application for managing specimen pickup requests, vehicle assignments, workstations, and physician data for a hospital logistics workflow.

## Architecture

- **Backend:** Spring Boot (Java), RESTful API, JPA/Hibernate, PostgreSQL
- **Frontend:** Angular (TypeScript)
- **Database:** PostgreSQL

## Main Modules

### 1. Specimen Pickup Management

- Entity: `SpecimenPickupRequest`
- Features: Create, view, assign, and track specimen pickup requests
- Relationships: Linked to `Physician` and `Vehicle`

### 2. Vehicle Management

- Entity: `Vehicle`
- Features: Assign vehicles to specimen requests, view vehicle status
- Relationships: One-to-many with `SpecimenPickupRequest`

### 3. Physician Management

- Entity: `Physician`
- Features: Manage physician profiles, associate with workstations and pickup requests
- Relationships: Many-to-many with `Workstation`, one-to-many with `SpecimenPickupRequest`

### 4. Workstation Management

- Entity: `Workstation`
- Features: Add, view, and associate workstations with physicians
- Relationships: Many-to-many with `Physician`

## Key Classes

- `Physician`: Represents a doctor, linked to workstations and pickup requests
- `Workstation`: Represents a location, linked to physicians
- `Vehicle`: Represents a transport vehicle, assigned to pickup requests
- `SpecimenPickupRequest`: Represents a specimen pickup, linked to physician and vehicle

## REST API Endpoints

- `/api/physicians`: CRUD for physicians
- `/api/workstations`: CRUD for workstations
- `/api/vehicles`: CRUD for vehicles
- `/api/specimen-pickups`: CRUD for specimen pickup requests

## Data Model (ER Diagram)

- **Physician** <--> **Workstation** (Many-to-Many)
- **Physician** <--> **SpecimenPickupRequest** (One-to-Many)
- **Vehicle** <--> **SpecimenPickupRequest** (One-to-Many)

## Exception Handling

- Global exception handler using `@ControllerAdvice`
- Custom exceptions for not found entities

## Business Logic Highlights

- Assign vehicles to multiple specimen requests based on route and priority
- Geocoding and route optimization for pickups
- Real-time notifications for vehicle assignments

## Security

- (Optional) Spring Security for authentication and authorization

## Frontend Features

- Dashboard for physicians, vehicles, and workstations
- Forms for creating and managing entities
- Real-time updates for specimen pickups and vehicle assignments

## Future Enhancements

- Integrate maps for route visualization
- Reporting and analytics
