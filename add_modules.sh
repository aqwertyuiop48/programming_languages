#!/bin/bash

# Format: "path|url|branch"
modules=(
"gate|https://github.com/aqwertyuiop48/gate.git|main"
"profiles|https://github.com/aqwertyuiop48/profiles.git|main"
"credentials_/credentials|https://github.com/aqwertyuiop48/credentials.git|main"
"javascript/vue_/vuejs_training|https://github.com/aqwertyuiop48/vuejs_training.git|main"
"javascript/vue_/vuejs_training_typescript|https://github.com/aqwertyuiop48/vuejs_training.git|typescript"
"Python/flask_/vercel_flask_app|https://github.com/aqwertyuiop48/vercel_flask_app.git|main"
# add all other entries here...
)

for entry in "${modules[@]}"; do
    IFS="|" read -r path url branch <<< "$entry"

    # Create parent directory if not exists
    mkdir -p "$(dirname "$path")"

    # Add submodule
    echo "Adding submodule: $path"
    git submodule add -b "$branch" "$url" "$path"
    echo
done
