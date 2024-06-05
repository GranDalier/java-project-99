.PHONY: build, test

.DEFAULT_GOAL = app

setup:
	./gradlew wrapper --gradle-version 8.8
	./gradlew build

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
	./gradlew test

lint:
	./gradlew checkstyleMain checkstyleTest

reload-classes:
	./gradlew -t classes

check-deps:
	./gradlew dependencyUpdates -Drevision=release
