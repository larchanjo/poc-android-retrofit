package br.com.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.com.app.domain.Product;
import br.com.app.domain.ProductService;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

  private Button getButton;
  private Button postButton;
  private Button putButton;
  private Button deleteButton;
  private final ProductService productService;
  private final Product randomProduct;

  public MainActivity() {
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    httpLoggingInterceptor.setLevel(Level.BODY);

    OkHttpClient httpClient = new OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://5b9d5606a4647e0014745172.mockapi.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build();

    this.productService = retrofit.create(ProductService.class);

    Product product = new Product();
    product.setId(1);
    product.setCreatedAt(Calendar.getInstance().getTime());
    product.setName(UUID.randomUUID().toString());
    product.setPrice(Math.random());

    this.randomProduct = product;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    bindComponents();
    configureGetButton();
    configurePostButton();
    configurePutButton();
    configureDeleteButton();
  }

  private void bindComponents() {
    logInfo("Binding components");
    this.getButton = findViewById(R.id.buttonGet);
    this.postButton = findViewById(R.id.buttonPost);
    this.putButton = findViewById(R.id.buttonPut);
    this.deleteButton = findViewById(R.id.buttonDelete);
  }

  private void configureGetButton() {
    logInfo("Configurating GetButton");
    this.getButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Runnable runnable = new Runnable() {
          @Override
          public void run() {
            try {
              Call<List<Product>> call = productService.getProducts();
              Response<List<Product>> response = call.execute();
              List<Product> products = response.body();
            } catch (IOException e) {
              logError("Error to execute GET", e);
            }
          }
        };

        new AsyncExecutor(runnable).execute();
      }
    });
  }

  private void configurePostButton() {
    logInfo("Configurating PostButton");
    this.postButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Runnable runnable = new Runnable() {
          @Override
          public void run() {
            try {
              Call<Void> call = productService.post(randomProduct);
              call.execute();
            } catch (IOException e) {
              logError("Error to execute POST", e);
            }
          }
        };

        new AsyncExecutor(runnable).execute();
      }
    });
  }

  private void configurePutButton() {
    logInfo("Configurating PutButton");
    this.putButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Runnable runnable = new Runnable() {
          @Override
          public void run() {
            try {
              randomProduct.setName(UUID.randomUUID().toString());
              Call<Void> call = productService.put(randomProduct.getId(), randomProduct);
              call.execute();
            } catch (IOException e) {
              logError("Error to execute PUT", e);
            }
          }
        };

        new AsyncExecutor(runnable).execute();
      }
    });
  }

  private void configureDeleteButton() {
    logInfo("Configurating DeleteButton");
    this.deleteButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Runnable runnable = new Runnable() {
          @Override
          public void run() {
            try {
              Call<Void> call = productService.delete(randomProduct.getId());
              call.execute();
            } catch (IOException e) {
              logError("Error to execute DELETE", e);
            }
          }
        };

        new AsyncExecutor(runnable).execute();
      }
    });
  }

  private void showGenericMessage() {
    Toast.makeText(getApplicationContext(), "Operation successfully completed", Toast.LENGTH_LONG).show();
  }

  private void logError(@NonNull String message, @NonNull Throwable throwable) {
    Log.e(getTag(), message, throwable);
  }

  private void logInfo(@NonNull String message) {
    Log.i(getTag(), message);
  }

  private String getTag() {
    return MainActivity.class.getSimpleName();
  }

  @Data
  public class AsyncExecutor extends AsyncTask<Runnable, Void, Void> {

    private final Runnable runnable;

    public AsyncExecutor(Runnable runnable) {
      this.runnable = runnable;
    }

    @Override
    protected Void doInBackground(Runnable... runnables) {
      runnable.run();
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      showGenericMessage();
    }
  }

}