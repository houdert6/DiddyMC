package me.houdert6.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.houdert6.common.util.MessageSender;
import me.houdert6.discordsrv.DiscordSRVInterface;
import me.houdert6.discordsrv.MaybeHasSRV;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.logging.Level;

public class DiddyMCCommon {
    public String diddyApi;

    private JavaPlugin plugin;
    private HttpClient apiClient = HttpClient.newHttpClient();
    private MessageSender sender;

    // This is a TextComponent on Paper and a String on Spigot
    private Object prefix;
    private String[] pickupLines;

    // DiscordSRV interface
    private DiscordSRVInterface srv;

    public DiddyMCCommon(JavaPlugin plugin, MessageSender sender) {
        this.plugin = plugin;
        this.sender = sender;
    }

    @SuppressWarnings("deprecation") // Spigot components are only used on Spigot
    public void onEnable() {
        // Config
        this.plugin.saveDefaultConfig();
        this.plugin.saveResource("pickuplines.yml", false);

        // Prefix (Set up MessageSender)
        this.sender.loadPrefix(this.plugin);

        // Load the diddy bot api
        this.diddyApi = this.plugin.getConfig().getString("diddyapi", "http://35.208.224.85");

        // Pickuplines

        boolean hasPickupLines = true;
        // Attempt 1: API
        // Do not attempt the API if always using offline pickup lines
        if (this.plugin.getConfig().getBoolean("always-use-offline-pickuplines", false)) {
            hasPickupLines = false;
        } else {
            try {
                HttpResponse<String> pickuplinesRes = apiClient.send(HttpRequest.newBuilder().uri(URI.create(diddyApi + "/pickuplines")).build(), HttpResponse.BodyHandlers.ofString());
                if (pickuplinesRes.statusCode() >= 400) {
                    this.plugin.getLogger().log(Level.WARNING, "Failed to get pickup lines through API: " + pickuplinesRes.body());
                    hasPickupLines = false; // Status code for an error
                }
                String pickupLines = pickuplinesRes.body();
                JsonElement jsonPickupLines = JsonParser.parseString(pickupLines);
                if (jsonPickupLines.isJsonArray()) {
                    this.pickupLines = jsonPickupLines.getAsJsonArray().asList().stream().filter(e -> e.isJsonPrimitive() && e.getAsJsonPrimitive().isString()).map(JsonElement::getAsString).toArray(String[]::new);
                    this.plugin.getLogger().log(Level.INFO, "Loaded pickup lines! [API]");
                } else {
                    this.plugin.getLogger().log(Level.WARNING, "Failed to get pickup lines through API: Response is not a JSON array");
                    hasPickupLines = false;
                }
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.WARNING, "Failed to get pickup lines through API: ", e);
                hasPickupLines = false; // Status code for an error
            }
        }
        if (!hasPickupLines) {
            hasPickupLines = true;
            // Attempt 2: Diddy Bot github
            // Do not attempt the github if always using offline pickup lines
            if (this.plugin.getConfig().getBoolean("always-use-offline-pickuplines", false)) {
                hasPickupLines = false;
            } else {
                try {
                    HttpResponse<String> pickuplinesRes = apiClient.send(HttpRequest.newBuilder().uri(URI.create("https://raw.githubusercontent.com/PrestonCurtis1/Diddy-Bot/refs/heads/master/pickup_lines.txt")).build(), HttpResponse.BodyHandlers.ofString());
                    if (pickuplinesRes.statusCode() >= 400) {
                        this.plugin.getLogger().log(Level.WARNING, "Failed to get pickup lines through github: " + pickuplinesRes.body());
                        hasPickupLines = false; // Status code for an error
                    }
                    String pickupLines = pickuplinesRes.body();
                    this.pickupLines = Arrays.stream(pickupLines.split("\n")).filter(line -> !line.trim().isEmpty()).toArray(String[]::new);
                    this.plugin.getLogger().log(Level.INFO, "Loaded pickup lines! [GitHub]");
                } catch (Exception e) {
                    this.plugin.getLogger().log(Level.WARNING, "Failed to get pickup lines through github: ", e);
                    hasPickupLines = false; // Status code for an error
                }
            }
        }
        if (!hasPickupLines) {
            hasPickupLines = true;
            // Attempt 3 (or always offline mode is on): pickuplines.yml
            // Do not attempt the github if always using offline pickup lines
            try {
                File rizzmeConfig = new File(this.plugin.getDataFolder(), "pickuplines.yml");
                YamlConfiguration pickuplinesYml = YamlConfiguration.loadConfiguration(rizzmeConfig);
                if (!pickuplinesYml.isList("pickuplines")) {
                    this.plugin.getLogger().log(Level.WARNING, "Failed to get pickup lines through pickuplines.yml: pickuplines is not a list");
                    hasPickupLines = false;
                }
                this.pickupLines = pickuplinesYml.getList("pickuplines").stream().map(Object::toString).toArray(String[]::new);
                this.plugin.getLogger().log(Level.INFO, "Loaded pickup lines! [pickuplines.yml]");
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.WARNING, "Failed to get pickup lines through pickuplines.yml: ", e);
                hasPickupLines = false;
            }
        }
        if (!hasPickupLines) {
            this.pickupLines = new String[]{"Are you a magician? Because whenever I look at you, everyone else disappears.","Do you have a map? I keep getting lost in your eyes.","Are you French? Because Eiffel for you.","If you were a vegetable, you'd be a cute-cumber!","Do you have a name, or can I call you mine?","Are you a time traveler? Because I see you in my future.","Is your name Wi-Fi? Because I’m feeling a connection.","Are you made of copper and tellurium? Because you’re Cu-Te.","Do you believe in love at first sight, or should I walk by again?","Are you a loan? Because you have my interest!","Are you a campfire? Because you’re hot and I want s'more.","Do you like Star Wars? Because Yoda one for me.","Are you a parking ticket? Because you’ve got FINE written all over you.","You must be tired because you’ve been running through my mind all day.","Are you an alien? Because you’ve abducted my heart.","Are you a keyboard? Because you’re my type.","Is your dad a boxer? Because you’re a knockout.","Are you Australian? Because when I look at you, I feel like I’m down under.","Are you a 90-degree angle? Because you’re looking right.","Do you like raisins? How do you feel about a date?","Are you the ocean? Because I’m lost at sea.","You must be made of stardust because you’re out of this world.","If you were a fruit, you’d be a fineapple.","Are you a snowstorm? Because you just made my heart freeze.","Is your last name Gillette? Because you’re the best a man can get.","Are you a dictionary? Because you’re adding meaning to my life.","Are you a sunrise? Because you brighten up my day.","Are you Google? Because you’ve got everything I’ve been searching for.","Do you have an eraser? Because I can't get you out of my mind.","Are you a shooting star? Because you just made my wish come true.","You must be a camera because every time I see you, I smile.","Are you a light bulb? Because you just brightened my day.","Are you a lock? Because you’ve got the key to my heart.","Are you a cloud? Because you’ve got me on cloud nine.","Are you a playlist? Because you’re my favorite track.","Are you a pizza? Because I’m falling for you slice by slice.","Are you a bank loan? Because you have my interest.","Do you have a Band-Aid? Because I just scraped my knee falling for you.","Are you the moon? Because even in the darkest times, you light up my world.","Do you have a twin? Because you’re twice as stunning.","If you were a star, you'd be the brightest in the galaxy.","Is your heart made of chocolate? Because you’re sweet inside and out.","Are you my phone charger? Because I feel a spark.","Do you have a pencil? Because I want to write you into my future.","Are you an FPS booster? Cuz you be looking opti**fine**.","Are you a snowflake? Because you’re one of a kind.","Are you a constellation? Because you shine brighter than the stars.","Are you an art piece? Because every detail of you is perfection.","Are you a sunflower? Because you brighten even the gloomiest days.","Are you a lighthouse? Because you guide me home.","Are you a melody? Because you’re stuck in my head.","Are you Houdert? Because every time Diddy crashes, you're the only one who can fix my broken heart—and my broken code. I'm helpless without you.","Are you my pillow because i can rest on you.","Are you my debt cause all i think about is you.","All the pickaxe but you still can't be mine.","Are you diddy because when i see you i try to oil you up.","Are you a couch because i love riding you.","Math always talk about x and y but never you and i.","Are you a Fw-190? Cause i feel hot whenever im near you.","You must be a Radar-Guided missile, cause my heart beats faster when i see you.","Are you a Twix bar? Cause i’ll go for any direction for you.","I feel like SpongeBob right now, Cause I can't wait to get to your bikini bottom."};
            this.plugin.getLogger().log(Level.WARNING, "Loaded pickup lines! [hardcoded]");
        }

        // DiscordSRV Interface
        srv = MaybeHasSRV.makeSRVInterface(this.plugin.getServer().getPluginManager());
    }

    // Commands:

    /**
     * Called when the rizzme command runs
     * @param sender The entity who ran the rizzme command
     */
    public void rizzMeCommand(CommandSender sender) {
        String pickupline = this.pickupLines[(int)(Math.random() * pickupLines.length)];
        this.sender.broadcastMessage(this.plugin.getServer(), pickupline, sender.getName() + " used /rizzme");
    }

    /**
     * Called when the oil command runs
     * @param sender The entity who ran the rizzme command
     */
    public void oilCommand(CommandSender sender, Entity target) {
        if (sender instanceof Entity) {
            this.sender.broadcastMessage(this.plugin.getServer(), "%m1 oiled up %m2", sender.getName() + " used /oil", (Entity) sender, target);
        } else {
            this.sender.broadcastMessage(this.plugin.getServer(), sender.getName() + " oiled up %m1", sender.getName() + " used /oil", target);
        }
    }

    /**
     * Called when the diddle command runs
     * @param sender The entity who ran the diddle command
     */
    public void diddleCommand(CommandSender sender, Entity target) {
        this.sender.broadcastMessage(this.plugin.getServer(), "%m1 has been diddled", sender.getName() + " used /diddle", target);
    }

    public void getAuraCommand(CommandSender sender, Player target) {
        String userId = this.srv.getLinkedDCID(target);
        if (userId == null) {
            this.sender.sendMessage(sender, "The player " + target.getName() + " has not linked their discord account, and therefore their aura cannot be checked :(", null, false, true);
            return;
        }
        try {
            HttpResponse<String> getAuraRes = apiClient.send(HttpRequest.newBuilder().uri(URI.create(diddyApi + "/getaura/" + userId)).build(), HttpResponse.BodyHandlers.ofString());
            if (getAuraRes.statusCode() >= 400) {
                this.plugin.getLogger().log(Level.WARNING, "Failed to get aura through API: " + getAuraRes.body());
                this.sender.sendMessage(sender, "Failed to get aura.", null, false, true);
                return;
            }
            String auraJsonText = getAuraRes.body();
            JsonElement auraJson = JsonParser.parseString(auraJsonText);
            if (auraJson.isJsonObject()) {
                int aura = auraJson.getAsJsonObject().get("aura").getAsInt();
                int level = (int)Math.pow(Math.floor((double)aura / 2), 1/2.25);
                this.sender.broadcastMessage(this.plugin.getServer(), "%m1 has " + aura + " aura and has a sigma level of " + level, sender.getName() + " used /getaura", target);
            } else {
                this.plugin.getLogger().log(Level.WARNING, "Failed to get aura through API: " + getAuraRes.body());
                this.sender.sendMessage(sender, "Failed to get aura.", null, false, true);
                return;
            }
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to get aura through API: ", e);
            this.sender.sendMessage(sender, "Failed to get aura.", null, false, true);
            return;
        }
    }
}
