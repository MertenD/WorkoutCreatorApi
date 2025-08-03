![API Banner](/docs/banner2_flach.png)

Create personalized training plans in seconds. This fitness API turns target **muscle groups**, **available equipment**, and a **short description** into structured workouts with exercises, step-for-step instructions, sets, reps - perfect for **AI fitness**, **digital coaching**, and **workout plan automation**.

---

## Endpoints

POST `/workout´

### Request Body

Provide a JSON object with three fields.

| Field          | Type          | Required | Constraints                                                               | Description                                                                                  |
| -------------- | ------------- | -------- | ------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| `muscleGroups` | array&lt;string&gt; | yes      | `minItems: 1`, `maxItems: 10`; each item: `minLength: 1`, `maxLength: 50` | Target muscle groups for the plan. Examples: `"chest"`, `"back"`, `"legs"`.                  |
| `equipment`    | array&lt;string&gt; | yes      | `minItems: 0`, `maxItems: 10`; each item: `minLength: 1`, `maxLength: 50` | Available equipment. `[]` → bodyweight only. `["any"]` → all equipment available (shortcut). |
| `description`  | string        | yes      | `minLength: 1`, `maxLength: 1000`                                         | Short free-text prompt describing the workout (level, focus, constraints).                   |

**Validation rules**

* The object **must not** include extra fields (`additionalProperties: false`).
* `muscleGroups`, `equipment`, and `description` are **required**.
* Arrays exceeding `maxItems` or items exceeding `maxLength` are rejected with **400 Bad Request**.

### Request Body JSON Schema

```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "type": "object",
  "additionalProperties": false,
  "properties": {
    "muscleGroups": {
      "type": "array",
      "items": {
        "type": "string",
        "minLength": 1,
        "maxLength": 50
      },
      "minItems": 1,
      "maxItems": 10
    },
    "equipment": {
      "type": "array",
      "items": {
        "type": "string",
        "minLength": 1,
        "maxLength": 50
      },
      "minItems": 0,
      "maxItems": 10
    },
    "description": {
      "type": "string",
      "maxLength": 1000,
      "minLength": 1
    }
  },
  "required": [
    "muscleGroups",
    "equipment",
    "description"
  ]
}
```

### Example Requests

**Bodyweight only**

```bash
curl -X POST https://api.example.com/v1/workouts/generate \
  -H "Authorization: Bearer $API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "muscleGroups": ["chest", "triceps", "shoulders"],
    "equipment": [],
    "description": "Beginner push session with chest emphasis and minimal setup."
  }'
```

**All equipment available**

```bash
curl -X POST https://api.example.com/v1/workouts/generate \
  -H "Authorization: Bearer $API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "muscleGroups": ["legs", "glutes"],
    "equipment": ["any"],
    "description": "Intermediate lower-body strength workout with heavier compound lifts."
  }'
```

### Example Response

```json
{
  "name": "Back Strength Builder",
  "description": "A focused back workout targeting the upper and lower back muscles using a pullup bar, without utilizing standard shoulder-width pullups.",
  "exercises": [	    
    {
      "name": "Chin-ups",
      "description": "An exercise targeting the upper back with an undersized grip to maximize upper back engagement.",
      "instructions": [
        "Grip the pullup bar with your palms facing towards you.",
        "Extend your arms fully and hang with your feet off the ground.",
        "Engage your upper back muscles and core as you pull yourself up until your chin is above the bar.",
        "Lower yourself down in a controlled manner to the starting position.",
        "Repeat."
      ],
      "muscleGroups": [
        "UPPER_BACK",
        "LATS"
      ],
      "equipment": [
        "Pullup Bar"
      ],
      "sets": 3,
      "reps": 8
    },
    {
      "name": "Neutral Grip Pull-ups",
      "description": "A variation of the pull-up using a neutral grip to target the upper back.",
      "instructions": [
        "Find a pullup bar with parallel bars for a neutral grip, or use attachments.",
        "Grab the grips so your palms face each other.",
        "Hang with your arms fully extended.",
        "Pull yourself up until your chin is above your hands, focusing on squeezing your upper back.",
        "Lower yourself back to the starting position in a controlled manner.",
        "Repeat."
      ],
      "muscleGroups": [
        "UPPER_BACK",
        "LATS"
      ],
      "equipment": [
        "Pullup Bar"
      ],
      "sets": 3,
      "reps": 6
    },
    {
      "name": "Hanging Pelvic Floor Raise",
      "description": "An exercise aimed at strengthening the lower back by raising and lowering the pelvis while hanging from a bar.",
      "instructions": [
        "Grip the pullup bar with your hands shoulder-width apart and hang with straight arms.",
        "While keeping your body straight, slowly raise your pelvis upwards by engaging your lower back and abdominal muscles.",
        "Hold for a moment at the top, then lower your pelvis back to the starting position.",
        "Ensure movement is slow and controlled to maximize lower back engagement.",
        "Repeat."
      ],
      "muscleGroups": [
        "LOWER_BACK",
        "ABDOMINALS"
      ],
      "equipment": [
        "Pullup Bar"
      ],
      "sets": 3,
      "reps": 10
    }
  ]
}
```