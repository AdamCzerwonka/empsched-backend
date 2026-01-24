## Example Usage of SchedulingService
### Create Draft Schedule
`POST
`/schedules/draft`
#### Request Body
Pattern-Based Schedule Creation Example:
```json
{
  "startDate": "2024-12-23",
  "endDate": "2024-12-27",
  "weeklyPattern": {
    "MONDAY": [
      {
        "startTime": "08:00",
        "endTime": "16:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 },
          { "positionId": "4a00246d-69c7-4724-9e76-aae5ff62f320", "quantity": 1 }
        ]
      },
      {
        "startTime": "16:00",
        "endTime": "00:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 }
        ]
      }
    ],
    "TUESDAY": [
      {
        "startTime": "08:00",
        "endTime": "16:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 },
          { "positionId": "4a00246d-69c7-4724-9e76-aae5ff62f320", "quantity": 1 }
        ]
      },
      {
        "startTime": "16:00",
        "endTime": "00:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 }
        ]
      }
    ],
    "WEDNESDAY": [
      {
        "startTime": "08:00",
        "endTime": "16:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 },
          { "positionId": "4a00246d-69c7-4724-9e76-aae5ff62f320", "quantity": 1 }
        ]
      },
      {
        "startTime": "16:00",
        "endTime": "00:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 }
        ]
      }
    ],
    "THURSDAY": [
      {
        "startTime": "08:00",
        "endTime": "16:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 },
          { "positionId": "4a00246d-69c7-4724-9e76-aae5ff62f320", "quantity": 1 }
        ]
      },
      {
        "startTime": "16:00",
        "endTime": "00:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 }
        ]
      }
    ],
    "FRIDAY": [
      {
        "startTime": "08:00",
        "endTime": "16:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 },
          { "positionId": "4a00246d-69c7-4724-9e76-aae5ff62f320", "quantity": 1 }
        ]
      },
      {
        "startTime": "16:00",
        "endTime": "00:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 }
        ]
      }
    ]
  },
  "dateOverrides": {}
}
```
Assign template for each weekday from Monday to Friday with two shifts per day. The first shift runs from 08:00 to 16:00 requiring 2 employees for positionId "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b" and 1 employee for positionId "4a00246d-69c7-4724-9e76-aae5ff62f320". The second shift runs from 16:00 to 00:00 requiring 2 employees for positionId "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b". No overrides are specified.
When weekday is not specified in weeklyPattern, no shifts will be created for that day.
When dateOverrides is added, it will take precedence over weeklyPattern for the specified dates.

# Date overrides example:
```json
{
  "startDate": "2024-12-23",
  "endDate": "2024-12-27",
  "weeklyPattern": {
    "MONDAY": [
      {
        "startTime": "08:00",
        "endTime": "16:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 2 }
        ]
      }
    ]
  },
  "dateOverrides": {
    "2024-12-25": [  // Christmas Day
      {
        "startTime": "10:00",
        "endTime": "14:00",
        "shiftRequirements": [
          { "positionId": "2a097638-8d8f-431b-a8cb-3b51ea6e0c6b", "quantity": 1 }
        ]
      }
    ]
  }
}
```