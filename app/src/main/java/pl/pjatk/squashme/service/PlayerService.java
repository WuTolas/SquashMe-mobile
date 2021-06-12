package pl.pjatk.squashme.service;

public interface PlayerService {
    /**
     * Gets id of the player based on the name. If player is not found, he is saved before
     * returning id.
     *
     * @param name - player name
     * @return id of the player
     */
    long getIdWithSave(String name);
}
