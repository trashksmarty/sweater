insert into usr (id, username, password, active)
    values (1, 'Admin', '$2a$08$R/0CHn3K4WkgG5br5BJxr.24oFhCdZQxQzZZMyOHEvwsPYtWfpyj.', true);

insert into user_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');