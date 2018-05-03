package ashu.rento.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import ashu.rento.R;
import ashu.rento.adapter.MovieAdapter;
import ashu.rento.model.MovieDTO;
import ashu.rento.model.MoviesResponse;
import ashu.rento.network.NetworkService;
import ashu.rento.network.RetrofitProvider;
import ashu.rento.utils.Constant;
import ashu.rento.view.MainActivity;
import ashu.rento.view.MainView;
//import io.realm.Realm;
//import io.realm.RealmResults;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by apple on 02/05/18.
 */

public class MainPresenter implements Callback<MoviesResponse>{

    private Context context;
    private MainView mainView;
    private static int count = 1;
    private Realm realm;
    private List<MovieDTO> m;

    public MainPresenter(Context context, MainView mainView){
        this.context = context;
        this.mainView = mainView;

        realm = Realm.getDefaultInstance();

    }

    public void onDestroy(){
        mainView = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getListOfMovies(int n){

        Retrofit retrofit = RetrofitProvider.getClient();

        NetworkService networkInterface = retrofit.create(NetworkService.class);

        if(fetchData() == 0 ||  n % fetchData() == 0){
            Call<MoviesResponse> resultDTOCall = networkInterface.getMovies(Constant.KEY, ++count);
            resultDTOCall.enqueue(this);
        }
        else{
            RealmResults<MovieDTO> results = realm.where(MovieDTO.class).findAll();
            updateUI(results);
        }

    }


    public int fetchData(){
        RealmResults<MovieDTO> results = realm.where(MovieDTO.class).findAll();
        if(results.size() > 0){
            return results.size();
        }
        else
            return 0;
    }




    @Override
    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
        MoviesResponse moviesResponse = (MoviesResponse) response.body();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(moviesResponse.getResults());
        realm.commitTransaction();

        updateUI(moviesResponse.getResults());
    }


    @Override
    public void onFailure(Call<MoviesResponse> call, Throwable t) {
    }

    public void updateUI(List<MovieDTO> moviesResponse){
        if(mainView != null && moviesResponse != null) {
            if(m == null) {
                m = new ArrayList<>();
                m.addAll(moviesResponse);
                mainView.populateAdapter(m, 0);
            }
            else{
                m.addAll(moviesResponse);
                mainView.populateAdapter(m, 1);
            }
        }
    }

    public void displayDialog(int pos){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_item);
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;
        ImageView imgMovie;
        LinearLayout bottomSheet;

        BottomSheetBehavior bv;

        movieTitle = (TextView) dialog.findViewById(R.id.title);
        data = (TextView) dialog.findViewById(R.id.subtitle);
        movieDescription = (TextView) dialog.findViewById(R.id.description);
        rating = (TextView) dialog.findViewById(R.id.rating);
        imgMovie = (ImageView) dialog.findViewById(R.id.imgMovie);


        MovieDTO itemDTO = m.get(pos);

        Glide.with(context)
                .load(Constant.POSTER_URL + itemDTO.getPosterPath())
                .centerCrop()
                .override(getWidth(),getHeight() /2)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgMovie);
        movieTitle.setText(itemDTO.getTitle());
        data.setText(itemDTO.getReleaseDate());
        movieDescription.setText(itemDTO.getOverview());
        rating.setText(itemDTO.getVoteAverage().toString());

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        if(!dialog.isShowing())
            dialog.show();
    }

    private int getWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        return width;
    }

    private int getHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        return height;
    }
}
