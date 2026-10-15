# Fraud Alert Review Dashboard

A lightweight web dashboard for reviewing fraud alerts. Analysts can record alert events, search them, see summary indicators, drill down by status and spot high-risk or overdue alerts. The project is also a DevOps Lab case study: every commit is built, tested, packaged, containerised and deployed by an automated pipeline, which also provisions and updates an Ubuntu node with Ansible.

> Dummy data only. No real customer or bank data is used.

## Features

1. Alert entry form with validation (duplicate transaction IDs are rejected)
2. Searchable alert list (transaction ID, customer, status, risk level)
3. Dashboard with summary indicators (total, new, under review, escalated, closed)
4. Status drill-down from each indicator to its alerts
5. Exception view for high-risk and overdue alerts that are not closed

## Technology stack

| Area | Choice |
|------|--------|
| Language | Java 21 |
| Framework | Spring Boot 4.1 (Web, Thymeleaf, Data JPA, Validation, Actuator) |
| Build tool | Apache Maven 3.9 |
| Database | H2 (file mode) |
| Testing | JUnit 5, Selenium WebDriver (Microsoft Edge) |
| CI/CD | Git, GitHub, Jenkins (Jenkinsfile) |
| Containers | Docker, local registry on port 5000 |
| Deployment targets | Apache Tomcat 11, Docker container, Ubuntu (WSL 2) node configured with Ansible |

## Delivery pipeline

![Delivery pipeline](docs/pipeline.png)

## Quick start

```
git clone https://github.com/Sunil-Saini123/fraud-alert-dashboard.git
cd fraud-alert-dashboard
mvn clean package
mvn spring-boot:run
```

- Dashboard: http://localhost:8081
- Alert list: http://localhost:8081/alerts
- Health check: http://localhost:8081/actuator/health

Run the browser tests with `mvn test -Pselenium`.

## Ports

| Port | Used by |
|------|---------|
| 8080 | Jenkins |
| 8081 | Application (local run and Ansible-provisioned node) |
| 8082 | Docker container started by the pipeline |
| 8090 | Apache Tomcat |
| 5000 | Local Docker registry |

## Project structure

```
fraud-alert-dashboard/
  Jenkinsfile                 pipeline as code
  Dockerfile, .dockerignore   container image
  ansible/                    inventory, site.yml, deploy.yml, teardown.yml, templates
  docs/                       technical guide and diagrams
  src/main/java/...           controller, service, repository, model
  src/main/resources/         Thymeleaf templates, application.properties
  src/test/java/...           Selenium journey tests
  BRANCHING.md, .github/      branch policy, issue and pull request templates
```

## Documentation

See [docs/TECHNICAL_GUIDE.md](docs/TECHNICAL_GUIDE.md) for the architecture, build and release process, troubleshooting guide, limitations and future plan. Branch names and commit rules are in [BRANCHING.md](BRANCHING.md).

## Status

| Stage | Tooling | Status |
|-------|---------|--------|
| Repository and branching | Git, GitHub | Done |
| Continuous integration | Jenkins, Maven | Done |
| Quality gate | Selenium, JUnit | Done |
| Containerisation | Docker | Done |
| Provisioning | Ansible | Done |

## Author

Sunil Saini, DevOps Lab, B.E. Semester VII, Computer Engineering.
