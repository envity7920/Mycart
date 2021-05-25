package hanu.a2_1801040191.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040191.R;
import hanu.a2_1801040191.database.ProductManager;
import hanu.a2_1801040191.models.Product;

public class StallAdapter extends RecyclerView.Adapter<StallAdapter.ShopHolder> implements Filterable {

    List<Product> products;
    List<Product> searchedProducts;
    ProductManager productManager;

    public StallAdapter(List<Product> products) {
        this.products = products;
        this.searchedProducts = products;
    }

    @NonNull
    @Override
    public StallAdapter.ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.stall_product, parent, false);

        return new StallAdapter.ShopHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull StallAdapter.ShopHolder holder, int position) {
        Product product = searchedProducts.get(position);

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return searchedProducts.size();
    }


    public class ShopHolder extends RecyclerView.ViewHolder {
        private TextView name, price;
        private ImageButton addToCartBtn;
        private ImageView thumbnail;
        private Context context;

        public ShopHolder(@NonNull View itemView, Context context) {
            super(itemView);
           mapping();

            this.context = context;
        }

        public void bind(Product product) {
            name.setText(product.getName());
            price.setText(product.getUnitPrice() + " VND");

            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productManager = ProductManager.getInstance(context);
                    boolean isAdded = false;
                    boolean isUpdated = false;
                    Product productTmp = productManager.findProductById(product.getId());
                    if(productTmp == null){
                        product.increaseQuantity();
                        isAdded = productManager.addProduct(product);
                    }else{
                        productTmp.increaseQuantity();
                        isUpdated = productManager.updateProduct(productTmp);
                    }

                    if(isAdded || isUpdated){
                        Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Add Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(product.getThumbnail());
        }

        public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
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
                thumbnail.setImageBitmap(bitmap);
            }
        }
    public  void mapping(){
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
    }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString();
                if (keyword.isEmpty()) {
                    searchedProducts = products;
                } else {
                    List<Product> resultsList = new ArrayList<>();
                    for (Product product : products) {
                        if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                            resultsList.add(product);
                        }
                    }

                    searchedProducts = resultsList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = searchedProducts;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                searchedProducts = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
