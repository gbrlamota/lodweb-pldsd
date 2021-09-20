SET SQL_SAFE_UPDATES=0;
delete FROM lod.evaluation;
delete FROM lod.hitrate;
delete FROM lod.prediction;
delete FROM lod.semantic;
delete FROM lod.onlineevaluation;
delete FROM lod.graph;
SET SQL_SAFE_UPDATES=1;



select userid from `lod`.`prediction` as b where b.sim = "LDSD" group by userid;

select count(uri) from `lod`.`prediction` as b where b.userid = 5122 and b.sim = "LDSD";
select * from `lod`.`prediction` as b where b.userid = 5122 and b.sim = "LDSD";
select * from `lod`.`prediction` as b where b.userid = 5122 and b.sim = "LDSD_LOD";

select * from `lod`.`prediction` as b where b.sim = "LDSD";
select * from `lod`.`prediction` as b where b.sim = "LDSD_LOD";

select count(*) from `lod`.`prediction` as b where b.sim = "LDSD_LOD" and b.seed = 'SEED_EVALUATION';
select count(*) from `lod`.`prediction` as b where b.sim = "LDSD" and b.seed = 'SEED_EVALUATION';



select userid from `lod`.`prediction` as b where b.sim = "LDSD" and b.original_candidate = b.uri group by userid;
select userid from `lod`.`prediction` as b where b.sim = "LDSD_LOD" and b.original_candidate = b.uri group by userid;

select count(userid) from `lod`.`prediction` as b where b.sim = "LDSD_LOD" and b.original_candidate = b.uri order by userid;
select count(userid) from `lod`.`prediction` as b where b.sim = "LDSD" and b.original_candidate = b.uri order by userid;

select count(userid) from `lod`.`prediction` as b where b.sim = "LDSD_LOD" and b.original_candidate != b.uri order by userid;
select count(userid) from `lod`.`prediction` as b where b.sim = "LDSD" and b.original_candidate != b.uri order by userid;

select * from `lod`.`prediction` as b where b.original_candidate = b.uri order by userid;

select max(userid)  FROM ((select distinct ml.userid from music as m, music_like as ml where  m.id = ml.musicid  )   UNION ALL (select distinct ml.userid from movie as m, movie_like as ml where  m.id = ml.movieid  )   UNION ALL (select distinct ml.userid from book  as m, book_like  as ml where  m.id = ml.bookid   )  ) as x;

SELECT * FROM lod.hitrate;	
SELECT after FROM lod.hitrate;
SELECT * FROM lod.evaluation where original_candidate=uri;

SELECT * FROM lod.evaluation where original_candidate=uri and `sim`= "LDSD_LOD";

SELECT count(*) FROM lod.evaluation where `sim`= "LDSD_LOD" and original_candidate=uri;
SELECT count(*) FROM lod.evaluation where `sim`= "LDSD_LOD";
SELECT count(*) FROM lod.evaluation where `sim`= "LDSD";


SELECT count(original_candidate) FROM lod.evaluation where `sim`= "LDSD_LOD" and userid = (select max(userid)  FROM lod.evaluation where original_candidate=uri ) ;

select max(userid)  FROM lod.evaluation where original_candidate=uri;

select count(original_candidate) from `lod`.`evaluation` as b where b.userid = 1;
select count(original_candidate) from `lod`.`evaluation` as b where b.userid = 458;

select count(original_candidate) from `lod`.`evaluation`   as b where b.userid = 458 and `sim`= "LDSD_LOD" ;
select count(original_candidate) from `lod`.`evaluation` as b where b.userid = 458 and `sim`= "LDSD"   ;


select max(userid)  FROM lod.evaluation where original_candidate=uri;


select max(userid) FROM lod.evaluation where original_candidate=uri;

SET SQL_SAFE_UPDATES=0;
delete FROM lod.evaluation WHERE userid = 458;
SELECT * FROM lod.evaluation;

SELECT * FROM lod.evaluation where uri = 'http://dbpedia.org/resource/The_Man_Who_Cried';

SELECT * FROM lod.evaluation where userid = 179850;

SELECT count(*) FROM lod.evaluation;

select distinct *  FROM ((select distinct m.id, m.uri from music as m, music_like as ml where  m.id = ml.musicid and ml.userid = 200 ) 
  UNION ALL (select distinct m.id, m.uri from movie as m, movie_like as ml where  m.id = ml.movieid and ml.userid = 200 )
  UNION ALL (select distinct m.id, m.uri from book  as m, book_like  as ml where  m.id = ml.bookid  and ml.userid = 200 )  ) as x;

select distinct userid  FROM ((select distinct count(mu.id) as total, userid  from music as mu, music_like as mul where  mu.id = mul.musicid  group by userid)   UNION ALL (select distinct count(mo.id) as total, userid from movie as mo, movie_like as mol where  mo.id = mol.movieid group by userid)   UNION ALL (select distinct count(bo.id) as total, userid from book  as bo, book_like  as bol where  bo.id = bol.bookid  group by userid)) as x group by userid  having sum(total) > 0 and sum(total) < 5;

select distinct *  FROM ((select distinct m.uri from music as m, music_like as ml where  m.id = ml.musicid and ml.userid = 2008 )
   UNION ALL (select distinct m.uri from movie as m, movie_like as ml where  m.id = ml.movieid and ml.userid = 2008 ) 
   UNION ALL (select distinct m.uri from book  as m, book_like  as ml where  m.id = ml.bookid  and ml.userid = 2008 )  ) as x ORDER BY RAND() LIMIT 20;


SELECT * FROM lod.evaluation;
SELECT count(*) FROM lod.evaluation  WHERE `sim`='LDSD_LOD' and uri = original_candidate ;
SELECT count(*) FROM lod.evaluation  WHERE `sim`='LDSD' and uri = original_candidate ;

OPTIMIZE  TABLE LINK;
OPTIMIZE  TABLE PREDICTION;
OPTIMIZE  TABLE SEMANTIC;


SET SQL_SAFE_UPDATES=0;
delete FROM lod.evaluation;
delete FROM lod.hitrate;
delete FROM lod.prediction;
SET SQL_SAFE_UPDATES=1;

SELECT * FROM lod.hitrate;


SET SQL_SAFE_UPDATES=1;

SET SQL_SAFE_UPDATES=0;
select count(*) FROM lod.link WHERE uri1 = uri2;
delete FROM lod.link WHERE uri1 = uri2;
SET SQL_SAFE_UPDATES=1;

SET SQL_SAFE_UPDATES=0;
select * FROM lod.link WHERE uri1 = "https://www.w3.org/2002/07/owl#Thing";
select count(*) FROM lod.link WHERE uri2 = "https://www.w3.org/2002/07/owl#Thing";

select count(*) FROM lod.link WHERE uri2 = "https://www.w3.org/2002/07/owl#Thing";
select count(*) FROM lod.link WHERE uri1 = uri2;
select * FROM lod.link WHERE uri1 = uri2;

select * FROM lod.link LIMIT 500;





SET SQL_SAFE_UPDATES=1;



SET SQL_SAFE_UPDATES=0;
delete FROM lod.hitrate WHERE lod.hitrate.after = 1;
SET SQL_SAFE_UPDATES=1;

SELECT distinct userid FROM lod.evaluation where uri=original_candidate order by userid ;


SELECT * FROM lod.evaluation where `sim`='LDSD' and original_candidate=uri order by userid;
SELECT *  FROM lod.evaluation where `sim`='LDSD_LOD' and original_candidate=uri order by userid;
SELECT max(userid) FROM lod.evaluation where `sim`='LDSD_LOD' and original_candidate=uri order by userid;
SELECT max(userid) FROM lod.evaluation where `sim`='LDSD' and original_candidate=uri order by userid;

SELECT count(*) FROM lod.evaluation;

SELECT * FROM lod.evaluation where `sim`='LDSD_LOD' and original_candidate!=uri;
SELECT * FROM lod.evaluation where `sim`='LDSD' and original_candidate!=uri;


SELECT * FROM lod.evaluation where `sim`='LDSD_LOD' order by position;
SELECT * FROM lod.evaluation where `sim`='LDSD' and original_candidate!=uri;
