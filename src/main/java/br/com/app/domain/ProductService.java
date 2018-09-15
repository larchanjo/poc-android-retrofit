package br.com.app.domain;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductService {

  @GET("products")
  Call<List<Product>> getProducts();

  @GET("products/{id}")
  Call<Product> get(@Path("id") Integer id);

  @POST("products")
  Call<Void> post(@Body Product product);

  @PUT("products/{id}")
  Call<Void> put(@Path("id") Integer id, @Body Product product);

  @DELETE("products/{id}")
  Call<Void> delete(@Path("id") Integer id);

}
