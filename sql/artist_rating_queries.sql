SELECT * FROM `lod-gabriela`.artist_rating;

SELECT max(id) FROM `lod-gabriela`.user;

SELECT avg(rating) FROM `lod-gabriela`.artist_rating;

SELECT min(rating) FROM `lod-gabriela`.artist_rating;
-- 1

SELECT max(rating) FROM `lod-gabriela`.artist_rating;
-- 352698

-- rating 1 = 1 - 70539
-- rating 2 = 70540 - 141078
-- rating 3 = 141079 - 211617
-- rating 4 = 211618 - 282156
-- rating 5 = 282157 - 352695 + 3

SELECT * FROM `lod-gabriela`.artist_rating where rating >= 282157 AND rating <= 352698;

SELECT count(*) FROM `lod-gabriela`.artist_rating where userid = 270; -- 14 ratings

SELECT count(*) FROM `lod-gabriela`.artist_rating where userid = 192; -- 30 ratings

SELECT count(*) FROM `lod-gabriela`.artist_rating where userid = 2; -- 50 ratings


SELECT userid, count(*) FROM `lod-gabriela`.artist_rating GROUP BY userid; -- 40 ratings

