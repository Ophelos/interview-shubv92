# Exercise: Affordability Report

> This brief is **stack-agnostic**. Pick the example you're most comfortable in —
> `typescript_example`, `python_example`, or `java_example`. The concepts below
> exist in all three; only the names differ (see the mapping table).

## Background

Each of these applications records a customer's **income & expenditure (I&E)
statement** — a snapshot of what a person earns and spends. The purpose is to
work out how much they can realistically afford to pay: their **disposable
income**.

Core concepts, common to every example:

- A **statement** belongs to a user and represents one I&E snapshot.
- A statement has a number of **income** items and **expenditure** items.
- Each item has an **amount**. Amounts are stored as **positive integers in the
  minor currency unit (pence)** — e.g. `150000` = £1,500.00 — and are never
  negative. Whether an amount counts as income or expenditure is determined by
  its **type/category**, not by its sign.

Today each app can create statements and view a single statement, but viewing a
statement only shows the raw items. There is no summary.

### Where each concept lives

| Concept | `typescript_example` | `python_example` | `java_example` |
|---|---|---|---|
| Statement | `Statement` (Prisma) | `Statement` model | `Statement` / `StatementEntity` |
| Income/expenditure | one `Transaction` with `type` = `Income`/`Expenditure` | one `Transaction` with `category` = `income`/`expenditure` | separate `Income` and `Expenditure` entities |
| Amount field | `amount_in_cents` | `amount` (pence) | `amount` (pence) |
| Statement period | `name` (free text) | `statement_period` (a month) | `reportedDate` |

Spend a few minutes reading the model and the statement-detail view in your
chosen example before you start.

## Your task

Add an **affordability report** to the single-statement view.

The report summarises a statement so someone can see at a glance whether the
person has money left over, and how much.

### Core requirements (must do)

For a given statement, compute and display:

1. **Total income** — sum of all income amounts.
2. **Total expenditure** — sum of all expenditure amounts.
3. **Disposable income** — `total income − total expenditure`.
4. **Affordability rating** — derive a simple rating/band for the statement from
   disposable income as a share of total income (the "surplus ratio"), and
   display it (e.g. a label and/or colour):

   | Rating | Condition (suggested) |
   |---|---|
   | **Deficit** | disposable income < 0 |
   | **At risk** | surplus ratio ≤ 10% |
   | **Stable** | 10% < surplus ratio ≤ 30% |
   | **Comfortable** | surplus ratio > 30% |

   where `surplus_ratio = disposable_income / total_income`. The exact thresholds
   are yours to choose — we're interested in your reasoning, not the specific
   numbers.

Display all monetary values in pounds (e.g. `£1,500.00`), not raw pence.

**Acceptance criteria**

- Figures are correct for a statement with a mix of income and expenditure.
- The rating reflects the figures, including the boundary cases you chose.
- When **total income is 0**, the rating does not crash on divide-by-zero
  (treat as Deficit / at risk) — handle it explicitly.
- A statement with **no items** shows zeros, not an error.
- The report only covers the **logged-in user's own** statement (don't regress
  the existing access control).
- Income vs expenditure is determined by **type/category**, never by assuming
  negative amounts.

### Target (if you have time)

5. **Affordable payment** — what we'd suggest the person pays, as a share of
   disposable income. Use a **named constant** for the rate (e.g. 50%):
   `affordable_payment = max(0, round(disposable_income * RATE))`.
6. **Deficit handling** — if disposable income is negative, show the person is
   **in deficit** and the affordable payment is £0 (never negative).

### Stretch (optional — pick what interests you)

- **Trend** — across the user's statements, show how disposable income changes
  over time (ordered by statement period). A table or a simple chart is fine.
- **Export** — download the report (or the items) as CSV.
- **Tests** — cover the calculation and its edge cases (empty statement,
  deficit).

## What we're assessing

- **Correctness** of the calculation, including the edge cases.
- **Placement of logic** — is the calculation in a sensible, testable place
  (a model method / service / helper) rather than buried in the view/template?
- **Readability** — does your code match the conventions already in the app?
- **Tests** — sensible and minimal; use the test setup the project already has.
- **Money handling** — clear separation of pence (storage) and pounds (display).

You are **not** expected to finish the stretch goals. We care more about how you
approach the core than how far you get.

## Getting set up

Follow the `README.md` in your chosen example to install, seed, and run it, then
log in with the credentials it lists. Create a statement with a few income and
expenditure items so you have data to report on, and run that example's test
suite before you begin.

## Time

Aim for **30–40 minutes** on the core + target. Stretch goals are genuinely
optional — if you run out of time, leave a short note on what you'd do next.
