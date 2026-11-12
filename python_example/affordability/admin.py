from django.contrib import admin

from .models import Statement
from .models import Transaction

admin.site.register(Statement)
admin.site.register(Transaction)
