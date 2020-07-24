package SharedPreferences;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferences {

    public static final String PREFS_NAME = "favorite_images";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreferences() {
        super();
    }

    public void saveFavorites(List<String> favorites, Context context) {
        android.content.SharedPreferences settings;
        android.content.SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.apply();
    }

    public List<String> getFavorites(Context context) {
        android.content.SharedPreferences settings;
        List<String> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            List<String> favoriteItems = gson.fromJson(jsonFavorites,
                    List.class);

            favorites = favoriteItems;
            favorites = new ArrayList<String>(favorites);
        } else{
            return null;
        }

        return (List<String>) favorites;
    }

    public void addFavorite(String string, Context context) {
        List<String> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<String>();
        favorites.add(string);
        saveFavorites(favorites, context);
    }

    public void removeFavorite(String string, Context context) {
        List<String> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(string);
            saveFavorites(favorites, context);
        }

        favorites.remove(string);
    }

    public boolean isFavorite(String string,Context context){
        return getFavorites(context).contains(string);
    }
}
