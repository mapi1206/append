package wifi.svdew.myapplication.ui.home;

public class Team {
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

    public String getPlayerImageUrl() {
        return playerImageUrl;
    }
}
