# Backend Job Hunter System
A recruitment management system that allows managing companies, users, jobs, applications (resumes), permissions, and more.

## Project Description
**Backend Job Hunter System** is built on **Java Spring Boot**, providing RESTful APIs for the Job Hunter application – a platform for managing recruitment information, supporting job seekers and employers. It adopts a Stateless architecture (separating Frontend and Backend), enabling easier scaling and integration with various technologies.

Key project objectives:
- Manage Company, Job, and User information.
- Support Resume CRUD (application process) and approval workflow for employers.
- Provide an RBAC (Role-Based Access Control) module to manage user privileges within the system.
- Integrate security using Spring Security and JSON Web Token (JWT).
- Support file upload (company logos, CV files) and download if needed.
- Implement CORS to allow cross-domain API access.

## Main Project Modules
The project is divided into multiple modules or features for easier management and maintenance:

1. Authentication & Authorization (Auth)
    - Login, logout, and token refresh functionality.
    - Manages user authentication with JWT.
    - Built on top of Spring Security and Bearer Token approach.

2. User Module
    - CRUD operations for user data: create, update, delete, and retrieve.
    - Fields: name, email, hashed password, gender, address, age, etc.
    - Relationship with the Role table (one Role per User).

3. Company Module
    - CRUD operations for company data: name, address, logo, description, etc.
    - Allows viewing/updating/deleting companies, related to the User entities belonging to them.
    - Automatically updates createdAt, updatedAt, createdBy, and updatedBy.

4. Job Module
    - Manages job postings.
    - Relationship with Company (1-N) and multiple Skills (N-N).
    - Fields: job title, salary, quantity needed, level (Intern, Fresher, Middle, Senior), description, etc.
    - Supports filtering (search criteria) and pagination.

5. Resume Module
    - Manages the application process from job seekers (resumes).
    - Fields: applicant email, CV URL, status (Pending, Reviewing, Approved, Rejected), etc.
    - Relationships: (N-1) with User and (N-1) with Job.

6. Permission & Role Module
    - Manages Permission entities (e.g. CREATE_USER, UPDATE_USER, etc.).
    - Manages Role entities (collections of permissions). For example, “Admin” role might include many permissions.
    - Each User has exactly one Role, which in turn determines user privileges.

7. Subscriber Module (Email)
    - Manages subscribers (people who sign up for job alerts via email).
    - Can be integrated with scheduled email sending (cron jobs).

8. File Upload/Download Module
    - Allows uploading files (company logos, user avatars, PDF CVs, etc.) to a configured folder on the server.
    - Checks MIME type and file size limits.
    - Provides a download API if required.

9. Common Utilities
    - Global exception handling using @ControllerAdvice.
    - CORS configuration to allow cross-domain requests from the frontend.
    - Response formatting (ResponseBodyAdvice) for consistent JSON output (statusCode, message, data, error, etc.).
    - Gradle (Kotlin DSL) build management, Lombok setup, and others.

## Project Prerequisites
Before running the project, ensure the following installations:

- Java (version 17+).

- MySQL (version 8.x, with MySQL Workbench).

- Git (to clone source code, manage versions).

- IDE (Visual Studio Code, IntelliJ, Eclipse, or STS).

- Node.js (version 16.20.0 - if you want to run and test the frontend directly).

## Frontend Repository

How to set up and run:

- Clone the frontend repo (git clone ...)

- npm install to install the needed packages

- Update .env.development to point to your backend API URL

- npm run dev (or npm run build + npm run preview for production)

## Personal Contact

**LinkedIn**: https://www.linkedin.com/in/tantruongg23/