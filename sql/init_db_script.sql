/*
SELECT id, export_file_path, export_type, tamplate_file_path, title, virtualaizer_type, virtualaizer_prop_id
	FROM public.reports;
SELECT id, virtualaizer_block_size, virtualaizer_compression_level, virtualaizer_files_path, virtualaizer_max_size, virtualaizer_min_grow_count
	FROM public.virtualaizer_properties;*/

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
    
INSERT INTO public.reports(
	id, export_file_path, export_type, tamplate_file_path, title, virtualaizer_type, virtualaizer_prop_id)
	VALUES (1, 'd:\users\kovtunenko_ve\reports\report1\export\report_1.xlsx', 'XLS', 'd:\users\kovtunenko_ve\reports\report1\template\report1.jasper', 'test_report1', 'NOT_USE', null);
INSERT INTO public.reports(
	id, export_file_path, export_type, tamplate_file_path, title, virtualaizer_type, virtualaizer_prop_id)
	VALUES (2, 'd:\users\kovtunenko_ve\reports\report2\export\report_2.xlsx', 'XLS', 'd:\users\kovtunenko_ve\reports\report2\template\report2.jasper', 'test_report2', 'SWAP', 1);
INSERT INTO public.reports(
	id, export_file_path, export_type, tamplate_file_path, title, virtualaizer_type, virtualaizer_prop_id)
	VALUES (3, 'd:\users\kovtunenko_ve\reports\report2\export\report_2.xlsx', 'XLS', 'd:\users\kovtunenko_ve\reports\report2\template\report2.jasper', 'test_report3', 'SWAP', 1);
INSERT INTO public.reports(
	id, export_file_path, export_type, tamplate_file_path, title, virtualaizer_type, virtualaizer_prop_id)
	VALUES (4, 'd:\users\kovtunenko_ve\reports\report2\export\report_2.xlsx', 'XLS', 'd:\users\kovtunenko_ve\reports\report2\template\report2.jasper', 'test_report3', 'SWAP', 1);
INSERT INTO public.reports (
    id, export_file_path, export_type, tamplate_file_path, title, virtualaizer_type, virtualaizer_prop_id)
    VALUES (5, 'D:\Users\kovtunenko_ve\reports\out\pos_terminals.xlsx','XLS','P:\OWSWork\Prod\Data\Jasper reports\Bfko.reports.jrxml.arm-udp-pos-terminals\pos_terminals_view.jasper','test_pos_terminals','NOT_USE',null);
INSERT INTO public.reports (
    id, export_file_path, export_type, tamplate_file_path, title, virtualaizer_type, virtualaizer_prop_id)
    VALUES (6, '/home/podmanusr/open_shares/prod/reports/testreports/report1.xlsx','XLS','/home/podmanusr/open_shares/prod/reports-template/report1/report1.jasper','test_report1','NOT_USE',null);
INSERT INTO public.reports (
    id, export_file_path, export_type, tamplate_file_path, title, virtualaizer_type, virtualaizer_prop_id)
    VALUES (7, '/home/reports/report1.xlsx','XLS','/home/reports-template/report1/report1.jasper','test_report1','NOT_USE',null);