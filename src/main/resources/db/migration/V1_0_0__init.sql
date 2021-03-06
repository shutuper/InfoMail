-- DIALECT IS POSTGRESQL
create table qrtz_job_details
(
    sched_name        varchar(120)  not null,
    job_name          varchar(200)  not null
        primary key,
    job_group         varchar(200)  not null,
    description       varchar(250),
    job_class_name    varchar(250)  not null,
    is_durable        boolean       not null,
    is_nonconcurrent  boolean       not null,
    is_update_data    boolean       not null,
    requests_recovery boolean       not null,
    job_data          bytea,
    order_id          serial unique not null,
    constraint qrtz_job_details_uq
        unique (job_name, sched_name, job_group)
);

CREATE TABLE qrtz_triggers
(
    sched_name     VARCHAR(120) NOT NULL,
    trigger_name   VARCHAR(200) NOT NULL,
    trigger_group  VARCHAR(200) NOT NULL,
    job_name       VARCHAR(200) NOT NULL,
    job_group      VARCHAR(200) NOT NULL,
    description    VARCHAR(250) NULL,
    next_fire_time BIGINT       NULL,
    prev_fire_time BIGINT       NULL,
    priority       INTEGER      NULL,
    trigger_state  VARCHAR(16)  NOT NULL,
    trigger_type   VARCHAR(8)   NOT NULL,
    start_time     BIGINT       NOT NULL,
    end_time       BIGINT       NULL,
    calendar_name  VARCHAR(200) NULL,
    misfire_instr  SMALLINT     NULL,
    job_data       BYTEA        NULL,
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, job_name, job_group)
        REFERENCES qrtz_job_details (sched_name, job_name, job_group) ON DELETE CASCADE
);

CREATE TABLE qrtz_simple_triggers
(
    sched_name      VARCHAR(120) NOT NULL,
    trigger_name    VARCHAR(200) NOT NULL,
    trigger_group   VARCHAR(200) NOT NULL,
    repeat_count    BIGINT       NOT NULL,
    repeat_interval BIGINT       NOT NULL,
    times_triggered BIGINT       NOT NULL,
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES
        qrtz_triggers (sched_name, trigger_name, trigger_group)  ON DELETE CASCADE
);

CREATE TABLE qrtz_cron_triggers
(
    sched_name      VARCHAR(120) NOT NULL,
    trigger_name    VARCHAR(200) NOT NULL,
    trigger_group   VARCHAR(200) NOT NULL,
    cron_expression VARCHAR(120) NOT NULL,
    time_zone_id    VARCHAR(80),
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES
        qrtz_triggers (sched_name, trigger_name, trigger_group)  ON DELETE CASCADE
);

CREATE TABLE qrtz_simprop_triggers
(
    sched_name    VARCHAR(120)   NOT NULL,
    trigger_name  VARCHAR(200)   NOT NULL,
    trigger_group VARCHAR(200)   NOT NULL,
    str_prop_1    VARCHAR(512)   NULL,
    str_prop_2    VARCHAR(512)   NULL,
    str_prop_3    VARCHAR(512)   NULL,
    int_prop_1    INT            NULL,
    int_prop_2    INT            NULL,
    long_prop_1   BIGINT         NULL,
    long_prop_2   BIGINT         NULL,
    dec_prop_1    NUMERIC(13, 4) NULL,
    dec_prop_2    NUMERIC(13, 4) NULL,
    bool_prop_1   BOOL           NULL,
    bool_prop_2   BOOL           NULL,
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES
        qrtz_triggers (sched_name, trigger_name, trigger_group)  ON DELETE CASCADE
);

CREATE TABLE qrtz_blob_triggers
(
    sched_name    VARCHAR(120) NOT NULL,
    trigger_name  VARCHAR(200) NOT NULL,
    trigger_group VARCHAR(200) NOT NULL,
    blob_data     BYTEA        NULL,
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES
        qrtz_triggers (sched_name, trigger_name, trigger_group)  ON DELETE CASCADE
);

CREATE TABLE qrtz_calendars
(
    sched_name    VARCHAR(120) NOT NULL,
    calendar_name VARCHAR(200) NOT NULL,
    calendar      BYTEA        NOT NULL,
    PRIMARY KEY (sched_name, calendar_name)
);

CREATE TABLE qrtz_paused_trigger_grps
(
    sched_name    VARCHAR(120) NOT NULL,
    trigger_group VARCHAR(200) NOT NULL,
    PRIMARY KEY (sched_name, trigger_group)
);

CREATE TABLE qrtz_fired_triggers
(
    sched_name        VARCHAR(120) NOT NULL,
    entry_id          VARCHAR(95)  NOT NULL,
    trigger_name      VARCHAR(200) NOT NULL,
    trigger_group     VARCHAR(200) NOT NULL,
    instance_name     VARCHAR(200) NOT NULL,
    fired_time        BIGINT       NOT NULL,
    sched_time        BIGINT       NOT NULL,
    priority          INTEGER      NOT NULL,
    state             VARCHAR(16)  NOT NULL,
    job_name          VARCHAR(200) NULL,
    job_group         VARCHAR(200) NULL,
    is_nonconcurrent  BOOL         NULL,
    requests_recovery BOOL         NULL,
    PRIMARY KEY (sched_name, entry_id)
);

CREATE TABLE qrtz_scheduler_state
(
    sched_name        VARCHAR(120) NOT NULL,
    instance_name     VARCHAR(200) NOT NULL,
    last_checkin_time BIGINT       NOT NULL,
    checkin_interval  BIGINT       NOT NULL,
    PRIMARY KEY (sched_name, instance_name)
);

CREATE TABLE qrtz_locks
(
    sched_name VARCHAR(120) NOT NULL,
    lock_name  VARCHAR(40)  NOT NULL,
    PRIMARY KEY (sched_name, lock_name)
);

CREATE INDEX idx_qrtz_j_req_recovery
    ON qrtz_job_details (sched_name, requests_recovery);

CREATE INDEX idx_qrtz_j_grp
    ON qrtz_job_details (sched_name, job_group);

CREATE INDEX idx_qrtz_t_j
    ON qrtz_triggers (sched_name, job_name, job_group);

create index idx_qrtz_j_order
    on qrtz_job_details (order_id);

CREATE INDEX idx_qrtz_t_jg
    ON qrtz_triggers (sched_name, job_group);

CREATE INDEX idx_qrtz_t_c
    ON qrtz_triggers (sched_name, calendar_name);

CREATE INDEX idx_qrtz_t_g
    ON qrtz_triggers (sched_name, trigger_group);

CREATE INDEX idx_qrtz_t_state
    ON qrtz_triggers (sched_name, trigger_state);

CREATE INDEX idx_qrtz_t_n_state
    ON qrtz_triggers (sched_name, trigger_name, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_t_n_g_state
    ON qrtz_triggers (sched_name, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_t_next_fire_time
    ON qrtz_triggers (sched_name, next_fire_time);

CREATE INDEX idx_qrtz_t_nft_st
    ON qrtz_triggers (sched_name, trigger_state, next_fire_time);

CREATE INDEX idx_qrtz_t_nft_misfire
    ON qrtz_triggers (sched_name, misfire_instr, next_fire_time);

CREATE INDEX idx_qrtz_t_nft_st_misfire
    ON qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_state);

CREATE INDEX idx_qrtz_t_nft_st_misfire_grp
    ON qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_ft_trig_inst_name
    ON qrtz_fired_triggers (sched_name, instance_name);

CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry
    ON qrtz_fired_triggers (sched_name, instance_name, requests_recovery);

CREATE INDEX idx_qrtz_ft_j_g
    ON qrtz_fired_triggers (sched_name, job_name, job_group);

CREATE INDEX idx_qrtz_ft_jg
    ON qrtz_fired_triggers (sched_name, job_group);

CREATE INDEX idx_qrtz_ft_t_g
    ON qrtz_fired_triggers (sched_name, trigger_name, trigger_group);

CREATE INDEX idx_qrtz_ft_tg
    ON qrtz_fired_triggers (sched_name, trigger_group);

create table app_user
(
    user_id                    bigint       not null
        constraint app_user_pkey
            primary key,
    email                      varchar(150) not null
        constraint email_unique
            unique,
    is_account_non_locked      boolean      not null,
    is_credentials_non_expired boolean      not null,
    is_enabled                 boolean      not null,
    password                   varchar(150) not null,
    role                       varchar(25)  not null
);


create table email_template
(
    id          bigint       not null
        constraint email_template_pkey
            primary key,
    body        text         not null,
    subject     varchar(255) not null,
    app_user_id bigint       not null
        constraint app_user_id_ref
            references app_user
);

create table app_user_emails_info
(
    id                 bigint       not null
        constraint app_user_emails_info_pkey
            primary key,
    email_template_id  bigint       not null
        constraint email_template_id_ref
            references email_template,
    qrtz_job_detail_id varchar(200) null
        constraint qrtz_job_name_ref
            references qrtz_job_details ON DELETE SET NULL
);

create table app_recipient
(
    id             bigint       not null
        constraint app_recipient_pkey
            primary key,
    email          varchar(150) not null,
    recipient_type varchar(25)  not null,
    user_info_id   bigint       not null
        constraint user_info_id_ref
            references app_user_emails_info
);

create table confirmation_token
(
    token_id     bigint       not null
        constraint confirmation_token_pkey
            primary key,
    confirmed_at timestamp,
    created_at   timestamp    not null,
    expired_at   timestamp    not null,
    token        varchar(255) not null,
    app_user_id  bigint       not null
        constraint app_user_id_unique
            unique
        constraint app_user_id_ref
            references app_user
);

create table email_log
(
    id            bigint       not null
        constraint email_log_pkey
            primary key,
    email_status  varchar(50),
    log_date_time timestamp    not null,
    message       varchar(300),
    user_info_id  bigint       not null
        constraint user_info_id_ref
            references app_user_emails_info,
    sender_email  varchar(100) not null
);

create table user_email_template
(
    id           bigint       not null
        primary key,
    body         text         not null,
    name         varchar(255) not null,
    sharing_link varchar(255) not null
        constraint sharing_link_unique
            unique,
    subject      varchar(255) not null,
    app_user_id  bigint
        constraint app_user_ref
            references app_user
);
