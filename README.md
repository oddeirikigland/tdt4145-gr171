---
author: Group 171
title: Documentation
output: pdf_document
---

[![Build Status](https://travis-ci.com/vegarab/tdt4145-gr171.svg?token=DJHitvEMvK9mbYbzJ3H2&branch=develop)](https://travis-ci.com/vegarab/tdt4145-gr171)

\tableofcontents

## Packages
### controllers
* The controllers package contains database-controllers for all the entities in
  the SQL database
* Each controller implements a common interface `DatabaseCRUD` to ensure
  create, retrieve, update and delete-operations
* Some controllers also have methods for special queries
* All queries are done trough a controller

### core
The core package represents the persistence-layer in our application.

#### Exercise
An abstract class that is extended by `FreeExercise` and `MachineExercise`.
This class contains the `exercise_id`, the `name` and holds `exercise_groups`
in a `Collection`.

#### FreeExercise
Represents the `free_exericse` entity, containing a `description` attribute -
and extends `Exercise`

#### MachineExercise
Represents the `machine_exercise` entity, containing `kilo`, `set` attributes,
and a representation of the relation between `machine_exercise` and `machine`.

#### Machine
Represents the `machine` entity, containing `machine_id`, `name` and
`description`

#### ExerciseGroup
Represetns the `exercise_group` entity, containing `exercise_group_id`, `name`
and a `Collection` representation of the relation between `exercise_group` and
`exercise`.

#### Workout
Represetns the `workout` entity, containing `workout_id`, `timestamp`,
`duration`, `form`, `performance`, `note` and a `Collection` representation of
the relation between `workout` and `exercise`, `exercise_done`.

#### ExerciseDone
Represents the `exercise_done` relation, containing `workout_id`, `exercise_id`
and `duration`. 

#### ResultSetConnection
A class for being able to return both a `ResultSet` and a `Connection` from a
database-controller. It only contains a `ResultSet` and `Connection`-attribute

### data
#### DatabaseHandler
A static class that creates a new database if there is none, and sets up the
necessary SQL tables. Also has functionality to wipe the data in the database
for a reset.

#### DataLoader
A static class that loads some example-data to the database

### net.efabrika.util
#### DBTablePrinter
A generic SQL-table printer
([Source](https://github.com/htorun/dbtableprinter)) that has been modified to
print pretty dates and duration. This class is used to present the user with
the rows returned from database-queries.

### ui
#### UserInterface
This is a static class that contains the application's `main`-method. It
contains a simple CLI for the user to interact with, providing options for all
the features of the application.

## Features
### 1. Register equipment, exercises and workout with associated data
#### Register equipment
Press 1 to register a `Machine` in the UI, fill in data when prompted.

#### Register exercises
Press 2 to register a `Exercise` in the UI, fill in data when prompted.

#### Register workout
Press 3 to register a `Workout` in the UI, fill in data when prompted.

### 2. Get information about the n latest workouts with their notes
Press 4 to load previous workouts. Input how many you want to see when
prompted.

### 3. For every exercise, retrieve a result log for a given time interval
Press 5 to view result logs. Select an `Exercise`, then input start and end of
interval when prompted.

### 4. Form exercise groups and find similar exercises
Press 6 to register an exercise group, fill in data when prompted.
Press 8 to select an `ExerciseGroup`, then view `Exercises` in that group

### 5. View workouts based on a machine
Press 7 to select a `Machine`, then view all `Workouts` performed on that
`Machine`.
