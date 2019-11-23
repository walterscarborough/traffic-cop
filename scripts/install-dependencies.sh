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

function install_jvm_dependencies() {
    ./mvnw clean install
}

function install_brew_dependencies() {
    brew install shellcheck
}

function display_success_message() {
    local -r green_color_code='\033[1;32m'
    local -r default_color_code='\033[00m'
    echo -e "${green_color_code}\\nDependencies installed successfully 🔌 ${default_color_code} \\n"
}

function main() {
    set_bash_error_handling
    go_to_project_root_directory
    install_jvm_dependencies
    install_brew_dependencies
    display_success_message
}

main
