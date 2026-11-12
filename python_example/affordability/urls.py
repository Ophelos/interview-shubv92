from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='home'),
    path('statements/', views.statements, name='statements'),
    path('statements/new/', views.new_statement, name='new_statement'),
    path('statements/<int:statement_id>/', views.view_statement, name='view_statement'),
]
