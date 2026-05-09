# Monthly Expo Logging Guide

This project uses Lombok `@Slf4j`, SLF4J, and Spring Boot's default Logback support.

## Where logs are added

- Controllers: incoming API actions and successful completion
- Services: business decisions like OTP send/verify, registration, login, payments
- Security: JWT validation and security configuration
- Exception handler: validation and runtime exceptions
- Reports: monthly/dashboard report generation

## Important safety rules

- Do not log passwords.
- Do not log real OTP values in production.
- Do not log JWT tokens.
- Avoid logging full request bodies that may contain sensitive data.

## Log locations

Console logs are shown in the terminal.
File logs are written to:

```text
backend/logs/monthly-expo.log
```

## Useful settings

In `application.properties`:

```properties
logging.level.root=INFO
logging.level.com.monthlyexpo=DEBUG
logging.file.name=logs/monthly-expo.log
```

For production, change:

```properties
logging.level.com.monthlyexpo=INFO
```
