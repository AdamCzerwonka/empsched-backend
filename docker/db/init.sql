CREATE
    DATABASE empshed_auth;
CREATE
    DATABASE empshed_employee;
CREATE DATABASE empshed_organisation;
CREATE DATABASE empshed_scheduling;

CREATE
    USER empshed PASSWORD 'empshed';

GRANT ALL
    ON DATABASE empshed_auth TO empshed;
GRANT ALL
    ON DATABASE empshed_employee TO empshed;
GRANT ALL
    ON DATABASE empshed_organisation TO empshed;
GRANT ALL
    ON DATABASE empshed_scheduling TO empshed;

ALTER
    DATABASE empshed_auth OWNER TO empshed;
ALTER
    DATABASE empshed_employee OWNER TO empshed;
ALTER
    DATABASE empshed_organisation OWNER TO empshed;
ALTER
    DATABASE empshed_scheduling OWNER TO empshed;


