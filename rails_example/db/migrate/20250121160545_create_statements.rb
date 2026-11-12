class CreateStatements < ActiveRecord::Migration[8.0]
  def change
    create_table :statements do |t|
      t.references :user, null: false, foreign_key: true
      t.date :statement_period, null: false

      t.timestamps
    end
  end
end
