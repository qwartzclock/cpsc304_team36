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
drop table users;

alter session set nls_date_format='yyyy-mm-dd hh24:mi:ss';

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
    Primary Key (teamName)
);

grant select on team to public;

CREATE TABLE users (
	userName varchar(100),
	userPassword varchar(100),
	accessLevel int,
	fullName varchar(200),
	favTeam varchar(100),
	Primary Key (userName),
	FOREIGN KEY (favTeam) REFERENCES team (teamName)
);

grant select on users to public;
 
CREATE TABLE manager (
    managerID int,
    name varchar(50),
    phone int,
    email varchar(100),
    Primary Key (managerID)
);

grant select on manager to public;

CREATE TABLE manages (
	teamName varchar(100),
	managerID int,
	Primary Key (managerID),
	Foreign Key (managerID) REFERENCES manager ON DELETE CASCADE,
	Foreign Key (teamName) REFERENCES team ON DELETE CASCADE
);

grant select on manages to public;

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
    Foreign Key (game_date, location) REFERENCES game ON DELETE CASCADE,
    Foreign Key (teamName) REFERENCES team);

 
grant select on plays to public;
 
CREATE TABLE referee (
    ref_number int,
    name varchar(50),
    Primary Key (ref_number));

 
grant select on referee to public;
 
CREATE TABLE referees (
    game_date date,
    location varchar(100),
    ref_number int,
    Primary Key (game_date, location, ref_number),
    Foreign Key (game_date, location) REFERENCES game ON DELETE CASCADE,
    Foreign Key (ref_number) REFERENCES referee ON DELETE CASCADE);

grant select on referees to public;

CREATE TABLE event (
    gameTime varchar(50),
    playerID int,
    game_date date,
    location varchar(100),
    Primary Key (gameTime, playerID, game_date, location),
    Foreign Key (playerID) REFERENCES player ON DELETE CASCADE,
    Foreign Key (game_date, location) REFERENCES game ON DELETE CASCADE);

grant select on event to public;

CREATE TABLE penalty (
    gameTime varchar(50),
    playerID int,
    game_date date,
    location varchar(100),
    Primary Key (gameTime, playerID, game_date, location),
    Foreign Key (gameTime, playerID, game_date, location) REFERENCES event ON DELETE CASCADE);

grant select on penalty to public;

CREATE TABLE goal (
    gameTime varchar(50),
    playerID int,
    game_date date,
    location varchar(100),
    Primary Key (gameTime, playerID, game_date, location),
    Foreign Key (gameTime, playerID, game_date, location) REFERENCES event ON DELETE CASCADE);

CREATE TABLE assist (
    gameTime varchar(50),
    playerID int,
    scoringPlayerID int,
    game_date date,
    location varchar(100),
    Primary Key (gameTime, playerID, game_date, location),
    Foreign Key (gameTime, playerID, game_date, location) REFERENCES event ON DELETE CASCADE,
    Foreign Key (gameTime, scoringPlayerID, game_date, location) REFERENCES goal(gameTime, playerID, game_date, location) ON DELETE CASCADE
);

grant select on assist to public;
