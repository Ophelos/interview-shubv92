import pytest
from datetime import date, timedelta
from affordability.forms import TransactionForm

@pytest.mark.parametrize("data,valid", [
    ({"category": "income", "description": "Test", "amount": 100}, True),
    ({"category": "invalid", "description": "Test", "amount": 100}, False),
    ({"category": "income", "description": "", "amount": 100}, False),
])
def test_transaction_form(data, valid):
    form = TransactionForm(data)
    assert form.is_valid() == valid
