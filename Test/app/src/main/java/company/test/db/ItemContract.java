package company.test.db;

import android.provider.BaseColumns;

public class ItemContract {

    public static final String DB_NAME = "com.example.TodoList.db.items";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "items";

    public class Columns {
        public static final String ITEM = "item";
        public static final String _ID = BaseColumns._ID;
    }
}
