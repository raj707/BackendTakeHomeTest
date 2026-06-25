# Sleep API

## API Endpoints

Base URL:

```
http://localhost:8080
```

All APIs use UUID based user identifiers.

Example:

```
11111111-1111-1111-1111-111111111111
```

---

## 1. Create Sleep Log

Creates a sleep log for a user.

### Endpoint

```http
POST /users/{userId}/sleep-logs
```

### Headers

```http
Content-Type: application/json
```

### Request Body

```json
{
  "bedtime": "2026-06-23T22:30:00",
  "wakeupTime": "2026-06-24T06:30:00",
  "feeling": "GOOD"
}
```

Supported values:

```
GOOD
OK
BAD
```

### Response

HTTP `201 Created`

```json
{
  "id": "a9c1c3d8-8c2b-4c9e-bf21-123456789abc",
  "sleepDate": "2026-06-24",
  "bedtime": "2026-06-23T22:30:00",
  "wakeupTime": "2026-06-24T06:30:00",
  "totalMinutesInBed": 480,
  "feeling": "GOOD"
}
```

---

## 2. Fetch Last Night Sleep

Returns the latest sleep log for a user.

### Endpoint

```http
GET /users/{userId}/sleep-logs/latest
```

### Response

HTTP `200 OK`

```json
{
  "id": "a9c1c3d8-8c2b-4c9e-bf21-123456789abc",
  "sleepDate": "2026-06-24",
  "bedtime": "2026-06-23T22:30:00",
  "wakeupTime": "2026-06-24T06:30:00",
  "totalMinutesInBed": 480,
  "feeling": "GOOD"
}
```

If no sleep record exists:

```
HTTP 404 Not Found
```

---

## 3. Fetch 30-Day Sleep Summary

Returns sleep averages and feeling frequency for the last 30 days.

### Endpoint

```http
GET /users/{userId}/sleep-logs/summary
```

### Response

HTTP `200 OK`

```json
{
  "fromDate": "2026-05-26",
  "toDate": "2026-06-24",
  "averageTotalMinutesInBed": 470,
  "averageBedtime": "22:45:00",
  "averageWakeupTime": "06:30:00",
  "feelingFrequency": {
    "GOOD": 20,
    "OK": 8,
    "BAD": 2
  }
}
```

---

# Postman Collection

The Postman collection is available at:

```
postman/Sleep-API.postman_collection.json
```

## Import

1. Open Postman
2. Select **Import**
3. Choose:

```
postman/Sleep-API.postman_collection.json
```

---

## Test Execution Order

Run requests in this order:

### Create Sleep Logs

Creates multiple sleep records:

1. Create Sleep Log - GOOD
2. Create Sleep Log - OK
3. Create Sleep Log - BAD

Validates:

- Multiple records can be created
- UUID generation
- Feeling enum handling


### Fetch Latest Sleep

Validates:

- Latest sleep retrieval
- Response mapping


### Fetch Summary

Validates:

- 30-day date range
- Average total sleep time
- Average bedtime
- Average wakeup time
- Feeling frequency


### Negative Tests

Included scenarios:

- Unknown user
- Missing sleep data
- Invalid request payload
