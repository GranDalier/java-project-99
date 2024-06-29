.PHONY: build test

.DEFAULT_GOAL := app

setup:
	gradle wrapper --gradle-version 8.7
	gradle build

app:
	./gradlew bootRun --args='--spring.profiles.active=dev'

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew installDist

dev: app

test:
	./gradlew test checkstyleMain checkstyleTest

reload-classes:
	./gradlew -t classes

check-deps:
	./gradlew dependencyUpdates -Drevision=release
