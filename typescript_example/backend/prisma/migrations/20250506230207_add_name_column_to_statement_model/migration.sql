/*
  Warnings:

  - Added the required column `name` to the `Statement` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE "Statement" ADD COLUMN     "name" TEXT NOT NULL;
