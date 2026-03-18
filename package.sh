#!/bin/bash
set -e

clear

./gradlew clean

./gradlew installDist

./gradlew jpackageTask