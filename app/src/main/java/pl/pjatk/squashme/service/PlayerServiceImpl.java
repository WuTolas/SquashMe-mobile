package pl.pjatk.squashme.service;

import javax.inject.Inject;

import pl.pjatk.squashme.dao.PlayerDao;
import pl.pjatk.squashme.model.Player;

public class PlayerServiceImpl implements PlayerService {

    private final PlayerDao playerDao;

    @Inject
    public PlayerServiceImpl(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    public long getIdOrSave(String name) {
        Player player = playerDao.getPlayer(name).orElseGet(Player::new);
        if (player.getId() == 0) {
            player.setName(name);
            return playerDao.insert(player);
        } else {
            return player.getId();
        }
    }
}
