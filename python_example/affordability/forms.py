from django import forms
from django.forms import inlineformset_factory
from .models import Statement, Transaction

class StatementForm(forms.ModelForm):
    statement_period = forms.DateField(
        widget=forms.DateInput(attrs={'type': 'date'}),
        required=True
    )
    class Meta:
        model = Statement
        fields = ['statement_period']

class TransactionForm(forms.ModelForm):
    class Meta:
        model = Transaction
        fields = ['category', 'description', 'amount']

TransactionFormSet = inlineformset_factory(Statement, Transaction, form=TransactionForm, extra=5, can_delete=False)
