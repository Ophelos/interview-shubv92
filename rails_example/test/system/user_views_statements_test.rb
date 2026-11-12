require "application_system_test_case"

class UserViewsStatementsTest < ApplicationSystemTestCase
  test "user views statements" do
    _user = User.create(email_address: "test@example.com", password: "password")

    visit root_url

    fill_in "Email address", with: "test@example.com"
    fill_in "Password", with: "password"
    click_button "Sign in"

    assert_selector "h1", text: "Your affordability statements"
  end
end
