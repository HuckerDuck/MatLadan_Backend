-- Change the id from Long to UUID

ALTER TABLE users DROP CONSTRAINT users_pkey;

ALTER TABLE users DROP COLUMN id;

ALTER TABLE users ADD COLUMN id UUID DEFAULT  gen_random_uuid() PRIMARY KEY;