Wbuser:
SELECT * from weibo_tweet where userid not in  (SELECT userid from wb_user)
delete from law9_txt where uid not in (select * from tmp)
delete from law9_user where userid not in (select userid from law9_txt)

insert wb_statueswh select * from wb_statues where wb_statues.uid not in (select uid from wb_statueswh)


delete from  wb_pois 
where uid not  in ( 
select max(uid) from wb_poiskfc 
group by pid) 

create table abc as select * from tabe

UPDATE wb_statueswh_all SET cdate = DATE_FORMAT(posttime,'%Y-%m-%d');

INSERT into wb_statueswh_all SELECT * from wb_statueswh t where  t.uid not in (select  uid from wb_statueswh_all)

select a.*,b.* from
(select pid,
max(case when ukey='肯德基' AND distance=800 then num else null end) as '肯德基800',
max(case when ukey='肯德基' and distance=1600 then num else null end) as "肯德基1600",
max(case when ukey='沃尔玛' and distance=800 then num else null end) as "沃尔玛800",
max(case when ukey='沃尔玛' and distance=1600 then num else null end) as "沃尔玛1600",
max(case when ukey='体育馆' and distance=800 then num else null end) as "体育馆800",
max(case when ukey='体育馆' and distance=1600 then num else null end) as "体育馆1600"
from wb_poiseffect 
group by pid) as b, wb_pois as a
where a.pid=b.pid
l

delete from wb_statueswh where tag like '%20140601%';
insert wb_statueswh select * from wb_statues;
select count(*) from wb_statueswh;

//字符串分解
update weibo_tweet set lon= substring_index(geo,",",1)  where not isnull(geo);
update weibo_tweet set lat= substring_index(substring_index(geo,",",2),",",-1)  where not isnull(geo);