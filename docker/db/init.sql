CREATE
DATABASE empshed_auth;
CREATE
DATABASE empshed_employee;

CREATE
USER empshed PASSWORD 'empshed';

GRANT ALL
ON DATABASE empshed_auth TO empshed;
GRANT ALL
ON DATABASE empshed_employee TO empshed;

ALTER
DATABASE empshed_auth OWNER TO empshed;
ALTER
DATABASE empshed_employee OWNER TO empshed;
