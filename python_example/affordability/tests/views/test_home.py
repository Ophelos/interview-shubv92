import pytest
from datetime import date
from django.contrib.auth.models import User
from django.urls import reverse

@pytest.mark.django_db
def test_home_view(client):
    response = client.get(reverse("home"))
    assert response.status_code == 200
    assert "Login" in response.content.decode()
