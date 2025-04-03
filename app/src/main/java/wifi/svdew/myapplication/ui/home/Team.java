package wifi.svdew.myapplication.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable {
    public int id;
    public String name;
    public int logoUrl;
    public int playerImageUrl;

    public Team(int id, String name, int logoUrl, int playerImageUrl) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.playerImageUrl = playerImageUrl;
    }

    protected Team(Parcel in) {
        id = in.readInt();
        name = in.readString();
        logoUrl = in.readInt();
        playerImageUrl = in.readInt();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(logoUrl);
        parcel.writeInt(playerImageUrl);
    }

    public int getPlayerImageUrl() {
        return playerImageUrl;
    }

    public String getName() {
        return name;
    }

    public int getLogo() {
        return logoUrl;  // Now returns the drawable resource ID for the logo
    }
}
