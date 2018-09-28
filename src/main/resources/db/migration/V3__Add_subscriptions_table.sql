create table user_subscriptions (
    subscriber_id bigint not null,
    channel_id bigint not null,
    primary key (channel_id, subscriber_id)
    ) engine=MyISAM;

alter table user_subscriptions
    add constraint user_subscriptions_usr_fk
    foreign key (channel_id)
    references usr (id);

alter table user_subscriptions
    add constraint user_subscriptions_usr_fk
    foreign key (subscriber_id)
    references usr (id);