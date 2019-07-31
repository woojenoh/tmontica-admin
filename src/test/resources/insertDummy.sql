-- option sql
insert into options(type , name, price) values('Temperature', 'HOT', 0);
insert into options(type , name, price) values('Temperature', 'ICE', 0);
insert into options(type , name, price) values('Shot', 'AddShot', 300);
insert into options(type, name, price) values('Syrup', 'AddSyrup', 300);
insert into options(type , name, price) values('Size', 'SizeUp', 500);

-- -- users 임시 데이터
-- insert into users values('김현정', 'testid', 'test@tmoncorp.com', '1994-11-11', '1234', 'user', sysdate(), 5000);
-- -- orders 임시 데이터
-- insert into orders values(1, sysdate(), '현장결제', 5000, 2000, 3000, '결제완료', 'testid', '모바일');
-- insert into orders values(2, sysdate(), '현장결제', 7000, 2000, 5000, '준비완', 'testid', '모바일');
-- -- menus 임시 데이터
-- insert into menus values(0,'americano',1000,'음료','beverage',0,1,'url','맛있는 아메리카노',1000,10,'2019-07-01','2019-07-11','admin','admin',20,'아메리카노',null,null);
-- insert into menus values(0,'coppuccino',1500,'음료','beverage',0,1,'url','맛있는 카푸치노',1500,10,'2019-07-01','2019-07-11','admin','admin',20,'카푸치노',null,null);
-- -- order_details 임시 데이터
-- insert into order_details values(0,1, '1__13__2', 1000, 1, 1);
-- insert into order_details values(0,1, '1__13__2', 3000, 2, 2);
-- insert into order_details values(0,2, '1__1/3__2', 1000, 1, 1);
-- insert into order_details values(0,2, '1__1/3__2', 3000, 2, 2);
--
-- -- cart_menus  추가
-- insert into cart_menus values(2, '1__1/3__2', 0 , 'testid', 1000, 1, false);
-- insert into cart_menus values(2, '1__1/3__2', 0 , 'testid', 1000, 1, true);

-- menus
INSERT INTO menus (name_ko, name_eng, product_price, category_ko, category_eng, monthly_menu, usable, img_url, description, sell_price, discount_rate, created_date, creator_id, stock) VALUES ('카페라떼','coffeelatte', 1500, '커피','coffee', 0 , 1 , '/img/coffee/caffeelatte.png', '맛있는 카페라떼', 1500, 0 , '2018-08-28 17:22:21',  'jungmisu', 100);
INSERT INTO menus (name_ko, name_eng, product_price, category_ko, category_eng, monthly_menu, usable, img_url, description, sell_price, discount_rate, created_date, creator_id, stock) VALUES ('자몽에이드','grapefruitade', 1500, '에이드','ade', 0 , 1 , '/img/ade/grapefruitade.png', '맛있는 자몽에이드', 1500, 0 , '2018-08-28 17:22:21',  'jungmisu', 100);
INSERT INTO menus (name_ko, name_eng, product_price, category_ko, category_eng, monthly_menu, usable, img_url, description, sell_price, discount_rate, created_date, creator_id, stock) VALUES ('레몬에이드','lemonade', 1500, '에이드','ade', 1 , 1 , '/img/ade/lemonade.png', '맛있는 레몬에이드', 1500, 0 , '2018-08-28 17:22:21',  'jungmisu', 200);
INSERT INTO menus (name_ko, name_eng, product_price, category_ko, category_eng, monthly_menu, usable, img_url, description, sell_price, discount_rate, created_date, creator_id, stock) VALUES ('카페라떼','coffeelatte', 1500, '커피','coffee', 0 , 1 , '/img/coffee/caffeelatte.png', '맛있는 카페라떼', 1500, 0 , '2018-08-29 17:22:21',  'jungmisu', 100);
INSERT INTO menus (name_ko, name_eng, product_price, category_ko, category_eng, monthly_menu, usable, img_url, description, sell_price, discount_rate, created_date, creator_id, stock) VALUES ('바닐라라떼','Vanilla latte', 1500, '커피','coffee', 0 , 1 , '/img/coffee/vanillalatte.png', '달콤한 바닐라라떼', 1500, 0 , '2018-08-30 17:22:21',  'jungmisu', 200);
INSERT INTO menus (name_ko, name_eng, product_price, category_ko, category_eng, monthly_menu, usable, img_url, description, sell_price, discount_rate, created_date, creator_id, stock) VALUES ('소보루빵','Soboru bread', 2000 , '빵','bread', 0 , 1 , '/img/bread/soborubread.png', '맛있는 소보루빵', 2000, 0 , '2019-06-30 15:30:30',  'jungmisu', 10);
INSERT INTO menus (name_ko, name_eng, product_price, category_ko, category_eng, monthly_menu, usable, img_url, description, sell_price, discount_rate, created_date, creator_id, stock) VALUES ('카스테라','castella', 1500 , '빵','bread', 0 , 1 , '/img/bread/castella.png', '폭신한 카스테라', 1500, 0 , '2019-06-30 15:35:30',  'jungmisu', 20);


