# Fraud Alert Review Dashboard

A lightweight web dashboard for reviewing fraud alerts. Analysts can record alert events, search them, see summary indicators, drill down by status and spot high-risk or overdue alerts. The project is also a DevOps Lab case study: it is built, tested, containerised and deployed through an automated pipeline (Git, GitHub, Jenkins, Selenium, Docker, Puppet/Ansible).

> Dummy data only. No real customer or bank data is used.

## MVP features

1. Data / event entry form for new alerts
2. Searchable dashboard of all alerts
3. Summary indicators (total, new, under review, escalated, closed)
4. Status drill-down from an indicator to its alerts
5. Alert / exception view for high-risk and overdue alerts

## Technology stack

| Area | Choice |
|------|--------|
| Language | Java 21 |
| Framework | Spring Boot 4.1 (Web, Thymeleaf, Data JPA, Validation, Actuator) |
| Build tool | Apache Maven 3.9 |
| Database | H2 (file mode) |
| Deployment target | Apache Tomcat 11 (WAR) and Docker |
| Testing | JUnit 5, Selenium WebDriver |
| CI/CD and config | Jenkins, Docker, Puppet or Ansible |

## Prerequisites

- JDK 21
- Apache Maven 3.9 or newer
- Git

## Getting started

```
git clone https://github.com/Sunil-Saini123/fraud-alert-dashboard.git
cd fraud-alert-dashboard
mvn clean package
mvn spring-boot:run
```

The application starts on port 8081 (set in `src/main/resources/application.properties`).

- Dashboard: http://localhost:8081
- Health check: http://localhost:8081/actuator/health

## Project structure

```
fraud-alert-dashboard/
  pom.xml
  README.md
  BRANCHING.md
  .github/                      issue and pull request templates
  docs/                         task documents and diagrams
  src/main/java/com/example/fraudalert/
      FraudAlertDashboardApplication.java
      ServletInitializer.java
      controller/  service/  repository/  model/
  src/main/resources/
      templates/  static/  application.properties
  src/test/java/com/example/fraudalert/
```

## Workflow

Work is planned as GitHub issues and delivered through short-lived feature branches and pull requests. See [BRANCHING.md](BRANCHING.md) for branch names, commit message rules and merge policy.

## DevOps roadmap

| Stage | Tooling | Status |
|-------|---------|--------|
| Repository and branching | Git, GitHub | In progress |
| Continuous integration | Jenkins, Maven | Planned |
| Quality gate | Selenium, JUnit | Planned |
| Containerisation | Docker | Planned |
| Provisioning | Puppet or Ansible | Planned |

## Author

Sunil Saini, DevOps Lab, B.E. Semester VII, Computer Engineering.
