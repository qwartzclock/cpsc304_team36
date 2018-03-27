drop table manager;
drop table playsFor;
drop table game;
drop table plays;
drop table referee;
drop table referees;
drop table event;
drop table penalty;
drop table assist;
drop table team;
drop table player;

CREATE TABLE player (
    PlayerID int,
    height float,
    weight float,
    name varchar(50),
    salary float,
    Primary Key (PlayerID)
);

grant select on player to public;

CREATE TABLE team (
    teamName varchar(100),
    city varchar(100),
    managerID int UNIQUE NOT NULL,
    Primary Key (teamName)
);

grant select on team to public;
 
CREATE TABLE manager (
    managerID int,
    name varchar(50),
    phone int,
    email varchar(100),
    teamName varchar(100) UNIQUE NOT NULL,
    Primary Key (managerID),
    Foreign Key (teamName) REFERENCES team 
        ON DELETE SET NULL
);

grant select on manager to public;

ALTER TABLE team ADD FOREIGN KEY (managerID) REFERENCES manager (managerID);

CREATE TABLE playsFor (
    PlayerID int,
    teamName varchar(100),
    jerseyNum int,
    Primary Key (PlayerID),
    Foreign Key (PlayerID) REFERENCES player
        ON DELETE CASCADE,
    Foreign Key (teamName) REFERENCES team
        ON DELETE CASCADE
);
 
grant select on playsFor to public;
 
CREATE TABLE game (
    game_date date,
    location varchar(100),
    Primary Key (game_date, location)
);

grant select on game to public;
 
CREATE TABLE plays (
    game_date date,
    location varchar(100),
    teamName varchar(100),
    Primary Key (game_date, location),
    Foreign Key (game_date, location) REFERENCES game
        ON DELETE CASCADE,
    Foreign Key (teamName) REFERENCES team
        ON DELETE SET DEFAULT
);

 
grant select on plays to public;
 
CREATE TABLE referee (
    number int,
    name varchar(50),
    Primary Key (number)
);

 
grant select on referee to public;
 
CREATE TABLE referees (
    game_date date,
    location varchar(100),
    number int,
    Primary Key (game_date, location, number),
    Foreign Key (game_date, location) REFERENCES game,
        ON DELETE CASCADE,
    Foreign Key (number) REFERENCES referee
        ON DELETE CASCADE
);

grant select on referees to public;

CREATE TABLE event (
    gameTime varchar(50),
    playerID int,
    game_date date,
    location varchar(100),
    Primary Key (gameTime, playerID, game_date, location),
    Foreign Key (playerID) REFERENCES player,
        ON DELETE CASCADE,
    Foreign Key (game_date, location) REFERENCES game,
        ON DELETE CASCADE
);

grant select on event to public;

CREATE TABLE penalty (
    gameTime varchar(50),
    playerID int,
    game_date date,
    location varchar(100),
    number int,
    Primary Key (gameTime, playerID, game_date, location),
    Foreign Key (gameTime, playerID, game_date, location) REFERENCES event,
        ON DELETE CASCADE,
    Foreign Key (number) REFERENCES referee,
        ON DELETE SET DEFAULT
);

grant select on penalty to public;

CREATE TABLE assist (
    gameTime varchar(50),
    playerID int,
    scoringPlayerID int,
    game_date date,
    location varchar(100),
    Primary Key (gameTime, playerID, game_date, location),
    Foreign Key (gameTime, playerID, game_date, location) REFERENCES event,
        ON DELETE CASCADE,
    Foreign Key (gameTime, scoringPlayerID, game_date, location) REFERENCES 
        goal(gameTime, playerID, game_date. location)
        ON DELETE CASCADE
);

grant select on assist to public;
