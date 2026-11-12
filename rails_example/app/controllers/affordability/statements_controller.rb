module Affordability
  class StatementsController < ApplicationController
    def new
      @statement = Statement.new(user: Current.user)
      5.times { @statement.transactions.build }
    end

    def create
      @statement = Statement.new(statement_params.merge(user: Current.user))

      if @statement.save
        redirect_to affordability_statements_path
      else
        5.times { @statement.transactions.build }
        render :new, status: :unprocessable_entity
      end
    end

    def index
      @statements = Statement.where(user: Current.user).order(created_at: :desc)
    end

    private

    def statement_params
      params.expect(
        affordability_statement:
          [ :statement_period, transactions_attributes: [ [ :category, :description, :amount ] ] ]
        )
    end
  end
end
