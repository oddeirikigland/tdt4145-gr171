create table if not exists workout (
    workout_id      integer primary key autoincrement,
    timestamp       date not null,
    duration        integer not null,
    form            integer not null,
    performance     integer not null,
    notes            varchar(255)
);

create table if not exists exercise_group (
    exercise_group_id   integer primary key autoincrement,
    name                varchar(31) not null
);

create table if not exists  machine (
    machine_id      integer primary key autoincrement,
    name            varchar(31) not null
);

create table if not exists exercise (
    exercise_id     integer primary key autoincrement,
    name            varchar(31) not null
);

create table if not exists machine_exercise (
    exercise_id     integer primary key,
    kilo            integer not null,
    sets             integer not null,
    machine_id      integer not null,
    foreign key (exercise_id) references exercise(exercise_id)
                            on update cascade
                            on delete cascade,
    foreign key (machine_id) references machine(machine_id)
                            on update cascade
                            on delete cascade
);

create table if not exists free_exercise (
    exercise_id     integer primary key,
    description            varchar(31) not null,
    foreign key (exercise_id) references exercise(exercise_id)
                            on update cascade
                            on delete cascade
);

create table if not exists exercise_done (
    workout_id      integer not null,
    exercise_id     integer not null,
    duration        integer not null,
    primary key (workout_id, exercise_id),
    foreign key (workout_id) references workout(workout_id),
    foreign key (exercise_id) references exercise(exercise_id)
);

create table if not exists includes (
    exercise_group_id   integer not null,
    exercise_id         integer not null,
    primary key (exercise_group_id, exercise_id),
    foreign key (exercise_group_id) references exercise_group(exercise_group_id),
    foreign key (exercise_id) references exercise(exercise_id)
);
