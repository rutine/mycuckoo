--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: cuckoo; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA cuckoo;


ALTER SCHEMA cuckoo OWNER TO postgres;

--
-- Name: SCHEMA cuckoo; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA cuckoo IS 'standard public schema';


SET search_path = cuckoo, pg_catalog;

--
-- Name: create_time; Type: DOMAIN; Schema: cuckoo; Owner: postgres
--

CREATE DOMAIN create_time AS timestamp without time zone;


ALTER DOMAIN cuckoo.create_time OWNER TO postgres;

--
-- Name: creator; Type: DOMAIN; Schema: cuckoo; Owner: postgres
--

CREATE DOMAIN creator AS character varying(15);


ALTER DOMAIN cuckoo.creator OWNER TO postgres;

--
-- Name: memo; Type: DOMAIN; Schema: cuckoo; Owner: postgres
--

CREATE DOMAIN memo AS character varying(100);


ALTER DOMAIN cuckoo.memo OWNER TO postgres;

--
-- Name: status; Type: DOMAIN; Schema: cuckoo; Owner: postgres
--

CREATE DOMAIN status AS character varying(15);


ALTER DOMAIN cuckoo.status OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: portal; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE portal (
    portal_id bigint NOT NULL,
    portal_userid bigint,
    portlet1 character varying(20),
    portlet2 character varying(20),
    portlet3 character varying(20),
    portlet4 character varying(20),
    portlet5 character varying(20),
    portlet6 character varying(20),
    portlet7 character varying(20),
    portlet8 character varying(20),
    portlet9 character varying(20)
);


ALTER TABLE cuckoo.portal OWNER TO postgres;

--
-- Name: portal_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE portal_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.portal_seq OWNER TO postgres;

--
-- Name: syspl_accessory; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_accessory (
    accessory_id bigint NOT NULL,
    info_id bigint,
    accessory_name character varying(100)
);


ALTER TABLE cuckoo.syspl_accessory OWNER TO postgres;

--
-- Name: syspl_accessory_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_accessory_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_accessory_seq OWNER TO postgres;

--
-- Name: syspl_affiche; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_affiche (
    affiche_id bigint NOT NULL,
    affiche_title character varying(100),
    affiche_invalidate date,
    affiche_pulish smallint,
    affiche_content text
);


ALTER TABLE cuckoo.syspl_affiche OWNER TO postgres;

--
-- Name: TABLE syspl_affiche; Type: COMMENT; Schema: cuckoo; Owner: postgres
--

COMMENT ON TABLE syspl_affiche IS '系统公告';


--
-- Name: syspl_affiche_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_affiche_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_affiche_seq OWNER TO postgres;

--
-- Name: syspl_code; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_code (
    code_id bigint NOT NULL,
    code_eng_name character varying(20),
    code_name character varying(20),
    module_name character varying(20),
    delimite character varying(2),
    part_num integer,
    part1 character varying(20),
    part1_con character varying(20),
    part2 character varying(20),
    part2_con character varying(20),
    part3 character varying(20),
    part3_con character varying(20),
    part4 character varying(20),
    part4_con character varying(20),
    code_effect character varying(50),
    memo memo,
    status status NOT NULL,
    creator creator,
    create_date create_time
);


ALTER TABLE cuckoo.syspl_code OWNER TO postgres;

--
-- Name: syspl_code_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_code_seq
    START WITH 20
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_code_seq OWNER TO postgres;

--
-- Name: syspl_dic_big_type; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_dic_big_type (
    big_type_id bigint NOT NULL,
    big_type_name character varying(20),
    big_type_code character varying(15),
    memo memo,
    status status NOT NULL,
    creator creator,
    create_date create_time
);


ALTER TABLE cuckoo.syspl_dic_big_type OWNER TO postgres;

--
-- Name: syspl_dic_big_type_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_dic_big_type_seq
    START WITH 20
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_dic_big_type_seq OWNER TO postgres;

--
-- Name: syspl_dic_small_type; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_dic_small_type (
    small_type_id bigint NOT NULL,
    big_type_id bigint,
    small_type_name character varying(20),
    small_type_code character varying(15)
);


ALTER TABLE cuckoo.syspl_dic_small_type OWNER TO postgres;

--
-- Name: syspl_dic_small_type_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_dic_small_type_seq
    START WITH 20
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_dic_small_type_seq OWNER TO postgres;

--
-- Name: syspl_district; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_district (
    district_id bigint NOT NULL,
    district_name character varying(20),
    district_code character varying(20),
    district_postal character varying(6),
    district_telcode character varying(10),
    district_level character varying(20),
    dis_parent_id bigint,
    memo memo,
    status status NOT NULL,
    creator creator,
    create_date create_time
);


ALTER TABLE cuckoo.syspl_district OWNER TO postgres;

--
-- Name: syspl_district_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_district_seq
    START WITH 2000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_district_seq OWNER TO postgres;

--
-- Name: syspl_mod_opt_ref; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_mod_opt_ref (
    mod_opt_id bigint NOT NULL,
    module_id bigint,
    operate_id bigint
);


ALTER TABLE cuckoo.syspl_mod_opt_ref OWNER TO postgres;

--
-- Name: syspl_mod_opt_ref_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_mod_opt_ref_seq
    START WITH 150
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_mod_opt_ref_seq OWNER TO postgres;

--
-- Name: syspl_module_memu; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_module_memu (
    module_id bigint NOT NULL,
    mod_name character varying(10),
    mod_en_id character varying(40),
    mod_img_cls character varying(30),
    mod_level character varying(2),
    mod_order integer,
    mod_parent_id bigint,
    status status NOT NULL,
    memo memo,
    creator creator,
    create_date create_time,
    belong_to_sys character varying(15),
    mod_page_type character varying(20)
);


ALTER TABLE cuckoo.syspl_module_memu OWNER TO postgres;

--
-- Name: syspl_module_memu_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_module_memu_seq
    START WITH 60
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_module_memu_seq OWNER TO postgres;

--
-- Name: syspl_operate; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_operate (
    operate_id bigint NOT NULL,
    operate_name character varying(10),
    opt_img_link character varying(20),
    opt_order integer,
    opt_group integer,
    memo memo,
    status status NOT NULL,
    creator creator,
    create_date create_time,
    opt_fun_link character varying(20)
);


ALTER TABLE cuckoo.syspl_operate OWNER TO postgres;

--
-- Name: syspl_operate_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_operate_seq
    START WITH 25
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_operate_seq OWNER TO postgres;

--
-- Name: syspl_scheduler_job; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_scheduler_job (
    job_id bigint NOT NULL,
    job_name character varying(30),
    job_class_descript character varying(100),
    trigger_type character varying(20),
    time_express character varying(100),
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    repeat_time integer,
    split_time bigint,
    status status NOT NULL,
    memo memo,
    creator creator,
    create_date create_time
);


ALTER TABLE cuckoo.syspl_scheduler_job OWNER TO postgres;

--
-- Name: syspl_scheduler_job_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_scheduler_job_seq
    START WITH 20
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_scheduler_job_seq OWNER TO postgres;

--
-- Name: syspl_sys_opt_log; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_sys_opt_log (
    opt_id bigint NOT NULL,
    opt_mod_name character varying(20),
    opt_name character varying(10),
    opt_content character varying(1000),
    opt_business_id character varying(20),
    opt_time timestamp without time zone,
    opt_pc_name character varying(25),
    opt_pc_ip character varying(25),
    opt_user_name character varying(20),
    opt_user_role character varying(20),
    opt_user_ogan character varying(20)
);


ALTER TABLE cuckoo.syspl_sys_opt_log OWNER TO postgres;

--
-- Name: syspl_sys_opt_log_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_sys_opt_log_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_sys_opt_log_seq OWNER TO postgres;

--
-- Name: syspl_sys_parameter; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE syspl_sys_parameter (
    para_id bigint NOT NULL,
    para_name character varying(20),
    para_key_name character varying(20),
    para_value character varying(20),
    para_type character varying(20),
    memo memo,
    status status NOT NULL,
    creator creator,
    create_date create_time
);


ALTER TABLE cuckoo.syspl_sys_parameter OWNER TO postgres;

--
-- Name: COLUMN syspl_sys_parameter.para_id; Type: COMMENT; Schema: cuckoo; Owner: postgres
--

COMMENT ON COLUMN syspl_sys_parameter.para_id IS '主键';


--
-- Name: syspl_sys_parameter_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE syspl_sys_parameter_seq
    START WITH 20
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.syspl_sys_parameter_seq OWNER TO postgres;

--
-- Name: uum_org_role_ref; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE uum_org_role_ref (
    org_role_id bigint NOT NULL,
    org_id bigint,
    role_id bigint
);


ALTER TABLE cuckoo.uum_org_role_ref OWNER TO postgres;

--
-- Name: uum_org_role_ref_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE uum_org_role_ref_seq
    START WITH 10
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.uum_org_role_ref_seq OWNER TO postgres;

--
-- Name: uum_organ; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE uum_organ (
    org_id bigint NOT NULL,
    org_simple_name character varying(10),
    org_full_name character varying(60),
    org_code character varying(10),
    org_address1 character varying(100),
    org_address2 character varying(100),
    org_tel1 character varying(25),
    org_tel2 character varying(25),
    org_begin_date date,
    org_type character varying(20),
    org_fax character varying(20),
    org_postal character varying(6),
    org_legal character varying(20),
    org_tax_no character varying(25),
    org_reg_no character varying(25),
    org_belong_dist bigint,
    org_parent bigint,
    status status NOT NULL,
    memo memo,
    creator creator,
    create_date create_time
);


ALTER TABLE cuckoo.uum_organ OWNER TO postgres;

--
-- Name: uum_organ_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE uum_organ_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.uum_organ_seq OWNER TO postgres;

--
-- Name: uum_privilege; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE uum_privilege (
    privilege_id bigint NOT NULL,
    resource_id character varying(20),
    owner_id bigint,
    owner_type character varying(10),
    privilege_scope character varying(10),
    privilege_type character varying(10)
);


ALTER TABLE cuckoo.uum_privilege OWNER TO postgres;

--
-- Name: COLUMN uum_privilege.privilege_scope; Type: COMMENT; Schema: cuckoo; Owner: postgres
--

COMMENT ON COLUMN uum_privilege.privilege_scope IS '针对操作
inc  包含
exc 不包含
all   全部
针对行
org 机构
rol  角色
usr 用户
';


--
-- Name: COLUMN uum_privilege.privilege_type; Type: COMMENT; Schema: cuckoo; Owner: postgres
--

COMMENT ON COLUMN uum_privilege.privilege_type IS 'row  行
opt   操作';


--
-- Name: uum_privilege_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE uum_privilege_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.uum_privilege_seq OWNER TO postgres;

--
-- Name: uum_role; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE uum_role (
    role_id bigint NOT NULL,
    role_name character varying(10),
    status status NOT NULL,
    memo memo,
    creator creator,
    create_date create_time,
    role_level smallint
);


ALTER TABLE cuckoo.uum_role OWNER TO postgres;

--
-- Name: uum_role_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE uum_role_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.uum_role_seq OWNER TO postgres;

--
-- Name: uum_role_user_ref; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE uum_role_user_ref (
    org_role_user_id bigint NOT NULL,
    user_id bigint,
    org_role_id bigint,
    is_default character varying(2)
);


ALTER TABLE cuckoo.uum_role_user_ref OWNER TO postgres;

--
-- Name: uum_role_user_ref_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE uum_role_user_ref_seq
    START WITH 10
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.uum_role_user_ref_seq OWNER TO postgres;

--
-- Name: uum_user; Type: TABLE; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE TABLE uum_user (
    user_id bigint NOT NULL,
    user_code character varying(20),
    user_name character varying(20),
    user_password character varying(20),
    user_gender character varying(2),
    user_position character varying(30),
    user_photo_url character varying(200),
    user_qq character varying(20),
    user_msn character varying(20),
    user_mobile character varying(20),
    user_mobile2 character varying(20),
    user_office_tel character varying(20),
    user_family_tel character varying(20),
    user_email character varying(30),
    user_avidate date,
    user_is_agent character varying(2),
    user_belongto_org bigint,
    memo memo,
    status status NOT NULL,
    creator creator,
    create_date create_time,
    user_address character varying(100),
    user_name_py character varying(20)
);


ALTER TABLE cuckoo.uum_user OWNER TO postgres;

--
-- Name: uum_user_seq; Type: SEQUENCE; Schema: cuckoo; Owner: postgres
--

CREATE SEQUENCE uum_user_seq
    START WITH 10
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuckoo.uum_user_seq OWNER TO postgres;

--
-- Data for Name: portal; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY portal (portal_id, portal_userid, portlet1, portlet2, portlet3, portlet4, portlet5, portlet6, portlet7, portlet8, portlet9) FROM stdin;
1	1	门户1	门户2	门户3	门户4	\N	\N	\N	\N	\N
2	1	门户1	门户2	门户3	门户4	\N	\N	\N	\N	\N
\.


--
-- Name: portal_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('portal_seq', 3, true);


--
-- Data for Name: syspl_accessory; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_accessory (accessory_id, info_id, accessory_name) FROM stdin;
10	6	tbTnr1_1_jacbo.jpg
2	2	cRopIJ_blog_fifth.bmp
3	2	jba458_blog_second.bmp
4	2	b6wewK_blog_second.bmp
7	3	Ihwp1f_blog_fourth.bmp
\.


--
-- Name: syspl_accessory_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_accessory_seq', 42, true);


--
-- Data for Name: syspl_affiche; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_affiche (affiche_id, affiche_title, affiche_invalidate, affiche_pulish, affiche_content) FROM stdin;
12	2.0版本	2017-11-30	0	测试数据
6	第二版本发布了！	2017-05-01	0	<u>​啊啊啊啊啊啊啊啊</u>
\.


--
-- Name: syspl_affiche_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_affiche_seq', 13, true);


--
-- Data for Name: syspl_code; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_code (code_id, code_eng_name, code_name, module_name, delimite, part_num, part1, part1_con, part2, part2_con, part3, part3_con, part4, part4_con, code_effect, memo, status, creator, create_date) FROM stdin;
25	RKD	入库单	用户信息管理	:	4	char	RKD	date	yyyyMM	number	001	sysPara	userName	RKD:201709:001:zhangsan		enable	admin	2012-01-08 16:40:01
\.


--
-- Name: syspl_code_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_code_seq', 26, true);


--
-- Data for Name: syspl_dic_big_type; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_dic_big_type (big_type_id, big_type_name, big_type_code, memo, status, creator, create_date) FROM stdin;
23	机构类型	organType		enable	admin	2011-12-21 09:33:12
21	系统类别	systemType		enable	admin	2011-12-20 14:32:22
22	地区级别	district		enable	admin	2011-12-20 15:11:29.519
25	页面类型	modPageType		enable	admin	2012-01-13 12:09:36.718
\.


--
-- Name: syspl_dic_big_type_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_dic_big_type_seq', 28, true);


--
-- Data for Name: syspl_dic_small_type; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_dic_small_type (small_type_id, big_type_id, small_type_name, small_type_code) FROM stdin;
47	23	分公司	branch
48	23	门店	store
49	23	部门	department
37	21	系统平台	platform
38	21	统一用户	uum
50	22	省	province
51	22	市	city
52	22	县	town
53	22	区	district
60	25	js	js
61	25	html	html
62	25	flex	flex
63	25	jsp	jsp
\.


--
-- Name: syspl_dic_small_type_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_dic_small_type_seq', 63, true);


--
-- Data for Name: syspl_district; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_district (district_id, district_name, district_code, district_postal, district_telcode, district_level, dis_parent_id, memo, status, creator, create_date) FROM stdin;
0	地区树	0	\N	\N	\N	0	\N	enable	\N	\N
2003	上海				province	0		enable	admin	2012-03-06 14:38:46.359
2004	天津				city	0		enable	admin	2012-03-06 14:39:21.687
2005	深圳				city	0		enable	admin	2012-03-06 14:39:37.218
2002	海淀				province	2000		enable	admin	2012-01-06 16:11:32
2001	北海				province	2000		enable	admin	2011-12-26 09:55:00.109
2006	广州	6008	020	020	province	0		enable	admin	2014-10-18 11:40:29.141
2000	北京	beijing			province	0		enable	admin	2011-12-20 15:12:04.16
\.


--
-- Name: syspl_district_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_district_seq', 2011, true);


--
-- Data for Name: syspl_mod_opt_ref; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_mod_opt_ref (mod_opt_id, module_id, operate_id) FROM stdin;
5	7	1
6	7	2
7	7	4
10	8	1
11	8	2
12	8	4
13	8	5
14	8	6
15	10	1
16	10	2
17	10	4
18	10	5
19	10	6
25	13	1
26	13	2
27	13	4
28	13	5
29	13	6
30	14	1
31	14	2
33	14	4
34	14	5
35	14	6
36	15	1
37	15	2
38	15	4
39	15	5
40	15	6
44	18	4
45	32	1
46	32	2
47	32	4
48	32	5
49	32	6
50	33	1
51	33	2
52	33	4
53	33	5
54	33	6
55	33	8
56	35	9
57	35	10
58	34	1
59	34	2
60	34	4
61	34	5
62	34	6
155	7	14
87	7	7
91	34	11
152	34	28
97	34	13
98	49	1
99	49	2
100	49	3
101	49	4
104	53	1
105	53	2
106	53	3
107	53	4
108	53	16
109	53	17
110	53	18
111	53	19
156	7	3
\.


--
-- Name: syspl_mod_opt_ref_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_mod_opt_ref_seq', 163, true);


--
-- Data for Name: syspl_module_memu; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_module_memu (module_id, mod_name, mod_en_id, mod_img_cls, mod_level, mod_order, mod_parent_id, status, memo, creator, create_date, belong_to_sys, mod_page_type) FROM stdin;
52	省市地区	districtMgr	district-mgr-title-icon	2	3	1	enable		admin	2011-01-17 10:16:14	platform	jsp
17	日志管理	logMgr	log-mgr-icon	2	5	1	enable		admin	2010-08-20 14:38:46	platform	jsp
14	省市地区管理	districtMgr	district-mgr-icon	3	1	52	enable		admin	2010-08-20 14:34:52	platform	jsp
13	系统编码管理	codeMgr	code-mgr-icon	3	2	12	enable		admin	2010-08-20 14:33:31	platform	jsp
10	类别字典管理	typeDictionaryMgr	type-dictionary-mgr-icon	3	1	9	enable		admin	2010-08-20 14:30:13	platform	jsp
2	统一用户	uum	uum-icon	1	2	0	enable		admin	2010-08-20 13:44:29	uum	jsp
1	系统平台	platform	platform-icon	1	1	0	enable		admin	2010-08-20 13:43:34	platform	jsp
9	字典管理	dictionaryMgr	dictionary-mgr-icon	2	4	1	enable		admin	2010-08-20 14:27:12	platform	jsp
53	系统调度管理	schedulerMgr	scheduler-mgr-icon	3	5	12	enable		admin	2011-01-17 10:34:43	platform	jsp
12	系统管理	systemMgr	system-mgr-icon	2	1	1	enable		admin	2010-08-20 14:32:28	platform	jsp
49	系统公告管理	afficheMgr	affiche-mgr-icon	3	4	12	enable		admin	2010-12-01 10:47:27	platform	jsp
6	菜单管理	moduleOptMgr	module-operate-icon	2	3	1	enable		admin	2010-08-20 14:20:15	platform	jsp
35	角色分配管理	roleAssignMgr	role-assign-mgr-icon	3	1	21	enable		admin	2010-09-14 10:29:14	uum	jsp
8	操作按钮管理	operateMgr	operate-mgr-icon	3	2	6	enable		admin	2010-08-20 14:22:45	platform	jsp
7	操作菜单管理	moduleMgr	module-mgr-icon	3	1	6	enable		admin	2010-08-20 14:21:50	platform	jsp
34	用户信息管理	userMgr	user-mgr-icon	3	1	22	enable		admin	2010-09-13 21:16:03	uum	jsp
33	角色信息管理	roleMgr	role-mgr-icon	3	1	21	enable		admin	2010-09-13 21:15:10	uum	jsp
32	机构信息管理	organMgr	organ-mgr-icon	3	1	19	enable		admin	2010-09-13 21:14:44	uum	jsp
18	系统日志管理	systemLogMgr	system-log-mgr-icon	3	1	17	enable		admin	2010-08-20 14:39:33	platform	jsp
25	系统配置管理	systemConfigMgr	system-config-mgr-icon	3	1	12	enable		admin	2010-09-13 21:09:13	platform	jsp
15	系统参数管理	systemParameterMgr	system-parameter-mgr-icon	3	3	12	enable		admin	2010-08-20 14:35:39	platform	jsp
0	模块树	moduleTree	module-tree-icon	0	1	0	enable		admin	2010-07-27 09:29:36	\N	\N
22	用户管理	userMgr	user-mgr-title-icon	2	3	2	enable		admin	2010-09-13 21:05:20	uum	jsp
21	角色管理	roleMgr	role-mgr-title-icon	2	2	2	enable		admin	2010-09-13 21:04:30	uum	jsp
19	机构管理	organMgrTitle	organ-mgr-title-icon	2	1	2	enable		admin	2010-08-21 13:33:29	uum	jsp
\.


--
-- Name: syspl_module_memu_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_module_memu_seq', 65, true);


--
-- Data for Name: syspl_operate; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_operate (operate_id, operate_name, opt_img_link, opt_order, opt_group, memo, status, creator, create_date, opt_fun_link) FROM stdin;
1	增加	table-add-icon	1	1	增加	enable	admin	2010-10-28 15:16:25	add
28	特殊权限分配	assign-icon	8	1		enable	admin	2012-02-08 11:16:26	optpri
19	停止任务	job-stop-icon	8	1		enable	admin	2011-01-17 15:05:07	jobStop
18	启动任务	job-start-icon	7	1		enable	admin	2011-01-17 15:01:19	jobStart
17	停止调度器	scheduler-stop-icon	6	1		enable	admin	2011-01-17 14:20:54	schedulerStop
16	启动调度器	scheduler-start-icon	5	1		enable	admin	2011-01-17 14:20:06	schedulerStart
15	保留时间	save-time-icon	5	1		enable	admin	2010-12-30 16:05:31	saveTime
12	行权限	privilege-mgr-icon	8	1		enable	admin	2010-11-07 14:01:16	rowprivilege
11	分配角色	role-mgr-icon	6	1		enable	admin	2010-11-02 11:46:31	assignrole
10	角色删除	table-delete-icon	7	1		enable	admin	2010-11-02 11:44:39	roledel
9	角色分配	assign-icon	6	1		enable	admin	2010-11-02 11:44:10	roleassign
8	普通权限分配	assign-icon	8	1		enable	admin	2010-11-02 11:42:42	optpri
7	模块分配操作	assign-icon	7	1		enable	admin	2010-11-01 20:07:40	optassign
6	停用	disable-icon	6	1	停用	enable	admin	2010-10-29 09:02:11	disable
5	启用	enable-icon	5	1		enable	admin	2010-10-29 09:01:42.734	enable
4	查看	table-icon	4	1		enable	admin	2010-10-28 19:07:11	view
3	删除	table-delete-icon	3	1		enable	admin	2010-10-28 15:30:29	delete
2	修改	table-edit-icon	2	1		enable	admin	2010-10-28 15:27:36	modify
13	重置密码	reset-pwd-icon	7	1	重置密码	enable	admin	2010-11-07 18:14:23	resetpwd
14	模块标签修改	module-label-icon	8	1		enable	admin	2010-12-26 10:23:29	moduleLabel
\.


--
-- Name: syspl_operate_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_operate_seq', 29, true);


--
-- Data for Name: syspl_scheduler_job; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_scheduler_job (job_id, job_name, job_class_descript, trigger_type, time_express, start_time, end_time, repeat_time, split_time, status, memo, creator, create_date) FROM stdin;
1	日志清理	com.mycuckoo.service.impl.platform.job.LoggerJob	Cron	* * 0 15 * ?	2011-01-19 00:00:00	2011-02-19 00:00:00	2	2	disable	每月15日0点清除日志	admin	2011-01-19 15:28:44
\.


--
-- Name: syspl_scheduler_job_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_scheduler_job_seq', 26, true);


--
-- Data for Name: syspl_sys_opt_log; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_sys_opt_log (opt_id, opt_mod_name, opt_name, opt_content, opt_business_id, opt_time, opt_pc_name, opt_pc_ip, opt_user_name, opt_user_role, opt_user_ogan) FROM stdin;
3	用户登录	用户登录	总部-管理员-管理员		2011-12-20 11:36:05.457	localhost	127.0.0.1	管理员	管理员	总部
4	角色管理	保存	经理;	3	2011-12-20 11:37:31.801	localhost	127.0.0.1	管理员	管理员	总部
5	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;64;91;97;65;66;68;69;116;117;118;119;112;113;114;115;	3;rolePri	2011-12-20 11:38:02.066	localhost	127.0.0.1	管理员	管理员	总部
6	用户登录	用户登录	总部-管理员-管理员		2011-12-20 11:50:35.176	localhost	127.0.0.1	管理员	管理员	总部
7	类别字典管理	保存	字典大类名称：a;	20	2011-12-20 11:51:28.488	localhost	127.0.0.1	管理员	管理员	总部
8	用户登录	用户登录	总部-管理员-管理员		2011-12-20 13:15:26.723	localhost	127.0.0.1	管理员	管理员	总部
9	系统公告管理	保存	保存公告标题：aa;有效期限:Tue Dec 20 00:00:00 CST 2011	1	2011-12-20 13:16:15.691	localhost	127.0.0.1	管理员	管理员	总部
10	系统附件	保存	附件业务表ID：1;附件名称:4M504g_blog_second.bmp	1	2011-12-20 13:16:46.848	localhost	127.0.0.1	管理员	管理员	总部
11	用户登录	用户登录	总部-管理员-管理员		2011-12-20 13:17:28.816	localhost	127.0.0.1	管理员	管理员	总部
12	用户登录	用户登录	总部-管理员-管理员		2011-12-20 13:37:42.144	localhost	127.0.0.1	管理员	管理员	总部
13	系统公告管理	修改	修改公告ID：1;修改公告标题：aaab;有效期：Wed Dec 21 00:00:00 CST 2011;是否发布：0;	1	2011-12-20 13:43:07.035	localhost	127.0.0.1	管理员	管理员	总部
14	用户登录	用户登录	总部-管理员-管理员		2011-12-20 13:55:28.801	localhost	127.0.0.1	管理员	管理员	总部
15	系统模块操作管理	保存	分配;a;aaa;	25	2011-12-20 13:56:07.41	localhost	127.0.0.1	管理员	管理员	总部
16	系统模块操作管理	修改	分配;a;aaa;	25	2011-12-20 13:56:20.176	localhost	127.0.0.1	管理员	管理员	总部
17	系统模块操作管理	修改	分配;a;aaa;	25	2011-12-20 13:56:24.801	localhost	127.0.0.1	管理员	管理员	总部
18	系统模块管理	保存	测试;abc;	60	2011-12-20 13:56:54.926	localhost	127.0.0.1	管理员	管理员	总部
19	系统模块管理	修改	测试;abc;	60	2011-12-20 13:57:05.441	localhost	127.0.0.1	管理员	管理员	总部
20	模块分配操作	保存	模块分配操作;25	60	2011-12-20 13:57:13.91	localhost	127.0.0.1	管理员	管理员	总部
21	用户登录	用户登录	总部-管理员-管理员		2011-12-20 13:57:21.019	localhost	127.0.0.1	管理员	管理员	总部
22	模块分配操作	保存	模块分配操作;1	60	2011-12-20 13:58:09.457	localhost	127.0.0.1	管理员	管理员	总部
23	用户登录	用户登录	总部-管理员-管理员		2011-12-20 13:58:15.035	localhost	127.0.0.1	管理员	管理员	总部
24	类别字典管理	修改	字典大类名称：a;	20	2011-12-20 13:59:32.723	localhost	127.0.0.1	管理员	管理员	总部
25	类别字典管理	保存	字典大类名称：系统类别;	21	2011-12-20 14:32:23.066	localhost	127.0.0.1	管理员	管理员	总部
26	系统参数管理	保存	参数名称：用户默认密码;参数键值：userdefaultpassword;参数值：123456参数类型：plt	20	2011-12-20 14:48:25.363	localhost	127.0.0.1	管理员	管理员	总部
27	系统参数管理	保存	参数名称：用户有效期;参数键值：uservalidate;参数值：6参数类型：uus	21	2011-12-20 14:48:55.488	localhost	127.0.0.1	管理员	管理员	总部
28	系统参数管理	修改	参数名称：用户默认密码;参数键值：userdefaultpassword;参数值：123456参数类型：uus	20	2011-12-20 14:49:09.457	localhost	127.0.0.1	管理员	管理员	总部
29	类别字典管理	保存	字典大类名称：地区级别;	22	2011-12-20 15:11:29.535	localhost	127.0.0.1	管理员	管理员	总部
30	省市地区管理	保存	北京;province;	2000	2011-12-20 15:12:04.176	localhost	127.0.0.1	管理员	管理员	总部
31	主页面功能区	删除	删除功能区的id:;		2011-12-20 15:15:27.066	localhost	127.0.0.1	管理员	管理员	总部
32	主页面功能区	保存	增加的功能区信息 id:30;funName:header;funURI:general;funMemo:;	30	2011-12-20 15:15:27.082	localhost	127.0.0.1	管理员	管理员	总部
33	主页面功能区	删除	删除功能区的id:30;		2011-12-20 15:15:33.754	localhost	127.0.0.1	管理员	管理员	总部
34	主页面功能区	删除	删除功能区的id:;		2011-12-20 15:15:38.129	localhost	127.0.0.1	管理员	管理员	总部
35	主页面功能区	保存	增加的功能区信息 id:30;funName:header;funURI:general;funMemo:;	30	2011-12-20 15:15:38.144	localhost	127.0.0.1	管理员	管理员	总部
36	主页面功能区	删除	删除功能区的id:30;		2011-12-20 15:15:44.41	localhost	127.0.0.1	管理员	管理员	总部
37	主页面功能区	保存	增加的功能区信息 id:30;funName:header;funURI:general;funMemo:aa;	30	2011-12-20 15:15:44.426	localhost	127.0.0.1	管理员	管理员	总部
38	主页面功能区	删除	删除功能区的id:30;		2011-12-20 15:15:54.051	localhost	127.0.0.1	管理员	管理员	总部
39	系统配置管理	保存	设置系统名称:my协同办公管理系统;		2011-12-20 15:16:04.191	localhost	127.0.0.1	管理员	管理员	总部
40	用户登录	用户登录	总部-管理员-管理员		2011-12-20 15:16:12.676	localhost	127.0.0.1	管理员	管理员	总部
41	用户登录	用户登录	总部-管理员-管理员		2011-12-20 15:22:08.551	localhost	127.0.0.1	管理员	管理员	总部
42	用户登录	用户登录	总部-管理员-管理员		2011-12-20 15:29:16.879	localhost	127.0.0.1	管理员	管理员	总部
43	用户登录	用户登录	总部-管理员-管理员		2011-12-20 17:25:37.035	localhost	127.0.0.1	管理员	管理员	总部
44	用户登录	用户登录	总部-管理员-管理员		2011-12-20 17:32:00.363	localhost	127.0.0.1	管理员	管理员	总部
45	用户登录	用户登录	总部-管理员-管理员		2011-12-20 17:32:48.957	localhost	127.0.0.1	管理员	管理员	总部
46	系统配置管理	保存	设置系统名称:my协同办公管理系统;		2011-12-20 17:34:21.613	localhost	127.0.0.1	管理员	管理员	总部
47	用户登录	用户登录	总部-管理员-管理员		2011-12-20 17:34:28.879	localhost	127.0.0.1	管理员	管理员	总部
48	用户登录	用户登录	总部-管理员-管理员		2011-12-21 09:29:41.859	localhost	127.0.0.1	管理员	管理员	总部
49	类别字典管理	保存	字典大类名称：机构类型;	23	2011-12-21 09:33:12.593	localhost	127.0.0.1	管理员	管理员	总部
50	机构管理	保存	机构名称：河北;机构代码：;上级机构：总部;	1	2011-12-21 09:33:51.062	localhost	127.0.0.1	管理员	管理员	总部
51	机构管理	修改	机构名称：河北;机构代码：HBS;上级机构：总部;	1	2011-12-21 09:34:35.171	localhost	127.0.0.1	管理员	管理员	总部
52	角色管理	修改	经理在;	3	2011-12-21 09:35:02.078	localhost	127.0.0.1	管理员	管理员	总部
53	角色分配	保存	机构id:1;角色id:1;		2011-12-21 09:35:45.375	localhost	127.0.0.1	管理员	管理员	总部
54	角色分配	删除	删除机构下的角色ID：1;	1	2011-12-21 09:35:56.703	localhost	127.0.0.1	管理员	管理员	总部
55	角色分配	保存	机构id:1;角色id:3;		2011-12-21 09:36:04.5	localhost	127.0.0.1	管理员	管理员	总部
56	用户分配角色	保存	11	12	2011-12-21 10:09:20.593	localhost	127.0.0.1	管理员	管理员	总部
57	用户管理	保存	jacob;jacob;经理在;	12	2011-12-21 10:09:20.609	localhost	127.0.0.1	管理员	管理员	总部
58	用户管理	修改	jacob;jacob;null;	12	2011-12-21 10:10:02.265	localhost	127.0.0.1	管理员	管理员	总部
59	用户分配角色	保存	11,1	12	2011-12-21 10:10:27.109	localhost	127.0.0.1	管理员	管理员	总部
60	用户分配角色	保存	11,1	12	2011-12-21 10:10:38.078	localhost	127.0.0.1	管理员	管理员	总部
61	用户分配角色	保存	11,1	12	2011-12-21 10:10:45.531	localhost	127.0.0.1	管理员	管理员	总部
62	用户管理	重置密码	重置密码用户：jacob	12	2011-12-21 10:10:59.734	localhost	127.0.0.1	管理员	管理员	总部
63	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:11:24.046	localhost	127.0.0.1	管理员	管理员	总部
64	用户登录	用户登录	河北-经理在-jacob		2011-12-21 10:12:08.39	localhost	127.0.0.1	jacob	经理在	河北
65	模块分配操作	保存	模块操作关系ID:	3;rolePri	2011-12-21 10:13:29.812	localhost	127.0.0.1	jacob	经理在	河北
66	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;	3;rolePri	2011-12-21 10:13:50.281	localhost	127.0.0.1	jacob	经理在	河北
67	用户分配角色	保存	11	12	2011-12-21 10:14:09.015	localhost	127.0.0.1	jacob	经理在	河北
68	用户登录	用户登录	河北-经理在-jacob		2011-12-21 10:14:23.468	localhost	127.0.0.1	jacob	经理在	河北
69	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:17:05.062	localhost	127.0.0.1	管理员	管理员	总部
70	用户登录	用户登录	河北-经理在-jacob		2011-12-21 10:17:47.359	localhost	127.0.0.1	jacob	经理在	河北
71	用户登录	用户登录	河北-经理在-jacob		2011-12-21 10:18:32	localhost	127.0.0.1	jacob	经理在	河北
72	用户常用功能设置	保存	用户常用功能保存:用户ID12;模块ID：32		2011-12-21 10:18:54.296	localhost	127.0.0.1	jacob	经理在	河北
73	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:19:25.812	localhost	127.0.0.1	管理员	管理员	总部
74	用户(普通)组管理	保存	组名称：员工组;组类型：userGroup;	1	2011-12-21 10:20:29.703	localhost	127.0.0.1	管理员	管理员	总部
75	用户(普通)组管理	修改	组名称：员工组1;组类型：userGroup;	1	2011-12-21 10:20:50.078	localhost	127.0.0.1	管理员	管理员	总部
76	角色组管理	保存	组名称：角色组;组类型：roleGroup;	2	2011-12-21 10:21:13.421	localhost	127.0.0.1	管理员	管理员	总部
77	用户(普通)组管理	保存	组名称：普通1;组类型：generalGroup;	3	2011-12-21 10:22:08.578	localhost	127.0.0.1	管理员	管理员	总部
78	用户分配角色	保存	0	12	2011-12-21 10:23:30.234	localhost	127.0.0.1	管理员	管理员	总部
79	用户管理	修改	jacob;jacob;经理在;	12	2011-12-21 10:23:30.281	localhost	127.0.0.1	管理员	管理员	总部
80	用户管理	停用	jacob;jacob;经理在;	12	2011-12-21 10:23:30.281	localhost	127.0.0.1	管理员	管理员	总部
81	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:24:37.843	localhost	127.0.0.1	管理员	管理员	总部
82	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：25,53,4,7		2011-12-21 10:28:02.906	localhost	127.0.0.1	管理员	管理员	总部
83	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:28:54.609	localhost	127.0.0.1	管理员	管理员	总部
84	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:35:08.75	localhost	127.0.0.1	管理员	管理员	总部
85	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:40:40.25	localhost	127.0.0.1	管理员	管理员	总部
86	系统模块管理	修改	工作流;workflowId;	50	2011-12-21 10:42:18.64	localhost	127.0.0.1	管理员	管理员	总部
87	系统模块管理	修改	测试;test;	60	2011-12-21 10:42:36	localhost	127.0.0.1	管理员	管理员	总部
88	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:42:52.578	localhost	127.0.0.1	管理员	管理员	总部
89	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:43:22.75	localhost	127.0.0.1	管理员	管理员	总部
90	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:46:38.89	localhost	127.0.0.1	管理员	管理员	总部
91	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:49:13.187	localhost	127.0.0.1	管理员	管理员	总部
92	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:53:56.421	localhost	127.0.0.1	管理员	管理员	总部
93	系统模块管理	修改	测试;test;	60	2011-12-21 10:54:54.89	localhost	127.0.0.1	管理员	管理员	总部
94	用户登录	用户登录	总部-管理员-管理员		2011-12-21 10:55:07.078	localhost	127.0.0.1	管理员	管理员	总部
95	系统模块管理	保存	待办任务;underwayTaskId;	61	2011-12-21 11:02:50.296	localhost	127.0.0.1	管理员	管理员	总部
96	用户登录	用户登录	总部-管理员-管理员		2011-12-21 11:04:07.562	localhost	127.0.0.1	管理员	管理员	总部
97	用户登录	用户登录	总部-管理员-管理员		2011-12-21 11:31:50.281	localhost	127.0.0.1	管理员	管理员	总部
98	用户登录	用户登录	总部-管理员-管理员		2011-12-21 11:33:20.437	localhost	127.0.0.1	管理员	管理员	总部
99	用户登录	用户登录	总部-管理员-管理员		2011-12-21 11:34:22.453	localhost	127.0.0.1	管理员	管理员	总部
100	用户登录	用户登录	总部-管理员-管理员		2011-12-21 13:48:59.218	localhost	127.0.0.1	管理员	管理员	总部
101	用户登录	用户登录	总部-管理员-管理员		2011-12-21 13:50:02.64	localhost	127.0.0.1	管理员	管理员	总部
102	用户登录	用户登录	总部-管理员-管理员		2011-12-21 13:53:46.453	localhost	127.0.0.1	管理员	管理员	总部
103	用户登录	用户登录	总部-管理员-管理员		2011-12-21 13:56:26.109	localhost	127.0.0.1	管理员	管理员	总部
104	用户登录	用户登录	总部-管理员-管理员		2011-12-21 14:22:48.171	localhost	127.0.0.1	管理员	管理员	总部
105	用户登录	用户登录	总部-管理员-管理员		2011-12-21 14:24:30.234	localhost	127.0.0.1	管理员	管理员	总部
106	用户登录	用户登录	总部-管理员-管理员		2011-12-21 14:52:21	localhost	127.0.0.1	管理员	管理员	总部
107	用户登录	用户登录	总部-管理员-管理员		2011-12-21 14:53:15.703	localhost	127.0.0.1	管理员	管理员	总部
108	用户登录	用户登录	总部-管理员-管理员		2011-12-21 14:54:37.156	localhost	127.0.0.1	管理员	管理员	总部
260	系统调度管理	启动job	启动job:;jobName:postgresqlBbBackup;job类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;	3	2011-12-28 09:40:36.656	localhost	127.0.0.1	管理员	管理员	总部
261	系统调度管理	停止job	停止job:postgresqlBbBackup		2011-12-28 09:40:56.406	localhost	127.0.0.1	管理员	管理员	总部
265	用户登录	用户登录	总部-管理员-管理员		2011-12-28 10:34:18.015	localhost	127.0.0.1	管理员	管理员	总部
272	用户登录	用户登录	总部-管理员-管理员		2011-12-28 11:55:13.046	localhost	127.0.0.1	管理员	管理员	总部
273	用户登录	用户登录	总部-管理员-管理员		2011-12-28 12:03:27.39	localhost	127.0.0.1	管理员	管理员	总部
278	用户管理	修改	jacob;jacob;null;	12	2011-12-28 13:55:13.156	localhost	127.0.0.1	管理员	管理员	总部
286	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:31:02.171	localhost	127.0.0.1	管理员	管理员	总部
291	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:40:02.828	localhost	127.0.0.1	管理员	管理员	总部
292	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:41:05.421	localhost	127.0.0.1	管理员	管理员	总部
294	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:45:13.031	localhost	127.0.0.1	管理员	管理员	总部
299	用户管理	修改	jacob;jacob;null;	12	2011-12-28 14:52:48.718	localhost	127.0.0.1	管理员	管理员	总部
306	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:15:20.359	localhost	127.0.0.1	管理员	管理员	总部
308	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:16:56.906	localhost	127.0.0.1	管理员	管理员	总部
310	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:18:46.296	localhost	127.0.0.1	管理员	管理员	总部
315	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:26:50.218	localhost	127.0.0.1	管理员	管理员	总部
318	用户登录	用户登录	总部-管理员-管理员		2011-12-28 16:29:18.937	localhost	127.0.0.1	管理员	管理员	总部
322	用户登录	用户登录	总部-管理员-管理员		2011-12-28 17:31:00.312	localhost	127.0.0.1	管理员	管理员	总部
326	主页面功能区	删除	删除功能区的id:;		2011-12-28 17:35:01.781	localhost	127.0.0.1	管理员	管理员	总部
329	主页面功能区	删除	删除功能区的id:21;		2011-12-28 17:37:21.609	localhost	127.0.0.1	管理员	管理员	总部
333	主页面功能区	删除	删除功能区的id:25;		2011-12-28 17:42:15.218	localhost	127.0.0.1	管理员	管理员	总部
338	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:18:22.765	localhost	127.0.0.1	管理员	管理员	总部
341	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:27:26.171	localhost	127.0.0.1	管理员	管理员	总部
344	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:30:21.625	localhost	127.0.0.1	管理员	管理员	总部
346	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:31:42.968	localhost	127.0.0.1	管理员	管理员	总部
347	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:49:19.75	localhost	127.0.0.1	管理员	管理员	总部
349	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:51:10.546	localhost	127.0.0.1	管理员	管理员	总部
352	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:53:39.156	localhost	127.0.0.1	管理员	管理员	总部
354	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:57:09.625	localhost	127.0.0.1	管理员	管理员	总部
355	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:02:13.046	localhost	127.0.0.1	管理员	管理员	总部
357	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:03:01.265	localhost	127.0.0.1	管理员	管理员	总部
360	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:03:45.062	localhost	127.0.0.1	管理员	管理员	总部
362	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:04:10.875	localhost	127.0.0.1	管理员	管理员	总部
365	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:12:50.859	localhost	127.0.0.1	管理员	管理员	总部
368	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:21:29.609	localhost	127.0.0.1	管理员	管理员	总部
370	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:29:41.515	localhost	127.0.0.1	管理员	管理员	总部
373	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:31:48.375	localhost	127.0.0.1	管理员	管理员	总部
375	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:34:20.078	localhost	127.0.0.1	管理员	管理员	总部
376	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:50:59.343	localhost	127.0.0.1	管理员	管理员	总部
379	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:56:43.968	localhost	127.0.0.1	管理员	管理员	总部
381	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:59:23.078	localhost	127.0.0.1	管理员	管理员	总部
384	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:02:05.671	localhost	127.0.0.1	管理员	管理员	总部
386	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:03:29.343	localhost	127.0.0.1	管理员	管理员	总部
389	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:04:33.375	localhost	127.0.0.1	管理员	管理员	总部
391	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:09:47.359	localhost	127.0.0.1	管理员	管理员	总部
394	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:14:27.687	localhost	127.0.0.1	管理员	管理员	总部
396	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:17:36.421	localhost	127.0.0.1	管理员	管理员	总部
399	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:20:54.687	localhost	127.0.0.1	管理员	管理员	总部
401	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:45:03.031	localhost	127.0.0.1	管理员	管理员	总部
404	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:50:37.281	localhost	127.0.0.1	管理员	管理员	总部
407	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:57:14.953	localhost	127.0.0.1	管理员	管理员	总部
410	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:58:55.968	localhost	127.0.0.1	管理员	管理员	总部
413	用户登录	用户登录	总部-管理员-管理员		2011-12-29 12:00:30.609	localhost	127.0.0.1	管理员	管理员	总部
415	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:19:11.5	localhost	127.0.0.1	管理员	管理员	总部
109	用户登录	用户登录	总部-管理员-管理员		2011-12-21 14:55:31.125	localhost	127.0.0.1	管理员	管理员	总部
110	系统编码管理	保存	编码英文名称：a;编码中文名称:a;编码所属模块名称:a;编码效果:20111221-;	20	2011-12-21 14:56:45.937	localhost	127.0.0.1	管理员	管理员	总部
111	用户登录	用户登录	总部-管理员-管理员		2011-12-21 14:57:55.359	localhost	127.0.0.1	管理员	管理员	总部
112	用户登录	用户登录	总部-管理员-管理员		2011-12-21 14:59:30.546	localhost	127.0.0.1	管理员	管理员	总部
113	系统编码管理	保存	编码英文名称：ab;编码中文名称:ab;编码所属模块名称:ab;编码效果:YKD-20111221;	21	2011-12-21 15:00:31.781	localhost	127.0.0.1	管理员	管理员	总部
114	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:02:39.453	localhost	127.0.0.1	管理员	管理员	总部
115	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:02:59.781	localhost	127.0.0.1	管理员	管理员	总部
116	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:03:40.187	localhost	127.0.0.1	管理员	管理员	总部
117	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:06:02.796	localhost	127.0.0.1	管理员	管理员	总部
118	系统编码管理	保存	编码英文名称：liang;编码中文名称:zh;编码所属模块名称:zh;编码效果:abc-20111221-001-北京~管理员~zhangsan;	22	2011-12-21 15:07:10	localhost	127.0.0.1	管理员	管理员	总部
119	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:07:58.625	localhost	127.0.0.1	管理员	管理员	总部
120	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:08:12.156	localhost	127.0.0.1	管理员	管理员	总部
121	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:08:43.89	localhost	127.0.0.1	管理员	管理员	总部
122	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:09:45.343	localhost	127.0.0.1	管理员	管理员	总部
123	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:10:13.578	localhost	127.0.0.1	管理员	管理员	总部
124	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:14:26.046	localhost	127.0.0.1	管理员	管理员	总部
125	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:18:02.437	localhost	127.0.0.1	管理员	管理员	总部
126	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:18:45.375	localhost	127.0.0.1	管理员	管理员	总部
127	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:19:59.828	localhost	127.0.0.1	管理员	管理员	总部
128	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:21:56.515	localhost	127.0.0.1	管理员	管理员	总部
129	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:27:19.75	localhost	127.0.0.1	管理员	管理员	总部
130	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:28:04.937	localhost	127.0.0.1	管理员	管理员	总部
131	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:28:34.968	localhost	127.0.0.1	管理员	管理员	总部
132	系统编码管理	保存	编码英文名称：test;编码中文名称:测试;编码所属模块名称:测试;编码效果:a-20111221-0001-zhangsan;	23	2011-12-21 15:29:30.031	localhost	127.0.0.1	管理员	管理员	总部
133	系统编码管理	修改	编码英文名称：test;编码中文名称:测试编码所属模块名称:测试编码效果:	23	2011-12-21 15:32:06.546	localhost	127.0.0.1	管理员	管理员	总部
134	系统配置管理	保存	设置角色切换:login;		2011-12-21 15:33:16.187	localhost	127.0.0.1	管理员	管理员	总部
135	用户管理	修改	jacob;jacob;无角色用户;	12	2011-12-21 15:33:40.281	localhost	127.0.0.1	管理员	管理员	总部
136	用户管理	停用	jacob;jacob;无角色用户;	12	2011-12-21 15:33:40.296	localhost	127.0.0.1	管理员	管理员	总部
137	用户分配角色	保存	0,11	12	2011-12-21 15:34:04.343	localhost	127.0.0.1	管理员	管理员	总部
138	用户分配角色	保存	11	12	2011-12-21 15:34:12.656	localhost	127.0.0.1	管理员	管理员	总部
139	用户登录	用户登录	河北-经理在-jacob		2011-12-21 15:34:48.234	localhost	127.0.0.1	jacob	经理在	河北
140	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:35:46.187	localhost	127.0.0.1	管理员	管理员	总部
141	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:36:02.937	localhost	127.0.0.1	管理员	管理员	总部
142	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:40:09.703	localhost	127.0.0.1	管理员	管理员	总部
143	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:51:18.468	localhost	127.0.0.1	管理员	管理员	总部
144	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:51:38.187	localhost	127.0.0.1	管理员	管理员	总部
145	用户登录	用户登录	总部-管理员-管理员		2011-12-21 15:53:29.531	localhost	127.0.0.1	管理员	管理员	总部
146	用户登录	用户登录	总部-管理员-管理员		2011-12-22 10:17:28.796	localhost	127.0.0.1	管理员	管理员	总部
147	用户登录	用户登录	总部-管理员-管理员		2011-12-22 10:18:02.421	localhost	127.0.0.1	管理员	管理员	总部
148	用户登录	用户登录	总部-管理员-管理员		2011-12-22 10:37:18.843	localhost	127.0.0.1	管理员	管理员	总部
149	用户登录	用户登录	总部-管理员-管理员		2011-12-22 13:55:02.421	localhost	127.0.0.1	管理员	管理员	总部
150	用户登录	用户登录	总部-管理员-管理员		2011-12-22 13:58:06.14	localhost	127.0.0.1	管理员	管理员	总部
151	用户登录	用户登录	总部-管理员-管理员		2011-12-22 13:58:27.25	localhost	127.0.0.1	管理员	管理员	总部
152	用户登录	用户登录	总部-管理员-管理员		2011-12-22 14:00:04.953	localhost	127.0.0.1	管理员	管理员	总部
153	用户登录	用户登录	总部-管理员-管理员		2011-12-22 14:00:34.734	localhost	127.0.0.1	管理员	管理员	总部
154	用户登录	用户登录	总部-管理员-管理员		2011-12-23 09:35:47.484	localhost	127.0.0.1	管理员	管理员	总部
155	用户登录	用户登录	总部-管理员-管理员		2011-12-23 09:36:33.328	localhost	127.0.0.1	管理员	管理员	总部
156	用户登录	用户登录	总部-管理员-管理员		2011-12-23 09:36:41.468	localhost	127.0.0.1	管理员	管理员	总部
157	角色管理	保存	会计;	4	2011-12-23 09:37:13.359	localhost	127.0.0.1	管理员	管理员	总部
158	用户登录	用户登录	总部-管理员-管理员		2011-12-23 15:42:01.031	localhost	127.0.0.1	管理员	管理员	总部
159	用户登录	用户登录	总部-管理员-管理员		2011-12-23 16:30:27.14	localhost	127.0.0.1	管理员	管理员	总部
160	用户登录	用户登录	总部-管理员-管理员		2011-12-23 16:30:59.984	localhost	127.0.0.1	管理员	管理员	总部
161	角色管理	保存	平衡;	5	2011-12-23 16:33:20.093	localhost	127.0.0.1	管理员	管理员	总部
262	用户登录	用户登录	总部-管理员-管理员		2011-12-28 09:54:07.734	localhost	127.0.0.1	管理员	管理员	总部
266	用户登录	用户登录	总部-管理员-管理员		2011-12-28 10:36:42.281	localhost	127.0.0.1	管理员	管理员	总部
268	用户登录	用户登录	总部-管理员-管理员		2011-12-28 10:38:30.125	localhost	127.0.0.1	管理员	管理员	总部
274	用户登录	用户登录	总部-管理员-管理员		2011-12-28 13:22:44.578	localhost	127.0.0.1	管理员	管理员	总部
275	用户登录	用户登录	总部-管理员-管理员		2011-12-28 13:23:34.062	localhost	127.0.0.1	管理员	管理员	总部
279	用户管理	修改	jacob;jacob;null;	12	2011-12-28 14:02:20.312	localhost	127.0.0.1	管理员	管理员	总部
282	用户管理	修改	jacob;jacob;null;	12	2011-12-28 14:19:58.718	localhost	127.0.0.1	管理员	管理员	总部
284	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:29:13.703	localhost	127.0.0.1	管理员	管理员	总部
339	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:19:56.937	localhost	127.0.0.1	管理员	管理员	总部
295	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:47:59.218	localhost	127.0.0.1	管理员	管理员	总部
296	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:49:55.875	localhost	127.0.0.1	管理员	管理员	总部
298	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:52:26.64	localhost	127.0.0.1	管理员	管理员	总部
300	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:56:55.953	localhost	127.0.0.1	管理员	管理员	总部
301	用户管理	修改	jacob;jacob;null;	12	2011-12-28 15:01:18.953	localhost	127.0.0.1	管理员	管理员	总部
302	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:07:33.593	localhost	127.0.0.1	管理员	管理员	总部
303	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:09:34.875	localhost	127.0.0.1	管理员	管理员	总部
305	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:15:11.25	localhost	127.0.0.1	管理员	管理员	总部
312	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:23:49.625	localhost	127.0.0.1	管理员	管理员	总部
313	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:25:06.453	localhost	127.0.0.1	管理员	管理员	总部
320	用户登录	用户登录	总部-管理员-管理员		2011-12-28 17:25:50.859	localhost	127.0.0.1	管理员	管理员	总部
323	用户登录	用户登录	总部-管理员-管理员		2011-12-28 17:32:05.546	localhost	127.0.0.1	管理员	管理员	总部
327	主页面功能区	删除	删除功能区的id:30;		2011-12-28 17:35:07.906	localhost	127.0.0.1	管理员	管理员	总部
331	主页面功能区	删除	删除功能区的id:23;		2011-12-28 17:41:25.5	localhost	127.0.0.1	管理员	管理员	总部
332	主页面功能区	删除	删除功能区的id:24;		2011-12-28 17:41:44.859	localhost	127.0.0.1	管理员	管理员	总部
336	主页面功能区	删除	删除功能区的id:28;		2011-12-28 17:43:37.875	localhost	127.0.0.1	管理员	管理员	总部
337	主页面功能区	删除	删除功能区的id:29;		2011-12-28 17:43:45.89	localhost	127.0.0.1	管理员	管理员	总部
342	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:29:08.031	localhost	127.0.0.1	管理员	管理员	总部
345	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:30:39.078	localhost	127.0.0.1	管理员	管理员	总部
350	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:51:50.812	localhost	127.0.0.1	管理员	管理员	总部
356	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:02:41.921	localhost	127.0.0.1	管理员	管理员	总部
358	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:03:22.937	localhost	127.0.0.1	管理员	管理员	总部
361	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:04:00.093	localhost	127.0.0.1	管理员	管理员	总部
363	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:06:17.39	localhost	127.0.0.1	管理员	管理员	总部
371	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:30:22.312	localhost	127.0.0.1	管理员	管理员	总部
377	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:55:56.843	localhost	127.0.0.1	管理员	管理员	总部
380	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:56:59.218	localhost	127.0.0.1	管理员	管理员	总部
382	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:59:47.828	localhost	127.0.0.1	管理员	管理员	总部
385	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:02:27.562	localhost	127.0.0.1	管理员	管理员	总部
387	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:03:59.5	localhost	127.0.0.1	管理员	管理员	总部
390	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:09:17.109	localhost	127.0.0.1	管理员	管理员	总部
392	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:13:17.875	localhost	127.0.0.1	管理员	管理员	总部
395	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:16:40	localhost	127.0.0.1	管理员	管理员	总部
397	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:19:44.843	localhost	127.0.0.1	管理员	管理员	总部
400	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:22:39.89	localhost	127.0.0.1	管理员	管理员	总部
402	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:47:27.093	localhost	127.0.0.1	管理员	管理员	总部
405	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:51:00.656	localhost	127.0.0.1	管理员	管理员	总部
408	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:57:42.859	localhost	127.0.0.1	管理员	管理员	总部
417	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:19:33.984	localhost	127.0.0.1	管理员	管理员	总部
420	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:21:54.109	localhost	127.0.0.1	管理员	管理员	总部
421	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:28:32.39	localhost	127.0.0.1	管理员	管理员	总部
422	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:42:00.531	localhost	127.0.0.1	管理员	管理员	总部
423	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:42:54.875	localhost	127.0.0.1	管理员	管理员	总部
424	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:45:16.562	localhost	127.0.0.1	管理员	管理员	总部
425	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:45:56.953	localhost	127.0.0.1	管理员	管理员	总部
162	用户登录	用户登录	总部-管理员-管理员		2011-12-23 16:34:15.531	localhost	127.0.0.1	管理员	管理员	总部
163	系统配置管理	保存	设置系统名称:my协同办公管理系统a;		2011-12-23 16:37:30.593	localhost	127.0.0.1	管理员	管理员	总部
164	用户登录	用户登录	总部-管理员-管理员		2011-12-23 16:37:42.765	localhost	127.0.0.1	管理员	管理员	总部
165	系统配置管理	保存	设置系统名称:my协同办公管理系统ab;		2011-12-23 16:58:14.078	localhost	127.0.0.1	管理员	管理员	总部
166	用户登录	用户登录	总部-管理员-管理员		2011-12-23 16:58:44.625	localhost	127.0.0.1	管理员	管理员	总部
167	系统参数管理	修改	参数名称：用户有效期;参数键值：uservalidate;参数值：6参数类型：uus	2	2011-12-23 17:07:22.625	localhost	127.0.0.1	管理员	管理员	总部
168	角色管理	保存	q;	6	2011-12-23 17:21:56.5	localhost	127.0.0.1	管理员	管理员	总部
169	角色管理	保存	a;	7	2011-12-23 17:22:24.296	localhost	127.0.0.1	管理员	管理员	总部
170	用户登录	用户登录	总部-管理员-管理员		2011-12-26 09:48:43.921	localhost	127.0.0.1	管理员	管理员	总部
171	用户登录	用户登录	总部-管理员-管理员		2011-12-26 09:50:58.765	localhost	127.0.0.1	管理员	管理员	总部
172	用户登录	用户登录	总部-管理员-管理员		2011-12-26 09:51:42.484	localhost	127.0.0.1	管理员	管理员	总部
173	用户登录	用户登录	总部-管理员-管理员		2011-12-26 09:53:34.75	localhost	127.0.0.1	管理员	管理员	总部
174	角色管理	保存	出纳;	8	2011-12-26 09:54:22.656	localhost	127.0.0.1	管理员	管理员	总部
175	角色管理	保存	会计2;	9	2011-12-26 09:54:33.609	localhost	127.0.0.1	管理员	管理员	总部
176	省市地区管理	保存	北海;province;	2001	2011-12-26 09:55:00.156	localhost	127.0.0.1	管理员	管理员	总部
177	用户登录	用户登录	总部-管理员-管理员		2011-12-26 09:55:36.468	localhost	127.0.0.1	管理员	管理员	总部
178	角色管理	修改	会计2;	9	2011-12-26 09:56:01.14	localhost	127.0.0.1	管理员	管理员	总部
179	角色管理	修改	会计2;	9	2011-12-26 09:56:11.359	localhost	127.0.0.1	管理员	管理员	总部
180	用户登录	用户登录	总部-管理员-管理员		2011-12-26 09:56:24.968	localhost	127.0.0.1	管理员	管理员	总部
181	用户登录	用户登录	总部-管理员-管理员		2011-12-26 10:41:45.593	localhost	127.0.0.1	管理员	管理员	总部
182	用户登录	用户登录	总部-管理员-管理员		2011-12-26 10:41:58.093	localhost	127.0.0.1	管理员	管理员	总部
183	用户登录	用户登录	总部-管理员-管理员		2011-12-26 11:36:46.921	localhost	127.0.0.1	管理员	管理员	总部
184	用户登录	用户登录	总部-管理员-管理员		2011-12-26 13:24:01.406	localhost	127.0.0.1	管理员	管理员	总部
185	用户登录	用户登录	总部-管理员-管理员		2011-12-26 13:26:11.671	localhost	127.0.0.1	管理员	管理员	总部
186	用户登录	用户登录	总部-管理员-管理员		2011-12-26 13:40:01.734	localhost	127.0.0.1	管理员	管理员	总部
187	用户登录	用户登录	总部-管理员-管理员		2011-12-26 13:43:20.671	localhost	127.0.0.1	管理员	管理员	总部
188	系统编码管理	保存	编码英文名称：test2;编码中文名称:测试2;编码所属模块名称:测试2;编码效果:CKD-201112-001-zhangsan;	24	2011-12-26 13:49:34.359	localhost	127.0.0.1	管理员	管理员	总部
189	系统编码管理	修改	编码英文名称：test2;编码中文名称:测试2编码所属模块名称:测试2编码效果:CKD-201112-001-zhangsan	24	2011-12-26 13:51:13.109	localhost	127.0.0.1	管理员	管理员	总部
190	系统编码管理	停用	编码停用：编码英文名称：test2;编码中文名称:测试2编码所属模块名称:测试2编码效果:CKD-201112-001-zhangsan	24	2011-12-26 13:51:13.109	localhost	127.0.0.1	管理员	管理员	总部
191	系统编码管理	修改	编码英文名称：test2;编码中文名称:测试2编码所属模块名称:测试2编码效果:CKD-201112-001-zhangsan	24	2011-12-26 13:51:16.843	localhost	127.0.0.1	管理员	管理员	总部
192	系统编码管理	启用	编码启用：编码英文名称：test2;编码中文名称:测试2编码所属模块名称:测试2编码效果:CKD-201112-001-zhangsan	24	2011-12-26 13:51:16.843	localhost	127.0.0.1	管理员	管理员	总部
193	系统调度管理	启动调度器	启动调度器并初始化任务		2011-12-26 13:56:05.062	localhost	127.0.0.1	管理员	管理员	总部
194	系统调度管理	停止调度器	停止调度器		2011-12-26 13:56:40.625	localhost	127.0.0.1	管理员	管理员	总部
195	用户登录	用户登录	总部-管理员-管理员		2011-12-26 15:42:18.828	localhost	127.0.0.1	管理员	管理员	总部
196	用户登录	用户登录	总部-管理员-管理员		2011-12-27 10:37:03.499	localhost	127.0.0.1	管理员	管理员	总部
197	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2011-12-27 10:37:56.265	localhost	127.0.0.1	管理员	管理员	总部
198	用户登录	用户登录	总部-管理员-管理员		2011-12-27 10:45:20.265	localhost	127.0.0.1	管理员	管理员	总部
199	系统配置管理	保存	设置角色切换:login;		2011-12-27 10:46:22.311	localhost	127.0.0.1	管理员	管理员	总部
200	系统配置管理	保存	设置日志级别:2;		2011-12-27 10:46:54.233	localhost	127.0.0.1	管理员	管理员	总部
201	系统配置管理	保存	设置日志保留天数:60;		2011-12-27 10:47:19.015	localhost	127.0.0.1	管理员	管理员	总部
202	系统配置管理	保存	增加管理员:jacob;		2011-12-27 10:48:40.14	localhost	127.0.0.1	管理员	管理员	总部
203	系统配置管理	保存	删除管理员:jacob;删除管理员:jacob;		2011-12-27 10:48:46.233	localhost	127.0.0.1	管理员	管理员	总部
204	系统配置管理	保存	增加管理员:jacob;		2011-12-27 10:48:57.296	localhost	127.0.0.1	管理员	管理员	总部
205	系统配置管理	保存	删除管理员:jacob;删除管理员:jacob;		2011-12-27 10:50:24.952	localhost	127.0.0.1	管理员	管理员	总部
206	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:00:52.186	localhost	127.0.0.1	管理员	管理员	总部
207	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2011-12-27 11:01:03.093	localhost	127.0.0.1	管理员	管理员	总部
208	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:01:22.796	localhost	127.0.0.1	管理员	管理员	总部
209	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2011-12-27 11:01:38.546	localhost	127.0.0.1	管理员	管理员	总部
210	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:01:46.483	localhost	127.0.0.1	管理员	管理员	总部
211	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户a;		2011-12-27 11:01:52.983	localhost	127.0.0.1	管理员	管理员	总部
212	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:02:29.28	localhost	127.0.0.1	管理员	管理员	总部
263	用户登录	用户登录	总部-管理员-管理员		2011-12-28 10:26:05.296	localhost	127.0.0.1	管理员	管理员	总部
267	用户登录	用户登录	总部-管理员-管理员		2011-12-28 10:37:38.453	localhost	127.0.0.1	管理员	管理员	总部
270	用户登录	用户登录	总部-管理员-管理员		2011-12-28 10:59:59.89	localhost	127.0.0.1	管理员	管理员	总部
271	用户登录	用户登录	总部-管理员-管理员		2011-12-28 11:44:51.968	localhost	127.0.0.1	管理员	管理员	总部
276	用户管理	修改	jacob;jacob;null;	12	2011-12-28 13:48:53.937	localhost	127.0.0.1	管理员	管理员	总部
280	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:09:52.234	localhost	127.0.0.1	管理员	管理员	总部
281	用户管理	修改	jacob;jacob;null;	12	2011-12-28 14:18:01.875	localhost	127.0.0.1	管理员	管理员	总部
288	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:37:19.765	localhost	127.0.0.1	管理员	管理员	总部
289	用户管理	修改	jacob;jacob;null;	12	2011-12-28 14:37:42.75	localhost	127.0.0.1	管理员	管理员	总部
290	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:38:52.89	localhost	127.0.0.1	管理员	管理员	总部
293	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:42:40.906	localhost	127.0.0.1	管理员	管理员	总部
297	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:50:52.781	localhost	127.0.0.1	管理员	管理员	总部
304	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:10:51.89	localhost	127.0.0.1	管理员	管理员	总部
307	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:16:21.296	localhost	127.0.0.1	管理员	管理员	总部
309	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:17:48.265	localhost	127.0.0.1	管理员	管理员	总部
311	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:23:18.171	localhost	127.0.0.1	管理员	管理员	总部
314	用户登录	用户登录	总部-管理员-管理员		2011-12-28 15:25:46.593	localhost	127.0.0.1	管理员	管理员	总部
316	用户登录	用户登录	总部-管理员-管理员		2011-12-28 16:14:40.546	localhost	127.0.0.1	管理员	管理员	总部
317	用户登录	用户登录	总部-管理员-管理员		2011-12-28 16:28:51.921	localhost	127.0.0.1	管理员	管理员	总部
319	用户登录	用户登录	总部-管理员-管理员		2011-12-28 16:37:37.093	localhost	127.0.0.1	管理员	管理员	总部
321	用户登录	用户登录	总部-管理员-管理员		2011-12-28 17:29:54.828	localhost	127.0.0.1	管理员	管理员	总部
324	用户登录	用户登录	总部-管理员-管理员		2011-12-28 17:33:40.296	localhost	127.0.0.1	管理员	管理员	总部
325	用户登录	用户登录	总部-管理员-管理员		2011-12-28 17:34:04.671	localhost	127.0.0.1	管理员	管理员	总部
328	主页面功能区	删除	删除功能区的id:20;		2011-12-28 17:36:54.625	localhost	127.0.0.1	管理员	管理员	总部
330	主页面功能区	删除	删除功能区的id:22;		2011-12-28 17:41:03.781	localhost	127.0.0.1	管理员	管理员	总部
334	主页面功能区	删除	删除功能区的id:26;		2011-12-28 17:42:39.765	localhost	127.0.0.1	管理员	管理员	总部
335	主页面功能区	删除	删除功能区的id:27;		2011-12-28 17:43:03.093	localhost	127.0.0.1	管理员	管理员	总部
340	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:21:42.578	localhost	127.0.0.1	管理员	管理员	总部
343	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:29:34.64	localhost	127.0.0.1	管理员	管理员	总部
348	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:50:20.187	localhost	127.0.0.1	管理员	管理员	总部
351	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:52:59.75	localhost	127.0.0.1	管理员	管理员	总部
353	用户登录	用户登录	总部-管理员-管理员		2011-12-29 09:56:19.468	localhost	127.0.0.1	管理员	管理员	总部
359	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:03:35.515	localhost	127.0.0.1	管理员	管理员	总部
364	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:12:44.984	localhost	127.0.0.1	管理员	管理员	总部
366	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:13:33.875	localhost	127.0.0.1	管理员	管理员	总部
367	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:19:27.968	localhost	127.0.0.1	管理员	管理员	总部
369	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:28:58.359	localhost	127.0.0.1	管理员	管理员	总部
372	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:31:16.859	localhost	127.0.0.1	管理员	管理员	总部
374	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:33:28.234	localhost	127.0.0.1	管理员	管理员	总部
378	用户登录	用户登录	总部-管理员-管理员		2011-12-29 10:56:19.125	localhost	127.0.0.1	管理员	管理员	总部
383	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:01:51.656	localhost	127.0.0.1	管理员	管理员	总部
388	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:04:09.968	localhost	127.0.0.1	管理员	管理员	总部
393	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:14:12.843	localhost	127.0.0.1	管理员	管理员	总部
398	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:19:53.125	localhost	127.0.0.1	管理员	管理员	总部
403	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:50:05.562	localhost	127.0.0.1	管理员	管理员	总部
406	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:56:51.593	localhost	127.0.0.1	管理员	管理员	总部
409	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:58:10.953	localhost	127.0.0.1	管理员	管理员	总部
411	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:59:11.468	localhost	127.0.0.1	管理员	管理员	总部
412	用户登录	用户登录	总部-管理员-管理员		2011-12-29 11:59:51.906	localhost	127.0.0.1	管理员	管理员	总部
414	用户登录	用户登录	总部-管理员-管理员		2011-12-29 12:00:54.593	localhost	127.0.0.1	管理员	管理员	总部
416	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:19:19.937	localhost	127.0.0.1	管理员	管理员	总部
418	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:20:16.593	localhost	127.0.0.1	管理员	管理员	总部
419	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:21:04.562	localhost	127.0.0.1	管理员	管理员	总部
426	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:51:47.468	localhost	127.0.0.1	管理员	管理员	总部
213	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2011-12-27 11:02:44.952	localhost	127.0.0.1	管理员	管理员	总部
214	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:05:52.015	localhost	127.0.0.1	管理员	管理员	总部
215	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户a;		2011-12-27 11:05:59.249	localhost	127.0.0.1	管理员	管理员	总部
216	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2011-12-27 11:07:40.202	localhost	127.0.0.1	管理员	管理员	总部
217	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:08:28.296	localhost	127.0.0.1	管理员	管理员	总部
218	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:09:50.577	localhost	127.0.0.1	管理员	管理员	总部
219	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2011-12-27 11:09:57.749	localhost	127.0.0.1	管理员	管理员	总部
220	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户a;		2011-12-27 11:10:04.858	localhost	127.0.0.1	管理员	管理员	总部
221	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:10:35.64	localhost	127.0.0.1	管理员	管理员	总部
222	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2011-12-27 11:10:44.452	localhost	127.0.0.1	管理员	管理员	总部
223	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:51:58.233	localhost	127.0.0.1	管理员	管理员	总部
224	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户a;		2011-12-27 11:52:05.983	localhost	127.0.0.1	管理员	管理员	总部
225	用户登录	用户登录	总部-管理员-管理员		2011-12-27 11:52:37.827	localhost	127.0.0.1	管理员	管理员	总部
226	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2011-12-27 11:52:44.03	localhost	127.0.0.1	管理员	管理员	总部
227	用户登录	用户登录	总部-管理员-管理员		2011-12-27 13:02:53.686	localhost	127.0.0.1	管理员	管理员	总部
228	用户登录	用户登录	总部-管理员-管理员		2011-12-27 13:05:30.436	localhost	127.0.0.1	管理员	管理员	总部
229	用户登录	用户登录	总部-管理员-管理员		2011-12-27 13:08:23.436	localhost	127.0.0.1	管理员	管理员	总部
230	用户登录	用户登录	总部-管理员-管理员		2011-12-27 13:09:05.733	localhost	127.0.0.1	管理员	管理员	总部
231	用户登录	用户登录	总部-管理员-管理员		2011-12-27 13:10:06.968	localhost	127.0.0.1	管理员	管理员	总部
232	用户登录	用户登录	总部-管理员-管理员		2011-12-27 13:31:04.218	localhost	127.0.0.1	管理员	管理员	总部
233	系统附件	删除	删除附件ID：1		2011-12-27 13:31:16.468	localhost	127.0.0.1	管理员	管理员	总部
234	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:23:30.265	localhost	127.0.0.1	管理员	管理员	总部
235	系统公告管理	修改	修改公告ID：3;修改公告标题：测试;有效期：Sat Dec 31 00:00:00 CST 2011;是否发布：0;	3	2011-12-27 14:23:57.655	localhost	127.0.0.1	管理员	管理员	总部
236	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:24:03.718	localhost	127.0.0.1	管理员	管理员	总部
237	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:25:22.483	localhost	127.0.0.1	管理员	管理员	总部
238	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:32:11.405	localhost	127.0.0.1	管理员	管理员	总部
239	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:39:55.858	localhost	127.0.0.1	管理员	管理员	总部
240	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:42:11.858	localhost	127.0.0.1	管理员	管理员	总部
241	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:45:47.843	localhost	127.0.0.1	管理员	管理员	总部
242	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:54:49.015	localhost	127.0.0.1	管理员	管理员	总部
243	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:55:47.452	localhost	127.0.0.1	管理员	管理员	总部
244	用户登录	用户登录	总部-管理员-管理员		2011-12-27 14:56:08.108	localhost	127.0.0.1	管理员	管理员	总部
245	用户登录	用户登录	总部-管理员-管理员		2011-12-27 15:02:20.483	localhost	127.0.0.1	管理员	管理员	总部
246	用户登录	用户登录	总部-管理员-管理员		2011-12-27 15:06:38.046	localhost	127.0.0.1	管理员	管理员	总部
247	系统附件	删除	删除附件ID：6		2011-12-27 15:06:44.046	localhost	127.0.0.1	管理员	管理员	总部
248	用户登录	用户登录	总部-管理员-管理员		2011-12-27 15:08:15.983	localhost	127.0.0.1	管理员	管理员	总部
249	用户登录	用户登录	总部-管理员-管理员		2011-12-27 15:13:54.905	localhost	127.0.0.1	管理员	管理员	总部
250	用户登录	用户登录	总部-管理员-管理员		2011-12-27 15:22:22.811	localhost	127.0.0.1	管理员	管理员	总部
251	系统附件	删除	删除附件ID：5		2011-12-27 15:32:06.686	localhost	127.0.0.1	管理员	管理员	总部
252	系统公告管理	修改	修改公告ID：3;修改公告标题：测试;有效期：Sat Dec 31 00:00:00 CST 2011;是否发布：0;	3	2011-12-27 15:32:31.077	localhost	127.0.0.1	管理员	管理员	总部
253	用户登录	用户登录	总部-管理员-管理员		2011-12-27 15:33:57.186	localhost	127.0.0.1	管理员	管理员	总部
254	用户登录	用户登录	总部-管理员-管理员		2011-12-27 15:38:17.108	localhost	127.0.0.1	管理员	管理员	总部
255	用户登录	用户登录	总部-管理员-管理员		2011-12-27 15:39:56.655	localhost	127.0.0.1	管理员	管理员	总部
256	用户登录	用户登录	总部-管理员-管理员		2011-12-27 15:40:14.03	localhost	127.0.0.1	管理员	管理员	总部
257	用户登录	用户登录	总部-管理员-管理员		2011-12-28 09:19:11.796	localhost	127.0.0.1	管理员	管理员	总部
258	系统调度管理	启动job	启动job:;jobName:postgresqlBbBackup;job类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;	3	2011-12-28 09:32:13.296	localhost	127.0.0.1	管理员	管理员	总部
259	系统调度管理	停止job	停止job:postgresqlBbBackup		2011-12-28 09:32:37.093	localhost	127.0.0.1	管理员	管理员	总部
264	用户登录	用户登录	总部-管理员-管理员		2011-12-28 10:31:48.609	localhost	127.0.0.1	管理员	管理员	总部
269	用户登录	用户登录	总部-管理员-管理员		2011-12-28 10:40:56.609	localhost	127.0.0.1	管理员	管理员	总部
277	用户管理	修改	jacob;jacob;null;	12	2011-12-28 13:52:49.125	localhost	127.0.0.1	管理员	管理员	总部
283	用户登录	用户登录	总部-管理员-管理员		2011-12-28 14:27:26.656	localhost	127.0.0.1	管理员	管理员	总部
489	用户分配角色	保存	11,26	12	2011-12-30 17:09:35.843	localhost	127.0.0.1	管理员	管理员	总部
427	用户登录	用户登录	总部-管理员-管理员		2011-12-29 13:52:02.734	localhost	127.0.0.1	管理员	管理员	总部
494	用户登录	用户登录	总部-管理员-管理员		2012-01-04 11:18:11.485	localhost	127.0.0.1	管理员	管理员	总部
498	用户登录	用户登录	总部-管理员-管理员		2012-01-04 11:41:31.454	localhost	127.0.0.1	管理员	管理员	总部
501	用户登录	用户登录	总部-管理员-管理员		2012-01-04 11:59:31.079	localhost	127.0.0.1	管理员	管理员	总部
510	用户登录	用户登录	总部-管理员-管理员		2012-01-04 14:45:31.656	localhost	127.0.0.1	管理员	管理员	总部
512	模块分配操作	保存	模块分配操作;	7	2012-01-04 14:48:30.468	localhost	127.0.0.1	管理员	管理员	总部
515	用户登录	用户登录	总部-管理员-管理员		2012-01-04 14:57:32.39	localhost	127.0.0.1	管理员	管理员	总部
523	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:30:53.687	localhost	127.0.0.1	管理员	管理员	总部
531	用户登录	用户登录	总部-管理员-管理员		2012-01-04 17:24:31.937	localhost	127.0.0.1	管理员	管理员	总部
532	用户登录	用户登录	总部-管理员-管理员		2012-01-04 17:25:00.015	localhost	127.0.0.1	管理员	管理员	总部
535	用户登录	用户登录	总部-管理员-管理员		2012-01-04 17:31:53.625	localhost	127.0.0.1	管理员	管理员	总部
536	用户登录	用户登录	总部-管理员-管理员		2012-01-04 17:32:43	localhost	127.0.0.1	管理员	管理员	总部
539	用户登录	用户登录	总部-管理员-管理员		2012-01-04 17:34:52.296	localhost	127.0.0.1	管理员	管理员	总部
540	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:26:35.703	localhost	127.0.0.1	管理员	管理员	总部
545	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:38:20.531	localhost	127.0.0.1	管理员	管理员	总部
550	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:49:38.64	localhost	127.0.0.1	管理员	管理员	总部
560	用户登录	用户登录	总部-管理员-管理员		2012-01-06 09:53:05.171	localhost	127.0.0.1	管理员	管理员	总部
565	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:07:26	localhost	127.0.0.1	管理员	管理员	总部
574	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:42:14.203	localhost	127.0.0.1	管理员	管理员	总部
600	角色组管理	修改	组名称：新组;组类型：roleGroup;	4	2012-01-06 13:52:30.984	localhost	127.0.0.1	管理员	管理员	总部
608	系统模块管理	修改	省市地区;plt_priCity;	52	2012-01-06 14:18:25.437	localhost	127.0.0.1	管理员	管理员	总部
618	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:38:47.343	localhost	127.0.0.1	管理员	管理员	总部
626	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:23:34.187	localhost	127.0.0.1	管理员	管理员	总部
633	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:38:15.703	localhost	127.0.0.1	管理员	管理员	总部
646	省市地区管理	修改	海淀;district;	2002	2012-01-06 16:12:18.484	localhost	127.0.0.1	管理员	管理员	总部
657	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:48:20.312	localhost	127.0.0.1	管理员	管理员	总部
665	用户登录	用户登录	总部-管理员-管理员		2012-01-06 17:31:02.312	localhost	127.0.0.1	管理员	管理员	总部
668	用户登录	用户登录	总部-管理员-管理员		2012-01-08 15:55:51.843	localhost	127.0.0.1	管理员	管理员	总部
678	用户登录	用户登录	总部-管理员-管理员		2012-01-08 16:26:48.218	localhost	127.0.0.1	管理员	管理员	总部
684	用户登录	用户登录	总部-管理员-管理员		2012-01-08 16:38:35.578	localhost	127.0.0.1	管理员	管理员	总部
688	系统编码管理	修改	编码英文名称：tt;编码中文名称:测试编码所属模块名称:测试编码效果:CKD-201201-001-zhangsan	25	2012-01-08 16:40:23.75	localhost	127.0.0.1	管理员	管理员	总部
689	系统编码管理	启用	编码启用：编码英文名称：tt;编码中文名称:测试编码所属模块名称:测试编码效果:CKD-201201-001-zhangsan	25	2012-01-08 16:40:23.75	localhost	127.0.0.1	管理员	管理员	总部
691	用户登录	用户登录	总部-管理员-管理员		2012-01-08 16:50:30.89	localhost	127.0.0.1	管理员	管理员	总部
698	主页面功能区	删除	删除功能区的id:40;		2012-01-08 17:03:04.843	localhost	127.0.0.1	管理员	管理员	总部
699	用户登录	用户登录	总部-管理员-管理员		2012-01-09 09:24:09.375	localhost	127.0.0.1	管理员	管理员	总部
715	系统调度管理	停止job	停止job:postgresqlBbBackup		2012-01-09 09:48:05.187	localhost	127.0.0.1	管理员	管理员	总部
721	系统调度管理	停止调度器	停止调度器		2012-01-09 09:57:57.343	localhost	127.0.0.1	管理员	管理员	总部
722	用户登录	用户登录	总部-管理员-管理员		2012-01-09 09:58:44.921	localhost	127.0.0.1	管理员	管理员	总部
725	系统调度管理	停止job	停止job:postgresqlBbBackup		2012-01-09 09:59:25.984	localhost	127.0.0.1	管理员	管理员	总部
747	类别字典管理	修改	字典大类名称：系统类别;	21	2012-01-09 10:53:39.125	localhost	127.0.0.1	管理员	管理员	总部
766	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:58:55.734	localhost	127.0.0.1	管理员	管理员	总部
793	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:56:19.187	localhost	127.0.0.1	管理员	管理员	总部
794	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:57:01.343	localhost	127.0.0.1	管理员	管理员	总部
807	系统模块管理	修改	省市地区;priCityMgr;	52	2012-01-09 15:02:38.39	localhost	127.0.0.1	管理员	管理员	总部
808	系统模块管理	修改	日志管理;logMgr;	17	2012-01-09 15:02:44.593	localhost	127.0.0.1	管理员	管理员	总部
809	系统模块管理	修改	系统管理;systemMgr;	12	2012-01-09 15:02:49.984	localhost	127.0.0.1	管理员	管理员	总部
810	系统模块管理	修改	字典管理;dicmgr;	9	2012-01-09 15:02:56.203	localhost	127.0.0.1	管理员	管理员	总部
811	系统模块管理	修改	模块操作管理;modoptmgr;	6	2012-01-09 15:03:02.546	localhost	127.0.0.1	管理员	管理员	总部
812	系统模块管理	修改	主页框架管理;mainframemgr;	3	2012-01-09 15:03:12.578	localhost	127.0.0.1	管理员	管理员	总部
816	类别字典管理	修改	字典大类名称：系统类别;	21	2012-01-09 15:04:27.312	localhost	127.0.0.1	管理员	管理员	总部
817	用户登录	用户登录	总部-管理员-管理员		2012-01-09 15:06:57.359	localhost	127.0.0.1	管理员	管理员	总部
819	用户登录	用户登录	总部-管理员-管理员		2012-01-09 15:31:50.718	localhost	127.0.0.1	管理员	管理员	总部
820	用户登录	用户登录	总部-管理员-管理员		2012-01-09 15:37:45.531	localhost	127.0.0.1	管理员	管理员	总部
428	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:10:25.281	localhost	127.0.0.1	管理员	管理员	总部
433	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:19:30.875	localhost	127.0.0.1	管理员	管理员	总部
495	用户登录	用户登录	总部-管理员-管理员		2012-01-04 11:19:36.157	localhost	127.0.0.1	管理员	管理员	总部
499	用户登录	用户登录	总部-管理员-管理员		2012-01-04 11:44:02.47	localhost	127.0.0.1	管理员	管理员	总部
511	用户登录	用户登录	总部-管理员-管理员		2012-01-04 14:47:20.921	localhost	127.0.0.1	管理员	管理员	总部
522	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:29:36.984	localhost	127.0.0.1	管理员	管理员	总部
527	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:46:42	localhost	127.0.0.1	管理员	管理员	总部
534	用户登录	用户登录	总部-管理员-管理员		2012-01-04 17:30:26.375	localhost	127.0.0.1	管理员	管理员	总部
541	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:27:32.593	localhost	127.0.0.1	管理员	管理员	总部
546	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:43:54.078	localhost	127.0.0.1	管理员	管理员	总部
551	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:50:33.625	localhost	127.0.0.1	管理员	管理员	总部
555	用户登录	用户登录	总部-管理员-管理员		2012-01-05 17:31:05.531	localhost	127.0.0.1	管理员	管理员	总部
561	用户登录	用户登录	总部-管理员-管理员		2012-01-06 09:57:10.656	localhost	127.0.0.1	管理员	管理员	总部
566	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:15:18.468	localhost	127.0.0.1	管理员	管理员	总部
567	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:17:17.171	localhost	127.0.0.1	管理员	管理员	总部
568	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:18:05.125	localhost	127.0.0.1	管理员	管理员	总部
575	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:43:11.796	localhost	127.0.0.1	管理员	管理员	总部
576	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:45:46.421	localhost	127.0.0.1	管理员	管理员	总部
577	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:49:26.531	localhost	127.0.0.1	管理员	管理员	总部
592	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:52:56.296	localhost	127.0.0.1	管理员	管理员	总部
601	角色组管理	停用	组名称：新组;组类型：roleGroup;	4	2012-01-06 13:52:37.375	localhost	127.0.0.1	管理员	管理员	总部
609	系统模块管理	修改	省市地区;plt_priCityMgr;	52	2012-01-06 14:19:28.718	localhost	127.0.0.1	管理员	管理员	总部
616	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:35:28.609	localhost	127.0.0.1	管理员	管理员	总部
619	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:39:41.609	localhost	127.0.0.1	管理员	管理员	总部
627	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:24:39.515	localhost	127.0.0.1	管理员	管理员	总部
631	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:34:55.609	localhost	127.0.0.1	管理员	管理员	总部
634	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:39:44.734	localhost	127.0.0.1	管理员	管理员	总部
647	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:13:41.156	localhost	127.0.0.1	管理员	管理员	总部
652	系统模块管理	修改	系统配置管理;platform_sysConMgr;	25	2012-01-06 16:24:54.203	localhost	127.0.0.1	管理员	管理员	总部
656	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:45:34.078	localhost	127.0.0.1	管理员	管理员	总部
669	用户登录	用户登录	总部-管理员-管理员		2012-01-08 16:11:30.031	localhost	127.0.0.1	管理员	管理员	总部
700	系统模块管理	修改	操作按钮管理;platform_operateMgr;	8	2012-01-09 09:25:17.062	localhost	127.0.0.1	管理员	管理员	总部
710	系统模块管理	修改	系统调度管理;platform_schedulerMgr;	53	2012-01-09 09:32:36.234	localhost	127.0.0.1	管理员	管理员	总部
716	用户登录	用户登录	总部-管理员-管理员		2012-01-09 09:50:25.5	localhost	127.0.0.1	管理员	管理员	总部
723	系统调度管理	修改	任务名称：postgresqlBbBackup;任务类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;时间表达式:0/20 * * * * ?;	3	2012-01-09 09:59:06.265	localhost	127.0.0.1	管理员	管理员	总部
726	系统模块管理	修改	系统参数管理;platform_systemParaMgr;	15	2012-01-09 10:00:51.515	localhost	127.0.0.1	管理员	管理员	总部
728	系统参数管理	保存	参数名称：test;参数键值：test;参数值：test参数类型：uus	22	2012-01-09 10:06:40.468	localhost	127.0.0.1	管理员	管理员	总部
730	系统参数管理	修改	参数名称：test;参数键值：test;参数值：test参数类型：uus	22	2012-01-09 10:07:00.437	localhost	127.0.0.1	管理员	管理员	总部
751	用户登录	用户登录	总部-管理员-管理员		2012-01-09 10:56:16.218	localhost	127.0.0.1	管理员	管理员	总部
759	系统模块管理	修改	模块菜单管理;platform_moduleMgr;	7	2012-01-09 11:20:47.531	localhost	127.0.0.1	管理员	管理员	总部
767	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:59:08.531	localhost	127.0.0.1	管理员	管理员	总部
769	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:18:42.859	localhost	127.0.0.1	管理员	管理员	总部
773	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:26:37.812	localhost	127.0.0.1	管理员	管理员	总部
781	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:42:33.109	localhost	127.0.0.1	管理员	管理员	总部
789	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:49:34.859	localhost	127.0.0.1	管理员	管理员	总部
795	系统模块管理	修改	系统调度管理;schedulerMgr;	53	2012-01-09 14:59:31.703	localhost	127.0.0.1	管理员	管理员	总部
796	系统模块管理	修改	系统公告管理;afficheMgr;	49	2012-01-09 14:59:42.421	localhost	127.0.0.1	管理员	管理员	总部
797	系统模块管理	修改	系统配置管理;sysConMgr;	25	2012-01-09 14:59:52.078	localhost	127.0.0.1	管理员	管理员	总部
798	系统模块管理	修改	系统参数管理;systemParaMgr;	15	2012-01-09 15:00:02.109	localhost	127.0.0.1	管理员	管理员	总部
813	系统模块管理	修改	平台管理;pltmgr;	1	2012-01-09 15:03:30.078	localhost	127.0.0.1	管理员	管理员	总部
814	系统模块管理	修改	统一用户;uusmgr;	2	2012-01-09 15:03:46.984	localhost	127.0.0.1	管理员	管理员	总部
825	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:35:35.953	localhost	127.0.0.1	管理员	管理员	总部
429	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:16:32.078	localhost	127.0.0.1	管理员	管理员	总部
496	用户登录	用户登录	总部-管理员-管理员		2012-01-04 11:19:40.61	localhost	127.0.0.1	管理员	管理员	总部
509	用户登录	用户登录	总部-管理员-管理员		2012-01-04 14:05:34.093	localhost	127.0.0.1	管理员	管理员	总部
513	用户登录	用户登录	总部-管理员-管理员		2012-01-04 14:48:39.562	localhost	127.0.0.1	管理员	管理员	总部
528	用户登录	用户登录	总部-管理员-管理员		2012-01-04 16:23:01.421	localhost	127.0.0.1	管理员	管理员	总部
542	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:28:06.218	localhost	127.0.0.1	管理员	管理员	总部
547	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:47:13.171	localhost	127.0.0.1	管理员	管理员	总部
552	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:51:56.421	localhost	127.0.0.1	管理员	管理员	总部
556	用户登录	用户登录	总部-管理员-管理员		2012-01-05 17:32:00.89	localhost	127.0.0.1	管理员	管理员	总部
562	用户登录	用户登录	总部-管理员-管理员		2012-01-06 09:59:32.375	localhost	127.0.0.1	管理员	管理员	总部
602	角色组管理	启用	组名称：新组;组类型：roleGroup;	4	2012-01-06 13:52:40.125	localhost	127.0.0.1	管理员	管理员	总部
610	系统模块管理	修改	角色组管理;uum_roleGroupMgr;	48	2012-01-06 14:21:26.031	localhost	127.0.0.1	管理员	管理员	总部
620	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:41:16.156	localhost	127.0.0.1	管理员	管理员	总部
628	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:27:40.625	localhost	127.0.0.1	管理员	管理员	总部
636	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:51:29.718	localhost	127.0.0.1	管理员	管理员	总部
638	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:04:41.875	localhost	127.0.0.1	管理员	管理员	总部
640	省市地区管理	保存	海淀;district;	2002	2012-01-06 16:11:33.031	localhost	127.0.0.1	管理员	管理员	总部
642	省市地区管理	修改	海淀;district;	2002	2012-01-06 16:11:53.906	localhost	127.0.0.1	管理员	管理员	总部
643	省市地区管理	停用	海淀;district;	2002	2012-01-06 16:11:53.906	localhost	127.0.0.1	管理员	管理员	总部
664	用户登录	用户登录	总部-管理员-管理员		2012-01-06 17:26:58.734	localhost	127.0.0.1	管理员	管理员	总部
666	用户登录	用户登录	总部-管理员-管理员		2012-01-06 21:26:22.25	localhost	127.0.0.1	管理员	管理员	总部
670	用户登录	用户登录	总部-管理员-管理员		2012-01-08 16:11:48.531	localhost	127.0.0.1	管理员	管理员	总部
671	用户登录	用户登录	总部-管理员-管理员		2012-01-08 16:14:17.343	localhost	127.0.0.1	管理员	管理员	总部
673	系统模块管理	修改	待办任务;underwayTaskId;	61	2012-01-08 16:17:21.687	localhost	127.0.0.1	管理员	管理员	总部
674	系统模块管理	停用	待办任务;underwayTaskId;	61	2012-01-08 16:17:21.718	localhost	127.0.0.1	管理员	管理员	总部
694	主页面功能区	删除	删除功能区的id:;		2012-01-08 17:02:48.968	localhost	127.0.0.1	管理员	管理员	总部
695	主页面功能区	保存	增加的功能区信息 id:40;funName:header;funURI:general;funURIDesc:;funMemo:;	40	2012-01-08 17:02:48.984	localhost	127.0.0.1	管理员	管理员	总部
701	用户登录	用户登录	总部-管理员-管理员		2012-01-09 09:30:07.875	localhost	127.0.0.1	管理员	管理员	总部
703	系统模块操作管理	修改	分配test;atest;aaatest;	25	2012-01-09 09:30:57.234	localhost	127.0.0.1	管理员	管理员	总部
704	系统模块操作管理	删除	根据操作ID删除模块操作关系,级联删除权限	25	2012-01-09 09:30:57.375	localhost	127.0.0.1	管理员	管理员	总部
705	系统模块操作管理	停用	分配test;atest;aaatest;	25	2012-01-09 09:30:57.375	localhost	127.0.0.1	管理员	管理员	总部
717	用户登录	用户登录	总部-管理员-管理员		2012-01-09 09:50:41	localhost	127.0.0.1	管理员	管理员	总部
718	用户登录	用户登录	总部-管理员-管理员		2012-01-09 09:57:07.421	localhost	127.0.0.1	管理员	管理员	总部
724	系统调度管理	启动job	启动job:;jobName:postgresqlBbBackup;job类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;	3	2012-01-09 09:59:12.312	localhost	127.0.0.1	管理员	管理员	总部
727	用户登录	用户登录	总部-管理员-管理员		2012-01-09 10:06:13.078	localhost	127.0.0.1	管理员	管理员	总部
739	用户登录	用户登录	总部-管理员-管理员		2012-01-09 10:14:56.75	localhost	127.0.0.1	管理员	管理员	总部
743	类别字典管理	修改	字典大类名称：ab;	24	2012-01-09 10:19:44.5	localhost	127.0.0.1	管理员	管理员	总部
752	用户登录	用户登录	总部-管理员-管理员		2012-01-09 10:58:42.828	localhost	127.0.0.1	管理员	管理员	总部
753	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:00:17.937	localhost	127.0.0.1	管理员	管理员	总部
765	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:58:23.062	localhost	127.0.0.1	管理员	管理员	总部
774	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:28:08.468	localhost	127.0.0.1	管理员	管理员	总部
775	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:32:10.984	localhost	127.0.0.1	管理员	管理员	总部
778	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:41:30.765	localhost	127.0.0.1	管理员	管理员	总部
780	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:42:07.953	localhost	127.0.0.1	管理员	管理员	总部
786	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:48:10.171	localhost	127.0.0.1	管理员	管理员	总部
799	系统模块管理	修改	系统参数管理;systemParaMgr;	15	2012-01-09 15:00:08.234	localhost	127.0.0.1	管理员	管理员	总部
800	系统模块管理	启用	系统参数管理;systemParaMgr;	15	2012-01-09 15:00:08.234	localhost	127.0.0.1	管理员	管理员	总部
802	系统模块管理	修改	页面功能区管理;mainFrameFunMgr;	4	2012-01-09 15:00:39.984	localhost	127.0.0.1	管理员	管理员	总部
821	用户登录	用户登录	总部-管理员-管理员		2012-01-09 15:39:50.593	localhost	127.0.0.1	管理员	管理员	总部
826	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:36:04.062	localhost	127.0.0.1	管理员	管理员	总部
842	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：7,25		2012-01-09 17:01:53.75	localhost	127.0.0.1	管理员	管理员	总部
849	用户登录	用户登录	总部-管理员-管理员		2012-01-09 17:39:32.359	localhost	127.0.0.1	管理员	管理员	总部
851	用户登录	用户登录	总部-管理员-管理员		2012-01-10 09:56:51.734	localhost	127.0.0.1	管理员	管理员	总部
430	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:16:58.312	localhost	127.0.0.1	管理员	管理员	总部
497	用户登录	用户登录	总部-管理员-管理员		2012-01-04 11:41:09.079	localhost	127.0.0.1	管理员	管理员	总部
504	用户登录	用户登录	河北-经理在-jacob		2012-01-04 12:47:31.298	localhost	127.0.0.1	jacob	经理在	河北
514	用户登录	用户登录	总部-管理员-管理员		2012-01-04 14:50:46.14	localhost	127.0.0.1	管理员	管理员	总部
518	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:27:21.218	localhost	127.0.0.1	管理员	管理员	总部
543	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:31:16.546	localhost	127.0.0.1	管理员	管理员	总部
548	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:48:04.5	localhost	127.0.0.1	管理员	管理员	总部
558	用户登录	用户登录	总部-管理员-管理员		2012-01-05 17:39:34.39	localhost	127.0.0.1	管理员	管理员	总部
563	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:01:58.234	localhost	127.0.0.1	管理员	管理员	总部
599	角色组管理	保存	组名称：新组;组类型：roleGroup;	4	2012-01-06 13:52:17.437	localhost	127.0.0.1	管理员	管理员	总部
603	用户登录	用户登录	总部-管理员-管理员		2012-01-06 13:57:14.343	localhost	127.0.0.1	管理员	管理员	总部
611	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:26:09.312	localhost	127.0.0.1	管理员	管理员	总部
614	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:30:14.562	localhost	127.0.0.1	管理员	管理员	总部
621	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:45:43.765	localhost	127.0.0.1	管理员	管理员	总部
629	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:28:15.406	localhost	127.0.0.1	管理员	管理员	总部
635	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:50:29.875	localhost	127.0.0.1	管理员	管理员	总部
639	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:10:20.984	localhost	127.0.0.1	管理员	管理员	总部
650	角色组管理	启用	组名称：a;组类型：roleGroup;	5	2012-01-06 16:19:08.64	localhost	127.0.0.1	管理员	管理员	总部
667	用户登录	用户登录	总部-管理员-管理员		2012-01-06 21:30:00.765	localhost	127.0.0.1	管理员	管理员	总部
672	用户登录	用户登录	总部-管理员-管理员		2012-01-08 16:16:18.437	localhost	127.0.0.1	管理员	管理员	总部
677	系统模块管理	修改	系统公告管理;platform_afficheMgr;	49	2012-01-08 16:18:48.734	localhost	127.0.0.1	管理员	管理员	总部
686	系统编码管理	修改	编码英文名称：tt;编码中文名称:测试编码所属模块名称:测试编码效果:CKD-201201-001-zhangsan	25	2012-01-08 16:40:20.328	localhost	127.0.0.1	管理员	管理员	总部
687	系统编码管理	停用	编码停用：编码英文名称：tt;编码中文名称:测试编码所属模块名称:测试编码效果:CKD-201201-001-zhangsan	25	2012-01-08 16:40:20.328	localhost	127.0.0.1	管理员	管理员	总部
702	系统模块操作管理	修改	分配test;atest;aaatest;	25	2012-01-09 09:30:41.375	localhost	127.0.0.1	管理员	管理员	总部
719	系统调度管理	停止调度器	停止调度器		2012-01-09 09:57:30.421	localhost	127.0.0.1	管理员	管理员	总部
729	系统参数管理	修改	参数名称：testa;参数键值：test;参数值：test参数类型：uus	22	2012-01-09 10:06:48.812	localhost	127.0.0.1	管理员	管理员	总部
731	系统参数管理	修改	参数名称：test;参数键值：test;参数值：test参数类型：uus	22	2012-01-09 10:07:09.953	localhost	127.0.0.1	管理员	管理员	总部
732	系统参数管理	停用	参数名称：test;参数键值：test;参数值：test参数类型：uus	22	2012-01-09 10:07:09.953	localhost	127.0.0.1	管理员	管理员	总部
754	系统模块管理	修改	待办任务;underwayTaskId;	61	2012-01-09 11:00:30.453	localhost	127.0.0.1	管理员	管理员	总部
758	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:20:19.5	localhost	127.0.0.1	管理员	管理员	总部
760	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:23:50.515	localhost	127.0.0.1	管理员	管理员	总部
763	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:31:02.687	localhost	127.0.0.1	管理员	管理员	总部
772	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:25:16.203	localhost	127.0.0.1	管理员	管理员	总部
777	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:40:01.234	localhost	127.0.0.1	管理员	管理员	总部
801	系统模块管理	修改	系统编码管理;codeMgr;	13	2012-01-09 15:00:18.578	localhost	127.0.0.1	管理员	管理员	总部
827	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:36:31.968	localhost	127.0.0.1	管理员	管理员	总部
834	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:45:49.218	localhost	127.0.0.1	管理员	管理员	总部
843	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：7,25,14		2012-01-09 17:02:05.203	localhost	127.0.0.1	管理员	管理员	总部
852	用户登录	用户登录	总部-管理员-管理员		2012-01-10 09:57:04.343	localhost	127.0.0.1	管理员	管理员	总部
857	用户登录	用户登录	总部-管理员-管理员		2012-01-10 10:24:45.984	localhost	127.0.0.1	管理员	管理员	总部
860	用户登录	用户登录	总部-管理员-管理员		2012-01-10 10:34:40.265	localhost	127.0.0.1	管理员	管理员	总部
861	用户登录	用户登录	总部-管理员-管理员		2012-01-10 10:40:55.421	localhost	127.0.0.1	管理员	管理员	总部
867	用户登录	用户登录	总部-管理员-管理员		2012-01-10 13:33:52.171	localhost	127.0.0.1	管理员	管理员	总部
872	系统模块管理	修改	机构信息管理;organMgr;	32	2012-01-10 13:56:30.796	localhost	127.0.0.1	管理员	管理员	总部
876	类别字典管理	修改	字典大类名称：机构类型;	23	2012-01-10 14:11:17.453	localhost	127.0.0.1	管理员	管理员	总部
893	模块分配操作	保存	模块操作关系ID:	10;rolePri	2012-01-10 14:24:30.484	localhost	127.0.0.1	管理员	管理员	总部
894	角色管理	修改	测试;	10	2012-01-10 14:24:34.328	localhost	127.0.0.1	管理员	管理员	总部
895	角色管理	停用	测试;	10	2012-01-10 14:24:34.328	localhost	127.0.0.1	管理员	管理员	总部
898	角色分配	保存	机构id:2;角色id:10;		2012-01-10 14:31:00.437	localhost	127.0.0.1	管理员	管理员	总部
899	角色分配	保存	机构id:2;角色id:4;		2012-01-10 14:31:00.453	localhost	127.0.0.1	管理员	管理员	总部
900	角色分配	保存	机构id:2;角色id:5;		2012-01-10 14:31:00.468	localhost	127.0.0.1	管理员	管理员	总部
901	角色分配	删除	删除机构下的角色ID：5;	2	2012-01-10 14:31:27.078	localhost	127.0.0.1	管理员	管理员	总部
431	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:18:52.39	localhost	127.0.0.1	管理员	管理员	总部
432	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:19:19.625	localhost	127.0.0.1	管理员	管理员	总部
500	用户登录	用户登录	总部-管理员-管理员		2012-01-04 11:59:10.688	localhost	127.0.0.1	管理员	管理员	总部
516	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:01:35.125	localhost	127.0.0.1	管理员	管理员	总部
520	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:28:34.687	localhost	127.0.0.1	管理员	管理员	总部
525	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:32:44.062	localhost	127.0.0.1	管理员	管理员	总部
529	用户登录	用户登录	总部-管理员-管理员		2012-01-04 16:23:36.671	localhost	127.0.0.1	管理员	管理员	总部
533	用户登录	用户登录	总部-管理员-管理员		2012-01-04 17:26:35.765	localhost	127.0.0.1	管理员	管理员	总部
544	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:37:06.968	localhost	127.0.0.1	管理员	管理员	总部
549	用户登录	用户登录	总部-管理员-管理员		2012-01-05 16:48:59.25	localhost	127.0.0.1	管理员	管理员	总部
559	用户登录	用户登录	总部-管理员-管理员		2012-01-05 17:42:04.406	localhost	127.0.0.1	管理员	管理员	总部
564	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:02:22.687	localhost	127.0.0.1	管理员	管理员	总部
604	角色组管理	保存	组名称：a;组类型：roleGroup;	5	2012-01-06 13:58:09.015	localhost	127.0.0.1	管理员	管理员	总部
612	系统模块管理	修改	省市地区;platform_priCityMgr;	52	2012-01-06 14:28:32.718	localhost	127.0.0.1	管理员	管理员	总部
622	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:52:20.281	localhost	127.0.0.1	管理员	管理员	总部
630	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:32:36.89	localhost	127.0.0.1	管理员	管理员	总部
648	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:18:15.296	localhost	127.0.0.1	管理员	管理员	总部
653	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:42:00.812	localhost	127.0.0.1	管理员	管理员	总部
660	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2012-01-06 16:49:51.625	localhost	127.0.0.1	管理员	管理员	总部
662	用户登录	用户登录	总部-管理员-管理员		2012-01-06 17:11:27.468	localhost	127.0.0.1	管理员	管理员	总部
663	用户登录	用户登录	总部-管理员-管理员		2012-01-06 17:24:01.406	localhost	127.0.0.1	管理员	管理员	总部
675	系统模块管理	修改	待办任务;underwayTaskId;	61	2012-01-08 16:17:24.968	localhost	127.0.0.1	管理员	管理员	总部
676	系统模块管理	启用	待办任务;underwayTaskId;	61	2012-01-08 16:17:24.968	localhost	127.0.0.1	管理员	管理员	总部
680	系统公告管理	修改	修改公告ID：5;修改公告标题：testf;有效期：Sun Jan 08 00:00:00 CST 2012;是否发布：0;	5	2012-01-08 16:27:31.515	localhost	127.0.0.1	管理员	管理员	总部
682	系统模块管理	修改	系统编码管理;platform_codMgr;	13	2012-01-08 16:28:53.375	localhost	127.0.0.1	管理员	管理员	总部
692	系统模块管理	修改	页面功能区管理;platform_mainFrameFunMgr;	4	2012-01-08 16:52:01.25	localhost	127.0.0.1	管理员	管理员	总部
696	主页面功能区	删除	删除功能区的id:40;		2012-01-08 17:02:59.281	localhost	127.0.0.1	管理员	管理员	总部
697	主页面功能区	保存	增加的功能区信息 id:40;funName:header;funURI:general;funURIDesc:f;funMemo:;	40	2012-01-08 17:02:59.296	localhost	127.0.0.1	管理员	管理员	总部
706	系统模块操作管理	修改	分配test;atest;aaatest;	25	2012-01-09 09:31:00.75	localhost	127.0.0.1	管理员	管理员	总部
707	系统模块操作管理	启用	分配test;atest;aaatest;	25	2012-01-09 09:31:00.765	localhost	127.0.0.1	管理员	管理员	总部
711	用户登录	用户登录	总部-管理员-管理员		2012-01-09 09:45:48.156	localhost	127.0.0.1	管理员	管理员	总部
720	系统调度管理	启动调度器	启动调度器并初始化任务		2012-01-09 09:57:39.171	localhost	127.0.0.1	管理员	管理员	总部
733	系统参数管理	修改	参数名称：test;参数键值：test;参数值：test参数类型：uus	22	2012-01-09 10:07:12.453	localhost	127.0.0.1	管理员	管理员	总部
734	系统参数管理	启用	参数名称：test;参数键值：test;参数值：test参数类型：uus	22	2012-01-09 10:07:12.453	localhost	127.0.0.1	管理员	管理员	总部
742	用户登录	用户登录	总部-管理员-管理员		2012-01-09 10:19:07.39	localhost	127.0.0.1	管理员	管理员	总部
745	类别字典管理	停用	字典大类名称：ab;	24	2012-01-09 10:20:33.093	localhost	127.0.0.1	管理员	管理员	总部
749	系统参数管理	修改	参数名称：用户默认密码;参数键值：userdefaultpassword;参数值：123456参数类型：uum	1	2012-01-09 10:54:07.703	localhost	127.0.0.1	管理员	管理员	总部
755	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:08:18.968	localhost	127.0.0.1	管理员	管理员	总部
756	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:10:39.343	localhost	127.0.0.1	管理员	管理员	总部
761	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:26:34.671	localhost	127.0.0.1	管理员	管理员	总部
768	用户登录	用户登录	总部-管理员-管理员		2012-01-09 13:56:10.234	localhost	127.0.0.1	管理员	管理员	总部
770	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:22:25.203	localhost	127.0.0.1	管理员	管理员	总部
779	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:41:56.062	localhost	127.0.0.1	管理员	管理员	总部
783	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:43:52.562	localhost	127.0.0.1	管理员	管理员	总部
790	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:51:49.125	localhost	127.0.0.1	管理员	管理员	总部
803	系统模块管理	修改	操作按钮管理;operateMgr;	8	2012-01-09 15:00:53.781	localhost	127.0.0.1	管理员	管理员	总部
815	用户登录	用户登录	总部-管理员-管理员		2012-01-09 15:03:58.906	localhost	127.0.0.1	管理员	管理员	总部
818	用户登录	用户登录	总部-管理员-管理员		2012-01-09 15:08:08.375	localhost	127.0.0.1	管理员	管理员	总部
822	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:02:51.5	localhost	127.0.0.1	管理员	管理员	总部
829	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:39:14.14	localhost	127.0.0.1	管理员	管理员	总部
833	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:43:50.828	localhost	127.0.0.1	管理员	管理员	总部
904	用户分配角色	保存	26	13	2012-01-10 14:55:24.89	localhost	127.0.0.1	管理员	管理员	总部
434	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:19:38	localhost	127.0.0.1	管理员	管理员	总部
435	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:25:53.828	localhost	127.0.0.1	管理员	管理员	总部
436	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:26:06.718	localhost	127.0.0.1	管理员	管理员	总部
437	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:26:28.781	localhost	127.0.0.1	管理员	管理员	总部
438	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:26:31.812	localhost	127.0.0.1	管理员	管理员	总部
439	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:26:35.625	localhost	127.0.0.1	管理员	管理员	总部
440	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:26:38.437	localhost	127.0.0.1	管理员	管理员	总部
441	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:26:47.343	localhost	127.0.0.1	管理员	管理员	总部
442	用户登录	用户登录	总部-管理员-管理员		2011-12-29 14:29:34.968	localhost	127.0.0.1	管理员	管理员	总部
443	用户登录	用户登录	总部-管理员-管理员		2011-12-29 16:04:53.484	localhost	127.0.0.1	管理员	管理员	总部
444	用户登录	用户登录	总部-管理员-管理员		2011-12-29 16:09:24.031	localhost	127.0.0.1	管理员	管理员	总部
445	用户登录	用户登录	总部-管理员-管理员		2011-12-29 16:09:33.875	localhost	127.0.0.1	管理员	管理员	总部
446	用户登录	用户登录	总部-管理员-管理员		2011-12-29 16:19:27.812	localhost	127.0.0.1	管理员	管理员	总部
447	用户登录	用户登录	总部-管理员-管理员		2011-12-29 16:53:27.687	localhost	127.0.0.1	管理员	管理员	总部
448	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:02:38.312	localhost	127.0.0.1	jacob	经理在	河北
449	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:02:55.125	localhost	127.0.0.1	jacob	经理在	河北
450	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:03:03.906	localhost	127.0.0.1	jacob	经理在	河北
451	用户登录	用户登录	总部-管理员-管理员		2011-12-29 17:04:40.375	localhost	127.0.0.1	管理员	管理员	总部
452	用户登录	用户登录	总部-管理员-管理员		2011-12-29 17:04:43.125	localhost	127.0.0.1	管理员	管理员	总部
453	用户登录	用户登录	总部-管理员-管理员		2011-12-29 17:04:44.703	localhost	127.0.0.1	管理员	管理员	总部
454	用户登录	用户登录	总部-管理员-管理员		2011-12-29 17:04:49.14	localhost	127.0.0.1	管理员	管理员	总部
455	用户登录	用户登录	总部-管理员-管理员		2011-12-29 17:04:50.578	localhost	127.0.0.1	管理员	管理员	总部
456	用户登录	用户登录	总部-管理员-管理员		2011-12-29 17:06:40.015	localhost	127.0.0.1	管理员	管理员	总部
457	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:06:57.14	localhost	127.0.0.1	jacob	经理在	河北
458	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:13:09.437	localhost	127.0.0.1	jacob	经理在	河北
459	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:13:11.671	localhost	127.0.0.1	jacob	经理在	河北
460	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:13:17.234	localhost	127.0.0.1	jacob	经理在	河北
461	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:13:23.343	localhost	127.0.0.1	jacob	经理在	河北
462	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:13:24.921	localhost	127.0.0.1	jacob	经理在	河北
463	用户登录	用户登录	河北-经理在-jacob		2011-12-29 17:13:27.234	localhost	127.0.0.1	jacob	经理在	河北
464	用户登录	用户登录	总部-管理员-管理员		2011-12-29 17:14:28.921	localhost	127.0.0.1	管理员	管理员	总部
465	用户登录	用户登录	总部-管理员-管理员		2011-12-29 17:56:56.25	localhost	127.0.0.1	管理员	管理员	总部
466	用户登录	用户登录	总部-管理员-管理员		2011-12-29 17:59:42.296	localhost	127.0.0.1	管理员	管理员	总部
467	用户登录	用户登录	总部-管理员-管理员		2011-12-30 16:48:10.984	localhost	127.0.0.1	管理员	管理员	总部
468	用户登录	用户登录	总部-管理员-管理员		2011-12-30 16:51:06.562	localhost	127.0.0.1	管理员	管理员	总部
469	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户a;		2011-12-30 16:51:24.062	localhost	127.0.0.1	管理员	管理员	总部
470	系统配置管理	保存	设置角色切换:login;		2011-12-30 16:51:28.531	localhost	127.0.0.1	管理员	管理员	总部
471	系统配置管理	保存	设置日志级别:2;		2011-12-30 16:51:37.437	localhost	127.0.0.1	管理员	管理员	总部
472	系统配置管理	保存	设置日志保留天数:360;		2011-12-30 16:51:43.609	localhost	127.0.0.1	管理员	管理员	总部
473	系统配置管理	保存	增加管理员:jacob;		2011-12-30 16:51:51.343	localhost	127.0.0.1	管理员	管理员	总部
474	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2011-12-30 16:52:18.296	localhost	127.0.0.1	管理员	管理员	总部
475	系统配置管理	保存	设置角色切换:insystem;		2011-12-30 16:52:22.046	localhost	127.0.0.1	管理员	管理员	总部
476	系统配置管理	保存	设置日志级别:1;		2011-12-30 16:52:24.437	localhost	127.0.0.1	管理员	管理员	总部
477	用户登录	用户登录	总部-管理员-管理员		2011-12-30 16:52:31.437	localhost	127.0.0.1	管理员	管理员	总部
478	用户登录	用户登录	总部-管理员-管理员		2011-12-30 17:03:14.984	localhost	127.0.0.1	管理员	管理员	总部
479	用户登录	用户登录	总部-管理员-管理员		2011-12-30 17:05:59.187	localhost	127.0.0.1	管理员	管理员	总部
480	用户登录	用户登录	总部-管理员-管理员		2011-12-30 17:06:01.765	localhost	127.0.0.1	管理员	管理员	总部
481	用户登录	用户登录	总部-管理员-管理员		2011-12-30 17:06:03.765	localhost	127.0.0.1	管理员	管理员	总部
482	用户登录	用户登录	总部-管理员-管理员		2011-12-30 17:06:05.578	localhost	127.0.0.1	管理员	管理员	总部
483	用户登录	用户登录	河北-经理在-jacob		2011-12-30 17:07:40.609	localhost	127.0.0.1	jacob	经理在	河北
484	系统配置管理	保存	删除管理员:jacob;删除管理员:jacob;		2011-12-30 17:07:54.578	localhost	127.0.0.1	jacob	经理在	河北
485	用户登录	用户登录	河北-经理在-jacob		2011-12-30 17:08:07.812	localhost	127.0.0.1	jacob	经理在	河北
486	用户登录	用户登录	总部-管理员-管理员		2011-12-30 17:08:26.906	localhost	127.0.0.1	管理员	管理员	总部
487	模块分配操作	保存	模块操作关系ID:44;	4;rolePri	2011-12-30 17:08:51.328	localhost	127.0.0.1	管理员	管理员	总部
488	角色分配	保存	机构id:1;角色id:4;		2011-12-30 17:09:26.875	localhost	127.0.0.1	管理员	管理员	总部
491	用户登录	用户登录	河北-会计-jacob		2011-12-30 17:10:01.937	localhost	127.0.0.1	jacob	会计	河北
502	用户登录	用户登录	总部-管理员-管理员		2012-01-04 12:45:42.891	localhost	127.0.0.1	管理员	管理员	总部
505	用户登录	用户登录	总部-管理员-管理员		2012-01-04 12:48:04.204	localhost	127.0.0.1	管理员	管理员	总部
517	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:25:15.5	localhost	127.0.0.1	管理员	管理员	总部
524	用户管理	重置密码	重置密码用户：jacob	12	2012-01-04 15:31:15.468	localhost	127.0.0.1	管理员	管理员	总部
538	用户登录	用户登录	总部-管理员-管理员		2012-01-04 17:34:36.125	localhost	127.0.0.1	管理员	管理员	总部
553	用户登录	用户登录	总部-管理员-管理员		2012-01-05 17:24:44.046	localhost	127.0.0.1	管理员	管理员	总部
569	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:31:11.437	localhost	127.0.0.1	管理员	管理员	总部
570	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:31:53.484	localhost	127.0.0.1	管理员	管理员	总部
571	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:33:13.546	localhost	127.0.0.1	管理员	管理员	总部
572	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:36:08.765	localhost	127.0.0.1	管理员	管理员	总部
573	用户登录	用户登录	总部-管理员-管理员		2012-01-06 10:36:32.921	localhost	127.0.0.1	管理员	管理员	总部
589	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:29:57.328	localhost	127.0.0.1	管理员	管理员	总部
590	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:30:48.109	localhost	127.0.0.1	管理员	管理员	总部
591	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:33:30.046	localhost	127.0.0.1	管理员	管理员	总部
593	用户登录	用户登录	总部-管理员-管理员		2012-01-06 13:37:18.578	localhost	127.0.0.1	管理员	管理员	总部
594	用户登录	用户登录	总部-管理员-管理员		2012-01-06 13:38:48.843	localhost	127.0.0.1	管理员	管理员	总部
595	用户登录	用户登录	总部-管理员-管理员		2012-01-06 13:40:34.921	localhost	127.0.0.1	管理员	管理员	总部
596	用户登录	用户登录	总部-管理员-管理员		2012-01-06 13:43:13.515	localhost	127.0.0.1	管理员	管理员	总部
597	用户登录	用户登录	总部-管理员-管理员		2012-01-06 13:48:53.109	localhost	127.0.0.1	管理员	管理员	总部
598	用户登录	用户登录	总部-管理员-管理员		2012-01-06 13:51:31.593	localhost	127.0.0.1	管理员	管理员	总部
605	角色组管理	修改	组名称：a;组类型：roleGroup;	5	2012-01-06 13:58:56.89	localhost	127.0.0.1	管理员	管理员	总部
613	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:28:51.656	localhost	127.0.0.1	管理员	管理员	总部
623	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:56:06.609	localhost	127.0.0.1	管理员	管理员	总部
632	用户登录	用户登录	总部-管理员-管理员		2012-01-06 15:36:54.843	localhost	127.0.0.1	管理员	管理员	总部
641	省市地区管理	修改	海淀;district;	2002	2012-01-06 16:11:45.75	localhost	127.0.0.1	管理员	管理员	总部
649	角色组管理	停用	组名称：a;组类型：roleGroup;	5	2012-01-06 16:19:05.343	localhost	127.0.0.1	管理员	管理员	总部
654	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:42:26.687	localhost	127.0.0.1	管理员	管理员	总部
679	系统公告管理	保存	保存公告标题：test;有效期限:Sun Jan 08 00:00:00 CST 2012	5	2012-01-08 16:27:18.796	localhost	127.0.0.1	管理员	管理员	总部
708	系统模块操作管理	保存	test;test;test;	26	2012-01-09 09:31:19.203	localhost	127.0.0.1	管理员	管理员	总部
712	用户登录	用户登录	总部-管理员-管理员		2012-01-09 09:47:08.046	localhost	127.0.0.1	管理员	管理员	总部
714	系统调度管理	启动job	启动job:;jobName:postgresqlBbBackup;job类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;	3	2012-01-09 09:47:45.562	localhost	127.0.0.1	管理员	管理员	总部
735	系统参数管理	修改	参数名称：test;参数键值：test;参数值：test参数类型：uus	22	2012-01-09 10:07:15.171	localhost	127.0.0.1	管理员	管理员	总部
736	系统参数管理	停用	参数名称：test;参数键值：test;参数值：test参数类型：uus	22	2012-01-09 10:07:15.171	localhost	127.0.0.1	管理员	管理员	总部
746	类别字典管理	启用	字典大类名称：ab;	24	2012-01-09 10:20:35.984	localhost	127.0.0.1	管理员	管理员	总部
757	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:11:04.906	localhost	127.0.0.1	管理员	管理员	总部
784	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:44:24.375	localhost	127.0.0.1	管理员	管理员	总部
785	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:48:02.062	localhost	127.0.0.1	管理员	管理员	总部
791	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:52:41	localhost	127.0.0.1	管理员	管理员	总部
804	系统模块管理	修改	省市地区管理;priCityMgr;	14	2012-01-09 15:01:08.14	localhost	127.0.0.1	管理员	管理员	总部
828	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:37:54.234	localhost	127.0.0.1	管理员	管理员	总部
831	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:41:13.218	localhost	127.0.0.1	管理员	管理员	总部
835	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:47:58.265	localhost	127.0.0.1	管理员	管理员	总部
844	用户登录	用户登录	总部-管理员-管理员		2012-01-09 17:18:46.421	localhost	127.0.0.1	管理员	管理员	总部
853	用户登录	用户登录	总部-管理员-管理员		2012-01-10 10:00:39.468	localhost	127.0.0.1	管理员	管理员	总部
863	用户登录	用户登录	总部-管理员-管理员		2012-01-10 13:23:37.843	localhost	127.0.0.1	管理员	管理员	总部
864	用户登录	用户登录	总部-管理员-管理员		2012-01-10 13:24:55.437	localhost	127.0.0.1	管理员	管理员	总部
868	用户登录	用户登录	总部-管理员-管理员		2012-01-10 13:36:01.531	localhost	127.0.0.1	管理员	管理员	总部
873	用户登录	用户登录	总部-管理员-管理员		2012-01-10 14:04:51.843	localhost	127.0.0.1	管理员	管理员	总部
874	用户登录	用户登录	总部-管理员-管理员		2012-01-10 14:06:13.953	localhost	127.0.0.1	管理员	管理员	总部
878	机构管理	修改	机构名称：北京;机构代码：;上级机构：总部;	2	2012-01-10 14:12:07.593	localhost	127.0.0.1	管理员	管理员	总部
884	系统模块管理	修改	角色信息管理;roleMgr;	33	2012-01-10 14:21:55.375	localhost	127.0.0.1	管理员	管理员	总部
490	用户登录	用户登录	河北-经理在-jacob		2011-12-30 17:09:49.14	localhost	127.0.0.1	jacob	经理在	河北
503	用户登录	用户登录	总部-管理员-管理员		2012-01-04 12:46:59.235	localhost	127.0.0.1	管理员	管理员	总部
507	用户登录	用户登录	总部-管理员-管理员		2012-01-04 13:53:16.39	localhost	127.0.0.1	管理员	管理员	总部
519	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:28:28.046	localhost	127.0.0.1	管理员	管理员	总部
537	用户登录	用户登录	总部-管理员-管理员		2012-01-04 17:33:48.421	localhost	127.0.0.1	管理员	管理员	总部
554	用户登录	用户登录	总部-管理员-管理员		2012-01-05 17:25:43.296	localhost	127.0.0.1	管理员	管理员	总部
578	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:04:43.765	localhost	127.0.0.1	管理员	管理员	总部
579	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:05:54.609	localhost	127.0.0.1	管理员	管理员	总部
580	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:06:37.171	localhost	127.0.0.1	管理员	管理员	总部
606	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:04:13.375	localhost	127.0.0.1	管理员	管理员	总部
615	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:31:40.312	localhost	127.0.0.1	管理员	管理员	总部
624	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:58:29.828	localhost	127.0.0.1	管理员	管理员	总部
637	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:03:12.343	localhost	127.0.0.1	管理员	管理员	总部
651	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:23:51.437	localhost	127.0.0.1	管理员	管理员	总部
658	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:49:04.89	localhost	127.0.0.1	管理员	管理员	总部
661	用户登录	用户登录	总部-管理员-管理员		2012-01-06 17:10:18.718	localhost	127.0.0.1	管理员	管理员	总部
681	系统公告管理	删除	删除的公告ID：5		2012-01-08 16:27:42.359	localhost	127.0.0.1	管理员	管理员	总部
693	用户登录	用户登录	总部-管理员-管理员		2012-01-08 16:57:51	localhost	127.0.0.1	管理员	管理员	总部
709	系统模块操作管理	保存	test2;;;	27	2012-01-09 09:31:26.234	localhost	127.0.0.1	管理员	管理员	总部
737	用户登录	用户登录	总部-管理员-管理员		2012-01-09 10:08:28.343	localhost	127.0.0.1	管理员	管理员	总部
741	类别字典管理	保存	字典大类名称：a;	24	2012-01-09 10:17:07.484	localhost	127.0.0.1	管理员	管理员	总部
762	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:26:53.015	localhost	127.0.0.1	管理员	管理员	总部
771	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:23:09.281	localhost	127.0.0.1	管理员	管理员	总部
782	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:42:53.75	localhost	127.0.0.1	管理员	管理员	总部
787	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:48:44.968	localhost	127.0.0.1	管理员	管理员	总部
792	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:54:13.625	localhost	127.0.0.1	管理员	管理员	总部
805	系统模块管理	修改	类别字典管理;typeDicMgr;	10	2012-01-09 15:01:39.156	localhost	127.0.0.1	管理员	管理员	总部
823	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:03:27.203	localhost	127.0.0.1	管理员	管理员	总部
830	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:40:00.281	localhost	127.0.0.1	管理员	管理员	总部
836	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：7,25,53		2012-01-09 16:54:11.015	localhost	127.0.0.1	管理员	管理员	总部
837	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:55:23.406	localhost	127.0.0.1	管理员	管理员	总部
838	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：7,25		2012-01-09 16:55:35.343	localhost	127.0.0.1	管理员	管理员	总部
840	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:55:56.906	localhost	127.0.0.1	管理员	管理员	总部
845	用户登录	用户登录	总部-管理员-管理员		2012-01-09 17:33:32.187	localhost	127.0.0.1	管理员	管理员	总部
847	用户登录	用户登录	总部-管理员-管理员		2012-01-09 17:35:08.796	localhost	127.0.0.1	管理员	管理员	总部
854	用户登录	用户登录	总部-管理员-管理员		2012-01-10 10:14:20.343	localhost	127.0.0.1	管理员	管理员	总部
855	用户登录	用户登录	总部-管理员-管理员		2012-01-10 10:24:06.406	localhost	127.0.0.1	管理员	管理员	总部
858	用户登录	用户登录	总部-管理员-管理员		2012-01-10 10:28:32.281	localhost	127.0.0.1	管理员	管理员	总部
865	用户登录	用户登录	总部-管理员-管理员		2012-01-10 13:26:15.125	localhost	127.0.0.1	管理员	管理员	总部
869	用户登录	用户登录	总部-管理员-管理员		2012-01-10 13:38:52.25	localhost	127.0.0.1	管理员	管理员	总部
871	用户登录	用户登录	总部-管理员-管理员		2012-01-10 13:53:56.437	localhost	127.0.0.1	管理员	管理员	总部
875	用户登录	用户登录	总部-管理员-管理员		2012-01-10 14:07:59.515	localhost	127.0.0.1	管理员	管理员	总部
877	机构管理	保存	机构名称：北京;机构代码：;上级机构：总部;	2	2012-01-10 14:11:58.281	localhost	127.0.0.1	管理员	管理员	总部
879	机构管理	修改	机构名称：北京;机构代码：;上级机构：总部;	2	2012-01-10 14:12:12.703	localhost	127.0.0.1	管理员	管理员	总部
880	机构管理	停用	机构名称：北京;机构代码：;上级机构：总部;	2	2012-01-10 14:12:12.703	localhost	127.0.0.1	管理员	管理员	总部
881	机构管理	修改	机构名称：北京;机构代码：;上级机构：总部;	2	2012-01-10 14:12:15.718	localhost	127.0.0.1	管理员	管理员	总部
882	机构管理	启用	机构名称：北京;机构代码：;上级机构：总部;	2	2012-01-10 14:12:15.718	localhost	127.0.0.1	管理员	管理员	总部
886	角色管理	保存	测试;	10	2012-01-10 14:23:10.765	localhost	127.0.0.1	管理员	管理员	总部
888	角色管理	修改	测试;	10	2012-01-10 14:23:41.671	localhost	127.0.0.1	管理员	管理员	总部
889	角色管理	停用	测试;	10	2012-01-10 14:23:41.671	localhost	127.0.0.1	管理员	管理员	总部
890	角色管理	修改	测试;	10	2012-01-10 14:23:45.531	localhost	127.0.0.1	管理员	管理员	总部
891	角色管理	启用	测试;	10	2012-01-10 14:23:45.531	localhost	127.0.0.1	管理员	管理员	总部
896	系统模块管理	修改	角色分配管理;roleOrganMgr;	35	2012-01-10 14:26:01.375	localhost	127.0.0.1	管理员	管理员	总部
897	用户登录	用户登录	总部-管理员-管理员		2012-01-10 14:30:31.312	localhost	127.0.0.1	管理员	管理员	总部
492	用户登录	用户登录	河北-会计-jacob		2011-12-30 17:10:15.937	localhost	127.0.0.1	jacob	会计	河北
493	用户登录	用户登录	河北-会计-jacob		2011-12-30 17:10:18.078	localhost	127.0.0.1	jacob	会计	河北
506	用户登录	用户登录	总部-管理员-管理员		2012-01-04 13:28:35.062	localhost	127.0.0.1	管理员	管理员	总部
508	模块分配操作	保存	模块分配操作;	7	2012-01-04 14:05:27.718	localhost	127.0.0.1	管理员	管理员	总部
521	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:28:49.265	localhost	127.0.0.1	管理员	管理员	总部
526	用户登录	用户登录	总部-管理员-管理员		2012-01-04 15:45:43.5	localhost	127.0.0.1	管理员	管理员	总部
530	用户登录	用户登录	总部-管理员-管理员		2012-01-04 16:53:28.5	localhost	127.0.0.1	管理员	管理员	总部
557	用户登录	用户登录	总部-管理员-管理员		2012-01-05 17:36:20.781	localhost	127.0.0.1	管理员	管理员	总部
581	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:18:05.562	localhost	127.0.0.1	管理员	管理员	总部
582	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:20:43.5	localhost	127.0.0.1	管理员	管理员	总部
583	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:22:06.437	localhost	127.0.0.1	管理员	管理员	总部
584	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:24:26.125	localhost	127.0.0.1	管理员	管理员	总部
585	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:25:05.078	localhost	127.0.0.1	管理员	管理员	总部
586	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:27:30.468	localhost	127.0.0.1	管理员	管理员	总部
587	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:28:24.218	localhost	127.0.0.1	管理员	管理员	总部
588	用户登录	用户登录	总部-管理员-管理员		2012-01-06 11:29:25.734	localhost	127.0.0.1	管理员	管理员	总部
607	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:17:35.937	localhost	127.0.0.1	管理员	管理员	总部
617	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:36:43.578	localhost	127.0.0.1	管理员	管理员	总部
625	用户登录	用户登录	总部-管理员-管理员		2012-01-06 14:59:14.437	localhost	127.0.0.1	管理员	管理员	总部
644	省市地区管理	修改	海淀;district;	2002	2012-01-06 16:12:06.203	localhost	127.0.0.1	管理员	管理员	总部
645	省市地区管理	启用	海淀;district;	2002	2012-01-06 16:12:06.203	localhost	127.0.0.1	管理员	管理员	总部
655	用户登录	用户登录	总部-管理员-管理员		2012-01-06 16:44:47.703	localhost	127.0.0.1	管理员	管理员	总部
659	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户a;		2012-01-06 16:49:47.875	localhost	127.0.0.1	管理员	管理员	总部
683	系统模块管理	修改	系统编码管理;platform_codeMgr;	13	2012-01-08 16:29:20.656	localhost	127.0.0.1	管理员	管理员	总部
685	系统编码管理	保存	编码英文名称：tt;编码中文名称:测试;编码所属模块名称:测试;编码效果:CKD-201201-001-zhangsan;	25	2012-01-08 16:40:01.921	localhost	127.0.0.1	管理员	管理员	总部
690	系统模块管理	修改	系统日志管理;platform_sysLogMgr;	18	2012-01-08 16:41:46.359	localhost	127.0.0.1	管理员	管理员	总部
713	系统调度管理	修改	任务名称：postgresqlBbBackup;任务类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;时间表达式:0/10 * * * * ?;	3	2012-01-09 09:47:30.984	localhost	127.0.0.1	管理员	管理员	总部
738	系统模块管理	修改	类别字典管理;platform_typeDicMgr;	10	2012-01-09 10:09:09.703	localhost	127.0.0.1	管理员	管理员	总部
740	用户登录	用户登录	总部-管理员-管理员		2012-01-09 10:16:06.953	localhost	127.0.0.1	管理员	管理员	总部
744	用户登录	用户登录	总部-管理员-管理员		2012-01-09 10:20:20.734	localhost	127.0.0.1	管理员	管理员	总部
748	系统参数管理	修改	参数名称：用户有效期;参数键值：uservalidate;参数值：6参数类型：uum	2	2012-01-09 10:54:00.25	localhost	127.0.0.1	管理员	管理员	总部
750	系统参数管理	修改	参数名称：test;参数键值：test;参数值：test参数类型：platform	22	2012-01-09 10:54:14.875	localhost	127.0.0.1	管理员	管理员	总部
764	用户登录	用户登录	总部-管理员-管理员		2012-01-09 11:56:44.718	localhost	127.0.0.1	管理员	管理员	总部
776	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:35:43.296	localhost	127.0.0.1	管理员	管理员	总部
788	用户登录	用户登录	总部-管理员-管理员		2012-01-09 14:49:17.375	localhost	127.0.0.1	管理员	管理员	总部
806	系统模块管理	修改	系统日志管理;sysLogMgr;	18	2012-01-09 15:02:10.187	localhost	127.0.0.1	管理员	管理员	总部
824	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:33:50.312	localhost	127.0.0.1	管理员	管理员	总部
832	用户登录	用户登录	总部-管理员-管理员		2012-01-09 16:43:07.312	localhost	127.0.0.1	管理员	管理员	总部
839	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：7,25,14		2012-01-09 16:55:44.265	localhost	127.0.0.1	管理员	管理员	总部
841	用户登录	用户登录	总部-管理员-管理员		2012-01-09 17:01:28.687	localhost	127.0.0.1	管理员	管理员	总部
846	用户登录	用户登录	总部-管理员-管理员		2012-01-09 17:34:30.765	localhost	127.0.0.1	管理员	管理员	总部
848	用户登录	用户登录	总部-管理员-管理员		2012-01-09 17:38:54.5	localhost	127.0.0.1	管理员	管理员	总部
850	用户登录	用户登录	总部-管理员-管理员		2012-01-10 09:20:02.625	localhost	127.0.0.1	管理员	管理员	总部
856	系统模块管理	修改	角色组管理;roleGroupMgr;	48	2012-01-10 10:24:29.734	localhost	127.0.0.1	管理员	管理员	总部
859	用户登录	用户登录	总部-管理员-管理员		2012-01-10 10:29:37.89	localhost	127.0.0.1	管理员	管理员	总部
862	系统模块管理	修改	普通组管理;generalGroupMgr;	28	2012-01-10 10:51:11.015	localhost	127.0.0.1	管理员	管理员	总部
866	用户登录	用户登录	总部-管理员-管理员		2012-01-10 13:28:54.515	localhost	127.0.0.1	管理员	管理员	总部
870	系统模块管理	修改	用户组管理;userGroupMgr;	27	2012-01-10 13:46:14.406	localhost	127.0.0.1	管理员	管理员	总部
883	用户登录	用户登录	总部-管理员-管理员		2012-01-10 14:20:51.781	localhost	127.0.0.1	管理员	管理员	总部
885	用户登录	用户登录	总部-管理员-管理员		2012-01-10 14:22:41.453	localhost	127.0.0.1	管理员	管理员	总部
887	角色管理	修改	测试;	10	2012-01-10 14:23:31.843	localhost	127.0.0.1	管理员	管理员	总部
892	模块分配操作	保存	模块操作关系ID:45;49;	10;rolePri	2012-01-10 14:24:07.375	localhost	127.0.0.1	管理员	管理员	总部
902	系统模块管理	修改	用户信息管理;userMgr;	34	2012-01-10 14:33:12.5	localhost	127.0.0.1	管理员	管理员	总部
906	用户管理	修改	jacob_liang;jacob;null;	13	2012-01-10 14:55:32.468	localhost	127.0.0.1	管理员	管理员	总部
919	用户登录	用户登录	总部-管理员-管理员		2012-01-11 10:24:40.935	localhost	127.0.0.1	管理员	管理员	总部
923	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:04:50.359	localhost	127.0.0.1	管理员	管理员	总部
936	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:44:45.25	localhost	127.0.0.1	管理员	管理员	总部
939	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:48:15.109	localhost	127.0.0.1	管理员	管理员	总部
949	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:07:09.062	localhost	127.0.0.1	管理员	管理员	总部
954	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:11:43.265	localhost	127.0.0.1	管理员	管理员	总部
958	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:24:40.984	localhost	127.0.0.1	管理员	管理员	总部
971	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:06:50.281	localhost	127.0.0.1	管理员	管理员	总部
981	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:16:52.671	localhost	127.0.0.1	管理员	管理员	总部
987	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:24:56.734	localhost	127.0.0.1	管理员	管理员	总部
1011	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:45:04.968	localhost	127.0.0.1	jacob	经理在	河北
1021	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:50:51.5	localhost	127.0.0.1	管理员	管理员	总部
1032	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:06:27.906	localhost	127.0.0.1	管理员	管理员	总部
1036	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:12:40.781	localhost	127.0.0.1	管理员	管理员	总部
1043	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:22:13.187	localhost	127.0.0.1	管理员	管理员	总部
1050	用户登录	用户登录	总部-管理员-管理员		2012-01-12 09:39:56.812	localhost	127.0.0.1	管理员	管理员	总部
1060	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:02:03.703	localhost	127.0.0.1	管理员	管理员	总部
1066	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:15:53.078	localhost	127.0.0.1	管理员	管理员	总部
1074	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:01:55.468	localhost	127.0.0.1	管理员	管理员	总部
1075	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:02:23.515	localhost	127.0.0.1	管理员	管理员	总部
1078	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：7,14,25,13		2012-01-12 11:14:03.093	localhost	127.0.0.1	管理员	管理员	总部
1080	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：7,14,25		2012-01-12 11:20:05.328	localhost	127.0.0.1	管理员	管理员	总部
1120	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:30:23.828	localhost	127.0.0.1	管理员	管理员	总部
1123	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;64;91;97;65;66;68;69;116;117;118;119;112;113;114;115;	10;rolePri	2012-01-12 17:32:30.562	localhost	127.0.0.1	管理员	管理员	总部
1131	用户登录	用户登录	总部-管理员-管理员		2012-01-13 10:08:54.968	localhost	127.0.0.1	管理员	管理员	总部
1132	用户登录	用户登录	总部-管理员-管理员		2012-01-13 11:08:21	localhost	127.0.0.1	管理员	管理员	总部
1144	用户登录	用户登录	总部-管理员-管理员		2012-01-13 13:36:01.328	localhost	127.0.0.1	管理员	管理员	总部
1161	系统模块管理	修改	系统调度管理;schedulerMgr;	53	2012-01-13 14:32:40.687	localhost	127.0.0.1	管理员	管理员	总部
1170	系统模块管理	修改	机构信息管理;organMgr;	32	2012-01-13 14:34:07.468	localhost	127.0.0.1	管理员	管理员	总部
1183	系统模块管理	修改	主页框架管理;mainframemgr;	3	2012-01-13 14:38:01.531	localhost	127.0.0.1	管理员	管理员	总部
1189	系统模块管理	修改	用户信息管理;userMgr;	34	2012-01-13 14:38:58.203	localhost	127.0.0.1	管理员	管理员	总部
1206	用户登录	用户登录	河北-会计(默认)-jacob_liang		2012-01-13 16:52:26.156	localhost	127.0.0.1	jacob_liang	会计(默认)	河北
1209	系统配置管理	保存	设置角色切换:insystem;		2012-01-13 16:53:15.937	localhost	127.0.0.1	管理员	管理员	总部
1215	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 17:14:30.171	localhost	127.0.0.1	jacob_liang	会计	河北
1219	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 17:17:43.687	localhost	127.0.0.1	jacob_liang	会计	河北
1226	用户登录	用户登录	总部-管理员-jacob_liang		2012-01-13 17:19:43.046	localhost	127.0.0.1	jacob_liang	管理员	总部
1229	用户登录	用户登录	总部-管理员-管理员		2012-01-16 13:32:19.109	localhost	127.0.0.1	管理员	管理员	总部
1232	用户分配角色	保存	26	13	2012-01-16 13:34:51.843	localhost	127.0.0.1	管理员	管理员	总部
1238	用户登录	用户登录	河北-会计-jacob_liang		2012-01-16 16:11:55.046	localhost	127.0.0.1	jacob_liang	会计	河北
1241	用户登录	用户登录	总部-管理员-管理员		2012-01-17 10:40:42.296	localhost	127.0.0.1	管理员	管理员	总部
1249	用户登录	用户登录	河北-会计-jacob_liang		2012-01-17 16:02:13.406	localhost	127.0.0.1	jacob_liang	会计	河北
1250	用户登录	用户登录	河北-会计-jacob_liang		2012-01-17 16:55:38.546	localhost	127.0.0.1	jacob_liang	会计	河北
1252	用户登录	用户登录	河北-会计-jacob_liang		2012-01-17 17:21:51.937	localhost	127.0.0.1	jacob_liang	会计	河北
1261	用户登录	用户登录	河北-会计-jacob_liang		2012-01-18 13:26:31.965	localhost	127.0.0.1	jacob_liang	会计	河北
1268	用户登录	用户登录	总部-管理员-管理员		2012-01-18 13:54:59.34	localhost	127.0.0.1	管理员	管理员	总部
1282	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:46:22.062	localhost	127.0.0.1	管理员	管理员	总部
1286	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:49:51	localhost	127.0.0.1	管理员	管理员	总部
1295	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:53:47.687	localhost	127.0.0.1	管理员	管理员	总部
1298	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:19:17.109	localhost	127.0.0.1	管理员	管理员	总部
1305	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:29:38.078	localhost	127.0.0.1	管理员	管理员	总部
1308	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:40:31.687	localhost	127.0.0.1	管理员	管理员	总部
903	用户登录	用户登录	总部-管理员-管理员		2012-01-10 14:54:27.375	localhost	127.0.0.1	管理员	管理员	总部
911	用户登录	用户登录	总部-管理员-管理员		2012-01-10 14:59:37.593	localhost	127.0.0.1	管理员	管理员	总部
920	用户登录	用户登录	总部-管理员-管理员		2012-01-11 10:48:30.31	localhost	127.0.0.1	管理员	管理员	总部
935	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:41:49.812	localhost	127.0.0.1	管理员	管理员	总部
944	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:50:56.25	localhost	127.0.0.1	管理员	管理员	总部
946	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:57:58.609	localhost	127.0.0.1	管理员	管理员	总部
947	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:03:25.875	localhost	127.0.0.1	管理员	管理员	总部
953	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:10:58.765	localhost	127.0.0.1	管理员	管理员	总部
955	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:15:40.265	localhost	127.0.0.1	管理员	管理员	总部
957	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:22:27.515	localhost	127.0.0.1	管理员	管理员	总部
972	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:07:30.265	localhost	127.0.0.1	管理员	管理员	总部
990	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:29:10.703	localhost	127.0.0.1	管理员	管理员	总部
993	系统模块管理	修改	群组管理;groupmgr;	23	2012-01-11 15:30:26.906	localhost	127.0.0.1	管理员	管理员	总部
994	系统模块管理	修改	用户管理;usermgr;	22	2012-01-11 15:30:34.328	localhost	127.0.0.1	管理员	管理员	总部
995	系统模块管理	修改	角色管理;rolemgr;	21	2012-01-11 15:30:40.39	localhost	127.0.0.1	管理员	管理员	总部
997	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:31:48.5	localhost	127.0.0.1	管理员	管理员	总部
998	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:32:27.593	localhost	127.0.0.1	管理员	管理员	总部
1017	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:49:18.515	localhost	127.0.0.1	jacob	经理在	河北
1020	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:50:23.625	localhost	127.0.0.1	管理员	管理员	总部
1027	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:03:17.828	localhost	127.0.0.1	管理员	管理员	总部
1042	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:22:06.671	localhost	127.0.0.1	管理员	管理员	总部
1051	用户登录	用户登录	总部-管理员-管理员		2012-01-12 09:43:21.796	localhost	127.0.0.1	管理员	管理员	总部
1052	用户登录	用户登录	总部-管理员-管理员		2012-01-12 09:45:23.765	localhost	127.0.0.1	管理员	管理员	总部
1064	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:06:12.546	localhost	127.0.0.1	管理员	管理员	总部
1071	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:33:38.046	localhost	127.0.0.1	管理员	管理员	总部
1077	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:13:12.984	localhost	127.0.0.1	管理员	管理员	总部
1079	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:18:18.796	localhost	127.0.0.1	管理员	管理员	总部
1088	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:52:28.171	localhost	127.0.0.1	管理员	管理员	总部
1092	用户登录	用户登录	总部-管理员-管理员		2012-01-12 16:55:35.203	localhost	127.0.0.1	管理员	管理员	总部
1097	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:03:15.828	localhost	127.0.0.1	管理员	管理员	总部
1103	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:10:20.656	localhost	127.0.0.1	管理员	管理员	总部
1106	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:12:44.312	localhost	127.0.0.1	管理员	管理员	总部
1110	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:15:25.484	localhost	127.0.0.1	管理员	管理员	总部
1112	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:17:43.656	localhost	127.0.0.1	管理员	管理员	总部
1115	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:19:58.406	localhost	127.0.0.1	管理员	管理员	总部
1121	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:30:27.656	localhost	127.0.0.1	管理员	管理员	总部
1124	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:33:16.187	localhost	127.0.0.1	管理员	管理员	总部
1128	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:40:05.578	localhost	127.0.0.1	管理员	管理员	总部
1129	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:40:29.281	localhost	127.0.0.1	管理员	管理员	总部
1133	用户登录	用户登录	总部-管理员-管理员		2012-01-13 12:00:56.453	localhost	127.0.0.1	管理员	管理员	总部
1147	用户登录	用户登录	总部-管理员-管理员		2012-01-13 13:42:19.453	localhost	127.0.0.1	管理员	管理员	总部
1164	系统模块管理	修改	系统参数管理;systemParaMgr;	15	2012-01-13 14:33:03.828	localhost	127.0.0.1	管理员	管理员	总部
1168	系统模块管理	修改	类别字典管理;typeDicMgr;	10	2012-01-13 14:33:45.421	localhost	127.0.0.1	管理员	管理员	总部
1178	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:37:08.078	localhost	127.0.0.1	管理员	管理员	总部
1181	系统模块管理	修改	字典管理;dicmgr;	9	2012-01-13 14:37:48.375	localhost	127.0.0.1	管理员	管理员	总部
1187	系统模块管理	修改	机构管理;organmgr;	19	2012-01-13 14:38:39.062	localhost	127.0.0.1	管理员	管理员	总部
1200	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 15:44:20.828	localhost	127.0.0.1	jacob_liang	会计	河北
1207	用户登录	用户登录	总部-管理员-jacob_liang		2012-01-13 16:52:34.25	localhost	127.0.0.1	jacob_liang	管理员	总部
1217	用户管理	重置密码	重置密码用户：jacob_liang	13	2012-01-13 17:17:06.703	localhost	127.0.0.1	jacob_liang	管理员	总部
1227	用户登录	用户登录	河北-会计(默认)-jacob_liang		2012-01-13 17:19:58.156	localhost	127.0.0.1	jacob_liang	会计(默认)	河北
1230	用户登录	用户登录	总部-管理员-管理员		2012-01-16 13:32:54.843	localhost	127.0.0.1	管理员	管理员	总部
1239	用户登录	用户登录	总部-管理员-管理员		2012-01-16 17:04:41.89	localhost	127.0.0.1	管理员	管理员	总部
1242	用户登录	用户登录	总部-管理员-管理员		2012-01-17 10:55:03.171	localhost	127.0.0.1	管理员	管理员	总部
1251	用户登录	用户登录	河北-会计-jacob_liang		2012-01-17 17:01:47.375	localhost	127.0.0.1	jacob_liang	会计	河北
905	用户管理	保存	jacob_liang;jacob;会计;	13	2012-01-10 14:55:24.89	localhost	127.0.0.1	管理员	管理员	总部
910	用户登录	用户登录	总部-管理员-管理员		2012-01-10 14:57:15.734	localhost	127.0.0.1	管理员	管理员	总部
914	用户登录	用户登录	河北-会计-jacob		2012-01-10 15:00:54.703	localhost	127.0.0.1	jacob	会计	河北
915	用户登录	用户登录	总部-管理员-管理员		2012-01-10 15:01:16.75	localhost	127.0.0.1	管理员	管理员	总部
921	用户登录	用户登录	总部-管理员-管理员		2012-01-11 10:49:24.264	localhost	127.0.0.1	管理员	管理员	总部
925	系统附件	保存	附件业务表ID：6;附件名称:4mchFc_1_jacbo.jpg	8	2012-01-11 13:05:34.921	localhost	127.0.0.1	管理员	管理员	总部
926	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:08:01.781	localhost	127.0.0.1	管理员	管理员	总部
931	系统公告管理	修改	修改公告ID：6;修改公告标题：工;有效期：Wed Jan 11 00:00:00 CST 2012;是否发布：0;	6	2012-01-11 13:27:26.625	localhost	127.0.0.1	管理员	管理员	总部
945	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:56:45.734	localhost	127.0.0.1	管理员	管理员	总部
959	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:27:14.656	localhost	127.0.0.1	管理员	管理员	总部
966	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:40:59.203	localhost	127.0.0.1	管理员	管理员	总部
968	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:42:20.796	localhost	127.0.0.1	管理员	管理员	总部
977	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:13:56.468	localhost	127.0.0.1	管理员	管理员	总部
1003	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:26:07.703	localhost	127.0.0.1	管理员	管理员	总部
1014	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:48:00.218	localhost	127.0.0.1	jacob	经理在	河北
1028	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:04:49.531	localhost	127.0.0.1	管理员	管理员	总部
1039	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:20:41.562	localhost	127.0.0.1	管理员	管理员	总部
1048	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:26:31.859	localhost	127.0.0.1	管理员	管理员	总部
1053	用户登录	用户登录	总部-管理员-管理员		2012-01-12 09:45:32.812	localhost	127.0.0.1	管理员	管理员	总部
1059	用户管理	修改	jacob_liang;jacob_liang;null;	13	2012-01-12 10:01:54.031	localhost	127.0.0.1	管理员	管理员	总部
1065	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:14:04.156	localhost	127.0.0.1	管理员	管理员	总部
1076	用户(普通)组管理	保存	组名称：a;组类型：userGroup;	6	2012-01-12 11:03:15.046	localhost	127.0.0.1	管理员	管理员	总部
1091	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:58:02.781	localhost	127.0.0.1	管理员	管理员	总部
1093	用户登录	用户登录	总部-管理员-管理员		2012-01-12 16:56:39.937	localhost	127.0.0.1	管理员	管理员	总部
1094	用户登录	用户登录	总部-管理员-管理员		2012-01-12 16:58:26.218	localhost	127.0.0.1	管理员	管理员	总部
1096	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:02:37.109	localhost	127.0.0.1	管理员	管理员	总部
1100	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:06:12.359	localhost	127.0.0.1	管理员	管理员	总部
1105	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:12:23.64	localhost	127.0.0.1	管理员	管理员	总部
1114	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:19:24.75	localhost	127.0.0.1	管理员	管理员	总部
1126	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:37:10.546	localhost	127.0.0.1	管理员	管理员	总部
1127	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:39:46.531	localhost	127.0.0.1	管理员	管理员	总部
1134	用户登录	用户登录	总部-管理员-管理员		2012-01-13 12:02:23.296	localhost	127.0.0.1	管理员	管理员	总部
1138	用户登录	用户登录	总部-管理员-管理员		2012-01-13 12:11:23.968	localhost	127.0.0.1	管理员	管理员	总部
1142	系统模块管理	修改	模块菜单管理;moduleMgr;	7	2012-01-13 13:23:06.062	localhost	127.0.0.1	管理员	管理员	总部
1148	用户登录	用户登录	总部-管理员-管理员		2012-01-13 13:43:28.515	localhost	127.0.0.1	管理员	管理员	总部
1166	系统模块管理	修改	省市地区管理;priCityMgr;	14	2012-01-13 14:33:20.171	localhost	127.0.0.1	管理员	管理员	总部
1172	系统模块管理	修改	角色信息管理;roleMgr;	33	2012-01-13 14:34:19.546	localhost	127.0.0.1	管理员	管理员	总部
1174	系统模块管理	修改	角色组管理;roleGroupMgr;	48	2012-01-13 14:34:35.234	localhost	127.0.0.1	管理员	管理员	总部
1177	系统模块管理	修改	角色组管理;roleGroupMgr;	48	2012-01-13 14:35:14.203	localhost	127.0.0.1	管理员	管理员	总部
1179	系统模块管理	修改	省市地区;priCityMgr;	52	2012-01-13 14:37:30.468	localhost	127.0.0.1	管理员	管理员	总部
1185	系统模块管理	修改	用户管理;usermgr;	22	2012-01-13 14:38:29.875	localhost	127.0.0.1	管理员	管理员	总部
1208	用户登录	用户登录	总部-管理员-管理员		2012-01-13 16:52:51.484	localhost	127.0.0.1	管理员	管理员	总部
1218	模块分配操作	保存	模块操作关系ID:151;	1;rolePri	2012-01-13 17:17:34.328	localhost	127.0.0.1	jacob_liang	管理员	总部
1231	用户登录	用户登录	总部-管理员-管理员		2012-01-16 13:33:21.14	localhost	127.0.0.1	管理员	管理员	总部
1240	用户登录	用户登录	总部-管理员-管理员		2012-01-16 17:17:06.937	localhost	127.0.0.1	管理员	管理员	总部
1243	用户登录	用户登录	总部-管理员-管理员		2012-01-17 11:46:04.843	localhost	127.0.0.1	管理员	管理员	总部
1253	用户登录	用户登录	河北-会计-jacob_liang		2012-01-17 17:31:11.75	localhost	127.0.0.1	jacob_liang	会计	河北
1262	用户登录	用户登录	河北-会计-jacob_liang		2012-01-18 13:37:52.121	localhost	127.0.0.1	jacob_liang	会计	河北
1269	用户登录	用户登录	总部-管理员-管理员		2012-01-18 13:59:50.981	localhost	127.0.0.1	管理员	管理员	总部
1283	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:47:56.625	localhost	127.0.0.1	管理员	管理员	总部
1287	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:49:59.75	localhost	127.0.0.1	管理员	管理员	总部
1290	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:51:39.859	localhost	127.0.0.1	管理员	管理员	总部
1293	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:52:46.125	localhost	127.0.0.1	管理员	管理员	总部
1299	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:19:51.531	localhost	127.0.0.1	管理员	管理员	总部
907	用户分配角色	保存	26,1	13	2012-01-10 14:56:20.671	localhost	127.0.0.1	管理员	管理员	总部
922	用户登录	用户登录	总部-管理员-管理员		2012-01-11 10:59:05.56	localhost	127.0.0.1	管理员	管理员	总部
933	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:34:18.812	localhost	127.0.0.1	管理员	管理员	总部
941	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:49:14.39	localhost	127.0.0.1	管理员	管理员	总部
951	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:08:33.562	localhost	127.0.0.1	管理员	管理员	总部
960	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:28:04.203	localhost	127.0.0.1	管理员	管理员	总部
967	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:41:35.265	localhost	127.0.0.1	管理员	管理员	总部
969	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:42:56.312	localhost	127.0.0.1	管理员	管理员	总部
970	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:03:14.375	localhost	127.0.0.1	管理员	管理员	总部
976	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:13:39.343	localhost	127.0.0.1	管理员	管理员	总部
978	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:15:19.937	localhost	127.0.0.1	管理员	管理员	总部
1004	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:26:30.125	localhost	127.0.0.1	jacob	经理在	河北
1018	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:49:48.609	localhost	127.0.0.1	jacob	经理在	河北
1025	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:00:49.828	localhost	127.0.0.1	管理员	管理员	总部
1040	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:21:08.859	localhost	127.0.0.1	管理员	管理员	总部
1054	用户登录	用户登录	总部-管理员-管理员		2012-01-12 09:46:08.281	localhost	127.0.0.1	管理员	管理员	总部
1055	用户登录	用户登录	总部-管理员-管理员		2012-01-12 09:46:29.671	localhost	127.0.0.1	管理员	管理员	总部
1057	用户登录	用户登录	总部-管理员-管理员		2012-01-12 09:57:50.953	localhost	127.0.0.1	管理员	管理员	总部
1058	用户登录	用户登录	总部-管理员-管理员		2012-01-12 09:58:17.734	localhost	127.0.0.1	管理员	管理员	总部
1068	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:21:47.468	localhost	127.0.0.1	管理员	管理员	总部
1070	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:24:50.703	localhost	127.0.0.1	管理员	管理员	总部
1081	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:31:24.203	localhost	127.0.0.1	管理员	管理员	总部
1087	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:52:06.796	localhost	127.0.0.1	管理员	管理员	总部
1095	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:00:51.078	localhost	127.0.0.1	管理员	管理员	总部
1098	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:04:11.531	localhost	127.0.0.1	管理员	管理员	总部
1101	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:07:08.578	localhost	127.0.0.1	管理员	管理员	总部
1107	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:13:11.343	localhost	127.0.0.1	管理员	管理员	总部
1130	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:43:09.718	localhost	127.0.0.1	管理员	管理员	总部
1135	用户登录	用户登录	总部-管理员-管理员		2012-01-13 12:05:12.328	localhost	127.0.0.1	管理员	管理员	总部
1140	系统模块管理	修改	系统调度管理;schedulerMgr;	53	2012-01-13 12:13:43.312	localhost	127.0.0.1	管理员	管理员	总部
1149	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:22:57.5	localhost	127.0.0.1	管理员	管理员	总部
1150	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:23:54.375	localhost	127.0.0.1	管理员	管理员	总部
1152	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:27:05.843	localhost	127.0.0.1	管理员	管理员	总部
1160	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:30:40.562	localhost	127.0.0.1	管理员	管理员	总部
1163	系统模块管理	修改	系统配置管理;sysConMgr;	25	2012-01-13 14:32:57.062	localhost	127.0.0.1	管理员	管理员	总部
1171	系统模块管理	修改	角色分配管理;roleOrganMgr;	35	2012-01-13 14:34:15.468	localhost	127.0.0.1	管理员	管理员	总部
1184	系统模块管理	修改	群组管理;groupmgr;	23	2012-01-13 14:38:16.328	localhost	127.0.0.1	管理员	管理员	总部
1191	系统模块管理	修改	机构信息管理;organMgr;	32	2012-01-13 14:39:12.75	localhost	127.0.0.1	管理员	管理员	总部
1195	系统模块管理	修改	日志管理;logMgr;	17	2012-01-13 14:40:06.515	localhost	127.0.0.1	管理员	管理员	总部
1198	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:47:18	localhost	127.0.0.1	管理员	管理员	总部
1201	用户登录	用户登录	总部-管理员-管理员		2012-01-13 16:31:59	localhost	127.0.0.1	管理员	管理员	总部
1210	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 16:54:25.765	localhost	127.0.0.1	jacob_liang	会计	河北
1222	用户登录	用户登录	总部-管理员-jacob_liang		2012-01-13 17:19:02.937	localhost	127.0.0.1	jacob_liang	管理员	总部
1233	模块分配操作	保存	模块操作关系ID:1;2;3;4;5;6;7;87;89;90;10;11;12;13;14;15;16;17;18;19;25;26;27;28;29;36;37;38;39;40;98;99;100;101;104;105;106;107;108;109;110;111;44;30;31;33;34;35;45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;64;91;97;65;66;68;69;116;117;118;119;112;113;114;115;151;	13;userPri	2012-01-16 13:34:59.078	localhost	127.0.0.1	管理员	管理员	总部
1244	用户登录	用户登录	总部-管理员-管理员		2012-01-17 12:38:45.234	localhost	127.0.0.1	管理员	管理员	总部
1254	用户登录	用户登录	河北-会计-jacob_liang		2012-01-17 17:33:43.875	localhost	127.0.0.1	jacob_liang	会计	河北
1256	用户登录	用户登录	总部-管理员-管理员		2012-01-18 09:48:23.293	localhost	127.0.0.1	管理员	管理员	总部
1263	用户登录	用户登录	河北-会计-jacob_liang		2012-01-18 13:45:08.356	localhost	127.0.0.1	jacob_liang	会计	河北
1270	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:03:18.137	localhost	127.0.0.1	管理员	管理员	总部
1275	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:26:12.984	localhost	127.0.0.1	管理员	管理员	总部
1284	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:48:21.281	localhost	127.0.0.1	管理员	管理员	总部
1291	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:52:14.64	localhost	127.0.0.1	管理员	管理员	总部
1300	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:21:36.171	localhost	127.0.0.1	管理员	管理员	总部
908	用户分配角色	保存	26,1	13	2012-01-10 14:56:29.109	localhost	127.0.0.1	管理员	管理员	总部
916	用户登录	用户登录	河北-会计-jacob		2012-01-10 15:01:32.25	localhost	127.0.0.1	jacob	会计	河北
924	系统公告管理	保存	保存公告标题：工;有效期限:Wed Jan 11 00:00:00 CST 2012	6	2012-01-11 13:05:34.796	localhost	127.0.0.1	管理员	管理员	总部
930	系统附件	删除	删除附件ID：8		2012-01-11 13:27:23.75	localhost	127.0.0.1	管理员	管理员	总部
948	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:04:38.765	localhost	127.0.0.1	管理员	管理员	总部
961	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:28:40.468	localhost	127.0.0.1	管理员	管理员	总部
975	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:10:44.937	localhost	127.0.0.1	管理员	管理员	总部
983	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:22:01.953	localhost	127.0.0.1	管理员	管理员	总部
988	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:25:21.437	localhost	127.0.0.1	管理员	管理员	总部
1005	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:29:26.703	localhost	127.0.0.1	管理员	管理员	总部
1009	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:44:24.937	localhost	127.0.0.1	jacob	经理在	河北
1012	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:45:15.218	localhost	127.0.0.1	jacob	经理在	河北
1015	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:48:19.531	localhost	127.0.0.1	jacob	经理在	河北
1019	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:50:08.015	localhost	127.0.0.1	管理员	管理员	总部
1022	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:51:28.937	localhost	127.0.0.1	管理员	管理员	总部
1023	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:59:54.046	localhost	127.0.0.1	管理员	管理员	总部
1026	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:02:30.875	localhost	127.0.0.1	管理员	管理员	总部
1029	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:05:12.953	localhost	127.0.0.1	管理员	管理员	总部
1034	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:11:10.125	localhost	127.0.0.1	管理员	管理员	总部
1041	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:21:22.406	localhost	127.0.0.1	管理员	管理员	总部
1044	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:22:27.125	localhost	127.0.0.1	管理员	管理员	总部
1047	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:25:45.546	localhost	127.0.0.1	管理员	管理员	总部
1049	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:27:03.14	localhost	127.0.0.1	管理员	管理员	总部
1056	用户登录	用户登录	总部-管理员-管理员		2012-01-12 09:54:12.031	localhost	127.0.0.1	管理员	管理员	总部
1083	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:38:49.859	localhost	127.0.0.1	管理员	管理员	总部
1099	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:05:28.062	localhost	127.0.0.1	管理员	管理员	总部
1104	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:11:15.328	localhost	127.0.0.1	管理员	管理员	总部
1108	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:13:37.218	localhost	127.0.0.1	管理员	管理员	总部
1113	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:18:32.015	localhost	127.0.0.1	管理员	管理员	总部
1116	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:23:51.171	localhost	127.0.0.1	管理员	管理员	总部
1117	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:26:20.984	localhost	127.0.0.1	管理员	管理员	总部
1136	类别字典管理	保存	字典大类名称：页面类型;	25	2012-01-13 12:09:38.25	localhost	127.0.0.1	管理员	管理员	总部
1151	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:26:04.625	localhost	127.0.0.1	管理员	管理员	总部
1153	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:27:33.343	localhost	127.0.0.1	管理员	管理员	总部
1156	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:28:41.578	localhost	127.0.0.1	管理员	管理员	总部
1175	系统模块管理	修改	普通组管理;generalGroupMgr;	28	2012-01-13 14:34:39.843	localhost	127.0.0.1	管理员	管理员	总部
1180	系统模块管理	修改	系统管理;systemMgr;	12	2012-01-13 14:37:39.937	localhost	127.0.0.1	管理员	管理员	总部
1186	系统模块管理	修改	角色管理;rolemgr;	21	2012-01-13 14:38:34.453	localhost	127.0.0.1	管理员	管理员	总部
1193	系统模块管理	修改	用户组管理;userGroupMgr;	27	2012-01-13 14:39:25.14	localhost	127.0.0.1	管理员	管理员	总部
1194	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:39:37.562	localhost	127.0.0.1	管理员	管理员	总部
1197	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:41:55.421	localhost	127.0.0.1	管理员	管理员	总部
1211	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 17:10:43.453	localhost	127.0.0.1	jacob_liang	会计	河北
1223	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 17:19:12.687	localhost	127.0.0.1	jacob_liang	会计	河北
1225	系统配置管理	保存	设置角色切换:login;		2012-01-13 17:19:24.609	localhost	127.0.0.1	管理员	管理员	总部
1234	用户登录	用户登录	河北-会计-jacob_liang		2012-01-16 13:35:16.765	localhost	127.0.0.1	jacob_liang	会计	河北
1245	用户登录	用户登录	总部-管理员-管理员		2012-01-17 13:23:27.828	localhost	127.0.0.1	管理员	管理员	总部
1255	用户登录	用户登录	河北-会计-jacob_liang		2012-01-17 17:39:30.5	localhost	127.0.0.1	jacob_liang	会计	河北
1257	用户登录	用户登录	总部-管理员-管理员		2012-01-18 11:31:59.168	localhost	127.0.0.1	管理员	管理员	总部
1264	用户登录	用户登录	总部-管理员-管理员		2012-01-18 13:48:04.356	localhost	127.0.0.1	管理员	管理员	总部
1271	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:03:19.918	localhost	127.0.0.1	管理员	管理员	总部
1276	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:30:26.625	localhost	127.0.0.1	管理员	管理员	总部
1288	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:50:20.093	localhost	127.0.0.1	管理员	管理员	总部
1294	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:53:32.968	localhost	127.0.0.1	管理员	管理员	总部
1301	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:22:06.625	localhost	127.0.0.1	管理员	管理员	总部
1302	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:23:01.906	localhost	127.0.0.1	管理员	管理员	总部
909	用户管理	重置密码	重置密码用户：jacob	13	2012-01-10 14:56:33.656	localhost	127.0.0.1	管理员	管理员	总部
913	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;64;91;97;65;66;68;69;116;117;118;119;112;113;114;115;	13;userPri	2012-01-10 15:00:28.14	localhost	127.0.0.1	管理员	管理员	总部
927	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:24:25.296	localhost	127.0.0.1	管理员	管理员	总部
928	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:25:41.343	localhost	127.0.0.1	管理员	管理员	总部
929	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:27:12.265	localhost	127.0.0.1	管理员	管理员	总部
932	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:32:48.125	localhost	127.0.0.1	管理员	管理员	总部
940	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:48:58.078	localhost	127.0.0.1	管理员	管理员	总部
950	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:07:51.296	localhost	127.0.0.1	管理员	管理员	总部
962	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:33:05.156	localhost	127.0.0.1	管理员	管理员	总部
965	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:38:35.953	localhost	127.0.0.1	管理员	管理员	总部
973	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:07:52.765	localhost	127.0.0.1	管理员	管理员	总部
984	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:22:44.5	localhost	127.0.0.1	管理员	管理员	总部
989	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:25:37.765	localhost	127.0.0.1	管理员	管理员	总部
1000	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:50:25.39	localhost	127.0.0.1	管理员	管理员	总部
1002	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:23:05.703	localhost	127.0.0.1	管理员	管理员	总部
1006	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:29:43.187	localhost	127.0.0.1	jacob	经理在	河北
1007	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:31:01.89	localhost	127.0.0.1	jacob	经理在	河北
1010	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:44:36.156	localhost	127.0.0.1	jacob	经理在	河北
1013	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:47:19.796	localhost	127.0.0.1	jacob	经理在	河北
1016	用户登录	用户登录	河北-经理在-jacob		2012-01-11 16:48:56.234	localhost	127.0.0.1	jacob	经理在	河北
1024	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:00:32.109	localhost	127.0.0.1	管理员	管理员	总部
1031	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:06:13.656	localhost	127.0.0.1	管理员	管理员	总部
1037	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:19:38.562	localhost	127.0.0.1	管理员	管理员	总部
1046	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:24:16.718	localhost	127.0.0.1	管理员	管理员	总部
1061	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:02:25.937	localhost	127.0.0.1	管理员	管理员	总部
1067	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:20:32.359	localhost	127.0.0.1	管理员	管理员	总部
1069	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:23:50.031	localhost	127.0.0.1	管理员	管理员	总部
1082	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:38:09.5	localhost	127.0.0.1	管理员	管理员	总部
1102	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:09:32.281	localhost	127.0.0.1	管理员	管理员	总部
1109	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:14:59.656	localhost	127.0.0.1	管理员	管理员	总部
1111	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:16:28.375	localhost	127.0.0.1	管理员	管理员	总部
1137	用户登录	用户登录	总部-管理员-管理员		2012-01-13 12:09:59.578	localhost	127.0.0.1	管理员	管理员	总部
1139	用户登录	用户登录	总部-管理员-管理员		2012-01-13 12:13:19.687	localhost	127.0.0.1	管理员	管理员	总部
1146	用户登录	用户登录	总部-管理员-管理员		2012-01-13 13:36:43.171	localhost	127.0.0.1	管理员	管理员	总部
1154	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:27:41.218	localhost	127.0.0.1	管理员	管理员	总部
1157	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:29:33.906	localhost	127.0.0.1	管理员	管理员	总部
1167	系统模块管理	修改	操作按钮管理;operateMgr;	8	2012-01-13 14:33:37.062	localhost	127.0.0.1	管理员	管理员	总部
1169	系统模块管理	修改	系统日志管理;sysLogMgr;	18	2012-01-13 14:33:56.093	localhost	127.0.0.1	管理员	管理员	总部
1192	系统模块管理	修改	普通组管理;generalGroupMgr;	28	2012-01-13 14:39:19.234	localhost	127.0.0.1	管理员	管理员	总部
1196	系统模块管理	修改	系统日志管理;sysLogMgr;	18	2012-01-13 14:40:18.921	localhost	127.0.0.1	管理员	管理员	总部
1199	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:47:32.156	localhost	127.0.0.1	管理员	管理员	总部
1203	用户登录	用户登录	总部-管理员-管理员		2012-01-13 16:46:32.25	localhost	127.0.0.1	管理员	管理员	总部
1212	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 17:11:43.015	localhost	127.0.0.1	jacob_liang	会计	河北
1216	用户登录	用户登录	总部-管理员-jacob_liang		2012-01-13 17:16:07.437	localhost	127.0.0.1	jacob_liang	管理员	总部
1220	用户登录	用户登录	总部-管理员-jacob_liang		2012-01-13 17:17:53.406	localhost	127.0.0.1	jacob_liang	管理员	总部
1224	用户登录	用户登录	总部-管理员-管理员		2012-01-13 17:19:19.812	localhost	127.0.0.1	管理员	管理员	总部
1228	用户登录	用户登录	总部-管理员-jacob_liang		2012-01-13 17:20:05.64	localhost	127.0.0.1	jacob_liang	管理员	总部
1235	用户登录	用户登录	河北-会计-jacob_liang		2012-01-16 14:42:02.734	localhost	127.0.0.1	jacob_liang	会计	河北
1246	用户登录	用户登录	总部-管理员-管理员		2012-01-17 14:18:11.062	localhost	127.0.0.1	管理员	管理员	总部
1258	用户登录	用户登录	总部-管理员-管理员		2012-01-18 11:37:06.371	localhost	127.0.0.1	管理员	管理员	总部
1265	用户登录	用户登录	总部-管理员-管理员		2012-01-18 13:48:14.996	localhost	127.0.0.1	管理员	管理员	总部
1272	用户管理	重置密码	重置密码用户：jacob	12	2012-01-18 14:04:49.731	localhost	127.0.0.1	管理员	管理员	总部
1277	用户登录	用户登录	河北-会计-jacob_liang		2012-01-18 14:38:13.687	localhost	127.0.0.1	jacob_liang	会计	河北
1473	用户登录	用户登录	河北-经理-jacob		2012-02-09 09:36:00.531	localhost	127.0.0.1	jacob	经理	河北
912	用户登录	用户登录	总部-管理员-管理员		2012-01-10 15:00:15.656	localhost	127.0.0.1	管理员	管理员	总部
918	用户登录	用户登录	总部-管理员-管理员		2012-01-10 15:06:16.562	localhost	127.0.0.1	管理员	管理员	总部
934	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:40:14.953	localhost	127.0.0.1	管理员	管理员	总部
938	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:47:31.546	localhost	127.0.0.1	管理员	管理员	总部
943	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:50:23.656	localhost	127.0.0.1	管理员	管理员	总部
952	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:10:37.031	localhost	127.0.0.1	管理员	管理员	总部
956	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:18:39.984	localhost	127.0.0.1	管理员	管理员	总部
963	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:34:15.437	localhost	127.0.0.1	管理员	管理员	总部
980	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:16:28.859	localhost	127.0.0.1	管理员	管理员	总部
982	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:18:28.859	localhost	127.0.0.1	管理员	管理员	总部
985	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:24:21.734	localhost	127.0.0.1	管理员	管理员	总部
991	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:29:21	localhost	127.0.0.1	管理员	管理员	总部
992	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:29:39.984	localhost	127.0.0.1	管理员	管理员	总部
999	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:39:42.843	localhost	127.0.0.1	管理员	管理员	总部
1008	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:44:16.875	localhost	127.0.0.1	管理员	管理员	总部
1033	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:08:31.671	localhost	127.0.0.1	管理员	管理员	总部
1035	系统模块管理	修改	系统平台;pltmgr;	1	2012-01-11 17:12:14.718	localhost	127.0.0.1	管理员	管理员	总部
1062	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:03:56.671	localhost	127.0.0.1	管理员	管理员	总部
1073	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:58:01.156	localhost	127.0.0.1	管理员	管理员	总部
1084	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:46:44.625	localhost	127.0.0.1	管理员	管理员	总部
1085	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:47:48.187	localhost	127.0.0.1	管理员	管理员	总部
1086	用户(普通)组管理	保存	组名称：test;组类型：userGroup;	7	2012-01-12 11:48:12.343	localhost	127.0.0.1	管理员	管理员	总部
1089	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:53:14.078	localhost	127.0.0.1	管理员	管理员	总部
1090	用户登录	用户登录	总部-管理员-管理员		2012-01-12 11:57:08.093	localhost	127.0.0.1	管理员	管理员	总部
1118	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:27:21.125	localhost	127.0.0.1	管理员	管理员	总部
1122	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:32:06.406	localhost	127.0.0.1	管理员	管理员	总部
1125	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:36:24.781	localhost	127.0.0.1	管理员	管理员	总部
1141	用户登录	用户登录	总部-管理员-管理员		2012-01-13 12:14:39.312	localhost	127.0.0.1	管理员	管理员	总部
1145	系统模块管理	修改	页面功能区管理;mainFrameFunMgr;	4	2012-01-13 13:36:27.187	localhost	127.0.0.1	管理员	管理员	总部
1155	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:27:55.359	localhost	127.0.0.1	管理员	管理员	总部
1159	系统模块管理	修改	日志管理;logMgr;	17	2012-01-13 14:30:31.968	localhost	127.0.0.1	管理员	管理员	总部
1162	系统模块管理	修改	系统公告管理;afficheMgr;	49	2012-01-13 14:32:49.828	localhost	127.0.0.1	管理员	管理员	总部
1173	系统模块管理	修改	用户信息管理;userMgr;	34	2012-01-13 14:34:26.343	localhost	127.0.0.1	管理员	管理员	总部
1190	系统模块管理	修改	角色信息管理;roleMgr;	33	2012-01-13 14:39:04.765	localhost	127.0.0.1	管理员	管理员	总部
1204	用户登录	用户登录	总部-管理员-jacob_liang		2012-01-13 16:51:12.765	localhost	127.0.0.1	jacob_liang	管理员	总部
1213	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 17:13:20.531	localhost	127.0.0.1	jacob_liang	会计	河北
1221	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 17:18:52.578	localhost	127.0.0.1	jacob_liang	会计	河北
1236	用户常用功能设置	保存	用户常用功能保存:用户ID13;模块ID：13,15,49,53,4,7,8		2012-01-16 14:43:02.515	localhost	127.0.0.1	jacob_liang	会计	河北
1247	用户登录	用户登录	总部-管理员-管理员		2012-01-17 14:34:13.5	localhost	127.0.0.1	管理员	管理员	总部
1259	用户登录	用户登录	总部-管理员-管理员		2012-01-18 11:38:50.184	localhost	127.0.0.1	管理员	管理员	总部
1266	用户登录	用户登录	总部-管理员-管理员		2012-01-18 13:48:23.949	localhost	127.0.0.1	管理员	管理员	总部
1273	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:05:36.043	localhost	127.0.0.1	管理员	管理员	总部
1278	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:39:14.984	localhost	127.0.0.1	管理员	管理员	总部
1279	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:39:47.546	localhost	127.0.0.1	管理员	管理员	总部
1296	系统模块管理	修改	待办任务;underwayTaskId;	61	2012-01-18 14:55:30.078	localhost	127.0.0.1	管理员	管理员	总部
1303	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:26:32.859	localhost	127.0.0.1	管理员	管理员	总部
1306	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:29:53.046	localhost	127.0.0.1	管理员	管理员	总部
1309	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:43:45.562	localhost	127.0.0.1	管理员	管理员	总部
1312	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:54:35.437	localhost	127.0.0.1	管理员	管理员	总部
1314	系统模块管理	修改	系统平台;pltmgr;	1	2012-01-18 15:56:07.093	localhost	127.0.0.1	管理员	管理员	总部
1315	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:58:58.921	localhost	127.0.0.1	管理员	管理员	总部
1316	用户登录	用户登录	总部-管理员-管理员		2012-01-19 10:47:02.609	localhost	127.0.0.1	管理员	管理员	总部
1318	用户登录	用户登录	总部-管理员-管理员		2012-01-19 11:28:37.734	localhost	127.0.0.1	管理员	管理员	总部
1320	系统配置管理	保存	设置角色切换:login;		2012-01-19 13:48:04.5	localhost	127.0.0.1	管理员	管理员	总部
917	用户登录	用户登录	总部-管理员-管理员		2012-01-10 15:05:21.89	localhost	127.0.0.1	管理员	管理员	总部
937	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:46:22.453	localhost	127.0.0.1	管理员	管理员	总部
942	用户登录	用户登录	总部-管理员-管理员		2012-01-11 13:49:38.718	localhost	127.0.0.1	管理员	管理员	总部
964	用户登录	用户登录	总部-管理员-管理员		2012-01-11 14:37:07.828	localhost	127.0.0.1	管理员	管理员	总部
974	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:08:15.781	localhost	127.0.0.1	管理员	管理员	总部
979	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:15:51.015	localhost	127.0.0.1	管理员	管理员	总部
986	用户登录	用户登录	总部-管理员-管理员		2012-01-11 15:24:42.687	localhost	127.0.0.1	管理员	管理员	总部
996	系统模块管理	修改	机构管理;organmgr;	19	2012-01-11 15:30:46.578	localhost	127.0.0.1	管理员	管理员	总部
1001	用户登录	用户登录	总部-管理员-管理员		2012-01-11 16:21:16.875	localhost	127.0.0.1	管理员	管理员	总部
1030	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:06:04.734	localhost	127.0.0.1	管理员	管理员	总部
1038	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:19:52.296	localhost	127.0.0.1	管理员	管理员	总部
1045	用户登录	用户登录	总部-管理员-管理员		2012-01-11 17:22:40.437	localhost	127.0.0.1	管理员	管理员	总部
1063	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:04:13.265	localhost	127.0.0.1	管理员	管理员	总部
1072	用户登录	用户登录	总部-管理员-管理员		2012-01-12 10:39:42.359	localhost	127.0.0.1	管理员	管理员	总部
1119	用户登录	用户登录	总部-管理员-管理员		2012-01-12 17:27:51.39	localhost	127.0.0.1	管理员	管理员	总部
1143	用户登录	用户登录	总部-管理员-管理员		2012-01-13 13:35:40.968	localhost	127.0.0.1	管理员	管理员	总部
1158	用户登录	用户登录	总部-管理员-管理员		2012-01-13 14:29:52.671	localhost	127.0.0.1	管理员	管理员	总部
1165	系统模块管理	修改	系统编码管理;codeMgr;	13	2012-01-13 14:33:08.328	localhost	127.0.0.1	管理员	管理员	总部
1176	系统模块管理	修改	用户组管理;userGroupMgr;	27	2012-01-13 14:34:43.953	localhost	127.0.0.1	管理员	管理员	总部
1182	系统模块管理	修改	模块操作管理;modoptmgr;	6	2012-01-13 14:37:54.078	localhost	127.0.0.1	管理员	管理员	总部
1188	系统模块管理	修改	角色分配管理;roleOrganMgr;	35	2012-01-13 14:38:50	localhost	127.0.0.1	管理员	管理员	总部
1202	系统配置管理	保存	设置角色切换:login;		2012-01-13 16:32:07.046	localhost	127.0.0.1	管理员	管理员	总部
1205	用户登录	用户登录	总部-管理员-jacob_liang		2012-01-13 16:51:27.656	localhost	127.0.0.1	jacob_liang	管理员	总部
1214	用户登录	用户登录	河北-会计-jacob_liang		2012-01-13 17:13:38.234	localhost	127.0.0.1	jacob_liang	会计	河北
1237	用户登录	用户登录	河北-会计-jacob_liang		2012-01-16 15:47:41.281	localhost	127.0.0.1	jacob_liang	会计	河北
1248	用户登录	用户登录	河北-会计-jacob_liang		2012-01-17 14:37:11.156	localhost	127.0.0.1	jacob_liang	会计	河北
1260	用户登录	用户登录	河北-会计-jacob_liang		2012-01-18 13:25:37.824	localhost	127.0.0.1	jacob_liang	会计	河北
1267	用户登录	用户登录	总部-管理员-管理员		2012-01-18 13:52:57.793	localhost	127.0.0.1	管理员	管理员	总部
1274	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:08:18.527	localhost	127.0.0.1	管理员	管理员	总部
1280	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:43:10.125	localhost	127.0.0.1	管理员	管理员	总部
1281	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:45:49.703	localhost	127.0.0.1	管理员	管理员	总部
1285	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:48:31.046	localhost	127.0.0.1	管理员	管理员	总部
1289	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:51:29.89	localhost	127.0.0.1	管理员	管理员	总部
1292	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:52:23.656	localhost	127.0.0.1	管理员	管理员	总部
1297	用户登录	用户登录	总部-管理员-管理员		2012-01-18 14:58:12.437	localhost	127.0.0.1	管理员	管理员	总部
1304	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:29:21.812	localhost	127.0.0.1	管理员	管理员	总部
1307	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:33:41.218	localhost	127.0.0.1	管理员	管理员	总部
1310	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:46:12.593	localhost	127.0.0.1	管理员	管理员	总部
1311	用户登录	用户登录	总部-管理员-管理员		2012-01-18 15:50:27.375	localhost	127.0.0.1	管理员	管理员	总部
1313	系统模块管理	修改	统一用户;uusmgr;	2	2012-01-18 15:55:57.64	localhost	127.0.0.1	管理员	管理员	总部
1317	用户登录	用户登录	总部-管理员-管理员		2012-01-19 11:11:57.312	localhost	127.0.0.1	管理员	管理员	总部
1319	用户登录	用户登录	总部-管理员-管理员		2012-01-19 13:46:37.031	localhost	127.0.0.1	管理员	管理员	总部
1321	用户登录	用户登录	总部-管理员-管理员		2012-01-19 13:49:04.703	localhost	127.0.0.1	管理员	管理员	总部
1322	用户登录	用户登录	总部-管理员-管理员		2012-01-19 13:49:18.64	localhost	127.0.0.1	管理员	管理员	总部
1323	用户登录	用户登录	总部-管理员-管理员		2012-01-19 13:50:41.703	localhost	127.0.0.1	管理员	管理员	总部
1324	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:14:32.796	localhost	127.0.0.1	管理员	管理员	总部
1325	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:18:13.531	localhost	127.0.0.1	管理员	管理员	总部
1326	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:20:26.859	localhost	127.0.0.1	管理员	管理员	总部
1327	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:21:59.156	localhost	127.0.0.1	管理员	管理员	总部
1328	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:22:26.39	localhost	127.0.0.1	管理员	管理员	总部
1329	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:22:41.5	localhost	127.0.0.1	管理员	管理员	总部
1330	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:23:08.64	localhost	127.0.0.1	管理员	管理员	总部
1331	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:24:39.406	localhost	127.0.0.1	管理员	管理员	总部
1332	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:25:21.187	localhost	127.0.0.1	管理员	管理员	总部
1906	用户登录	用户登录	河北-经理-jacob		2012-02-15 16:48:30.593	localhost	127.0.0.1	jacob	经理	河北
1333	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:25:38.281	localhost	127.0.0.1	管理员	管理员	总部
1334	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:26:10.25	localhost	127.0.0.1	管理员	管理员	总部
1337	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:30:22.203	localhost	127.0.0.1	管理员	管理员	总部
1340	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:33:42.062	localhost	127.0.0.1	管理员	管理员	总部
1343	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:37:14.562	localhost	127.0.0.1	管理员	管理员	总部
1344	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:37:21.593	localhost	127.0.0.1	管理员	管理员	总部
1350	用户登录	用户登录	总部-管理员-管理员		2012-02-08 10:26:51.921	localhost	127.0.0.1	管理员	管理员	总部
1357	系统模块操作管理	保存	特殊权限;assign;;	28	2012-02-08 11:16:26.375	localhost	127.0.0.1	管理员	管理员	总部
1364	系统模块操作管理	修改	特殊权限分配;assign;optpri;	28	2012-02-08 13:42:39.865	localhost	127.0.0.1	管理员	管理员	总部
1369	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:18:08.849	localhost	127.0.0.1	管理员	管理员	总部
1370	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:21:24.896	localhost	127.0.0.1	管理员	管理员	总部
1395	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:03:32.537	localhost	127.0.0.1	管理员	管理员	总部
1396	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:03:54.912	localhost	127.0.0.1	管理员	管理员	总部
1397	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:06:10.209	localhost	127.0.0.1	管理员	管理员	总部
1398	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:07:09.052	localhost	127.0.0.1	管理员	管理员	总部
1399	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:07:37.693	localhost	127.0.0.1	管理员	管理员	总部
1404	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:21:22.912	localhost	127.0.0.1	管理员	管理员	总部
1407	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:27:12.974	localhost	127.0.0.1	管理员	管理员	总部
1408	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:27:52.396	localhost	127.0.0.1	管理员	管理员	总部
1409	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:28:23.834	localhost	127.0.0.1	管理员	管理员	总部
1410	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:32:12.443	localhost	127.0.0.1	管理员	管理员	总部
1411	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:32:58.521	localhost	127.0.0.1	管理员	管理员	总部
1412	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:34:15.662	localhost	127.0.0.1	管理员	管理员	总部
1413	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:34:52.177	localhost	127.0.0.1	管理员	管理员	总部
1421	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:47:11.74	localhost	127.0.0.1	管理员	管理员	总部
1423	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:50:08.099	localhost	127.0.0.1	管理员	管理员	总部
1440	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:52:30.474	localhost	127.0.0.1	管理员	管理员	总部
1444	模块分配操作	保存	模块操作关系ID:	1;opt	2012-02-08 16:57:35.052	localhost	127.0.0.1	管理员	管理员	总部
1446	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-08 16:58:33.052	localhost	127.0.0.1	管理员	管理员	总部
1448	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-08 16:59:55.677	localhost	127.0.0.1	管理员	管理员	总部
1450	模块分配操作	保存	模块操作关系ID:	3;opt	2012-02-08 17:00:52.896	localhost	127.0.0.1	管理员	管理员	总部
1455	用户登录	用户登录	总部-管理员-管理员		2012-02-08 17:10:21.006	localhost	127.0.0.1	管理员	管理员	总部
1464	用户登录	用户登录	总部-管理员-管理员		2012-02-09 09:12:09	localhost	127.0.0.1	管理员	管理员	总部
1474	模块分配操作	保存	模块操作关系ID:1;2;3;4;5;6;7;87;89;90;10;11;12;13;14;15;16;17;18;19;25;26;27;28;29;36;37;38;39;40;98;99;100;101;104;105;106;107;108;109;110;111;44;30;31;33;34;35;45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;151;	3;opt	2012-02-09 09:38:42.921	localhost	127.0.0.1	jacob	经理	河北
1475	用户登录	用户登录	河北-经理-jacob		2012-02-09 09:38:59.343	localhost	127.0.0.1	jacob	经理	河北
1480	用户登录	用户登录	河北-经理-jacob		2012-02-09 09:50:12.859	localhost	127.0.0.1	jacob	经理	河北
1486	用户登录	用户登录	总部-管理员-管理员		2012-02-09 13:40:53.359	localhost	127.0.0.1	管理员	管理员	总部
1493	用户登录	用户登录	总部-管理员-管理员		2012-02-09 13:52:59.375	localhost	127.0.0.1	管理员	管理员	总部
1503	模块分配操作	保存	模块操作关系ID:55;	3;opt	2012-02-09 14:25:25.75	localhost	127.0.0.1	管理员	管理员	总部
1509	用户登录	用户登录	总部-管理员-管理员		2012-02-09 14:43:00.843	localhost	127.0.0.1	管理员	管理员	总部
1514	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:01:37.078	localhost	127.0.0.1	管理员	管理员	总部
1515	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：7,14,25,33		2012-02-09 15:01:55.39	localhost	127.0.0.1	管理员	管理员	总部
1516	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:02:31.937	localhost	127.0.0.1	管理员	管理员	总部
1519	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:09:02.765	localhost	127.0.0.1	管理员	管理员	总部
1526	系统公告管理	修改	修改公告ID：6;修改公告标题：工;有效期：Thu Mar 29 00:00:00 CST 2012;是否发布：0;	6	2012-02-09 15:40:55.875	localhost	127.0.0.1	管理员	管理员	总部
1532	系统调度管理	启动job	启动job:;jobName:postgresqlBbBackup;job类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;	3	2012-02-09 15:52:38.14	localhost	127.0.0.1	管理员	管理员	总部
1537	用户登录	用户登录	总部-管理员-管理员		2012-02-09 16:31:27.625	localhost	127.0.0.1	管理员	管理员	总部
1538	模块分配操作	保存	模块分配操作;1	62	2012-02-09 16:34:04.812	localhost	127.0.0.1	管理员	管理员	总部
1543	用户登录	用户登录	河北-经理-jacob		2012-02-09 16:35:57.937	localhost	127.0.0.1	jacob	经理	河北
1548	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:17:16.984	localhost	127.0.0.1	管理员	管理员	总部
1335	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:26:14.906	localhost	127.0.0.1	管理员	管理员	总部
1336	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:26:35.562	localhost	127.0.0.1	管理员	管理员	总部
1342	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:36:52.156	localhost	127.0.0.1	管理员	管理员	总部
1346	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:40:10.14	localhost	127.0.0.1	管理员	管理员	总部
1351	角色管理	修改	经理;	3	2012-02-08 10:27:48.421	localhost	127.0.0.1	管理员	管理员	总部
1359	系统模块操作管理	修改	特殊权限;assign;optpri;	28	2012-02-08 11:17:14.937	localhost	127.0.0.1	管理员	管理员	总部
1365	系统模块操作管理	修改	普通权限分配;assign;optpri;	8	2012-02-08 13:43:03.912	localhost	127.0.0.1	管理员	管理员	总部
1367	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:16:39.631	localhost	127.0.0.1	管理员	管理员	总部
1374	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:24:35.365	localhost	127.0.0.1	管理员	管理员	总部
1402	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:12:49.177	localhost	127.0.0.1	管理员	管理员	总部
1403	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:15:27.49	localhost	127.0.0.1	管理员	管理员	总部
1443	模块分配操作	保存	模块操作关系ID:	1;opt	2012-02-08 16:57:17.318	localhost	127.0.0.1	管理员	管理员	总部
1445	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:58:01.302	localhost	127.0.0.1	管理员	管理员	总部
1452	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-08 17:07:17.677	localhost	127.0.0.1	管理员	管理员	总部
1456	用户登录	用户登录	总部-管理员-管理员		2012-02-08 17:32:07.459	localhost	127.0.0.1	管理员	管理员	总部
1465	用户分配角色	保存	11	12	2012-02-09 09:16:49.312	localhost	127.0.0.1	管理员	管理员	总部
1469	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-09 09:26:32.828	localhost	127.0.0.1	jacob	经理	河北
1476	用户登录	用户登录	总部-管理员-管理员		2012-02-09 09:41:02.562	localhost	127.0.0.1	管理员	管理员	总部
1477	用户登录	用户登录	总部-管理员-管理员		2012-02-09 09:43:55.281	localhost	127.0.0.1	管理员	管理员	总部
1481	用户登录	用户登录	河北-经理-jacob		2012-02-09 11:46:44.796	localhost	127.0.0.1	jacob	经理	河北
1487	用户登录	用户登录	总部-管理员-管理员		2012-02-09 13:42:28.609	localhost	127.0.0.1	管理员	管理员	总部
1494	模块分配操作	保存	模块操作关系ID:usr;	3;row	2012-02-09 13:54:04.328	localhost	127.0.0.1	管理员	管理员	总部
1498	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-09 14:00:58.734	localhost	127.0.0.1	管理员	管理员	总部
1505	用户登录	用户登录	河北-经理-jacob		2012-02-09 14:26:00.812	localhost	127.0.0.1	jacob	经理	河北
1508	用户登录	用户登录	总部-管理员-管理员		2012-02-09 14:41:44.125	localhost	127.0.0.1	管理员	管理员	总部
1517	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:03:49.125	localhost	127.0.0.1	管理员	管理员	总部
1527	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:41:04.031	localhost	127.0.0.1	管理员	管理员	总部
1531	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户a;		2012-02-09 15:45:23.484	localhost	127.0.0.1	管理员	管理员	总部
1539	用户登录	用户登录	总部-管理员-管理员		2012-02-09 16:34:17.296	localhost	127.0.0.1	管理员	管理员	总部
1549	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:18:04.359	localhost	127.0.0.1	管理员	管理员	总部
1559	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:39:26.312	HENRYCOMPUTER	192.168.1.205	管理员	管理员	总部
1561	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:40:32.843	HENRYCOMPUTER	192.168.1.205	管理员	管理员	总部
1572	用户登录	用户登录	总部-管理员-管理员		2012-02-09 21:43:24.75	localhost	127.0.0.1	管理员	管理员	总部
1576	用户登录	用户登录	总部-管理员-管理员		2012-02-09 21:50:30.75	localhost	127.0.0.1	管理员	管理员	总部
1577	用户登录	用户登录	总部-管理员-管理员		2012-02-12 18:16:34.015	localhost	127.0.0.1	管理员	管理员	总部
1583	用户登录	用户登录	总部-管理员-管理员		2012-02-12 18:53:00.796	localhost	127.0.0.1	管理员	管理员	总部
1585	用户登录	用户登录	总部-管理员-管理员		2012-02-12 18:54:44.984	localhost	127.0.0.1	管理员	管理员	总部
1588	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:01:15.484	localhost	127.0.0.1	管理员	管理员	总部
1590	系统调度管理	停止调度器	停止调度器		2012-02-12 19:01:52.39	localhost	127.0.0.1	管理员	管理员	总部
1594	系统附件	保存	附件业务表ID：6;附件名称:bJjlVK_PowerWordFull.exe	9	2012-02-12 19:14:59.453	localhost	127.0.0.1	管理员	管理员	总部
1598	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:28:59.218	localhost	127.0.0.1	管理员	管理员	总部
1602	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:35:09.765	localhost	127.0.0.1	管理员	管理员	总部
1605	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:36:17.921	localhost	127.0.0.1	管理员	管理员	总部
1608	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:43:19.921	localhost	127.0.0.1	管理员	管理员	总部
1614	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:48:53.015	localhost	127.0.0.1	管理员	管理员	总部
1616	用户登录	用户登录	总部-管理员-管理员		2012-02-13 09:46:10.312	localhost	127.0.0.1	管理员	管理员	总部
1619	用户登录	用户登录	总部-管理员-管理员		2012-02-13 09:55:31.937	localhost	127.0.0.1	管理员	管理员	总部
1623	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:05:09.562	localhost	127.0.0.1	管理员	管理员	总部
1625	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:06:29.656	localhost	127.0.0.1	管理员	管理员	总部
1634	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:23:26.25	localhost	127.0.0.1	管理员	管理员	总部
1638	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:26:15.14	localhost	127.0.0.1	管理员	管理员	总部
1640	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:27:20.062	localhost	127.0.0.1	管理员	管理员	总部
1338	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:31:06.953	localhost	127.0.0.1	管理员	管理员	总部
1348	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:46:30.781	localhost	127.0.0.1	管理员	管理员	总部
1352	用户登录	用户登录	总部-管理员-管理员		2012-02-08 11:01:45.062	localhost	127.0.0.1	管理员	管理员	总部
1360	用户登录	用户登录	总部-管理员-管理员		2012-02-08 11:25:51.437	localhost	127.0.0.1	管理员	管理员	总部
1371	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:23:15.177	localhost	127.0.0.1	管理员	管理员	总部
1385	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:44:22.693	localhost	127.0.0.1	管理员	管理员	总部
1386	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:46:04.693	localhost	127.0.0.1	管理员	管理员	总部
1389	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:53:59.052	localhost	127.0.0.1	管理员	管理员	总部
1393	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:01:00.006	localhost	127.0.0.1	管理员	管理员	总部
1400	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:11:08.474	localhost	127.0.0.1	管理员	管理员	总部
1401	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:12:01.099	localhost	127.0.0.1	管理员	管理员	总部
1406	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:24:58.037	localhost	127.0.0.1	管理员	管理员	总部
1442	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:56:12.193	localhost	127.0.0.1	管理员	管理员	总部
1451	用户登录	用户登录	总部-管理员-管理员		2012-02-08 17:07:00.459	localhost	127.0.0.1	管理员	管理员	总部
1453	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-08 17:09:24.287	localhost	127.0.0.1	管理员	管理员	总部
1457	用户登录	用户登录	总部-管理员-管理员		2012-02-08 17:34:14.131	localhost	127.0.0.1	管理员	管理员	总部
1458	用户登录	用户登录	总部-管理员-管理员		2012-02-08 17:35:53.631	localhost	127.0.0.1	管理员	管理员	总部
1459	用户登录	用户登录	总部-管理员-管理员		2012-02-08 17:38:59.256	localhost	127.0.0.1	管理员	管理员	总部
1466	用户登录	用户登录	河北-经理-jacob		2012-02-09 09:21:36.14	localhost	127.0.0.1	jacob	经理	河北
1482	用户登录	用户登录	总部-管理员-管理员		2012-02-09 11:48:23.703	localhost	127.0.0.1	管理员	管理员	总部
1489	用户登录	用户登录	总部-管理员-管理员		2012-02-09 13:47:48.687	localhost	127.0.0.1	管理员	管理员	总部
1495	用户登录	用户登录	总部-管理员-管理员		2012-02-09 13:58:00.359	localhost	127.0.0.1	管理员	管理员	总部
1506	模块分配操作	保存	模块操作关系ID:usr;	3;row	2012-02-09 14:26:40.109	localhost	127.0.0.1	jacob	经理	河北
1512	用户登录	用户登录	总部-管理员-管理员		2012-02-09 14:56:33.718	localhost	127.0.0.1	管理员	管理员	总部
1520	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:17:16.687	localhost	127.0.0.1	管理员	管理员	总部
1524	系统公告管理	修改	修改公告ID：6;修改公告标题：工;有效期：Tue Jan 31 00:00:00 CST 2012;是否发布：0;	6	2012-02-09 15:40:23.593	localhost	127.0.0.1	管理员	管理员	总部
1528	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:42:17.625	localhost	127.0.0.1	管理员	管理员	总部
1530	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:44:26.39	localhost	127.0.0.1	管理员	管理员	总部
1534	系统模块管理	保存	测试二;testMgrId;	62	2012-02-09 16:30:03.187	localhost	127.0.0.1	管理员	管理员	总部
1541	用户登录	用户登录	河北-经理-jacob		2012-02-09 16:35:02.953	localhost	127.0.0.1	jacob	经理	河北
1542	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-09 16:35:48.218	localhost	127.0.0.1	jacob	经理	河北
1550	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:18:51.953	localhost	127.0.0.1	管理员	管理员	总部
1556	模块分配操作	保存	模块操作关系ID:58;59;60;61;62;91;97;152;	12;opt	2012-02-09 17:37:56.171	localhost	127.0.0.1	管理员	管理员	总部
1562	用户登录	用户登录	河北-经理-jacob		2012-02-09 17:41:54.718	localhost	127.0.0.1	jacob	经理	河北
1564	模块分配操作	保存	模块操作关系ID:1;2;3;4;5;6;7;87;89;90;10;11;12;13;14;15;16;17;18;19;25;26;27;28;29;36;37;38;39;40;98;99;100;101;154;104;105;106;107;108;109;110;111;153;44;30;31;33;34;35;45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;65;66;68;69;116;117;118;119;112;113;114;115;151;	12;opt	2012-02-09 17:43:22.718	localhost	127.0.0.1	jacob	经理	河北
1567	用户登录	用户登录	河北-经理-jacob		2012-02-09 17:46:37.046	localhost	127.0.0.1	jacob	经理	河北
1568	模块分配操作	保存	模块操作关系ID:152;91;	12;opt	2012-02-09 17:47:05.609	localhost	127.0.0.1	jacob	经理	河北
1573	用户登录	用户登录	总部-管理员-管理员		2012-02-09 21:45:02.437	localhost	127.0.0.1	管理员	管理员	总部
1578	用户登录	用户登录	总部-管理员-管理员		2012-02-12 18:32:18.75	localhost	127.0.0.1	管理员	管理员	总部
1579	用户登录	用户登录	总部-管理员-管理员		2012-02-12 18:37:19.031	localhost	127.0.0.1	管理员	管理员	总部
1580	用户登录	用户登录	总部-管理员-管理员		2012-02-12 18:40:45.484	localhost	127.0.0.1	管理员	管理员	总部
1584	用户登录	用户登录	总部-管理员-管理员		2012-02-12 18:53:36.14	localhost	127.0.0.1	管理员	管理员	总部
1591	系统模块管理	修改	系统日志管理;sysLogMgr;	18	2012-02-12 19:02:30.484	localhost	127.0.0.1	管理员	管理员	总部
1595	系统公告管理	修改	修改公告ID：6;修改公告标题：工;有效期：Thu Mar 29 00:00:00 CST 2012;是否发布：0;	6	2012-02-12 19:14:59.5	localhost	127.0.0.1	管理员	管理员	总部
1599	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:33:53.218	localhost	127.0.0.1	管理员	管理员	总部
1609	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:43:42.39	localhost	127.0.0.1	管理员	管理员	总部
1612	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:45:14.203	localhost	127.0.0.1	管理员	管理员	总部
1617	用户登录	用户登录	总部-管理员-管理员		2012-02-13 09:48:02.109	localhost	127.0.0.1	管理员	管理员	总部
1618	用户登录	用户登录	总部-管理员-管理员		2012-02-13 09:54:55.64	localhost	127.0.0.1	管理员	管理员	总部
1620	用户登录	用户登录	总部-管理员-管理员		2012-02-13 09:55:52.125	localhost	127.0.0.1	管理员	管理员	总部
1626	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:11:19.234	localhost	127.0.0.1	管理员	管理员	总部
1339	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:31:27.375	localhost	127.0.0.1	管理员	管理员	总部
1353	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;64;91;97;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-08 11:02:10.14	localhost	127.0.0.1	管理员	管理员	总部
1355	系统模块操作管理	修改	普通权限;assign;optpri;	8	2012-02-08 11:13:26.468	localhost	127.0.0.1	管理员	管理员	总部
1358	系统模块操作管理	修改	特殊权限;assign;optpri;	28	2012-02-08 11:17:02.64	localhost	127.0.0.1	管理员	管理员	总部
1361	用户登录	用户登录	总部-管理员-管理员		2012-02-08 13:37:06.834	localhost	127.0.0.1	管理员	管理员	总部
1372	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:24:08.224	localhost	127.0.0.1	管理员	管理员	总部
1373	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:24:25.49	localhost	127.0.0.1	管理员	管理员	总部
1387	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:46:20.974	localhost	127.0.0.1	管理员	管理员	总部
1388	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:49:28.771	localhost	127.0.0.1	管理员	管理员	总部
1390	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:55:00.037	localhost	127.0.0.1	管理员	管理员	总部
1394	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:02:59.287	localhost	127.0.0.1	管理员	管理员	总部
1405	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:22:51.506	localhost	127.0.0.1	管理员	管理员	总部
1422	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:48:16.74	localhost	127.0.0.1	管理员	管理员	总部
1439	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	1;opt	2012-02-08 16:46:51.177	localhost	127.0.0.1	管理员	管理员	总部
1449	模块分配操作	保存	模块操作关系ID:	3;opt	2012-02-08 17:00:45.912	localhost	127.0.0.1	管理员	管理员	总部
1460	用户管理	重置密码	重置密码用户：jacob	12	2012-02-08 17:40:20.849	localhost	127.0.0.1	管理员	管理员	总部
1467	用户登录	用户登录	河北-经理-jacob		2012-02-09 09:23:46.546	localhost	127.0.0.1	jacob	经理	河北
1478	模块分配操作	保存	模块操作关系ID:55;	3;opt	2012-02-09 09:44:40.953	localhost	127.0.0.1	管理员	管理员	总部
1483	用户登录	用户登录	河北-经理-jacob		2012-02-09 13:31:47.953	localhost	127.0.0.1	jacob	经理	河北
1490	模块分配操作	保存	模块操作关系ID:;	3;row	2012-02-09 13:50:24.75	localhost	127.0.0.1	管理员	管理员	总部
1496	模块分配操作	保存	模块操作关系ID:rol;	1;row	2012-02-09 13:59:04.671	localhost	127.0.0.1	管理员	管理员	总部
1499	模块分配操作	保存	模块操作关系ID:rol;	3;row	2012-02-09 14:01:25.093	localhost	127.0.0.1	管理员	管理员	总部
1501	模块分配操作	保存	模块操作关系ID:usr;	3;row	2012-02-09 14:02:04.343	localhost	127.0.0.1	管理员	管理员	总部
1510	用户登录	用户登录	总部-管理员-管理员		2012-02-09 14:47:32.562	localhost	127.0.0.1	管理员	管理员	总部
1521	模块分配操作	保存	模块操作关系ID:55;	3;opt	2012-02-09 15:29:05.875	localhost	127.0.0.1	管理员	管理员	总部
1529	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:42:45.281	localhost	127.0.0.1	管理员	管理员	总部
1535	用户登录	用户登录	总部-管理员-管理员		2012-02-09 16:30:27.843	localhost	127.0.0.1	管理员	管理员	总部
1544	用户登录	用户登录	总部-管理员-管理员		2012-02-09 16:43:20.296	localhost	127.0.0.1	管理员	管理员	总部
1545	用户登录	用户登录	总部-管理员-管理员		2012-02-09 16:52:04.859	HENRYCOMPUTER	192.168.1.205	管理员	管理员	总部
1551	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：33,7,34		2012-02-09 17:20:03.125	localhost	127.0.0.1	管理员	管理员	总部
1557	模块分配操作	保存	模块操作关系ID:58;59;60;61;62;91;97;152;	12;opt	2012-02-09 17:38:51.312	localhost	127.0.0.1	管理员	管理员	总部
1571	角色分配	保存	机构id:4;角色id:10;		2012-02-09 17:48:51.515	HENRYCOMPUTER	192.168.1.205	管理员	管理员	总部
1574	系统配置管理	保存	设置系统名称:jacob_liang平台管理统一用户;		2012-02-09 21:46:17.406	localhost	127.0.0.1	管理员	管理员	总部
1581	用户登录	用户登录	总部-管理员-管理员		2012-02-12 18:44:28.968	localhost	127.0.0.1	管理员	管理员	总部
1586	系统调度管理	启动job	启动job:;jobName:postgresqlBbBackup;job类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;	3	2012-02-12 18:59:19.703	localhost	127.0.0.1	管理员	管理员	总部
1592	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:02:40.687	localhost	127.0.0.1	管理员	管理员	总部
1596	系统公告管理	修改	修改公告ID：4;修改公告标题：ff;有效期：Sat Dec 31 00:00:00 CST 2011;是否发布：0;	4	2012-02-12 19:15:17.5	localhost	127.0.0.1	管理员	管理员	总部
1600	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:34:06.312	localhost	127.0.0.1	管理员	管理员	总部
1603	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:35:33.734	localhost	127.0.0.1	管理员	管理员	总部
1606	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:36:44.468	localhost	127.0.0.1	管理员	管理员	总部
1611	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:44:47.031	localhost	127.0.0.1	管理员	管理员	总部
1621	用户登录	用户登录	总部-管理员-管理员		2012-02-13 09:56:58.343	localhost	127.0.0.1	管理员	管理员	总部
1624	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:05:59.875	localhost	127.0.0.1	管理员	管理员	总部
1627	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:12:21.531	localhost	127.0.0.1	管理员	管理员	总部
1628	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:13:11.25	localhost	127.0.0.1	管理员	管理员	总部
1629	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:14:00.078	localhost	127.0.0.1	管理员	管理员	总部
1630	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:15:49.156	localhost	127.0.0.1	管理员	管理员	总部
1633	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:22:11.406	localhost	127.0.0.1	管理员	管理员	总部
1635	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:24:32.765	localhost	127.0.0.1	管理员	管理员	总部
1636	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:25:04.906	localhost	127.0.0.1	管理员	管理员	总部
1641	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:27:43.578	localhost	127.0.0.1	管理员	管理员	总部
1341	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:35:02.765	localhost	127.0.0.1	管理员	管理员	总部
1345	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:37:48.593	localhost	127.0.0.1	管理员	管理员	总部
1349	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:51:16.765	localhost	127.0.0.1	管理员	管理员	总部
1354	用户管理	修改	jacob;jacob;null;	12	2012-02-08 11:10:22	localhost	127.0.0.1	管理员	管理员	总部
1362	模块分配操作	保存	模块分配操作;28	34	2012-02-08 13:41:51.631	localhost	127.0.0.1	管理员	管理员	总部
1375	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:28:47.818	localhost	127.0.0.1	管理员	管理员	总部
1376	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:29:58.646	localhost	127.0.0.1	管理员	管理员	总部
1377	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:31:37.131	localhost	127.0.0.1	管理员	管理员	总部
1378	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:32:40.693	localhost	127.0.0.1	管理员	管理员	总部
1379	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:34:36.365	localhost	127.0.0.1	管理员	管理员	总部
1391	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:57:07.271	localhost	127.0.0.1	管理员	管理员	总部
1431	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:21:53.912	localhost	127.0.0.1	管理员	管理员	总部
1435	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:34:51.427	localhost	127.0.0.1	管理员	管理员	总部
1436	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:39:10.099	localhost	127.0.0.1	管理员	管理员	总部
1437	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:43:44.334	localhost	127.0.0.1	管理员	管理员	总部
1438	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:46:03.099	localhost	127.0.0.1	管理员	管理员	总部
1454	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-08 17:10:03.006	localhost	127.0.0.1	管理员	管理员	总部
1461	用户登录	用户登录	河北-经理(默认)-jacob		2012-02-08 17:41:20.74	localhost	127.0.0.1	jacob	经理(默认)	河北
1462	用户登录	用户登录	河北-经理(默认)-jacob		2012-02-08 17:47:37.724	localhost	127.0.0.1	jacob	经理(默认)	河北
1468	用户登录	用户登录	河北-经理-jacob		2012-02-09 09:24:00.187	localhost	127.0.0.1	jacob	经理	河北
1472	用户登录	用户登录	河北-经理-jacob		2012-02-09 09:35:45.609	localhost	127.0.0.1	jacob	经理	河北
1479	用户登录	用户登录	河北-经理-jacob		2012-02-09 09:44:50	localhost	127.0.0.1	jacob	经理	河北
1484	模块分配操作	保存	模块操作关系ID:55;45;46;47;48;49;	3;opt	2012-02-09 13:32:13.343	localhost	127.0.0.1	jacob	经理	河北
1488	用户登录	用户登录	总部-管理员-管理员		2012-02-09 13:43:54.343	localhost	127.0.0.1	管理员	管理员	总部
1491	用户登录	用户登录	总部-管理员-管理员		2012-02-09 13:50:42.984	localhost	127.0.0.1	管理员	管理员	总部
1500	模块分配操作	保存	模块操作关系ID:55;	3;opt	2012-02-09 14:01:49.625	localhost	127.0.0.1	管理员	管理员	总部
1511	用户登录	用户登录	总部-管理员-管理员		2012-02-09 14:52:27.671	localhost	127.0.0.1	管理员	管理员	总部
1522	用户常用功能设置	保存	用户常用功能保存:用户ID0;模块ID：33,7,14,25,10		2012-02-09 15:30:53.859	localhost	127.0.0.1	管理员	管理员	总部
1533	系统调度管理	停止job	停止job:postgresqlBbBackup		2012-02-09 16:01:18.89	localhost	127.0.0.1	管理员	管理员	总部
1546	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:08:07.39	localhost	127.0.0.1	管理员	管理员	总部
1552	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:33:49.843	localhost	127.0.0.1	管理员	管理员	总部
1553	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:36:13.421	bhtec	192.168.1.3	管理员	管理员	总部
1554	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:36:19.015	bhtec	192.168.1.3	管理员	管理员	总部
1575	用户登录	用户登录	总部-管理员-管理员		2012-02-09 21:47:46.531	localhost	127.0.0.1	管理员	管理员	总部
1582	用户登录	用户登录	总部-管理员-管理员		2012-02-12 18:48:16.39	localhost	127.0.0.1	管理员	管理员	总部
1587	系统调度管理	停止调度器	停止调度器		2012-02-12 18:59:29.453	localhost	127.0.0.1	管理员	管理员	总部
1589	系统调度管理	启动job	启动job:;jobName:postgresqlBbBackup;job类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;	3	2012-02-12 19:01:29.437	localhost	127.0.0.1	管理员	管理员	总部
1593	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:09:14.703	localhost	127.0.0.1	管理员	管理员	总部
1597	用户管理	修改	jacob;jacob;null;	12	2012-02-12 19:16:08.14	localhost	127.0.0.1	管理员	管理员	总部
1601	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:34:59.453	localhost	127.0.0.1	管理员	管理员	总部
1604	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:36:03.234	localhost	127.0.0.1	管理员	管理员	总部
1607	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:36:59.031	localhost	127.0.0.1	管理员	管理员	总部
1610	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:44:24.765	localhost	127.0.0.1	管理员	管理员	总部
1613	用户登录	用户登录	总部-管理员-管理员		2012-02-12 19:48:29.281	localhost	127.0.0.1	管理员	管理员	总部
1615	用户登录	用户登录	总部-管理员-管理员		2012-02-13 09:15:22.234	localhost	127.0.0.1	管理员	管理员	总部
1622	用户登录	用户登录	总部-管理员-管理员		2012-02-13 09:58:36.203	localhost	127.0.0.1	管理员	管理员	总部
1631	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:16:20.687	localhost	127.0.0.1	管理员	管理员	总部
1632	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:17:07.687	localhost	127.0.0.1	管理员	管理员	总部
1637	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:25:30.156	localhost	127.0.0.1	管理员	管理员	总部
1639	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:27:02.906	localhost	127.0.0.1	管理员	管理员	总部
1642	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:28:51.015	localhost	127.0.0.1	管理员	管理员	总部
1643	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:31:32.187	localhost	127.0.0.1	管理员	管理员	总部
1644	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:32:37.453	localhost	127.0.0.1	管理员	管理员	总部
1347	用户登录	用户登录	总部-管理员-管理员		2012-01-19 14:45:20.937	localhost	127.0.0.1	管理员	管理员	总部
1356	用户登录	用户登录	总部-管理员-管理员		2012-02-08 11:13:39.625	localhost	127.0.0.1	管理员	管理员	总部
1363	用户登录	用户登录	总部-管理员-管理员		2012-02-08 13:42:03.646	localhost	127.0.0.1	管理员	管理员	总部
1366	用户登录	用户登录	总部-管理员-管理员		2012-02-08 13:43:16.974	localhost	127.0.0.1	管理员	管理员	总部
1368	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:17:42.084	localhost	127.0.0.1	管理员	管理员	总部
1380	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:34:59.974	localhost	127.0.0.1	管理员	管理员	总部
1381	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:35:09.599	localhost	127.0.0.1	管理员	管理员	总部
1382	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:35:30.209	localhost	127.0.0.1	管理员	管理员	总部
1383	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:40:16.021	localhost	127.0.0.1	管理员	管理员	总部
1384	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:41:28.896	localhost	127.0.0.1	管理员	管理员	总部
1392	用户登录	用户登录	总部-管理员-管理员		2012-02-08 14:59:29.162	localhost	127.0.0.1	管理员	管理员	总部
1414	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:36:52.959	localhost	127.0.0.1	管理员	管理员	总部
1415	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:37:42.959	localhost	127.0.0.1	管理员	管理员	总部
1416	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:38:19.693	localhost	127.0.0.1	管理员	管理员	总部
1417	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:40:43.052	localhost	127.0.0.1	管理员	管理员	总部
1418	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:41:58.037	localhost	127.0.0.1	管理员	管理员	总部
1419	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:44:58.271	localhost	127.0.0.1	管理员	管理员	总部
1420	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:46:32.756	localhost	127.0.0.1	管理员	管理员	总部
1424	用户登录	用户登录	总部-管理员-管理员		2012-02-08 15:55:37.349	localhost	127.0.0.1	管理员	管理员	总部
1425	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:01:10.521	localhost	127.0.0.1	管理员	管理员	总部
1426	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:03:02.412	localhost	127.0.0.1	管理员	管理员	总部
1427	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:04:08.037	localhost	127.0.0.1	管理员	管理员	总部
1428	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:06:32.131	localhost	127.0.0.1	管理员	管理员	总部
1429	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:07:27.006	localhost	127.0.0.1	管理员	管理员	总部
1430	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:08:25.506	localhost	127.0.0.1	管理员	管理员	总部
1432	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:24:30.959	localhost	127.0.0.1	管理员	管理员	总部
1433	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:29:13.412	localhost	127.0.0.1	管理员	管理员	总部
1434	用户登录	用户登录	总部-管理员-管理员		2012-02-08 16:30:29.771	localhost	127.0.0.1	管理员	管理员	总部
1441	模块分配操作	保存	模块操作关系ID:	3;opt	2012-02-08 16:53:43.834	localhost	127.0.0.1	管理员	管理员	总部
1447	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-08 16:58:41.881	localhost	127.0.0.1	管理员	管理员	总部
1463	用户登录	用户登录	河北-经理(默认)-jacob		2012-02-08 17:49:52.037	localhost	127.0.0.1	jacob	经理(默认)	河北
1470	用户登录	用户登录	河北-经理-jacob		2012-02-09 09:27:04.656	localhost	127.0.0.1	jacob	经理	河北
1471	用户登录	用户登录	总部-管理员-管理员		2012-02-09 09:28:15.921	localhost	127.0.0.1	管理员	管理员	总部
1485	用户登录	用户登录	河北-经理-jacob		2012-02-09 13:32:31.656	localhost	127.0.0.1	jacob	经理	河北
1492	模块分配操作	保存	模块操作关系ID:;	3;row	2012-02-09 13:52:42.968	localhost	127.0.0.1	管理员	管理员	总部
1497	模块分配操作	保存	模块操作关系ID:org;	3;row	2012-02-09 13:59:50.921	localhost	127.0.0.1	管理员	管理员	总部
1502	用户登录	用户登录	总部-管理员-管理员		2012-02-09 14:24:09.89	localhost	127.0.0.1	管理员	管理员	总部
1504	模块分配操作	保存	模块操作关系ID:rol;	3;row	2012-02-09 14:25:37.89	localhost	127.0.0.1	管理员	管理员	总部
1507	用户登录	用户登录	河北-经理-jacob		2012-02-09 14:27:04.765	localhost	127.0.0.1	jacob	经理	河北
1513	用户登录	用户登录	总部-管理员-管理员		2012-02-09 14:57:53.296	localhost	127.0.0.1	管理员	管理员	总部
1518	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:07:16.062	localhost	127.0.0.1	管理员	管理员	总部
1523	角色分配	保存	机构id:2;角色id:3;		2012-02-09 15:32:14.546	localhost	127.0.0.1	管理员	管理员	总部
1525	用户登录	用户登录	总部-管理员-管理员		2012-02-09 15:40:28.328	localhost	127.0.0.1	管理员	管理员	总部
1536	系统模块管理	修改	测试二;testMgrId;	62	2012-02-09 16:31:24	localhost	127.0.0.1	管理员	管理员	总部
1540	用户登录	用户登录	总部-管理员-管理员		2012-02-09 16:34:29.406	localhost	127.0.0.1	管理员	管理员	总部
1547	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:15:48.187	localhost	127.0.0.1	管理员	管理员	总部
1555	用户登录	用户登录	总部-管理员-管理员		2012-02-09 17:36:34.734	HENRYCOMPUTER	192.168.1.205	管理员	管理员	总部
1558	模块分配操作	保存	模块操作关系ID:58;59;60;61;62;91;97;152;	12;opt	2012-02-09 17:39:22.296	localhost	127.0.0.1	管理员	管理员	总部
1560	用户登录	用户登录	河北-经理-jacob		2012-02-09 17:39:58.125	localhost	127.0.0.1	jacob	经理	河北
1563	模块分配操作	保存	模块分配操作;8	49	2012-02-09 17:42:10.515	HENRYCOMPUTER	192.168.1.205	管理员	管理员	总部
1565	用户登录	用户登录	河北-经理-jacob		2012-02-09 17:43:38.281	localhost	127.0.0.1	jacob	经理	河北
1566	机构管理	保存	机构名称：山西;机构代码：;上级机构：总部;	3	2012-02-09 17:44:32.64	HENRYCOMPUTER	192.168.1.205	管理员	管理员	总部
1569	用户登录	用户登录	河北-经理-jacob		2012-02-09 17:47:27.859	localhost	127.0.0.1	jacob	经理	河北
1570	机构管理	保存	机构名称：山西太原;机构代码：;上级机构：山西;	4	2012-02-09 17:48:27.89	HENRYCOMPUTER	192.168.1.205	管理员	管理员	总部
1645	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:33:49.921	localhost	127.0.0.1	管理员	管理员	总部
1650	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:41:20.703	localhost	127.0.0.1	管理员	管理员	总部
1659	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:48:55.296	localhost	127.0.0.1	管理员	管理员	总部
1924	用户登录	用户登录	总部-管理员-管理员		2012-02-16 13:40:41.765	localhost	127.0.0.1	管理员	管理员	总部
1931	用户登录	用户登录	总部-管理员-管理员		2012-02-16 14:06:23.156	localhost	127.0.0.1	管理员	管理员	总部
1966	用户登录	用户登录	总部-管理员-管理员		2012-02-17 09:34:27.968	localhost	127.0.0.1	管理员	管理员	总部
1977	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:41:44.562	localhost	127.0.0.1	管理员	管理员	总部
2011	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:51:28.25	localhost	127.0.0.1	管理员	管理员	总部
2016	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:54:57.953	localhost	127.0.0.1	管理员	管理员	总部
2022	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:06:49.046	localhost	127.0.0.1	管理员	管理员	总部
2023	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:20:53.781	localhost	127.0.0.1	管理员	管理员	总部
2034	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:46:01.046	localhost	127.0.0.1	管理员	管理员	总部
2041	用户登录	用户登录	总部-管理员-管理员		2012-02-20 09:48:14.265	localhost	127.0.0.1	管理员	管理员	总部
2068	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:22:04.156	localhost	127.0.0.1	管理员	管理员	总部
2082	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:46:00.093	localhost	127.0.0.1	管理员	管理员	总部
2101	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:38:43.25	localhost	127.0.0.1	管理员	管理员	总部
2109	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:11:39.671	localhost	127.0.0.1	管理员	管理员	总部
2110	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:13:05.687	localhost	127.0.0.1	管理员	管理员	总部
2111	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:15:13.64	localhost	127.0.0.1	管理员	管理员	总部
2112	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:17:27.234	localhost	127.0.0.1	管理员	管理员	总部
2113	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:18:02.031	localhost	127.0.0.1	管理员	管理员	总部
2114	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:18:50.046	localhost	127.0.0.1	管理员	管理员	总部
2115	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:21:11.843	localhost	127.0.0.1	管理员	管理员	总部
2129	用户登录	用户登录	总部-管理员-管理员		2012-02-21 12:01:19.921	localhost	127.0.0.1	管理员	管理员	总部
2146	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:28:28.734	localhost	127.0.0.1	管理员	管理员	总部
2161	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:54:42.281	localhost	127.0.0.1	管理员	管理员	总部
2177	用户登录	用户登录	总部-管理员-管理员		2012-02-22 10:34:06.578	localhost	127.0.0.1	管理员	管理员	总部
2180	用户登录	用户登录	总部-管理员-管理员		2012-02-22 10:41:22.64	localhost	127.0.0.1	管理员	管理员	总部
2181	用户登录	用户登录	总部-管理员-管理员		2012-02-22 10:51:37.718	localhost	127.0.0.1	管理员	管理员	总部
2188	用户登录	用户登录	总部-管理员-管理员		2012-02-22 11:56:43.828	localhost	127.0.0.1	管理员	管理员	总部
2189	用户登录	用户登录	总部-管理员-管理员		2012-02-22 13:00:13.75	localhost	127.0.0.1	管理员	管理员	总部
2241	用户登录	用户登录	总部-管理员-管理员		2012-02-23 14:53:35.937	localhost	127.0.0.1	管理员	管理员	总部
2242	用户登录	用户登录	总部-管理员-管理员		2012-02-23 14:54:45.968	localhost	127.0.0.1	管理员	管理员	总部
2253	用户登录	用户登录	总部-管理员-雅各布-D-管理员		2012-02-24 13:57:22.093	localhost	127.0.0.1	雅各布-D-管理员	管理员	总部
2279	用户登录	用户登录	总部-管理员-管理员		2012-02-25 21:02:55.468	localhost	127.0.0.1	管理员	管理员	总部
2280	用户分配角色	保存	11,30	12	2012-02-25 21:03:34.328	localhost	127.0.0.1	管理员	管理员	总部
2281	用户登录	用户登录	河北-经理-雅各布		2012-02-27 10:13:44.359	localhost	127.0.0.1	雅各布	经理	河北
2300	用户登录	用户登录	总部-管理员-管理员		2012-02-28 09:59:13.437	localhost	127.0.0.1	管理员	管理员	总部
2359	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 16:03:22.359	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2374	用户登录	用户登录	总部-管理员-管理员		2012-02-29 10:55:39.984	localhost	127.0.0.1	管理员	管理员	总部
2380	用户登录	用户登录	总部-管理员-管理员		2012-03-06 11:36:31.796	localhost	127.0.0.1	管理员	管理员	总部
2384	角色管理	修改	副总经理;	4	2012-03-06 11:45:57.062	localhost	127.0.0.1	管理员	管理员	总部
2389	角色管理	修改	运营经理;	7	2012-03-06 11:47:37.156	localhost	127.0.0.1	管理员	管理员	总部
2397	角色管理	保存	储运主管;	12	2012-03-06 11:50:30.921	localhost	127.0.0.1	管理员	管理员	总部
2400	角色管理	保存	培训主管;	15	2012-03-06 14:07:16.718	localhost	127.0.0.1	管理员	管理员	总部
2406	省市地区管理	保存	天津;city;	2004	2012-03-06 14:39:21.687	localhost	127.0.0.1	管理员	管理员	总部
2418	机构管理	保存	机构名称：储运部;机构代码：;上级机构：总经办;	12	2012-03-06 14:48:38.093	localhost	127.0.0.1	管理员	管理员	总部
2435	角色分配	保存	机构id:2;角色id:7;		2012-03-06 14:57:14.531	localhost	127.0.0.1	管理员	管理员	总部
2444	角色分配	保存	机构id:1;角色id:20;		2012-03-06 15:06:45.031	localhost	127.0.0.1	管理员	管理员	总部
2452	用户分配角色	保存	46	14	2012-03-06 15:14:04.875	localhost	127.0.0.1	管理员	管理员	总部
2456	用户管理	保存	gaoxf;高晓峰;五道口店-收银;	18	2012-03-06 15:21:32.765	localhost	127.0.0.1	管理员	管理员	总部
2463	用户登录	用户登录	总部-管理员-管理员		2012-03-06 15:30:43.75	localhost	127.0.0.1	管理员	管理员	总部
2512	用户登录	用户登录	总部-管理员-管理员		2012-03-07 09:56:59.453	localhost	127.0.0.1	管理员	管理员	总部
2514	用户登录	用户登录	总部-管理员-管理员		2012-03-07 10:02:15.687	localhost	127.0.0.1	管理员	管理员	总部
2515	用户登录	用户登录	总部-管理员-管理员		2012-03-07 10:06:10.687	localhost	127.0.0.1	管理员	管理员	总部
1646	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:35:02.281	localhost	127.0.0.1	管理员	管理员	总部
1651	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:42:37.39	localhost	127.0.0.1	管理员	管理员	总部
1925	用户登录	用户登录	总部-管理员-管理员		2012-02-16 13:43:12.125	localhost	127.0.0.1	管理员	管理员	总部
1926	用户登录	用户登录	总部-管理员-管理员		2012-02-16 13:46:57.734	localhost	127.0.0.1	管理员	管理员	总部
1930	用户登录	用户登录	总部-管理员-管理员		2012-02-16 14:01:30.359	localhost	127.0.0.1	管理员	管理员	总部
1967	用户登录	用户登录	总部-管理员-管理员		2012-02-17 09:35:50.609	localhost	127.0.0.1	管理员	管理员	总部
1984	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:52:44.203	localhost	127.0.0.1	管理员	管理员	总部
2012	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:51:54	localhost	127.0.0.1	管理员	管理员	总部
2017	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:56:12.562	localhost	127.0.0.1	管理员	管理员	总部
2035	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:46:49.968	localhost	127.0.0.1	管理员	管理员	总部
2042	用户登录	用户登录	总部-管理员-管理员		2012-02-20 10:33:38.281	localhost	127.0.0.1	管理员	管理员	总部
2069	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:28:46.031	localhost	127.0.0.1	管理员	管理员	总部
2071	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:36:51.281	localhost	127.0.0.1	管理员	管理员	总部
2081	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:45:43.828	localhost	127.0.0.1	管理员	管理员	总部
2091	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:52:32.25	localhost	127.0.0.1	管理员	管理员	总部
2107	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:06:25.078	localhost	127.0.0.1	管理员	管理员	总部
2108	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:09:41.718	localhost	127.0.0.1	管理员	管理员	总部
2148	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:30:05.906	localhost	127.0.0.1	管理员	管理员	总部
2152	用户代理	保存	8;0;12;null;	8	2012-02-21 15:37:32.437	localhost	127.0.0.1	管理员	管理员	总部
2164	用户代理	保存	12;0;12;null;	12	2012-02-21 15:58:09.968	localhost	127.0.0.1	管理员	管理员	总部
2178	用户登录	用户登录	总部-管理员-管理员		2012-02-22 10:35:34.937	localhost	127.0.0.1	管理员	管理员	总部
2191	用户登录	用户登录	总部-管理员-管理员		2012-02-22 13:10:18.859	localhost	127.0.0.1	管理员	管理员	总部
2193	用户代理	保存	19;12;13;null;	19	2012-02-22 13:13:12.156	localhost	127.0.0.1	雅各布	经理	河北
2198	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:10:37.13	localhost	127.0.0.1	管理员	管理员	总部
2243	用户登录	用户登录	总部-管理员-管理员		2012-02-23 17:02:31.484	localhost	127.0.0.1	管理员	管理员	总部
2254	用户登录	用户登录	总部-管理员-管理员		2012-02-24 13:59:14.765	localhost	127.0.0.1	管理员	管理员	总部
2257	用户登录	用户登录	总部-管理员-管理员		2012-02-24 14:02:29.89	localhost	127.0.0.1	管理员	管理员	总部
2282	用户登录	用户登录	河北-经理-雅各布		2012-02-27 10:28:48.203	localhost	127.0.0.1	雅各布	经理	河北
2301	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 10:00:01.14	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2302	用户登录	用户登录	北京-北京-会计-雅各布		2012-02-28 10:00:25.234	localhost	127.0.0.1	雅各布	北京-会计	北京
2360	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 16:07:06.937	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2375	系统模块操作管理	修改	模块标签修改;module_label;moduleLabel;	14	2012-02-29 10:56:00.671	localhost	127.0.0.1	管理员	管理员	总部
2376	模块分配操作	保存	模块分配操作;14	7	2012-02-29 10:56:27.953	localhost	127.0.0.1	管理员	管理员	总部
2381	模块分配操作	保存	模块分配操作;	7	2012-03-06 11:37:08.281	localhost	127.0.0.1	管理员	管理员	总部
2383	角色管理	修改	总经理;	3	2012-03-06 11:45:31.406	localhost	127.0.0.1	管理员	管理员	总部
2388	角色管理	修改	储运部经理;	6	2012-03-06 11:47:09.812	localhost	127.0.0.1	管理员	管理员	总部
2396	角色管理	保存	人事主管;	11	2012-03-06 11:50:08.578	localhost	127.0.0.1	管理员	管理员	总部
2399	角色管理	保存	财务主管;	14	2012-03-06 14:06:50.75	localhost	127.0.0.1	管理员	管理员	总部
2404	用户登录	用户登录	总部-管理员-管理员		2012-03-06 14:38:25.359	localhost	127.0.0.1	管理员	管理员	总部
2419	机构管理	保存	机构名称：开发部;机构代码：;上级机构：总经办;	13	2012-03-06 14:49:02.406	localhost	127.0.0.1	管理员	管理员	总部
2425	机构管理	保存	机构名称：北京采购;机构代码：;上级机构：采购部;	14	2012-03-06 14:52:44.171	localhost	127.0.0.1	管理员	管理员	总部
2446	用户管理	修改	wangj;王娟;null;	12	2012-03-06 15:11:45.328	localhost	127.0.0.1	管理员	管理员	总部
2513	机构管理	修改	机构名称：总经办;机构代码：ZJB;上级机构：董事会;	11	2012-03-07 09:59:59.687	localhost	127.0.0.1	管理员	管理员	总部
1647	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:38:31.015	localhost	127.0.0.1	管理员	管理员	总部
1648	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:40:12.484	localhost	127.0.0.1	管理员	管理员	总部
1649	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:40:47.296	localhost	127.0.0.1	管理员	管理员	总部
1658	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:48:40.406	localhost	127.0.0.1	管理员	管理员	总部
1661	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:50:48.265	localhost	127.0.0.1	管理员	管理员	总部
1662	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:53:33.593	localhost	127.0.0.1	管理员	管理员	总部
1664	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:59:50.109	localhost	127.0.0.1	管理员	管理员	总部
1665	用户登录	用户登录	总部-管理员-管理员		2012-02-13 11:01:32.375	localhost	127.0.0.1	管理员	管理员	总部
1666	用户登录	用户登录	总部-管理员-管理员		2012-02-13 11:10:29.796	localhost	127.0.0.1	管理员	管理员	总部
1927	用户登录	用户登录	总部-管理员-管理员		2012-02-16 13:48:50.25	localhost	127.0.0.1	管理员	管理员	总部
1968	用户登录	用户登录	总部-管理员-管理员		2012-02-17 09:44:52.453	localhost	127.0.0.1	管理员	管理员	总部
1986	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:53:58.093	localhost	127.0.0.1	管理员	管理员	总部
2013	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:53:21.593	localhost	127.0.0.1	管理员	管理员	总部
2018	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:56:38.218	localhost	127.0.0.1	管理员	管理员	总部
2031	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:41:47.859	localhost	127.0.0.1	管理员	管理员	总部
2036	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:47:44.359	localhost	127.0.0.1	管理员	管理员	总部
2043	用户登录	用户登录	总部-管理员-管理员		2012-02-20 10:34:14.031	localhost	127.0.0.1	管理员	管理员	总部
2070	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:35:42.187	localhost	127.0.0.1	管理员	管理员	总部
2080	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:44:55.015	localhost	127.0.0.1	管理员	管理员	总部
2090	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:51:17.031	localhost	127.0.0.1	管理员	管理员	总部
2092	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:00:13.187	localhost	127.0.0.1	管理员	管理员	总部
2149	用户代理	保存	6;12;0;null;	6	2012-02-21 15:30:36.39	localhost	127.0.0.1	管理员	管理员	总部
2150	用户代理	保存	7;0;null;null;	7	2012-02-21 15:35:43.187	localhost	127.0.0.1	管理员	管理员	总部
2162	用户代理	保存	11;0;13;null;	11	2012-02-21 15:55:15.14	localhost	127.0.0.1	管理员	管理员	总部
2179	用户登录	用户登录	总部-管理员-管理员		2012-02-22 10:39:10.843	localhost	127.0.0.1	管理员	管理员	总部
2187	用户登录	用户登录	总部-管理员-管理员		2012-02-22 11:56:14.046	localhost	127.0.0.1	管理员	管理员	总部
2192	用户登录	用户登录	河北-经理-雅各布		2012-02-22 13:11:06.593	localhost	127.0.0.1	雅各布	经理	河北
2194	用户登录	用户登录	总部-管理员-管理员		2012-02-22 13:19:34.234	localhost	127.0.0.1	管理员	管理员	总部
2244	用户代理	保存	24;0;12;	24	2012-02-23 17:04:25.484	localhost	127.0.0.1	管理员	管理员	总部
2246	用户登录	用户登录	河北-经理-雅各布(代理)管理员		2012-02-23 17:15:01.828	localhost	127.0.0.1	雅各布(代理)管理员	经理	河北
2255	用户登录	用户登录	总部-管理员-管理员		2012-02-24 14:00:36.593	localhost	127.0.0.1	管理员	管理员	总部
2283	用户登录	用户登录	河北-经理-雅各布		2012-02-27 14:09:40.609	localhost	127.0.0.1	雅各布	经理	河北
2303	用户登录	用户登录	河北-河北-经理(默认)-雅各布(D-管理员)		2012-02-28 10:00:53.5	localhost	127.0.0.1	雅各布(D-管理员)	河北-经理(默认)	河北
2364	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:13:56.968	localhost	127.0.0.1	挑大梁	经理	北京
2373	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:20:52.75	localhost	127.0.0.1	挑大梁	经理	北京
2377	用户登录	用户登录	总部-管理员-管理员		2012-02-29 10:56:48.25	localhost	127.0.0.1	管理员	管理员	总部
2382	用户登录	用户登录	总部-管理员-管理员		2012-03-06 11:37:24.265	localhost	127.0.0.1	管理员	管理员	总部
2395	角色管理	修改	采购经理;	10	2012-03-06 11:49:41.671	localhost	127.0.0.1	管理员	管理员	总部
2427	机构管理	保存	机构名称：北京储运;机构代码：;上级机构：储运部;	16	2012-03-06 14:54:15.39	localhost	127.0.0.1	管理员	管理员	总部
2429	角色分配	保存	机构id:11;角色id:3;机构id:11;角色id:4;		2012-03-06 14:55:37.046	localhost	127.0.0.1	管理员	管理员	总部
2447	用户分配角色	保存	56	12	2012-03-06 15:12:00.062	localhost	127.0.0.1	管理员	管理员	总部
2455	用户管理	保存	zhangl;张丽;五道口店-店长;	17	2012-03-06 15:16:22.39	localhost	127.0.0.1	管理员	管理员	总部
2516	用户登录	用户登录	总部-管理员-管理员		2012-03-07 10:07:19.703	localhost	127.0.0.1	管理员	管理员	总部
2522	用户登录	用户登录	运营部-运营经理-于志平		2012-03-07 10:15:46.578	localhost	127.0.0.1	于志平	运营经理	运营部
1652	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:43:41.984	localhost	127.0.0.1	管理员	管理员	总部
1657	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:47:46.484	localhost	127.0.0.1	管理员	管理员	总部
1928	模块分配操作	保存	模块操作关系ID:58;59;60;61;62;91;97;152;	3;opt	2012-02-16 13:51:47.796	localhost	127.0.0.1	管理员	管理员	总部
1969	用户登录	用户登录	总部-管理员-管理员		2012-02-17 09:49:58.328	localhost	127.0.0.1	管理员	管理员	总部
1980	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:44:57.859	localhost	127.0.0.1	管理员	管理员	总部
2014	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:53:42.203	localhost	127.0.0.1	管理员	管理员	总部
2019	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:57:00.203	localhost	127.0.0.1	管理员	管理员	总部
2032	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:42:16	localhost	127.0.0.1	管理员	管理员	总部
2037	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:48:29.765	localhost	127.0.0.1	管理员	管理员	总部
2038	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:50:28.437	localhost	127.0.0.1	管理员	管理员	总部
2039	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:51:03.875	localhost	127.0.0.1	管理员	管理员	总部
2044	用户登录	用户登录	总部-管理员-管理员		2012-02-20 11:43:45.14	localhost	127.0.0.1	管理员	管理员	总部
2072	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:37:49.312	localhost	127.0.0.1	管理员	管理员	总部
2083	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:46:41.531	localhost	127.0.0.1	管理员	管理员	总部
2094	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:28:52.437	localhost	127.0.0.1	管理员	管理员	总部
2102	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:38:57.875	localhost	127.0.0.1	管理员	管理员	总部
2117	用户代理	保存	4;12;0;null;	4	2012-02-21 11:23:57.453	localhost	127.0.0.1	管理员	管理员	总部
2151	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:37:15.25	localhost	127.0.0.1	管理员	管理员	总部
2153	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:42:35.203	localhost	127.0.0.1	管理员	管理员	总部
2158	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:48:31.093	localhost	127.0.0.1	管理员	管理员	总部
2182	用户登录	用户登录	总部-管理员-管理员		2012-02-22 10:54:25.031	localhost	127.0.0.1	管理员	管理员	总部
2195	用户代理	保存	20;0;12;null;	20	2012-02-22 13:20:14.781	localhost	127.0.0.1	管理员	管理员	总部
2245	用户登录	用户登录	河北-经理-管理员(代理)雅各布		2012-02-23 17:08:13.468	localhost	127.0.0.1	管理员(代理)雅各布	经理	河北
2256	用户登录	用户登录	总部-管理员-管理员		2012-02-24 14:02:08.234	localhost	127.0.0.1	管理员	管理员	总部
2262	用户登录	用户登录	河北-会计-雅各布-D-伊米		2012-02-24 14:29:05.062	localhost	127.0.0.1	雅各布-D-伊米	会计	河北
2284	用户登录	用户登录	河北-会计-伊米		2012-02-27 14:50:21.062	localhost	127.0.0.1	伊米	会计	河北
2304	用户登录	用户登录	总部-管理员-管理员		2012-02-28 10:43:42.812	localhost	127.0.0.1	管理员	管理员	总部
2306	用户登录	用户登录	总部-管理员-管理员		2012-02-28 10:52:43.875	localhost	127.0.0.1	管理员	管理员	总部
2322	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 11:32:30.609	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2365	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:14:00.828	localhost	127.0.0.1	挑大梁	经理	北京
2378	系统模块操作管理	修改	模块标签修改;module_label;moduleLabel;	14	2012-02-29 10:57:24.906	localhost	127.0.0.1	管理员	管理员	总部
2385	角色管理	修改	人事经理;	5	2012-03-06 11:46:24.062	localhost	127.0.0.1	管理员	管理员	总部
2390	角色管理	修改	财务经理;	8	2012-03-06 11:48:17.562	localhost	127.0.0.1	管理员	管理员	总部
2393	角色管理	修改	运营经理;	7	2012-03-06 11:49:01.718	localhost	127.0.0.1	管理员	管理员	总部
2401	角色管理	保存	采购主管;	16	2012-03-06 14:07:33.39	localhost	127.0.0.1	管理员	管理员	总部
2424	机构管理	修改	机构名称：山西运营;机构代码：;上级机构：运营部;	3	2012-03-06 14:51:26.312	localhost	127.0.0.1	管理员	管理员	总部
2434	角色分配	删除	删除机构下的角色ID：3;4;10;	2	2012-03-06 14:56:56.781	localhost	127.0.0.1	管理员	管理员	总部
2440	角色分配	保存	机构id:6;角色id:17;机构id:6;角色id:18;机构id:6;角色id:19;		2012-03-06 14:59:16.187	localhost	127.0.0.1	管理员	管理员	总部
2441	机构管理	修改	机构名称：五道口店;机构代码：;上级机构：海淀;	6	2012-03-06 14:59:52.281	localhost	127.0.0.1	管理员	管理员	总部
2460	系统配置管理	保存	增加管理员:wangj;		2012-03-06 15:26:21.859	localhost	127.0.0.1	管理员	管理员	总部
2517	用户登录	用户登录	总部-管理员-管理员		2012-03-07 10:07:35.171	localhost	127.0.0.1	管理员	管理员	总部
2519	用户登录	用户登录	总部-管理员-管理员		2012-03-07 10:13:06.859	localhost	127.0.0.1	管理员	管理员	总部
2524	用户登录	用户登录	运营部-运营经理-于志平		2012-03-07 10:17:00.125	localhost	127.0.0.1	于志平	运营经理	运营部
1653	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:44:20.171	localhost	127.0.0.1	管理员	管理员	总部
1667	用户登录	用户登录	总部-管理员-管理员		2012-02-13 11:16:11.265	localhost	127.0.0.1	管理员	管理员	总部
1929	用户登录	用户登录	河北-经理-jacob		2012-02-16 13:52:50.859	localhost	127.0.0.1	jacob	经理	河北
1932	用户登录	用户登录	总部-管理员-管理员		2012-02-16 14:15:45.703	localhost	127.0.0.1	管理员	管理员	总部
1970	用户登录	用户登录	总部-管理员-管理员		2012-02-17 09:51:02.906	localhost	127.0.0.1	管理员	管理员	总部
1975	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:36:34.593	localhost	127.0.0.1	管理员	管理员	总部
1981	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:47:27.984	localhost	127.0.0.1	管理员	管理员	总部
1982	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:49:40.468	localhost	127.0.0.1	管理员	管理员	总部
2015	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:54:20.921	localhost	127.0.0.1	管理员	管理员	总部
2020	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:57:27.359	localhost	127.0.0.1	管理员	管理员	总部
2021	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:06:15.703	localhost	127.0.0.1	管理员	管理员	总部
2033	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:45:12.031	localhost	127.0.0.1	管理员	管理员	总部
2040	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:52:44.75	localhost	127.0.0.1	管理员	管理员	总部
2045	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:12:04.687	localhost	127.0.0.1	管理员	管理员	总部
2073	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:38:03.718	localhost	127.0.0.1	管理员	管理员	总部
2084	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:47:00.906	localhost	127.0.0.1	管理员	管理员	总部
2095	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:30:37.656	localhost	127.0.0.1	管理员	管理员	总部
2103	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:40:55	localhost	127.0.0.1	管理员	管理员	总部
2105	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:46:30.578	localhost	127.0.0.1	管理员	管理员	总部
2106	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:53:03.765	localhost	127.0.0.1	管理员	管理员	总部
2127	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:58:14.734	localhost	127.0.0.1	管理员	管理员	总部
2128	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:59:40.703	localhost	127.0.0.1	管理员	管理员	总部
2154	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:45:52.828	localhost	127.0.0.1	管理员	管理员	总部
2163	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:57:33.64	localhost	127.0.0.1	管理员	管理员	总部
2165	用户代理	保存	13;0;12;null;	13	2012-02-21 16:06:48.078	localhost	127.0.0.1	管理员	管理员	总部
2183	用户登录	用户登录	总部-管理员-管理员		2012-02-22 10:54:56.375	localhost	127.0.0.1	管理员	管理员	总部
2184	用户登录	用户登录	总部-管理员-管理员		2012-02-22 11:03:47.296	localhost	127.0.0.1	管理员	管理员	总部
2185	用户登录	用户登录	总部-管理员-管理员		2012-02-22 11:12:37.921	localhost	127.0.0.1	管理员	管理员	总部
2190	用户登录	用户登录	总部-管理员-管理员		2012-02-22 13:02:27.75	localhost	127.0.0.1	管理员	管理员	总部
2247	用户登录	用户登录	总部-管理员-雅各布(代理)管理员		2012-02-23 17:18:39.25	localhost	127.0.0.1	雅各布(代理)管理员	管理员	总部
2251	用户代理	保存	25;13;12;	25	2012-02-23 18:03:55.312	localhost	127.0.0.1	伊米	会计	河北
2258	用户登录	用户登录	总部-管理员-管理员		2012-02-24 14:02:40.656	localhost	127.0.0.1	管理员	管理员	总部
2260	用户登录	用户登录	总部-管理员-管理员		2012-02-24 14:09:29.562	localhost	127.0.0.1	管理员	管理员	总部
2285	用户登录	用户登录	河北-经理-雅各布		2012-02-27 16:33:50.078	localhost	127.0.0.1	雅各布	经理	河北
2305	用户登录	用户登录	总部-无角色用户-雅各布(D-管理员)		2012-02-28 10:49:30.781	localhost	127.0.0.1	雅各布(D-管理员)	无角色用户	总部
2314	用户登录	用户登录	北京-北京-会计-雅各布		2012-02-28 11:10:08.187	localhost	127.0.0.1	雅各布	北京-会计	北京
2317	用户代理	保存	32;13;12;	32	2012-02-28 11:13:18.75	localhost	127.0.0.1	伊米	会计	河北
2366	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:14:36.765	localhost	127.0.0.1	挑大梁	经理	北京
2368	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:14:42	localhost	127.0.0.1	挑大梁	经理	北京
2370	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:16:26.64	localhost	127.0.0.1	挑大梁	经理	北京
2372	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:19:17.265	localhost	127.0.0.1	挑大梁	经理	北京
2379	用户登录	用户登录	总部-管理员-管理员		2012-02-29 10:57:30.875	localhost	127.0.0.1	管理员	管理员	总部
2386	角色管理	修改	副总经理;	4	2012-03-06 11:46:30.671	localhost	127.0.0.1	管理员	管理员	总部
2411	机构管理	保存	机构名称：财务部;机构代码：;上级机构：董事会;	9	2012-03-06 14:44:45.187	localhost	127.0.0.1	管理员	管理员	总部
2426	机构管理	保存	机构名称：上海采购;机构代码：;上级机构：采购部;	15	2012-03-06 14:53:18.921	localhost	127.0.0.1	管理员	管理员	总部
2430	角色分配	保存	机构id:7;角色id:7;		2012-03-06 14:56:03.875	localhost	127.0.0.1	管理员	管理员	总部
2457	用户管理	保存	wangx;王旭;五道口店-店员;	19	2012-03-06 15:21:56.578	localhost	127.0.0.1	管理员	管理员	总部
2518	用户登录	用户登录	总部-管理员-管理员		2012-03-07 10:09:47.265	localhost	127.0.0.1	管理员	管理员	总部
2531	用户登录	用户登录	五道口店-五道口店-店员-于志平		2012-03-07 10:45:42.265	localhost	127.0.0.1	于志平	五道口店-店员	五道口店
2532	用户登录	用户登录	总部-管理员-管理员		2012-03-07 11:11:48.125	localhost	127.0.0.1	管理员	管理员	总部
1654	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:45:02.421	localhost	127.0.0.1	管理员	管理员	总部
1933	用户登录	用户登录	总部-管理员-管理员		2012-02-16 14:25:26.906	localhost	127.0.0.1	管理员	管理员	总部
1971	用户登录	用户登录	总部-管理员-管理员		2012-02-17 09:51:37.859	localhost	127.0.0.1	管理员	管理员	总部
1976	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:38:50.468	localhost	127.0.0.1	管理员	管理员	总部
2025	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:23:42.046	localhost	127.0.0.1	管理员	管理员	总部
2046	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:12:56.718	localhost	127.0.0.1	管理员	管理员	总部
2074	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:39:12.609	localhost	127.0.0.1	管理员	管理员	总部
2085	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:47:35.5	localhost	127.0.0.1	管理员	管理员	总部
2096	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:31:32.625	localhost	127.0.0.1	管理员	管理员	总部
2104	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:43:00.39	localhost	127.0.0.1	管理员	管理员	总部
2116	用户代理	保存	3;12;null;null;	3	2012-02-21 11:22:40.843	localhost	127.0.0.1	管理员	管理员	总部
2126	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:51:27.796	localhost	127.0.0.1	管理员	管理员	总部
2156	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:46:56.046	localhost	127.0.0.1	管理员	管理员	总部
2157	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:47:46.921	localhost	127.0.0.1	管理员	管理员	总部
2186	用户登录	用户登录	总部-管理员-管理员		2012-02-22 11:13:16.062	localhost	127.0.0.1	管理员	管理员	总部
2248	用户登录	用户登录	总部-管理员-雅各布(代理)管理员		2012-02-23 17:20:29.453	localhost	127.0.0.1	雅各布(代理)管理员	管理员	总部
2259	用户登录	用户登录	总部-管理员-管理员		2012-02-24 14:07:37.109	localhost	127.0.0.1	管理员	管理员	总部
2286	用户登录	用户登录	北京-会计-雅各布		2012-02-27 16:34:48.109	localhost	127.0.0.1	雅各布	会计	北京
2307	用户代理	保存	29;0;12;	29	2012-02-28 10:54:59.234	localhost	127.0.0.1	管理员	管理员	总部
2315	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 11:10:29.031	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2320	用户登录	用户登录	河北-会计-伊米(雅各布D)		2012-02-28 11:26:06.906	localhost	127.0.0.1	伊米(雅各布D)	会计	河北
2387	用户登录	用户登录	总部-管理员-管理员		2012-03-06 11:46:46.843	localhost	127.0.0.1	管理员	管理员	总部
2392	角色管理	修改	财务经理;	8	2012-03-06 11:48:55.781	localhost	127.0.0.1	管理员	管理员	总部
2398	角色管理	保存	运营主管;	13	2012-03-06 14:06:27.703	localhost	127.0.0.1	管理员	管理员	总部
2402	类别字典管理	修改	字典大类名称：机构类型;	23	2012-03-06 14:14:38.89	localhost	127.0.0.1	管理员	管理员	总部
2410	机构管理	保存	机构名称：人力资源;机构代码：;上级机构：董事会;	8	2012-03-06 14:44:23.89	localhost	127.0.0.1	管理员	管理员	总部
2417	机构管理	修改	机构名称：运营部;机构代码：;上级机构：总经办;	7	2012-03-06 14:47:35.562	localhost	127.0.0.1	管理员	管理员	总部
2422	机构管理	修改	机构名称：太原;机构代码：;上级机构：山西;	4	2012-03-06 14:50:18.968	localhost	127.0.0.1	管理员	管理员	总部
2433	用户管理	停用	jacob;雅各布;总经理;	12	2012-03-06 14:56:50.062	localhost	127.0.0.1	管理员	管理员	总部
2437	角色管理	保存	店长;	17	2012-03-06 14:58:25.875	localhost	127.0.0.1	管理员	管理员	总部
2445	角色分配	删除	删除机构下的角色ID：7;	5	2012-03-06 15:09:09.187	localhost	127.0.0.1	管理员	管理员	总部
2449	用户管理	停用	shijh;石纪红;无角色用户;	13	2012-03-06 15:12:50.546	localhost	127.0.0.1	管理员	管理员	总部
2464	用户登录	用户登录	总部-管理员-管理员		2012-03-06 15:35:42.609	localhost	127.0.0.1	管理员	管理员	总部
2520	用户登录	用户登录	总部-管理员-管理员		2012-03-07 10:13:59.75	localhost	127.0.0.1	管理员	管理员	总部
2526	用户登录	用户登录	运营部-运营经理-于志平		2012-03-07 10:18:26.5	localhost	127.0.0.1	于志平	运营经理	运营部
1655	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:45:59.609	localhost	127.0.0.1	管理员	管理员	总部
1663	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:57:17.296	localhost	127.0.0.1	管理员	管理员	总部
1934	系统配置管理	保存	设置权限级别:rol;		2012-02-16 14:26:04.046	localhost	127.0.0.1	管理员	管理员	总部
1972	用户登录	用户登录	总部-管理员-管理员		2012-02-17 09:54:31.015	localhost	127.0.0.1	管理员	管理员	总部
1983	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:50:18.343	localhost	127.0.0.1	管理员	管理员	总部
2026	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:25:51.359	localhost	127.0.0.1	管理员	管理员	总部
2027	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:26:56.843	localhost	127.0.0.1	管理员	管理员	总部
2029	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:28:18.812	localhost	127.0.0.1	管理员	管理员	总部
2030	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:29:16	localhost	127.0.0.1	管理员	管理员	总部
2047	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:13:11.531	localhost	127.0.0.1	管理员	管理员	总部
2075	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:39:25.14	localhost	127.0.0.1	管理员	管理员	总部
2086	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:49:09.796	localhost	127.0.0.1	管理员	管理员	总部
2097	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:33:01.453	localhost	127.0.0.1	管理员	管理员	总部
2118	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:26:32.171	localhost	127.0.0.1	管理员	管理员	总部
2159	用户代理	保存	10;0;12;null;	10	2012-02-21 15:49:00.921	localhost	127.0.0.1	管理员	管理员	总部
2196	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:09:23.692	localhost	127.0.0.1	管理员	管理员	总部
2249	用户登录	用户登录	总部-管理员-雅各布(D)管理员		2012-02-23 17:49:34.843	localhost	127.0.0.1	雅各布(D)管理员	管理员	总部
2252	用户登录	用户登录	河北-会计-雅各布(D)伊米		2012-02-23 18:15:34.859	localhost	127.0.0.1	雅各布(D)伊米	会计	河北
2261	用户登录	用户登录	总部-管理员-管理员		2012-02-24 14:09:36.109	localhost	127.0.0.1	管理员	管理员	总部
2287	用户登录	用户登录	总部-管理员-管理员		2012-02-27 16:35:55.609	localhost	127.0.0.1	管理员	管理员	总部
2288	模块分配操作	保存	模块操作关系ID:5;6;7;87;89;90;10;11;12;13;14;	4;opt	2012-02-27 16:36:48.89	localhost	127.0.0.1	管理员	管理员	总部
2308	用户代理	保存	30;0;12;	30	2012-02-28 10:55:00.234	localhost	127.0.0.1	管理员	管理员	总部
2311	用户登录	用户登录	总部-管理员-雅各布(D-管理员)		2012-02-28 10:59:10.64	localhost	127.0.0.1	雅各布(D-管理员)	管理员	总部
2316	用户登录	用户登录	河北-会计-伊米		2012-02-28 11:11:30.359	localhost	127.0.0.1	伊米	会计	河北
2391	角色管理	修改	培训经理;	9	2012-03-06 11:48:46.64	localhost	127.0.0.1	管理员	管理员	总部
2394	角色管理	修改	采购;	10	2012-03-06 11:49:33.609	localhost	127.0.0.1	管理员	管理员	总部
2403	机构管理	修改	机构名称：董事会;机构代码：DSH;上级机构：总部;	1	2012-03-06 14:15:42.765	localhost	127.0.0.1	管理员	管理员	总部
2428	机构管理	保存	机构名称：上海储运;机构代码：;上级机构：储运部;	17	2012-03-06 14:54:36.656	localhost	127.0.0.1	管理员	管理员	总部
2432	用户管理	停用	jacob_liang;伊米;副总经理;	13	2012-03-06 14:56:45.421	localhost	127.0.0.1	管理员	管理员	总部
2448	用户管理	修改	shijh;石纪红;null;	13	2012-03-06 15:12:43.718	localhost	127.0.0.1	管理员	管理员	总部
2450	用户分配角色	保存	45	13	2012-03-06 15:13:02.718	localhost	127.0.0.1	管理员	管理员	总部
2453	用户管理	保存	yuzp;于志平;运营部-运营经理;	15	2012-03-06 15:15:09	localhost	127.0.0.1	管理员	管理员	总部
2458	用户管理	保存	wangxm;王帅明;五道口店-店员;	20	2012-03-06 15:23:08.875	localhost	127.0.0.1	管理员	管理员	总部
2461	模块分配操作	保存	模块操作关系ID:58;59;60;61;62;91;97;152;45;46;47;48;49;50;51;52;53;54;55;56;57;65;66;68;69;116;117;118;119;112;113;114;115;1;2;3;4;5;6;7;87;155;10;11;12;13;14;15;16;17;18;19;25;26;27;28;29;36;37;38;39;40;98;99;100;101;154;104;105;106;107;108;109;110;111;44;30;31;33;34;35;	3;opt	2012-03-06 15:26:56.718	localhost	127.0.0.1	管理员	管理员	总部
2521	模块分配操作	保存	模块操作关系ID:rol;	7;row	2012-03-07 10:15:27.078	localhost	127.0.0.1	管理员	管理员	总部
2529	用户登录	用户登录	运营部-运营部-运营经理(默认)-于志平		2012-03-07 10:45:22.562	localhost	127.0.0.1	于志平	运营部-运营经理(默认)	运营部
1656	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:46:35.14	localhost	127.0.0.1	管理员	管理员	总部
1660	用户登录	用户登录	总部-管理员-管理员		2012-02-13 10:49:31.843	localhost	127.0.0.1	管理员	管理员	总部
1668	用户登录	用户登录	总部-管理员-管理员		2012-02-13 11:47:35.015	localhost	127.0.0.1	管理员	管理员	总部
1669	用户登录	用户登录	总部-管理员-管理员		2012-02-13 11:52:44.906	localhost	127.0.0.1	管理员	管理员	总部
1670	用户登录	用户登录	总部-管理员-管理员		2012-02-13 11:54:01.859	localhost	127.0.0.1	管理员	管理员	总部
1671	用户登录	用户登录	总部-管理员-管理员		2012-02-13 11:55:43.609	localhost	127.0.0.1	管理员	管理员	总部
1672	用户登录	用户登录	总部-管理员-管理员		2012-02-13 11:58:21.484	localhost	127.0.0.1	管理员	管理员	总部
1673	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:19:44.187	localhost	127.0.0.1	管理员	管理员	总部
1674	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:20:20.687	localhost	127.0.0.1	管理员	管理员	总部
1675	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:23:25.562	localhost	127.0.0.1	管理员	管理员	总部
1676	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:26:09.781	localhost	127.0.0.1	管理员	管理员	总部
1677	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:30:00.171	localhost	127.0.0.1	管理员	管理员	总部
1678	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:31:22.093	localhost	127.0.0.1	管理员	管理员	总部
1679	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:32:44.531	localhost	127.0.0.1	管理员	管理员	总部
1680	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:35:08.14	localhost	127.0.0.1	管理员	管理员	总部
1681	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:41:47.812	localhost	127.0.0.1	管理员	管理员	总部
1682	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:42:38.671	localhost	127.0.0.1	管理员	管理员	总部
1683	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:43:43.953	localhost	127.0.0.1	管理员	管理员	总部
1684	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:51:11.781	localhost	127.0.0.1	管理员	管理员	总部
1685	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:52:02.765	localhost	127.0.0.1	管理员	管理员	总部
1686	用户登录	用户登录	总部-管理员-管理员		2012-02-13 15:57:06.64	localhost	127.0.0.1	管理员	管理员	总部
1687	用户登录	用户登录	总部-管理员-管理员		2012-02-13 16:02:25.015	localhost	127.0.0.1	管理员	管理员	总部
1688	用户登录	用户登录	总部-管理员-管理员		2012-02-13 16:11:02.406	localhost	127.0.0.1	管理员	管理员	总部
1689	用户登录	用户登录	总部-管理员-管理员		2012-02-13 16:23:04.515	localhost	127.0.0.1	管理员	管理员	总部
1690	用户登录	用户登录	总部-管理员-管理员		2012-02-13 16:28:57.937	localhost	127.0.0.1	管理员	管理员	总部
1691	用户登录	用户登录	总部-管理员-管理员		2012-02-13 16:38:47.984	localhost	127.0.0.1	管理员	管理员	总部
1692	用户登录	用户登录	总部-管理员-管理员		2012-02-13 16:51:05.515	localhost	127.0.0.1	管理员	管理员	总部
1693	用户登录	用户登录	总部-管理员-管理员		2012-02-13 16:56:18.609	localhost	127.0.0.1	管理员	管理员	总部
1694	用户登录	用户登录	总部-管理员-管理员		2012-02-13 16:57:45.312	localhost	127.0.0.1	管理员	管理员	总部
1695	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:00:12.218	localhost	127.0.0.1	管理员	管理员	总部
1696	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:01:59.937	localhost	127.0.0.1	管理员	管理员	总部
1697	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:04:52.281	localhost	127.0.0.1	管理员	管理员	总部
1698	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:08:37.562	localhost	127.0.0.1	管理员	管理员	总部
1699	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:10:03.796	localhost	127.0.0.1	管理员	管理员	总部
1700	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:11:23.453	localhost	127.0.0.1	管理员	管理员	总部
1701	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:12:47.843	localhost	127.0.0.1	管理员	管理员	总部
1702	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:13:18.406	localhost	127.0.0.1	管理员	管理员	总部
1703	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:16:23.5	localhost	127.0.0.1	管理员	管理员	总部
1704	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:17:26.031	localhost	127.0.0.1	管理员	管理员	总部
1705	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:21:49.125	localhost	127.0.0.1	管理员	管理员	总部
1706	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:24:38.953	localhost	127.0.0.1	管理员	管理员	总部
1707	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:26:35.546	localhost	127.0.0.1	管理员	管理员	总部
1708	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:31:02.343	localhost	127.0.0.1	管理员	管理员	总部
1709	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:31:35.906	localhost	127.0.0.1	管理员	管理员	总部
1710	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:32:09.14	localhost	127.0.0.1	管理员	管理员	总部
1711	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:33:28.671	localhost	127.0.0.1	管理员	管理员	总部
1712	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:34:07.39	localhost	127.0.0.1	管理员	管理员	总部
1713	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:34:58.125	localhost	127.0.0.1	管理员	管理员	总部
1714	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:36:46.953	localhost	127.0.0.1	管理员	管理员	总部
1715	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:38:50.453	localhost	127.0.0.1	管理员	管理员	总部
1716	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:39:14.796	localhost	127.0.0.1	管理员	管理员	总部
1717	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:39:50.281	localhost	127.0.0.1	管理员	管理员	总部
1718	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:40:52.484	localhost	127.0.0.1	管理员	管理员	总部
1719	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:43:50.734	localhost	127.0.0.1	管理员	管理员	总部
1720	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:44:05.875	localhost	127.0.0.1	管理员	管理员	总部
1721	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:44:08.187	localhost	127.0.0.1	管理员	管理员	总部
1935	用户登录	用户登录	河北-经理-jacob		2012-02-16 14:37:17.234	localhost	127.0.0.1	jacob	经理	河北
1973	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:01:55.718	localhost	127.0.0.1	管理员	管理员	总部
1979	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:44:00.203	localhost	127.0.0.1	管理员	管理员	总部
2028	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:27:54.625	localhost	127.0.0.1	管理员	管理员	总部
2048	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:15:32.265	localhost	127.0.0.1	管理员	管理员	总部
2049	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:18:11.156	localhost	127.0.0.1	管理员	管理员	总部
2076	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:39:50.14	localhost	127.0.0.1	管理员	管理员	总部
2087	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:49:38.234	localhost	127.0.0.1	管理员	管理员	总部
2093	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:17:06.593	localhost	127.0.0.1	管理员	管理员	总部
2098	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:35:23.625	localhost	127.0.0.1	管理员	管理员	总部
2120	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:42:31.781	localhost	127.0.0.1	管理员	管理员	总部
2121	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:43:20.593	localhost	127.0.0.1	管理员	管理员	总部
2122	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:43:44.921	localhost	127.0.0.1	管理员	管理员	总部
2123	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:46:03.375	localhost	127.0.0.1	管理员	管理员	总部
2124	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:48:08.75	localhost	127.0.0.1	管理员	管理员	总部
2125	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:49:26	localhost	127.0.0.1	管理员	管理员	总部
2166	用户登录	用户登录	总部-管理员-管理员		2012-02-21 16:50:59.75	localhost	127.0.0.1	管理员	管理员	总部
2171	用户登录	用户登录	总部-管理员-管理员		2012-02-21 16:59:34.234	localhost	127.0.0.1	管理员	管理员	总部
2197	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:10:11.52	localhost	127.0.0.1	管理员	管理员	总部
2250	用户登录	用户登录	河北-会计-伊米		2012-02-23 18:03:34.359	localhost	127.0.0.1	伊米	会计	河北
2263	用户登录	用户登录	总部-管理员-t-D-管理员		2012-02-24 15:08:57.031	localhost	127.0.0.1	t-D-管理员	管理员	总部
2274	用户登录	用户登录	总部-管理员-管理员		2012-02-24 18:05:30.828	localhost	127.0.0.1	管理员	管理员	总部
2289	用户登录	用户登录	总部-管理员-管理员		2012-02-27 17:22:56.453	localhost	127.0.0.1	管理员	管理员	总部
2291	用户代理	保存	28;0;13;	28	2012-02-27 17:25:03.859	localhost	127.0.0.1	管理员	管理员	总部
2309	用户登录	用户登录	总部-管理员-管理员		2012-02-28 10:57:39.14	localhost	127.0.0.1	管理员	管理员	总部
2310	用户代理	保存	31;0;12;	31	2012-02-28 10:58:18.484	localhost	127.0.0.1	管理员	管理员	总部
2321	用户登录	用户登录	总部-管理员-管理员		2012-02-28 11:27:54.265	localhost	127.0.0.1	管理员	管理员	总部
2405	省市地区管理	保存	上海;province;	2003	2012-03-06 14:38:46.375	localhost	127.0.0.1	管理员	管理员	总部
2407	省市地区管理	保存	深圳;city;	2005	2012-03-06 14:39:37.234	localhost	127.0.0.1	管理员	管理员	总部
2408	用户登录	用户登录	总部-管理员-管理员		2012-03-06 14:42:09.656	localhost	127.0.0.1	管理员	管理员	总部
2409	机构管理	保存	机构名称：运营部;机构代码：;上级机构：董事会;	7	2012-03-06 14:43:37.25	localhost	127.0.0.1	管理员	管理员	总部
2431	用户管理	停用	tiaodaliang;挑大梁;总经理;	14	2012-03-06 14:56:38.656	localhost	127.0.0.1	管理员	管理员	总部
2436	角色分配	保存	机构id:5;角色id:7;		2012-03-06 14:57:53.156	localhost	127.0.0.1	管理员	管理员	总部
2438	角色管理	保存	收银;	18	2012-03-06 14:58:43.859	localhost	127.0.0.1	管理员	管理员	总部
2443	角色管理	保存	董事长;	20	2012-03-06 15:06:36.25	localhost	127.0.0.1	管理员	管理员	总部
2454	用户管理	保存	zhangsp;张世鹏;北京运营-运营经理;	16	2012-03-06 15:15:39.078	localhost	127.0.0.1	管理员	管理员	总部
2459	用户管理	保存	zhangj;张军;五道口店-店员;	21	2012-03-06 15:23:39.765	localhost	127.0.0.1	管理员	管理员	总部
2523	模块分配操作	保存	模块操作关系ID:6;14;	15;row	2012-03-07 10:16:50.234	localhost	127.0.0.1	于志平	运营经理	运营部
1722	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:44:37.312	localhost	127.0.0.1	管理员	管理员	总部
1936	用户登录	用户登录	河北-经理-jacob		2012-02-16 14:37:17.25	localhost	127.0.0.1	jacob	经理	河北
1939	用户登录	用户登录	河北-经理-jacob		2012-02-16 14:38:37.562	localhost	127.0.0.1	jacob	经理	河北
1942	用户登录	用户登录	总部-管理员-管理员		2012-02-16 14:42:39.406	localhost	127.0.0.1	管理员	管理员	总部
1974	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:05:42.609	localhost	127.0.0.1	管理员	管理员	总部
1985	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:53:34.609	localhost	127.0.0.1	管理员	管理员	总部
2050	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:20:23.593	localhost	127.0.0.1	管理员	管理员	总部
2055	用户管理	修改	jacob;雅各布;null;	12	2012-02-20 15:51:42.343	localhost	127.0.0.1	管理员	管理员	总部
2077	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:40:10.953	localhost	127.0.0.1	管理员	管理员	总部
2088	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:49:57.781	localhost	127.0.0.1	管理员	管理员	总部
2099	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:36:44.593	localhost	127.0.0.1	管理员	管理员	总部
2167	用户登录	用户登录	总部-管理员-管理员		2012-02-21 16:53:07.921	localhost	127.0.0.1	管理员	管理员	总部
2168	用户代理	保存	14;0;12;null;	14	2012-02-21 16:53:32.359	localhost	127.0.0.1	管理员	管理员	总部
2199	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:39:44.692	localhost	127.0.0.1	管理员	管理员	总部
2200	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:46:02.989	localhost	127.0.0.1	管理员	管理员	总部
2203	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:52:56.458	localhost	127.0.0.1	管理员	管理员	总部
2204	用户代理	保存	21;0;13;null;	21	2012-02-22 14:53:54.161	localhost	127.0.0.1	管理员	管理员	总部
2207	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:00:35.864	localhost	127.0.0.1	管理员	管理员	总部
2209	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:07:00.239	localhost	127.0.0.1	管理员	管理员	总部
2214	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:31:06.973	localhost	127.0.0.1	管理员	管理员	总部
2225	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:52:20.114	localhost	127.0.0.1	管理员	管理员	总部
2226	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:52:47.926	localhost	127.0.0.1	管理员	管理员	总部
2227	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:54:29.317	localhost	127.0.0.1	管理员	管理员	总部
2239	用户登录	用户登录	总部-管理员-管理员		2012-02-22 17:44:03.317	localhost	127.0.0.1	管理员	管理员	总部
2264	用户登录	用户登录	总部-管理员-t-D-管理员		2012-02-24 15:09:34.984	localhost	127.0.0.1	t-D-管理员	管理员	总部
2265	用户登录	用户登录	总部-管理员-雅各布-D-管理员		2012-02-24 15:09:44.171	localhost	127.0.0.1	雅各布-D-管理员	管理员	总部
2272	用户登录	用户登录	总部-管理员-管理员		2012-02-24 18:00:05.843	localhost	127.0.0.1	管理员	管理员	总部
2290	用户代理	保存	27;0;12;	27	2012-02-27 17:24:28.781	localhost	127.0.0.1	管理员	管理员	总部
2312	用户登录	用户登录	总部-管理员-雅各布(D-管理员)		2012-02-28 11:06:27.75	localhost	127.0.0.1	雅各布(D-管理员)	管理员	总部
2318	用户登录	用户登录	河北-会计-伊米(雅各布D)		2012-02-28 11:13:51.75	localhost	127.0.0.1	伊米(雅各布D)	会计	河北
2319	用户登录	用户登录	河北-会计-伊米(雅各布D)		2012-02-28 11:14:20.234	localhost	127.0.0.1	伊米(雅各布D)	会计	河北
2208	用户代理	保存	22;0;12;null;	22	2012-02-22 15:04:42.661	localhost	127.0.0.1	管理员	管理员	总部
2412	机构管理	保存	机构名称：采购部;机构代码：;上级机构：董事会;	10	2012-03-06 14:45:25.171	localhost	127.0.0.1	管理员	管理员	总部
2415	机构管理	修改	机构名称：财务部;机构代码：;上级机构：总经办;	9	2012-03-06 14:47:06.531	localhost	127.0.0.1	管理员	管理员	总部
2416	机构管理	修改	机构名称：人力资源;机构代码：;上级机构：总经办;	8	2012-03-06 14:47:22.296	localhost	127.0.0.1	管理员	管理员	总部
2423	机构管理	修改	机构名称：北京运营;机构代码：;上级机构：运营部;	2	2012-03-06 14:51:03.468	localhost	127.0.0.1	管理员	管理员	总部
2451	用户管理	修改	yangwj;杨文菊;null;	14	2012-03-06 15:13:52.14	localhost	127.0.0.1	管理员	管理员	总部
2462	模块分配操作	保存	模块操作关系ID:5;6;7;87;155;10;11;12;13;14;1;2;3;4;15;16;17;18;19;25;26;27;28;29;36;37;38;39;40;98;99;100;101;154;104;105;106;107;108;109;110;111;44;30;31;33;34;35;	4;opt	2012-03-06 15:27:16.453	localhost	127.0.0.1	管理员	管理员	总部
2525	模块分配操作	保存	模块操作关系ID:58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	15;opt	2012-03-07 10:18:16.625	localhost	127.0.0.1	于志平	运营经理	运营部
2527	用户登录	用户登录	运营部-运营经理-于志平		2012-03-07 10:18:47.968	localhost	127.0.0.1	于志平	运营经理	运营部
2530	用户登录	用户登录	太原-太原-采购经理-于志平		2012-03-07 10:45:31.765	localhost	127.0.0.1	于志平	太原-采购经理	太原
1723	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:45:41.359	localhost	127.0.0.1	管理员	管理员	总部
1726	用户登录	用户登录	总部-管理员-管理员		2012-02-13 18:04:13.375	localhost	127.0.0.1	管理员	管理员	总部
1727	用户登录	用户登录	总部-管理员-管理员		2012-02-13 18:04:45.515	localhost	127.0.0.1	管理员	管理员	总部
1937	用户登录	用户登录	总部-管理员-管理员		2012-02-16 14:38:05.328	localhost	127.0.0.1	管理员	管理员	总部
1978	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:43:25.796	localhost	127.0.0.1	管理员	管理员	总部
2051	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:21:38.64	localhost	127.0.0.1	管理员	管理员	总部
2053	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:25:29.015	localhost	127.0.0.1	管理员	管理员	总部
2058	用户登录	用户登录	总部-管理员-管理员		2012-02-20 16:36:33.171	localhost	127.0.0.1	管理员	管理员	总部
2078	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:41:34.437	localhost	127.0.0.1	管理员	管理员	总部
2079	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:43:14.484	localhost	127.0.0.1	管理员	管理员	总部
2089	用户登录	用户登录	总部-管理员-管理员		2012-02-21 09:50:37.203	localhost	127.0.0.1	管理员	管理员	总部
2100	用户登录	用户登录	总部-管理员-管理员		2012-02-21 10:38:02.937	localhost	127.0.0.1	管理员	管理员	总部
2119	用户登录	用户登录	总部-管理员-管理员		2012-02-21 11:39:56.406	localhost	127.0.0.1	管理员	管理员	总部
2169	用户登录	用户登录	总部-管理员-管理员		2012-02-21 16:57:56.656	localhost	127.0.0.1	管理员	管理员	总部
2201	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:47:35.395	localhost	127.0.0.1	管理员	管理员	总部
2205	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:57:42.88	localhost	127.0.0.1	管理员	管理员	总部
2240	用户登录	用户登录	总部-管理员-管理员		2012-02-22 17:44:36.77	localhost	127.0.0.1	管理员	管理员	总部
2266	用户登录	用户登录	总部-管理员-雅各布(D管理员)		2012-02-24 15:10:30.093	localhost	127.0.0.1	雅各布(D管理员)	管理员	总部
2268	用户登录	用户登录	总部-管理员-管理员		2012-02-24 15:22:19.468	localhost	127.0.0.1	管理员	管理员	总部
2292	用户登录	用户登录	河北-河北-经理(默认)-雅各布(D-管理员)		2012-02-27 17:51:25.14	localhost	127.0.0.1	雅各布(D-管理员)	河北-经理(默认)	河北
2293	用户登录	用户登录	总部-管理员-管理员(D-管理员)		2012-02-27 17:52:31.609	localhost	127.0.0.1	管理员(D-管理员)	管理员	总部
2294	用户登录	用户登录	总部-管理员-管理员(D-管理员)		2012-02-27 17:53:15.859	localhost	127.0.0.1	管理员(D-管理员)	管理员	总部
2313	用户登录	用户登录	总部-管理员-管理员(雅各布D)		2012-02-28 11:09:37.281	localhost	127.0.0.1	管理员(雅各布D)	管理员	总部
2323	用户登录	用户登录	北京-会计-雅各布		2012-02-28 11:32:41.484	localhost	127.0.0.1	雅各布	会计	北京
2413	机构管理	保存	机构名称：总经办;机构代码：;上级机构：董事会;	11	2012-03-06 14:46:04.187	localhost	127.0.0.1	管理员	管理员	总部
2414	机构管理	修改	机构名称：采购部;机构代码：;上级机构：总经办;	10	2012-03-06 14:46:32.296	localhost	127.0.0.1	管理员	管理员	总部
2420	机构管理	修改	机构名称：北京;机构代码：;上级机构：运营部;	2	2012-03-06 14:49:26.046	localhost	127.0.0.1	管理员	管理员	总部
2421	机构管理	修改	机构名称：山西;机构代码：;上级机构：运营部;	3	2012-03-06 14:49:53.468	localhost	127.0.0.1	管理员	管理员	总部
2439	角色管理	保存	店员;	19	2012-03-06 14:59:00.015	localhost	127.0.0.1	管理员	管理员	总部
2442	角色分配	删除	删除机构下的角色ID：4;3;	1	2012-03-06 15:06:15.64	localhost	127.0.0.1	管理员	管理员	总部
2528	用户分配角色	保存	47,53,54,55,40,45,46,56,1	15	2012-03-07 10:19:44.359	localhost	127.0.0.1	于志平	运营经理	运营部
1724	用户登录	用户登录	总部-管理员-管理员		2012-02-13 17:46:11.937	localhost	127.0.0.1	管理员	管理员	总部
1938	系统配置管理	保存	设置权限级别:usr;		2012-02-16 14:38:12.796	localhost	127.0.0.1	管理员	管理员	总部
1941	系统配置管理	保存	设置权限级别:org;		2012-02-16 14:39:49.89	localhost	127.0.0.1	管理员	管理员	总部
1987	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:56:07.593	localhost	127.0.0.1	管理员	管理员	总部
2007	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:49:15.843	localhost	127.0.0.1	管理员	管理员	总部
2052	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:22:43.593	localhost	127.0.0.1	管理员	管理员	总部
2059	用户登录	用户登录	总部-管理员-管理员		2012-02-20 16:39:00.125	localhost	127.0.0.1	管理员	管理员	总部
2130	用户登录	用户登录	总部-管理员-管理员		2012-02-21 13:23:09.593	localhost	127.0.0.1	管理员	管理员	总部
2170	用户代理	保存	15;0;12;null;	15	2012-02-21 16:58:35.296	localhost	127.0.0.1	管理员	管理员	总部
2202	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:49:16.348	localhost	127.0.0.1	管理员	管理员	总部
2219	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:46:02.442	localhost	127.0.0.1	管理员	管理员	总部
2267	用户登录	用户登录	总部-管理员-伊米(D-管理员)		2012-02-24 15:11:44.125	localhost	127.0.0.1	伊米(D-管理员)	管理员	总部
2295	用户登录	用户登录	总部-管理员-管理员		2012-02-27 17:55:11.171	localhost	127.0.0.1	管理员	管理员	总部
2296	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-27 17:55:51.453	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2324	用户登录	用户登录	总部-管理员-管理员		2012-02-28 11:37:33.468	localhost	127.0.0.1	管理员	管理员	总部
2465	模块分配操作	保存	模块操作关系ID:1;2;3;4;5;6;7;87;155;10;11;12;13;14;15;16;17;18;19;25;26;27;28;29;36;37;38;39;40;98;99;100;101;154;104;105;106;107;108;109;110;111;44;30;31;33;34;35;	5;opt	2012-03-06 15:36:13.5	localhost	127.0.0.1	管理员	管理员	总部
2472	用户登录	用户登录	董事会-董事长-王娟		2012-03-06 16:04:12.39	localhost	127.0.0.1	王娟	董事长	董事会
2473	用户登录	用户登录	总经办-总经理-石纪红		2012-03-06 16:04:30.703	localhost	127.0.0.1	石纪红	总经理	总经办
2474	用户登录	用户登录	总经办-副总经理-杨文菊		2012-03-06 16:05:41.937	localhost	127.0.0.1	杨文菊	副总经理	总经办
2478	用户登录	用户登录	北京运营-运营经理-张世鹏		2012-03-06 16:21:35.406	localhost	127.0.0.1	张世鹏	运营经理	北京运营
2495	用户分配角色	保存	55,54	19	2012-03-06 16:33:16.593	localhost	127.0.0.1	管理员	管理员	总部
2496	用户登录	用户登录	五道口店-五道口店-店员(默认)-王旭		2012-03-06 16:33:44	localhost	127.0.0.1	王旭	五道口店-店员(默认)	五道口店
2511	系统编码管理	修改	编码英文名称：RKD;编码中文名称:入库单编码所属模块名称:入库单编码效果:RKD:201203:001:zhangsan	25	2012-03-06 16:46:37.687	localhost	127.0.0.1	管理员	管理员	总部
1725	用户登录	用户登录	总部-管理员-管理员		2012-02-13 18:00:08.687	localhost	127.0.0.1	管理员	管理员	总部
1728	用户登录	用户登录	总部-管理员-管理员		2012-02-14 08:58:42.296	localhost	127.0.0.1	管理员	管理员	总部
1729	用户登录	用户登录	总部-管理员-管理员		2012-02-14 09:08:19.406	localhost	127.0.0.1	管理员	管理员	总部
1730	用户登录	用户登录	总部-管理员-管理员		2012-02-14 09:24:46.937	localhost	127.0.0.1	管理员	管理员	总部
1731	用户登录	用户登录	总部-管理员-管理员		2012-02-14 09:29:00.875	localhost	127.0.0.1	管理员	管理员	总部
1732	用户登录	用户登录	总部-管理员-管理员		2012-02-14 09:30:43.39	localhost	127.0.0.1	管理员	管理员	总部
1733	用户登录	用户登录	总部-管理员-管理员		2012-02-14 09:34:54.093	localhost	127.0.0.1	管理员	管理员	总部
1734	用户登录	用户登录	总部-管理员-管理员		2012-02-14 09:35:06.781	localhost	127.0.0.1	管理员	管理员	总部
1735	用户登录	用户登录	总部-管理员-管理员		2012-02-14 09:50:15.453	localhost	127.0.0.1	管理员	管理员	总部
1736	用户登录	用户登录	总部-管理员-管理员		2012-02-14 09:51:00.671	localhost	127.0.0.1	管理员	管理员	总部
1737	用户登录	用户登录	总部-管理员-管理员		2012-02-14 09:58:18.203	localhost	127.0.0.1	管理员	管理员	总部
1738	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:01:10.234	localhost	127.0.0.1	管理员	管理员	总部
1739	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:03:32.109	localhost	127.0.0.1	管理员	管理员	总部
1740	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:05:29.359	localhost	127.0.0.1	管理员	管理员	总部
1741	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:07:57.843	localhost	127.0.0.1	管理员	管理员	总部
1742	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:09:10.687	localhost	127.0.0.1	管理员	管理员	总部
1743	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:10:47.484	localhost	127.0.0.1	管理员	管理员	总部
1744	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:11:54.468	localhost	127.0.0.1	管理员	管理员	总部
1745	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:12:59.671	localhost	127.0.0.1	管理员	管理员	总部
1746	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:16:23.078	localhost	127.0.0.1	管理员	管理员	总部
1747	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:18:51.078	localhost	127.0.0.1	管理员	管理员	总部
1748	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:36:30.562	localhost	127.0.0.1	管理员	管理员	总部
1749	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:38:46.703	localhost	127.0.0.1	管理员	管理员	总部
1750	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:39:35.75	localhost	127.0.0.1	管理员	管理员	总部
1751	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:41:02.703	localhost	127.0.0.1	管理员	管理员	总部
1752	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:41:29.468	localhost	127.0.0.1	管理员	管理员	总部
1753	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:42:01.718	localhost	127.0.0.1	管理员	管理员	总部
1754	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:42:52.937	localhost	127.0.0.1	管理员	管理员	总部
1755	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:44:58.062	localhost	127.0.0.1	管理员	管理员	总部
1756	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:46:35.062	localhost	127.0.0.1	管理员	管理员	总部
1757	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:47:19.953	localhost	127.0.0.1	管理员	管理员	总部
1758	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:48:02.343	localhost	127.0.0.1	管理员	管理员	总部
1759	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:48:42.234	localhost	127.0.0.1	管理员	管理员	总部
1760	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:49:06.343	localhost	127.0.0.1	管理员	管理员	总部
1761	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:49:37.968	localhost	127.0.0.1	管理员	管理员	总部
1762	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:50:10.968	localhost	127.0.0.1	管理员	管理员	总部
1763	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:50:57.578	localhost	127.0.0.1	管理员	管理员	总部
1764	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:51:19.437	localhost	127.0.0.1	管理员	管理员	总部
1765	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:52:06.656	localhost	127.0.0.1	管理员	管理员	总部
1766	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:54:14.812	localhost	127.0.0.1	管理员	管理员	总部
1767	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:54:51.125	localhost	127.0.0.1	管理员	管理员	总部
1768	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:55:16.765	localhost	127.0.0.1	管理员	管理员	总部
1769	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:55:48.671	localhost	127.0.0.1	管理员	管理员	总部
1770	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:56:42.093	localhost	127.0.0.1	管理员	管理员	总部
1771	用户登录	用户登录	总部-管理员-管理员		2012-02-14 10:59:43.859	localhost	127.0.0.1	管理员	管理员	总部
1772	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:00:23.296	localhost	127.0.0.1	管理员	管理员	总部
1773	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:03:34.968	localhost	127.0.0.1	管理员	管理员	总部
1774	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:04:54.906	localhost	127.0.0.1	管理员	管理员	总部
1775	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:12:31.296	localhost	127.0.0.1	管理员	管理员	总部
1776	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:13:08.031	localhost	127.0.0.1	管理员	管理员	总部
1777	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:14:18.953	localhost	127.0.0.1	管理员	管理员	总部
1778	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:14:51.375	localhost	127.0.0.1	管理员	管理员	总部
1779	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:15:51.843	localhost	127.0.0.1	管理员	管理员	总部
1780	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:16:15.437	localhost	127.0.0.1	管理员	管理员	总部
1781	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:16:37.812	localhost	127.0.0.1	管理员	管理员	总部
1782	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:17:17.906	localhost	127.0.0.1	管理员	管理员	总部
1940	用户登录	用户登录	总部-管理员-管理员		2012-02-16 14:39:21.828	localhost	127.0.0.1	管理员	管理员	总部
1988	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:56:37.14	localhost	127.0.0.1	管理员	管理员	总部
2008	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:53:02.796	localhost	127.0.0.1	管理员	管理员	总部
2054	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:50:47.062	localhost	127.0.0.1	管理员	管理员	总部
2057	用户登录	用户登录	总部-管理员-管理员		2012-02-20 16:10:20.984	localhost	127.0.0.1	管理员	管理员	总部
2131	用户登录	用户登录	总部-管理员-管理员		2012-02-21 13:25:20.765	localhost	127.0.0.1	管理员	管理员	总部
2172	用户代理	保存	16;0;12;null;	16	2012-02-21 17:00:13.984	localhost	127.0.0.1	管理员	管理员	总部
2206	用户登录	用户登录	总部-管理员-管理员		2012-02-22 14:59:56.473	localhost	127.0.0.1	管理员	管理员	总部
2210	用户代理	保存	23;0;12;null;	23	2012-02-22 15:07:33.926	localhost	127.0.0.1	管理员	管理员	总部
2224	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:49:38.833	localhost	127.0.0.1	管理员	管理员	总部
2228	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:55:54.848	localhost	127.0.0.1	管理员	管理员	总部
2269	用户登录	用户登录	河北-经理-雅各布		2012-02-24 15:31:38.703	localhost	127.0.0.1	雅各布	经理	河北
2278	用户登录	用户登录	总部-管理员-管理员		2012-02-24 18:14:52.484	localhost	127.0.0.1	管理员	管理员	总部
2297	用户登录	用户登录	北京-北京-会计-雅各布		2012-02-27 17:56:12.093	localhost	127.0.0.1	雅各布	北京-会计	北京
2325	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 13:15:06.546	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2328	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 13:37:05.781	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2343	系统模块管理	保存	已办任务;finishedMgr;	63	2012-02-28 14:42:06.75	localhost	127.0.0.1	管理员	管理员	总部
2345	用户登录	用户登录	总部-管理员-管理员		2012-02-28 14:46:03.296	localhost	127.0.0.1	管理员	管理员	总部
2354	用户分配角色	保存	11	14	2012-02-28 15:23:31.5	localhost	127.0.0.1	管理员	管理员	总部
2355	用户管理	保存	tiaodaliang;挑大梁;经理;	14	2012-02-28 15:23:31.5	localhost	127.0.0.1	管理员	管理员	总部
2466	角色分配	保存	机构id:2;角色id:13;		2012-03-06 15:45:24.234	localhost	127.0.0.1	管理员	管理员	总部
2486	用户登录	用户登录	五道口店-收银-高晓峰		2012-03-06 16:24:30.656	localhost	127.0.0.1	高晓峰	收银	五道口店
2487	用户登录	用户登录	五道口店-店长-张丽		2012-03-06 16:25:14.484	localhost	127.0.0.1	张丽	店长	五道口店
2492	用户登录	用户登录	五道口店-店长-张丽(王旭D)		2012-03-06 16:31:40.875	localhost	127.0.0.1	张丽(王旭D)	店长	五道口店
2494	用户登录	用户登录	总部-管理员-管理员		2012-03-06 16:32:25.5	localhost	127.0.0.1	管理员	管理员	总部
2510	用户登录	用户登录	总部-管理员-管理员		2012-03-06 16:44:28.531	localhost	127.0.0.1	管理员	管理员	总部
1783	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:17:57.187	localhost	127.0.0.1	管理员	管理员	总部
1787	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:45:36.64	localhost	127.0.0.1	管理员	管理员	总部
1943	用户登录	用户登录	总部-管理员-管理员		2012-02-16 16:03:21.89	localhost	127.0.0.1	管理员	管理员	总部
1960	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:35:26.515	localhost	127.0.0.1	管理员	管理员	总部
1961	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:40:48.781	localhost	127.0.0.1	管理员	管理员	总部
1962	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:44:04.796	localhost	127.0.0.1	管理员	管理员	总部
1989	用户登录	用户登录	总部-管理员-管理员		2012-02-17 10:57:25.984	localhost	127.0.0.1	管理员	管理员	总部
2003	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:41:42.343	localhost	127.0.0.1	管理员	管理员	总部
2056	用户登录	用户登录	总部-管理员-管理员		2012-02-20 15:58:18.859	localhost	127.0.0.1	管理员	管理员	总部
2061	用户登录	用户登录	总部-管理员-管理员		2012-02-20 16:39:56.531	localhost	127.0.0.1	管理员	管理员	总部
2132	用户登录	用户登录	总部-管理员-管理员		2012-02-21 13:25:42.906	localhost	127.0.0.1	管理员	管理员	总部
2173	用户登录	用户登录	总部-管理员-管理员		2012-02-21 17:01:18.906	localhost	127.0.0.1	管理员	管理员	总部
2211	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:26:44.067	localhost	127.0.0.1	管理员	管理员	总部
2212	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:27:20.583	localhost	127.0.0.1	管理员	管理员	总部
2216	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:38:26.895	localhost	127.0.0.1	管理员	管理员	总部
2221	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:47:27.255	localhost	127.0.0.1	管理员	管理员	总部
2233	用户登录	用户登录	总部-管理员-管理员		2012-02-22 16:18:21.005	localhost	127.0.0.1	管理员	管理员	总部
2270	用户登录	用户登录	河北-会计-雅各布(D-伊米)		2012-02-24 17:54:01.046	localhost	127.0.0.1	雅各布(D-伊米)	会计	河北
2277	用户登录	用户登录	河北-会计-雅各布(D-伊米)		2012-02-24 18:12:39.968	localhost	127.0.0.1	雅各布(D-伊米)	会计	河北
2298	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-27 17:56:30.437	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2326	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 13:32:53.625	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2467	模块分配操作	保存	模块操作关系ID:1;2;3;4;5;6;7;87;155;10;11;12;13;14;15;16;17;18;19;	13;opt	2012-03-06 15:55:32.375	localhost	127.0.0.1	管理员	管理员	总部
2489	用户登录	用户登录	五道口店-店员-王旭		2012-03-06 16:28:34.078	localhost	127.0.0.1	王旭	店员	五道口店
2491	用户代理	保存	34;17;19;	34	2012-03-06 16:31:12.984	localhost	127.0.0.1	张丽	店长	五道口店
2493	用户登录	用户登录	五道口店-店员-王旭		2012-03-06 16:32:05.109	localhost	127.0.0.1	王旭	店员	五道口店
2503	用户登录	用户登录	五道口店-五道口店-店员(默认)-王旭		2012-03-06 16:41:07.765	localhost	127.0.0.1	王旭	五道口店-店员(默认)	五道口店
2507	系统附件	删除	删除附件ID：9		2012-03-06 16:44:15.156	localhost	127.0.0.1	管理员	管理员	总部
1784	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:18:26.625	localhost	127.0.0.1	管理员	管理员	总部
1944	用户登录	用户登录	总部-管理员-管理员		2012-02-16 16:22:05.734	localhost	127.0.0.1	管理员	管理员	总部
1946	用户登录	用户登录	总部-管理员-管理员		2012-02-16 16:37:08.375	localhost	127.0.0.1	管理员	管理员	总部
1950	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:14:02.89	localhost	127.0.0.1	管理员	管理员	总部
1952	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:16:12.968	localhost	127.0.0.1	管理员	管理员	总部
1965	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:51:06.859	localhost	127.0.0.1	管理员	管理员	总部
1990	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:17:14.437	localhost	127.0.0.1	管理员	管理员	总部
1995	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:22:16.796	localhost	127.0.0.1	管理员	管理员	总部
2000	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:30:09.89	localhost	127.0.0.1	管理员	管理员	总部
2002	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:41:09.359	localhost	127.0.0.1	管理员	管理员	总部
2060	用户登录	用户登录	总部-管理员-管理员		2012-02-20 16:39:30.14	localhost	127.0.0.1	管理员	管理员	总部
2133	用户登录	用户登录	总部-管理员-管理员		2012-02-21 13:25:57.312	localhost	127.0.0.1	管理员	管理员	总部
2134	用户登录	用户登录	总部-管理员-管理员		2012-02-21 13:29:01.078	localhost	127.0.0.1	管理员	管理员	总部
2135	用户登录	用户登录	总部-管理员-管理员		2012-02-21 13:29:56.906	localhost	127.0.0.1	管理员	管理员	总部
2174	用户代理	保存	17;0;12;null;	17	2012-02-21 17:02:02.546	localhost	127.0.0.1	管理员	管理员	总部
2213	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:28:26.864	localhost	127.0.0.1	管理员	管理员	总部
2218	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:41:32.255	localhost	127.0.0.1	管理员	管理员	总部
2232	用户登录	用户登录	总部-管理员-管理员		2012-02-22 16:17:25.958	localhost	127.0.0.1	管理员	管理员	总部
2236	用户登录	用户登录	总部-管理员-管理员		2012-02-22 17:36:00.223	localhost	127.0.0.1	管理员	管理员	总部
2271	用户登录	用户登录	河北-会计-伊米		2012-02-24 17:59:27.812	localhost	127.0.0.1	伊米	会计	河北
2299	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-27 17:56:54.703	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2327	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 13:33:52.171	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2333	用户登录	用户登录	总部-管理员-管理员		2012-02-28 13:42:27.421	localhost	127.0.0.1	管理员	管理员	总部
2342	用户登录	用户登录	总部-管理员-管理员		2012-02-28 14:40:55.718	localhost	127.0.0.1	管理员	管理员	总部
2353	用户管理	修改	jacob;雅各布;null;	12	2012-02-28 15:22:08.562	localhost	127.0.0.1	管理员	管理员	总部
2468	用户登录	用户登录	总部-管理员-管理员		2012-03-06 16:02:31.781	localhost	127.0.0.1	管理员	管理员	总部
2470	模块分配操作	保存	模块操作关系ID:5;6;7;87;155;	18;opt	2012-03-06 16:03:29.015	localhost	127.0.0.1	管理员	管理员	总部
2481	用户登录	用户登录	五道口店-店员-王旭		2012-03-06 16:22:13.671	localhost	127.0.0.1	王旭	店员	五道口店
2504	用户登录	用户登录	五道口店-店长-张丽(王旭D)		2012-03-06 16:41:41.578	localhost	127.0.0.1	张丽(王旭D)	店长	五道口店
2505	用户登录	用户登录	五道口店-五道口店-店员(默认)-王旭		2012-03-06 16:42:17.109	localhost	127.0.0.1	王旭	五道口店-店员(默认)	五道口店
1785	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:19:50.921	localhost	127.0.0.1	管理员	管理员	总部
1788	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:46:40.937	localhost	127.0.0.1	管理员	管理员	总部
1945	用户登录	用户登录	总部-管理员-管理员		2012-02-16 16:35:55.656	localhost	127.0.0.1	管理员	管理员	总部
1949	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:13:16	localhost	127.0.0.1	管理员	管理员	总部
1964	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:48:20.203	localhost	127.0.0.1	管理员	管理员	总部
1991	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:17:45.64	localhost	127.0.0.1	管理员	管理员	总部
1996	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:26:05.765	localhost	127.0.0.1	管理员	管理员	总部
2001	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:31:29.062	localhost	127.0.0.1	管理员	管理员	总部
2062	用户登录	用户登录	总部-管理员-管理员		2012-02-20 16:43:15.687	localhost	127.0.0.1	管理员	管理员	总部
2136	用户登录	用户登录	总部-管理员-管理员		2012-02-21 14:06:41.593	localhost	127.0.0.1	管理员	管理员	总部
2138	用户登录	用户登录	总部-管理员-管理员		2012-02-21 14:17:05.437	localhost	127.0.0.1	管理员	管理员	总部
2175	用户登录	用户登录	总部-管理员-管理员		2012-02-21 17:41:29.89	localhost	127.0.0.1	管理员	管理员	总部
2215	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:36:35.598	localhost	127.0.0.1	管理员	管理员	总部
2222	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:47:42.942	localhost	127.0.0.1	管理员	管理员	总部
2230	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:59:52.395	localhost	127.0.0.1	管理员	管理员	总部
2273	用户登录	用户登录	总部-管理员-伊米(D-管理员)		2012-02-24 18:02:32.75	localhost	127.0.0.1	伊米(D-管理员)	管理员	总部
2276	用户登录	用户登录	总部-管理员-伊米(D-管理员)		2012-02-24 18:10:05	localhost	127.0.0.1	伊米(D-管理员)	管理员	总部
2329	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 13:39:34.25	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2337	用户登录	用户登录	北京-北京-会计-雅各布		2012-02-28 13:45:52.781	localhost	127.0.0.1	雅各布	北京-会计	北京
2344	用户登录	用户登录	总部-管理员-管理员		2012-02-28 14:43:59	localhost	127.0.0.1	管理员	管理员	总部
2351	用户登录	用户登录	总部-管理员-管理员		2012-02-28 15:01:32.187	localhost	127.0.0.1	管理员	管理员	总部
2469	模块分配操作	保存	模块操作关系ID:5;6;7;87;155;10;11;12;13;14;60;	17;opt	2012-03-06 16:03:08.781	localhost	127.0.0.1	管理员	管理员	总部
2476	用户登录	用户登录	运营部-运营经理-于志平		2012-03-06 16:20:29.109	localhost	127.0.0.1	于志平	运营经理	运营部
2483	用户代理	保存	33;19;12;	33	2012-03-06 16:23:04.156	localhost	127.0.0.1	王旭	店员	五道口店
2488	用户登录	用户登录	五道口店-店长-张丽		2012-03-06 16:26:03.609	localhost	127.0.0.1	张丽	店长	五道口店
1786	用户登录	用户登录	总部-管理员-管理员		2012-02-14 11:24:22.156	localhost	127.0.0.1	管理员	管理员	总部
1789	用户登录	用户登录	总部-管理员-管理员		2012-02-14 13:42:46.812	localhost	127.0.0.1	管理员	管理员	总部
1790	用户登录	用户登录	总部-管理员-管理员		2012-02-14 13:44:20.515	localhost	127.0.0.1	管理员	管理员	总部
1791	用户登录	用户登录	总部-管理员-管理员		2012-02-14 13:46:08.656	localhost	127.0.0.1	管理员	管理员	总部
1792	用户登录	用户登录	总部-管理员-管理员		2012-02-14 13:48:03.281	localhost	127.0.0.1	管理员	管理员	总部
1793	用户登录	用户登录	总部-管理员-管理员		2012-02-14 13:49:28.703	localhost	127.0.0.1	管理员	管理员	总部
1794	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:09:17.968	localhost	127.0.0.1	管理员	管理员	总部
1795	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:13:04.5	localhost	127.0.0.1	管理员	管理员	总部
1796	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:16:37.593	localhost	127.0.0.1	管理员	管理员	总部
1797	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:21:42.718	localhost	127.0.0.1	管理员	管理员	总部
1798	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:23:39.968	localhost	127.0.0.1	管理员	管理员	总部
1799	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:24:34.781	localhost	127.0.0.1	管理员	管理员	总部
1800	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:26:14.968	localhost	127.0.0.1	管理员	管理员	总部
1801	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:28:35.359	localhost	127.0.0.1	管理员	管理员	总部
1802	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:30:06.171	localhost	127.0.0.1	管理员	管理员	总部
1803	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:39:45.593	localhost	127.0.0.1	管理员	管理员	总部
1804	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:43:32.5	localhost	127.0.0.1	管理员	管理员	总部
1805	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:44:51.281	localhost	127.0.0.1	管理员	管理员	总部
1806	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:50:59.906	localhost	127.0.0.1	管理员	管理员	总部
1807	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:51:48.203	localhost	127.0.0.1	管理员	管理员	总部
1808	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:52:38.359	localhost	127.0.0.1	管理员	管理员	总部
1809	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:55:44.718	localhost	127.0.0.1	管理员	管理员	总部
1810	用户登录	用户登录	总部-管理员-管理员		2012-02-14 14:58:30.375	localhost	127.0.0.1	管理员	管理员	总部
1811	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:01:51.578	localhost	127.0.0.1	管理员	管理员	总部
1812	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:04:07.515	localhost	127.0.0.1	管理员	管理员	总部
1813	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:06:49.75	localhost	127.0.0.1	管理员	管理员	总部
1814	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:09:16.593	localhost	127.0.0.1	管理员	管理员	总部
1815	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:12:21.812	localhost	127.0.0.1	管理员	管理员	总部
1816	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:13:36.234	localhost	127.0.0.1	管理员	管理员	总部
1817	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:14:41.125	localhost	127.0.0.1	管理员	管理员	总部
1818	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:16:49.984	localhost	127.0.0.1	管理员	管理员	总部
1819	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:25:35.265	localhost	127.0.0.1	管理员	管理员	总部
1820	模块分配操作	保存	模块操作关系ID:91;152;	12;opt	2012-02-14 15:34:21.421	localhost	127.0.0.1	管理员	管理员	总部
1821	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:53:41.484	localhost	127.0.0.1	管理员	管理员	总部
1822	用户登录	用户登录	总部-管理员-管理员		2012-02-14 15:56:24.078	localhost	127.0.0.1	管理员	管理员	总部
1823	用户登录	用户登录	总部-管理员-管理员		2012-02-14 16:01:10.265	localhost	127.0.0.1	管理员	管理员	总部
1824	用户登录	用户登录	总部-管理员-管理员		2012-02-14 16:03:31.671	localhost	127.0.0.1	管理员	管理员	总部
1825	模块分配操作	保存	模块操作关系ID:1;2;3;	0;row	2012-02-14 16:05:52.234	localhost	127.0.0.1	管理员	管理员	总部
1826	用户登录	用户登录	总部-管理员-管理员		2012-02-14 16:06:28.593	localhost	127.0.0.1	管理员	管理员	总部
1827	模块分配操作	保存	模块操作关系ID:1;2;3;	12;row	2012-02-14 16:06:57.703	localhost	127.0.0.1	管理员	管理员	总部
1828	模块分配操作	保存	模块操作关系ID:26;11;1;	12;row	2012-02-14 16:07:36.937	localhost	127.0.0.1	管理员	管理员	总部
1829	用户登录	用户登录	总部-管理员-管理员		2012-02-15 09:57:00.984	localhost	127.0.0.1	管理员	管理员	总部
1830	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:07:44.14	localhost	127.0.0.1	管理员	管理员	总部
1831	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:10:30.015	localhost	127.0.0.1	管理员	管理员	总部
1832	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:11:36.171	localhost	127.0.0.1	管理员	管理员	总部
1833	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:14:28.031	localhost	127.0.0.1	管理员	管理员	总部
1834	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:16:00.046	localhost	127.0.0.1	管理员	管理员	总部
1835	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:17:02.25	localhost	127.0.0.1	管理员	管理员	总部
1836	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:22:59	localhost	127.0.0.1	管理员	管理员	总部
1837	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:25:20.515	localhost	127.0.0.1	管理员	管理员	总部
1838	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:26:32.187	localhost	127.0.0.1	管理员	管理员	总部
1839	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:28:49.609	localhost	127.0.0.1	管理员	管理员	总部
1840	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:46:26.375	localhost	127.0.0.1	管理员	管理员	总部
1841	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:49:35.062	localhost	127.0.0.1	管理员	管理员	总部
1901	用户登录	用户登录	河北-经理-jacob		2012-02-15 16:44:37.734	localhost	127.0.0.1	jacob	经理	河北
1842	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:51:20.046	localhost	127.0.0.1	管理员	管理员	总部
1947	用户登录	用户登录	总部-管理员-管理员		2012-02-16 16:47:08.828	localhost	127.0.0.1	管理员	管理员	总部
1963	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:45:15.828	localhost	127.0.0.1	管理员	管理员	总部
1992	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:18:15.515	localhost	127.0.0.1	管理员	管理员	总部
1997	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:28:02.265	localhost	127.0.0.1	管理员	管理员	总部
2063	用户管理	修改	jacob_liang;伊米;null;	13	2012-02-20 16:52:12.781	localhost	127.0.0.1	管理员	管理员	总部
2137	用户登录	用户登录	总部-管理员-管理员		2012-02-21 14:16:13.875	localhost	127.0.0.1	管理员	管理员	总部
2176	用户代理	保存	18;0;12;null;	18	2012-02-21 17:42:20.156	localhost	127.0.0.1	管理员	管理员	总部
2217	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:41:32.239	localhost	127.0.0.1	管理员	管理员	总部
2223	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:48:07.52	localhost	127.0.0.1	管理员	管理员	总部
2229	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:57:24.098	localhost	127.0.0.1	管理员	管理员	总部
2234	用户登录	用户登录	总部-管理员-管理员		2012-02-22 16:18:47.02	localhost	127.0.0.1	管理员	管理员	总部
2238	用户登录	用户登录	总部-管理员-管理员		2012-02-22 17:43:44.411	localhost	127.0.0.1	管理员	管理员	总部
2275	用户代理	保存	26;0;13;	26	2012-02-24 18:09:21.734	localhost	127.0.0.1	管理员	管理员	总部
2330	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 13:39:58.906	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2339	用户登录	用户登录	总部-管理员-管理员		2012-02-28 14:34:21.812	localhost	127.0.0.1	管理员	管理员	总部
2341	用户登录	用户登录	总部-管理员-管理员		2012-02-28 14:35:38.125	localhost	127.0.0.1	管理员	管理员	总部
2347	系统模块管理	修改	用户信息管理;userMgr;	34	2012-02-28 14:50:33.593	localhost	127.0.0.1	管理员	管理员	总部
2471	模块分配操作	保存	模块操作关系ID:10;11;12;13;14;	19;opt	2012-03-06 16:03:45.39	localhost	127.0.0.1	管理员	管理员	总部
2477	用户登录	用户登录	北京运营-运营经理-张世鹏		2012-03-06 16:20:48.781	localhost	127.0.0.1	张世鹏	运营经理	北京运营
2490	用户登录	用户登录	五道口店-店长-张丽		2012-03-06 16:29:33.031	localhost	127.0.0.1	张丽	店长	五道口店
2499	用户登录	用户登录	五道口店-店长-张丽(王旭D)		2012-03-06 16:34:28.828	localhost	127.0.0.1	张丽(王旭D)	店长	五道口店
2502	用户登录	用户登录	五道口店-五道口店-收银-王旭		2012-03-06 16:40:53.75	localhost	127.0.0.1	王旭	五道口店-收银	五道口店
2508	系统附件	保存	附件业务表ID：6;附件名称:tbTnr1_1_jacbo.jpg	10	2012-03-06 16:44:21.859	localhost	127.0.0.1	管理员	管理员	总部
1843	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:51:49.046	localhost	127.0.0.1	管理员	管理员	总部
1844	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:52:25.296	localhost	127.0.0.1	管理员	管理员	总部
1847	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:59:56.546	localhost	127.0.0.1	管理员	管理员	总部
1948	用户登录	用户登录	总部-管理员-管理员		2012-02-16 16:49:09.14	localhost	127.0.0.1	管理员	管理员	总部
1954	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:18:34.765	localhost	127.0.0.1	管理员	管理员	总部
1993	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:18:52.531	localhost	127.0.0.1	管理员	管理员	总部
1998	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:29:00.156	localhost	127.0.0.1	管理员	管理员	总部
2064	用户登录	用户登录	总部-管理员-管理员		2012-02-20 17:09:59.281	localhost	127.0.0.1	管理员	管理员	总部
2067	用户代理	保存	2;0;12;null;	2	2012-02-20 17:22:35.031	localhost	127.0.0.1	管理员	管理员	总部
2139	用户登录	用户登录	总部-管理员-管理员		2012-02-21 14:33:56.156	localhost	127.0.0.1	管理员	管理员	总部
2220	用户登录	用户登录	总部-管理员-管理员		2012-02-22 15:46:45.989	localhost	127.0.0.1	管理员	管理员	总部
2231	用户登录	用户登录	总部-管理员-管理员		2012-02-22 16:02:28.02	localhost	127.0.0.1	管理员	管理员	总部
2235	用户登录	用户登录	总部-管理员-管理员		2012-02-22 17:35:35.614	localhost	127.0.0.1	管理员	管理员	总部
2331	用户登录	用户登录	总部-管理员-管理员		2012-02-28 13:40:19.234	localhost	127.0.0.1	管理员	管理员	总部
2334	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 13:42:37.109	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2350	机构管理	修改	机构名称：山西;机构代码：;上级机构：总部;	3	2012-02-28 15:00:02.687	localhost	127.0.0.1	管理员	管理员	总部
2475	用户登录	用户登录	总经办-副总经理-杨文菊		2012-03-06 16:20:04.843	localhost	127.0.0.1	杨文菊	副总经理	总经办
2479	用户登录	用户登录	五道口店-店长-张丽		2012-03-06 16:21:42.718	localhost	127.0.0.1	张丽	店长	五道口店
2480	用户登录	用户登录	五道口店-收银-高晓峰		2012-03-06 16:21:58.328	localhost	127.0.0.1	高晓峰	收银	五道口店
2500	用户登录	用户登录	总部-管理员-管理员		2012-03-06 16:39:53.281	localhost	127.0.0.1	管理员	管理员	总部
1845	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:54:16.984	localhost	127.0.0.1	管理员	管理员	总部
1951	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:14:27.062	localhost	127.0.0.1	管理员	管理员	总部
1953	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:16:56.312	localhost	127.0.0.1	管理员	管理员	总部
1994	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:19:52.5	localhost	127.0.0.1	管理员	管理员	总部
1999	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:29:25.234	localhost	127.0.0.1	管理员	管理员	总部
2065	用户登录	用户登录	总部-管理员-管理员		2012-02-20 17:20:08.921	localhost	127.0.0.1	管理员	管理员	总部
2140	用户登录	用户登录	总部-管理员-管理员		2012-02-21 14:56:15.937	localhost	127.0.0.1	管理员	管理员	总部
2237	用户登录	用户登录	总部-管理员-管理员		2012-02-22 17:36:05.583	localhost	127.0.0.1	管理员	管理员	总部
2332	用户登录	用户登录	总部-管理员-管理员		2012-02-28 13:42:05.359	localhost	127.0.0.1	管理员	管理员	总部
2336	用户登录	用户登录	河北-会计-伊米		2012-02-28 13:45:17.296	localhost	127.0.0.1	伊米	会计	河北
2338	用户登录	用户登录	总部-管理员-管理员		2012-02-28 14:14:27.515	localhost	127.0.0.1	管理员	管理员	总部
2346	用户登录	用户登录	总部-管理员-管理员		2012-02-28 14:50:07.437	localhost	127.0.0.1	管理员	管理员	总部
2348	用户登录	用户登录	总部-管理员-管理员		2012-02-28 14:58:27.046	localhost	127.0.0.1	管理员	管理员	总部
2482	用户登录	用户登录	五道口店-店员-王旭		2012-03-06 16:22:35.703	localhost	127.0.0.1	王旭	店员	五道口店
2497	用户登录	用户登录	五道口店-收银-王旭		2012-03-06 16:33:51.718	localhost	127.0.0.1	王旭	收银	五道口店
2498	用户登录	用户登录	五道口店-店员-王旭		2012-03-06 16:34:03.64	localhost	127.0.0.1	王旭	店员	五道口店
2506	用户登录	用户登录	总部-管理员-管理员		2012-03-06 16:42:29.437	localhost	127.0.0.1	管理员	管理员	总部
1846	用户登录	用户登录	总部-管理员-管理员		2012-02-15 10:59:24.937	localhost	127.0.0.1	管理员	管理员	总部
1850	用户登录	用户登录	总部-管理员-管理员		2012-02-15 11:04:14.031	localhost	127.0.0.1	管理员	管理员	总部
1955	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:20:16.734	localhost	127.0.0.1	管理员	管理员	总部
1956	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:27:50.5	localhost	127.0.0.1	管理员	管理员	总部
2004	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:41:58.843	localhost	127.0.0.1	管理员	管理员	总部
2066	用户代理	保存	1;0;12;null;	1	2012-02-20 17:20:28.218	localhost	127.0.0.1	管理员	管理员	总部
2141	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:01:36.062	localhost	127.0.0.1	管理员	管理员	总部
2144	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:12:09.031	localhost	127.0.0.1	管理员	管理员	总部
2335	用户登录	用户登录	河北-河北-经理(默认)-雅各布		2012-02-28 13:45:00.015	localhost	127.0.0.1	雅各布	河北-经理(默认)	河北
2340	用户登录	用户登录	总部-管理员-管理员		2012-02-28 14:35:13.406	localhost	127.0.0.1	管理员	管理员	总部
2352	用户登录	用户登录	总部-管理员-管理员		2012-02-28 15:20:58.687	localhost	127.0.0.1	管理员	管理员	总部
2484	用户登录	用户登录	董事会-董事长-王娟		2012-03-06 16:23:53.093	localhost	127.0.0.1	王娟	董事长	董事会
2501	模块分配操作	保存	模块操作关系ID:44;	19;opt	2012-03-06 16:40:24.328	localhost	127.0.0.1	管理员	管理员	总部
2509	系统公告管理	修改	修改公告ID：6;修改公告标题：jacob_liang第二版本发布了！;有效期：Mon May 01 00:00:00 CST 2017;是否发布：0;	6	2012-03-06 16:44:21.906	localhost	127.0.0.1	管理员	管理员	总部
1848	用户登录	用户登录	总部-管理员-管理员		2012-02-15 11:00:47.531	localhost	127.0.0.1	管理员	管理员	总部
1957	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:28:56.375	localhost	127.0.0.1	管理员	管理员	总部
1958	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:34:03.546	localhost	127.0.0.1	管理员	管理员	总部
2005	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:42:34.468	localhost	127.0.0.1	管理员	管理员	总部
2006	用户登录	用户登录	总部-管理员-管理员		2012-02-17 11:48:40.5	localhost	127.0.0.1	管理员	管理员	总部
2142	用户登录	用户登录	河北-经理-雅各布		2012-02-21 15:08:17.921	localhost	127.0.0.1	雅各布	经理	河北
2349	机构管理	保存	机构名称：海淀;机构代码：;上级机构：北京;	5	2012-02-28 14:58:56.515	localhost	127.0.0.1	管理员	管理员	总部
2356	机构管理	保存	机构名称：五道口;机构代码：;上级机构：海淀;	6	2012-02-28 15:40:17.64	localhost	127.0.0.1	管理员	管理员	总部
2485	用户登录	用户登录	五道口店-收银-高晓峰		2012-03-06 16:24:12.156	localhost	127.0.0.1	高晓峰	收银	五道口店
1849	用户登录	用户登录	总部-管理员-管理员		2012-02-15 11:02:09.015	localhost	127.0.0.1	管理员	管理员	总部
1851	用户登录	用户登录	总部-管理员-管理员		2012-02-15 13:56:12.328	localhost	127.0.0.1	管理员	管理员	总部
1852	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:02:47.734	localhost	127.0.0.1	管理员	管理员	总部
1853	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:04:20.687	localhost	127.0.0.1	管理员	管理员	总部
1854	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:27:17.093	localhost	127.0.0.1	管理员	管理员	总部
1855	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:27:47.265	localhost	127.0.0.1	管理员	管理员	总部
1856	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:28:09.953	localhost	127.0.0.1	管理员	管理员	总部
1857	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:28:54.359	localhost	127.0.0.1	管理员	管理员	总部
1858	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:29:25.859	localhost	127.0.0.1	管理员	管理员	总部
1859	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:30:08.156	localhost	127.0.0.1	管理员	管理员	总部
1860	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:30:23.093	localhost	127.0.0.1	管理员	管理员	总部
1861	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:30:59.312	localhost	127.0.0.1	管理员	管理员	总部
1862	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:33:03.421	localhost	127.0.0.1	管理员	管理员	总部
1863	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:34:56.89	localhost	127.0.0.1	管理员	管理员	总部
1864	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:42:44.796	localhost	127.0.0.1	管理员	管理员	总部
1865	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:43:18.781	localhost	127.0.0.1	管理员	管理员	总部
1866	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:46:23.406	localhost	127.0.0.1	管理员	管理员	总部
1867	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:48:48.359	localhost	127.0.0.1	管理员	管理员	总部
1868	用户登录	用户登录	总部-管理员-管理员		2012-02-15 14:54:23.546	localhost	127.0.0.1	管理员	管理员	总部
1869	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:25:32.343	localhost	127.0.0.1	管理员	管理员	总部
1870	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:27:37.265	localhost	127.0.0.1	管理员	管理员	总部
1871	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:28:43.609	localhost	127.0.0.1	管理员	管理员	总部
1872	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:29:19.953	localhost	127.0.0.1	管理员	管理员	总部
1873	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:31:40.687	localhost	127.0.0.1	管理员	管理员	总部
1874	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:34:11.156	localhost	127.0.0.1	管理员	管理员	总部
1875	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:41:20.453	localhost	127.0.0.1	管理员	管理员	总部
1876	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:43:24.89	localhost	127.0.0.1	管理员	管理员	总部
1877	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:45:23.796	localhost	127.0.0.1	管理员	管理员	总部
1878	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:48:13.125	localhost	127.0.0.1	管理员	管理员	总部
1879	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:50:48.468	localhost	127.0.0.1	管理员	管理员	总部
1880	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:52:54.421	localhost	127.0.0.1	管理员	管理员	总部
1881	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:54:22.921	localhost	127.0.0.1	管理员	管理员	总部
1882	用户登录	用户登录	总部-管理员-管理员		2012-02-15 15:56:22.078	localhost	127.0.0.1	管理员	管理员	总部
1883	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:00:35.296	localhost	127.0.0.1	管理员	管理员	总部
1884	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:02:51.296	localhost	127.0.0.1	管理员	管理员	总部
1885	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:04:23.109	localhost	127.0.0.1	管理员	管理员	总部
1886	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:10:19.218	localhost	127.0.0.1	管理员	管理员	总部
1887	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:16:05.968	localhost	127.0.0.1	管理员	管理员	总部
1888	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:20:40.453	localhost	127.0.0.1	管理员	管理员	总部
1889	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:25:23.078	localhost	127.0.0.1	管理员	管理员	总部
1890	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:26:47.843	localhost	127.0.0.1	管理员	管理员	总部
1891	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:32:22.312	localhost	127.0.0.1	管理员	管理员	总部
1892	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:34:26.203	localhost	127.0.0.1	管理员	管理员	总部
1893	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:35:39.296	localhost	127.0.0.1	管理员	管理员	总部
1894	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:38:27.734	localhost	127.0.0.1	管理员	管理员	总部
1895	模块分配操作	保存	模块操作关系ID:4;	12;row	2012-02-15 16:38:58.328	localhost	127.0.0.1	管理员	管理员	总部
1896	模块分配操作	保存	模块操作关系ID:39;	12;row	2012-02-15 16:39:45.859	localhost	127.0.0.1	管理员	管理员	总部
1897	模块分配操作	保存	模块操作关系ID:12;13;	12;row	2012-02-15 16:40:12.875	localhost	127.0.0.1	管理员	管理员	总部
1898	模块分配操作	保存	模块操作关系ID:58;59;60;61;62;91;97;152;	12;opt	2012-02-15 16:42:31.703	localhost	127.0.0.1	管理员	管理员	总部
1899	用户登录	用户登录	河北-经理-jacob		2012-02-15 16:43:15.531	localhost	127.0.0.1	jacob	经理	河北
1900	模块分配操作	保存	模块操作关系ID:30;39;	12;row	2012-02-15 16:44:13.265	localhost	127.0.0.1	jacob	经理	河北
1902	用户登录	用户登录	河北-经理-jacob		2012-02-15 16:45:18.062	localhost	127.0.0.1	jacob	经理	河北
1903	模块分配操作	保存	模块操作关系ID:1;2;3;	12;row	2012-02-15 16:46:17.125	localhost	127.0.0.1	jacob	经理	河北
1904	用户登录	用户登录	河北-经理-jacob		2012-02-15 16:46:41.39	localhost	127.0.0.1	jacob	经理	河北
1905	用户登录	用户登录	河北-经理-jacob		2012-02-15 16:47:21.281	localhost	127.0.0.1	jacob	经理	河北
1907	用户登录	用户登录	总部-管理员-管理员		2012-02-15 16:48:49.484	localhost	127.0.0.1	管理员	管理员	总部
1959	用户登录	用户登录	总部-管理员-管理员		2012-02-16 17:34:50.89	localhost	127.0.0.1	管理员	管理员	总部
2009	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:34:24.203	localhost	127.0.0.1	管理员	管理员	总部
2024	用户登录	用户登录	总部-管理员-管理员		2012-02-17 14:22:36.421	localhost	127.0.0.1	管理员	管理员	总部
2143	用户代理	保存	5;0;12;null;	5	2012-02-21 15:11:56.453	localhost	127.0.0.1	雅各布	经理	河北
2357	用户登录	用户登录	河北-会计-伊米		2012-02-28 16:01:36.984	localhost	127.0.0.1	伊米	会计	河北
2362	用户分配角色	保存	39	14	2012-02-28 16:08:56.39	localhost	127.0.0.1	管理员	管理员	总部
2363	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:09:30.937	localhost	127.0.0.1	挑大梁	经理	北京
1908	用户登录	用户登录	河北-经理-jacob		2012-02-15 16:50:40.656	localhost	127.0.0.1	jacob	经理	河北
1909	用户登录	用户登录	河北-经理-jacob		2012-02-15 17:28:48.25	localhost	127.0.0.1	jacob	经理	河北
1910	模块分配操作	保存	模块操作关系ID:rol;	3;row	2012-02-15 17:30:27.203	localhost	127.0.0.1	jacob	经理	河北
1911	用户登录	用户登录	河北-经理-jacob		2012-02-15 17:31:48.468	localhost	127.0.0.1	jacob	经理	河北
1912	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-15 17:33:31.546	localhost	127.0.0.1	jacob	经理	河北
1913	模块分配操作	保存	模块操作关系ID:usr;	3;row	2012-02-15 17:33:44.75	localhost	127.0.0.1	jacob	经理	河北
1914	用户登录	用户登录	河北-经理-jacob		2012-02-15 17:34:03.015	localhost	127.0.0.1	jacob	经理	河北
1915	用户登录	用户登录	总部-管理员-管理员		2012-02-15 17:34:30.843	localhost	127.0.0.1	管理员	管理员	总部
1916	模块分配操作	保存	模块操作关系ID:org;	3;row	2012-02-15 17:34:51.406	localhost	127.0.0.1	管理员	管理员	总部
1917	模块分配操作	保存	模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;91;97;152;65;66;68;69;116;117;118;119;112;113;114;115;	3;opt	2012-02-15 17:34:59.156	localhost	127.0.0.1	管理员	管理员	总部
1918	用户登录	用户登录	河北-经理-jacob		2012-02-15 17:35:16.921	localhost	127.0.0.1	jacob	经理	河北
1919	模块分配操作	保存	模块操作关系ID:58;59;60;61;62;91;97;152;	12;opt	2012-02-15 17:37:44.937	localhost	127.0.0.1	jacob	经理	河北
1920	用户登录	用户登录	河北-经理-jacob		2012-02-15 17:38:54.39	localhost	127.0.0.1	jacob	经理	河北
1921	模块分配操作	保存	模块操作关系ID:26;11;1;	12;row	2012-02-15 17:39:33.078	localhost	127.0.0.1	jacob	经理	河北
1922	用户登录	用户登录	河北-经理-jacob		2012-02-15 17:40:02.718	localhost	127.0.0.1	jacob	经理	河北
1923	用户登录	用户登录	河北-经理-jacob		2012-02-15 17:47:24.093	localhost	127.0.0.1	jacob	经理	河北
2010	用户登录	用户登录	总部-管理员-管理员		2012-02-17 13:50:20.14	localhost	127.0.0.1	管理员	管理员	总部
2145	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:25:18.125	localhost	127.0.0.1	管理员	管理员	总部
2147	用户登录	用户登录	河北-经理-雅各布		2012-02-21 15:29:13.125	localhost	127.0.0.1	雅各布	经理	河北
2155	用户代理	保存	9;0;null;null;	9	2012-02-21 15:46:01.328	localhost	127.0.0.1	管理员	管理员	总部
2160	用户登录	用户登录	总部-管理员-管理员		2012-02-21 15:52:43.859	localhost	127.0.0.1	管理员	管理员	总部
2358	用户登录	用户登录	总部-管理员-管理员		2012-02-28 16:02:13.718	localhost	127.0.0.1	管理员	管理员	总部
2361	用户登录	用户登录	总部-管理员-管理员		2012-02-28 16:07:24.265	localhost	127.0.0.1	管理员	管理员	总部
2367	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:14:39.796	localhost	127.0.0.1	挑大梁	经理	北京
2369	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:15:55.546	localhost	127.0.0.1	挑大梁	经理	北京
2371	用户登录	用户登录	北京-经理-挑大梁		2012-02-28 16:16:29.234	localhost	127.0.0.1	挑大梁	经理	北京
2533	用户登录	用户登录	总部-管理员-管理员		2014-09-27 13:57:03.611	localhost	127.0.0.1	管理员	管理员	总部
2534	用户登录	用户登录	总部-管理员-管理员		2014-09-27 14:01:37.069	localhost	127.0.0.1	管理员	管理员	总部
2535	系统模块管理	修改	页面功能区管理１;mainFrameFunMgr;	4	2014-10-03 15:49:39.633	localhost	127.0.0.1	管理员	管理员	总部
2536	系统模块管理	修改	页面功能区管理;mainFrameFunMgr;	4	2014-10-03 15:49:50.693	localhost	127.0.0.1	管理员	管理员	总部
2537	模块分配操作	保存	模块分配操作;3	7	2014-10-03 16:18:58.733	localhost	127.0.0.1	管理员	管理员	总部
2538	系统模块管理	修改	模块菜单管理;module/moduleMgr;	7	2014-10-11 21:04:36.198	localhost	127.0.0.1	管理员	管理员	总部
2539	系统模块管理	修改	模块菜单管理;module/moduleMgr;	7	2014-10-11 21:05:08.593	localhost	127.0.0.1	管理员	管理员	总部
2540	系统模块管理	修改	已办任务;finishedMgr;	63	2014-10-12 15:26:01.248	localhost	127.0.0.1	管理员	管理员	总部
2541	系统模块管理	修改	模块菜单管理;moduleMgr;	7	2014-10-12 16:18:43.248	localhost	127.0.0.1	管理员	管理员	总部
2542	系统模块管理	修改	系统配置管理;systemConfigMgr;	25	2014-10-13 21:37:11.8	localhost	127.0.0.1	管理员	管理员	总部
2543	系统配置管理	保存	设置系统名称: rutine权限管理平台;		2014-10-14 14:10:38.442	localhost	127.0.0.1	管理员	管理员	总部
2544	系统配置管理	保存	设置日志级别:1;		2014-10-14 14:10:53.247	localhost	127.0.0.1	管理员	管理员	总部
2545	系统配置管理	保存	设置日志保留天数:30;		2014-10-14 14:10:54.679	localhost	127.0.0.1	管理员	管理员	总部
2546	系统配置管理	保存	增加管理员:zhangj;增加管理员:wangxm;		2014-10-14 14:23:51.789	localhost	127.0.0.1	管理员	管理员	总部
2547	系统配置管理	保存	删除管理员:zhangj;删除管理员:wangxm;		2014-10-14 14:33:40.372	localhost	127.0.0.1	管理员	管理员	总部
2548	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201203:001:zhangsan;	25	2014-10-14 20:28:16.888	localhost	127.0.0.1	管理员	管理员	总部
2549	系统编码管理	停用	编码停用：编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201203:001:zhangsan;	25	2014-10-14 20:28:17.048	localhost	127.0.0.1	管理员	管理员	总部
2550	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201203:001:zhangsan;	25	2014-10-14 20:28:27.566	localhost	127.0.0.1	管理员	管理员	总部
2551	系统编码管理	启用	编码启用：编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201203:001:zhangsan;	25	2014-10-14 20:28:27.569	localhost	127.0.0.1	管理员	管理员	总部
2552	系统模块管理	修改	系统参数管理;systemParameterMgr;	15	2014-10-14 21:18:05.19	localhost	127.0.0.1	管理员	管理员	总部
2553	系统参数管理	修改	参数名称：用户有效期;参数键值：uservalidate;参数值：6;参数类型：uum	2	2014-10-14 21:18:37.489	localhost	127.0.0.1	管理员	管理员	总部
2554	系统参数管理	停用	参数名称：用户有效期;参数键值：uservalidate;参数值：6;参数类型：uum	2	2014-10-14 21:18:37.495	localhost	127.0.0.1	管理员	管理员	总部
2555	系统参数管理	修改	参数名称：用户有效期;参数键值：uservalidate;参数值：6;参数类型：uum	2	2014-10-14 21:18:41.075	localhost	127.0.0.1	管理员	管理员	总部
2556	系统参数管理	启用	参数名称：用户有效期;参数键值：uservalidate;参数值：6;参数类型：uum	2	2014-10-14 21:18:41.077	localhost	127.0.0.1	管理员	管理员	总部
2561	系统附件	保存	附件业务表ID：7;附件名称:Xo7821iOvf13yqsgIYt0C84Cog5b49xL_14088094757641.gif	11	2014-10-17 17:00:20.786	localhost	127.0.0.1	管理员	管理员	总部
2562	系统公告管理	保存	保存公告标题：rutine测试用;有效期限:Fri Oct 24 00:00:00 CST 2014	7	2014-10-17 17:00:20.816	localhost	127.0.0.1	管理员	管理员	总部
2563	系统公告管理	保存	保存公告标题：asdf;有效期限:Thu Oct 02 00:00:00 CST 2014	8	2014-10-17 18:06:09.113	localhost	127.0.0.1	管理员	管理员	总部
2567	系统附件	删除	删除附件ID：[11]		2014-10-17 20:59:32.318	localhost	127.0.0.1	管理员	管理员	总部
2568	删除	系统公告管理	删除的公告ID：[8, 7]		2014-10-17 20:59:32.325	localhost	127.0.0.1	管理员	管理员	总部
2569	系统附件	保存	附件业务表ID：9;附件名称:O169gT8HtqpTDIk3jCgX40r63PhBiJJo_20140629_172256_HDR.jpg	12	2014-10-17 21:08:42.487	localhost	127.0.0.1	管理员	管理员	总部
2570	系统附件	保存	附件业务表ID：9;附件名称:1fBcCrgJ9Efk9ilfMkn1ygWm2b37t75R_20140629_172256_HDR (2).jpg	13	2014-10-17 21:08:42.492	localhost	127.0.0.1	管理员	管理员	总部
2571	系统公告管理	保存	保存公告标题：2.0版本;有效期限:Tue Feb 14 00:00:00 CST 2017	9	2014-10-17 21:08:42.495	localhost	127.0.0.1	管理员	管理员	总部
2572	系统附件	删除	删除附件ID：[12]		2014-10-17 21:44:27.595	localhost	127.0.0.1	管理员	管理员	总部
2573	系统附件	删除	删除附件ID：[13]		2014-10-17 21:46:53.473	localhost	127.0.0.1	管理员	管理员	总部
2574	系统附件	保存	附件业务表ID：9;附件名称:Q58Ju1mIHv303b221kqm10LG1cmF0bec_20140629_172256_HDR.jpg	14	2014-10-17 22:46:22.291	localhost	127.0.0.1	管理员	管理员	总部
2575	系统附件	删除	删除附件ID：[14]		2014-10-17 22:46:34.369	localhost	127.0.0.1	管理员	管理员	总部
2576	系统附件	保存	附件业务表ID：9;附件名称:TG7IBzulni0Ngjt4tPy729zunyW1vyx1_20140629_172256_HDR (2).jpg	15	2014-10-17 22:47:43.81	localhost	127.0.0.1	管理员	管理员	总部
2577	系统附件	删除	删除附件ID：[15]		2014-10-17 22:52:10.265	localhost	127.0.0.1	管理员	管理员	总部
2578	系统附件	保存	附件业务表ID：9;附件名称:oPO4bj778zD3fRlKbTVv194MIuoNI24m_20140629_172256_HDR.jpg	16	2014-10-17 22:52:24.498	localhost	127.0.0.1	管理员	管理员	总部
2579	系统附件	删除	删除附件ID：[16]		2014-10-17 22:56:23.576	localhost	127.0.0.1	\N	\N	\N
2580	系统附件	保存	附件业务表ID：9;附件名称:r2ytoEQkDfrsY5I2Y3lM1DLSGwutc4jn_20140629_172256_HDR.jpg	17	2014-10-17 22:56:56.042	localhost	127.0.0.1	管理员	管理员	总部
2581	系统附件	删除	删除附件ID：[17]		2014-10-17 22:56:57.166	localhost	127.0.0.1	管理员	管理员	总部
2582	系统附件	删除	删除附件ID：[17]		2014-10-17 22:56:57.186	localhost	127.0.0.1	管理员	管理员	总部
2583	系统附件	保存	附件业务表ID：9;附件名称:2sYQ4ClnueEil4dULz0qr6k20uKq4vqn_20140629_172256_HDR.jpg	18	2014-10-17 22:57:45.822	localhost	127.0.0.1	管理员	管理员	总部
2584	系统附件	删除	删除附件ID：[18]		2014-10-17 22:57:46.593	localhost	127.0.0.1	管理员	管理员	总部
2585	系统附件	删除	删除附件ID：[18]		2014-10-17 22:57:46.612	localhost	127.0.0.1	管理员	管理员	总部
2586	系统附件	保存	附件业务表ID：9;附件名称:ii58153itiJO1hchnj0iBM1gM1CMFfns_20140629_172256_HDR (2).jpg	19	2014-10-17 22:57:52.059	localhost	127.0.0.1	管理员	管理员	总部
2587	系统调度管理	修改	任务名称：logJob;任务类描述:com.mycuckoo.service.impl.platform.job.LoggerJob;触发器类型:Cron;时间表达式:* * 0 15 * ?;	1	2014-10-18 00:09:36.601	localhost	127.0.0.1	管理员	管理员	总部
2588	系统调度管理	修改	任务名称：logJob;任务类描述:com.mycuckoo.service.impl.platform.job.LoggerJob;触发器类型:Cron;时间表达式:* * 0 15 * ?;	1	2014-10-18 00:19:27.993	localhost	127.0.0.1	管理员	管理员	总部
2589	省市地区管理	修改	海淀;province;	2002	2014-10-18 11:27:32.104	localhost	127.0.0.1	管理员	管理员	总部
2590	省市地区管理	修改	北海;province;	2001	2014-10-18 11:31:03.326	localhost	127.0.0.1	管理员	管理员	总部
2591	省市地区管理	停用	北海;province;	2001	2014-10-18 11:31:03.329	localhost	127.0.0.1	管理员	管理员	总部
2592	省市地区管理	修改	北海;province;	2001	2014-10-18 11:31:06.126	localhost	127.0.0.1	管理员	管理员	总部
2593	省市地区管理	启用	北海;province;	2001	2014-10-18 11:31:06.129	localhost	127.0.0.1	管理员	管理员	总部
2594	省市地区管理	保存	广州;province;	2006	2014-10-18 11:40:29.186	localhost	127.0.0.1	管理员	管理员	总部
2595	模块分配操作	保存	模块分配操作;3	14	2014-10-18 11:47:53.262	localhost	127.0.0.1	管理员	管理员	总部
2596	模块分配操作	保存	模块分配操作;	14	2014-10-18 11:51:10.429	localhost	127.0.0.1	管理员	管理员	总部
2597	系统模块管理	修改	类别字典管理;typeDicMgr;	10	2014-10-18 11:51:46.034	localhost	127.0.0.1	管理员	管理员	总部
2598	系统模块管理	修改	类别字典管理;dictionaryMgr;	10	2014-10-18 11:52:46.751	localhost	127.0.0.1	管理员	管理员	总部
2599	机构管理	修改	机构名称 : 上海储运;机构代码 : ;上级机构 : 储运部;	17	2014-10-18 14:55:36.311	localhost	127.0.0.1	管理员	管理员	总部
2600	机构管理	修改	角色名称 : 董事长;	20	2014-10-18 21:36:22.515	localhost	127.0.0.1	管理员	管理员	总部
2601	模块分配操作	保存	模块操作关系ID: rol;	19;row	2014-10-18 22:20:43.533	localhost	127.0.0.1	管理员	管理员	总部
2602	模块分配操作	保存	模块操作关系ID: org;	19;row	2014-10-18 22:21:48.044	localhost	127.0.0.1	管理员	管理员	总部
2603	模块分配操作	保存	模块操作关系ID: org;	20;row	2014-10-18 22:21:48.057	localhost	127.0.0.1	管理员	管理员	总部
2604	模块分配操作	保存	模块操作关系ID: org;	19;row	2014-10-18 22:21:48.078	localhost	127.0.0.1	管理员	管理员	总部
2605	模块分配操作	保存	模块操作关系ID: org;	19;row	2014-10-18 22:21:48.078	localhost	127.0.0.1	管理员	管理员	总部
2606	模块分配操作	保存	模块操作关系ID: org;	19;row	2014-10-18 22:22:38.703	localhost	127.0.0.1	管理员	管理员	总部
2607	模块分配操作	保存	模块操作关系ID: org;	20;row	2014-10-18 22:22:38.714	localhost	127.0.0.1	管理员	管理员	总部
2608	模块分配操作	保存	模块操作关系ID: org;	19;row	2014-10-18 22:22:38.722	localhost	127.0.0.1	管理员	管理员	总部
2609	模块分配操作	保存	模块操作关系ID: org;	19;row	2014-10-18 22:22:38.742	localhost	127.0.0.1	管理员	管理员	总部
2610	模块分配操作	保存	模块操作关系ID: org;	19;row	2014-10-18 22:22:38.744	localhost	127.0.0.1	管理员	管理员	总部
2611	模块分配操作	保存	模块操作关系ID: urs;	19;row	2014-10-18 22:22:46.511	localhost	127.0.0.1	管理员	管理员	总部
2612	模块分配操作	保存	模块操作关系ID: org;	19;row	2014-10-18 22:22:57.534	localhost	127.0.0.1	管理员	管理员	总部
2613	模块分配操作	保存	模块操作关系ID: org;	19;row	2014-10-18 22:22:57.559	localhost	127.0.0.1	管理员	管理员	总部
2614	模块分配操作	保存	模块操作关系ID: 	19;opt	2014-10-18 22:23:23.453	localhost	127.0.0.1	管理员	管理员	总部
2615	模块分配操作	保存	模块操作关系ID: 	19;opt	2014-10-18 22:23:23.47	localhost	127.0.0.1	管理员	管理员	总部
2616	模块分配操作	保存	模块操作关系ID: 	19;opt	2014-10-18 22:23:23.476	localhost	127.0.0.1	管理员	管理员	总部
2617	角色分配	保存	机构id:1;角色id:1;机构id:1;角色id:5;		2014-10-19 09:03:24.669	localhost	127.0.0.1	管理员	管理员	总部
2618	角色分配	删除	删除机构下的角色ID：5;1;	1	2014-10-19 09:03:56.97	localhost	127.0.0.1	管理员	管理员	总部
2619	角色分配	保存	机构id:1;角色id:10;		2014-10-19 12:34:10.485	localhost	127.0.0.1	管理员	管理员	总部
2620	角色分配	删除	删除机构下的角色ID：10;	1	2014-10-19 12:34:36.31	localhost	127.0.0.1	管理员	管理员	总部
2621	模块分配操作	保存	模块操作关系ID: 1;11;	21;row	2014-10-19 17:16:46.038	localhost	127.0.0.1	管理员	管理员	总部
2622	模块分配操作	保存	模块操作关系ID: 1;11;	21;row	2014-10-19 17:16:57.363	localhost	127.0.0.1	管理员	管理员	总部
2623	模块分配操作	保存	模块操作关系ID: 	21;row	2014-10-19 17:17:57.009	localhost	127.0.0.1	管理员	管理员	总部
2624	用户(普通)组管理	停用	组名称：普通1;组类型：generalGroup;	3	2014-10-20 22:15:21.581	localhost	127.0.0.1	管理员	管理员	总部
2625	系统公告管理	修改	修改公告ID：6;修改公告标题：第二版本发布了！;有效期：Mon May 01 00:00:00 CST 2017;是否发布：0;	6	2014-10-20 22:17:18.539	localhost	127.0.0.1	管理员	管理员	总部
2626	系统模块管理	删除	待办任务;underwayTaskId;	61	2014-11-01 16:39:15.146	localhost	127.0.0.1	管理员	管理员	总部
2627	系统模块管理	删除	已办任务;finishedMgr;	63	2014-11-01 16:39:30.025	localhost	127.0.0.1	管理员	管理员	总部
2628	系统模块管理	删除	测试;test;	60	2014-11-01 16:51:17.676	localhost	127.0.0.1	管理员	管理员	总部
2629	系统模块管理	删除	工作流;workflowId;	50	2014-11-01 16:51:30.124	localhost	127.0.0.1	管理员	管理员	总部
2630	系统模块管理	删除	页面功能区管理;mainFrameFunMgr;	4	2014-11-01 16:58:48.273	localhost	127.0.0.1	管理员	管理员	总部
2631	系统模块管理	删除	主页框架管理;mainframemgr;	3	2014-11-01 16:58:59.593	localhost	127.0.0.1	管理员	管理员	总部
2632	用户登录	用户登录	总部-管理员-管理员		2014-11-01 16:59:14.374	localhost	127.0.0.1	管理员	管理员	总部
2633	用户登录	用户登录	总部-管理员-管理员		2014-11-01 19:31:42.166	localhost	127.0.0.1	管理员	管理员	总部
2634	用户登录	用户登录	总部-管理员-管理员		2014-11-02 09:24:08.68	localhost	127.0.0.1	管理员	管理员	总部
2635	用户管理	重置密码	重置密码用户：张军	21	2014-11-02 10:02:33.783	localhost	127.0.0.1	管理员	管理员	总部
2636	用户登录	用户登录	总部-管理员-管理员		2014-11-02 13:57:15.365	localhost	127.0.0.1	管理员	管理员	总部
2637	用户登录	用户登录	总部-管理员-管理员		2014-11-02 15:37:16.238	localhost	127.0.0.1	管理员	管理员	总部
2638	用户登录	用户登录	总部-管理员-管理员		2014-11-02 19:13:14.623	localhost	127.0.0.1	管理员	管理员	总部
2639	用户登录	用户登录	总部-管理员-管理员		2014-11-02 19:21:04.17	localhost	127.0.0.1	管理员	管理员	总部
2640	用户登录	用户登录	总部-管理员-管理员		2014-11-03 21:27:47.558	localhost	127.0.0.1	管理员	管理员	总部
2641	用户登录	用户登录	总部-管理员-管理员		2014-11-04 20:46:20.789	localhost	127.0.0.1	管理员	管理员	总部
2642	用户登录	用户登录	总部-管理员-管理员		2014-11-04 20:59:43.734	localhost	127.0.0.1	管理员	管理员	总部
2643	用户登录	用户登录	总部-管理员-管理员		2014-11-04 21:07:20.549	localhost	127.0.0.1	管理员	管理员	总部
2644	用户登录	用户登录	总部-管理员-管理员		2014-11-04 21:13:55.469	localhost	127.0.0.1	管理员	管理员	总部
2645	用户登录	用户登录	总部-管理员-管理员		2014-11-04 21:18:26.018	localhost	127.0.0.1	管理员	管理员	总部
2646	用户登录	用户登录	总部-管理员-管理员		2014-11-04 21:25:41.524	localhost	127.0.0.1	管理员	管理员	总部
2647	用户登录	用户登录	总部-管理员-管理员		2014-11-05 20:16:07.607	localhost	127.0.0.1	管理员	管理员	总部
2648	模块分配操作	保存	模块操作关系ID: 	17;opt	2014-11-05 20:20:29.427	localhost	127.0.0.1	管理员	管理员	总部
2649	用户登录	用户登录	总部-管理员-管理员		2014-11-05 20:43:15.752	localhost	127.0.0.1	管理员	管理员	总部
2650	用户登录	用户登录	总部-管理员-管理员		2014-11-06 21:22:28.588	localhost	127.0.0.1	管理员	管理员	总部
2651	用户登录	用户登录	总部-管理员-管理员		2014-11-07 21:33:29.354	localhost	127.0.0.1	管理员	管理员	总部
2652	用户登录	用户登录	总部-管理员-管理员		2014-11-07 21:37:26.611	localhost	127.0.0.1	管理员	管理员	总部
2653	用户登录	用户登录	总部-管理员-管理员		2014-11-07 21:41:54.256	localhost	127.0.0.1	管理员	管理员	总部
2654	用户登录	用户登录	总部-管理员-管理员		2014-11-07 21:57:35.484	localhost	127.0.0.1	管理员	管理员	总部
2655	用户登录	用户登录	总部-管理员-管理员		2014-11-08 20:54:19.012	localhost	127.0.0.1	管理员	管理员	总部
2656	用户登录	用户登录	总部-管理员-管理员		2014-11-09 08:44:19.671	localhost	127.0.0.1	管理员	管理员	总部
2657	系统配置管理	保存	增加管理员:gaoxf;		2014-11-09 08:44:44.598	localhost	127.0.0.1	管理员	管理员	总部
2658	系统配置管理	保存	删除管理员:gaoxf;		2014-11-09 08:44:59.738	localhost	127.0.0.1	管理员	管理员	总部
2659	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201203:001:zhangsan;	25	2014-11-09 08:46:28.763	localhost	127.0.0.1	管理员	管理员	总部
2660	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201203:001:zhangsan;	25	2014-11-09 08:47:33.825	localhost	127.0.0.1	管理员	管理员	总部
2661	系统编码管理	停用	编码停用：编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201203:001:zhangsan;	25	2014-11-09 08:47:33.827	localhost	127.0.0.1	管理员	管理员	总部
2662	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201203:001:zhangsan;	25	2014-11-09 08:47:37.531	localhost	127.0.0.1	管理员	管理员	总部
2663	系统编码管理	启用	编码启用：编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201203:001:zhangsan;	25	2014-11-09 08:47:37.535	localhost	127.0.0.1	管理员	管理员	总部
2664	系统模块管理	修改	系统参数管理;systemParameterMgr;	15	2014-11-09 09:17:12.461	localhost	127.0.0.1	管理员	管理员	总部
2665	用户登录	用户登录	总部-管理员-管理员		2014-11-09 09:19:21.488	localhost	127.0.0.1	管理员	管理员	总部
2666	系统参数管理	修改	参数名称：用户默认密码;参数键值：userdefaultpassword;参数值：12345;参数类型：platform	1	2014-11-09 09:21:25.037	localhost	127.0.0.1	管理员	管理员	总部
2667	系统参数管理	修改	参数名称：用户默认密码;参数键值：userdefaultpassword;参数值：123456;参数类型：platform	1	2014-11-09 09:21:33.313	localhost	127.0.0.1	管理员	管理员	总部
2668	系统附件	删除	删除附件ID：[19]		2014-11-09 09:34:14.826	localhost	127.0.0.1	管理员	管理员	总部
2669	删除	系统公告管理	删除的公告ID：[9]		2014-11-09 09:34:14.833	localhost	127.0.0.1	管理员	管理员	总部
2670	系统附件	保存	附件业务表ID：11;附件名称:20140629_172256_HDR!jpg	20	2014-11-09 10:23:52.268	localhost	127.0.0.1	管理员	管理员	总部
2671	系统公告管理	保存	保存公告标题：2.0版本;有效期限:Thu Nov 30 00:00:00 CST 2017	11	2014-11-09 10:23:52.274	localhost	127.0.0.1	管理员	管理员	总部
2672	系统附件	删除	删除附件ID：[20]		2014-11-09 10:26:43.607	localhost	127.0.0.1	管理员	管理员	总部
2673	删除	系统公告管理	删除的公告ID：[11]		2014-11-09 10:26:43.612	localhost	127.0.0.1	管理员	管理员	总部
2674	系统附件	保存	附件业务表ID：12;附件名称:20140629_172256_HDR.jpg	21	2014-11-09 10:27:52.456	localhost	127.0.0.1	管理员	管理员	总部
2675	系统附件	保存	附件业务表ID：12;附件名称:20140629_172256_HDR (2).jpg	22	2014-11-09 10:27:52.462	localhost	127.0.0.1	管理员	管理员	总部
2676	系统公告管理	保存	保存公告标题：2.0版本;有效期限:Thu Nov 30 00:00:00 CST 2017	12	2014-11-09 10:27:52.465	localhost	127.0.0.1	管理员	管理员	总部
2677	系统附件	删除	删除附件ID：[22]		2014-11-09 10:28:12.948	localhost	127.0.0.1	管理员	管理员	总部
2678	系统附件	删除	删除附件ID：[21]		2014-11-09 10:28:31.689	localhost	127.0.0.1	管理员	管理员	总部
2679	系统调度管理	停止调度器	停止调度器		2014-11-09 10:33:37.624	localhost	127.0.0.1	管理员	管理员	总部
2680	系统调度管理	停止调度器	停止调度器		2014-11-09 10:33:39.056	localhost	127.0.0.1	管理员	管理员	总部
2681	系统调度管理	停止调度器	停止调度器		2014-11-09 10:33:47.586	localhost	127.0.0.1	管理员	管理员	总部
2682	系统调度管理	修改	任务名称：logJob;任务类描述:com.mycuckoo.service.impl.platform.job.LoggerJob;触发器类型:Cron;时间表达式:* * 0 15 * ?;	1	2014-11-09 10:38:53.866	localhost	127.0.0.1	管理员	管理员	总部
2683	系统调度管理	删除	任务名称：postgresqlBbBackup;任务类描述:com.bhtec.service.impl.platform.job.PostgresqlDbBackupJob;触发器类型:Cron;时间表达式:0/20 * * * * ?;	3	2014-11-09 11:05:44.851	localhost	127.0.0.1	管理员	管理员	总部
2684	模块分配操作	保存	模块分配操作;	49	2014-11-09 11:29:18.544	localhost	127.0.0.1	管理员	管理员	总部
2685	系统模块管理	修改	系统参数管理;systemParameterMgr;	15	2014-11-09 11:36:33.12	localhost	127.0.0.1	管理员	管理员	总部
2686	系统模块管理	修改	省市地区;districtMgr;	52	2014-11-09 11:54:56.171	localhost	127.0.0.1	管理员	管理员	总部
2687	用户登录	用户登录	总部-管理员-管理员		2014-11-09 11:55:14.898	localhost	127.0.0.1	管理员	管理员	总部
2688	系统模块管理	修改	省市地区管理;districtMgr;	14	2014-11-09 11:57:01.033	localhost	127.0.0.1	管理员	管理员	总部
2689	用户登录	用户登录	总部-管理员-管理员		2014-11-09 11:57:09.246	localhost	127.0.0.1	管理员	管理员	总部
2690	用户登录	用户登录	总部-管理员-管理员		2014-11-09 12:05:27.429	localhost	127.0.0.1	管理员	管理员	总部
2691	省市地区管理	修改	广州;province;	2006	2014-11-09 12:15:25.28	localhost	127.0.0.1	管理员	管理员	总部
2692	省市地区管理	修改	广州;province;	2006	2014-11-09 12:27:44.163	localhost	127.0.0.1	管理员	管理员	总部
2693	类别字典管理	修改	字典大类名称：地区级别;	22	2014-11-09 12:44:29.201	localhost	127.0.0.1	管理员	管理员	总部
2694	系统模块管理	修改	类别字典管理;dictionaryMgr;	10	2014-11-09 12:49:35.054	localhost	127.0.0.1	管理员	管理员	总部
2695	系统模块管理	修改	类别字典管理;dictionaryMgr;	10	2014-11-09 12:51:18.66	localhost	127.0.0.1	管理员	管理员	总部
2696	系统模块管理	修改	类别字典管理;typeDictionaryMgr;	10	2014-11-09 12:52:52.93	localhost	127.0.0.1	管理员	管理员	总部
2697	用户(普通)组管理	停用	组名称：test;组类型：userGroup;	7	2014-11-09 13:23:18.607	localhost	127.0.0.1	管理员	管理员	总部
2698	用户(普通)组管理	启用	组名称：test;组类型：userGroup;	7	2014-11-09 13:23:27.338	localhost	127.0.0.1	管理员	管理员	总部
2699	用户登录	用户登录	总部-管理员-管理员		2014-11-09 21:02:13.052	localhost	127.0.0.1	管理员	管理员	总部
2700	机构管理	修改	机构名称 : 董事会;机构代码 : DSH;上级机构 : 总部;	1	2014-11-09 21:27:24.531	localhost	127.0.0.1	管理员	管理员	总部
2701	机构管理	修改	角色名称 : 董事长;	20	2014-11-09 21:42:49.303	localhost	127.0.0.1	管理员	管理员	总部
2702	模块分配操作	保存	模块操作关系ID: 5;6;7;87;155;156;	20;opt	2014-11-09 21:59:46.739	localhost	127.0.0.1	管理员	管理员	总部
2703	模块分配操作	保存	模块操作关系ID: 	20;opt	2014-11-09 21:59:58.862	localhost	127.0.0.1	管理员	管理员	总部
2704	模块分配操作	保存	模块操作关系ID: rol;	20;row	2014-11-09 22:00:07.786	localhost	127.0.0.1	管理员	管理员	总部
2705	模块分配操作	保存	模块操作关系ID: org;	20;row	2014-11-09 22:00:14.837	localhost	127.0.0.1	管理员	管理员	总部
2706	机构管理	修改	角色名称 : 董事长;	20	2014-11-09 22:00:32.693	localhost	127.0.0.1	管理员	管理员	总部
2707	用户登录	用户登录	总部-管理员-管理员		2014-11-17 09:33:07.979	localhost	127.0.0.1	管理员	管理员	总部
2708	角色分配	保存	机构id:11;角色id:9;		2014-11-17 09:34:00.506	localhost	127.0.0.1	管理员	管理员	总部
2709	角色分配	删除	删除机构下的角色ID：9;	11	2014-11-17 09:34:04.526	localhost	127.0.0.1	管理员	管理员	总部
2710	用户登录	用户登录	总部-管理员-管理员		2017-06-04 16:55:09.302	localhost	127.0.0.1	管理员	管理员	总部
2713	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-08-27 16:28:28.007	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2714	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-08-27 16:30:14.791	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2715	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-08-27 16:35:10.828	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2716	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-08-27 16:53:48.484	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2717	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-08-27 17:32:43.465	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2718	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 10:06:16.668	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2719	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 10:10:46.033	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2720	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 10:16:25.109	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2721	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 10:20:52.785	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2722	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 10:21:03.914	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2723	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 10:21:41.203	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2724	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 10:56:16.047	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2725	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 10:59:43.376	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2726	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 11:14:56.865	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2727	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 12:00:22.146	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2728	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 12:19:01.429	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2729	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 14:46:00.227	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2730	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 14:48:04.223	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2731	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 14:48:51.33	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2734	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 14:56:05.048	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2737	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 15:59:00.958	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2740	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 16:25:18.996	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2744	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 16:50:02.759	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2732	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 14:52:52.52	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2733	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 14:54:52.849	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2735	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 15:12:00.941	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2736	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 15:16:31.405	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2738	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 16:06:05.807	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2739	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 16:07:40.578	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2741	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 16:36:17.006	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2742	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 16:37:26.661	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2743	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 16:46:20.242	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2745	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 17:31:26.557	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2746	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 17:35:13.447	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2747	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 17:35:55.065	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2748	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 17:36:15.709	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2749	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 17:36:52.984	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2750	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 17:37:57.721	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2751	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 17:41:34.965	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2752	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 17:43:45.984	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2753	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 18:11:41.207	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2754	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 18:15:24.076	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2755	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 18:23:51.282	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2756	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 18:43:27.114	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2757	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 18:45:55.324	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2758	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 18:46:18.647	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2759	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 18:49:41.822	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2760	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 18:49:58.933	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2761	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-02 18:50:17.435	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2762	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 08:51:24.248	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2763	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:06:30.138	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2764	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:14:15.236	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2765	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:28:44.807	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2766	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:30:58.547	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2767	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:32:02.509	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2768	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:32:41.055	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2769	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:35:54.258	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2770	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:37:03.186	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2771	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:37:45.808	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2772	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:38:09.731	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2773	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:41:57.146	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2774	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:42:03.89	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2775	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:44:24.721	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2776	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:45:03.296	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2777	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:49:07.649	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2778	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:51:27.281	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2779	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:52:43.422	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2780	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:53:52.585	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2781	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:53:57.962	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2782	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:56:38.589	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2783	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 09:58:02.6	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2784	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:03:47.95	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2785	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:04:32.919	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2786	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:08:01.06	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2787	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:12:17.403	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2788	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:13:32.59	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2789	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:13:43.278	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2790	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:14:51.255	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2791	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:21:26.227	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2792	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:22:16.975	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2793	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 10:25:14.337	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2794	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 16:36:03.807	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2795	用户管理	重置密码	重置密码用户：张军	21	2017-09-03 16:37:59.469	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2796	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 18:21:03.183	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2797	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-03 18:27:23.38	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2798	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 11:39:51.986	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2799	用户管理	重置密码	重置密码用户：张军	21	2017-09-09 11:43:12.128	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2800	用户管理	重置密码	重置密码用户：张军	21	2017-09-09 11:43:43.099	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2801	用户管理	重置密码	重置密码用户：张军	21	2017-09-09 11:44:14.586	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2802	用户管理	重置密码	重置密码用户：张军	21	2017-09-09 11:44:20.991	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2803	用户管理	重置密码	重置密码用户：张军	21	2017-09-09 11:44:42.844	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2804	用户管理	重置密码	重置密码用户：张军	21	2017-09-09 11:44:52.158	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2805	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 12:30:40.581	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2806	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 12:35:01.816	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2807	用户分配角色	保存	55	21	2017-09-09 12:48:10.18	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2808	用户分配角色	保存	55, 0	21	2017-09-09 12:49:21.23	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2809	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 14:04:52.857	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2810	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 14:19:04.73	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2811	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 14:54:39.211	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2812	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 15:00:58.433	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2813	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 15:02:43.575	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2814	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 15:24:22.699	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2815	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 15:39:14.982	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2816	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 16:00:10.85	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2817	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 16:26:34.308	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2818	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 16:44:19.593	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2819	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 17:14:29.796	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2820	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 17:27:04.343	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2821	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-09 17:41:54.631	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2822	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 09:38:34.858	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2823	用户分配角色	保存	55	21	2017-09-10 09:39:10.502	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2824	模块分配操作	保存	模块操作关系ID: 20;	21;row	2017-09-10 09:45:31.645	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2825	模块分配操作	保存	模块操作关系ID: 20;19;	21;row	2017-09-10 09:45:54.285	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2826	模块分配操作	保存	模块操作关系ID: 19;	21;row	2017-09-10 09:55:46.796	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2827	模块分配操作	保存	模块操作关系ID: 1_2;	21;row	2017-09-10 10:20:01.094	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2828	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 11:10:33.607	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2829	用户分配角色	保存	55, 56	21	2017-09-10 11:10:47.919	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2830	用户分配角色	保存	55	21	2017-09-10 11:10:54.081	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2831	模块分配操作	保存	模块操作关系ID: 56;	21;row	2017-09-10 11:13:36.466	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2832	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 12:34:56.399	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2833	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 13:33:27.323	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2834	模块分配操作	保存	模块操作关系ID: 56;	21;row	2017-09-10 13:52:59.178	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2835	模块分配操作	保存	模块操作关系ID: 47;	21;row	2017-09-10 13:53:19.047	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2836	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 14:20:37.055	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2837	模块分配操作	保存	模块操作关系ID: 21;	21;row	2017-09-10 14:20:53.075	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2838	模块分配操作	保存	模块操作关系ID: 30;31;33;34;35;	21;opt	2017-09-10 14:23:44.866	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2839	模块分配操作	保存	模块操作关系ID: 	21;opt	2017-09-10 14:27:07.587	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2840	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 16:36:27.596	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2841	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 16:37:34.075	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2842	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 16:38:31.16	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2843	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 16:38:55.836	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2844	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 16:54:30.974	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2845	机构管理	修改	角色名称 : 董事长;	20	2017-09-10 17:04:43.926	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2846	机构管理	修改	角色名称 : 董事长;	20	2017-09-10 17:06:27.925	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2847	机构管理	修改	角色名称 : 董事长;	20	2017-09-10 17:07:52.343	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2848	机构管理	修改	角色名称 : 董事长;	20	2017-09-10 17:09:36.565	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2849	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 17:34:11.236	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2850	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 17:43:59.938	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2851	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 17:53:06.044	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2852	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 17:54:11.768	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2853	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 17:59:43.845	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2854	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 18:01:20.229	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2855	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-10 18:02:07.073	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2856	模块分配操作	保存	模块操作关系ID: rol;	20;row	2017-09-10 18:02:13.832	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2857	模块分配操作	保存	模块操作关系ID: 45;46;47;48;49;	20;opt	2017-09-10 18:02:33.729	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2858	模块分配操作	保存	模块操作关系ID: 45;46;47;48;49;	20;opt	2017-09-10 18:02:42.942	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2859	模块分配操作	保存	模块操作关系ID: org;	20;row	2017-09-10 18:03:07.639	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2860	模块分配操作	保存	模块操作关系ID: 	20;opt	2017-09-10 18:03:11.633	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2861	机构管理	停用	角色名称 : 人事主管;	22	2017-09-10 18:08:06.307	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2862	机构管理	启用	角色名称 : 人事主管;	23	2017-09-10 18:08:39.231	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2863	机构管理	停用	角色名称 : 人事主管;	24	2017-09-10 18:08:44.507	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2864	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 09:36:13.463	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2865	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 09:37:36.738	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2866	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 09:45:13.889	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2867	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 10:04:57.998	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2868	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 10:08:14.031	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2869	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 10:09:30.251	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2870	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 10:12:45.816	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2871	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 10:15:13.845	ip6-localhost	0:0:0:0:0:0:0:1	管理员	总部 - 管理员(默认)	总部
2872	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 10:21:52.726	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2873	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 10:26:28.676	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2874	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 10:38:35.833	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2875	角色分配	保存	机构id:1;角色id:24;		2017-09-17 10:40:49.996	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2876	角色分配	删除	删除机构下的角色ID：24;	1	2017-09-17 10:41:10.302	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2877	角色分配	保存	机构id:1;角色id:24;		2017-09-17 10:41:18.442	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2878	角色分配	删除	删除机构下的角色ID：24;	1	2017-09-17 10:41:38.339	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2879	角色分配	保存	机构id:1;角色id:24;		2017-09-17 10:41:42.198	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2880	角色分配	删除	删除机构下的角色ID：24;	1	2017-09-17 10:41:47.553	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2881	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 15:49:32.205	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2882	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 16:12:15.034	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2883	机构管理	停用	机构名称 : 开发部;机构代码 : ;上级机构 : 11;	13	2017-09-17 16:13:03.758	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2884	机构管理	启用	机构名称 : 开发部;机构代码 : ;上级机构 : 11;	13	2017-09-17 16:13:06.426	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2885	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 16:21:01.022	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2886	机构管理	修改	机构名称 : 采购部;机构代码 : ;上级机构 : 11;	10	2017-09-17 16:21:20.564	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2887	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 18:22:02.718	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2888	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 18:23:03.497	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2889	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 18:23:37.806	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2890	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 18:24:12.507	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2891	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 18:25:11.502	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2892	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-17 18:26:20.055	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2893	系统模块管理	修改	角色组管理;roleGroupMgr;	48	2017-09-17 18:33:40.016	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2894	模块分配操作	保存	模块分配操作;	48	2017-09-17 18:43:53.025	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2895	模块分配操作	保存	模块分配操作;	48	2017-09-17 18:44:19.65	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2896	模块分配操作	保存	模块分配操作;	48	2017-09-17 18:44:33.105	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2897	模块分配操作	保存	模块分配操作;	48	2017-09-17 18:45:20.319	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2898	模块分配操作	保存	模块分配操作;28	48	2017-09-17 18:45:27.426	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2899	模块分配操作	保存	模块分配操作;	48	2017-09-17 18:46:02.594	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2900	系统模块管理	修改	角色组管理;roleGroupMgr;	48	2017-09-17 18:46:31.398	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2901	系统模块管理	修改	角色组管理;roleGroupMgr3;	48	2017-09-17 18:46:37.337	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2902	系统模块管理	修改	角色组管理;roleGroupMgr;	48	2017-09-17 18:46:43.42	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2903	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 09:29:59.32	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2904	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 09:29:59.32	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2905	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 09:30:06	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2906	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 09:31:28.633	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2907	模块分配操作	保存	模块分配操作;28	48	2017-09-23 09:48:58.06	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2908	模块分配操作	保存	模块分配操作;28	48	2017-09-23 09:52:02.997	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2909	模块分配操作	保存	模块分配操作;28	48	2017-09-23 10:02:45.869	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2910	模块分配操作	保存	模块分配操作;	48	2017-09-23 10:05:14.781	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2911	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 10:05:45.426	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2912	模块分配操作	保存	模块分配操作;	48	2017-09-23 10:05:56.482	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2913	系统模块管理	修改	角色组管理;roleGroupMgr;	48	2017-09-23 10:11:55.762	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2914	系统模块管理	修改	角色组管理;roleGroupMgr;	48	2017-09-23 10:12:47.673	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2915	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 10:20:30.596	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2916	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 10:33:06.953	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2917	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 10:33:41.458	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2918	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 10:44:44.842	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2919	系统模块操作管理	修改	模块标签修改;module-label-icon;moduleLabel;	14	2017-09-23 10:46:56.366	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2966	系统附件	删除	删除附件ID：[38]		2017-09-23 17:12:58.665	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2920	系统模块操作管理	修改	模块标签修改;module-label-icon;moduleLabel2;	14	2017-09-23 10:47:08.591	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2921	系统模块操作管理	修改	模块标签修改;module-label-icon;moduleLabel;	14	2017-09-23 10:47:14.755	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2922	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 10:48:52.084	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2923	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 14:36:13.636	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2924	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 14:37:56.186	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2925	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 14:38:03.204	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2926	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 14:38:10.022	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2927	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 14:38:45.713	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2928	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 14:38:52.445	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2929	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 14:39:00.07	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2930	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 15:05:12.357	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2931	系统附件	保存	附件业务表ID：12;附件名称:pPtx4iNVa2861nj5p1RdGq70t367ebNV_20141030_221345.jpg	26	2017-09-23 15:43:53.261	localhost	127.0.0.1	\N	\N	\N
2932	系统附件	删除	删除附件ID：[26]		2017-09-23 15:44:01.794	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2933	系统附件	保存	附件业务表ID：12;附件名称:ue0h0g22VlSem8gFQV38y1KRLtLJnt4s_20141030_221345.jpg	27	2017-09-23 16:04:38.976	localhost	127.0.0.1	\N	\N	\N
2934	系统附件	删除	删除附件ID：[27]		2017-09-23 16:05:13.447	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2935	系统附件	保存	附件业务表ID：12;附件名称:NqjjLog7jG8uB8AdJWMT0Pa9ag2zDEbh_20141030_221345.jpg	28	2017-09-23 16:09:14.199	localhost	127.0.0.1	\N	\N	\N
2936	系统附件	删除	删除附件ID：[28]		2017-09-23 16:10:08.685	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2937	系统公告管理	修改	修改公告ID：12;修改公告标题：2.0版本;有效期：Thu Nov 30 00:00:00 CST 2017;是否发布：0;修改公告ID：12;	12	2017-09-23 16:11:04.163	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2938	系统附件	保存	附件业务表ID：12;附件名称:9U81h9_20141030_221345.jpg	29	2017-09-23 16:11:15.155	localhost	127.0.0.1	\N	\N	\N
2939	系统附件	删除	删除附件ID：[29]		2017-09-23 16:11:40.68	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2940	系统附件	保存	附件业务表ID：12;附件名称:rFK4BW_20141030_221345.jpg	30	2017-09-23 16:11:47.27	localhost	127.0.0.1	\N	\N	\N
2941	系统附件	删除	删除附件ID：[30]		2017-09-23 16:13:34.82	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2942	系统附件	保存	附件业务表ID：12;附件名称:TYyYl4_20141030_221345.jpg	31	2017-09-23 16:13:48.12	localhost	127.0.0.1	\N	\N	\N
2943	系统附件	删除	删除附件ID：[31]		2017-09-23 16:14:01.951	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2944	系统附件	保存	附件业务表ID：12;附件名称:sAh9t6_20141030_221345.jpg	32	2017-09-23 16:15:00.38	localhost	127.0.0.1	\N	\N	\N
2945	系统附件	删除	删除附件ID：[32]		2017-09-23 16:16:52.073	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2946	系统附件	保存	附件业务表ID：12;附件名称:Qhdeuv_20141030_221345.jpg	33	2017-09-23 16:17:00.908	localhost	127.0.0.1	\N	\N	\N
2947	系统附件	删除	删除附件ID：[33]		2017-09-23 16:17:32.824	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2948	系统附件	保存	附件业务表ID：12;附件名称:eam5nw_20141030_221345.jpg	34	2017-09-23 16:17:45.674	localhost	127.0.0.1	\N	\N	\N
2949	系统附件	删除	删除附件ID：[34]		2017-09-23 16:19:18.8	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2950	系统附件	保存	附件业务表ID：12;附件名称:JduG3y_20141030_221345.jpg	35	2017-09-23 16:20:00.454	localhost	127.0.0.1	\N	\N	\N
2951	系统附件	删除	删除附件ID：[35]		2017-09-23 16:21:40.693	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2952	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 16:24:27.858	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2953	系统附件	保存	附件业务表ID：12;附件名称:KsUsgq_20141030_221345.jpg	36	2017-09-23 16:34:23.557	localhost	127.0.0.1	\N	\N	\N
2954	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 16:36:31.911	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2955	系统附件	删除	删除附件ID：[36]		2017-09-23 16:36:48.342	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2956	系统附件	保存	附件业务表ID：12;附件名称:JB0uHO_20141030_221345.jpg	37	2017-09-23 16:36:57.925	localhost	127.0.0.1	\N	\N	\N
2957	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 17:00:05.01	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2958	系统附件	删除	删除附件ID：[37]		2017-09-23 17:00:15.109	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2959	系统公告管理	修改	修改公告ID：12;修改公告标题：2.0版本;有效期：Thu Nov 30 00:00:00 CST 2017;是否发布：0;修改公告ID：12;	12	2017-09-23 17:01:16.815	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2960	系统公告管理	修改	修改公告ID：12;修改公告标题：2.0版本;有效期：Thu Nov 30 00:00:00 CST 2017;是否发布：0;修改公告ID：12;	12	2017-09-23 17:02:50.606	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2961	系统公告管理	修改	修改公告ID：12;修改公告标题：2.0版本;有效期：Thu Nov 30 00:00:00 CST 2017;是否发布：0;修改公告ID：12;	12	2017-09-23 17:02:59.619	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2962	系统公告管理	修改	修改公告ID：12;修改公告标题：2.0版本;有效期：Thu Nov 30 00:00:00 CST 2017;是否发布：0;修改公告ID：12;	12	2017-09-23 17:03:15.116	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2963	系统公告管理	修改	修改公告ID：12;修改公告标题：2.0版本;有效期：Thu Nov 30 00:00:00 CST 2017;是否发布：0;修改公告ID：12;	12	2017-09-23 17:03:55.641	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2964	系统附件	保存	附件业务表ID：12;附件名称:_20141030_2213456nhVm8.jpg	38	2017-09-23 17:12:31.725	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2965	系统公告管理	修改	修改公告ID：12;修改公告标题：2.0版本;有效期：Thu Nov 30 00:00:00 CST 2017;是否发布：0;修改公告ID：12;	12	2017-09-23 17:12:31.824	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2970	系统附件	保存	附件业务表ID：12;附件名称:20141030_221345_0pjVrH.jpg	40	2017-09-23 17:14:12.707	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2971	系统公告管理	修改	修改公告ID：12;修改公告标题：2.0版本;有效期：Thu Nov 30 00:00:00 CST 2017;是否发布：0;修改公告ID：12;	12	2017-09-23 17:14:12.714	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2973	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 17:16:34.318	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2974	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 17:24:10.748	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2975	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 17:26:31.905	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2976	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 17:50:37.649	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2977	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 17:51:18.258	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2967	系统附件	保存	附件业务表ID：12;附件名称:20141030_221345_oIvX5d.jpg	39	2017-09-23 17:13:28.204	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2968	系统公告管理	修改	修改公告ID：12;修改公告标题：2.0版本;有效期：Thu Nov 30 00:00:00 CST 2017;是否发布：0;修改公告ID：12;	12	2017-09-23 17:13:28.21	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2969	系统附件	删除	删除附件ID：[39]		2017-09-23 17:13:45.413	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2972	系统附件	删除	删除附件ID：[40]		2017-09-23 17:14:20.257	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2978	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 17:55:40.852	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2979	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 18:36:51.499	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2980	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 19:52:40.693	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2981	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 19:58:47.563	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2982	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 20:18:24.063	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2983	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 20:23:25.415	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2984	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201709:001:zhangsan;	25	2017-09-23 20:49:43.804	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2985	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201709:001;	25	2017-09-23 20:52:58.8	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2986	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201709:001:zhangsan;	25	2017-09-23 20:53:05.969	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2987	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 20:55:34.505	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2988	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201709:001:zhangsan;	25	2017-09-23 20:55:39.454	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2989	系统编码管理	停用	编码停用：编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201709:001:zhangsan;	25	2017-09-23 20:55:39.465	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2990	系统编码管理	修改	编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201709:001:zhangsan;	25	2017-09-23 20:55:43.74	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2991	系统编码管理	启用	编码启用：编码英文名称：RKD;编码中文名称: 入库单;编码所属模块名称: 入库单;编码效果: RKD:201709:001:zhangsan;	25	2017-09-23 20:55:43.747	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2992	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 20:57:22.344	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2993	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 21:22:59.281	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2994	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-23 21:31:28.16	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2995	省市地区管理	修改	北京;province;	2000	2017-09-23 21:31:58.456	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2996	省市地区管理	修改	北京;province;	2000	2017-09-23 21:32:04.326	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2997	省市地区管理	修改	北京;province;	2000	2017-09-23 21:32:12.542	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2998	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:31:29.827	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
2999	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:32:44.151	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3000	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:39:46.666	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3001	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:41:42.887	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3002	类别字典管理	修改	字典大类名称：页面类型;	25	2017-09-24 09:41:49.521	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3003	类别字典管理	修改	字典大类名称：页面类型;	25	2017-09-24 09:42:16.463	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3004	类别字典管理	停用	字典大类名称：页面类型;	25	2017-09-24 09:43:24.087	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3005	类别字典管理	启用	字典大类名称：页面类型;	25	2017-09-24 09:43:26.685	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3006	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:45:10.76	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3007	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:45:16.761	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3008	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:45:22.545	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3009	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:45:31.339	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3010	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:47:27.491	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3011	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:48:11.205	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3012	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 09:50:22.655	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3013	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 10:24:23.293	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3014	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 10:26:51.892	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3015	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 10:26:56.514	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3016	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 10:27:08.912	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3017	系统调度管理	修改	任务名称：日志清理;任务类描述:com.mycuckoo.service.impl.platform.job.LoggerJob;触发器类型:Cron;时间表达式:* * 0 15 * ?;	1	2017-09-24 10:30:46.074	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3018	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 10:30:53.023	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3019	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 10:30:57.584	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3020	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 11:30:55.701	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3021	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 11:31:21.471	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3022	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 11:33:59.659	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3023	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 11:41:48.386	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3024	系统配置管理	保存	设置系统名称: 系统平台/统一用户;		2017-09-24 11:41:56.03	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3025	系统配置管理	保存	设置系统名称: 系统平台/统一用户;		2017-09-24 11:44:58.035	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3026	系统配置管理	保存	设置系统名称: 系统平台/统一用户;		2017-09-24 11:45:42.328	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3027	系统配置管理	保存	设置系统名称: 系统平台/统一用户;		2017-09-24 11:46:11.71	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3028	系统配置管理	保存	设置系统名称: 系统平台/统一用户;		2017-09-24 11:46:40.535	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3029	系统配置管理	保存	设置日志保留天数:3;		2017-09-24 11:49:35.731	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3030	系统配置管理	保存	设置日志保留天数:30;		2017-09-24 11:49:40.229	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3031	系统配置管理	保存	设置日志级别:2;		2017-09-24 11:49:43.127	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3032	系统配置管理	保存	设置日志级别:1;		2017-09-24 11:49:45.78	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3033	系统配置管理	保存	设置权限级别:rol;		2017-09-24 11:49:50.841	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3034	系统配置管理	保存	设置权限级别:org;		2017-09-24 11:49:54.614	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3035	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 11:51:39.935	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3036	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:02:28.989	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3037	系统配置管理	保存	增加管理员:zhangj;		2017-09-24 12:02:35.296	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3038	系统配置管理	保存	删除管理员:zhangj;		2017-09-24 12:02:49.551	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3039	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:08:51.765	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3040	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:10:13.511	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3041	系统配置管理	保存	设置系统名称: 系统平台 - 统一用户;		2017-09-24 12:10:58.677	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3042	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:15:05.545	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3043	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:26:39.354	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3044	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:26:55.652	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3045	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:28:04.13	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3046	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:28:19.896	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3047	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:28:24.426	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3048	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:28:32.745	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3049	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:28:41.889	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3050	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:28:45.766	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3051	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:28:49.829	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3052	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:29:06.231	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3053	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 12:30:02.485	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3054	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 13:27:57.03	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3055	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 13:41:17.602	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3056	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 13:48:41.776	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3057	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 13:55:49.908	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3058	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 14:10:17.806	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3059	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 14:11:46.15	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3060	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 14:12:06.676	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3061	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 14:12:16.946	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3062	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 14:12:22.051	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3063	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 14:12:32.525	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3064	系统参数管理	修改	参数名称：用户有效期;参数键值：uservalidate;参数值：6;参数类型：uum;	2	2017-09-24 14:15:11.746	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3065	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 14:16:22.069	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3066	系统参数管理	修改	参数名称：用户有效期;参数键值：uservalidate;参数值：6;参数类型：uum;	2	2017-09-24 14:16:26.452	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3067	系统参数管理	停用	参数名称：用户有效期;参数键值：uservalidate;参数值：6;参数类型：uum;	2	2017-09-24 14:16:26.458	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3068	系统参数管理	修改	参数名称：用户有效期;参数键值：uservalidate;参数值：6;参数类型：uum;	2	2017-09-24 14:16:29.996	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3069	系统参数管理	启用	参数名称：用户有效期;参数键值：uservalidate;参数值：6;参数类型：uum;	2	2017-09-24 14:16:30.002	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3070	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-09-24 14:41:39.835	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3071	用户登录	用户登录	总部-管理员-null		2017-10-06 09:42:21.558	localhost	127.0.0.1	\N	管理员	总部
3072	用户登录	用户登录	总部-管理员-null		2017-10-06 09:45:33.631	localhost	127.0.0.1	\N	管理员	总部
3073	用户登录	用户登录	总部-管理员-null		2017-10-06 09:49:31.252	localhost	127.0.0.1	\N	管理员	总部
3074	系统模块管理	删除	普通组管理;generalGroupMgr;	28	2017-10-06 09:49:41.146	localhost	127.0.0.1	\N	管理员	总部
3075	用户登录	用户登录	总部-管理员-null		2017-10-06 09:50:01.662	localhost	127.0.0.1	\N	管理员	总部
3076	系统模块管理	删除	用户组管理;userGroupMgr;	27	2017-10-06 09:50:15.691	localhost	127.0.0.1	\N	管理员	总部
3077	系统模块管理	删除	角色组管理;roleGroupMgr;	48	2017-10-06 09:50:21.808	localhost	127.0.0.1	\N	管理员	总部
3078	系统模块管理	删除	群组管理;groupMgr;	23	2017-10-06 09:50:27.594	localhost	127.0.0.1	\N	管理员	总部
3079	系统模块管理	修改	菜单管理;moduleMgr;	7	2017-10-06 09:51:19.807	localhost	127.0.0.1	\N	管理员	总部
3080	系统模块管理	修改	操作管理;moduleOptMgr;	6	2017-10-06 09:51:32.146	localhost	127.0.0.1	\N	管理员	总部
3081	系统模块管理	修改	按钮管理;operateMgr;	8	2017-10-06 09:51:47.673	localhost	127.0.0.1	\N	管理员	总部
3082	用户登录	用户登录	总部-管理员-null		2017-10-06 09:52:20.339	localhost	127.0.0.1	\N	管理员	总部
3083	系统模块管理	修改	菜单管理;moduleOptMgr;	6	2017-10-06 09:53:03.137	localhost	127.0.0.1	\N	管理员	总部
3084	用户登录	用户登录	总部-管理员-null		2017-10-06 09:59:04.03	localhost	127.0.0.1	\N	管理员	总部
3085	系统模块管理	修改	操作按钮管理;operateMgr;	8	2017-10-06 10:00:20.901	localhost	127.0.0.1	\N	管理员	总部
3086	系统模块管理	修改	操作菜单管理;moduleMgr;	7	2017-10-06 10:00:55.778	localhost	127.0.0.1	\N	管理员	总部
3087	用户登录	用户登录	总部-管理员-null		2017-10-06 10:01:09.33	localhost	127.0.0.1	\N	管理员	总部
3088	用户登录	用户登录	总部-管理员-null		2017-10-06 10:41:49.786	localhost	127.0.0.1	\N	管理员	总部
3089	用户登录	用户登录	总部-管理员-null		2017-10-06 10:45:52.038	localhost	127.0.0.1	\N	管理员	总部
3090	用户登录	用户登录	总部-管理员-null		2017-10-06 10:56:17.402	localhost	127.0.0.1	\N	管理员	总部
3091	用户登录	用户登录	总部-管理员-null		2017-10-06 11:03:45.708	localhost	127.0.0.1	\N	管理员	总部
3092	用户登录	用户登录	总部-管理员-null		2017-10-06 11:04:23.862	localhost	127.0.0.1	\N	管理员	总部
3093	系统附件	保存	附件业务表ID：6;附件名称:mycuckoo/upload/document/20141030_221345_ho43e2.jpg	41	2017-10-06 11:10:10.74	localhost	127.0.0.1	\N	管理员	总部
3094	系统公告管理	修改	修改公告ID：6;修改公告标题：第二版本发布了！;有效期：Mon May 01 00:00:00 CST 2017;是否发布：0;修改公告ID：6;	6	2017-10-06 11:10:10.78	localhost	127.0.0.1	\N	管理员	总部
3095	系统附件	删除	删除附件ID：[41]		2017-10-06 11:10:30.26	localhost	127.0.0.1	\N	管理员	总部
3096	系统附件	保存	附件业务表ID：6;附件名称:mycuckoo/upload/document/20141030_221345_F5xocg.jpg	42	2017-10-06 11:13:06.36	localhost	127.0.0.1	\N	管理员	总部
3097	系统公告管理	修改	修改公告ID：6;修改公告标题：第二版本发布了！;有效期：Mon May 01 00:00:00 CST 2017;是否发布：0;修改公告ID：6;	6	2017-10-06 11:13:06.371	localhost	127.0.0.1	\N	管理员	总部
3098	用户登录	用户登录	总部-管理员-null		2017-10-06 11:14:12.89	localhost	127.0.0.1	\N	管理员	总部
3099	用户登录	用户登录	总部-管理员-null		2017-10-06 11:23:31.092	localhost	127.0.0.1	\N	管理员	总部
3100	系统附件	删除	删除附件ID：[42]		2017-10-06 11:27:23.788	localhost	127.0.0.1	\N	管理员	总部
3101	用户登录	用户登录	总部-管理员-null		2017-10-06 11:27:45.214	localhost	127.0.0.1	\N	管理员	总部
3102	用户登录	用户登录	总部-管理员-null		2017-10-06 11:39:34.084	localhost	127.0.0.1	\N	管理员	总部
3103	用户登录	用户登录	总部-管理员-null		2017-10-06 11:40:06.88	localhost	127.0.0.1	\N	管理员	总部
3104	用户登录	用户登录	总部-管理员-null		2017-10-06 11:45:26.615	localhost	127.0.0.1	\N	管理员	总部
3105	用户登录	用户登录	总部-管理员-null		2017-10-06 11:47:36.643	localhost	127.0.0.1	\N	管理员	总部
3106	用户登录	用户登录	总部-管理员-null		2017-10-06 11:49:01.627	localhost	127.0.0.1	\N	管理员	总部
3107	用户登录	用户登录	总部-管理员-null		2017-10-06 11:49:32.7	localhost	127.0.0.1	\N	管理员	总部
3108	用户登录	用户登录	总部-管理员-null		2017-10-06 11:51:14.27	localhost	127.0.0.1	\N	管理员	总部
3109	用户登录	用户登录	总部-管理员-null		2017-10-06 11:52:28.461	localhost	127.0.0.1	\N	管理员	总部
3110	用户登录	用户登录	总部-管理员-null		2017-10-06 11:53:34.601	localhost	127.0.0.1	\N	管理员	总部
3111	用户登录	用户登录	总部-管理员-null		2017-10-06 11:56:53.43	localhost	127.0.0.1	\N	管理员	总部
3112	用户登录	用户登录	总部-管理员-null		2017-10-06 11:57:03.088	localhost	127.0.0.1	\N	管理员	总部
3113	用户登录	用户登录	总部-管理员-null		2017-10-06 11:58:41.438	localhost	127.0.0.1	\N	管理员	总部
3114	用户登录	用户登录	总部-管理员-null		2017-10-06 11:59:18.672	localhost	127.0.0.1	\N	管理员	总部
3115	用户登录	用户登录	总部-管理员-null		2017-10-06 11:59:54.565	localhost	127.0.0.1	\N	管理员	总部
3116	用户登录	用户登录	总部-管理员-null		2017-10-06 12:00:19.55	localhost	127.0.0.1	\N	管理员	总部
3117	用户登录	用户登录	总部-管理员-null		2017-10-06 12:02:00.924	localhost	127.0.0.1	\N	管理员	总部
3118	用户登录	用户登录	总部-管理员-null		2017-10-06 12:03:28.549	localhost	127.0.0.1	\N	管理员	总部
3119	用户登录	用户登录	总部-管理员-null		2017-10-06 12:06:18.142	localhost	127.0.0.1	\N	管理员	总部
3120	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:25:16.254	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3121	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:25:34.356	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3122	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:29:22.053	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3123	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:29:51.003	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3124	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:31:13.126	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3125	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:34:52.482	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3126	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:35:03.887	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3127	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:37:53.981	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3128	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:38:04.432	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3129	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:38:50.759	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3130	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:39:21.311	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3131	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:40:22.768	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3132	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:43:31.52	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3133	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:43:54.395	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3134	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 15:55:32.716	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3135	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:00:01.299	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3136	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:01:31.173	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3137	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:02:08.948	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3138	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:02:46.335	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3139	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:04:56.306	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3140	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:06:27.485	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3141	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:38:11.144	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3142	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:38:24.362	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3143	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:39:56.258	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3144	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 16:56:42.478	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
3145	用户登录	用户登录	总部-总部 - 管理员(默认)-管理员		2017-10-06 17:00:12.605	localhost	127.0.0.1	管理员	总部 - 管理员(默认)	总部
\.


--
-- Name: syspl_sys_opt_log_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_sys_opt_log_seq', 3145, true);


--
-- Data for Name: syspl_sys_parameter; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY syspl_sys_parameter (para_id, para_name, para_key_name, para_value, para_type, memo, status, creator, create_date) FROM stdin;
1	用户默认密码	userdefaultpassword	123456	platform		enable	admin	2011-12-20 14:48:25
2	用户有效期	uservalidate	6	uum		enable	admin	2011-12-20 14:48:55
\.


--
-- Name: syspl_sys_parameter_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('syspl_sys_parameter_seq', 23, true);


--
-- Data for Name: uum_org_role_ref; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY uum_org_role_ref (org_role_id, org_id, role_id) FROM stdin;
40	4	10
45	11	3
46	11	4
47	7	7
1	0	1
0	0	0
51	2	7
53	6	17
54	6	18
55	6	19
56	1	20
67	2	13
\.


--
-- Name: uum_org_role_ref_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('uum_org_role_ref_seq', 86, true);


--
-- Data for Name: uum_organ; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY uum_organ (org_id, org_simple_name, org_full_name, org_code, org_address1, org_address2, org_tel1, org_tel2, org_begin_date, org_type, org_fax, org_postal, org_legal, org_tax_no, org_reg_no, org_belong_dist, org_parent, status, memo, creator, create_date) FROM stdin;
5	海淀							\N	HQ	\N					2000	2	enable		admin	2012-02-28 14:58:56.453
0	总部	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	enable	\N	\N	\N
9	财务部							\N	department	\N					2000	11	enable		admin	2012-03-06 14:44:45
8	人力资源							\N	department	\N					2000	11	enable		admin	2012-03-06 14:44:23
7	运营部							\N	department	\N					2000	11	enable		admin	2012-03-06 14:43:37
12	储运部							\N	department	\N					2000	11	enable		admin	2012-03-06 14:48:38.078
4	太原							\N	branch	\N					0	3	enable		admin	2012-02-09 17:48:18
3	山西运营							\N	department	\N					0	7	enable		admin	2012-02-09 17:44:23
14	北京采购							\N	department	\N					2000	10	enable		admin	2012-03-06 14:52:44.171
15	上海采购							\N	department	\N					2003	10	enable		admin	2012-03-06 14:53:18.921
16	北京储运							\N	branch	\N					2000	12	enable		admin	2012-03-06 14:54:15.375
6	五道口店							\N	store	\N					2002	5	enable		admin	2012-02-28 15:40:17
11	总经办	北京总经办	ZJB	总经办总经办总经办		57568678768777	776845745654646	2012-03-02	department	\N	454656	总经办	66666666	666666	2000	1	enable		admin	2012-03-06 14:46:04
17	上海储运							2013-10-08	branch	\N					2003	12	enable		admin	2012-03-06 14:54:36.656
1	董事会	董事会	DSH					2011-12-21	branch	\N					0	0	enable		admin	2011-12-21 09:33:50
18	技术部	\N	10010	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	disable	测试	\N	2017-06-23 21:54:33.642
19	技术部	\N	10010	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	disable	测试	\N	2017-06-23 21:58:00.205
20	技术部	\N	10010	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	disable	测试	\N	2017-06-23 22:00:37.672
2	技术部	\N	10010	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	disable	测试	admin	2012-01-10 14:11:58
13	开发部							\N	department	\N					2000	11	enable		admin	2012-03-06 14:49:02.406
10	采购部							\N	department	\N					2000	11	enable		admin	2012-03-06 14:45:25
\.


--
-- Name: uum_organ_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('uum_organ_seq', 21, true);


--
-- Data for Name: uum_privilege; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY uum_privilege (privilege_id, resource_id, owner_id, owner_type, privilege_scope, privilege_type) FROM stdin;
899	58	3	rol	inc	opt
900	59	3	rol	inc	opt
901	60	3	rol	inc	opt
902	61	3	rol	inc	opt
903	62	3	rol	inc	opt
904	91	3	rol	inc	opt
905	97	3	rol	inc	opt
906	152	3	rol	inc	opt
907	45	3	rol	inc	opt
908	46	3	rol	inc	opt
909	47	3	rol	inc	opt
910	48	3	rol	inc	opt
911	49	3	rol	inc	opt
912	50	3	rol	inc	opt
913	51	3	rol	inc	opt
914	52	3	rol	inc	opt
915	53	3	rol	inc	opt
916	54	3	rol	inc	opt
917	55	3	rol	inc	opt
918	56	3	rol	inc	opt
919	57	3	rol	inc	opt
936	5	3	rol	inc	opt
937	6	3	rol	inc	opt
938	7	3	rol	inc	opt
939	87	3	rol	inc	opt
940	155	3	rol	inc	opt
941	10	3	rol	inc	opt
942	11	3	rol	inc	opt
943	12	3	rol	inc	opt
944	13	3	rol	inc	opt
945	14	3	rol	inc	opt
946	15	3	rol	inc	opt
947	16	3	rol	inc	opt
948	17	3	rol	inc	opt
949	18	3	rol	inc	opt
950	19	3	rol	inc	opt
951	25	3	rol	inc	opt
952	26	3	rol	inc	opt
953	27	3	rol	inc	opt
954	28	3	rol	inc	opt
955	29	3	rol	inc	opt
956	36	3	rol	inc	opt
957	37	3	rol	inc	opt
958	38	3	rol	inc	opt
959	39	3	rol	inc	opt
960	40	3	rol	inc	opt
961	98	3	rol	inc	opt
962	99	3	rol	inc	opt
963	100	3	rol	inc	opt
964	101	3	rol	inc	opt
966	104	3	rol	inc	opt
967	105	3	rol	inc	opt
968	106	3	rol	inc	opt
969	107	3	rol	inc	opt
970	108	3	rol	inc	opt
971	109	3	rol	inc	opt
972	110	3	rol	inc	opt
973	111	3	rol	inc	opt
974	44	3	rol	inc	opt
975	30	3	rol	inc	opt
976	31	3	rol	inc	opt
977	33	3	rol	inc	opt
978	34	3	rol	inc	opt
979	35	3	rol	inc	opt
980	5	4	rol	inc	opt
981	6	4	rol	inc	opt
982	7	4	rol	inc	opt
983	87	4	rol	inc	opt
984	155	4	rol	inc	opt
985	10	4	rol	inc	opt
986	11	4	rol	inc	opt
987	12	4	rol	inc	opt
988	13	4	rol	inc	opt
989	14	4	rol	inc	opt
994	15	4	rol	inc	opt
995	16	4	rol	inc	opt
996	17	4	rol	inc	opt
997	18	4	rol	inc	opt
998	19	4	rol	inc	opt
999	25	4	rol	inc	opt
1000	26	4	rol	inc	opt
1001	27	4	rol	inc	opt
1002	28	4	rol	inc	opt
1003	29	4	rol	inc	opt
1004	36	4	rol	inc	opt
1005	37	4	rol	inc	opt
1006	38	4	rol	inc	opt
1007	39	4	rol	inc	opt
1008	40	4	rol	inc	opt
1009	98	4	rol	inc	opt
1010	99	4	rol	inc	opt
1011	100	4	rol	inc	opt
1012	101	4	rol	inc	opt
1014	104	4	rol	inc	opt
1015	105	4	rol	inc	opt
1016	106	4	rol	inc	opt
1017	107	4	rol	inc	opt
1018	108	4	rol	inc	opt
1019	109	4	rol	inc	opt
1020	110	4	rol	inc	opt
1021	111	4	rol	inc	opt
1022	44	4	rol	inc	opt
1023	30	4	rol	inc	opt
1024	31	4	rol	inc	opt
1025	33	4	rol	inc	opt
1026	34	4	rol	inc	opt
1027	35	4	rol	inc	opt
1032	5	5	rol	exc	opt
1033	6	5	rol	exc	opt
1034	7	5	rol	exc	opt
1035	87	5	rol	exc	opt
1036	155	5	rol	exc	opt
1037	10	5	rol	exc	opt
1038	11	5	rol	exc	opt
1039	12	5	rol	exc	opt
1040	13	5	rol	exc	opt
1041	14	5	rol	exc	opt
1042	15	5	rol	exc	opt
1043	16	5	rol	exc	opt
1044	17	5	rol	exc	opt
1045	18	5	rol	exc	opt
1046	19	5	rol	exc	opt
1047	25	5	rol	exc	opt
1048	26	5	rol	exc	opt
1049	27	5	rol	exc	opt
1050	28	5	rol	exc	opt
1051	29	5	rol	exc	opt
1052	36	5	rol	exc	opt
1053	37	5	rol	exc	opt
1054	38	5	rol	exc	opt
1055	39	5	rol	exc	opt
1056	40	5	rol	exc	opt
1057	98	5	rol	exc	opt
1058	99	5	rol	exc	opt
1059	100	5	rol	exc	opt
1060	101	5	rol	exc	opt
1062	104	5	rol	exc	opt
1063	105	5	rol	exc	opt
1064	106	5	rol	exc	opt
1065	107	5	rol	exc	opt
1066	108	5	rol	exc	opt
1067	109	5	rol	exc	opt
1068	110	5	rol	exc	opt
1069	111	5	rol	exc	opt
1070	44	5	rol	exc	opt
1071	30	5	rol	exc	opt
1072	31	5	rol	exc	opt
1073	33	5	rol	exc	opt
1074	34	5	rol	exc	opt
1075	35	5	rol	exc	opt
1076	all	7	rol	all	opt
1081	5	13	rol	inc	opt
1082	6	13	rol	inc	opt
1083	7	13	rol	inc	opt
1084	87	13	rol	inc	opt
1085	155	13	rol	inc	opt
1086	10	13	rol	inc	opt
1087	11	13	rol	inc	opt
1088	12	13	rol	inc	opt
1089	13	13	rol	inc	opt
1090	14	13	rol	inc	opt
1091	15	13	rol	inc	opt
1092	16	13	rol	inc	opt
1093	17	13	rol	inc	opt
1094	18	13	rol	inc	opt
1095	19	13	rol	inc	opt
1096	5	17	rol	inc	opt
1097	6	17	rol	inc	opt
1098	7	17	rol	inc	opt
1099	87	17	rol	inc	opt
1100	155	17	rol	inc	opt
1101	10	17	rol	inc	opt
1102	11	17	rol	inc	opt
1103	12	17	rol	inc	opt
1104	13	17	rol	inc	opt
1105	14	17	rol	inc	opt
1106	60	17	rol	inc	opt
1107	5	18	rol	inc	opt
1108	6	18	rol	inc	opt
1109	7	18	rol	inc	opt
1110	87	18	rol	inc	opt
1111	155	18	rol	inc	opt
1117	44	19	usr	inc	opt
1118	rol	7	rol	rol	row
1119	6	15	usr	org	row
1120	14	15	usr	org	row
1121	58	15	usr	exc	opt
1122	59	15	usr	exc	opt
1123	60	15	usr	exc	opt
1124	61	15	usr	exc	opt
1125	62	15	usr	exc	opt
1126	91	15	usr	exc	opt
1127	97	15	usr	exc	opt
1128	152	15	usr	exc	opt
1152	org	19	rol	org	row
1153	org	19	rol	org	row
1174	21	21	usr	usr	row
1192	org	20	rol	org	row
\.


--
-- Name: uum_privilege_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('uum_privilege_seq', 1192, true);


--
-- Data for Name: uum_role; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY uum_role (role_id, role_name, status, memo, creator, create_date, role_level) FROM stdin;
3	总经理	enable		admin	2011-12-20 11:37:31	1
1	管理员	enable		admin	2011-02-11 16:57:01.187	1
0	无角色用户	enable		admin	\N	\N
5	人事经理	enable		admin	2011-12-23 16:33:19	3
4	副总经理	enable		admin	2011-12-23 09:37:13	2
6	储运部经理	enable		admin	2011-12-23 17:21:56	3
9	培训经理	enable	培训经理	admin	2011-12-26 09:54:33	3
8	财务经理	enable		admin	2011-12-26 09:54:22	3
7	运营经理	enable		admin	2011-12-23 17:22:24	3
10	采购经理	enable	工	admin	2012-01-10 14:23:10	3
11	人事主管	enable		admin	2012-03-06 11:50:08.515	4
12	储运主管	enable		admin	2012-03-06 11:50:30.921	4
13	运营主管	enable		admin	2012-03-06 14:06:27.687	4
14	财务主管	enable		admin	2012-03-06 14:06:50.734	4
15	培训主管	enable		admin	2012-03-06 14:07:16.703	4
16	采购主管	enable		admin	2012-03-06 14:07:33.39	4
17	店长	enable		admin	2012-03-06 14:58:25.875	5
18	收银	enable		admin	2012-03-06 14:58:43.859	6
19	店员	enable		admin	2012-03-06 14:59:00.015	7
20	董事长	enable	测试	admin	2012-03-06 15:06:36.25	1
22	人事主管	disable		admin	2012-03-06 11:50:08.515	4
23	人事主管	enable		admin	2012-03-06 11:50:08.515	4
24	人事主管	disable		admin	2012-03-06 11:50:08.515	4
\.


--
-- Name: uum_role_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('uum_role_seq', 24, true);


--
-- Data for Name: uum_role_user_ref; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY uum_role_user_ref (org_role_user_id, user_id, org_role_id, is_default) FROM stdin;
0	0	1	Y
57	12	56	Y
58	13	45	Y
59	14	46	Y
61	16	51	Y
62	17	53	Y
63	18	54	Y
65	20	55	Y
68	19	55	Y
69	19	54	N
70	15	47	Y
71	15	53	N
72	15	54	N
73	15	55	N
74	15	40	N
75	15	45	N
76	15	46	N
77	15	56	N
78	15	1	N
22	21	55	Y
\.


--
-- Name: uum_role_user_ref_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('uum_role_user_ref_seq', 22, true);


--
-- Data for Name: uum_user; Type: TABLE DATA; Schema: cuckoo; Owner: postgres
--

COPY uum_user (user_id, user_code, user_name, user_password, user_gender, user_position, user_photo_url, user_qq, user_msn, user_mobile, user_mobile2, user_office_tel, user_family_tel, user_email, user_avidate, user_is_agent, user_belongto_org, memo, status, creator, create_date, user_address, user_name_py) FROM stdin;
12	wangj	王娟	UlFQV1ZV	1		D:\\tomcat-7-1\\webapps\\bhtsys\\uploadFile\\img\\D:\\bhtsys_file_dir\\uploadFile\\img\\D:\\bhtsys_file_dir\\uploadFile\\img\\8cWQ48_1_jacbo.jpg			134444444444444450					2012-06-21	\N	1		enable	admin	2011-12-21 10:09:20		wj
13	shijh	石纪红	UlFQV1ZV	1		D:\\tomcat-7-1\\webapps\\bhtsys\\uploadFile\\img\\D:\\bhtsys_file_dir\\uploadFile\\img\\D:\\bhtsys_file_dir\\uploadFile\\img\\D:\\bhtsys_file_dir\\uploadFile\\img\\								2012-07-10	\N	11		enable	admin	2012-01-10 14:55:24		sjh
14	yangwj	杨文菊	UlFQV1ZV	1		D:\\tomcat-7-1\\webapps\\bhtsys\\uploadFile\\img\\								2012-08-28	\N	11		enable	admin	2012-02-28 15:23:30		ywj
16	zhangsp	张世鹏	UlFQV1ZV	0										2012-09-06	\N	2		enable	admin	2012-03-06 15:15:39.062		zsp
17	zhangl	张丽	UlFQV1ZV	1										2012-09-06	\N	6		enable	admin	2012-03-06 15:16:22.375		zl
18	gaoxf	高晓峰	UlFQV1ZV	0										2012-09-06	\N	6		enable	admin	2012-03-06 15:21:32.765		gxf
20	wangxm	王帅明	UlFQV1ZV	0										2012-09-06	\N	6		enable	admin	2012-03-06 15:23:08.875		wsm
19	wangx	王旭	UlFQV1ZV	0										2012-09-06	\N	6		enable	admin	2012-03-06 15:21:56.578		wx
15	yuzp	于志平	UlFQV1ZV	0										2012-09-06	\N	7		enable	admin	2012-03-06 15:15:08.953		yzp
0	admin	管理员	UlFQV1ZV	\N	\N	http://localhost:8080/mycuckoo/upload/photo/20141030_221345_sltpUw.jpg	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	\N	enable	admin	2011-02-11 16:57:01.187	\N	gly
21	zhangj	张军		0										2012-09-06	\N	6		enable	admin	2012-03-06 15:23:39.75		zj
\.


--
-- Name: uum_user_seq; Type: SEQUENCE SET; Schema: cuckoo; Owner: postgres
--

SELECT pg_catalog.setval('uum_user_seq', 28, true);


--
-- Name: pk_portal; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY portal
    ADD CONSTRAINT pk_portal PRIMARY KEY (portal_id);


--
-- Name: pk_syspl_accessory; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_accessory
    ADD CONSTRAINT pk_syspl_accessory PRIMARY KEY (accessory_id);


--
-- Name: pk_syspl_affiche; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_affiche
    ADD CONSTRAINT pk_syspl_affiche PRIMARY KEY (affiche_id);


--
-- Name: pk_syspl_code; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_code
    ADD CONSTRAINT pk_syspl_code PRIMARY KEY (code_id);


--
-- Name: pk_syspl_dic_big_type; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_dic_big_type
    ADD CONSTRAINT pk_syspl_dic_big_type PRIMARY KEY (big_type_id);


--
-- Name: pk_syspl_district; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_district
    ADD CONSTRAINT pk_syspl_district PRIMARY KEY (district_id);


--
-- Name: pk_syspl_mod_opt_ref; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_mod_opt_ref
    ADD CONSTRAINT pk_syspl_mod_opt_ref PRIMARY KEY (mod_opt_id);


--
-- Name: pk_syspl_module_memu; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_module_memu
    ADD CONSTRAINT pk_syspl_module_memu PRIMARY KEY (module_id);


--
-- Name: pk_syspl_operate; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_operate
    ADD CONSTRAINT pk_syspl_operate PRIMARY KEY (operate_id);


--
-- Name: pk_syspl_scheduler_job; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_scheduler_job
    ADD CONSTRAINT pk_syspl_scheduler_job PRIMARY KEY (job_id);


--
-- Name: pk_syspl_sys_opt_log; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_sys_opt_log
    ADD CONSTRAINT pk_syspl_sys_opt_log PRIMARY KEY (opt_id);


--
-- Name: pk_syspl_sys_parameter; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_sys_parameter
    ADD CONSTRAINT pk_syspl_sys_parameter PRIMARY KEY (para_id);


--
-- Name: pk_syspl_type_dictionary; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY syspl_dic_small_type
    ADD CONSTRAINT pk_syspl_type_dictionary PRIMARY KEY (small_type_id);


--
-- Name: pk_uum_org_role_ref; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY uum_org_role_ref
    ADD CONSTRAINT pk_uum_org_role_ref PRIMARY KEY (org_role_id);


--
-- Name: pk_uum_organ; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY uum_organ
    ADD CONSTRAINT pk_uum_organ PRIMARY KEY (org_id);


--
-- Name: pk_uum_privilege; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY uum_privilege
    ADD CONSTRAINT pk_uum_privilege PRIMARY KEY (privilege_id);


--
-- Name: pk_uum_role; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY uum_role
    ADD CONSTRAINT pk_uum_role PRIMARY KEY (role_id);


--
-- Name: pk_uum_role_user_ref; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY uum_role_user_ref
    ADD CONSTRAINT pk_uum_role_user_ref PRIMARY KEY (org_role_user_id);


--
-- Name: pk_uum_user; Type: CONSTRAINT; Schema: cuckoo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY uum_user
    ADD CONSTRAINT pk_uum_user PRIMARY KEY (user_id);


--
-- Name: big_type_id_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX big_type_id_index ON syspl_dic_small_type USING btree (big_type_id);


--
-- Name: district_name_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX district_name_index ON syspl_district USING btree (district_name);


--
-- Name: mod_name_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX mod_name_index ON syspl_module_memu USING btree (mod_name);


--
-- Name: module_id_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX module_id_index ON syspl_mod_opt_ref USING btree (module_id);


--
-- Name: opt_mod_name_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX opt_mod_name_index ON syspl_sys_opt_log USING btree (opt_mod_name);


--
-- Name: opt_time_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX opt_time_index ON syspl_sys_opt_log USING btree (opt_time);


--
-- Name: opt_user_name_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX opt_user_name_index ON syspl_sys_opt_log USING btree (opt_user_name);


--
-- Name: opt_user_ogan_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX opt_user_ogan_index ON syspl_sys_opt_log USING btree (opt_user_ogan);


--
-- Name: opt_user_role_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX opt_user_role_index ON syspl_sys_opt_log USING btree (opt_user_role);


--
-- Name: org_simple_name_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX org_simple_name_index ON uum_organ USING btree (org_simple_name);


--
-- Name: owner_type_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX owner_type_index ON uum_privilege USING btree (owner_type);


--
-- Name: para_key_name_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX para_key_name_index ON syspl_sys_parameter USING btree (para_key_name);


--
-- Name: privilege_type_index; Type: INDEX; Schema: cuckoo; Owner: postgres; Tablespace: 
--

CREATE INDEX privilege_type_index ON uum_privilege USING btree (privilege_type);


--
-- Name: fk_syspl_di_reference_syspl_di; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY syspl_district
    ADD CONSTRAINT fk_syspl_di_reference_syspl_di FOREIGN KEY (dis_parent_id) REFERENCES syspl_district(district_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_syspl_mo_reference_syspl_memu; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY syspl_module_memu
    ADD CONSTRAINT fk_syspl_mo_reference_syspl_memu FOREIGN KEY (mod_parent_id) REFERENCES syspl_module_memu(module_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_syspl_mo_reference_syspl_mo; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY syspl_mod_opt_ref
    ADD CONSTRAINT fk_syspl_mo_reference_syspl_mo FOREIGN KEY (module_id) REFERENCES syspl_module_memu(module_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_syspl_mo_reference_syspl_op; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY syspl_mod_opt_ref
    ADD CONSTRAINT fk_syspl_mo_reference_syspl_op FOREIGN KEY (operate_id) REFERENCES syspl_operate(operate_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_syspl_ty_reference_syspl_di; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY syspl_dic_small_type
    ADD CONSTRAINT fk_syspl_ty_reference_syspl_di FOREIGN KEY (big_type_id) REFERENCES syspl_dic_big_type(big_type_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_uum_org__reference_uum_org_role; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY uum_org_role_ref
    ADD CONSTRAINT fk_uum_org__reference_uum_org_role FOREIGN KEY (org_id) REFERENCES uum_organ(org_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_uum_org__reference_uum_role; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY uum_org_role_ref
    ADD CONSTRAINT fk_uum_org__reference_uum_role FOREIGN KEY (role_id) REFERENCES uum_role(role_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_uum_orga_reference_uum_orga; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY uum_organ
    ADD CONSTRAINT fk_uum_orga_reference_uum_orga FOREIGN KEY (org_parent) REFERENCES uum_organ(org_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_uum_role_reference_uum_org_; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY uum_role_user_ref
    ADD CONSTRAINT fk_uum_role_reference_uum_org_ FOREIGN KEY (org_role_id) REFERENCES uum_org_role_ref(org_role_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_uum_role_reference_uum_user; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY uum_role_user_ref
    ADD CONSTRAINT fk_uum_role_reference_uum_user FOREIGN KEY (user_id) REFERENCES uum_user(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_uum_user_reference_uum_orga; Type: FK CONSTRAINT; Schema: cuckoo; Owner: postgres
--

ALTER TABLE ONLY uum_user
    ADD CONSTRAINT fk_uum_user_reference_uum_orga FOREIGN KEY (user_belongto_org) REFERENCES uum_organ(org_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: cuckoo; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA cuckoo FROM PUBLIC;
REVOKE ALL ON SCHEMA cuckoo FROM postgres;
GRANT ALL ON SCHEMA cuckoo TO postgres;
GRANT ALL ON SCHEMA cuckoo TO PUBLIC;


--
-- PostgreSQL database dump complete
--

