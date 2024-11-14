#!/bin/bash
set -e

echo "Creating databases..."

# Function to create a database if it does not exist
create_database_if_not_exists() {
  local db_name=$1
  if ! psql --username "$POSTGRES_USER" --dbname=postgres -tAc "SELECT 1 FROM pg_database WHERE datname='$db_name'" | grep -q 1; then
    echo "Creating database $db_name..."
    psql --username "$POSTGRES_USER" --dbname=postgres <<-EOSQL
      CREATE DATABASE $db_name;
EOSQL
    echo "Database $db_name created."
  else
    echo "Database $db_name already exists."
  fi
}

# Create each database if it does not exist
create_database_if_not_exists "gym_crm_local"
create_database_if_not_exists "gym_crm_dev"
create_database_if_not_exists "gym_crm_stg"
create_database_if_not_exists "gym_crm_prod"

echo "Databases creation process complete!"
