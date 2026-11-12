module Affordability
  class Transaction < ApplicationRecord
    belongs_to :statement

    enum :category, [ :income, :expenditure ], validate: true

    validates :description, presence: true

    validates :amount, presence: true
    validates :amount,
      numericality: { greater_than: 0, only_integer: true },
      if: -> { amount.present? }
  end
end
