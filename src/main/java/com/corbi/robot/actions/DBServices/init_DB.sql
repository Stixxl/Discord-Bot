                    CREATE DATABASE HydraBotDB;
                    \c HydraBotDB
                    CREATE TABLE HydraBotDB.USERS
                    (ID varchar(255) NOT NULL, 
                    UPTIME bigint NOT NULL,  
                    NAME varchar(255) NOT NULL,
                    PRIMARY KEY (ID));              

                    CREATE TABLE  HydraBotDB.GAMES
                    (TITLE varchar(255) NOT NULL, 
                    ID varchar(255) not NULL,
                    TIME_PLAYED bigint NOT NULL, 
                    AMOUNT_PLAYED int NOT NULL, 
                    FOREIGN KEY (ID) references  HydraBot.USERS ON DELETE CASCADE,
                    PRIMARY KEY(ID, TITLE));

                    CREATE TABLE HydraBotDB.SOUNDS
                    (NAME varchar(255) NOT NULL,
                    PATH varchar(511) NOT NULL,
                    AMOUNT_REQUESTS int NOT NULL,
                    DESCRIPTION varchar(1023),
                    PRIMARY KEY (NAME));
                    
                    CREATE TABLE HydraBotDB.BINSENWEISHEITEN
                    (ID int NOT NULL SERIAL,
                     content varchar(511) NOT NULL,
                     PRIMARY KEY (ID));

                    CREATE TABLE HydraBotDB.FLAMEFORDANIEL
                    (ID int NOT NULL SERIAL,
                     content varchar(511) NOT NULL,
                     PRIMARY KEY (ID));