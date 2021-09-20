use `lod-gabriela`;

CREATE TABLE `artist` (
  `id` int(11) NOT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `artist_rating` (
  `userid` int(11) NOT NULL,
  `artistid` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  PRIMARY KEY (`userid`,`artistid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- https://stackoverflow.com/questions/18437689/error-1148-the-used-command-is-not-allowed-with-this-mysql-version
-- http://stackoverflow.com/questions/12485942/mysql-load-data-infile-cant-get-the-stat-of-file
-- https://dev.mysql.com/doc/refman/5.6/en/load-data-local.html

-- Using MySQL command line:
-- SHOW VARIABLES LIKE 'local_infile';
-- If you have SUPER privilege you can enable it (without restarting server with a new configuration) by executing:
-- SET GLOBAL local_infile = 1;

-- se não der certo pelo workbench, tentar direto pelo prompt
-- cd C:\Users\gbrla>cd C:\Program Files\MySQL\MySQL Server 8.0\bin
-- mysql -u username -p --local-infile


LOAD DATA LOCAL INFILE 'C:\\Users\\gbrla\\Google Drive\\UFBA\\Doutorado\\workspace-ufba\\lodweb-git\\sql\\artists-cleaned.csv'  INTO TABLE `lod-gabriela`.`artist` FIELDS TERMINATED BY ',';
LOAD DATA LOCAL INFILE 'C:\\Users\\gbrla\\Google Drive\\UFBA\\Doutorado\\workspace-ufba\\lodweb-git\\sql\\user_cleaned.csv'  INTO TABLE `lod-gabriela`.`user` FIELDS TERMINATED BY ',';
LOAD DATA LOCAL INFILE 'C:\\Users\\gbrla\\Google Drive\\UFBA\\Doutorado\\workspace-ufba\\lodweb-git\\sql\\user_artists_cleaned.csv'  INTO TABLE `lod-gabriela`.`artist_rating` FIELDS TERMINATED BY ',';

USE `lod-gabriela`;

CREATE TABLE `artist_rating_discrete` (
  `userid` int(11) NOT NULL,
  `artistid` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  PRIMARY KEY (`userid`,`artistid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- copiar os dados da tabela pra outra alterando o valor do rating de acordo com a regra da discretizacao