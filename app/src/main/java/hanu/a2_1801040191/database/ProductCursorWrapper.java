package hanu.a2_1801040191.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040191.models.Product;

public class ProductCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }
        // retrieve data
    public Product getProduct(){
        int id = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.PRODUCT_ID));
        String name = getString(getColumnIndex(DbSchema.ProductsTable.Cols.PRODUCT_NAME));
        String thumbnail = getString(getColumnIndex(DbSchema.ProductsTable.Cols.THUMBNAIL));
        int unitPrice = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.UNIT_PRICE));
        int quantity = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.QUANTITY));

        Product product = new Product(id, name, thumbnail, unitPrice, quantity);
        return product;
    }
    // get a list of products
    public List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()){
            Product product = getProduct();
            products.add(product);
            moveToNext();
        }
        return products;
    }


    public Product getProductByID(){
        Product product = null;
        moveToFirst();
        if(!isAfterLast()){
            product = getProduct();
        }
        return product;
    }
}
