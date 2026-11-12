require "application_system_test_case"

class UserCreatesStatementTest < ApplicationSystemTestCase
  test "user creates valid statement" do
    _user = User.create!(email_address: "test@example.com", password: "password")

    visit root_url

    fill_in "Email address", with: "test@example.com"
    fill_in "Password", with: "password"
    click_button "Sign in"

    assert_selector "h1", text: "Your affordability statements"

    click_link "Create a new statement"

    select "2024", from: "affordability_statement[statement_period(1i)]"
    select "January", from: "affordability_statement[statement_period(2i)]"

    within_fieldset "Transaction 1" do
      select "Income", from: "Category"
      fill_in "Description", with: "Salary"
      fill_in "Amount", with: "200000"
    end

    within_fieldset "Transaction 2" do
      select "Expenditure", from: "Category"
      fill_in "Description", with: "Rent"
      fill_in "Amount", with: "50000"
    end

    within_fieldset "Transaction 3" do
      select "Expenditure", from: "Category"
      fill_in "Description", with: "Groceries"
      fill_in "Amount", with: "10000"
    end

    click_button "Submit statement"

    assert_selector "h1", text: "Your affordability statements"
  end

  test "user cannot create invalid statement" do
    _user = User.create!(email_address: "test@example.com", password: "password")

    visit root_url

    fill_in "Email address", with: "test@example.com"
    fill_in "Password", with: "password"
    click_button "Sign in"

    assert_selector "h1", text: "Your affordability statements"

    click_link "Create a new statement"

    click_button "Submit statement"

    assert_text "There was a problem with your submission"
    assert_text "Statement period can't be blank"
    assert_text "Transactions can't be blank"

    select "2024", from: "affordability_statement[statement_period(1i)]"
    select "January", from: "affordability_statement[statement_period(2i)]"

    within_fieldset "Transaction 1" do
      select "Income", from: "Category"
      fill_in "Description", with: "Salary"
    end

    click_button "Submit statement"

    assert_text "There was a problem with your submission"
    assert_text "Transactions amount can't be blank"
    assert_text "Transactions is invalid"

    within_fieldset "Transaction 1" do
      fill_in "Amount", with: "200000"
    end

    click_button "Submit statement"

    assert_selector "h1", text: "Your affordability statements"
  end
end
