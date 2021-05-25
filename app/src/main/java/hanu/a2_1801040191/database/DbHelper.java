package hanu.a2_1801040191.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Mycart";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DbSchema.ProductsTable.TABLE_NAME + "(" +
                DbSchema.ProductsTable.Cols.PRODUCT_ID +" INTEGER PRIMARY KEY, " +
                DbSchema.ProductsTable.Cols.PRODUCT_NAME + " TEXT, " +
                DbSchema.ProductsTable.Cols.THUMBNAIL + " TEXT," +
                DbSchema.ProductsTable.Cols.UNIT_PRICE + " DOUBLE, "+
                DbSchema.ProductsTable.Cols.QUANTITY + " INTEGER" + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.ProductsTable.TABLE_NAME);

        onCreate(db);
    }
}
