package hanu.a2_1801040191.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import hanu.a2_1801040191.CartActivity;
import hanu.a2_1801040191.R;
import hanu.a2_1801040191.database.ProductManager;
import hanu.a2_1801040191.models.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    List<Product> cartList;
    private CartActivity cartActivity;

    public CartAdapter(List<Product> cartList, CartActivity cartActivity){
        this.cartList = cartList;
        this.cartActivity = cartActivity;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_product, parent, false);
        return new CartHolder(itemView, context);
    }


    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Product product = cartList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        private TextView nameRow, unitPriceRow, quantityRow, priceRow;
        private ImageButton addBtn, minusBtn;
        private ImageView thumbnailRow;
        private Button checkoutBtn;
        private Context context;

        public CartHolder(@NonNull View itemView, Context context) {
            super(itemView);
              mapping();
            this.context = context;
        }

        public void bind(Product product){
            nameRow.setText(product.getName());
            unitPriceRow.setText(product.getUnitPrice()+" VND");
            priceRow.setText(product.getPrice()+" VND");
            quantityRow.setText(product.getQuantity()+"");


            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductManager productManager = ProductManager.getInstance(context);

                    boolean isUpdated = false;
                    product.increaseQuantity();
                    isUpdated = productManager.updateProduct(product);


                    if( isUpdated){
                        cartActivity.totalPrice.setText(productManager.getTotalPrice()+" VND");
                    }else{
                        Toast.makeText(context, "Add fail", Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                }

            });

            minusBtn.setOnClickListener(new View.OnClickListener() {
                ProductManager productManager = ProductManager.getInstance(context);

                @Override
                public void onClick(View v) {
                    if(product.getQuantity() ==  1){
                        new AlertDialog.Builder(context)
                                .setTitle("Delete product")
                                .setMessage("Deleting a product from the cart cannot be undone, are you sure you want to delete it?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        boolean isDeleted = productManager.deleteProduct(product.getId());
                                        if(isDeleted){
                                            cartList.remove(product);
                                            cartActivity.totalPrice.setText(productManager.getTotalPrice()+" VND");
                                            Toast.makeText(context, "The product has been removed from the cart", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(context, "Delete failed!", Toast.LENGTH_SHORT).show();
                                        }
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();


                    }else{
                        product.decreaseQuantity();
                        productManager.updateProduct(product);
                        cartActivity.totalPrice.setText(productManager.getTotalPrice()+ " VND");
                        notifyDataSetChanged();
                    }
                }
            });

            ThumbnailLoader thumbnailLoader = new ThumbnailLoader();
            thumbnailLoader.execute(product.getThumbnail());

        }

        public class ThumbnailLoader extends AsyncTask<String, Void, Bitmap> {
            URL imgURL;
            HttpURLConnection urlConnection;

            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    imgURL = new URL(strings[0]);
                    urlConnection = (HttpURLConnection) imgURL.openConnection();
                    urlConnection.connect();

                    InputStream is = urlConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    return bitmap;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                thumbnailRow.setImageBitmap(bitmap);
            }
        }
        public void mapping(){
            nameRow = itemView.findViewById(R.id.nameRow);
            unitPriceRow = itemView.findViewById(R.id.unitPriceRow);
            quantityRow = itemView.findViewById(R.id.quantityRow);
            priceRow = itemView.findViewById(R.id.sumPriceRow);
            addBtn = itemView.findViewById(R.id.addBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            thumbnailRow = itemView.findViewById(R.id.thumbnailRow);
            checkoutBtn = itemView.findViewById(R.id.checkoutBtn);
        };
    }

}
