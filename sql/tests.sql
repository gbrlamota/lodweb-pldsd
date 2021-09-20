SELECT * FROM lod.semantic
WHERE uri1 = 'http://dbpedia.org/resource/Pitch_Perfect'
OR uri2 = 'http://dbpedia.org/resource/Coraline_(film)' ;
#sim = 'LDSD';

SELECT count(*) FROM lod.semantic
WHERE sim = 'LITERAL';

SELECT distinct b.score from `lod`.`semantic` as b 
where 
((b.uri1 =  'http://dbpedia.org/resource/Pitch_Perfect' 
and b.uri2 = 'http://dbpedia.org/resource/Beauty_and_the_Beast_(1991_film)' and b.sim = 'LDSD') 
OR 
(b.uri1 =  'http://dbpedia.org/resource/A_Clockwork_Orange_(film)' 
and b.uri2 = 'http://dbpedia.org/resource/Pitch_Perfect'  and b.sim = 'LDSD')) 


SELECT distinct b.score from `lod`.`semantic` as b 
where ((b.uri1 =  ? and b.uri2 = ? and b.sim = ?) OR (b.uri1 =  ? and b.uri2 = ?  and b.sim = ?));