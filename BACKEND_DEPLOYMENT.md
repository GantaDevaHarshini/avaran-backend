# Avaran Backend Deployment

Deploy this folder:

```text
C:\Users\devah\OneDrive\Desktop\Avaran\backend\backend
```

## Required Environment Variables

```text
PORT=8080
DB_URL=jdbc:mysql://<host>:<port>/<database>?useSSL=true&allowPublicKeyRetrieval=true
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
JPA_SHOW_SQL=false
```

Use the exact frontend deployment URL for `CORS_ALLOWED_ORIGINS`.

## Docker Deployment

The project includes a production Dockerfile.

```bash
docker build -t avaran-backend .
docker run -p 8080:8080 \
  -e DB_URL="jdbc:mysql://<host>:<port>/<database>?useSSL=true&allowPublicKeyRetrieval=true" \
  -e DB_USERNAME="<database-user>" \
  -e DB_PASSWORD="<database-password>" \
  -e CORS_ALLOWED_ORIGINS="https://your-frontend-domain.com" \
  avaran-backend
```

Health check:

```text
GET /health
```

Expected response:

```json
{"status":"ok"}
```
