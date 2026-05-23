BEGIN;

-- USERS (Missing ones from the script)
INSERT INTO users (id, created_at, email, full_name, password, phone, role, status, updated_at, username)
VALUES
('USR-003', CURRENT_TIMESTAMP, 'shemaphilbert7@gmail.com', 'David Manager', '12345678_hashed', '0788000003', 'SITE_MANAGER', 'ACTIVE', CURRENT_TIMESTAMP, 'manager3'),
('USR-004', CURRENT_TIMESTAMP, 'superex250@gmail.com', 'Sarah Manager', '12345678_hashed', '0788000004', 'SITE_MANAGER', 'ACTIVE', CURRENT_TIMESTAMP, 'manager4')
ON CONFLICT (id) DO NOTHING;

-- MATERIAL CATEGORIES
INSERT INTO material_categories (id, category_name, created_at, description, unit, updated_at)
VALUES
('MCA-002', 'Cement', CURRENT_TIMESTAMP, 'All types of cement', 'Bag', CURRENT_TIMESTAMP),
('MCA-003', 'Steel', CURRENT_TIMESTAMP, 'Reinforcement steel bars', 'Piece', CURRENT_TIMESTAMP),
('MCA-004', 'Wood', CURRENT_TIMESTAMP, 'Timber and wood materials', 'Piece', CURRENT_TIMESTAMP),
('MCA-005', 'Electrical', CURRENT_TIMESTAMP, 'Electrical installation materials', 'Meter', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;
INSERT INTO materials (id, created_at, description, material_name, status, unit, updated_at, category_id)
VALUES
('MAT-001', CURRENT_TIMESTAMP, 'CIMERWA OPC 42.5 cement', 'Cement', 'ACTIVE', 'Bag', CURRENT_TIMESTAMP, 'MCA-002'),
('MAT-002', CURRENT_TIMESTAMP, 'Kilimanjaro PPC cement', 'Cement', 'ACTIVE', 'Bag', CURRENT_TIMESTAMP, 'MCA-002'),
('MAT-003', CURRENT_TIMESTAMP, 'Rwandan reinforcement steel bars', 'Iron Rod', 'ACTIVE', 'Piece', CURRENT_TIMESTAMP, 'MCA-003'),
('MAT-004', CURRENT_TIMESTAMP, 'Premium eucalyptus timber', 'Wood', 'ACTIVE', 'Piece', CURRENT_TIMESTAMP, 'MCA-004'),
('MAT-005', CURRENT_TIMESTAMP, '2.5mm electrical installation cable', 'Electrical Cable', 'ACTIVE', 'Meter', CURRENT_TIMESTAMP, 'MCA-005')
ON CONFLICT (id) DO NOTHING;

-- WORKER TYPES
INSERT INTO worker_types (id, created_at, default_daily_rate, description, type_name, updated_at)
VALUES
('WKT-001', CURRENT_TIMESTAMP,15000,'Responsible for masonry and concrete works','Mason',CURRENT_TIMESTAMP),
('WKT-002', CURRENT_TIMESTAMP,12000,'Handles general site activities','Laborer',CURRENT_TIMESTAMP),
('WKT-003', CURRENT_TIMESTAMP,18000,'Responsible for wood and roofing works','Carpenter',CURRENT_TIMESTAMP),
('WKT-004', CURRENT_TIMESTAMP,20000,'Responsible for electrical installation','Electrician',CURRENT_TIMESTAMP),
('WKT-005', CURRENT_TIMESTAMP,18000,'Responsible for plumbing installation','Plumber',CURRENT_TIMESTAMP),
('WKT-006', CURRENT_TIMESTAMP,22000,'Responsible for finishing and interior works','Finisher',CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- PROJECTS
INSERT INTO projects (id,created_at,description,expected_end_date,location,project_name,start_date,status,updated_at,created_by)
VALUES
('PRJ-002',CURRENT_TIMESTAMP,'Construction of a commercial office complex',CURRENT_DATE,'Kigali - Gasabo','Gasabo Business Center',CURRENT_DATE,'ONGOING',CURRENT_TIMESTAMP,'USR-001'),
('PRJ-003',CURRENT_TIMESTAMP,'Development of a gated residential estate',CURRENT_DATE,'Bugesera','Bugesera Green Estate',CURRENT_DATE,'PLANNING',CURRENT_TIMESTAMP,'USR-001'),
('PRJ-004',CURRENT_TIMESTAMP,'Construction and finishing completed',CURRENT_DATE,'Kigali - Nyarugenge','City Plaza Development',CURRENT_DATE,'COMPLETED',CURRENT_TIMESTAMP,'USR-001')
ON CONFLICT (id) DO NOTHING;

-- PROJECT MANAGERS
INSERT INTO project_managers (id,assigned_date,created_at,status,updated_at,project_id,user_id)
VALUES
('PMG-002',CURRENT_DATE,CURRENT_TIMESTAMP,'ACTIVE',CURRENT_TIMESTAMP,'PRJ-002','USR-002'),
('PMG-003',CURRENT_DATE,CURRENT_TIMESTAMP,'ACTIVE',CURRENT_TIMESTAMP,'PRJ-003','USR-002'),
('PMG-004',CURRENT_DATE,CURRENT_TIMESTAMP,'ACTIVE',CURRENT_TIMESTAMP,'PRJ-004','USR-003'),
('PMG-005',CURRENT_DATE,CURRENT_TIMESTAMP,'ACTIVE',CURRENT_TIMESTAMP,'PRJ-003','USR-004')
ON CONFLICT (id) DO NOTHING;

-- SITE WORKERS
INSERT INTO site_workers (id,created_at,daily_rate,full_name,phone,status,updated_at,worker_type_id)
VALUES
('WKR-001',CURRENT_TIMESTAMP,15000,'Jean Claude Nshimiyimana','0788000001','ACTIVE',CURRENT_TIMESTAMP,'WKT-001'),
('WKR-002',CURRENT_TIMESTAMP,12000,'Eric Niyonzima','0788000002','ACTIVE',CURRENT_TIMESTAMP,'WKT-002'),
('WKR-003',CURRENT_TIMESTAMP,18000,'Patrick Uwimana','0788000003','ACTIVE',CURRENT_TIMESTAMP,'WKT-003'),
('WKR-004',CURRENT_TIMESTAMP,20000,'Emmanuel Habimana','0788000004','ACTIVE',CURRENT_TIMESTAMP,'WKT-004'),
('WKR-005',CURRENT_TIMESTAMP,18000,'Samuel Mugisha','0788000005','ACTIVE',CURRENT_TIMESTAMP,'WKT-005'),
('WKR-006',CURRENT_TIMESTAMP,22000,'Claude Tuyisenge','0788000006','ACTIVE',CURRENT_TIMESTAMP,'WKT-006')
ON CONFLICT (id) DO NOTHING;

-- MATERIAL PURCHASES
INSERT INTO material_purchases
(id,created_at,purchase_date,quantity,supplier_name,total_price,unit_price,updated_at,material_id,project_id,recorded_by,stock_status)
VALUES
('MPU-001',CURRENT_TIMESTAMP,CURRENT_DATE,120,'CIMERWA',1740000,14500,CURRENT_TIMESTAMP,'MAT-001','PRJ-002','USR-001','AVAILABLE'),
('MPU-002',CURRENT_TIMESTAMP,CURRENT_DATE,80,'Kilimanjaro Rwanda',1240000,15500,CURRENT_TIMESTAMP,'MAT-002','PRJ-002','USR-001','AVAILABLE'),
('MPU-003',CURRENT_TIMESTAMP,CURRENT_DATE,150,'SteelRwa Ltd',4800000,32000,CURRENT_TIMESTAMP,'MAT-003','PRJ-003','USR-001','AVAILABLE'),
('MPU-004',CURRENT_TIMESTAMP,CURRENT_DATE,60,'Rwanda Timber',1080000,18000,CURRENT_TIMESTAMP,'MAT-004','PRJ-004','USR-001','AVAILABLE'),
('MPU-005',CURRENT_TIMESTAMP,CURRENT_DATE,200,'Electro Rwanda',1700000,8500,CURRENT_TIMESTAMP,'MAT-005','PRJ-002','USR-001','AVAILABLE')
ON CONFLICT (id) DO NOTHING;

-- PROJECT MATERIAL STOCK
INSERT INTO project_material_stock
(id,average_unit_price,created_at,minimum_quantity,quantity_available,updated_at,material_id,project_id)
VALUES
('STK-001',14500,CURRENT_TIMESTAMP,20,120,CURRENT_TIMESTAMP,'MAT-001','PRJ-002'),
('STK-002',15500,CURRENT_TIMESTAMP,15,80,CURRENT_TIMESTAMP,'MAT-002','PRJ-002'),
('STK-003',8500,CURRENT_TIMESTAMP,50,200,CURRENT_TIMESTAMP,'MAT-005','PRJ-002'),
('STK-004',32000,CURRENT_TIMESTAMP,30,150,CURRENT_TIMESTAMP,'MAT-003','PRJ-003'),
('STK-005',18000,CURRENT_TIMESTAMP,20,60,CURRENT_TIMESTAMP,'MAT-004','PRJ-004')
ON CONFLICT (id) DO NOTHING;

-- MATERIAL USAGE
INSERT INTO material_usage
(id,activity_description,created_at,quantity_used,total_cost,unit_price,updated_at,usage_date,material_id,project_id,recorded_by)
VALUES
('MUS-001','Used CIMERWA cement for foundation concrete work',CURRENT_TIMESTAMP,30,435000,14500,CURRENT_TIMESTAMP,CURRENT_DATE,'MAT-001','PRJ-002','USR-002'),
('MUS-002','Used Kilimanjaro cement for wall plastering',CURRENT_TIMESTAMP,20,310000,15500,CURRENT_TIMESTAMP,CURRENT_DATE,'MAT-002','PRJ-002','USR-002'),
('MUS-003','Used steel rods for column reinforcement',CURRENT_TIMESTAMP,40,1280000,32000,CURRENT_TIMESTAMP,CURRENT_DATE,'MAT-003','PRJ-003','USR-003'),
('MUS-004','Used wood for roofing framework',CURRENT_TIMESTAMP,15,270000,18000,CURRENT_TIMESTAMP,CURRENT_DATE,'MAT-004','PRJ-004','USR-004')
ON CONFLICT (id) DO NOTHING;

-- USAGE PURCHASE ALLOCATIONS
INSERT INTO usage_purchase_allocations
(id,allocated_quantity,purchase_id,usage_id)
VALUES
('UPA-001',30,'MPU-001','MUS-001'),
('UPA-002',20,'MPU-002','MUS-002'),
('UPA-003',40,'MPU-003','MUS-003'),
('UPA-004',15,'MPU-004','MUS-004')
ON CONFLICT (id) DO NOTHING;

COMMIT;
