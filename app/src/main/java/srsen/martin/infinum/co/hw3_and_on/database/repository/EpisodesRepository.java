package srsen.martin.infinum.co.hw3_and_on.database.repository;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.models.Comment;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;

public interface EpisodesRepository {

    void getEpisodes(String showId, DatabaseCallback<List<Episode>> callback);
    void insertEpisodes(List<Episode> episode, DatabaseCallback<Void> callback);

    void getEpisodeComments(String episodeId, DatabaseCallback<List<Comment>> callback);
    void insertComments(List<Comment> comments, DatabaseCallback<Void> callback);
}
