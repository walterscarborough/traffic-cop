#!/usr/bin/env bash

declare WIREMOCK_PID="NOT SET"
readonly WIREMOCK_PORT=9090

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

function download_wiremock_if_not_available() {
    if [[ -f "integration-tests/wiremock.jar" ]]; then
        echo "Using cached wiremock jar"
    else
        local -r wiremock_version="2.25.1"
        curl -o "integration-tests/wiremock.jar" "http://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/${wiremock_version}/wiremock-standalone-${wiremock_version}.jar"
    fi
}

function start_wiremock_and_record_process_id() {
    killall wiremock
    java -jar integration-tests/wiremock.jar --root-dir integration-tests --port ${WIREMOCK_PORT} &
    WIREMOCK_PID=$!
}

function build_and_start_test_subject() {
    ./mvnw clean install spring-boot:run -Dspring-boot.run.profiles=dev &
    sleep 30
}

function reset_wiremock_request_counter() {
    curl -X DELETE "http://localhost:${WIREMOCK_PORT}/__admin/requests"
}

function test_post_command() {
    curl -X POST http://localhost:8080/run-load-test -d '{"payload": "{\"name\": \"my fancy chachkie\"}", "baseUrl": "http://localhost:9090", "endpoint": "/chachkies"}' -H "Content-Type: application/json"

    sleep 30

    curl -X POST http://localhost:8080/actuator/shutdown

    local -r post_command_output_success="$(curl http://localhost:${WIREMOCK_PORT}/__admin/requests | grep -c1 '"absoluteUrl" : "http://localhost:9090/chachkies"')"

    if [[ "${post_command_output_success}" != "15" ]]; then
        echo "Test failed"
        shutdown_wiremock

        exit 1;
    fi
}

function display_success_message() {
    local -r green_color_code='\033[1;32m'
    local -r default_color_code='\033[00m'
    echo -e "${green_color_code}\\nTests ran successfully 🧪 ${default_color_code} \\n"
}

function shutdown_wiremock() {
    kill ${WIREMOCK_PID}
}

function main() {
    set_bash_error_handling
    go_to_project_root_directory
    download_wiremock_if_not_available
    start_wiremock_and_record_process_id
    build_and_start_test_subject
    reset_wiremock_request_counter
    test_post_command
    shutdown_wiremock
    display_success_message
}

main "$@"
