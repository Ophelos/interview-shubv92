module Affordability
  class TransactionTest < ActiveSupport::TestCase
    test "valid with valid attributes" do
      transaction = Transaction.new(
        statement: Statement.new,
        category: :income,
        description: "Salary",
        amount: 1000_00,
      )

      assert_predicate transaction, :valid?
    end

    test "invalid with no statement" do
      transaction = Transaction.new

      assert_predicate transaction, :invalid?
      assert_equal [ "must exist" ], transaction.errors[:statement]
    end

    test "invalid with no category" do
      transaction = Transaction.new

      assert_predicate transaction, :invalid?
      assert_equal [ "is not included in the list" ], transaction.errors[:category]
    end

    test "invalid with no description" do
      transaction = Transaction.new

      assert_predicate transaction, :invalid?
      assert_equal [ "can't be blank" ], transaction.errors[:description]
    end

    test "invalid with no amount" do
      transaction = Transaction.new

      assert_predicate transaction, :invalid?
      assert_equal [ "can't be blank" ], transaction.errors[:amount]
    end

    test "invalid with non-integer amount" do
      transaction = Transaction.new(amount: 1.23)

      assert_predicate transaction, :invalid?
      assert_equal [ "must be an integer" ], transaction.errors[:amount]
    end

    test "invalid with negative amount" do
      transaction = Transaction.new(amount: -1)

      assert_predicate transaction, :invalid?
      assert_equal [ "must be greater than 0" ], transaction.errors[:amount]
    end
  end
end
