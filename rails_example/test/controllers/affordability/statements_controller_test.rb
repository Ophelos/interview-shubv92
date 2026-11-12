require "test_helper"

module Affordability
  class StatementsControllerTest < ActionDispatch::IntegrationTest
    test "POST create - creates statement and redirects to statements page" do
      user = users(:one)
      login_with user.email_address, "password"

      params = {
        "affordability_statement": {
          "statement_period(1i)" => "2024",
          "statement_period(2i)" => "1",
          "transactions_attributes": {
            "0" => {
              "category" => "income",
              "description" => "Salary",
              "amount" => "100000"
            },
            "1" => {
              "category" => "expenditure",
              "description" => "Rent",
              "amount" => "50000"
            },
            "2" => { "category" => "", "description" => "", "amount" => "" }
          }
        }
      }

      assert_difference -> { Statement.count }, 1 do
        post affordability_statements_url, params: params
      end

      statement = Statement.last
      assert_equal Date.new(2024, 1, 31), statement.statement_period
      assert_equal user, statement.user

      assert_equal 2, statement.transactions.size

      assert_equal "income", statement.transactions.first.category
      assert_equal "Salary", statement.transactions.first.description
      assert_equal 1000_00, statement.transactions.first.amount

      assert_equal "expenditure", statement.transactions.second.category
      assert_equal "Rent", statement.transactions.second.description
      assert_equal 500_00, statement.transactions.second.amount

      assert_redirected_to affordability_statements_url
    end

    test "POST create - returns unprocessable entity when statement is invalid" do
      user = users(:one)
      login_with user.email_address, "password"

      params = {
        "affordability_statement": {
          "statement_period(1i)" => "2024",
          "statement_period(2i)" => "1",
          "transactions_attributes": {
            "0" => { "category" => "", "description" => "", "amount" => "" }
          }
        }
      }

      assert_no_difference -> { Statement.count } do
        post affordability_statements_url, params: params
      end

      assert_response :unprocessable_entity
    end

    test "GET index - displays statements" do
      user = users(:one)

      Statement.create!(
        user: user,
        statement_period: Date.new(2024, 1, 31),
        transactions_attributes: [
          { category: "income", description: "Salary", amount: 150_00 },
          { category: "expenditure", description: "Rent", amount: 50_00 }
        ]
      )

      login_with user.email_address, "password"

      get affordability_statements_url

      assert_response :success
      assert_select "h1", "Your affordability statements"
      assert_select "tbody > tr", count: 1
      assert_select "tbody > tr > td", text: "January 2024"
      assert_select "tbody > tr > td", text: "£150.00"
      assert_select "tbody > tr > td", text: "£50.00"
      assert_select "tbody > tr > td", text: "£100.00"
    end

    private

    def login_with(email_address, password)
      params = {
        user: { email_address: email_address, password: password }
      }
      post session_url, params: params
    end
  end
end
