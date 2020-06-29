package com.cultofcheese.uhc.entities.game;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.UHCParticipant;
import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.UHCTeam;
import com.cultofcheese.uhc.entities.exceptions.InvalidConfigurationException;
import com.cultofcheese.uhc.events.GameStartEvent;
import com.cultofcheese.uhc.managers.CacheManager;
import com.cultofcheese.uhc.managers.ScoreboardManager;
import com.cultofcheese.uhc.util.TitleUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This object is a single instance of a game. Only one game can be in progress at any one time. Creating a new object creates a brand new game.
 */
public class Game {

    /**
     * A list of all possible game states. Any time these are not applicable, there should not be a game in the cache anyway.
     */
    public enum GameState {WAITING, FULL, USER_LOCK, LOCKED, PRESTART, STARTING, ACTIVE, ENDED}

    /**
     * A list of all border states during the game.
     */
    public enum BorderState {INACTIVE, MOVING, MOVING_FAST, DEATHMATCH, DISABLED}

    //Needed variables
    private final byte MAX = 100;
    private final Map<Player, UHCPlayer> players;
    private final List<UHCTeam> teams;
    private final Map<UHCParticipant, Location> spawnAssigned;
    private final World world;
    private World nether;
    private boolean generated;
    private int generatedChunks;
    private int chunksToGenerate;
    private GameState state;
    private Deque<Location> spawnPoints;
    private WorldBorder border;
    private final GameConfiguration config;
    private long gameTime;

    //Keeping track of when pvp is enabled.
    private boolean pvpEnabled;
    private BukkitTask pvpTimer;

    //Keeping track of when the border moves etc.
    private BorderState borderState;
    private BukkitTask borderTimer;
    private BukkitTask borderSpeedUpTimer;

    private boolean damageActive;
    private BukkitTask damageTimer;
    private BukkitTask damageRepeatTimer;

    //Timers
    private BukkitTask startTimer;
    private BukkitTask scoreboardUpdateTimer;
    private BukkitTask gameTimer;

    private ArmorStand armorStand;

    /**
     * Creates a brand new game with the specified GameConfiguration.
     * @param configuration The game configuration the game should use.
     * @throws InvalidConfigurationException if the configuration passed to the method is marked as invalid.
     */
    public Game(GameConfiguration configuration) throws InvalidConfigurationException {
        if (!configuration.isParsed()) {
            throw new InvalidConfigurationException("An invalid configuration was used to create a new game.", new Throwable("An un-parsed GameConfiguration was used to create a new game."));
        }
        CacheManager.setGame(this);
        config = configuration;
        generated = false;
        state = GameState.WAITING;
        spawnAssigned = new HashMap<>();
        players = new HashMap<>();
        teams = new ArrayList<>();

        generatedChunks = 0;
        //Deleting the world if it already exists.
        if (new File(Bukkit.getWorldContainer(),"uhc").exists()) {
            try {
                FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer(), "uhc"));
            } catch (IOException e) {
                Bukkit.getLogger().severe("Unable to delete world 'uhc'. Stack Trace:");
                e.printStackTrace();
            }
        }

        world = Bukkit.getServer().createWorld(new WorldCreator("uhc"));
        world.setDifficulty(Difficulty.HARD);
        onJoin(getHost().getPlayer());
        gameTime = 0;
    }

    /**
     * This generates the chunks in the UHC world in preparation for the game. This prevents lag while the game is in progress.<br><br>
     *
     * <strong>WARNING:</strong> This is likely to cause extreme lag while in progress and should only ever be called once. There is a safeguard in place to prevent the accidental re-generation of the chunks.
     * Once the game is created, it automatically generates the chunks.<br><br>
     *
     * This method will split chunk loading in to chunks of 2000 chunks at a time and then it pauses for 5 seconds, allowing time for the server time to unload all of the chunks and allow the server tick to catch up. This prevents the server from crashing from being unresponsive.<br><br>
     *
     * This method will automatically schedule for the chunk to be unloaded once the chunk has been generated.
     */
    public void generateChunks(int preLoaded) {
        if (!generated) {
            int minChunkX, maxChunkX, minChunkZ, maxChunkZ;
            minChunkX = minChunkZ = new Location(world, -config.getBorderSize(), 64, -config.getBorderSize()).getChunk().getX();
            maxChunkX = maxChunkZ = new Location(world, config.getBorderSize(), 64, config.getBorderSize()).getChunk().getX();

            int totalChunks = (((maxChunkX-minChunkX) + 1)*((maxChunkZ-minChunkZ) + 1));
            this.chunksToGenerate = totalChunks;
            int generatedChunks = preLoaded;
            double lastPercentage = (Math.round(((((double) generatedChunks) / ((double) totalChunks)) * 1000d))/10d);

            if (preLoaded == 0) {
                Bukkit.getLogger().info("Generating " + totalChunks + " chunks for world 'uhc'.");
                Bukkit.broadcastMessage(UHC.c("World Generator", "Generating &e" + totalChunks + "&r chunks for world 'uhc'. &4&lWARNING:&r This may take some time and will more than likely cause severe server lag. The server automatically breaks up the generation of chunks to prevent lag, and completes generation in chunks of 2000 chunks."));
            }

            int counter = 0;
            for (int x = minChunkX;x <= maxChunkX;x++) {
                for (int z = minChunkZ;z <= maxChunkZ;z++) {
                    if (preLoaded > counter) {
                        counter++;
                        continue;
                    }
                    if (world.loadChunk(x, z, true)) {
                        generatedChunks++;
                        this.generatedChunks++;
                        double percent = (Math.round(((((double) generatedChunks) / ((double) totalChunks)) * 1000d))/10d);
                        if (percent >= 10 && lastPercentage < 10 || percent >= 20 && lastPercentage < 20 || percent >= 30 && lastPercentage < 30 || percent >= 40 && lastPercentage < 40 || percent >= 50 && lastPercentage < 50 || percent >= 60 && lastPercentage < 60 || percent >= 70 && lastPercentage < 70 || percent >= 80 && lastPercentage < 80 || percent >= 90 && lastPercentage < 90) {
                            lastPercentage = percent;
                            chunkGenerationUpdate(generatedChunks, totalChunks, "uhc");
                        } else if (generatedChunks == totalChunks) {
                            Bukkit.getLogger().info("Chunk Generation for 'uhc' is 100% complete. " + totalChunks + " total chunks were generated, loaded and unloaded.");
                            Bukkit.broadcastMessage(UHC.c("World Generator", "Chunk Generation &e100%&r complete. &e" + totalChunks + "&r total chunks were generated, loaded and unloaded."));
                            border = world.getWorldBorder();
                        }

                        if (generatedChunks == preLoaded + 2000) {
                            final int totalGeneratedChunks = generatedChunks;
                            //Give the server 5 seconds to chill out and catch up. Splits loads into 2000 chunks at a time.
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    generateChunks(totalGeneratedChunks);
                                }
                            }.runTaskLater(UHC.get(), 100);
                            return;
                        }
                    } else {
                        Bukkit.getLogger().severe("Generation/Loading for chunk (" + x + "," + z + ") failed!");
                        Bukkit.broadcastMessage(UHC.c("World Generator", "&cGeneration/Loading for chunk (" + x + "," + z + ") failed!"));
                    }
                    world.unloadChunkRequest(x, z);
                }
                if (generatedChunks == preLoaded + 2000) {
                    return;
                }
            }
            if (config.isNetherEnabled()) {
                WorldCreator netherCreator = new WorldCreator("uhc_nether");
                netherCreator.environment(World.Environment.NETHER);
                netherCreator.seed(world.getSeed());


                nether = Bukkit.getServer().createWorld(netherCreator);
                nether.setDifficulty(Difficulty.HARD);
            } else {
                nether = null;
                generated = true;
            }
        }
    }

    /**
     * This generates the chunks in the UHC nether world in preparation for the game. This prevents lag while the game is in progress.<br><br>
     *
     * <strong>WARNING:</strong> This is likely to cause extreme lag while in progress and should only ever be called once. There is a safeguard in place to prevent the accidental re-generation of the chunks.
     * Once the game is created, it automatically generates the chunks.<br><br>
     *
     * This method will split chunk loading in to chunks of 2000 chunks at a time and then it pauses for 5 seconds, allowing time for the server time to unload all of the chunks and allow the server tick to catch up. This prevents the server from crashing from being unresponsive.<br><br>
     *
     * This method will automatically schedule for the chunk to be unloaded once the chunk has been generated.
     */
    public void generateChunksNether(int preLoaded) {
        if (!generated) {
            if (preLoaded == 0) {
                this.generatedChunks = 0;
            }
            int minChunkX, maxChunkX, minChunkZ, maxChunkZ;
            minChunkX = minChunkZ = new Location(nether, (-config.getBorderSize()) / 8f, 64, (-config.getBorderSize()) / 8f).getChunk().getX();
            maxChunkX = maxChunkZ = new Location(nether, (config.getBorderSize()) / 8f, 64, (config.getBorderSize()) / 8f).getChunk().getX();

            int totalChunks = (((maxChunkX-minChunkX) + 1)*((maxChunkZ-minChunkZ) + 1));
            chunksToGenerate = totalChunks;
            int generatedChunks = preLoaded;
            double lastPercentage = (Math.round(((((double) generatedChunks) / ((double) totalChunks)) * 1000d))/10d);;

            if (preLoaded == 0) {
                Bukkit.getLogger().info("Generating " + totalChunks + " chunks for world 'uhc_nether'.");
                Bukkit.broadcastMessage(UHC.c("World Generator", "Generating &e" + totalChunks + "&r chunks for world 'uhc_nether'. &4&lWARNING:&r This may take some time and will more than likely cause severe server lag. The server automatically breaks up the generation of chunks to prevent lag, and completes generation in chunks of 2000 chunks."));
            }

            int counter = 0;
            for (int x = minChunkX;x <= maxChunkX;x++) {
                for (int z = minChunkZ; z <= maxChunkZ; z++) {
                    if (preLoaded > counter) {
                        counter++;
                        continue;
                    }
                    if (nether.loadChunk(x, z, true)) {
                        generatedChunks++;
                        this.generatedChunks++;
                        double percent = (Math.round(((((double) generatedChunks) / ((double) totalChunks)) * 1000d)) / 10d);
                        if (percent >= 10 && lastPercentage < 10 || percent >= 20 && lastPercentage < 20 || percent >= 30 && lastPercentage < 30 || percent >= 40 && lastPercentage < 40 || percent >= 50 && lastPercentage < 50 || percent >= 60 && lastPercentage < 60 || percent >= 70 && lastPercentage < 70 || percent >= 80 && lastPercentage < 80 || percent >= 90 && lastPercentage < 90) {
                            lastPercentage = percent;
                            chunkGenerationUpdate(generatedChunks, totalChunks, "uhc_nether");
                        } else if (generatedChunks == totalChunks) {
                            Bukkit.getLogger().info("Chunk Generation for 'uhc_nether' 100% complete. " + totalChunks + " total chunks were generated, loaded and unloaded.");
                            Bukkit.broadcastMessage(UHC.c("World Generator", "Chunk Generation &e100%&r complete. &e" + totalChunks + "&r total chunks were generated, loaded and unloaded."));
                        }

                        if (generatedChunks == preLoaded + 2000) {
                            final int totalGeneratedChunks = generatedChunks;
                            //Give the server 5 seconds to chill out and catch up. Splits loads into 2000 chunks at a time. Stops the server from thinking it has stopped responding.
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    generateChunksNether(totalGeneratedChunks);
                                }
                            }.runTaskLater(UHC.get(), 100);
                            return;
                        }
                    } else {
                        Bukkit.getLogger().severe("Generation/Loading for chunk (" + x + "," + z + ") failed!");
                        Bukkit.broadcastMessage(UHC.c("World Generator", "&cGeneration/Loading for chunk (" + x + "," + z + ") failed!"));
                    }
                    nether.unloadChunkRequest(x, z);
                }
                if (generatedChunks == preLoaded + 2000) {
                    return;
                }
            }
            generated = true;
        }
    }

    /**
     * Helper method for sending chunk generation updates to the host and to console.
     *
     * @param generatedChunks the amount of generated chunks so far.
     * @param totalChunks the total amount of chunks the plugin has to load.
     */
    private void chunkGenerationUpdate(int generatedChunks, int totalChunks, String world) {
        Bukkit.getLogger().info("Chunk Generation " + (Math.round(((((double) generatedChunks) / ((double) totalChunks)) * 1000d))/10d) + "% complete. (" + generatedChunks + "/" + totalChunks + ")");
        Bukkit.broadcastMessage(UHC.c("World Generator", "Chunk Generation for '" + world + "' is &e" + (Math.round(((((double) generatedChunks) / ((double) totalChunks)) * 1000d))/10d) + "%&r complete. (" + generatedChunks + "/" + totalChunks + ")"));
    }

    /**
     * Handles what to do when the player joins a game.
     *
     * @param player the player that joined the game.
     */
    public void onJoin(Player player) {
        if (players.size() + 1 >= MAX) {
            this.state = GameState.FULL;
        }
        player.sendMessage(UHC.c("Cult of Cheese", "Welcome to the Cult of Cheese UHC event! We will be beginning shortly, feel free to stick around! The host today is: &e" + config.getHost().getPlayer().getName() + "&r.\n\nThis UHC plugin was created and provided for this event by Block2Block."));
        UHCPlayer uhcPlayer = new UHCPlayer(player);
        players.put(player, uhcPlayer);

        //Set Scoreboard.
        ScoreboardManager.changeLinePlayer(player, 15, "&6&l«PLAYERS»");
        ScoreboardManager.changeLineGlobal(14, players.size() + "/" + MAX);
        ScoreboardManager.changeLinePlayer(player, 13, " ");
        ScoreboardManager.changeLinePlayer(player, 12, "&6&l«HOST»");
        ScoreboardManager.changeLinePlayer(player, 11, config.getHost().getPlayer().getName());
        ScoreboardManager.changeLinePlayer(player, 10, "  ");
        ScoreboardManager.changeLinePlayer(player, 9, "&6&l«MODE»");
        ScoreboardManager.changeLinePlayer(player, 8, ((config.isTeamed())?"Teams of " + config.getTeamSize():"Solo"));
        ScoreboardManager.changeLinePlayer(player, 7, "   ");
        ScoreboardManager.changeLinePlayer(player, 6, "&6&l«MAP SIZE»");
        ScoreboardManager.changeLinePlayer(player, 5, (config.getBorderSize()*2) + "x" + (config.getBorderSize()*2));


        if (config.isTeamed()) {
            for (UHCTeam team : teams) {
                uhcPlayer.registerTeam(team);
            }
            if (!config.isRandomTeams()) {
                if (config.isForceFill()) {
                    //Assign them to an already existing team, if they are all full create a new team.
                    boolean teamFound = false;
                    for (UHCTeam team : teams) {
                        if (team.aliveMembers() < config.getTeamSize()) {
                            teamFound = true;
                            team.addMember(uhcPlayer);
                            uhcPlayer.joinTeam(team);
                            for (UHCPlayer otherPlayer : players.values()) {
                                if (uhcPlayer == otherPlayer) {
                                    continue;
                                }
                                otherPlayer.addToTeam(player, team);
                            }
                            player.sendMessage(UHC.c("Game Manager", "Teams are enabled, and you have been forced to fill a team. You can still change teams, but you cannot create a new one until one is created by the server."));
                            break;
                        }
                    }
                    if (!teamFound) {
                        UHCTeam team = new UHCTeam(CacheManager.newTeam());
                        team.addMember(uhcPlayer);
                        for (UHCPlayer otherPlayer : players.values()){
                            otherPlayer.registerTeam(team);
                        }
                        uhcPlayer.setTeam(team);
                        teams.add(team);
                        player.sendMessage(UHC.c("Game Manager", "Teams are enabled, and you have been forced to fill a team. You can still change teams, but you cannot create a new one until one is created by the server."));
                    }
                } else {
                    player.sendMessage(UHC.c("Game Manager", "Teams are currently enabled. Use &e/team &rto manage, join or create a team."));
                }
            } else {
                //Forcefill will have no effect on random teams, as it will auto assign everyone to teams anyway.
                player.sendMessage(UHC.c("Game Manager", "Random teams are currently enabled. When the game timer starts, you will be assigned randomly to a team."));
            }
        }
    }

    public void playerLeave(Player player) {
        if (CacheManager.getGame().getConfig().isTeamed()) {
            if (CacheManager.getGame().getPlayers().get(player).getTeam() != null) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (!player.equals(player2)) {
                        CacheManager.getGame().getPlayers().get(player2).removeFromTeam(player, CacheManager.getGame().getPlayers().get(player).getTeam());
                    }
                }
            }
        }
        players.remove(player);
        if (state != GameState.ACTIVE) {
            ScoreboardManager.changeLineGlobal(14, players.size() + "/" + MAX);
        }
    }

    /**
     * Deals with what happens when the game start timer starts.
     *
     * @param time the amount of time in seconds the timer should count down from.
     */
    public void startGame(int time) {
        if (generated) {
            state = GameState.PRESTART;
            Bukkit.broadcastMessage(UHC.c("Game Manager", "The game has been started. The server has been locked, and the game is about to start!" + ((config.isTeamed())?" Anyone not on a team will be assigned to one!.":"")));
            if (config.isTeamed()) {
                UHCTeam assignTeam = null;
                List<UHCPlayer> uhcPlayers = new ArrayList<>(players.values());
                Collections.shuffle(uhcPlayers);
                for (UHCPlayer player : uhcPlayers) {
                    if (player.getTeam() == null) {
                        if (config.isForceFill() || config.isRandomTeams()) {
                            boolean wasAssigned = false;
                            for (UHCTeam team : teams) {
                                if (team.aliveMembers() < config.getTeamSize()) {
                                    for (UHCPlayer player2 : players.values()) {
                                        player2.addToTeam(player.getPlayer(), team);
                                    }
                                    team.addMember(player);
                                    player.setTeam(team);
                                    wasAssigned = true;
                                    break;
                                }
                            }

                            if (wasAssigned) {
                                continue;
                            }
                        }

                        if (assignTeam == null || assignTeam.aliveMembers() >= config.getTeamSize()) {
                            assignTeam = new UHCTeam(CacheManager.newTeam());
                            assignTeam.addMember(player);
                            for (UHCPlayer otherPlayer : CacheManager.getGame().getPlayers().values()) {
                                otherPlayer.registerTeam(assignTeam);
                            }
                            CacheManager.getGame().addTeam(assignTeam);
                        } else {
                            for (UHCPlayer player2 : players.values()) {
                                player2.addToTeam(player.getPlayer(), assignTeam);
                            }
                            assignTeam.addMember(player);
                        }
                        player.setTeam(assignTeam);
                        player.getPlayer().sendMessage(UHC.c("Teams", "You were assigned to " + assignTeam.getFormattedName() + "&r."));

                    }
                }
            }
            Bukkit.broadcastMessage(UHC.c("World Generator","Generating spawn points..."));
            spawnPoints = generateSpawnPoints();
            Bukkit.broadcastMessage(UHC.c("World Generator","Successfully generated &e" + spawnPoints.size() + "&r spawn points."));

            startTimer = new BukkitRunnable() {
                int i = time + 1;

                @Override
                public void run() {
                    i--;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        TitleUtil.sendHotBar(p, UHC.c(null, "&6&lThe game will start in " + i + " seconds!"), ChatColor.GOLD, true);
                    }
                    switch (i) {
                        case 60:
                        case 30:
                        case 15:
                        case 10:
                        case 5:
                        case 4:
                        case 3:
                        case 2:
                        case 1:
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                TitleUtil.sendTitle(p, "" + i, "seconds till the game starts!", 20, 80, 20, ChatColor.GOLD, ChatColor.YELLOW, true, false);
                                p.sendMessage(UHC.c("UHC","The game starts in &e" + i + "&r seconds."));
                            }
                            break;
                        case 0:
                            //Start the game.
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    start();
                                }
                            }.runTask(UHC.get());
                    }
                }
            }.runTaskTimerAsynchronously(UHC.get(), 0, 20);
        }
    }

    /**
     * Handles what happens when the game is started. This will start a 20 second period where players cannot move while the server and players catch up from teleporting.
     */
    public void start() {
        startTimer.cancel();
        //Teleporting everyone to the world
        Bukkit.broadcastMessage(UHC.c("Game Manager", "Teleporting to spawn points. This may cause some lag while the chunks are being loaded."));
        for (UHCPlayer player : players.values()) {
            player.onGameStart();
            if (!config.isTeamed()) {
                spawnAssigned.put(player, spawnPoints.pop());
                player.getPlayer().teleport(spawnAssigned.get(player));
            }
        }

        //Teleporing the team to be together if its teamed mode.
        if (config.isTeamed()) {
            for (UHCTeam team : teams) {
                spawnAssigned.put(team, spawnPoints.pop());
                team.teleport(spawnAssigned.get(team));
            }
        }

        border.setCenter(0,0);
        border.setSize(config.getBorderSize()*2);
        border.setDamageAmount(100);
        border.setDamageBuffer(1);
        border.setWarningTime(5);

        world.setGameRuleValue("naturalRegeneration", "false");
        world.setDifficulty(Difficulty.HARD);

        state = GameState.STARTING;
        new BukkitRunnable(){
            int i = 10;
            @Override
            public void run() {
                if (i == 0) {
                    state = GameState.ACTIVE;
                    updateScoreboard();
                    gameTimer = new BukkitRunnable() {
                        @Override
                        public void run() {
                            gameTime++;
                        }
                    }.runTaskTimerAsynchronously(UHC.get(), 20, 20);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 2);
                    }
                    this.cancel();
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        TitleUtil.sendHotBar(player, UHC.c(null, "&6&lPlayers will be released in " + i + " seconds!"), ChatColor.GOLD, true);
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 1);
                    }
                }
                i--;
            }
        }.runTaskTimer(UHC.get(), 20, 20);

        //Game Timers.
        if (config.getPvpTimeMinutes() > 0) {
            pvpTimer = new BukkitRunnable(){
                @Override
                public void run() {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            startPvP();
                        }
                    }.runTask(UHC.get());
                }
            }.runTaskLater(UHC.get(), config.getPvpTimeMinutes() * 1200);
        } else {
            startPvP();
        }

        if (config.getBorderStartTimeMinutes() > 0 && config.getBorderSize() > 32) {
            borderState = BorderState.INACTIVE;
            borderTimer = new BukkitRunnable(){
                @Override
                public void run() {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            startBorder();
                        }
                    }.runTask(UHC.get());
                }
            }.runTaskLater(UHC.get(), config.getBorderStartTimeMinutes() * 1200);
        }

        if (config.getDamageTimeMinutes() > 0 && config.getDamageAmount() > 0) {
            damageTimer = new BukkitRunnable() {
                @Override
                public void run() {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            startDamage();
                        }
                    }.runTask(UHC.get());
                }
            }.runTaskLater(UHC.get(), config.getDamageTimeMinutes() * 1200);
        }

        if (config.getStarterItems() != null) {
            for (UHCPlayer player : players.values()) {
                player.getPlayer().getInventory().addItem(config.getStarterItems().toArray(new ItemStack[0]));
            }
        }

        //Registering crafting recipe

        ItemStack goldenHead = new ItemStack(Material.SKULL_ITEM, 1);
        ItemMeta im = goldenHead.getItemMeta();
        im.setDisplayName(UHC.c(null, "&6&lGolden Head"));
        goldenHead.setItemMeta(im);
        goldenHead.setDurability((short) 3);
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
        skull.setDurability((short) 3);

        ShapedRecipe normalRecipe = new ShapedRecipe(goldenHead);

        normalRecipe.shape("***","*h*","***");

        normalRecipe.setIngredient('*', Material.GOLD_INGOT);
        normalRecipe.setIngredient('h', skull.getData());

        Bukkit.getServer().addRecipe(normalRecipe);

        //Register listeners for game features
        for (GameFeature feature : GameFeature.values()) {
            if (config.getFeatureEnabled(feature)) {
                feature.registerListeners();
            }
        }

        Bukkit.getPluginManager().callEvent(new GameStartEvent(this));
    }

    /**
     * This creates a queue of spawn points for players to spawn in. Will only generate as many as it needs.
     *
     * @return a queue of all of the valid generated spawn points.
     */
    public Deque<Location> generateSpawnPoints() {
        Random random = new Random();
        Deque<Location> locations = new ArrayDeque<>();
        int xMin,xMax,zMin,zMax;
        xMin = zMin = -config.getBorderSize() + ((config.getBorderSize() > 32)?32:0);
        xMax = zMax = config.getBorderSize() - ((config.getBorderSize() > 32)?32:0);

        for(int i = 0; i < ((config.isTeamed())?teams.size():players.size()); ++i) {
            double x = xMin >= xMax ? xMin : random.nextDouble() * (xMax - xMin) + xMin;
            double z = zMin >= zMax ? zMin : random.nextDouble() * (zMax - zMin) + zMin;

            //If the generated location is in the exclusion zone, ignore it and generate a new one.
            if (config.getBorderSize() > 32) {
                if (32 >= x|| 32 >= z) {
                    i--;
                    continue;
                }
            }

            Location location = new Location(world, Math.round(x) + 0.5, world.getHighestBlockYAt((int) Math.round(Math.round(x) + 0.5),(int) Math.round(Math.round(z) + 0.5)) + 0.5, Math.round(z) + 0.5);
            if (location.getBlock().getBiome() == Biome.RIVER || location.getBlock().getBiome() == Biome.OCEAN || location.getBlock().getBiome() == Biome.DEEP_OCEAN) {
                i--;
                continue;
            }
            locations.addLast(location);
        }

        return locations;
    }

    public void lock() {
        state = GameState.USER_LOCK;
        Bukkit.broadcastMessage(UHC.c("Game Manager","The server has been locked by the host!"));
        Bukkit.broadcastMessage(UHC.c("World Generator","Generating spawn points..."));
        spawnPoints = generateSpawnPoints();
        Bukkit.broadcastMessage(UHC.c("World Generator","Successfully generated &e" + spawnPoints.size() + "&r spawn points."));
    }

    public void unlock() {
        Bukkit.broadcastMessage(UHC.c("Game Manager","The server has been unlocked by the host!"));
        state = GameState.WAITING;
    }

    public void startPvP() {
        Bukkit.broadcastMessage(UHC.c("Game Manager", "PvP is now enabled! If you disconnect, you can no longer respawn back into the game and your inventory will be dropped onto the floor."));
        for (Player players : Bukkit.getOnlinePlayers()) {
            TitleUtil.sendTitle(players, "PvP is now enabled!", "You can no longer disconnect and reconnect back into the game.", 20, 100, 20, ChatColor.GOLD, ChatColor.WHITE, true, false);
            players.playSound(players.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 1);
        }

        pvpEnabled = true;
        if (pvpTimer != null) {
            pvpTimer.cancel();
        }
    }

    public void startBorder() {
        if (borderTimer != null) {
            borderTimer.cancel();
        }
        if (config.getBorderSize() > 32 && border.getSize() > 32) {
            border.setSize(border.getSize());
            if (config.getBorderSpeedUpDistance() > 32 && config.getBorderSpeedUpDistance() < config.getBorderSize()) {
                border.setSize(config.getBorderSpeedUpDistance() * 2, Math.round(((border.getSize() - (config.getBorderSpeedUpDistance() * 2)) / config.getBorderSlowBlocksPerSecond())/2));
                borderSpeedUpTimer = new BukkitRunnable() {
                    @Override
                    public void run() {
                        startBorderSpeedUp();
                    }
                }.runTaskLater(UHC.get(), Math.round(((border.getSize() - (config.getBorderSpeedUpDistance() * 2)) / config.getBorderSlowBlocksPerSecond())/2)*20);
            } else {
                border.setSize(64, Math.round(((border.getSize() - 64)/config.getBorderSlowBlocksPerSecond())/2));
            }
            borderState = BorderState.MOVING;
            Bukkit.broadcastMessage(UHC.c("Game Manager","The border has started moving in!"));
            for (Player players : Bukkit.getOnlinePlayers()) {
                TitleUtil.sendTitle(players, "The Border is Moving!", "The border has starting moving inward!", 20, 100, 20, ChatColor.GOLD, ChatColor.WHITE, true, false);
            }
        }
    }

    public void startBorderSpeedUp() {
        if (borderSpeedUpTimer != null) {
            borderSpeedUpTimer.cancel();
        }
        if (config.getBorderSpeedUpDistance() > 32 && border.getSize() > 32) {
            borderState = BorderState.MOVING_FAST;
            border.setSize(border.getSize());
            border.setSize(64, Math.round(((border.getSize() - 64)/config.getBorderFastBlocksPerSecond())/2));
            Bukkit.broadcastMessage(UHC.c("Game Manager","The border has sped up!"));
            for (Player players : Bukkit.getOnlinePlayers()) {
                TitleUtil.sendTitle(players, "The Border Sped Up!", "The border has sped up and is moving faster!", 20, 100, 20, ChatColor.GOLD, ChatColor.WHITE, true, false);
            }
        }
    }

    public void startDamage() {
        if (damageTimer != null) {
            damageTimer.cancel();
        }
        if (damageRepeatTimer != null) {
            damageRepeatTimer.cancel();
        }
        if (config.getDamageTimeMinutes() > 0) {
            Bukkit.broadcastMessage(UHC.c("Game Manager","Damage is now active! You will take regular damage until the game ends!"));
            for (Player players : Bukkit.getOnlinePlayers()) {
                TitleUtil.sendTitle(players, "Damage is now active!", "You will now start to take damage till the game ends!", 20, 100, 20, ChatColor.GOLD, ChatColor.WHITE, true, false);
                players.playSound(players.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 1);
            }
            damageActive = true;
            armorStand = world.spawn(new Location(world, 0, 1000, 0), ArmorStand.class);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            damageRepeatTimer = new BukkitRunnable() {
                @Override
                public void run() {
                    for (UHCPlayer player : players.values()) {
                        if (!player.isDead()) {
                            player.getPlayer().damage(config.getDamageAmount(), armorStand);
                        }
                    }
                }
            }.runTaskTimer(UHC.get(), 20, 20);
        }
    }

    public void updateScoreboard() {
        scoreboardUpdateTimer = new BukkitRunnable() {
            @Override
            public void run() {
                int alive = 0;
                for (UHCPlayer player : players.values()) {
                    if (!player.isDead()) {
                        alive++;
                    }
                }

                ScoreboardManager.changeLineGlobal(15, "&6&l«PLAYERS ALIVE»");
                ScoreboardManager.changeLineGlobal(14, alive + "");

                ScoreboardManager.changeLineGlobal(12, "&6&l«BORDER»");
                ScoreboardManager.changeLineGlobal(11, ((borderState == BorderState.INACTIVE)?"Inactive":"+-" + Math.round(border.getSize()/2)));

                if (!pvpEnabled) {
                    ScoreboardManager.changeLineGlobal(9, "&6&l«PVP»");
                    ScoreboardManager.changeLineGlobal(8, ("" + (((config.getPvpTimeMinutes()*60) - gameTime < 60)?(config.getPvpTimeMinutes()*60) - gameTime + " seconds ":config.getPvpTimeMinutes() - (gameTime/60) + " minutes ")));
                } else {
                    ScoreboardManager.changeLineGlobal(9, "&6&l«DAMAGE»");
                    ScoreboardManager.changeLineGlobal(8, ((damageActive)?"Active":"" + (((config.getDamageTimeMinutes()*60) - gameTime < 60)?(config.getDamageTimeMinutes()*60) - gameTime + " seconds ":config.getDamageTimeMinutes() - (gameTime/60) + " minutes ")));
                }

                ScoreboardManager.changeLineGlobal(6, "&6&l«GAME TIME»");
                ScoreboardManager.changeLineGlobal(5, ((gameTime < 60)?(gameTime) + " seconds":(gameTime/60) + " minutes"));
                if (config.isTeamed()) {
                    int i = 0;
                    for (UHCTeam team : teams){
                        if (team.aliveMembers() > 0) {
                            i++;
                        }
                    }
                    ScoreboardManager.changeLineGlobal(4, "    ");
                    ScoreboardManager.changeLineGlobal(3, "&6&l«TEAMS ALIVE»");
                    ScoreboardManager.changeLineGlobal(2, "" + i + " ");
                }
            }
        }.runTaskTimer(UHC.get(), 0, 20);
    }

    public void end(UHCParticipant winner) {
        state = GameState.ENDED;
        border.setSize(border.getSize());
        if (winner instanceof UHCTeam) {
            UHCTeam team = (UHCTeam) winner;
            for (Player player : Bukkit.getOnlinePlayers()) {
                TitleUtil.sendTitle(player, "Team " + (team.getId() + 1), "won the game!", 20, 100, 20, ChatColor.GOLD, ChatColor.WHITE, true, false);
                player.sendMessage(UHC.c("Game Manager", team.getFormattedName() + "&rwon the game!"));
            }
        } else {
            UHCPlayer player = (UHCPlayer) winner;
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                TitleUtil.sendTitle(player2,  player.getPlayer().getName(), "won the game!", 20, 100, 20, ChatColor.GOLD, ChatColor.WHITE, true, false);
                player2.sendMessage(UHC.c("Game Manager", "&e" + player.getPlayer().getName() + " &rwon the game!"));
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            UHCPlayer uhcPlayer = players.get(player);
            if (uhcPlayer != null) {
                uhcPlayer.getPlayerStats().onEnd();
            }
        }

        if (gameTimer != null) {
            gameTimer.cancel();
        }
        if (scoreboardUpdateTimer != null) {
            scoreboardUpdateTimer.cancel();
        }

        if (borderSpeedUpTimer != null) {
            borderSpeedUpTimer.cancel();
        }

        if (borderTimer != null) {
            borderTimer.cancel();
        }

        if (damageRepeatTimer != null) {
            damageRepeatTimer.cancel();
        }

        if (damageTimer != null) {
            damageTimer.cancel();
        }

        if (pvpTimer != null) {
            pvpTimer.cancel();
        }

        if (startTimer != null) {
            startTimer.cancel();
        }

        new BukkitRunnable(){
            int runs = 0;
            final int[][] locations = new int[][]{{28, 28},{0, 28},{-28, 28},{-28, 0}, {-28, -28},{0,-28},{28,-28},{28,0},{0, 0}};
            @Override
            public void run() {
                for (int[] location : locations) {
                    Location loc = new Location(Bukkit.getWorld("uhc"), location[0], 100, location[1]);
                    Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                    FireworkMeta fwm = firework.getFireworkMeta();

                    fwm.setPower(0);
                    fwm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.LIME).build());

                    firework.setFireworkMeta(fwm);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            firework.detonate();
                        }
                    }.runTaskLater(UHC.get(), 1);

                }
                runs++;
                if (runs >= 15) {
                    this.cancel();
                }
            }
        }.runTaskTimer(UHC.get(), 0, 40);
    }

    public boolean isGenerated() {
        return generated;
    }

    public byte getMAX(){
        return MAX;
    }

    public boolean isLocked() {
        return state == GameState.LOCKED || state == GameState.USER_LOCK || state == GameState.PRESTART || state == GameState.STARTING;
    }

    public List<UHCTeam> getTeams() {
        return new ArrayList<>(teams);
    }

    public GameState getState() {
        return state;
    }

    public HashMap<Player, UHCPlayer> getPlayers() {
        return new HashMap<>(players);
    }

    public UHCPlayer getHost() {
        return config.getHost();
    }

    public void addTeam(UHCTeam team) {
        teams.add(team);
    }

    public void removeTeam(UHCTeam team) {
        teams.remove(team);
    }

    public GameConfiguration getConfig() {
        return config;
    }

    public boolean isDamageActive() {
        return damageActive;
    }

    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public BorderState getBorderState() {
        return borderState;
    }

    public double getPercentage() {
        return (Math.round(((((double) generatedChunks) / ((double) chunksToGenerate)) * 1000d))/10d);
    }


    public List<UHCParticipant> participantsAlive() {
        if (config.isTeamed()) {
            List<UHCParticipant> alive = new ArrayList<>();
            for (UHCTeam team : teams) {
                if (team.aliveMembers() > 0) {
                    alive.add(team);
                }
            }
            return alive;
        } else {
            List<UHCParticipant> alive = new ArrayList<>();
            for (UHCPlayer player : players.values()) {
                if (!player.isDead()) {
                    alive.add(player);
                }
            }
            return alive;
        }
    }

    public void onRejoin(UHCPlayer player) {
        for (Map.Entry<Player, UHCPlayer> entry : players.entrySet()) {
            if (entry.getValue().equals(player)) {
                players.remove(entry.getKey());
                players.put(player.getPlayer(), player);
                break;
            }
        }
    }

}
