class CreateTransactions < ActiveRecord::Migration[8.0]
  def change
    create_table :transactions do |t|
      t.references :statement, null: false, foreign_key: true
      t.integer :category, null: false
      t.string :description, null: false
      t.integer :amount, null: false

      t.timestamps
    end
  end
end
