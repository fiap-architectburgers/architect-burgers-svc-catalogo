#!/bin/sh

cd /app

java -cp $(ls | tr '\n' ':') com.example.fiap.archburgers.ArchitectBurgersApiApplication