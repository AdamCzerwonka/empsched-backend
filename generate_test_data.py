#!/usr/bin/env python3
"""
Test Data Generator for Scheduling Service

This script generates SQL INSERT statements for employees, positions, and availabilities,
as well as a JSON file with schedule templates ready to be filled.

Usage:
    python generate_test_data.py --employees 50 --positions 5 --start-date 2024-12-23 --end-date 2024-12-29

Output:
    - generated_data.sql: SQL INSERT statements for the scheduling service
    - schedule_template.json: JSON template for creating schedules via API
"""

import argparse
import json
import uuid
import random
from tqdm import tqdm
from datetime import datetime, timedelta, date, time
from typing import List, Dict, Any


# Sample data for generating realistic names
FIRST_NAMES = [
    "Anna", "Barbara", "Ewa", "Joanna", "Katarzyna", "Maria", "Magdalena", "Agnieszka",
    "Jan", "Piotr", "Andrzej", "Tomasz", "Krzysztof", "Michał", "Paweł", "Marcin",
    "Zofia", "Dorota", "Teresa", "Elżbieta", "Aleksandra", "Monika", "Beata", "Sylwia",
    "Jakub", "Adam", "Wojciech", "Marek", "Łukasz", "Kamil", "Mateusz", "Rafał",
    "Natalia", "Karolina", "Justyna", "Paulina", "Marta", "Izabela", "Ewelina", "Wiktoria",
    "Julia", "Alicja", "Klaudia", "Patrycja", "Weronika", "Martyna", "Oliwia", "Zuzanna",
    "Gabriela", "Nikola", "Małgorzata", "Danuta", "Halina", "Helena", "Grażyna", "Jadwiga",
    "Krystyna", "Irena", "Stanisława", "Marianna", "Genowefa", "Janina", "Józefa", "Kazimiera",
    "Sebastian", "Damian", "Adrian", "Bartosz", "Grzegorz", "Szymon", "Filip", "Maciej",
    "Dawid", "Artur", "Robert", "Dariusz", "Mariusz", "Zbigniew", "Ryszard", "Jacek",
    "Henryk", "Stanisław", "Józef", "Tadeusz", "Kazimierz", "Władysław", "Bogdan", "Leszek",
    "Mirosław", "Jarosław", "Bogusław", "Czesław", "Edward", "Roman", "Zdzisław", "Marian",
    "Witold", "Daniel", "Hubert", "Kacper", "Oskar", "Igor", "Dominik", "Michał", "Norbert",
    "Emil", "Leon", "Maksymilian", "Franciszek", "Antoni", "Mikołaj", "Wiktor", "Sławomir"
]

LAST_NAMES = [
    "Kowalski", "Wiśniewski", "Wójcik", "Kowalczyk", "Kamiński", "Lewandowski", "Zieliński",
    "Szymański", "Woźniak", "Dąbrowski", "Kozłowski", "Jankowski", "Mazur", "Wojciechowski",
    "Kwiatkowski", "Krawczyk", "Kaczmarek", "Piotrowski", "Grabowski", "Pawłowski", "Michalski",
    "Nowakowski", "Adamczyk", "Dudek", "Zajączkowski", "Król", "Urbański", "Baranowski",
    "Zawadzki", "Pawlak", "Walczak", "Górski", "Rutkowski", "Witkowski", "Olszewski", "Sikora",
    "Baran", "Duda", "Szewczyk", "Tomaszewski", "Pietrzak", "Marciniak", "Wróbel", "Zalewski",
    "Jasiński", "Bąk", "Włodarczyk", "Malinowski", "Laskowski", "Sawicki", "Lis", "Maciejewski",
    "Kubiak", "Kalinowski", "Borkowski", "Wysocki", "Sobczak", "Czerwiński", "Jaworski", "Mazurek",
    "Zakrzewski", "Krupa", "Krajewski", "Gajewski", "Szulc", "Głowacki", "Kowalewska", "Czarnecki",
    "Kołodziej", "Sikorski", "Krajewska", "Przybylski", "Kucharski", "Wilk", "Stępień", "Andrzejewski",
    "Sadowski", "Ostrowski", "Janik", "Brzeziński", "Kowal", "Czajkowski", "Kania", "Szczepański",
    "Owski", "Kwieciński", "Krawiec", "Błaszczyk", "Cieślak", "Nowicki", "Sobczyk", "Sosnowski"
]

POSITION_NAMES = [
    "Nurse", "Doctor", "Pediatric Care", "Emergency Care", "ICU Specialist",
    "Surgeon", "Anesthesiologist", "Radiologist", "Lab Technician", "Pharmacist",
    "Cardiology Specialist", "Orthopedic Surgeon", "Neurologist", "Psychiatrist", "Dermatologist",
    "Oncologist", "Physical Therapist", "Respiratory Therapist", "Medical Assistant", "Paramedic"
]


def generate_uuid() -> str:
    """Generate a UUID string."""
    return str(uuid.uuid4())


def generate_employee_name() -> tuple:
    """Generate a random first and last name."""
    return random.choice(FIRST_NAMES), random.choice(LAST_NAMES)


def generate_positions(count: int, org_id: str) -> List[Dict[str, Any]]:
    """Generate position data."""
    positions = []
    for i in range(count):
        position_id = generate_uuid()
        name = POSITION_NAMES[i % len(POSITION_NAMES)]
        if i >= len(POSITION_NAMES):
            name = f"{name} {i // len(POSITION_NAMES) + 1}"

        positions.append({
            "id": position_id,
            "name": name,
            "organisation_id": org_id
        })
    return positions


def generate_employees(count: int, org_id: str, positions: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
    """Generate employee data."""
    employees = []
    used_names = set()

    for i in tqdm(range(count)):
        # Generate unique name
        while True:
            first_name, last_name = generate_employee_name()
            full_name = f"{first_name} {last_name}"
            if full_name not in used_names:
                used_names.add(full_name)
                break

        employee_id = generate_uuid()
        max_weekly_hours = random.choice([24, 32, 40, 40, 40])  # Mostly full-time

        # Assign 1-3 random positions to each employee
        num_positions = random.randint(1, min(3, len(positions)))
        employee_positions = random.sample(positions, num_positions)

        employees.append({
            "id": employee_id,
            "first_name": first_name,
            "last_name": last_name,
            "max_weekly_hours": max_weekly_hours,
            "organisation_id": org_id,
            "positions": employee_positions
        })

    return employees


def generate_availabilities(employees: List[Dict[str, Any]], start_date: date, end_date: date) -> List[Dict[str, Any]]:
    """Generate employee availability data (optional absences)."""
    availabilities = []
    current_date = start_date

    while current_date <= end_date:
        # 20% chance of any employee having availability constraint on any day
        for employee in employees:
            if random.random() < 0.2:
                availability_type = random.choice(["UNAVAILABLE", "UNAVAILABLE", "UNAVAILABLE", "DESIRED"])  # More unavailable than desired
                availabilities.append({
                    "id": generate_uuid(),
                    "employee_id": employee["id"],
                    "date": current_date.isoformat(),
                    "type": availability_type,
                    "absence_id": generate_uuid()
                })

        current_date += timedelta(days=1)

    return availabilities


def generate_sql(org_id: str, positions: List[Dict[str, Any]], employees: List[Dict[str, Any]],
                 availabilities: List[Dict[str, Any]], include_org: bool = True) -> str:
    """Generate SQL INSERT statements."""
    sql_lines = [
        "-- ============================================================================",
        "-- Generated Test Data for Scheduling Service",
        f"-- Generated at: {datetime.now().isoformat()}",
        f"-- Employees: {len(employees)}",
        f"-- Positions: {len(positions)}",
        f"-- Availabilities: {len(availabilities)}",
        "-- ============================================================================",
        ""
    ]

    # Organisation
    if include_org:
        sql_lines.extend([
            "-- Organisation",
            "INSERT INTO organisation (id, created_at, updated_at, plan, version)",
            f"VALUES ('{org_id}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'DEPARTMENT', 0);",
            ""
        ])

    # Positions
    sql_lines.extend([
        "-- ============================================================================",
        "-- Positions",
        "-- ============================================================================",
        "INSERT INTO position (id, created_at, updated_at, organisation_id, version)",
        "VALUES"
    ])

    position_values = []
    for pos in positions:
        position_values.append(
            f"    ('{pos['id']}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{pos['organisation_id']}', 0)"
        )
    sql_lines.append(",\n".join(position_values) + ";")
    sql_lines.append("")

    # Employees
    sql_lines.extend([
        "-- ============================================================================",
        "-- Employees",
        "-- ============================================================================"
    ])

    for emp in employees:
        sql_lines.append(f"-- {emp['first_name']} {emp['last_name']} ({emp['max_weekly_hours']}h/week)")
        sql_lines.append("INSERT INTO employee (id, created_at, updated_at, max_weekly_hours, organisation_id, version)")
        sql_lines.append(
            f"VALUES ('{emp['id']}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, {emp['max_weekly_hours']}, "
            f"'{emp['organisation_id']}', 0);"
        )
        sql_lines.append("")

    # Employee-Position mappings
    sql_lines.extend([
        "-- ============================================================================",
        "-- Employee-Position Mappings",
        "-- ============================================================================"
    ])

    for emp in employees:
        sql_lines.append(f"-- {emp['first_name']} {emp['last_name']}")
        sql_lines.append("INSERT INTO employee_position (employee_id, position_id)")
        sql_lines.append("VALUES")
        position_mappings = [f"    ('{emp['id']}', '{pos['id']}')" for pos in emp['positions']]
        sql_lines.append(",\n".join(position_mappings) + ";")
        sql_lines.append("")

    # Availabilities (if any)
    if availabilities:
        sql_lines.extend([
            "-- ============================================================================",
            "-- Employee Availabilities",
            "-- ============================================================================"
        ])

        for avail in availabilities:
            sql_lines.append(
                "INSERT INTO employee_availability (id, created_at, updated_at, absence_id, employee_id, date, type, version)"
            )
            sql_lines.append(
                f"VALUES ('{avail['id']}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{avail['absence_id']}', "
                f"'{avail['employee_id']}', '{avail['date']}', '{avail['type']}', 0);"
            )
        sql_lines.append("")

    return "\n".join(sql_lines)


def generate_schedule_json(positions: List[Dict[str, Any]], start_date: date, end_date: date) -> Dict[str, Any]:
    """Generate a schedule template JSON."""

    # Define common shift patterns
    shift_patterns = {
        "MORNING": {"startTime": "08:00", "endTime": "16:00"},
        "AFTERNOON": {"startTime": "16:00", "endTime": "23:59"},
        "NIGHT": {"startTime": "00:00", "endTime": "08:00"}
    }

    # Create a weekly pattern for weekdays - using DayOfWeek enum values
    weekly_pattern = {}
    weekdays = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"]

    for weekday in weekdays:
        daily_shifts = []

        # Morning shift: 2-4 employees from various positions
        morning_requirements = []
        for i, pos in enumerate(positions[:min(3, len(positions))]):
            morning_requirements.append({
                "positionId": pos["id"],
                "quantity": random.randint(1, 3)
            })

        daily_shifts.append({
            "startTime": "08:00",
            "endTime": "16:00",
            "shiftRequirements": morning_requirements
        })

        # Afternoon shift: 1-3 employees
        afternoon_requirements = []
        for i, pos in enumerate(positions[:min(2, len(positions))]):
            afternoon_requirements.append({
                "positionId": pos["id"],
                "quantity": random.randint(1, 2)
            })

        daily_shifts.append({
            "startTime": "16:00",
            "endTime": "23:59",
            "shiftRequirements": afternoon_requirements
        })

        weekly_pattern[weekday] = daily_shifts

    # Create date overrides example (empty by default)
    date_overrides = {}

    schedule_template = {
        "startDate": start_date.isoformat(),
        "endDate": end_date.isoformat(),
        "weeklyPattern": weekly_pattern,
        "dateOverrides": date_overrides
    }

    return schedule_template


def main():
    parser = argparse.ArgumentParser(
        description="Generate test data for Scheduling Service"
    )
    parser.add_argument(
        "--employees",
        type=int,
        default=20,
        help="Number of employees to generate (default: 20)"
    )
    parser.add_argument(
        "--positions",
        type=int,
        default=5,
        help="Number of positions to generate (default: 5)"
    )
    parser.add_argument(
        "--org-id",
        type=str,
        default="7123f3ec-3517-4d3e-98e2-4e98a4cd9581",
        help="Organisation UUID (default: 7123f3ec-3517-4d3e-98e2-4e98a4cd9581)"
    )
    parser.add_argument(
        "--start-date",
        type=str,
        default="2024-12-23",
        help="Schedule start date (YYYY-MM-DD, default: 2024-12-23)"
    )
    parser.add_argument(
        "--end-date",
        type=str,
        default="2024-12-29",
        help="Schedule end date (YYYY-MM-DD, default: 2024-12-29)"
    )
    parser.add_argument(
        "--with-availabilities",
        action="store_true",
        help="Generate random availability constraints"
    )
    parser.add_argument(
        "--include-org",
        action="store_true",
        help="Include organisation INSERT in SQL output"
    )
    parser.add_argument(
        "--output-sql",
        type=str,
        default="generated_data.sql",
        help="Output SQL file path (default: generated_data.sql)"
    )
    parser.add_argument(
        "--output-json",
        type=str,
        default="schedule_template.json",
        help="Output JSON file path (default: schedule_template.json)"
    )

    args = parser.parse_args()

    # Parse dates
    start_date = datetime.strptime(args.start_date, "%Y-%m-%d").date()
    end_date = datetime.strptime(args.end_date, "%Y-%m-%d").date()

    # Generate data
    positions = generate_positions(args.positions, args.org_id)
    employees = generate_employees(args.employees, args.org_id, positions)

    availabilities = []
    if args.with_availabilities:
        availabilities = generate_availabilities(employees, start_date, end_date)

    # Generate SQL
    sql_content = generate_sql(args.org_id, positions, employees, availabilities, args.include_org)

    with open(args.output_sql, "w", encoding="utf-8") as f:
        f.write(sql_content)

    # Generate JSON
    schedule_json = generate_schedule_json(positions, start_date, end_date)

    with open(args.output_json, "w", encoding="utf-8") as f:
        json.dump(schedule_json, f, indent=2)



if __name__ == "__main__":
    main()
