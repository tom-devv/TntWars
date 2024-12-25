#!/bin/bash

# Function to count lines in .java files
count_lines() {
  find "$1" -type f -name "*.java" | while read -r file; do
    awk '
      # Ignore empty lines
      NF > 0 {
        # Ignore single-line comments
        # if ($1 ~ /^\/\//) next;
        # Ignore multi-line comment blocks
        if ($0 ~ /^\/\*/) { in_comment = 1; next }
        if (in_comment && $0 ~ /\*\//) { in_comment = 0; next }
        if (in_comment) next;

        # Count the line
        total++;
      }
      END { print total }
    ' "$file"
  done | awk '{total += $1} END {print total}'
}

# Ensure a directory is provided
if [ -z "$1" ]; then
  echo "Usage: $0 <path_to_java_project>"
  exit 1
fi

project_path="$1"

if [ ! -d "$project_path" ]; then
  echo "Error: Directory '$project_path' does not exist."
  exit 1
fi

# Count lines of code
echo "Counting lines of Java code in: $project_path"
loc=$(count_lines "$project_path")
echo "Total lines of code (excluding comments and empty lines): $loc"

