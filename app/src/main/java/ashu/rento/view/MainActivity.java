package ashu.rento.view;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ashu.rento.R;
import ashu.rento.adapter.MovieAdapter;
import ashu.rento.model.MovieDTO;
import ashu.rento.presenter.MainPresenter;
import ashu.rento.utils.RealmApp;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements MainView, MovieAdapter.OnItemClick{

    private MainPresenter mainPresenter;

    private RecyclerView recyclerMovieList;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 0;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.getInstance(MainActivity.this);

        recyclerMovieList = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerMovieList.setLayoutManager(new LinearLayoutManager(MainActivity.this, OrientationHelper.VERTICAL, false));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mainPresenter = new MainPresenter(MainActivity.this, this);

        loadMore(20);
    }

    @Override
    protected void onDestroy() {
        mainPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void populateAdapter(List<MovieDTO> movieDTO, int flag) {
        MovieAdapter adapter = new MovieAdapter(MainActivity.this, movieDTO, this);

        if(flag == 0){
            recyclerMovieList.setAdapter(adapter);
        }
        else
            adapter.notifyItemRangeInserted(adapter.getItemCount(), movieDTO.size() - 1);

        if(progressBar.isShown())
            hideProgressView();
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerMovieList.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerMovieList.addItemDecoration(horizontalDecoration);


//

        recyclerMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    LinearLayoutManager mLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (pastVisiblesItems + visibleThreshold)) {
                        showProgressView();
                        loadMore(totalItemCount);
                        loading = true;
                    }


                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void loadMore(int n){
        mainPresenter.getListOfMovies(n);
    }

    void showProgressView() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressView() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickItem(int pos) {
     mainPresenter.displayDialog(pos);
    }


}
