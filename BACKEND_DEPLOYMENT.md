# Avaran Backend Deployment

Deploy this folder:

```text
C:\Users\devah\OneDrive\Desktop\Avaran\backend\backend
```

## Required Environment Variables

```text
PORT=8080
DB_URL=jdbc:<database-url>
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
JPA_SHOW_SQL=false
```

Use the exact frontend deployment URL for `CORS_ALLOWED_ORIGINS`.

## Render PostgreSQL

If you are using Render PostgreSQL, copy the database values from the Render database page.

Use the internal database host when the backend and database are both on Render. The app also supports Render's raw `DATABASE_URL=postgres://...` value automatically.

```text
DB_URL=jdbc:postgresql://<internal-host>:5432/<database>
DB_USERNAME=<user>
DB_PASSWORD=<password>
```

If you manually set `DB_URL`, use the JDBC form:

```text
jdbc:postgresql://...
```

## External MySQL

If you are using an external MySQL provider, use:

```text
DB_URL=jdbc:mysql://<host>:<port>/<database>?useSSL=true&allowPublicKeyRetrieval=true
DB_USERNAME=<user>
DB_PASSWORD=<password>
```

Do not use `localhost` on Render. `localhost` means the Render container itself, not your laptop database.

## Docker Deployment

The project includes a production Dockerfile.

```bash
docker build -t avaran-backend .
docker run -p 8080:8080 \
  -e DB_URL="jdbc:postgresql://<host>:5432/<database>" \
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
