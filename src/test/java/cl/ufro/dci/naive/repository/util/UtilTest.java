package cl.ufro.dci.naive.repository.util;

import java.io.File;
import java.nio.file.Paths;

public class UtilTest {
    public static final File DATA_JSON_ACCESS = Paths.get("src", "test", "resources", "domain", "access.json").toFile();
    public static final File DATA_JSON_ACCESSLOGS = Paths.get("src", "test", "resources", "domain", "accesslogs.json").toFile();
    public static final File DATA_JSON_ACCOUNTS = Paths.get("src", "test", "resources", "domain", "accounts.json").toFile();
    public static final File DATA_JSON_CUSTOMERS = Paths.get("src", "test", "resources", "domain", "customers.json").toFile();
    public static final File DATA_JSON_KEYSTATES = Paths.get("src", "test", "resources", "domain", "keystates.json").toFile();
    public static final File DATA_JSON_TRANSACTIONS = Paths.get("src", "test", "resources", "domain", "transactions.json").toFile();
    public static final File DATA_JSON_TRANSACTIONSTATES = Paths.get("src", "test", "resources", "domain", "transactionstates.json").toFile();
}
