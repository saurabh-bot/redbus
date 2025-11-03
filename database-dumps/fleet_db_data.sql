--
-- PostgreSQL database dump
--

\restrict tKDSPUezWy8LnZ0BGwDbfwCwIetL8RzTmAUnh3QflGaOLFnFg08mYsuxw0j6zHh

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
-- Data for Name: buses; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.buses VALUES (1, 1, 'DL-01-TEST', 'SLEEPER', true, true, '{"wifi": true, "charging_point": true}', 40, 1762058423, 1762074114);
INSERT INTO public.buses VALUES (2, 1, 'DL-01-DEL-AYO', 'SLEEPER', true, true, '{"wifi": true, "water": true, "blanket": true, "live_tracking": true, "charging_point": true}', 40, 1762058427, 1762074115);
INSERT INTO public.buses VALUES (3, 1, 'HR-02-NONAC-SEATER', 'SEATER', true, false, '{"wifi": false, "water": true, "blanket": false, "charging_point": false}', 40, 1762085527, 1762085527);


--
-- Data for Name: cities; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.cities VALUES (1, 'Bangalore', 'Karnataka', 'India', 1762053403, 1762053403, 'BAN');
INSERT INTO public.cities VALUES (2, 'New Delhi', 'Delhi', 'India', 1762053870, 1762053951, 'NEW');
INSERT INTO public.cities VALUES (3, 'Pune', 'Maharashtra', 'India', 1762054471, 1762054471, 'PUNE');
INSERT INTO public.cities VALUES (4, 'Hyderabad', 'Telangana', 'India', 1762054571, 1762054751, 'HYDER');
INSERT INTO public.cities VALUES (5, 'Delhi', 'Delhi', 'India', 1762056050, 1762056050, 'DELHI');
INSERT INTO public.cities VALUES (6, 'Ajmer', 'Rajasthan', 'India', 1762056050, 1762056050, 'AJMER');
INSERT INTO public.cities VALUES (7, 'Jaipur', 'Rajasthan', 'India', 1762056050, 1762056050, 'JAIPR');
INSERT INTO public.cities VALUES (8, 'Kanpur', 'Uttar Pradesh', 'India', 1762056050, 1762056050, 'KANPU');
INSERT INTO public.cities VALUES (9, 'Lucknow', 'Uttar Pradesh', 'India', 1762056050, 1762056050, 'LUCKN');
INSERT INTO public.cities VALUES (10, 'Ayodhya', 'Uttar Pradesh', 'India', 1762056050, 1762056050, 'AYODH');
INSERT INTO public.cities VALUES (11, 'Hyderabads', 'Telangana', 'India', 1762071087, 1762071087, 'HYDER1');


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.flyway_schema_history VALUES (1, '1', 'Create fleet tables', 'SQL', 'V1__Create_fleet_tables.sql', -1809866678, 'saurabh.kaushal', '2025-11-02 08:44:41.662832', 142, true);
INSERT INTO public.flyway_schema_history VALUES (2, '2', 'Add city code', 'SQL', 'V2__Add_city_code.sql', -1422446698, 'saurabh.kaushal', '2025-11-02 09:02:45.021988', 80, true);
INSERT INTO public.flyway_schema_history VALUES (3, '3', 'Remove booking id from seat bookings', 'SQL', 'V3__Remove_booking_id_from_seat_bookings.sql', -421050444, 'saurabh.kaushal', '2025-11-02 22:40:07.885397', 27, true);
INSERT INTO public.flyway_schema_history VALUES (4, '4', 'Rename seat bookings to seat reservations', 'SQL', 'V4__Rename_seat_bookings_to_seat_reservations.sql', -390961531, 'saurabh.kaushal', '2025-11-02 22:40:07.971698', 20, true);
INSERT INTO public.flyway_schema_history VALUES (5, '5', 'Remove status from seat reservations', 'SQL', 'V5__Remove_status_from_seat_reservations.sql', -2000641004, 'saurabh.kaushal', '2025-11-03 00:14:03.674182', 26, true);
INSERT INTO public.flyway_schema_history VALUES (6, '6', 'Add status to seat reservations', 'SQL', 'V6__Add_status_to_seat_reservations.sql', -30188864, 'saurabh.kaushal', '2025-11-03 03:11:57.227781', 112, true);


--
-- Data for Name: routes; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.routes VALUES (1, 2, 5, 10, true, 850, 960, 1762058427, 1762058427);
INSERT INTO public.routes VALUES (2, 3, 5, 10, true, 1180, 1020, 1762085529, 1762085529);
INSERT INTO public.routes VALUES (3, 3, 5, 10, true, 1180, 1020, 1762113297, 1762113297);


--
-- Data for Name: route_stops; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.route_stops VALUES (1, 1, 5, 1, true, false, NULL, 0, 0, 1762058427, 1762058427);
INSERT INTO public.route_stops VALUES (2, 1, 6, 2, true, true, 180, 195, 200, 1762058427, 1762058427);
INSERT INTO public.route_stops VALUES (3, 1, 7, 3, true, true, 270, 285, 350, 1762058427, 1762058427);
INSERT INTO public.route_stops VALUES (4, 1, 8, 4, true, true, 600, 615, 550, 1762058428, 1762058428);
INSERT INTO public.route_stops VALUES (5, 1, 9, 5, true, true, 750, 765, 700, 1762058428, 1762058428);
INSERT INTO public.route_stops VALUES (6, 1, 10, 6, false, true, 900, NULL, 850, 1762058428, 1762058428);
INSERT INTO public.route_stops VALUES (7, 2, 5, 1, true, false, NULL, 0, 0, 1762085530, 1762085530);
INSERT INTO public.route_stops VALUES (8, 2, 6, 2, true, true, 270, 285, 200, 1762085531, 1762085531);
INSERT INTO public.route_stops VALUES (9, 2, 7, 3, true, true, 390, 405, 350, 1762085531, 1762085531);
INSERT INTO public.route_stops VALUES (10, 2, 8, 4, true, true, 960, 975, 750, 1762085532, 1762085532);
INSERT INTO public.route_stops VALUES (11, 2, 10, 5, false, true, 1020, NULL, 850, 1762085533, 1762085533);
INSERT INTO public.route_stops VALUES (12, 3, 5, 1, true, true, 0, 0, 0, 1762113297, 1762113297);
INSERT INTO public.route_stops VALUES (13, 3, 6, 2, true, true, 270, 285, 200, 1762113297, 1762113297);
INSERT INTO public.route_stops VALUES (14, 3, 7, 3, true, true, 390, 405, 350, 1762113297, 1762113297);
INSERT INTO public.route_stops VALUES (15, 3, 8, 4, true, true, 960, 975, 750, 1762113297, 1762113297);
INSERT INTO public.route_stops VALUES (16, 3, 10, 5, true, true, 1020, 0, 850, 1762113298, 1762113298);


--
-- Data for Name: seat_layouts; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.seat_layouts VALUES (44, 1, 'A1', 'WINDOW', 'LOWER_DECK', 1, 1, 60, 40, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (45, 1, 'A2', 'AISLE', 'LOWER_DECK', 1, 2, 120, 40, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (46, 1, 'B1', 'WINDOW', 'LOWER_DECK', 2, 1, 60, 80, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (47, 1, 'B2', 'AISLE', 'LOWER_DECK', 2, 2, 120, 80, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (48, 1, 'C1', 'WINDOW', 'LOWER_DECK', 3, 1, 60, 120, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (49, 1, 'C2', 'AISLE', 'LOWER_DECK', 3, 2, 120, 120, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (50, 1, 'D1', 'WINDOW', 'LOWER_DECK', 4, 1, 60, 160, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (51, 1, 'D2', 'AISLE', 'LOWER_DECK', 4, 2, 120, 160, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (52, 1, 'E1', 'WINDOW', 'LOWER_DECK', 5, 1, 60, 200, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (53, 1, 'E2', 'AISLE', 'LOWER_DECK', 5, 2, 120, 200, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (54, 1, 'F1', 'WINDOW', 'LOWER_DECK', 6, 1, 60, 240, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (55, 1, 'F2', 'AISLE', 'LOWER_DECK', 6, 2, 120, 240, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (56, 1, 'G1', 'WINDOW', 'LOWER_DECK', 7, 1, 60, 280, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (57, 1, 'G2', 'AISLE', 'LOWER_DECK', 7, 2, 120, 280, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (58, 1, 'H1', 'WINDOW', 'LOWER_DECK', 8, 1, 60, 320, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (59, 1, 'H2', 'AISLE', 'LOWER_DECK', 8, 2, 120, 320, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (60, 1, 'I1', 'WINDOW', 'LOWER_DECK', 9, 1, 60, 360, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (61, 1, 'I2', 'AISLE', 'LOWER_DECK', 9, 2, 120, 360, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (62, 1, 'J1', 'WINDOW', 'LOWER_DECK', 10, 1, 60, 400, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (63, 1, 'J2', 'AISLE', 'LOWER_DECK', 10, 2, 120, 400, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (64, 1, 'K1', 'WINDOW', 'UPPER_DECK', 11, 1, 60, 440, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (65, 1, 'K2', 'AISLE', 'UPPER_DECK', 11, 2, 120, 440, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (66, 1, 'L1', 'WINDOW', 'UPPER_DECK', 12, 1, 60, 480, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (67, 1, 'L2', 'AISLE', 'UPPER_DECK', 12, 2, 120, 480, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (68, 1, 'M1', 'WINDOW', 'UPPER_DECK', 13, 1, 60, 520, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (69, 1, 'M2', 'AISLE', 'UPPER_DECK', 13, 2, 120, 520, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (70, 1, 'N1', 'WINDOW', 'UPPER_DECK', 14, 1, 60, 560, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (71, 1, 'N2', 'AISLE', 'UPPER_DECK', 14, 2, 120, 560, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (72, 1, 'O1', 'WINDOW', 'UPPER_DECK', 15, 1, 60, 600, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (73, 1, 'O2', 'AISLE', 'UPPER_DECK', 15, 2, 120, 600, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (74, 1, 'P1', 'WINDOW', 'UPPER_DECK', 16, 1, 60, 640, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (75, 1, 'P2', 'AISLE', 'UPPER_DECK', 16, 2, 120, 640, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (76, 1, 'Q1', 'WINDOW', 'UPPER_DECK', 17, 1, 60, 680, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (77, 1, 'Q2', 'AISLE', 'UPPER_DECK', 17, 2, 120, 680, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (78, 1, 'R1', 'WINDOW', 'UPPER_DECK', 18, 1, 60, 720, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (79, 1, 'R2', 'AISLE', 'UPPER_DECK', 18, 2, 120, 720, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (80, 1, 'S1', 'WINDOW', 'UPPER_DECK', 19, 1, 60, 760, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (81, 1, 'S2', 'AISLE', 'UPPER_DECK', 19, 2, 120, 760, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (82, 1, 'T1', 'WINDOW', 'UPPER_DECK', 20, 1, 60, 800, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (83, 1, 'T2', 'AISLE', 'UPPER_DECK', 20, 2, 120, 800, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (84, 2, 'A1', 'WINDOW', 'LOWER_DECK', 1, 1, 60, 40, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (85, 2, 'A2', 'AISLE', 'LOWER_DECK', 1, 2, 120, 40, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (86, 2, 'B1', 'WINDOW', 'LOWER_DECK', 2, 1, 60, 80, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (87, 2, 'B2', 'AISLE', 'LOWER_DECK', 2, 2, 120, 80, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (88, 2, 'C1', 'WINDOW', 'LOWER_DECK', 3, 1, 60, 120, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (89, 2, 'C2', 'AISLE', 'LOWER_DECK', 3, 2, 120, 120, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (90, 2, 'D1', 'WINDOW', 'LOWER_DECK', 4, 1, 60, 160, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (91, 2, 'D2', 'AISLE', 'LOWER_DECK', 4, 2, 120, 160, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (92, 2, 'E1', 'WINDOW', 'LOWER_DECK', 5, 1, 60, 200, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (93, 2, 'E2', 'AISLE', 'LOWER_DECK', 5, 2, 120, 200, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (94, 2, 'F1', 'WINDOW', 'LOWER_DECK', 6, 1, 60, 240, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (95, 2, 'F2', 'AISLE', 'LOWER_DECK', 6, 2, 120, 240, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (96, 2, 'G1', 'WINDOW', 'LOWER_DECK', 7, 1, 60, 280, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (97, 2, 'G2', 'AISLE', 'LOWER_DECK', 7, 2, 120, 280, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (98, 2, 'H1', 'WINDOW', 'LOWER_DECK', 8, 1, 60, 320, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (99, 2, 'H2', 'AISLE', 'LOWER_DECK', 8, 2, 120, 320, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (100, 2, 'I1', 'WINDOW', 'LOWER_DECK', 9, 1, 60, 360, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (101, 2, 'I2', 'AISLE', 'LOWER_DECK', 9, 2, 120, 360, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (102, 2, 'J1', 'WINDOW', 'LOWER_DECK', 10, 1, 60, 400, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (103, 2, 'J2', 'AISLE', 'LOWER_DECK', 10, 2, 120, 400, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (104, 2, 'K1', 'WINDOW', 'UPPER_DECK', 11, 1, 60, 440, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (105, 2, 'K2', 'AISLE', 'UPPER_DECK', 11, 2, 120, 440, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (106, 2, 'L1', 'WINDOW', 'UPPER_DECK', 12, 1, 60, 480, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (107, 2, 'L2', 'AISLE', 'UPPER_DECK', 12, 2, 120, 480, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (108, 2, 'M1', 'WINDOW', 'UPPER_DECK', 13, 1, 60, 520, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (109, 2, 'M2', 'AISLE', 'UPPER_DECK', 13, 2, 120, 520, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (110, 2, 'N1', 'WINDOW', 'UPPER_DECK', 14, 1, 60, 560, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (111, 2, 'N2', 'AISLE', 'UPPER_DECK', 14, 2, 120, 560, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (112, 2, 'O1', 'WINDOW', 'UPPER_DECK', 15, 1, 60, 600, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (113, 2, 'O2', 'AISLE', 'UPPER_DECK', 15, 2, 120, 600, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (114, 2, 'P1', 'WINDOW', 'UPPER_DECK', 16, 1, 60, 640, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (115, 2, 'P2', 'AISLE', 'UPPER_DECK', 16, 2, 120, 640, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (116, 2, 'Q1', 'WINDOW', 'UPPER_DECK', 17, 1, 60, 680, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (117, 2, 'Q2', 'AISLE', 'UPPER_DECK', 17, 2, 120, 680, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (118, 2, 'R1', 'WINDOW', 'UPPER_DECK', 18, 1, 60, 720, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (119, 2, 'R2', 'AISLE', 'UPPER_DECK', 18, 2, 120, 720, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (120, 2, 'S1', 'WINDOW', 'UPPER_DECK', 19, 1, 60, 760, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (121, 2, 'S2', 'AISLE', 'UPPER_DECK', 19, 2, 120, 760, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (122, 2, 'T1', 'WINDOW', 'UPPER_DECK', 20, 1, 60, 800, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (123, 2, 'T2', 'AISLE', 'UPPER_DECK', 20, 2, 120, 800, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (124, 3, 'A1', 'WINDOW', 'LOWER_DECK', 1, 1, 10, 40, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (125, 3, 'A2', 'AISLE', 'LOWER_DECK', 1, 2, 70, 40, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (126, 3, 'A3', 'AISLE', 'LOWER_DECK', 1, 3, 130, 40, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (127, 3, 'A4', 'WINDOW', 'LOWER_DECK', 1, 4, 190, 40, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (128, 3, 'B1', 'WINDOW', 'LOWER_DECK', 2, 1, 10, 80, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (129, 3, 'B2', 'AISLE', 'LOWER_DECK', 2, 2, 70, 80, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (130, 3, 'B3', 'AISLE', 'LOWER_DECK', 2, 3, 130, 80, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (131, 3, 'B4', 'WINDOW', 'LOWER_DECK', 2, 4, 190, 80, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (132, 3, 'C1', 'WINDOW', 'LOWER_DECK', 3, 1, 10, 120, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (133, 3, 'C2', 'AISLE', 'LOWER_DECK', 3, 2, 70, 120, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (134, 3, 'C3', 'AISLE', 'LOWER_DECK', 3, 3, 130, 120, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (135, 3, 'C4', 'WINDOW', 'LOWER_DECK', 3, 4, 190, 120, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (136, 3, 'D1', 'WINDOW', 'LOWER_DECK', 4, 1, 10, 160, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (137, 3, 'D2', 'AISLE', 'LOWER_DECK', 4, 2, 70, 160, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (138, 3, 'D3', 'AISLE', 'LOWER_DECK', 4, 3, 130, 160, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (139, 3, 'D4', 'WINDOW', 'LOWER_DECK', 4, 4, 190, 160, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (140, 3, 'E1', 'WINDOW', 'LOWER_DECK', 5, 1, 10, 200, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (141, 3, 'E2', 'AISLE', 'LOWER_DECK', 5, 2, 70, 200, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (142, 3, 'E3', 'AISLE', 'LOWER_DECK', 5, 3, 130, 200, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (143, 3, 'E4', 'WINDOW', 'LOWER_DECK', 5, 4, 190, 200, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (144, 3, 'F1', 'WINDOW', 'LOWER_DECK', 6, 1, 10, 240, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (145, 3, 'F2', 'AISLE', 'LOWER_DECK', 6, 2, 70, 240, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (146, 3, 'F3', 'AISLE', 'LOWER_DECK', 6, 3, 130, 240, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (147, 3, 'F4', 'WINDOW', 'LOWER_DECK', 6, 4, 190, 240, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (148, 3, 'G1', 'WINDOW', 'LOWER_DECK', 7, 1, 10, 280, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (149, 3, 'G2', 'AISLE', 'LOWER_DECK', 7, 2, 70, 280, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (150, 3, 'G3', 'AISLE', 'LOWER_DECK', 7, 3, 130, 280, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (151, 3, 'G4', 'WINDOW', 'LOWER_DECK', 7, 4, 190, 280, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (152, 3, 'H1', 'WINDOW', 'LOWER_DECK', 8, 1, 10, 320, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (153, 3, 'H2', 'AISLE', 'LOWER_DECK', 8, 2, 70, 320, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (154, 3, 'H3', 'AISLE', 'LOWER_DECK', 8, 3, 130, 320, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (155, 3, 'H4', 'WINDOW', 'LOWER_DECK', 8, 4, 190, 320, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (156, 3, 'I1', 'WINDOW', 'LOWER_DECK', 9, 1, 10, 360, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (157, 3, 'I2', 'AISLE', 'LOWER_DECK', 9, 2, 70, 360, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (158, 3, 'I3', 'AISLE', 'LOWER_DECK', 9, 3, 130, 360, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (159, 3, 'I4', 'WINDOW', 'LOWER_DECK', 9, 4, 190, 360, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (160, 3, 'J1', 'WINDOW', 'LOWER_DECK', 10, 1, 10, 400, 1.2, false, NULL);
INSERT INTO public.seat_layouts VALUES (161, 3, 'J2', 'AISLE', 'LOWER_DECK', 10, 2, 70, 400, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (162, 3, 'J3', 'AISLE', 'LOWER_DECK', 10, 3, 130, 400, 1, false, NULL);
INSERT INTO public.seat_layouts VALUES (163, 3, 'J4', 'WINDOW', 'LOWER_DECK', 10, 4, 190, 400, 1.2, false, NULL);


--
-- Data for Name: trip_instances; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.trip_instances VALUES (12, 1, '2025-11-20', 'SCHEDULED', NULL, NULL, 1762076376, 1762076376);
INSERT INTO public.trip_instances VALUES (13, 1, '2025-11-25', 'SCHEDULED', NULL, NULL, 1762076801, 1762076801);
INSERT INTO public.trip_instances VALUES (14, 1, '2025-11-30', 'SCHEDULED', NULL, NULL, 1762076820, 1762076820);
INSERT INTO public.trip_instances VALUES (1, 1, '2025-11-05', 'SCHEDULED', 1762375500, 1762429500, 1762076245, 1762078029);
INSERT INTO public.trip_instances VALUES (2, 1, '2025-11-06', 'SCHEDULED', 1762432200, 1762486200, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (3, 1, '2025-11-07', 'SCHEDULED', 1762496100, 1762550100, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (4, 1, '2025-11-08', 'SCHEDULED', 1762615800, 1762669800, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (5, 1, '2025-11-09', 'SCHEDULED', 1762689600, 1762743600, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (6, 1, '2025-11-10', 'SCHEDULED', 1762806600, 1762860600, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (7, 1, '2025-11-11', 'SCHEDULED', 1762888500, 1762942500, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (8, 1, '2025-11-12', 'SCHEDULED', 1762989300, 1763043300, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (9, 1, '2025-11-13', 'SCHEDULED', 1763069400, 1763123400, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (10, 1, '2025-11-14', 'SCHEDULED', 1763142300, 1763196300, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (11, 1, '2025-11-15', 'SCHEDULED', 1763228700, 1763282700, 1762076248, 1762078029);
INSERT INTO public.trip_instances VALUES (15, 2, '2025-11-05', 'SCHEDULED', NULL, NULL, 1762085536, 1762085536);
INSERT INTO public.trip_instances VALUES (16, 2, '2025-11-06', 'SCHEDULED', NULL, NULL, 1762085536, 1762085536);
INSERT INTO public.trip_instances VALUES (17, 2, '2025-11-07', 'SCHEDULED', NULL, NULL, 1762085537, 1762085537);
INSERT INTO public.trip_instances VALUES (18, 2, '2025-11-08', 'SCHEDULED', NULL, NULL, 1762085537, 1762085537);
INSERT INTO public.trip_instances VALUES (19, 2, '2025-11-09', 'SCHEDULED', NULL, NULL, 1762085538, 1762085538);
INSERT INTO public.trip_instances VALUES (20, 2, '2025-11-10', 'SCHEDULED', NULL, NULL, 1762085538, 1762085538);
INSERT INTO public.trip_instances VALUES (21, 2, '2025-11-11', 'SCHEDULED', NULL, NULL, 1762085539, 1762085539);
INSERT INTO public.trip_instances VALUES (22, 2, '2025-11-12', 'SCHEDULED', NULL, NULL, 1762085539, 1762085539);
INSERT INTO public.trip_instances VALUES (23, 2, '2025-11-13', 'SCHEDULED', NULL, NULL, 1762085539, 1762085539);
INSERT INTO public.trip_instances VALUES (24, 2, '2025-11-14', 'SCHEDULED', NULL, NULL, 1762085540, 1762085540);
INSERT INTO public.trip_instances VALUES (25, 2, '2025-11-15', 'SCHEDULED', NULL, NULL, 1762085540, 1762085540);
INSERT INTO public.trip_instances VALUES (26, 1, '2025-11-03', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (27, 1, '2025-11-04', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (28, 1, '2025-11-16', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (29, 1, '2025-11-17', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (30, 1, '2025-11-18', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (31, 1, '2025-11-19', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (32, 1, '2025-11-21', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (33, 1, '2025-11-22', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (34, 1, '2025-11-23', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (35, 1, '2025-11-24', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (36, 1, '2025-11-26', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (37, 1, '2025-11-27', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (38, 1, '2025-11-28', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (39, 1, '2025-11-29', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (40, 1, '2025-12-01', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (41, 1, '2025-12-02', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (42, 1, '2025-12-03', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (43, 1, '2025-12-04', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (44, 1, '2025-12-05', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (45, 1, '2025-12-06', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (46, 1, '2025-12-07', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (47, 1, '2025-12-08', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (48, 1, '2025-12-09', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (49, 1, '2025-12-10', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (50, 1, '2025-12-11', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (51, 1, '2025-12-12', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (52, 1, '2025-12-13', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (53, 1, '2025-12-14', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (54, 1, '2025-12-15', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (55, 1, '2025-12-16', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (56, 1, '2025-12-17', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (57, 1, '2025-12-18', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (58, 1, '2025-12-19', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (59, 1, '2025-12-20', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (60, 1, '2025-12-21', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (61, 1, '2025-12-22', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (62, 1, '2025-12-23', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (63, 1, '2025-12-24', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (64, 1, '2025-12-25', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (65, 1, '2025-12-26', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (66, 1, '2025-12-27', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (67, 1, '2025-12-28', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (68, 1, '2025-12-29', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (69, 1, '2025-12-30', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (70, 1, '2025-12-31', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (71, 1, '2026-01-01', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (72, 1, '2026-01-02', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (73, 1, '2026-01-03', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (74, 1, '2026-01-04', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (75, 1, '2026-01-05', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (76, 1, '2026-01-06', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (77, 1, '2026-01-07', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (78, 1, '2026-01-08', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (79, 1, '2026-01-09', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (80, 1, '2026-01-10', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (81, 1, '2026-01-11', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (82, 1, '2026-01-12', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (83, 1, '2026-01-13', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (84, 1, '2026-01-14', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (85, 1, '2026-01-15', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (86, 1, '2026-01-16', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (87, 1, '2026-01-17', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (88, 1, '2026-01-18', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (89, 1, '2026-01-19', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (90, 1, '2026-01-20', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (91, 1, '2026-01-21', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (92, 1, '2026-01-22', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (93, 1, '2026-01-23', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (94, 1, '2026-01-24', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (95, 1, '2026-01-25', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (96, 1, '2026-01-26', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (97, 1, '2026-01-27', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (98, 1, '2026-01-28', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (99, 1, '2026-01-29', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (100, 1, '2026-01-30', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (101, 1, '2026-01-31', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (102, 1, '2026-02-01', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (103, 1, '2026-02-02', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (104, 1, '2026-02-03', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (105, 1, '2026-02-04', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (106, 1, '2026-02-05', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (107, 1, '2026-02-06', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (108, 1, '2026-02-07', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (109, 1, '2026-02-08', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (110, 1, '2026-02-09', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (111, 1, '2026-02-10', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (112, 1, '2026-02-11', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (113, 1, '2026-02-12', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (114, 1, '2026-02-13', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (115, 1, '2026-02-14', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (116, 1, '2026-02-15', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (117, 1, '2026-02-16', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (118, 1, '2026-02-17', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (119, 1, '2026-02-18', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (120, 1, '2026-02-19', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (121, 1, '2026-02-20', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (122, 1, '2026-02-21', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (123, 1, '2026-02-22', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (124, 1, '2026-02-23', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (125, 1, '2026-02-24', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (126, 1, '2026-02-25', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (127, 1, '2026-02-26', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (128, 1, '2026-02-27', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (129, 1, '2026-02-28', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (130, 1, '2026-03-01', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (131, 1, '2026-03-02', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (132, 1, '2026-03-03', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (133, 1, '2026-03-04', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (134, 1, '2026-03-05', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (135, 1, '2026-03-06', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (136, 1, '2026-03-07', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (137, 1, '2026-03-08', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (138, 1, '2026-03-09', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (139, 1, '2026-03-10', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (140, 1, '2026-03-11', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (141, 1, '2026-03-12', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (142, 1, '2026-03-13', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (143, 1, '2026-03-14', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (144, 1, '2026-03-15', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (145, 1, '2026-03-16', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (146, 1, '2026-03-17', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (147, 1, '2026-03-18', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (148, 1, '2026-03-19', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (149, 1, '2026-03-20', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (150, 1, '2026-03-21', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (151, 1, '2026-03-22', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (152, 1, '2026-03-23', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (153, 1, '2026-03-24', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (154, 1, '2026-03-25', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (155, 1, '2026-03-26', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (156, 1, '2026-03-27', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (157, 1, '2026-03-28', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (158, 1, '2026-03-29', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (159, 1, '2026-03-30', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (160, 1, '2026-03-31', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (161, 1, '2026-04-01', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (162, 1, '2026-04-02', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (163, 1, '2026-04-03', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (164, 1, '2026-04-04', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (165, 1, '2026-04-05', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (166, 1, '2026-04-06', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (167, 1, '2026-04-07', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (168, 1, '2026-04-08', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (169, 1, '2026-04-09', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (170, 1, '2026-04-10', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (171, 1, '2026-04-11', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (172, 1, '2026-04-12', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (173, 1, '2026-04-13', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (174, 1, '2026-04-14', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (175, 1, '2026-04-15', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (176, 1, '2026-04-16', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (177, 1, '2026-04-17', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (178, 1, '2026-04-18', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (179, 1, '2026-04-19', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (180, 1, '2026-04-20', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (181, 1, '2026-04-21', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (182, 1, '2026-04-22', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (183, 1, '2026-04-23', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (184, 1, '2026-04-24', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (185, 1, '2026-04-25', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (186, 1, '2026-04-26', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (187, 1, '2026-04-27', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (188, 1, '2026-04-28', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (189, 1, '2026-04-29', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (190, 1, '2026-04-30', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (191, 1, '2026-05-01', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (192, 1, '2026-05-02', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (193, 2, '2025-11-03', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (194, 2, '2025-11-04', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (195, 2, '2025-11-16', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (196, 2, '2025-11-17', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (197, 2, '2025-11-18', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (198, 2, '2025-11-19', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (199, 2, '2025-11-20', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (200, 2, '2025-11-21', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (201, 2, '2025-11-22', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (202, 2, '2025-11-23', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (203, 2, '2025-11-24', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (204, 2, '2025-11-25', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (205, 2, '2025-11-26', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (206, 2, '2025-11-27', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (207, 2, '2025-11-28', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (208, 2, '2025-11-29', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (209, 2, '2025-11-30', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (210, 2, '2025-12-01', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (211, 2, '2025-12-02', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (212, 2, '2025-12-03', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (213, 2, '2025-12-04', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (214, 2, '2025-12-05', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (215, 2, '2025-12-06', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (216, 2, '2025-12-07', 'SCHEDULED', NULL, NULL, 1762111800, 1762111800);
INSERT INTO public.trip_instances VALUES (217, 2, '2025-12-08', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (218, 2, '2025-12-09', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (219, 2, '2025-12-10', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (220, 2, '2025-12-11', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (221, 2, '2025-12-12', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (222, 2, '2025-12-13', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (223, 2, '2025-12-14', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (224, 2, '2025-12-15', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (225, 2, '2025-12-16', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (226, 2, '2025-12-17', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (227, 2, '2025-12-18', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (228, 2, '2025-12-19', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (229, 2, '2025-12-20', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (230, 2, '2025-12-21', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (231, 2, '2025-12-22', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (232, 2, '2025-12-23', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (233, 2, '2025-12-24', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (234, 2, '2025-12-25', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (235, 2, '2025-12-26', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (236, 2, '2025-12-27', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (237, 2, '2025-12-28', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (238, 2, '2025-12-29', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (239, 2, '2025-12-30', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (240, 2, '2025-12-31', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (241, 2, '2026-01-01', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (242, 2, '2026-01-02', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (243, 2, '2026-01-03', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (244, 2, '2026-01-04', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (245, 2, '2026-01-05', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (246, 2, '2026-01-06', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (247, 2, '2026-01-07', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (248, 2, '2026-01-08', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (249, 2, '2026-01-09', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (250, 2, '2026-01-10', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (251, 2, '2026-01-11', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (252, 2, '2026-01-12', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (253, 2, '2026-01-13', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (254, 2, '2026-01-14', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (255, 2, '2026-01-15', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (256, 2, '2026-01-16', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (257, 2, '2026-01-17', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (258, 2, '2026-01-18', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (259, 2, '2026-01-19', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (260, 2, '2026-01-20', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (261, 2, '2026-01-21', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (262, 2, '2026-01-22', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (263, 2, '2026-01-23', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (264, 2, '2026-01-24', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (265, 2, '2026-01-25', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (266, 2, '2026-01-26', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (267, 2, '2026-01-27', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (268, 2, '2026-01-28', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (269, 2, '2026-01-29', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (270, 2, '2026-01-30', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (271, 2, '2026-01-31', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (272, 2, '2026-02-01', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (273, 2, '2026-02-02', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (274, 2, '2026-02-03', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (275, 2, '2026-02-04', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (276, 2, '2026-02-05', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (277, 2, '2026-02-06', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (278, 2, '2026-02-07', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (279, 2, '2026-02-08', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (280, 2, '2026-02-09', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (281, 2, '2026-02-10', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (282, 2, '2026-02-11', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (283, 2, '2026-02-12', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (284, 2, '2026-02-13', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (285, 2, '2026-02-14', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (286, 2, '2026-02-15', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (287, 2, '2026-02-16', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (288, 2, '2026-02-17', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (289, 2, '2026-02-18', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (290, 2, '2026-02-19', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (291, 2, '2026-02-20', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (292, 2, '2026-02-21', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (293, 2, '2026-02-22', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (294, 2, '2026-02-23', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (295, 2, '2026-02-24', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (296, 2, '2026-02-25', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (297, 2, '2026-02-26', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (298, 2, '2026-02-27', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (299, 2, '2026-02-28', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (300, 2, '2026-03-01', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (301, 2, '2026-03-02', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (302, 2, '2026-03-03', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (303, 2, '2026-03-04', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (304, 2, '2026-03-05', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (305, 2, '2026-03-06', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (306, 2, '2026-03-07', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (307, 2, '2026-03-08', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (308, 2, '2026-03-09', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (309, 2, '2026-03-10', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (310, 2, '2026-03-11', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (311, 2, '2026-03-12', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (312, 2, '2026-03-13', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (313, 2, '2026-03-14', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (314, 2, '2026-03-15', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (315, 2, '2026-03-16', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (316, 2, '2026-03-17', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (317, 2, '2026-03-18', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (318, 2, '2026-03-19', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (319, 2, '2026-03-20', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (320, 2, '2026-03-21', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (321, 2, '2026-03-22', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (322, 2, '2026-03-23', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (323, 2, '2026-03-24', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (324, 2, '2026-03-25', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (325, 2, '2026-03-26', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (326, 2, '2026-03-27', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (327, 2, '2026-03-28', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (328, 2, '2026-03-29', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (329, 2, '2026-03-30', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (330, 2, '2026-03-31', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (331, 2, '2026-04-01', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (332, 2, '2026-04-02', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (333, 2, '2026-04-03', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (334, 2, '2026-04-04', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (335, 2, '2026-04-05', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (336, 2, '2026-04-06', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (337, 2, '2026-04-07', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (338, 2, '2026-04-08', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (339, 2, '2026-04-09', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (340, 2, '2026-04-10', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (341, 2, '2026-04-11', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (342, 2, '2026-04-12', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (343, 2, '2026-04-13', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (344, 2, '2026-04-14', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (345, 2, '2026-04-15', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (346, 2, '2026-04-16', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (347, 2, '2026-04-17', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (348, 2, '2026-04-18', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (349, 2, '2026-04-19', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (350, 2, '2026-04-20', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (351, 2, '2026-04-21', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (352, 2, '2026-04-22', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (353, 2, '2026-04-23', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (354, 2, '2026-04-24', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (355, 2, '2026-04-25', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (356, 2, '2026-04-26', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (357, 2, '2026-04-27', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (358, 2, '2026-04-28', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (359, 2, '2026-04-29', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (360, 2, '2026-04-30', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (361, 2, '2026-05-01', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (362, 2, '2026-05-02', 'SCHEDULED', NULL, NULL, 1762111801, 1762111801);
INSERT INTO public.trip_instances VALUES (363, 3, '2025-11-05', 'SCHEDULED', NULL, NULL, 1762113298, 1762113298);
INSERT INTO public.trip_instances VALUES (364, 3, '2025-11-06', 'SCHEDULED', NULL, NULL, 1762113298, 1762113298);
INSERT INTO public.trip_instances VALUES (365, 3, '2025-11-07', 'SCHEDULED', NULL, NULL, 1762113298, 1762113298);
INSERT INTO public.trip_instances VALUES (366, 3, '2025-11-08', 'SCHEDULED', NULL, NULL, 1762113298, 1762113298);
INSERT INTO public.trip_instances VALUES (367, 3, '2025-11-09', 'SCHEDULED', NULL, NULL, 1762113298, 1762113298);
INSERT INTO public.trip_instances VALUES (368, 3, '2025-11-10', 'SCHEDULED', NULL, NULL, 1762113298, 1762113298);


--
-- Data for Name: seat_reservations; Type: TABLE DATA; Schema: public; Owner: saurabh.kaushal
--

INSERT INTO public.seat_reservations VALUES (1, 1, 'A1', 1, 3, 1762098949, 1762098949, 'CREATED');
INSERT INTO public.seat_reservations VALUES (2, 2, 'E1', 3, 5, 1762105680, 1762105680, 'CREATED');
INSERT INTO public.seat_reservations VALUES (3, 2, 'E2', 3, 5, 1762109139, 1762109139, 'CREATED');
INSERT INTO public.seat_reservations VALUES (4, 1, 'B5', 1, 5, 1762117538, 1762117538, 'CREATED');
INSERT INTO public.seat_reservations VALUES (5, 1, 'B9', 1, 5, 1762118225, 1762118225, 'CREATED');
INSERT INTO public.seat_reservations VALUES (6, 1, 'C2', 1, 5, 1762118379, 1762118379, 'CREATED');
INSERT INTO public.seat_reservations VALUES (8, 16, 'A1', 1, 3, 1762119730, 1762119800, 'CANCELLED');
INSERT INTO public.seat_reservations VALUES (9, 16, 'A2', 1, 3, 1762119819, 1762119819, 'CREATED');
INSERT INTO public.seat_reservations VALUES (10, 16, 'A3', 1, 3, 1762120207, 1762120207, 'CREATED');
INSERT INTO public.seat_reservations VALUES (7, 17, 'A1', 1, 3, 1762119038, 1762120454, 'CANCELLED');
INSERT INTO public.seat_reservations VALUES (11, 16, 'A4', 1, 3, 1762120804, 1762120804, 'CANCELLED');
INSERT INTO public.seat_reservations VALUES (12, 17, 'A2', 1, 3, 1762121052, 1762121071, 'CANCELLED');
INSERT INTO public.seat_reservations VALUES (13, 17, 'A2', 1, 3, 1762122444, 1762122444, 'CREATED');
INSERT INTO public.seat_reservations VALUES (14, 17, 'B1', 1, 3, 1762122651, 1762122657, 'CANCELLED');
INSERT INTO public.seat_reservations VALUES (15, 17, 'B1', 1, 3, 1762122970, 1762122982, 'CANCELLED');
INSERT INTO public.seat_reservations VALUES (16, 17, 'B1', 1, 3, 1762123056, 1762124927, 'CANCELLED');
INSERT INTO public.seat_reservations VALUES (17, 17, 'B1', 1, 3, 1762124952, 1762124958, 'CANCELLED');
INSERT INTO public.seat_reservations VALUES (18, 17, 'A1', 1, 3, 1762126669, 1762126917, 'CANCELLED');


--
-- Name: buses_bus_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.buses_bus_id_seq', 3, true);


--
-- Name: cities_city_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.cities_city_id_seq', 11, true);


--
-- Name: route_stops_route_stop_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.route_stops_route_stop_id_seq', 16, true);


--
-- Name: routes_route_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.routes_route_id_seq', 3, true);


--
-- Name: seat_bookings_seat_booking_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.seat_bookings_seat_booking_id_seq', 18, true);


--
-- Name: seat_layouts_seat_layout_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.seat_layouts_seat_layout_id_seq', 163, true);


--
-- Name: trip_instances_trip_id_seq; Type: SEQUENCE SET; Schema: public; Owner: saurabh.kaushal
--

SELECT pg_catalog.setval('public.trip_instances_trip_id_seq', 368, true);


--
-- PostgreSQL database dump complete
--

\unrestrict tKDSPUezWy8LnZ0BGwDbfwCwIetL8RzTmAUnh3QflGaOLFnFg08mYsuxw0j6zHh

