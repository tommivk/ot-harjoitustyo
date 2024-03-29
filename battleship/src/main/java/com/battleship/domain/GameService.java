package com.battleship.domain;

import com.battleship.dao.GameDao;

/**
 * Class that is used to interact with the game and the database
 */
public class GameService {
    private GameDao gameDao;
    private Game currentGame;
    private String databaseAdress;

    public GameService(GameDao gameDao, String databaseAdress) {
        this.gameDao = gameDao;
        this.databaseAdress = databaseAdress;
    }

    /**
     * Creates new game and stores it in database
     * 
     * @param playerOne player one in the game
     * @param playerTwo player two in the game
     * 
     * @return returns true if succesfull
     */
    public boolean createGame(User playerOne, User playerTwo) {
        try {
            Game game = gameDao.createGame(this.databaseAdress, playerOne, playerTwo);
            this.currentGame = game;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets players total shot count from the database
     * 
     * @param playerId id of the player that's being queried
     * 
     * @return total count of the shots
     */
    public int getPlayerShotCount(int playerId) {
        try {
            int count = gameDao.getPlayerShotCount(this.databaseAdress, playerId);
            return count;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Gets players total hit count from the database
     * 
     * @param playerId id of the player that's being queried
     * 
     * @return total count of the hits
     */
    public int getPlayerHitCount(int playerId) {
        try {
            int count = gameDao.getPlayerHitCount(this.databaseAdress, playerId);
            return count;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Increases the player one shots in the game by one in the database
     */
    public void addPlayerOneShot() {
        try {
            gameDao.addPlayerOneShot(this.databaseAdress, currentGame.getGameId());

        } catch (Exception e) {

        }
    }

    /**
     * Increases the player two shots in the game by one in the database
     */
    public void addPlayerTwoShot() {
        try {
            gameDao.addPlayerTwoShot(this.databaseAdress, currentGame.getGameId());

        } catch (Exception e) {

        }
    }

    /**
     * Increases player one's hits in the game by one in the database
     */
    public void addPlayerOneHit() {
        try {
            gameDao.addPlayerOneHit(this.databaseAdress, currentGame.getGameId());

        } catch (Exception e) {

        }
    }

    /**
     * Increases player two's hits in the game by one in the database
     */
    public void addPlayerTwoHit() {
        try {
            gameDao.addPlayerTwoHit(this.databaseAdress, currentGame.getGameId());

        } catch (Exception e) {

        }
    }

    /**
     * Sets a winner to the game
     * 
     * @param playerId id of the player who won
     */
    public void setWinner(int playerId) {
        try {
            gameDao.setWinner(this.databaseAdress, currentGame.getGameId(), playerId);
        } catch (Exception e) {
        }
    }

    /**
     * Gets players win count
     * 
     * @param playerId id of the player
     * @return win count
     */
    public int getPlayerWinCount(int playerId) {
        try {
            return gameDao.getPlayerWinCount(this.databaseAdress, playerId);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Gets players game count
     * 
     * @param playerId id of the player
     * @return game count
     */
    public int getPlayerGameCount(int playerId) {
        try {
            return gameDao.getPlayerGameCount(this.databaseAdress, playerId);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Gets current game
     * 
     * @return Game object
     */
    public Game getGame() {
        return this.currentGame;
    }
}
