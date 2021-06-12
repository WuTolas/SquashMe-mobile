package pl.pjatk.squashme.service;

import javax.inject.Inject;

import pl.pjatk.squashme.dao.PlayerDao;
import pl.pjatk.squashme.model.Player;

/**
 * Implementation class for PlayersService interface.
 */
public class PlayerServiceImpl implements PlayerService {

    private final PlayerDao playerDao;

    @Inject
    public PlayerServiceImpl(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getIdWithSave(String name) {
        long id = playerDao.getId(name);
        if (id == 0) {
            Player player = new Player();
            player.setName(name);
            return playerDao.insert(player);
        } else {
            return id;
        }
    }
}
