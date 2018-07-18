package srsen.martin.infinum.co.hw3_and_on;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Util {

    private static final String SEPARATOR = "ß-//-";
    private static final String NEWLINE_SEPARATOR = "ß-ß--";

    public static boolean askPermission(Activity context, String permission, int messageId, int requestCode){
        if(ActivityCompat.checkSelfPermission(context, permission)
                == PERMISSION_GRANTED)   return true;

        if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)){
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage(messageId);
            alert.setOnDismissListener(dialog -> ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode));

            alert.create().show();
        }else {
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
        }

        return false;
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static void saveShowEpisodes(Context context, String showID){
        Show show = ShowsDB.getShowById(showID);
        List<Episode> showEpisodes = show.getEpisodes();

        String fileName = show.getName() + ".txt";

        try(FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)){
            for(Episode episode: showEpisodes){
                StringBuilder sb = new StringBuilder();
                sb.append(episode.getName()).append(SEPARATOR)
                        .append(episode.getDescription()).append(SEPARATOR)
                        .append(episode.getSeason() + SEPARATOR)
                        .append(episode.getEpisode() + SEPARATOR)
                        .append(episode.getImageUri().toString()).append(NEWLINE_SEPARATOR);

                String episodeString = sb.toString();
                Log.i("saved_episode", episodeString);
                outputStream.write(episodeString.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException exc){
            Toast.makeText(context, R.string.error_save_episodes, Toast.LENGTH_SHORT).show();
        }
    }

    public static void loadShowEpisodes(Context context, String showID){
        Show show = ShowsDB.getShowById(showID);
        List<Episode> showEpisodes = show.getEpisodes();

        String fileName = show.getName() + ".txt";
        File file = new File(context.getFilesDir(), fileName);
        if(!file.exists())  return;

        byte[] bytes = new byte[2048];
        int read = 0;
        StringBuilder content = new StringBuilder();

        try(FileInputStream inputStream = context.openFileInput(fileName)){
            while((read = inputStream.read(bytes)) > 0){
                content.append(new String(bytes, 0, read));
            }
        } catch (IOException exc){
            Toast.makeText(context, R.string.error_load_episodes, Toast.LENGTH_SHORT).show();
        }

        String episodesString = content.toString();
        if(episodesString.isEmpty())    return;

        List<Episode> episodes = parseEpisodes(content.toString());
        show.setEpisodes(episodes);
    }

    private static List<Episode> parseEpisodes(String episodesString){
        String[] episodes = episodesString.split(NEWLINE_SEPARATOR);
        List<Episode> episodesList = new ArrayList<>();

        for(String episode : episodes){
            Episode parsedEpisode = parseEpisode(episode);
            if(episode == null) continue;

            episodesList.add(parsedEpisode);
        }

        return episodesList;
    }

    private static Episode parseEpisode(String episodeString){
        if(episodeString.isEmpty()) return null;

        String[] pts = episodeString.split(SEPARATOR);
        if(pts.length != 5) return null;

        Episode episode = new Episode(pts[0], pts[1], Integer.parseInt(pts[2]), Integer.parseInt(pts[3]), Uri.parse(pts[4]));

        return  episode;
    }
}
