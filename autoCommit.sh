#!/bin/zsh

# Check if we're inside a Git repository
if ! git rev-parse --is-inside-work-tree > /dev/null 2>&1; then
  echo "Error: This script must be run from within a Git repository."
  exit 1
fi

# Get the current timestamp in a suitable format
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

# Stage all changes
git add .

# Check if there are any changes to commit
if ! git diff-index --quiet HEAD --; then
  # Commit with the timestamp as the message
  git commit -m "Automatic commit at $timestamp"

  # Push to the current branch (assumes remote is named 'origin')
  git push origin HEAD
else
  echo "No changes to commit."
fi