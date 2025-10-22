import java.util.Map;
import java.util.HashMap;

public interface ThemeManager {

    public static final Map<String, String> LIGHT_THEME = new HashMap<>() {{
        //----Menu Colors----
        put("-menu-bar-color", "#35303F"); // mid purple
        put("-button-color", "#8A7CA2"); // light purple
        put("-text-color", "white");

        put("-background-color", "#4A4357"); // purple
        put("-two-fa-buttons", "#4A4357");
        put("-fx-play-button", "#8A7CA2");

        //----Pressed Versions----
        put("-pressed-button-color", "#2C2835"); // dark purple
        put("-pressed-text-color", "#8A7CA2"); // light purple

    }};

    public static final Map<String, String> DARK_THEME = new HashMap<>() {{
        //----Menu Colors----
        put("-menu-bar-color", "#393939"); // dark gray
        put("-button-color", "#B0B0B0"); // light gray
        put("-text-color", "#242424");

        put("-background-color", "#D5D5D5"); // bg2
        put("-two-fa-buttons", "#393939");
        put("-fx-play-button", "#393939");

        //----Pressed Versions----
        put("-pressed-button-color", "#242424"); // dark purple
        put("-pressed-text-color", "#B0B0B0"); // light purple

    }};
    public void changeTheme();
}
