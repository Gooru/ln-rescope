
-- These tables should be present in LearnerProfileBaselined Component as well

CREATE TABLE learner_profile_baselined (
    id bigserial NOT NULL PRIMARY KEY,
    tx_subject_code text NOT NULL,
    class_id text,
    course_id text NOT NULL,
    user_id text NOT NULL,
    gut_codes text[],
    lp_data text NOT NULL,
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL
);

CREATE UNIQUE INDEX lpb_ucsc_unique_idx ON learner_profile_baselined USING btree (user_id, course_id, tx_subject_code, class_id) WHERE (class_id IS NOT NULL);

CREATE UNIQUE INDEX lpb_ucs_unique_idx ON learner_profile_baselined USING btree (user_id, course_id, tx_subject_code) WHERE (class_id IS NULL);

alter table class add column grade_lower_bound bigint;
alter table class add column grade_upper_bound bigint;
