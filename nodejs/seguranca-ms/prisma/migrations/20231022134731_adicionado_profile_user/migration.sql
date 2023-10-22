/*
  Warnings:

  - Added the required column `profile` to the `User` table without a default value. This is not possible if the table is not empty.

*/
-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_User" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "name" TEXT NOT NULL,
    "email" TEXT NOT NULL,
    "login" TEXT NOT NULL,
    "publickey" TEXT NOT NULL,
    "profile" TEXT NOT NULL,
    "created" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "lastUpdate" DATETIME,
    "active" BOOLEAN NOT NULL
);
INSERT INTO "new_User" ("active", "created", "email", "id", "lastUpdate", "login", "name", "publickey") SELECT "active", "created", "email", "id", "lastUpdate", "login", "name", "publickey" FROM "User";
DROP TABLE "User";
ALTER TABLE "new_User" RENAME TO "User";
CREATE UNIQUE INDEX "User_email_key" ON "User"("email");
CREATE UNIQUE INDEX "User_login_key" ON "User"("login");
CREATE UNIQUE INDEX "User_publickey_key" ON "User"("publickey");
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
