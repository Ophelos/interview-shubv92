module Affordability
  class Statement < ApplicationRecord
    belongs_to :user

    has_many :transactions

    accepts_nested_attributes_for :transactions, reject_if: :all_blank

    validates :statement_period, presence: true
    validates :statement_period,
      comparison: { less_than_or_equal_to: -> { Date.current } },
      if: -> { statement_period.present? }

    validates :transactions, presence: true

    validates_associated :transactions

    def total_income
      transactions.income.sum(:amount)
    end

    def total_expenditure
      transactions.expenditure.sum(:amount)
    end

    def disposable_income
      total_income - total_expenditure
    end

    def statement_period=(value)
      if value.is_a?(Hash)
        year, month, _day = value.values
        super Date.new(year, month, -1)
      else
        super
      end
    rescue TypeError, Date::Error
      nil
    end
  end
end
