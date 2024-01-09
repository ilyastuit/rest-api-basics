up: docker-up
down: docker-down
restart-app: build-app restart-tomcat
init: build-app docker-build docker-up migrate

build-app:
	./gradlew clean build -x test --stacktrace

build-app-test:
	./gradlew clean build --stacktrace

test:
	./gradlew test --stacktrace

docker-build:
	docker-compose build

migrate:
	./gradlew flywayClean flywayMigrate --stacktrace

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down --remove-orphans

restart-tomcat:
	docker-compose restart tomcat

