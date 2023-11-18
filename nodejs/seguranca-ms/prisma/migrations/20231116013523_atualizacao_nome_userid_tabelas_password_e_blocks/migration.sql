/*
  Warnings:

  - You are about to drop the column `user` on the `Blocks` table. All the data in the column will be lost.
  - You are about to drop the column `user` on the `Password` table. All the data in the column will be lost.
  - Added the required column `userId` to the `Blocks` table without a default value. This is not possible if the table is not empty.
  - Added the required column `userId` to the `Password` table without a default value. This is not possible if the table is not empty.

*/
-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Blocks" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "userId" TEXT NOT NULL,
    "reason" TEXT NOT NULL,
    "blocked" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "expire" DATETIME NOT NULL,
    "active" BOOLEAN NOT NULL
);
INSERT INTO "new_Blocks" ("active", "blocked", "expire", "id", "reason") SELECT "active", "blocked", "expire", "id", "reason" FROM "Blocks";
DROP TABLE "Blocks";
ALTER TABLE "new_Blocks" RENAME TO "Blocks";
CREATE TABLE "new_Password" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "userId" TEXT NOT NULL,
    "password" TEXT NOT NULL,
    "reason" TEXT NOT NULL,
    "expire" DATETIME,
    "created" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "active" BOOLEAN NOT NULL
);
INSERT INTO "new_Password" ("active", "created", "expire", "id", "password", "reason") SELECT "active", "created", "expire", "id", "password", "reason" FROM "Password";
DROP TABLE "Password";
ALTER TABLE "new_Password" RENAME TO "Password";
CREATE UNIQUE INDEX "Password_userId_password_key" ON "Password"("userId", "password");
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
