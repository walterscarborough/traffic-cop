#!/usr/bin/env bash

function set_bash_error_handling() {
    set -o errexit
    set -o errtrace
    set -o nounset
    set -o pipefail
}

function go_to_project_root_directory() {
    local -r script_dir=$(dirname "${BASH_SOURCE[0]}")

    cd "$script_dir/.."
}

function run_end_to_end_tests() {
    rm -rf /tmp/traffic-cop-end-to-end-test
    mkdir -p /tmp/traffic-cop-end-to-end-test

    ./gradlew clean build -x test
    cp application/build/libs/traffic-cop.jar /tmp/traffic-cop-end-to-end-test

    pushd /tmp/traffic-cop-end-to-end-test || exit 1
    jar xf traffic-cop.jar
    rm traffic-cop.jar

    java -Dreports.dir=/tmp/traffic-cop-end-to-end-test/reports -cp . org.springframework.boot.loader.JarLauncher &
    local -r app_pid=$!
    sleep 10

    curl 'http://localhost:8080/run-load-test' -X POST \
    -H 'Content-Type: application/json;charset=UTF-8' \
    -d '{"baseUrl":"http://localhost:52616","endpoint":"/chachkies","httpMethod":"PUT","payload":"{\"name\": \"my fancy chachkie\"}","constantUsersPerSecond":1,"constantUsersPerSecondDuration":1,"rampUsersPerSecondMinimum":1,"rampUsersPerSecondMaximum":1,"rampUsersPerSecondDuration":1}'

    sleep 10

    kill $app_pid

    cd /tmp/traffic-cop-end-to-end-test/reports/general*
    if [[ ! -f index.html ]]; then
      echo "Error: gatling report not found"
      exit 1
    fi

    popd || exit 1
}

function display_success_message() {
    local -r green_color_code='\033[1;32m'
    local -r default_color_code='\033[00m'
    echo -e "${green_color_code}\\nEnd to end tests ran successfully 🧪 ${default_color_code} \\n"
}

function main() {
    set_bash_error_handling
    go_to_project_root_directory
    run_end_to_end_tests
    display_success_message
}

main
