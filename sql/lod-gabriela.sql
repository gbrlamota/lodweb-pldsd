-- C:\Users\gbrla\Google Drive\UFBA\Doutorado\workspace-ufba\lodweb-git\sql\lod-gabriela.sqlartist_rating

create database `lod-gabriela`;

use `lod-gabriela`;

CREATE TABLE `lod-gabriela`.`user` (
  `id` INT NOT NULL,
  PRIMARY KEY (`iduser`));

CREATE TABLE `lod-gabriela`.`movie` (
  `id` INT NOT NULL,
  `title` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `uri` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE,
  UNIQUE INDEX `uri_UNIQUE` (`uri` ASC) VISIBLE)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
CREATE TABLE `lod-gabriela`.`movie_rating` (
  `userid` INT NOT NULL,
  `movieid` INT NOT NULL,
  `rating` INT NOT NULL,
  `timestamp` TIME NULL,
  PRIMARY KEY (`userid`, `movieid`))
ENGINE=InnoDB DEFAULT CHARSET=utf8;
  ALTER TABLE `lod-gabriela`.`movie_rating` 
ADD CONSTRAINT `fk_movie`
  FOREIGN KEY (`movieid`)
  REFERENCES `lod-gabriela`.`movie` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_user`
  FOREIGN KEY (`userid`)
  REFERENCES `lod-gabriela`.`user` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

  
CREATE TABLE `lod-gabriela`.`property_weighting` (
  `userid` INT NOT NULL,
  `propertyuri` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `weight` DECIMAL(3,3) NOT NULL,
  PRIMARY KEY (`userid`, `propertyuri`),
  CONSTRAINT `fk_property_user`
    FOREIGN KEY (`userid`)
    REFERENCES `lod-gabriela`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
	ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
ALTER TABLE `lod-gabriela`.`property_weighting` 
ADD COLUMN `level` INT NOT NULL AFTER `weight`;

ALTER TABLE `lod-gabriela`.`property_weighting` 
DROP PRIMARY KEY
ADD PRIMARY KEY (`userid`, `propertyuri`, `level`);

CREATE TABLE `lod-gabriela`.`similarity` (
  `uri1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `uri2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sim` varchar(10) NOT NULL,
  `score` double NOT NULL,
  PRIMARY KEY (`uri1`,`uri2`,`sim`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `similarity_user` (
  `uri1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `uri2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sim` varchar(10) NOT NULL,
  `userid` int(11) NOT NULL,
  `score` double NOT NULL,
  PRIMARY KEY (`uri1`,`uri2`,`userid`,`sim`),
  KEY `fk_similarity_user_user_idx` (`userid`),
  CONSTRAINT `fk_similarity_user_user` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



LOAD DATA LOCAL INFILE 'C:\\Users\\gbrla\\Google Drive\\UWO\\Research\\LOD-databases\\MappingMovielens2DBpedia-1.2.tsv'  INTO TABLE `lod-gabriela`.`movie` FIELDS TERMINATED BY '\t';

INSERT INTO `lod-gabriela`.`movie` VALUES (1,'Toy Story (1995)','http://dbpedia.org/resource/Toy_Story');

LOAD DATA LOCAL INFILE 'C:\\Users\\gbrla\\Google Drive\\UWO\\Research\\LOD-databases\\ml-1m\\ml-1m\\users.dat'  INTO TABLE `lod-gabriela`.`user` FIELDS TERMINATED BY '\t';
LOAD DATA LOCAL INFILE 'C:\\Users\\gbrla\\Google Drive\\UWO\\Research\\LOD-databases\\ml-1m\\ml-1m\\ratings.dat'  INTO TABLE `lod-gabriela`.`movie_rating` FIELDS TERMINATED BY '::';


  