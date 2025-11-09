# Gift-To-Go-Coding-Challenge

### Duplicate Request Handling (Not Implemented)

To prevent duplicate file processing, a request ID can be used:
- Each incoming request has a unique ID (header or generated per file)
- The system checks an in-memory cache or database to see if the ID has already been processed
- MDC (Mapped Diagnostic Context) can be used for tracing logs across services
- If a duplicate is detected, the request can be rejected or ignored

This feature was omitted in this implementation to focus on core file parsing, validation, mapping, and JSON generation.
