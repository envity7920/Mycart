package hanu.a2_1801040191;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.util.List;

import hanu.a2_1801040191.adapters.CartAdapter;
import hanu.a2_1801040191.database.ProductManager;
import hanu.a2_1801040191.models.Product;

public class CartActivity extends AppCompatActivity {

    RecyclerView orderList;
    public TextView totalPrice;
    List<Product> cartList;
    CartAdapter cartAdapter;
    ProductManager productManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"Aqua\">" +getString(R.string.app_name)+"</font>"));

        productManager = ProductManager.getInstance(this);

       mapping();
        totalPrice.setText(productManager.getTotalPrice()+ " VND");

        productManager = ProductManager.getInstance(this);
        cartList = productManager.getAllProducts();

        orderList.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartList, this);
        orderList.setAdapter(cartAdapter);

    }
    public  void  mapping(){
        orderList = findViewById(R.id.orderList);
        totalPrice = findViewById(R.id.orderTotalPrice);
    }
}