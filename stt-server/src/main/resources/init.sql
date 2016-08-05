alter table Transfer add foreign key (fromAccount) references Account(accountNumber)
alter table Transfer add foreign key (toAccount) references Account(accountNumber)

insert into Account(accountNumber, amount, description) values ('15800002589632588', 5000000, 'Account of Pizza Hutt')
insert into Account(accountNumber, amount, description) values ('25800002589632588', 3000000, 'Deposit of CIA')
insert into Account(accountNumber, amount, description) values ('35800002589632588', 2000000, 'Jurassic park account')
insert into Account(accountNumber, amount, description) values ('45800002589632588', 1000000, 'University account')
insert into Account(accountNumber, amount, description) values ('55800002589632588', 900000, 'Inter account')
insert into Account(accountNumber, amount, description) values ('65800002589632588', 800000, 'FinTech account')
