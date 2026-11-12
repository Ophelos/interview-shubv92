# README

## Setup

1. Install Python (3.12 or 3.13 — Django 5.1 does not support newer versions)
2. Create and activate a virtual environment, then install dependencies

```sh
python -m venv .venv
source .venv/bin/activate
pip install -r requirements-dev.txt   # or requirements.txt to skip test tooling
```

3. Setup the application

```sh
python manage.py migrate
python manage.py loaddata affordability/seed/0001_test_user.json
python manage.py runserver
```

4. Run tests with `pytest`
5. Have fun

### Login details

To login to the application, use the following credentials:

```txt
username: test@example.com
password: password123
```
