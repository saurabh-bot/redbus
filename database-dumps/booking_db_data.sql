--
-- PostgreSQL database dump
--

\restrict dNtIgUMaL5uylm8yivEZ2ejqSMdGREOdDSWBSHON8tCDEp0fLV2eiVfCL2IUCSN

-- Dumped from database version 15.14 (Homebrew)
-- Dumped by pg_dump version 15.14 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: bookings; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.bookings VALUES (1, 100, NULL, 'INITIATED', 1200, 1762111841, 1762111841, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103ZTVPPH');
INSERT INTO public.bookings VALUES (2, 100, NULL, 'INITIATED', 1200, 1762111847, 1762111847, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103R7C40H');
INSERT INTO public.bookings VALUES (3, 100, 'TXN123456789', 'COMPLETED', 1200, 1762111873, 1762112268, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103ELKY7I');
INSERT INTO public.bookings VALUES (5, 100, NULL, 'INITIATED', 1200, 1762115717, 1762115717, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS2025110384FLQ0');
INSERT INTO public.bookings VALUES (6, 100, NULL, 'INITIATED', 1200, 1762115885, 1762115885, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103ZMUQ1S');
INSERT INTO public.bookings VALUES (7, 100, NULL, 'INITIATED', 1200, 1762116891, 1762116891, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103NX3Q5B');
INSERT INTO public.bookings VALUES (8, 100, NULL, 'INITIATED', 1200, 1762117102, 1762117102, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103GW8NCV');
INSERT INTO public.bookings VALUES (9, 100, 'PAYMENT123456', 'COMPLETED', 1200, 1762117136, 1762117538, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103OWDNLW');
INSERT INTO public.bookings VALUES (10, 100, NULL, 'INITIATED', 1200, 1762118051, 1762118051, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS202511035RK5GG');
INSERT INTO public.bookings VALUES (11, 100, NULL, 'INITIATED', 1200, 1762118098, 1762118098, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS202511031DADP4');
INSERT INTO public.bookings VALUES (12, 100, NULL, 'INITIATED', 1200, 1762118205, 1762118205, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103OW3GQS');
INSERT INTO public.bookings VALUES (13, 100, 'PAYMENT123456', 'COMPLETED', 1200, 1762118225, 1762118225, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103P5IDL0');
INSERT INTO public.bookings VALUES (14, 100, NULL, 'INITIATED', 1200, 1762118234, 1762118234, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103XYNWJQ');
INSERT INTO public.bookings VALUES (15, 100, NULL, 'INITIATED', 1500, 1762118304, 1762118304, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103FTR75E');
INSERT INTO public.bookings VALUES (16, 100, 'PAYMENT_TEST_789', 'COMPLETED', 1500, 1762118378, 1762118379, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103X8X92L');
INSERT INTO public.bookings VALUES (17, 100, 'PAYMENT123456', 'COMPLETED', 1200, 1762119008, 1762119038, '{"trip_id": "17", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS20251103UYYBJT');
INSERT INTO public.bookings VALUES (18, 100, 'PAY123', 'COMPLETED', 1500, 1762119730, 1762119730, '{"trip_id": "16", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T02:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Jaipur", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS20251103EIPHDD');
INSERT INTO public.bookings VALUES (19, 100, 'PAY789', 'CANCELLED', 1500, 1762119819, 1762119862, '{"trip_id": "16", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T02:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Jaipur", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS20251103RLO4D1');
INSERT INTO public.bookings VALUES (4, 100, 'TXN123456789Z', 'CANCELLED', 1200, 1762112425, 1762120140, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103LURXCC');
INSERT INTO public.bookings VALUES (20, 100, 'PAY_TEST', 'CANCELLED', 1500, 1762120207, 1762120207, '{"trip_id": "16", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T02:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Jaipur", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS2025110374D3DI');
INSERT INTO public.bookings VALUES (21, 100, NULL, 'INITIATED', 1200, 1762120341, 1762120341, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103O1194S');
INSERT INTO public.bookings VALUES (22, 100, 'TXN123456789Z', 'CANCELLED', 1200, 1762120344, 1762120382, '{"trip_id": "1", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 5, "booked_from_stop_sequence": 1}', 'REDBUS20251103MIF9AH');
INSERT INTO public.bookings VALUES (23, 100, 'PAY_TEST', 'CANCELLED', 1500, 1762120804, 1762120804, '{"trip_id": "16", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T02:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Jaipur", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS20251103GMSVQ6');
INSERT INTO public.bookings VALUES (24, 100, 'PAYMENT1234561', 'CANCELLED', 1200, 1762121022, 1762121071, '{"trip_id": "17", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS202511036139FX');
INSERT INTO public.bookings VALUES (25, 100, NULL, 'INITIATED', 1500, 1762121215, 1762121215, '{"trip_id": "16", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T02:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Jaipur", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS20251103JKJU15');
INSERT INTO public.bookings VALUES (26, 100, 'PAYMENT1234561', 'COMPLETED', 1200, 1762122431, 1762122444, '{"trip_id": "17", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS20251103H4N1R0');
INSERT INTO public.bookings VALUES (27, 100, 'PAYMENT1234561', 'CANCELLED', 1200, 1762122643, 1762122657, '{"trip_id": "17", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS202511036KDSU8');
INSERT INTO public.bookings VALUES (28, 100, 'PAYMENT1234561', 'CANCELLED', 1200, 1762122954, 1762122982, '{"trip_id": "17", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS20251103XFLL5L');
INSERT INTO public.bookings VALUES (29, 100, 'PAYMENT1234561', 'CANCELLED', 1200, 1762123001, 1762124927, '{"trip_id": "17", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS20251103RLC9QD');
INSERT INTO public.bookings VALUES (30, 100, 'PAYMENT1234561', 'CANCELLED', 1200, 1762124940, 1762124958, '{"trip_id": "17", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Ayodhya", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS202511033B2YE3');
INSERT INTO public.bookings VALUES (31, 100, 'PAYMENT1234561UA', 'CANCELLED', 1200, 1762126391, 1762126917, '{"trip_id": "17", "boarding_datetime": "2025-11-05T22:00:00", "dropping_datetime": "2025-11-06T10:00:00", "boarding_city_name": "Delhi", "dropping_city_name": "Jaipur", "booked_to_stop_sequence": 3, "booked_from_stop_sequence": 1}', 'REDBUS20251103LYY7G4');


--
-- Data for Name: booking_passengers; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.booking_passengers VALUES (1, 1, 'John Doe', 'john.doe@example.com', '9876543210', 'MALE', 30, 'A1', 1762111841, 1762111841);
INSERT INTO public.booking_passengers VALUES (2, 1, 'Jane Doe', 'jane.doe@example.com', '9876543211', 'FEMALE', 28, 'A2', 1762111841, 1762111841);
INSERT INTO public.booking_passengers VALUES (3, 2, 'John Doe', 'john.doe@example.com', '9876543210', 'MALE', 30, 'A1', 1762111847, 1762111847);
INSERT INTO public.booking_passengers VALUES (4, 2, 'Jane Doe', 'jane.doe@example.com', '9876543211', 'FEMALE', 28, 'A2', 1762111847, 1762111847);
INSERT INTO public.booking_passengers VALUES (5, 3, 'John Doe', 'john.doe@example.com', '9876543210', 'MALE', 30, 'A1', 1762111873, 1762111873);
INSERT INTO public.booking_passengers VALUES (6, 3, 'Jane Doe', 'jane.doe@example.com', '9876543211', 'FEMALE', 28, 'A2', 1762111873, 1762111873);
INSERT INTO public.booking_passengers VALUES (7, 4, 'John Doe', 'john.doe@example.com', '9876543210', 'MALE', 30, 'A1', 1762112425, 1762112425);
INSERT INTO public.booking_passengers VALUES (8, 4, 'Jane Doe', 'jane.doe@example.com', '9876543211', 'FEMALE', 28, 'A2', 1762112425, 1762112425);
INSERT INTO public.booking_passengers VALUES (9, 5, 'Test User', 'test@example.com', '9876543210', 'MALE', 30, 'A1', 1762115717, 1762115717);
INSERT INTO public.booking_passengers VALUES (10, 6, 'John Doe', NULL, NULL, 'MALE', 30, 'A1', 1762115885, 1762115885);
INSERT INTO public.booking_passengers VALUES (11, 7, 'Test User', 'test@example.com', '9876543210', 'MALE', 30, 'B3', 1762116891, 1762116891);
INSERT INTO public.booking_passengers VALUES (12, 8, 'Test User', 'test@example.com', '9876543210', 'MALE', 30, 'B4', 1762117102, 1762117102);
INSERT INTO public.booking_passengers VALUES (13, 9, 'Test User', 'test@example.com', '9876543210', 'MALE', 30, 'B5', 1762117136, 1762117136);
INSERT INTO public.booking_passengers VALUES (14, 10, 'Test User', 'test@example.com', '9876543210', 'MALE', 30, 'B6', 1762118051, 1762118051);
INSERT INTO public.booking_passengers VALUES (15, 11, 'Test User', NULL, NULL, 'MALE', 30, 'B7', 1762118098, 1762118098);
INSERT INTO public.booking_passengers VALUES (16, 12, 'Test', NULL, NULL, NULL, NULL, 'B8', 1762118205, 1762118205);
INSERT INTO public.booking_passengers VALUES (17, 13, 'Test User', NULL, NULL, NULL, 30, 'B9', 1762118225, 1762118225);
INSERT INTO public.booking_passengers VALUES (18, 14, 'John Doe', 'john.doe@example.com', '9876543210', 'MALE', 30, 'A1', 1762118234, 1762118234);
INSERT INTO public.booking_passengers VALUES (19, 14, 'Jane Doe', 'jane.doe@example.com', '9876543211', 'FEMALE', 28, 'A2', 1762118234, 1762118234);
INSERT INTO public.booking_passengers VALUES (20, 15, 'Gateway Test User', 'gatewaytest@example.com', '9876543210', 'MALE', 35, 'C1', 1762118304, 1762118304);
INSERT INTO public.booking_passengers VALUES (21, 16, 'Test User', NULL, NULL, 'MALE', 35, 'C2', 1762118378, 1762118378);
INSERT INTO public.booking_passengers VALUES (22, 17, 'John Doe', NULL, NULL, 'MALE', 30, 'A1', 1762119008, 1762119008);
INSERT INTO public.booking_passengers VALUES (23, 18, 'Test User', NULL, NULL, NULL, 30, 'A1', 1762119730, 1762119730);
INSERT INTO public.booking_passengers VALUES (24, 19, 'Test User', NULL, NULL, NULL, 30, 'A2', 1762119819, 1762119819);
INSERT INTO public.booking_passengers VALUES (25, 20, 'Test User', NULL, NULL, NULL, 30, 'A3', 1762120207, 1762120207);
INSERT INTO public.booking_passengers VALUES (26, 21, 'John Doe', 'john.doe@example.com', '9876543210', 'MALE', 30, 'A1', 1762120341, 1762120341);
INSERT INTO public.booking_passengers VALUES (27, 21, 'Jane Doe', 'jane.doe@example.com', '9876543211', 'FEMALE', 28, 'A2', 1762120341, 1762120341);
INSERT INTO public.booking_passengers VALUES (28, 22, 'John Doe', 'john.doe@example.com', '9876543210', 'MALE', 30, 'A1', 1762120344, 1762120344);
INSERT INTO public.booking_passengers VALUES (29, 22, 'Jane Doe', 'jane.doe@example.com', '9876543211', 'FEMALE', 28, 'A2', 1762120344, 1762120344);
INSERT INTO public.booking_passengers VALUES (30, 23, 'Test User', NULL, NULL, NULL, 30, 'A4', 1762120804, 1762120804);
INSERT INTO public.booking_passengers VALUES (31, 24, 'John Doe', NULL, NULL, 'MALE', 30, 'A2', 1762121022, 1762121022);
INSERT INTO public.booking_passengers VALUES (32, 25, 'Test User', NULL, NULL, NULL, 30, 'B1', 1762121215, 1762121215);
INSERT INTO public.booking_passengers VALUES (33, 26, 'John Doe', NULL, NULL, 'MALE', 30, 'A2', 1762122431, 1762122431);
INSERT INTO public.booking_passengers VALUES (34, 27, 'John Doe', NULL, NULL, 'MALE', 30, 'B1', 1762122643, 1762122643);
INSERT INTO public.booking_passengers VALUES (35, 28, 'John Doe', NULL, NULL, 'MALE', 30, 'B1', 1762122954, 1762122954);
INSERT INTO public.booking_passengers VALUES (36, 29, 'John Doe', NULL, NULL, 'MALE', 30, 'B1', 1762123001, 1762123001);
INSERT INTO public.booking_passengers VALUES (37, 30, 'John Doe', NULL, NULL, 'MALE', 30, 'B1', 1762124940, 1762124940);
INSERT INTO public.booking_passengers VALUES (38, 31, 'John Doe', NULL, NULL, 'MALE', 30, 'A1', 1762126391, 1762126391);


--
-- Data for Name: cancellations; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--



--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.flyway_schema_history VALUES (1, '1', 'Create booking tables', 'SQL', 'V1__Create_booking_tables.sql', -56940231, 'saurabh.kaushal', '2025-11-02 08:44:41.541782', 51, true);
INSERT INTO public.flyway_schema_history VALUES (2, '2', 'Simplify bookings table', 'SQL', 'V2__Simplify_bookings_table.sql', 28474140, 'saurabh.kaushal', '2025-11-03 00:58:50.296254', 49, true);
INSERT INTO public.flyway_schema_history VALUES (3, '3', 'Add booking reference back', 'SQL', 'V3__Add_booking_reference_back.sql', 1613352915, 'saurabh.kaushal', '2025-11-03 00:58:50.385939', 37, true);
INSERT INTO public.flyway_schema_history VALUES (4, '4', 'Remove unused indices', 'SQL', 'V4__Remove_unused_indices.sql', -254168902, 'saurabh.kaushal', '2025-11-03 04:35:21.877774', 10, true);


--
-- Name: booking_passengers_passenger_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.booking_passengers_passenger_id_seq', 38, true);


--
-- Name: bookings_booking_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.bookings_booking_id_seq', 31, true);


--
-- Name: cancellations_cancellation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.cancellations_cancellation_id_seq', 1, false);


--
-- PostgreSQL database dump complete
--

\unrestrict dNtIgUMaL5uylm8yivEZ2ejqSMdGREOdDSWBSHON8tCDEp0fLV2eiVfCL2IUCSN

