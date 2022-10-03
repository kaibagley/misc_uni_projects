#Task 2.c Solution:

create database proj21984315;

use proj21984315;

create table customer (
PhoneNumber VARCHAR(20) primary key, 
FirstName VARCHAR(20), 
LastName VARCHAR(20), 
A_id INT, 
PaymentMethod VARCHAR(20)
);

create table address (
A_id INT primary key, 
StreetName VARCHAR(20), 
StreetNo INT, 
Suburb VARCHAR(20)
);

create table orders (
O_id INT primary key, 
OrderTime DATETIME,
Size VARCHAR(20),
Discounts FLOAT, 
Crust VARCHAR(20), 
PhoneNumber INT
);

create table sizes (
Size VARCHAR(20) primary key,
Price FLOAT
);

create table deliveredby (
O_id INT, 
E_id INT, 
constraint PK_deliveredby primary key (E_id, O_id)
);

create table cookedby (
O_id INT, 
E_id INT,
constraint PK_cookedby primary key (E_id, O_id)
);

create table sides (
S_id INT primary key, 
Descrip VARCHAR(50), 
Manufacturer VARCHAR(50), 
Supplier VARCHAR(50), 
RetailPrice FLOAT, 
Cost FLOAT
);

create table includes (
O_id INT, 
S_id INT,
constraint PK_includes primary key (O_id, S_id)
);

create table ingredients (
Topping VARCHAR(20) primary key, 
RetailPrice FLOAT, 
Cost FLOAT
);

create table composedof (
O_id INT, 
Topping VARCHAR(20),
Quantity INT,
constraint PK_composedof primary key (O_id, Topping)
);

create table employee (
E_id INT primary key, 
Roles VARCHAR(20), 
Salary FLOAT, 
A_id INT, 
FirstName VARCHAR(20), 
LastName VARCHAR(20),  
PhoneNumber INT
);

#Task 2.d Solution:

insert into customer
values
(12345234,'kai','bagley',1,'cash'),
(75846253,'hay','pattrick',2,'cash'),
(34567898,'big','dog',3,'card'),
(98765487,'toby','smith',4,'cash'),
(98765822,'jane','hocking',5,'cheque');

insert into employee
values
(1,'delivery',24.55,5,'oscar','hocking',98724444),
(2,'both',25.55,6,'bill','kuids',86371215),
(3,'cooking',25.55,7,'mick','holmes',09345224),
(4,'both',25.55,8,'lydia','shhhhhchhhhchhhh',86371215),
(5,'delivery',24.55,9,'sherry','eclpise',09345224);

insert into address
values
(1,'dingus avenue',23,'nedlands'),
(2,'nah lane',66,'crawley'),
(3,'stirling highway',35,'crawley'),
(4,'seventh street',2,'renmark'),
(5,'tenth avenue',10,'ten'),
(6,'garbage dump hell',69,'dontask'),
(7,'im a trash man',33,'canada'),
(8,'yeah nah yeah',44,'australia'),
(9,'go home biggie',9,'murica');

insert into orders
values
(1,'1000-01-01 00:00:01','hyper',10.00,'thicc',75846253),
(2,'2018-06-15 15:46:33','extreme',5.00,'nonexistent',34567898),
(3,'2018-12-30 16:46:34','microscopic',0.50,'regular',75846253),
(4,'2018-07-15 19:46:35','astronomical',500.00,'noodle',98765487),
(5,'2018-05-12 15:25:11','astronomical',30.00,'nonexistent',12345234),
(6,'2020-02-15 11:40:55','extreme',8.00,'nonexistent',34567898),
(7,'2011-12-15 18:35:14','hyper',20.00,'noodle',98765822),
(8,'2011-05-15 12:58:30','hyper',20.00,'thicc',98765822);

insert into sizes
values
('hyper',40),
('extreme',50),
('microscopic',1),
('astronomical',3000);

insert into deliveredby
values
(1,1),
(2,2),
(3,5),
(4,4),
(5,1),
(6,1),
(7,4),
(8,5);

insert into cookedby
values
(1,3),
(2,3),
(3,2),
(4,4),
(5,3),
(6,2),
(7,4),
(8,3);

insert into sides
values
(1,'chips','schmang incorporated','peckish distributors',5.00,2.00),
(2,'drink','dangus foods limited','peckish distributors',3.00,0.50),
(3,'jelly','hayden pattrick','hayden pattrick',40,0.25);

insert into includes
values
(1,1),
(1,2),
(2,1),
(3,2),
(4,1),
(4,2),
(4,3),
(5,2),
(6,3),
(7,3),
(7,2),
(8,1);

insert into ingredients
values
('cheese',1.00,0.50),
('bacon',2.00,1.00),
('marshmallows',1.00,0.25),
('breathmints',6.00,0.05),
('iced vovos',2.00,1.00),
('tomato',1.00,0.50),
('lettuce',1.00,0.25),
('sausage',2.00,1.00),
('olives',1.00,0.50),
('donuts',20,10);

insert into composedof
values
(1,'cheese',2),
(1,'marshmallows',10),
(1,'tomato',2),
(2,'cheese',4),
(2,'lettuce',2),
(3,'sausage',4),
(3,'bacon',1),
(4,'breathmints',2),
(4,'iced vovos',4),
(5,'lettuce',5),
(5,'marshmallows',2),
(5,'bacon',4),
(5,'iced vovos',1),
(6,'lettuce',2),
(7,'iced vovos',4),
(8,'bacon',5);

#Task 3.a Solution:

select *
from employee
where roles = 'both';

#Task 3.b Solution:
create procedure ingprice(subrb varchar(20), out num float)
select sum(i.RetailPrice * co.Quantity)
from orders o
join customer c on o.PhoneNumber = c.PhoneNumber
join address a on c.A_id=a.A_id
join sizes s on o.size=s.size
join composedof co on o.O_id=co.O_id
join ingredients i on co.Topping=i.Topping
where a.suburb = subrb
into num;

create procedure sidprice(subrb varchar(20),out num float)
select sum(sd.RetailPrice)
from orders o
join customer c on o.PhoneNumber = c.PhoneNumber
join address a on c.A_id=a.A_id
join sizes s on o.size=s.size
join includes inc on o.O_id=inc.O_id
join sides sd on inc.S_id=sd.S_id
where a.suburb = subrb
into num;

create procedure sizprice(subrb varchar(20),out num int)
select sum(s.price)-sum(discounts)
from orders o
join customer c on o.PhoneNumber = c.PhoneNumber
join address a on c.A_id=a.A_id
join sizes s on o.size=s.size
where a.suburb = subrb
into num;

call ingprice('Crawley',@num1);
call sidprice('Crawley',@num2);
call sizprice('Crawley',@num3);
select @num1+@num2+@num3;      #194

call ingprice('renmark',@num4);
call sidprice('renmark',@num5);
call sizprice('renmark',@num6);
select @num4+@num5+@num6;      #2568

call ingprice('nedlands',@num7);
call sidprice('nedlands',@num8);
call sizprice('nedlands',@num9);
select @num7+@num8+@num9;      #2990

call ingprice('ten',@num10);
call sidprice('ten',@num11);
call sizprice('ten',@num12);
select @num10+@num11+@num12;   #106

create table spentinsuburb (
suburb varchar(20),
moneyspent int
);

insert into spentinsuburb
values
('Crawley',@num1+@num2+@num3),
('Renmark',@num4+@num5+@num6),
('Nedlands',@num7+@num8+@num9),
('ten',@num10+@num11+@num12);

select *
from spentinsuburb
order by moneyspent desc;

#Task 3.c Solution:

#Task 3.d Solution:

select i.topping
from orders o
join composedof c on c.O_id=o.O_id
right join ingredients i on i.topping=c.topping
where o.O_id is null;

#Task 3.e Solution:

#Task 3.f Solution:

select *
from orders o
join customer c on o.PhoneNumber = c.PhoneNumber
join address a on c.A_id=a.A_id
join sizes s on o.size=s.size
join composedof co on o.O_id=co.O_id
join ingredients i on co.Topping=i.Topping;
