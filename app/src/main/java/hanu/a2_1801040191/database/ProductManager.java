package hanu.a2_1801040191.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_1801040191.models.Product;

public class ProductManager {

    private static ProductManager productManager;
    private DbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    private static final String INSERT = "INSERT INTO " +
            DbSchema.ProductsTable.TABLE_NAME +"(" + DbSchema.ProductsTable.Cols.PRODUCT_ID +"," +
            DbSchema.ProductsTable.Cols.PRODUCT_NAME +","+
            DbSchema.ProductsTable.Cols.THUMBNAIL +","+
            DbSchema.ProductsTable.Cols.UNIT_PRICE +","+
            DbSchema.ProductsTable.Cols.QUANTITY +
            ") VALUES (?, ?, ?,  ?, ?)";

    public static ProductManager getInstance(Context context){
        if(productManager == null){
            productManager = new ProductManager(context);
        }
        return productManager;
    }

    public ProductManager(Context context){
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public boolean addProduct(Product product){
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(INSERT);
        sqLiteStatement.bindLong(1, product.getId());
        sqLiteStatement.bindString(2, product.getName());
        sqLiteStatement.bindString(3, product.getThumbnail());
        sqLiteStatement.bindDouble(4, product.getUnitPrice());
        sqLiteStatement.bindString(5, product.getQuantity() +"");

        long isAdded = sqLiteStatement.executeInsert();
        return isAdded > 0;
    }

    public boolean updateProduct(Product product){
        ContentValues contentValues = new ContentValues();

        contentValues.put(DbSchema.ProductsTable.Cols.PRODUCT_ID, product.getId());
        contentValues.put(DbSchema.ProductsTable.Cols.PRODUCT_NAME, product.getName());
        contentValues.put(DbSchema.ProductsTable.Cols.THUMBNAIL, product.getThumbnail());
        contentValues.put(DbSchema.ProductsTable.Cols.UNIT_PRICE, product.getUnitPrice());
        contentValues.put(DbSchema.ProductsTable.Cols.QUANTITY, product.getQuantity());

        int isUpdated = sqLiteDatabase.update(DbSchema.ProductsTable.TABLE_NAME, contentValues,DbSchema.ProductsTable.Cols.PRODUCT_ID + "= ?", new String[] { product.getId() + "" });
        return isUpdated > 0;
    }

    public boolean deleteProduct(long productId){
        int isDeleted = sqLiteDatabase.delete(DbSchema.ProductsTable.TABLE_NAME,
                DbSchema.ProductsTable.Cols.PRODUCT_ID + "= ?", new String[]{ productId + "" } );

        return isDeleted> 0;
    }

    public List<Product> getAllProducts(){
        String sql = "SELECT * FROM " + DbSchema.ProductsTable.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);

        return cursorWrapper.getAllProducts();
    }

    public Product findProductById(long productId){
        String sql = "SELECT * FROM " + DbSchema.ProductsTable.TABLE_NAME + " WHERE "+ DbSchema.ProductsTable.Cols.PRODUCT_ID + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{productId+""});

        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);

        return cursorWrapper.getProductByID();
    }

    public double getTotalPrice(){
        String sql = "SELECT SUM ("+ DbSchema.ProductsTable.Cols.QUANTITY +" * "  + DbSchema.ProductsTable.Cols.UNIT_PRICE +  ") AS total FROM " + DbSchema.ProductsTable.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        int totalPrice;
        cursor.moveToFirst();

        totalPrice = cursor.getInt(0);
        return totalPrice;
    }
}
