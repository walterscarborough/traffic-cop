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

function run_docker_tests() {
    rm -rf /tmp/traffic-cop-docker-test
    mkdir -p /tmp/traffic-cop-docker-test/reports

    docker build -t traffic-cop-test .
    docker run --name traffic-cop-test -d -p 8080:8080 -v /tmp/traffic-cop-docker-test/reports:/traffic-cop/reports traffic-cop

    sleep 10

    pushd /tmp/traffic-cop-docker-test || exit 1

    curl 'http://localhost:8080/run-load-test' -X POST \
    -H 'Content-Type: application/json;charset=UTF-8' \
    -d '{"baseUrl":"http://localhost:52616","endpoint":"/chachkies","httpMethod":"PUT","payload":"{\"name\": \"my fancy chachkie\"}","constantUsersPerSecond":1,"constantUsersPerSecondDuration":1,"rampUsersPerSecondMinimum":1,"rampUsersPerSecondMaximum":1,"rampUsersPerSecondDuration":1}'

    sleep 10

    docker kill traffic-cop-test
    docker rm -f traffic-cop-test

    cd /tmp/traffic-cop-docker-test/reports/general*
    if [[ ! -f index.html ]]; then
      echo "Error: gatling report not found"
      exit 1
    fi

    popd || exit 1
}

function display_success_message() {
    local -r green_color_code='\033[1;32m'
    local -r default_color_code='\033[00m'
    echo -e "${green_color_code}\\nDocker tests ran successfully 🧪 ${default_color_code} \\n"
}

function main() {
    set_bash_error_handling
    go_to_project_root_directory
    run_docker_tests
    display_success_message
}

main
