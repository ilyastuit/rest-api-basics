up: docker-up
down: docker-down
restart-app: build-app restart-tomcat
init: build-app docker-build migrate

build-app:
	./gradlew clean build

docker-build:
	docker-compose build

migrate:
	./gradlew flywayClean flywayMigrate

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down --remove-orphans

restart-tomcat:
	docker-compose restart tomcat