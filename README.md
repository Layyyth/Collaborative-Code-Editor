# Collaborative Code Editor

The Collaborative Code Editor is a full-stack web application that enables real-time collaborative coding with multi-user support. Built with a modern tech stack, the platform offers code editing, execution, versioning, and role-based access control. It's ideal for remote coding interviews, team collaboration, and educational settings.

Key Features

-Real-Time Code Syncing: Powered by WebSockets (STOMP), users can collaboratively edit code in real time.

-Multi-User Roles:
  Admin: Can manage user roles, monitor users.
  User: Can edit, run, and save code.
  Guest: Can view only.
  
-Code Execution: Supports compiling and running C++ code using Docker containers.

-Version Control: Users can save, revert, and clear versions of code.

-Folder & File Management: Organize code with folders and files via a VS Code–like sidebar.

-Google OAuth2 Authentication: Seamless login with your Google account.

-Live Online Presence: Displays online users, editing status, and last seen.

-Fully Dockerized: Frontend, backend, and MySQL database run via Docker containers.

-CI/CD Pipeline: Automated build & deployment using GitHub Actions and DockerHub.

-------------------------------------------

🧱Tech Stack

-Frontend: React.js, Monaco Editor, STOMP WebSockets, Nginx (for production)

-Backend: Spring Boot, WebSocket, REST API, OAuth2, Docker

-Database: MySQL 8, JPA/Hibernate

-DevOps: Docker Compose, GitHub Actions, DockerHub

------------------------------------------

📁 Folder Structure

├── collaborativecodeeditor/    Spring Boot backend

├── FE/collab-editor-frontend/  React frontend

├── Dockerfile                  Backend Dockerfile

├── Docker-Compose.yml          Main orchestration file

└── .github/workflows/          CI/CD config


---------------------------------------

How to Run Locally:

1.Clone the repo:

git clone https://github.com/YourUsername/Collaborative-Code-Editor.git

cd Collaborative-Code-Editor

2. Start all services:
3. 
docker-compose up --build

--------------------------

GitHub Actions & DockerHub

On every push to the main branch:

 -Frontend and backend images are built using GitHub Actions.
 
 -Images are pushed to DockerHub automatically.
 
 -App can be deployed on any Docker-compatible server (e.g., AWS EC2, Render, Railway).


Full Document : 
