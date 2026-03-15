INSERT INTO departments (name) VALUES ('HR') ON CONFLICT (name) DO NOTHING;
INSERT INTO departments (name) VALUES ('IT') ON CONFLICT (name) DO NOTHING;
INSERT INTO departments (name) VALUES ('Finance') ON CONFLICT (name) DO NOTHING;