package wifi.svdew.myapplication.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable {
    public int id;
    public String name;
    public String logoUrl;
    public String playerImageUrl;

    public Team(int id, String name, String logoUrl, String playerImageUrl) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.playerImageUrl = playerImageUrl;
    }

    protected Team(Parcel in) {
        id = in.readInt();
        name = in.readString();
        logoUrl = in.readString();
        playerImageUrl = in.readString();
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
        parcel.writeString(logoUrl);
        parcel.writeString(playerImageUrl);
    }

    public String getPlayerImageUrl() {
        return playerImageUrl;
    }

    public String getName() {
        return name;
    }
}
