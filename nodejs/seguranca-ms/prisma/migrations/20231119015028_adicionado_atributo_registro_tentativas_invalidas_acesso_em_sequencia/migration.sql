-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Password" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "userId" TEXT NOT NULL,
    "password" TEXT NOT NULL,
    "expire" DATETIME,
    "invalidAttempt" INTEGER NOT NULL DEFAULT 0,
    "created" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "active" BOOLEAN NOT NULL
);
INSERT INTO "new_Password" ("active", "created", "expire", "id", "password", "userId") SELECT "active", "created", "expire", "id", "password", "userId" FROM "Password";
DROP TABLE "Password";
ALTER TABLE "new_Password" RENAME TO "Password";
CREATE UNIQUE INDEX "Password_userId_password_key" ON "Password"("userId", "password");
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
