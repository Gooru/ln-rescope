-- drop table user_rescoped_content
-- drop table rescope_queue

create table user_rescoped_content (
    id bigserial NOT NULL,
    user_id uuid NOT NULL,
    course_id uuid NOT NULL,
    class_id uuid,
    skipped_content jsonb NOT NULL,
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    CONSTRAINT urc_pkey PRIMARY KEY (id)
);

ALTER TABLE user_rescoped_content OWNER TO nucleus;

CREATE UNIQUE INDEX urc_ucc_null_unq_idx
    ON user_rescoped_content (user_id, course_id, class_id)
    where class_id is not null;

CREATE UNIQUE INDEX urc_ucc_unq_idx
    ON user_rescoped_content (user_id, course_id)
    where class_id is null;

COMMENT on table user_rescoped_content IS 'Store the rescoped content for user/course/class combination';

create table rescope_queue (
    id bigserial NOT NULL,
    user_id uuid NOT NULL,
    course_id uuid NOT NULL,
    class_id uuid,
    status int NOT NULL DEFAULT 0 CHECK (status::int = ANY (ARRAY[0::int, 1::int, 2::int])),
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    CONSTRAINT rq_pkey PRIMARY KEY (id)
);

ALTER TABLE rescope_queue OWNER TO nucleus;

CREATE UNIQUE INDEX rq_ucc_null_unq_idx
    ON rescope_queue (user_id, course_id, class_id)
    where class_id is not null;

CREATE UNIQUE INDEX rq_ucc_unq_idx
    ON rescope_queue (user_id, course_id)
    where class_id is null;

COMMENT on TABLE rescope_queue IS 'Persistent queue for rescope tasks';
COMMENT on COLUMN rescope_queue.status IS '0 means queued, 1 means dispatched for processing, 2 means in process'

