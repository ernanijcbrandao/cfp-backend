-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Account" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "name" TEXT NOT NULL,
    "description" TEXT,
    "owner" TEXT NOT NULL,
    "created" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "lastUpdate" DATETIME,
    "openingBalance" TEXT NOT NULL,
    "active" BOOLEAN NOT NULL
);
INSERT INTO "new_Account" ("active", "created", "description", "id", "lastUpdate", "name", "openingBalance", "owner") SELECT "active", "created", "description", "id", "lastUpdate", "name", "openingBalance", "owner" FROM "Account";
DROP TABLE "Account";
ALTER TABLE "new_Account" RENAME TO "Account";
CREATE UNIQUE INDEX "Account_name_key" ON "Account"("name");
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
