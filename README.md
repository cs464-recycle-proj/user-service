# User Service

## Description
User Service is a backend microservice that manages users, their interests, and event history. It also provides simple event recommendations based on user interests.

---

## Endpoints

| Endpoint | Method | Input | Result | Remarks |
|----------|--------|-------|--------|---------|
| `/api/users` | POST | JSON: {username, email, fullName, profileImg, bio, bday} | User object | Create new user |
| `/api/users` | GET | — | List of User objects | Get all users |
| `/api/users/{id}` | GET | Path variable: id | User object | Get user by ID |
| `/api/users/{id}` | PUT | Path variable: id, JSON User | User object | Update user info |
| `/api/users/{id}` | DELETE | Path variable: id | — | Delete user |
| `/api/users/{id}/interests` | GET | Path variable: id | List of UserInterest | Get user's interests |
| `/api/users/{id}/interests` | POST | Path variable: id, `interest` param | UserInterest | Add a new interest |
| `/api/users/{id}/events/history` | GET | Path variable: id | List of UserActionHistory | Get user's past event actions |
| `/api/users/{id}/events/history` | POST | Path variable: id, params: `eventId`, `action`, `rating` | UserActionHistory | Add a new event history entry |
| `/api/users/{id}/recommendations` | GET | Path variable: id, param: `allEventIds` (list of event IDs) | List<Long> | Returns recommended event IDs (placeholder logic) |

---

## DB Tables

- `users`  
- `user_interests`  
- `user_event_history`

---

## Notes

- Supabase PostgreSQL is used as the database.  
- Recommendation logic is currently simple; can be extended to filter events based on user interests and history.