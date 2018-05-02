package ashu.rento.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ashu.rento.R;
import ashu.rento.model.MovieDTO;
import ashu.rento.utils.Constant;

/**
 * Created by apple on 02/05/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private Context context;
    private List<MovieDTO> movies;
    private OnItemClick onItemClick;

    public MovieAdapter(Context context, List<MovieDTO> movies, OnItemClick onItemClick){
        this.context = context;
        this.movies = movies;
        this.onItemClick = onItemClick;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);

        return new MovieViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.movieTitle.setText(movies.get(position).getTitle());
        holder.data.setText(movies.get(position).getReleaseDate());
        holder.movieDescription.setText(movies.get(position).getOverview());
        holder.rating.setText(movies.get(position).getVoteAverage().toString());

        String path = Constant.POSTER_URL + movies.get(position).getPosterPath();
        Glide.with(context).load(path).into(holder.imgMovie);

        holder.moviesLayout.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;
        ImageView imgMovie;

        private OnItemClick onItemClick;


        public MovieViewHolder(View v, OnItemClick onItemClick) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            movieDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);
            imgMovie = (ImageView) v.findViewById(R.id.imgMovie);
            this.onItemClick = onItemClick;
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClickItem(getAdapterPosition());
        }
    }

    public interface OnItemClick{

        void onClickItem(int pos);
    }


}
