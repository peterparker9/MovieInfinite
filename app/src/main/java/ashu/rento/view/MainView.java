package ashu.rento.view;

import java.util.List;

import ashu.rento.model.MovieDTO;

/**
 * Created by apple on 02/05/18.
 */

public interface MainView {

    void populateAdapter(List<MovieDTO> movieDTO, int from);

}
