-- drop table user_rescoped_content

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

