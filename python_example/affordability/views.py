from django.contrib.auth import login
from django.contrib.auth.decorators import login_required
from django.contrib.auth.forms import AuthenticationForm
from django.shortcuts import render, redirect, get_object_or_404
from .forms import StatementForm, TransactionFormSet
from .models import Statement


def index(request):
    if request.user.is_authenticated:
        return redirect('statements')

    if request.method == 'POST':
        form = AuthenticationForm(request, data=request.POST)
        if form.is_valid():

            user = form.get_user()
            login(request, user)
            return redirect('statements')
    else:
        form = AuthenticationForm()

    return render(request, 'home.html', {'form': form})

@login_required
def statements(request):
    statements = Statement.objects.filter(user=request.user)
    return render(request, 'statements/index.html', {'statements': statements})

@login_required
def new_statement(request):
    if request.method == 'POST':
        statement_form = StatementForm(request.POST)
        transaction_formset = TransactionFormSet(request.POST)

        if statement_form.is_valid() and transaction_formset.is_valid():
            statement = statement_form.save(commit=False)
            statement.user = request.user
            statement.save()

            transactions = transaction_formset.save(commit=False)
            for transaction in transactions:
                transaction.statement = statement
                transaction.save()

            return redirect('statements')

    else:
        statement_form = StatementForm()
        transaction_formset = TransactionFormSet()

    return render(request, 'statements/new.html', {
        'form': statement_form,
        'transaction_formset': transaction_formset
    })

@login_required
def view_statement(request, statement_id):
    statement = get_object_or_404(Statement, id=statement_id, user=request.user)

    return render(request, 'statements/view.html', {
        'statement': statement
    })
