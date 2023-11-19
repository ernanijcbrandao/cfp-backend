-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Blocks" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "userId" TEXT NOT NULL,
    "reason" TEXT NOT NULL,
    "blocked" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "expire" DATETIME,
    "active" BOOLEAN NOT NULL
);
INSERT INTO "new_Blocks" ("active", "blocked", "expire", "id", "reason", "userId") SELECT "active", "blocked", "expire", "id", "reason", "userId" FROM "Blocks";
DROP TABLE "Blocks";
ALTER TABLE "new_Blocks" RENAME TO "Blocks";
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
