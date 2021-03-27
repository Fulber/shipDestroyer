package com.dejava.shipDestroyer.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class GameStartedEvent {

    @Id
    private String gameId;

    private String tournamentId;
    private Integer battlegroundSize;

    @OneToOne(cascade = CascadeType.ALL)
    private BattleshipCell core;

    @OneToOne(cascade = CascadeType.ALL)
    private BattleshipTemplate battleshipTemplate;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Integer getBattlegroundSize() {
        return battlegroundSize;
    }

    public void setBattlegroundSize(Integer battlegroundSize) {
        this.battlegroundSize = battlegroundSize;
    }

    public BattleshipCell getCore() {
        return core;
    }

    public void setCore(BattleshipCell core) {
        this.core = core;
    }

    public BattleshipTemplate getBattleshipTemplate() {
        return battleshipTemplate;
    }

    public void setBattleshipTemplate(BattleshipTemplate battleshipTemplate) {
        this.battleshipTemplate = battleshipTemplate;
    }
}
