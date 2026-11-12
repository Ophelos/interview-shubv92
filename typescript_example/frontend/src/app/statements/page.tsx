"use client";

import { apiFetch } from "@/lib/api";
import { useRouter } from "next/navigation";
import { FormEvent, useEffect, useState } from "react";
import Transactions, { Transaction } from "./Transactions";

interface Statement {
  id: number;
  name: string;
  createdAt: Date;
  transactions: Transaction[];
}

export default function StatementsPage() {
  const router = useRouter();

  const [statements, setStatements] = useState<Statement[]>([]);
  const [newName, setNewName] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  // Transaction creation modal state
  const [modalStatementId, setModalStatementId] = useState<number | null>(null);
  const [txType, setTxType] = useState<"Income" | "Expenditure">("Income");
  const [txLabel, setTxLabel] = useState("");
  const [txAmount, setTxAmount] = useState("");
  const [txDescription, setTxDescription] = useState("");
  const [txLoading, setTxLoading] = useState(false);
  const [txError, setTxError] = useState<string | null>(null);

  useEffect(() => {
    apiFetch<Statement[]>("statements")
      .then((data) => setStatements(data))
      .catch(() => router.push("/login"));
  }, [router]);

  async function handleAddStatement(event: FormEvent) {
    event.preventDefault();
    const name = newName.trim();

    if (!name) return;

    setIsLoading(true);
    try {
      const createdStatement = await apiFetch<Statement>("statements", {
        method: "POST",
        body: JSON.stringify({ name }),
      });

      setStatements((prev) => [createdStatement, ...prev]);
      setNewName("");
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  }

  const openTransactionModal = (statementId: number) => {
    setModalStatementId(statementId);
    setTxType("Income");
    setTxLabel("");
    setTxAmount("");
    setTxDescription("");
    setTxError(null);
  };

  const closeTransactionModal = () => {
    setModalStatementId(null);
  };

  async function handleAddTransaction(event: FormEvent) {
    event.preventDefault();
    if (modalStatementId === null) return;

    const label = txLabel.trim();
    const amount = parseFloat(txAmount);

    if (!label) {
      setTxError("Label is required");
      return;
    }
    if (isNaN(amount) || amount <= 0) {
      setTxError("Amount must be greater than 0");
      return;
    }

    const amount_in_cents = Math.round(amount * 100);

    setTxLoading(true);
    setTxError(null);
    try {
      const created = await apiFetch<Transaction[]>(
        `statements/${modalStatementId}/transactions`,
        {
          method: "POST",
          body: JSON.stringify([
            {
              type: txType,
              amount_in_cents,
              label,
              ...(txDescription.trim() ? { description: txDescription.trim() } : {}),
            },
          ]),
        },
      );

      setStatements((prev) =>
        prev.map((statement) =>
          statement.id === modalStatementId
            ? { ...statement, transactions: [...created, ...statement.transactions] }
            : statement,
        ),
      );
      closeTransactionModal();
    } catch (error) {
      setTxError(error instanceof Error ? error.message : "Failed to add transaction");
    } finally {
      setTxLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
      <div className="max-w-6xl mx-auto p-6">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">My Financial Statements</h1>
          <p className="text-gray-600">
            Track your income and expenses across different statements
          </p>
        </div>

        {/* Add Statement Form */}
        <div className="bg-white rounded-2xl shadow-lg p-6 mb-8">
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Create New Statement</h2>
          <form onSubmit={handleAddStatement} className="flex gap-4">
            <input
              type="text"
              placeholder="Enter statement name (e.g., 'Monthly Budget', 'Vacation Fund')"
              value={newName}
              onChange={(event) => setNewName(event.target.value)}
              className="flex-1 px-4 py-3 border border-gray-300 rounded-lg bg-white text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
            />
            <button
              type="submit"
              disabled={isLoading || !newName.trim()}
              className="px-6 py-3 bg-gradient-to-r from-green-600 to-green-700 text-white rounded-lg hover:from-green-700 hover:to-green-800 transition-all duration-200 transform hover:scale-105 shadow-lg hover:shadow-xl font-semibold disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
            >
              {isLoading ? (
                <div className="flex items-center">
                  <svg
                    className="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                  >
                    <circle
                      className="opacity-25"
                      cx="12"
                      cy="12"
                      r="10"
                      stroke="currentColor"
                      strokeWidth="4"
                    ></circle>
                    <path
                      className="opacity-75"
                      fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                    ></path>
                  </svg>
                  Creating...
                </div>
              ) : (
                "Create Statement"
              )}
            </button>
          </form>
        </div>

        {/* Statements List */}
        {Array.isArray(statements) && statements.length === 0 ? (
          <div className="bg-white rounded-2xl shadow-lg p-12 text-center">
            <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg
                className="w-8 h-8 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                />
              </svg>
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">No Statements Yet</h3>
            <p className="text-gray-600">
              Create your first statement to start tracking your finances
            </p>
          </div>
        ) : (
          <div className="space-y-6">
            {statements.map((statement) => (
              <div key={statement.id} className="bg-white rounded-2xl shadow-lg overflow-hidden">
                {/* Statement Header */}
                <div className="bg-gradient-to-r from-blue-600 to-green-600 px-6 py-4">
                  <div className="flex items-center justify-between">
                    <div>
                      <h2 className="text-xl font-bold text-white">{statement.name}</h2>
                      <p className="text-blue-100 text-sm">
                        Created{" "}
                        {new Date(statement.createdAt).toLocaleDateString("en-US", {
                          year: "numeric",
                          month: "long",
                          day: "numeric",
                        })}
                      </p>
                    </div>
                    <div className="text-right">
                      <div className="text-white font-semibold">
                        {statement.transactions.length} transaction
                        {statement.transactions.length !== 1 ? "s" : ""}
                      </div>
                    </div>
                  </div>
                </div>

                {/* Transactions Component */}
                <div className="p-6">
                  <Transactions
                    transactions={statement.transactions}
                    onCreateTransaction={() => openTransactionModal(statement.id)}
                  />
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Add Transaction Modal */}
      {modalStatementId !== null && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
          <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-semibold text-gray-900">Add Transaction</h2>
              <button
                onClick={closeTransactionModal}
                className="text-gray-400 hover:text-gray-600 text-2xl leading-none"
                aria-label="Close"
              >
                &times;
              </button>
            </div>

            <form onSubmit={handleAddTransaction} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Type</label>
                <select
                  value={txType}
                  onChange={(event) => setTxType(event.target.value as "Income" | "Expenditure")}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="Income">Income</option>
                  <option value="Expenditure">Expenditure</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Label</label>
                <input
                  type="text"
                  placeholder="e.g., Salary, Rent"
                  value={txLabel}
                  onChange={(event) => setTxLabel(event.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Amount</label>
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  placeholder="0.00"
                  value={txAmount}
                  onChange={(event) => setTxAmount(event.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Description <span className="text-gray-400">(optional)</span>
                </label>
                <input
                  type="text"
                  placeholder="Add a note"
                  value={txDescription}
                  onChange={(event) => setTxDescription(event.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              {txError && <p className="text-sm text-red-600">{txError}</p>}

              <div className="flex justify-end gap-3 pt-2">
                <button
                  type="button"
                  onClick={closeTransactionModal}
                  className="px-4 py-2 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50 transition"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={txLoading}
                  className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {txLoading ? "Adding..." : "Add Transaction"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </main>
  );
}
