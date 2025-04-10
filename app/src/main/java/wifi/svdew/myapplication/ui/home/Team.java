package wifi.svdew.myapplication.ui.home;

// Model class representing a Team with ID, name, logo, and player image
import android.os.Parcel;
import android.os.Parcelable;

// Implements Parcelable to allow Team objects to be passed between Android components
public class Team implements Parcelable {
    // Team ID
    public int id;
    // Team name
    public String name;
    // Drawable resource ID for the team's logo
    public int logoUrl;
    // Drawable resource ID for the player's story image
    public int playerImageUrl;

    // Constructor for creating a Team object
    public Team(int id, String name, int logoUrl, int playerImageUrl) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.playerImageUrl = playerImageUrl;
    }

    // Constructor used for recreating Team object from a Parcel
    protected Team(Parcel in) {
        id = in.readInt();
        name = in.readString();
        logoUrl = in.readInt();
        playerImageUrl = in.readInt();
    }

    // Parcelable creator for generating Team instances from a Parcel
    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    // Describe contents for parcelable (usually returns 0)
    @Override
    public int describeContents() {
        return 0;
    }

    // Write Team object data to the Parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(logoUrl);
        parcel.writeInt(playerImageUrl);
    }

    // Getter for player's image resource ID
    public int getPlayerImageUrl() {
        return playerImageUrl;
    }

    // Getter for team name
    public String getName() {
        return name;
    }

    // Getter for team logo resource ID
    public int getLogo() {
        return logoUrl;  // Now returns the drawable resource ID for the logo
    }
}
