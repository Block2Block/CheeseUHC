package com.cultofcheese.uhc.entities.game;

import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.exceptions.ConfigurationMismatchException;
import com.cultofcheese.uhc.entities.exceptions.ParseException;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfiguration {

    //Team Settings
    private boolean teamed;
    private byte teamSize;
    private boolean forceFill;
    private boolean randomTeams;

    //Border
    private int borderSize;
    private int borderStartTimeMinutes;
    private int borderSpeedUpDistance;
    private double borderSlowBlocksPerSecond;
    private double borderFastBlocksPerSecond;

    //Damage Settings
    private int pvpTimeMinutes;
    private int damageTimeMinutes;
    private double damageAmount;

    //Misc settings
    private final UHCPlayer host;
    private boolean parsed;
    private List<ItemStack> starterItems;
    private Map<GameFeature, Boolean> gameFeatures;

    //Nether Settings
    private boolean netherEnabled;
    private int netherDamageTimeMinutes;
    private double netherDamageAmount;

    /**
     * Start of the configuration process for a game.
     *
     * @param host the host of the game.
     */
    public GameConfiguration(Player host) {
        parsed = false;
        this.host = new UHCPlayer(host);
        teamed = false;
        borderSize = 1000;
        borderStartTimeMinutes = 20;
        borderSpeedUpDistance = 500;
        borderFastBlocksPerSecond = 1;
        borderSlowBlocksPerSecond = 0.25;
        damageTimeMinutes = 75;
        damageAmount = 0.5;
        pvpTimeMinutes = 10;
        teamSize = 0;
        forceFill = false;
        randomTeams = false;
        starterItems = new ArrayList<>();
        gameFeatures = new HashMap<>();
        for (GameFeature gf : GameFeature.values()) {
            gameFeatures.put(gf, false);
        }
        netherEnabled = true;
        netherDamageTimeMinutes = 0;
        netherDamageAmount = 0.5;
    }

    /**
     * This sets whether the game should be a teamed game or not. Calling this method will also reset the team size.
     *
     * The default values for team sizes are 0 when disabled and 2 when enabled.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param teamed whether the game should be teamed or not.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     */
    public void setTeamed(boolean teamed) throws UnsupportedOperationException {
        if (CacheManager.getGame() == null) {
            this.teamed = teamed;
            if (teamed) {
                teamSize = 2;
            } else {
                teamSize = 0;
            }
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * This sets the size of the world border at the start of the game.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param borderSize the size of the WorldBorder at the start of the game.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if the border size is less than 32.
     */
    public void setBorderSize(int borderSize) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (borderSize < 32) {
                throw new IllegalArgumentException("You cannot have a border size less than 32.");
            }
            this.borderSize = borderSize;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the max amount of players that are allowed on any one team.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param teamSize the max amount of players per team.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalStateException if called when team is set to false (default).
     * @throws IllegalArgumentException if you attempt to set the team size to be less than 2.
     */
    public void setTeamSize(byte teamSize) throws UnsupportedOperationException, IllegalStateException, IllegalArgumentException {
        if (!this.teamed) {
            throw new IllegalStateException("You cannot set a team size when the game is not set to be teamed.", new Throwable("Team mode is currently disabled."));
        }
        if (CacheManager.getGame() == null) {
            if (teamSize < 2) {
                throw new IllegalArgumentException("You cannot set the team size to lower than 2.", new Throwable("Value too low. Expected: >1, actual:" + teamSize));
            }
            this.teamSize = teamSize;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * This sets whether the game should for fill all teams. This will enforce that a team must comprise of the max team size unless there is not enough players.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param forceFill whether the game should be teamed or not.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     */
    public void setForceFill(boolean forceFill) throws UnsupportedOperationException {
        if (CacheManager.getGame() == null) {
            this.forceFill = forceFill;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * This sets whether the game should randomise teams. This means players cannot choose their teammates.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param randomTeams whether the game should be teamed or not.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     */
    public void setRandomTeams(boolean randomTeams) throws UnsupportedOperationException {
        if (CacheManager.getGame() == null) {
            this.randomTeams = randomTeams;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * This sets the items that the player will receive upon the game starting.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param items the items the player should receive when the game starts.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws NullPointerException if <code>items</code> is null.
     */
    public void setStarterItems(List<ItemStack> items) throws NullPointerException, UnsupportedOperationException {
        if (CacheManager.getGame() == null) {
            if (items == null) {
                throw new NullPointerException("items cannot be null.");
            }
            this.starterItems = items;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * This sets whether players should receive the ingot of whatever ore they've mined.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param feature the feature to change.
     * @param enabled if the feature should be set to enabled or not.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws NullPointerException if <code>feature</code> is null.
     */
    public void setGameFeature(GameFeature feature, boolean enabled) throws UnsupportedOperationException, NullPointerException {
        if (CacheManager.getGame() == null) {
            if (feature == null) {
                throw new NullPointerException("features cannot be null");
            }

            gameFeatures.replace(feature, enabled);
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the time in minutes into the match the border will start moving in
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  borderStartTimeMinutes the time in minutes into the match the border will start moving.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if you attempt to set the time to less than 0.
     */
    public void setBorderStartTimeMinutes(int borderStartTimeMinutes) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (borderStartTimeMinutes < 0) {
                throw new IllegalArgumentException("You cannot set the border time to lower than 0.", new Throwable("Value too low. Expected: >=0, actual:" + borderStartTimeMinutes));
            }
            this.borderStartTimeMinutes = borderStartTimeMinutes;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the distance away from the centre the border will speed up.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  borderSpeedUpDistance the distance away from centre the border should speed up at. Set to 32 to disable.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if you attempt to set the distance to less than 32.
     */
    public void setBorderSpeedUpDistance(int borderSpeedUpDistance) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (borderSpeedUpDistance < 32) {
                throw new IllegalArgumentException("You cannot set the border speed up distance to lower than 32.", new Throwable("Value too low. Expected: >=32, actual:" + borderStartTimeMinutes));
            }
            this.borderSpeedUpDistance = borderSpeedUpDistance;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the slow blocks per second the border will move when it initially starts moving.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  borderSlowBlocksPerSecond the block per second the border should move when it starts moving.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if you attempt to set the distance to less than 32.
     */
    public void setBorderSlowBlocksPerSecond(double borderSlowBlocksPerSecond) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (borderSlowBlocksPerSecond < 0) {
                throw new IllegalArgumentException("You cannot set the blocks/second to lower than 0.", new Throwable("Value too low. Expected: >=0, actual:" + borderStartTimeMinutes));
            }
            this.borderSlowBlocksPerSecond = borderSlowBlocksPerSecond;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the fast blocks per second the border will move when it speeds up.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  borderFastBlocksPerSecond the block per second the border should move when it speeds up.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if you attempt to set the distance to less than 32.
     */
    public void setBorderFastBlocksPerSecond(double borderFastBlocksPerSecond) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (borderFastBlocksPerSecond < 0) {
                throw new IllegalArgumentException("You cannot set the blocks/second to lower than 0.", new Throwable("Value too low. Expected: >=0, actual:" + borderStartTimeMinutes));
            }
            this.borderFastBlocksPerSecond = borderFastBlocksPerSecond;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets whether the nether should be accessible.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  netherEnabled Whether the nether should be enabled
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     */
    public void setNetherEnabled(boolean netherEnabled) throws UnsupportedOperationException {
        if (CacheManager.getGame() == null) {
            this.netherEnabled = netherEnabled;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the amount of damage applied per second to a player.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  netherDamageAmount the amount of damage to apply.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if you attempt to set the amount to less than 0.
     */
    public void setNetherDamageAmount(double netherDamageAmount) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (netherDamageAmount < 0) {
                throw new IllegalArgumentException("You cannot set the damage to lower than 0.", new Throwable("Value too low. Expected: >=0, actual:" + netherDamageAmount));
            }
            this.netherDamageAmount = netherDamageAmount;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the time in minutes into the match damage will start being applied to players.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  damageTimeMinutes the time in minutes into the match damage will start being applied to players.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if you attempt to set the time to less than 0.
     */
    public void setNetherDamageTimeMinutes(int damageTimeMinutes) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (damageTimeMinutes < 0) {
                throw new IllegalArgumentException("You cannot set the damage time to lower than 0.", new Throwable("Value too low. Expected: >=0, actual:" + damageTimeMinutes));
            }
            this.netherDamageTimeMinutes = damageTimeMinutes;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the time in minutes into the match pvp will be enabled. Set to 0 to disable.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  pvpTimeMinutes the time in minutes into the match pvp will be enabled. Set to 0 to disable.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if you attempt to set the time to less than 0.
     */
    public void setPvpTimeMinutes(int pvpTimeMinutes) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (pvpTimeMinutes < 0) {
                throw new IllegalArgumentException("You cannot set the pvp time to lower than 0.", new Throwable("Value too low. Expected: >=0, actual:" + pvpTimeMinutes));
            }
            this.pvpTimeMinutes = pvpTimeMinutes;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the time in minutes into the match damage will start being applied to players.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  damageTimeMinutes the time in minutes into the match damage will start being applied to players. Set to 0 to disable.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if you attempt to set the time to less than 0.
     */
    public void setDamageTimeMinutes(int damageTimeMinutes) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (damageTimeMinutes < 0) {
                throw new IllegalArgumentException("You cannot set the damage time to lower than 0.", new Throwable("Value too low. Expected: >=0, actual:" + damageTimeMinutes));
            }
            this.damageTimeMinutes = damageTimeMinutes;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }

    /**
     * Sets the amount of damage to apply to players when damage starts.
     *
     * This method should not be called once the Game Configuration has parsed.
     *
     * @param  damageAmount the time in minutes into the match damage will start being applied to players.
     * @throws UnsupportedOperationException if the configuration has already been parsed.
     * @throws IllegalArgumentException if you attempt to set the time to less than 0.
     */
    public void setDamageAmount(double damageAmount) throws UnsupportedOperationException, IllegalArgumentException {
        if (CacheManager.getGame() == null) {
            if (damageAmount < 0) {
                throw new IllegalArgumentException("You cannot set the damage to lower than 0.", new Throwable("Value too low. Expected: >=0, actual:" + damageAmount));
            }
            this.damageAmount = damageAmount;
        } else {
            throw new UnsupportedOperationException("You cannot modify the game configuration after it has been parsed.");
        }
    }


    public boolean isTeamed() {
        return teamed;
    }

    public byte getTeamSize() {
        return teamSize;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public UHCPlayer getHost() {
        return host;
    }

    public boolean isParsed() {
        return parsed;
    }

    public boolean isForceFill() {
        return forceFill;
    }

    public boolean isRandomTeams() {
        return randomTeams;
    }

    public List<ItemStack> getStarterItems() {
        return new ArrayList<>(starterItems);
    }

    public int getBorderStartTimeMinutes() {
        return borderStartTimeMinutes;
    }

    public double getBorderFastBlocksPerSecond() {
        return borderFastBlocksPerSecond;
    }

    public double getBorderSlowBlocksPerSecond() {
        return borderSlowBlocksPerSecond;
    }

    public int getBorderSpeedUpDistance() {
        return borderSpeedUpDistance;
    }

    public int getPvpTimeMinutes() {
        return pvpTimeMinutes;
    }

    public int getDamageTimeMinutes() {
        return damageTimeMinutes;
    }

    public double getDamageAmount() {
        return damageAmount;
    }

    public boolean isNetherEnabled() {
        return netherEnabled;
    }

    public int getNetherDamageTimeMinutes() {
        return netherDamageTimeMinutes;
    }

    public double getNetherDamageAmount() {
        return netherDamageAmount;
    }

    /**
     * Checks whether a specific feature is enabled.
     *
     * @param feature the feature to check
     * @return whether the feature is enabled.
     * @throws NullPointerException if feature is null.
     */
    public boolean getFeatureEnabled(GameFeature feature) throws NullPointerException {
        if (feature == null) {
            throw new NullPointerException("feature cannot be null");
        }

        return gameFeatures.get(feature);
    }

    /**
     * Parses all settings to make sure that they are all valid.
     *
     * @param createGame whether to automatically create a game with this configuration.
     * @throws ParseException if any settings are invalid.
     * @throws ConfigurationMismatchException if any of the set configuration options do not match expected values e.g. team size is 0 when teams are enabled.
     */
    public void parse(boolean createGame) throws ParseException, ConfigurationMismatchException {
        if (borderSize < 32) {
            throw new ParseException("Invalid configuration value found.", new Throwable("border size was less than 32."));
        }

        if (borderStartTimeMinutes < 0) {
            throw new ParseException("Invalid configuration value found.", new Throwable("Border start time was less than 0."));
        }

        if (borderSlowBlocksPerSecond < 0) {
            throw new ParseException("Invalid configuration value found.", new Throwable("border slow blocks per second was less than 0."));
        }

        if (borderFastBlocksPerSecond < 0) {
            throw new ParseException("Invalid configuration value found.", new Throwable("border fast blocks per second was less than 0."));
        }

        if (borderSpeedUpDistance < 32) {
            throw new ParseException("Invalid configuration value found.", new Throwable("border speed up distance was less than 32."));
        }

        if (pvpTimeMinutes < 0) {
            throw new ParseException("Invalid configuration value found.", new Throwable("pvp timer was less than 0."));
        }

        if (damageTimeMinutes < 0) {
            throw new ParseException("Invalid configuration value found.", new Throwable("damage timer was less than 0."));
        }

        if (damageAmount < 0) {
            throw new ParseException("Invalid configuration value found.", new Throwable("damage amount was less than 0."));
        }

        if (teamed) {
            if (teamSize < 2) {
                throw new ConfigurationMismatchException("Configuration settings do not match", new Throwable("the team size was less than 2 even though teams are enabled."));
            }
        } else {
            if (teamSize != 0) {
                throw new ConfigurationMismatchException("Configuration settings do not match", new Throwable("the team size was not 0 even though teams are disabled."));
            }
        }

        if (netherDamageTimeMinutes < 0) {
            throw new ParseException("Invalid configuration value found.", new Throwable("nether damage time was less than 0."));
        }

        if (netherDamageAmount < 0) {
            throw new ParseException("Invalid configuration value found.", new Throwable("nether damage amount was less than 0."));
        }

        this.parsed = true;

        host.getPlayer().closeInventory();
        if (createGame) {
            new Game(this);
        }
    }
}