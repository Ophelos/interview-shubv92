require "test_helper"

module Affordability
  class StatementTest < ActiveSupport::TestCase
    test "valid with a valid transaction" do
      transaction = Transaction.new(
        category: :income,
        description: "Salary",
        amount: 1000_00,
      )
      statement = Statement.new(
        user: users(:one),
        statement_period: Date.yesterday,
        transactions: [ transaction ],
      )

      assert_predicate statement, :valid?
    end

    test "invalid with no user" do
      statement = Statement.new

      assert_predicate statement, :invalid?
      assert_equal [ "must exist" ], statement.errors[:user]
    end

    test "invalid with no statement_period" do
      statement = Statement.new

      assert_predicate statement, :invalid?
      assert_equal [ "can't be blank" ], statement.errors[:statement_period]
    end

    test "invalid with future statement_period" do
      freeze_time
      statement = Statement.new(statement_period: 1.day.from_now)

      assert_predicate statement, :invalid?
      assert_equal [ "must be less than or equal to #{Date.current}" ],
        statement.errors[:statement_period]
    end

    test "invalid with no transactions" do
      statement = Statement.new

      assert_predicate statement, :invalid?
      assert_equal [ "can't be blank" ], statement.errors[:transactions]
    end

    test "invalid with invalid transactions" do
      statement = Statement.new(transactions: [ Transaction.new ])

      assert_predicate statement, :invalid?
      assert_equal [ "is invalid" ], statement.errors[:transactions]
    end
  end
end
