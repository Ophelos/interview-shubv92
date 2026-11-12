import pytest
from datetime import date, timedelta
from django.contrib.auth.models import User
from django.core.exceptions import ValidationError
from django.utils import timezone
from affordability.models import Statement, Transaction

@pytest.mark.django_db
def test_transaction_creation():
    test_user = User.objects.create_user(username="testuser", password="testpass")
    statement = Statement.objects.create(user=test_user, statement_period=date.today())
    transaction = Transaction.objects.create(
        statement=statement,
        category="income",
        description="Test Income",
        amount=1000
    )

    assert transaction.statement == statement
    assert transaction.category == "income"
    assert transaction.amount == 1000
    assert transaction.description == "Test Income"
