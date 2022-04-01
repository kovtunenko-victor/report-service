CREATE TABLE IF NOT EXISTS public.virtualaizer_properties
(
    id bigint NOT NULL,
    virtualaizer_block_size integer DEFAULT 1024,
    virtualaizer_compression_level integer DEFAULT 5,
    virtualaizer_files_path character varying(255) COLLATE pg_catalog."default",
    virtualaizer_max_size integer DEFAULT 200,
    virtualaizer_min_grow_count integer DEFAULT 100,
    CONSTRAINT virtualaizer_properties_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.virtualaizer_properties
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.reports
(
    id bigint NOT NULL,
    export_file_path character varying(255) COLLATE pg_catalog."default",
    export_type character varying(255) COLLATE pg_catalog."default",
    tamplate_file_path character varying(255) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default",
    virtualaizer_type character varying(255) COLLATE pg_catalog."default",
    virtualaizer_prop_id bigint,
    CONSTRAINT reports_pkey PRIMARY KEY (id),
    CONSTRAINT fk2ux1qvurq2i56j4md1ugybkch FOREIGN KEY (virtualaizer_prop_id)
        REFERENCES public.virtualaizer_properties (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.reports
    OWNER to postgres;

INSERT INTO public.virtualaizer_properties(
	id, virtualaizer_block_size, virtualaizer_compression_level, virtualaizer_files_path, virtualaizer_max_size, virtualaizer_min_grow_count)
	VALUES (1, 1024, 5, null, 200, 100);
INSERT INTO public.virtualaizer_properties(
	id, virtualaizer_block_size, virtualaizer_compression_level, virtualaizer_files_path, virtualaizer_max_size, virtualaizer_min_grow_count)
	VALUES (2, 1024, 5, 'c:\reports\virtualaizerData\', 200, 100);
 