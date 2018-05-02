package ashu.rento.network;

import ashu.rento.model.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by apple on 01/05/18.
 */

public interface NetworkService {

    @GET("discover/movie")
    Call<MoviesResponse> getMovies(@Query("api_key") String apiKey, @Query("page") int page);
}
