# Cloud file storage
Multi-user file cloud. Users of the service can use it to download and store files. The inspiration for the project is Google Drive.

## Functionality
### Users:
- sign up
- log in
- log out

### Files and folders:
- uploading
- renaming
- downloading
- deleting

## Technology stack
- Spring Boot
- Spring Security
- Spring MVC + Thymeleaf + Bootstrap
- Spring Sessions + Redis
- Data persistence:
  - Spring Data JPA + PostgreSQL
  - MinIO for files
- Docker compose for deployment

## Local deployment guide
1. Clone repository

```shell
git clone https://github.com/RuslanGren/Cloud-File-Storage.git
```

2. `cd` to the root folder of the cloned repository 
3. Run Docker compose stack for local development (Docker have to be installed and running)

```shell
docker compose -f compose/docker-compose.yml up
```
