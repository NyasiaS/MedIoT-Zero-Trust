# MedIoT Zero-Trust (Gateway + Control Plane)

This project is a small Zero-Trust IoT prototype. Devices do **not** talk directly to the Control Plane. They send actions to a **Gateway**, which enforces policy and forwards normalized events to the **Control Plane**. The Control Plane stores telemetry and raises alerts when behavior looks suspicious.

## What’s included
- **Gateway (8081)**: edge API that receives device actions and forwards outcomes
- **Control Plane (8080)**: stores devices/policies/events and generates alerts
- **Postgres (Docker)**: database
- **Flyway migrations**: reproducible schema
- **Alert deduplication**: repeated suspicious behavior increments a counter instead of creating endless alert rows

## Architecture

Device -> Gateway -> Control Plane -> Postgres


## How to run (recommended)
From the repo root (where `docker-compose.yml` is):

```bash
docker compose up --build

Stop and remove containers + DB volume:

docker compose down -v
URLs / Ports

Control Plane: http://localhost:8080

Gateway: http://localhost:8081

Database & Flyway

Flyway migrations are located here:

Control Plane/src/main/resources/db/migration/

Migrations included:

V1__init.sql (tables + indexes)

V2__device_keys.sql (persistent device key fields)

V3__jwt_audit.sql (JWT token audit table)

V4__alert_dedupe.sql (alert status + dedupe counter fields)

Main endpoints
Control Plane

PUT /policies/{role} – create/update policy for a role

GET /policies/{role} – view policy

POST /events/ingest – ingest an event (usually called by Gateway)

GET /alerts – view alerts (dedupe enabled)

Gateway

POST /gw/events – submit a device action (policy is enforced at the gateway)

Demo steps (copy/paste)
1) Start the stack
docker compose up --build
2) Create a policy (allow ping for role sensor)
curl -X PUT "http://localhost:8080/policies/sensor" \
  -H "Content-Type: application/json" \
  -d '{"policyJson":"{\"allow\":[\"ping\"]}"}'

Verify:

curl "http://localhost:8080/policies/sensor"
3) Send an allowed action through the Gateway
curl -i -X POST "http://localhost:8081/gw/events" \
  -H "Content-Type: application/json" \
  -d '{"action":"ping","payload":"hi"}'
4) Trigger an alert (blocked behavior)

Send an action not in the allow list (probe) multiple times:

for i in {1..5}; do
  curl -s -X POST "http://localhost:8081/gw/events" \
    -H "Content-Type: application/json" \
    -d '{"action":"probe","payload":"x"}' > /dev/null
done
echo sent
5) Check alerts (dedupe expected)
curl "http://localhost:8080/alerts"

Expected:

You should see a PROBING alert for the device

Repeating the same suspicious behavior should increase count instead of creating unlimited new rows (dedupe behavior)

Troubleshooting
“Connection refused” on 8080/8081

Check containers:

docker compose ps

Check logs:

docker compose logs -f --tail=200 control-plane
docker compose logs -f --tail=200 gateway
Flyway migrations not applying

Confirm migrations are here:

Control Plane/src/main/resources/db/migration/
and names match:

V1__something.sql (double underscore)