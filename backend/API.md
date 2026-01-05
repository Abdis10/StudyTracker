# StudyTracker API

Base URL (local):
http://localhost:7000

This document is the authoritative API contract and is **fully derived from the current Javalin controllers**.

---

## Authentication

### POST /auth/signup
Register a new user.

#### Headers
Content-Type: application/json

#### Request Body
```json
{
  "firstname": "Ola",
  "lastname": "Nordmann",
  "username": "olan",
  "email": "ola@example.com",
  "password": "StrongPassword123",
  "gender": "MALE"
}
```

#### Responses

**200 OK**
User registered successfully.

**409 Conflict – Username exists**
```json
{
  "error": "Username already exists",
  "username": "olan"
}
```

**409 Conflict – Email exists**
```json
{
  "error": "Email already exists",
  "email": "ola@example.com"
}
```

**400 Bad Request – Invalid email or password**
```json
{
  "error": "Invalid email format"
}
```

**500 Internal Server Error**
```json
"An unexpected error occurred"
```

---

### POST /auth/login
Authenticate user and create a session token.

#### Headers
Content-Type: application/json

#### Request Body
```json
{
  "email": "ola@example.com",
  "password": "StrongPassword123"
}
```

#### Responses

**200 OK**
```json
{
  "messsage": "User is authenticated",
  "authenticated user": "ola@example.com",
  "token": "SESSION_TOKEN_ID"
}
```

**401 Unauthorized**
```json
{
  "message": "User is unauthorized",
  "unauthorized user": "ola@example.com"
}
```

**404 Not Found**
```json
{
  "message": "User not found",
  "errorcode": "USER_NOT_FOUND"
}
```

---

## Authorization

All `/session/**` endpoints require:

```
Authorization: Bearer <SESSION_TOKEN_ID>
```

---

## Study Sessions

### POST /session/session-registration
Create a new study session.

#### Headers
Content-Type: application/json

#### Request Body
```json
{
  "date": "2026-01-04",
  "hours": 2.5,
  "productivityScore": 4,
  "comment": "Good focus"
}
```

#### Responses

**201 Created**
```json
{
  "message: ": "study session is successfully created"
}
```

**401 Unauthorized**
```json
{
  "message: ": "Invalid token",
  "errorcode": "UNAUTHORIZED_TOKEN"
}
```

---

### GET /session/sessions
Retrieve all study sessions for the authenticated user.

#### Headers
Authorization: Bearer <SESSION_TOKEN_ID>

#### Responses

**200 OK**
```json
[
  {
    "id": 1,
    "date": "2026-01-04",
    "hours": 2.5,
    "productivityScore": 4,
    "comment": "Good focus"
  }
]
```

**401 Unauthorized**
```json
{
  "message": "UNAUTHORIZED_TOKEN"
}
```

---

### PUT /session/{sessionsId}
Update an existing study session.

#### Path Parameters
- sessionsId — ID of the study session

#### Headers
Authorization: Bearer <SESSION_TOKEN_ID>
Content-Type: application/json

#### Request Body
```json
{
  "date": "2026-01-05",
  "hours": 3,
  "productivityScore": 5,
  "comment": "Very productive"
}
```

#### Responses

**200 OK**
```json
{
  "Message: ": "Session successfully updated"
}
```

**401 Unauthorized – Session ownership**
```json
{
  "Message: ": "SESSION_DOES_NOT_BELONG_TO_USER"
}
```

**403 Forbidden – Invalid token**
```json
{
  "Message: ": "INVALID_TOKEN"
}
```

**404 Not Found**
```json
{
  "Message: ": "SESSION_NOT_FOUND"
}
```

---

### DELETE /session/{sessionId}
Delete a study session.

#### Path Parameters
- sessionId — ID of the study session

#### Headers
Authorization: Bearer <SESSION_TOKEN_ID>

#### Responses

**204 No Content**
```json
{
  "Message:": "Session is successfully deteted",
  "Deleted: ": true
}
```

**401 Unauthorized**
```json
{
  "Message: ": "INVALID_TOKEN"
}
```

**403 Forbidden**
```json
{
  "Message: ": "SESSION_DOES_NOT_BELONG_TO_USER"
}
```

**404 Not Found**
```json
{
  "Message: ": "SESSION_NOT_FOUND"
}
```

---

## Notes

- Token-based authentication using DB-backed session tokens
- Service layer is HTTP-agnostic
- Controllers are responsible for status codes and responses
- DTO-based request and response handling
