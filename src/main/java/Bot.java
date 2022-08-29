

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class Bot {
    private static final String token = "token";
    private static final DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();


//    public static void roleCreator(Server server) {
//        RoleBuilder roleBuilder = new RoleBuilder(server);
//        List<Role> roleList = server.getRoles();
//        boolean find = false;
//
//
//        for (Role role : roleList) {
//            if (role.getName().equals("MeriCo")) {
//                find= true;
//            }
//        }
//
//        if(find){
//            return;
//        }else {
//            Pe
//            roleBuilder.setName("MeriCo");
//            roleBuilder.setPermissions(8);
//
//        }
//
//    }


    public static void main(String[] args) {
//--------------------------------------------------------------------------
//--------------------------------------------------------------------------
        //----------------Server----------------
        String user = "root";
        String password = "root";
        String URL = "jdbc:mysql://localhost:3306/discordmerico?useSSL=false";
//--------------------------------------------------------------------------
//--------------------------------------------------------------------------


//--------------------------------------------------------------------------
//--------------------------------------------------------------------------
        api.addMessageCreateListener(event -> {
            try (Connection connection = DriverManager.getConnection(URL, user, password)) {
                api.updateActivity("~help / ~helpAdmin - Starter commands ");
                Statement statement = connection.createStatement();
                String serverName = event.getServer().get().getName();
                long serverId = event.getServer().get().getId();
                String nick = event.getMessageAuthor().getName();               // serverName serverID
                long id = event.getMessageAuthor().getId();                     // nick Id


                //-----------------------------------------------------------------Проверка на наличие в таблице
                ResultSet resultSet = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "' and serverId = '" + serverId + "'");
                boolean inDataBaseOfIdPeople = resultSet.next();

//-----------------------------------------------------------------------------Добавление нового юсера в таблицу
                if (!inDataBaseOfIdPeople && !event.getMessageAuthor().isBotUser()) {
                    int startXp = 5;
                    int strartCash = 300;
                    int startLvl = 0;
                    statement.execute("INSERT INTO discordmerico values (" + id + ", '" + nick + "' ," + strartCash + "," + startXp + "," + startLvl + ", '" + serverName + "' ," + serverId + ")");
                } else {
//-----------------------------------------------------------------------------Добавление опыта за сообщение
                    statement.execute("UPDATE discordmerico SET xp=xp+5 where id = '" + id + "' and serverId = '" + serverId + "'");


//----------------------------------------------------------------------------------------Функция профиль
                    if (event.getMessageContent().equalsIgnoreCase("~profile") && !event.getMessageAuthor().isBotUser()) {

                        // Достает XP
                        ResultSet resultSet1 = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "' and serverId = '" + serverId + "'");
                        resultSet1.next();
                        int xp = resultSet1.getInt("xp");
                        int cash = resultSet1.getInt("cash");
                        int lvl = resultSet1.getInt("lvl");


                        new MessageBuilder()
                                .append("User's profile " + nick + "("+lvl+" lvl) :", MessageDecoration.BOLD)
                                .setEmbed(new EmbedBuilder()
                                        .setTitle(":comet: " + "Xp" + ":comet: ")
                                        .setDescription("Xp: " + xp)
                                        .setColor(Color.magenta))
                                .send(event.getChannel());
                        new MessageBuilder()
                                .setEmbed(new EmbedBuilder()
                                        .setTitle(":coin: " + "Balance" + ":coin: ")
                                        .setDescription("Cash: " + cash)
                                        .setColor(Color.YELLOW))
                                .send(event.getChannel());
                    }
//----------------------------------------------------------------------------------------wallet
                    if (event.getMessageContent().equalsIgnoreCase("~balance") && !event.getMessageAuthor().isBotUser() || event.getMessageContent().equalsIgnoreCase("~wallet") && !event.getMessageAuthor().isBotUser() || event.getMessageContent().equalsIgnoreCase("~money") && !event.getMessageAuthor().isBotUser()) {

                        // Достает XP Cash
                        ResultSet resultSet1 = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "' and serverId = '" + serverId + "'");
                        resultSet1.next();
                        int cash = resultSet1.getInt("cash");

                        new MessageBuilder()
                                .setEmbed(new EmbedBuilder()
                                        .setTitle(":coin: " + "Balance" + ":coin: ")
                                        .setDescription("Cash: " + cash)
                                        .setColor(Color.YELLOW))
                                .send(event.getChannel());
                    }
//-----------------------------------------------------------------------------------------------------------------------------Lvls
                    // Достает XP
                    ResultSet resultSet1 = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "' and serverId = '" + serverId + "'");
                    if (resultSet1.next()) {
                        int xp = resultSet1.getInt("xp");


                        //---------------------------------------Lvls

                        if (xp % 100 == 0 && xp <= 500) {
                            statement.execute("UPDATE discordmerico SET lvl=lvl+1 where id = '" + id + "' and serverId = '" + serverId + "'");
                            statement.execute("UPDATE discordmerico SET cash=cash+150 where id = '" + id + "' and serverId = '" + serverId + "'");
                            ResultSet lvldostat = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "' and serverId = '" + serverId + "'");
                            if (lvldostat.next()) {
                                int lvl = lvldostat.getInt("lvl");
                                event.getChannel().sendMessage("Player" + nick + " level upped! Your lvl :" + lvl + ". And bonus 150:coin: already on ur bank.");
                            }
                        }
                        if (xp % 200 == 0 && xp <= 1500 && xp > 500) {
                            statement.execute("UPDATE discordmerico SET lvl=lvl+1 where id = '" + id + "' and serverId = '" + serverId + "'");
                            statement.execute("UPDATE discordmerico SET cash=cash+100 where id = '" + id + "' and serverId = '" + serverId + "'");
                            ResultSet lvldostat = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "' and serverId = '" + serverId + "'");
                            if (lvldostat.next()) {
                                int lvl = lvldostat.getInt("lvl");
                                event.getChannel().sendMessage("Player" + nick + " level upped! Your lvl :" + lvl + ". And bonus 100:coin: already on ur bank.");
                            }
                        }
                        if (xp % 500 == 0 && xp <= 46500 && xp > 1500) {
                            statement.execute("UPDATE discordmerico SET lvl=lvl+1 where id = '" + id + "' and serverId = '" + serverId + "'");
                            statement.execute("UPDATE discordmerico SET cash=cash+50 where id = '" + id + "' and serverId = '" + serverId + "'");
                            ResultSet lvldostat = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "' and serverId = '" + serverId + "'");
                            if (lvldostat.next()) {
                                int lvl = lvldostat.getInt("lvl");
                                event.getChannel().sendMessage("Player" + nick + " level upped! Your lvl :" + lvl + ". And bonus 50:coin: already on ur bank.");
                            }
                        }
                    }
//--------------------------------------------------------------------------------------------------------------------Admin commands
                    if (event.getMessageContent().startsWith("~getmoney")) {
                        if (event.getMessageAuthor().isServerAdmin()) {
                            String msg = event.getMessageContent();
                            String[] parts = msg.split("\\s+");
                            if (parts.length == 2) {
                                String balanc = parts[1];
                                int balance = Integer.parseInt(balanc);
                                if (balance > 0) {
                                    if (balance<=5000) {
                                        statement.execute("UPDATE discordmerico SET cash=cash+" + balance + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                        event.getChannel().sendMessage( nick +"'s wallet topped up on " + balance + ":coin:");
                                    }else event.getChannel().sendMessage("Max amount of coins to get - 5000 :coin:");
                                } else event.getChannel().sendMessage("Wrong number of coins");
                            } else event.getChannel().sendMessage("Wrong command , try again");
                        } else event.getChannel().sendMessage("You don't have permissions to do that");
                    }
//-------------------------------------------------------------------------------------------------------------Передача Денег

                    if (event.getMessageContent().startsWith("~send") && !event.getMessageAuthor().isBotUser()) {
                        String msg = event.getMessageContent();
                        String[] parts = msg.split("\\s+");

                        if (parts.length == 3) {
                            String trans = parts[1];
                            String money = parts[2];
                            if (money.matches("\\d+")) {
                                ResultSet cashfounder = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "' and serverId = '" + serverId + "'");
                                if (cashfounder.next()) {
                                    int cash = cashfounder.getInt("cash");
                                    int moneyValue = Integer.parseInt(money);
                                    if (cash >= moneyValue) {
                                        ResultSet pepfounder = statement.executeQuery("SELECT * from discordmerico where name = '" + trans + "'and serverId = '" + serverId + "'");
                                        if (pepfounder.next()) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + moneyValue + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + moneyValue + " where name = '" + trans + "' and serverId = '" + serverId + "'");
                                            event.getChannel().sendMessage("You have transferred to " + trans + " " + moneyValue + ":coin:");
                                        } else event.getChannel().sendMessage("I don't see this person on the server :no_entry_sign: ");

                                    } else
                                        event.getChannel().sendMessage("Not enough money. Your balance:" + cash + ":coin:");
                                }
                            } else
                                event.getChannel().sendMessage("Wrong command , try again (number of money):no_entry_sign: ");

                        } else event.getChannel().sendMessage("Wrong command , try again :no_entry_sign: ");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Roles


                    List<Role> roles = event.getServer().get().getRoles();
                    ArrayList<String> shopik = new ArrayList<>();
                    ArrayList<String> shopikCost = new ArrayList<>();

                    Optional<User> userDis = event.getMessageAuthor().asUser();
                    User userDiscord = userDis.get();

                    int numberBuy = 0;

//---------------------------------------------------------------------------------------------------------------------------------------Roles
                    if (event.getMessageContent().startsWith("~shopAdd") && !event.getMessageAuthor().isBotUser()) {
                        if (event.getMessageAuthor().isServerAdmin()) {
                            String msg = event.getMessageContent();
                            String[] parts = msg.split("\\s+");
                            if (parts.length == 3) {
                                String roleName = parts[1];
                                String costy = parts[2];
                                boolean roleBoolean = false;

                                for (Role role : roles) {
                                    if (role.getName().equals(roleName)) {
                                        roleBoolean = true;
                                        break;
                                    }
                                }

                                if (roleBoolean) {
                                    int cost = Integer.parseInt(costy);
                                    resultSet = statement.executeQuery("SELECT * from discordmericoroles where rolename = '" + roleName + "'and serverId = '" + serverId + "'");
                                    boolean inDataBaseOfRoles = resultSet.next();
                                    if (!inDataBaseOfRoles) {
                                        if (cost > 0) {
                                            System.out.println(serverId);
                                                statement.execute("INSERT INTO discordmericoroles values ('" + roleName + "' ," + cost + ",'" + serverName + "' ," + serverId + ")");
                                                event.getChannel().sendMessage("Product has been added to cart :newspaper:");
                                        } else
                                            event.getChannel().sendMessage("Wrong cost number :no_entry_sign: ");
                                    } else
                                        event.getChannel().sendMessage("We already have this role in the shop :no_entry_sign: ");
                                } else
                                    event.getChannel().sendMessage("Тhis role does not exist on server :no_entry_sign: ");
                            }
                        } else event.getChannel().sendMessage("You are not the owner of this server(:no_entry_sign: ");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Roles
                    if (event.getMessageContent().startsWith("~shopChange") && !event.getMessageAuthor().isBotUser()) {
                        if (event.getMessageAuthor().isServerAdmin()) {
                            String msg = event.getMessageContent();
                            String[] parts = msg.split("\\s+");
                            if (parts.length == 3) {
                                String roleName = parts[1];
                                String costy = parts[2];
                                int cost = Integer.parseInt(costy);

                                resultSet = statement.executeQuery("SELECT * from discordmericoroles where rolename = '" + roleName + "'and serverId = '" + serverId + "'");
                                boolean inDataBaseOfRoles = resultSet.next();
                                if (inDataBaseOfRoles) {
                                    if (cost > 0) {
                                        statement.execute("UPDATE discordmericoroles SET cost=" + cost + " where serverId = '" + serverId + "' AND rolename = '" + roleName + "' ");
                                        event.getChannel().sendMessage("You have change the price :newspaper:");

                                    } else event.getChannel().sendMessage("Wrong cost number :no_entry_sign: ");
                                } else event.getChannel().sendMessage("I don't see this role in base :no_entry_sign: ");
                            } else event.getChannel().sendMessage("Wrong command  :no_entry_sign: ");
                        } else event.getChannel().sendMessage("You are not the owner :no_entry_sign: ");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Roles
                    if (event.getMessageContent().startsWith("~shopDelete") && !event.getMessageAuthor().isBotUser()) {
                        if (event.getMessageAuthor().isServerAdmin()) {
                            String msg = event.getMessageContent();
                            String[] parts = msg.split("\\s+");
                            if (parts.length == 2) {
                                String roleName = parts[1];
                                resultSet = statement.executeQuery("SELECT * from discordmericoroles where rolename = '" + roleName + "'and serverId = '" + serverId + "'");
                                boolean inDataBaseOfRoles = resultSet.next();
                                if (inDataBaseOfRoles) {
                                    statement.execute("DELETE FROM discordmericoroles where rolename = '" + roleName + "'and serverId = '" + serverId + "'");
                                    event.getChannel().sendMessage(":anger:  Role has been deleted  :anger: ");
                                } else event.getChannel().sendMessage("This role isn't in the shop :no_entry_sign: ");
                            } else event.getChannel().sendMessage("Wrong command :no_entry_sign: ");
                        } else event.getChannel().sendMessage("You are not the owner :no_entry_sign: ");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Roles
                    if (event.getMessageContent().startsWith("~shopList") && !event.getMessageAuthor().isBotUser()) {
                        if (event.getMessageAuthor().isServerAdmin()) {
                            resultSet = statement.executeQuery("SELECT * from discordmericoroles where serverId = '" + serverId + "'");
                            while (resultSet.next()) {
                                String rolesText = resultSet.getString("rolename");
                                event.getChannel().sendMessage("~~~" + rolesText);
                            }
                        } else event.getChannel().sendMessage("You are not the owner(:no_entry_sign: ");
                    }

//---------------------------------------------------------------------------------------------------------------------------------------Smile
                    ArrayList<String> circlesEmoji = new ArrayList<>();
                    circlesEmoji.add(":red_circle:");
                    circlesEmoji.add(":white_circle:");
                    circlesEmoji.add(":green_circle:");
                    circlesEmoji.add(":yellow_circle:");
                    circlesEmoji.add(":purple_circle:");
                    circlesEmoji.add(":orange_circle:");
                    circlesEmoji.add(":blue_circle:");
                    int nachalo = 0;
                    int konec = 6;
                    int random_number = nachalo + (int) (Math.random() * konec);

//---------------------------------------------------------------------------------------------------------------------------------------Smile
                    if (event.getMessageContent().equalsIgnoreCase("~shop") && !event.getMessageAuthor().isBotUser()) {
                        resultSet = statement.executeQuery("SELECT * from discordmericoroles where serverId = '" + serverId + "'");
                        while (resultSet.next()) {
                            String rolesNames = resultSet.getString("rolename");
                            String roleCost = resultSet.getString("cost");
                            shopik.add(rolesNames);
                            shopikCost.add(roleCost);
                        }
                        int sizeOfServerRoles = shopik.size();
                        if (sizeOfServerRoles > 0) {
                            if (sizeOfServerRoles <= 9) {

                                event.getChannel().sendMessage("**┌ ––––––––––––––––––––––––––––– **");
                                for (int i = 0; i < sizeOfServerRoles; i++) {
                                    int b = i + 1;
                                    event.getChannel().sendMessage("" + " |      " + circlesEmoji.get(random_number) + " " + b + "- **Role : " + shopik.get(i) + "**   'Cost- " + shopikCost.get(i) + ":coin: '");
                                }
                                event.getChannel().sendMessage("**└ ––––––––––––––––––––––––––––– **");
                            }
                        } else event.getChannel().sendMessage("Shop is empty :thought_balloon:");

                    }
//---------------------------------------------------------------------------------------------------------------------------------------Shop
                    if (event.getMessageContent().startsWith("~buy") && !event.getMessageAuthor().isBotUser()) {
                        String msg = event.getMessageContent();
                        String[] parts = msg.split("\\s+");

                        resultSet = statement.executeQuery("SELECT * from discordmericoroles where serverId = '" + serverId + "'");
                        while (resultSet.next()) {
                            String rolesNames = resultSet.getString("rolename");
                            String roleCost = resultSet.getString("cost");
                            shopik.add(rolesNames);
                            shopikCost.add(roleCost);
                        }

                        if (parts.length == 2) {
                            String roleName = parts[1];
                            if (shopik.contains(roleName)) {


                                ResultSet cashFounderOfRole = statement.executeQuery("SELECT * from discordmericoroles where rolename = '" + roleName + "'and serverId = '" + serverId + "'");
                                if (cashFounderOfRole.next()) {
                                    int costRole = cashFounderOfRole.getInt("cost");

                                    ResultSet cashfounder = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "'and serverId = '" + serverId + "'");
                                    if (cashfounder.next()) {
                                        int cash = cashfounder.getInt("cash");

                                        if (cash >= costRole) {


                                            for (Role role : roles) {
                                                if (role.getName().equals(roleName)) {
                                                    numberBuy = role.getPosition();
                                                    break;
                                                }
                                            }

                                            statement.execute("UPDATE discordmerico SET cash=cash-" + costRole + " where id = '" + id + "'and serverId = '" + serverId + "'");


                                            userDiscord.addRole(roles.get(numberBuy));

                                            event.getChannel().sendMessage(":beginner: You just bought the role :beginner: Congratulations :beginner: ");


                                        } else
                                            event.getChannel().sendMessage("Not enough money. Your balance:" + cash + ":coin:");

                                    }
                                }
                            } else
                                event.getChannel().sendMessage("This role isn't in the shop :no_entry_sign: Check shop -> '~shop' ");

                        } else event.getChannel().sendMessage("Wrong command :no_entry_sign: ");

                    }


//---------------------------------------------------------------------------------------------------------------------------------------Roulette


                    ArrayList<Integer> black = new ArrayList<>();
                    black.add(2);
                    black.add(4);
                    black.add(6);
                    black.add(11);
                    black.add(10);
                    black.add(13);
                    black.add(15);
                    black.add(17);
                    black.add(20);
                    black.add(22);
                    black.add(24);
                    black.add(29);
                    black.add(33);
                    black.add(31);
                    black.add(35);

                    if (event.getMessageContent().startsWith("~roulette") && !event.getMessageAuthor().isBotUser()) {
                        ResultSet resultSet2 = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "'and serverId = '" + serverId + "'");
                        resultSet2.next();
                        int cash = resultSet2.getInt("cash");
                        String msg = event.getMessageContent();
                        String[] parts = msg.split("\\s+");
                        if (parts.length == 3) {
                            String rouletteGame = parts[1];
                            String rouletteCas = parts[2];
                            int rouletteCash = Integer.parseInt(rouletteCas);
                            int a = 0;
                            int b = 36;
                            int rolledNumber = a + (int) (Math.random() * b);


                            if (rouletteCash < cash) {
                                if (rouletteCash > 0) {
                                    if (rouletteGame.equals("black")) {
                                        if (black.contains(rolledNumber)) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDD25You won!  (Rolled number :" + rolledNumber + ":black_circle: ) Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber != 0 && !black.contains(rolledNumber)) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose! ( Rolled number :" + rolledNumber + ":red_circle: ) Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber == 0) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + ":green_circle:) Your balance:" + cash + ":coin:");
                                        }
                                    }

                                    if (rouletteGame.equals("red")) {
                                        if (!black.contains(rolledNumber)) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDD25You won !  (Rolled number :" + rolledNumber + ":red_circle:)  Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber != 0 && black.contains(rolledNumber)) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + ":black_circle:) Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber == 0) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose! ( Rolled number :" + rolledNumber + ":green_circle:) Your balance:" + cash + ":coin:");
                                        }
                                    }

                                    if (rouletteGame.equals("1st12")) {
                                        if (rolledNumber < 13 && random_number != 0) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + "*3 where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + rouletteCash * 3;
                                            event.getChannel().sendMessage("\uD83D\uDD25You won !  (Rolled number :" + rolledNumber + "\uD83D\uDD30)  Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber > 12) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + "\uD83D\uDD30) Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber == 0) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + ":green_circle:) Your balance:" + cash + ":coin:");
                                        }
                                    }

                                    if (rouletteGame.equals("2nd12")) {
                                        if (rolledNumber < 25 && rolledNumber > 12) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + "*3 where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + rouletteCash * 3;
                                            event.getChannel().sendMessage("\uD83D\uDD25You won !  (Rolled number :" + rolledNumber + "\uD83D\uDD30)  Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber < 13 && rolledNumber != 0 || rolledNumber > 24) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + "\uD83D\uDD30) Your balance:" + cash + ":coin:");

                                        } else {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + ":green_circle:) Your balance:" + cash + ":coin:");
                                        }
                                    }

                                    if (rouletteGame.equals("3nd12")) {
                                        if (rolledNumber > 24) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + "*3 where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + rouletteCash * 3;
                                            event.getChannel().sendMessage("\uD83D\uDD25You won !  (Rolled number :" + rolledNumber + "\uD83D\uDD30)  Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber != 0) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + "\uD83D\uDD30) Your balance:" + cash + ":coin:");

                                        } else {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + ":green_circle:) Your balance:" + cash + ":coin:");
                                        }
                                    }

                                    if (rouletteGame.equals("1to18")) {
                                        if (rolledNumber > 0 && rolledNumber < 19) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDD25You won !  (Rolled number :" + rolledNumber + "\uD83D\uDD30)  Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber > 18) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + "\uD83D\uDD30) Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber == 0) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number:" + rolledNumber + ":green_circle:) Your balance:" + cash + ":coin:");
                                        }
                                    }

                                    if (rouletteGame.equals("19to36")) {
                                        if (rolledNumber > 19 && rolledNumber < 37) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDD25You won !  (Rolled number :" + rolledNumber + "\uD83D\uDD30)  Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber < 19 && rolledNumber != 0) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose! (Rolled number:" + rolledNumber + "\uD83D\uDD30) Your balance:" + cash + ":coin:");

                                        } else if (rolledNumber == 0) {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + ":green_circle:) Your balance:" + cash + ":coin:");
                                        }
                                    }

                                    if (rouletteGame.equals("even")) {
                                        if (rolledNumber != 0) {
                                            if (rolledNumber % 2 == 0) {
                                                statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                                cash = cash + rouletteCash;
                                                event.getChannel().sendMessage("\uD83D\uDD25You won !  (Rolled number :" + rolledNumber + "\uD83D\uDD30)  Your balance:" + cash + ":coin:");

                                            } else {
                                                statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                                cash = cash - rouletteCash;
                                                event.getChannel().sendMessage("\uD83D\uDE2CLose! (Rolled number :" + rolledNumber + "\uD83D\uDD30) Your balance:" + cash + ":coin:");

                                            }
                                        } else {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + ":green_circle:) Your balance:" + cash + ":coin:");
                                        }
                                    }

                                    if (rouletteGame.equals("odd")) {
                                        if (rolledNumber != 0) {
                                            if (rolledNumber % 2 != 0) {
                                                statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                                cash = cash + rouletteCash;
                                                event.getChannel().sendMessage("\uD83D\uDD25You won !  (Rolled number :" + rolledNumber + "\uD83D\uDD30)  Your balance:" + cash + ":coin:");

                                            } else {
                                                statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                                cash = cash - rouletteCash;
                                                event.getChannel().sendMessage("\uD83D\uDE2CLose! (Rolled number :" + rolledNumber + "\uD83D\uDD30) Your balance:" + cash + ":coin:");

                                            }
                                        } else {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - rouletteCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2CLose!  (Rolled number :" + rolledNumber + ":green_circle:) Your balance:" + cash + ":coin:");
                                        }
                                    }

                                    if (rouletteGame.equals("0")) {
                                        if (rolledNumber == 0) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + rouletteCash + "*14 where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + rouletteCash * 14;
                                            event.getChannel().sendMessage("\uD83D\uDD25You won !  (Rolled number :" + rolledNumber + ":green_circle:)  Your balance:" + cash + ":coin:");

                                        } else
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + rouletteCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                        cash = cash - rouletteCash;
                                        event.getChannel().sendMessage("\uD83D\uDE2CLose! (Rolled number :" + rolledNumber + "\uD83D\uDD30) Your balance:" + cash + ":coin:");
                                    }
                                } else event.getChannel().sendMessage("Wrong amount :coin:");
                            } else event.getChannel().sendMessage("Not enough money. Your balance:" + cash + ":coin:");
                        } else event.getChannel().sendMessage("Wrong command:no_entry_sign: ");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Roulette
                    if (event.getMessageContent().startsWith("~coinflip") && !event.getMessageAuthor().isBotUser()) {
                        ResultSet resultSet2 = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "'and serverId = '" + serverId + "'");
                        resultSet2.next();
                        int cash = resultSet2.getInt("cash");

                        String msg = event.getMessageContent();
                        String[] parts = msg.split("\\s+");
                        int a = 0;
                        int b = 11;
                        int rolledNumber = a + (int) (Math.random() * b);

                        if (parts.length == 3) {
                            String sideOfCoin = parts[1];
                            String betOfCoinflip = parts[2];
                            int coinflipCash = Integer.parseInt(betOfCoinflip);
                            if (coinflipCash <= cash) {

                                if (coinflipCash > 0) {
                                    if (sideOfCoin.equals("heads")) {
                                        if (rolledNumber >= 5) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + coinflipCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + coinflipCash;
                                            event.getChannel().sendMessage("\uD83D\uDD25 You won !  (Came up heads :fleur_de_lis: )  Your balance:" + cash + ":coin:");
                                            new MessageBuilder()
                                                    .addAttachment(new File("C:/MeriCo/Heads.png"))
                                                    .send(event.getChannel());

                                        } else {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + coinflipCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - coinflipCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2C You lose !  (Came up tails :fleur_de_lis: )  Your balance:" + cash + ":coin:");
                                            new MessageBuilder()
                                                    .addAttachment(new File("C:/MeriCo/Tails.png"))
                                                    .send(event.getChannel());
                                        }
                                    }

                                    if (sideOfCoin.equals("tails")) {
                                        if (rolledNumber >= 5) {
                                            statement.execute("UPDATE discordmerico SET cash=cash+" + coinflipCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash + coinflipCash;
                                            event.getChannel().sendMessage("\uD83D\uDD25 You won !  (Came up tails :fleur_de_lis: )  Your balance:" + cash + ":coin:");
                                            new MessageBuilder()
                                                    .addAttachment(new File("C:/MeriCo/Tails.png"))
                                                    .send(event.getChannel());

                                        } else {
                                            statement.execute("UPDATE discordmerico SET cash=cash-" + coinflipCash + " where id = '" + id + "' and serverId = '" + serverId + "'");
                                            cash = cash - coinflipCash;
                                            event.getChannel().sendMessage("\uD83D\uDE2C You lose !   (Came up heads :fleur_de_lis: )  Your balance:" + cash + ":coin:");
                                            new MessageBuilder()
                                                    .addAttachment(new File("C:/MeriCo/Heads.png"))
                                                    .send(event.getChannel());
                                        }

                                    }


                                } else event.getChannel().sendMessage("Wrong amount :coin:");
                            } else event.getChannel().sendMessage("Not enough money . Your balance:" + cash + ":coin:");
                        } else event.getChannel().sendMessage("Wrong command :no_entry_sign: ");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------------------------------------------------------------Gifts
                    if (event.getMessageContent().equalsIgnoreCase("~getGiftLvl5") && !event.getMessageAuthor().isBotUser()) {
                        ResultSet infoGeter = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "'and serverId = '" + serverId + "'");
                        infoGeter.next();
                        int lvl = infoGeter.getInt("lvl");
                        int gift = infoGeter.getInt("gift");
                        if (lvl >= 5) {
                            if (gift == 0) {
                                statement.execute("UPDATE discordmerico SET gift=gift+1 where id = '" + id + "' and serverId = '" + serverId + "'");
                                statement.execute("UPDATE discordmerico SET cash=cash+500 where id = '" + id + "' and serverId = '" + serverId + "'");
                                event.getChannel().sendMessage("You received a gift for 5 lvl its 500 coins! :gift: ");
                            } else event.getChannel().sendMessage("You already received a gift for 5 lv :cocktail: ");
                        } else
                            event.getChannel().sendMessage("You dont have 5 lvl to get the gift . :stars: Your lvl :" + lvl + ":stars:");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Gifts
                    if (event.getMessageContent().equalsIgnoreCase("~getGiftLvl10") && !event.getMessageAuthor().isBotUser()) {
                        ResultSet infoGeter = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "'and serverId = '" + serverId + "'");
                        infoGeter.next();
                        int lvl = infoGeter.getInt("lvl");
                        int gift = infoGeter.getInt("gift");
                        if (lvl >= 10) {
                            if (gift == 1) {
                                statement.execute("UPDATE discordmerico SET gift=gift+1 where id = '" + id + "' and serverId = '" + serverId + "'");
                                statement.execute("UPDATE discordmerico SET cash=cash+1000 where id = '" + id + "' and serverId = '" + serverId + "'");
                                event.getChannel().sendMessage("You received a gift for 10 lvl its 1000 coins! :gift: ");
                            } else event.getChannel().sendMessage("You already received a gift for 10 lvl :cocktail: ");
                        } else
                            event.getChannel().sendMessage("You dont have 10 lvl to get the gift . :stars: Your lvl:" + lvl + ":stars:");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Gifts
                    if (event.getMessageContent().equalsIgnoreCase("~getGiftLvl20") && !event.getMessageAuthor().isBotUser()) {
                        ResultSet infoGeter = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "'and serverId = '" + serverId + "'");
                        infoGeter.next();
                        int lvl = infoGeter.getInt("lvl");
                        int gift = infoGeter.getInt("gift");
                        if (lvl >= 20) {
                            if (gift == 2) {
                                statement.execute("UPDATE discordmerico SET gift=gift+1 where id = '" + id + "' and serverId = '" + serverId + "'");
                                statement.execute("UPDATE discordmerico SET cash=cash+1000 where id = '" + id + "' and serverId = '" + serverId + "'");
                                event.getChannel().sendMessage("You received a gift for 20 lvl its 1000 coins! :gift: ");
                            } else event.getChannel().sendMessage("You already received a gift for 20 lvl :cocktail: ");
                        } else
                            event.getChannel().sendMessage("You dont have 20 lvl to get the gift . :stars: Your lvl:" + lvl + ":stars:");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Gifts
                    if (event.getMessageContent().equalsIgnoreCase("~getGiftLvl30") && !event.getMessageAuthor().isBotUser()) {
                        ResultSet infoGeter = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "'and serverId = '" + serverId + "'");
                        infoGeter.next();
                        int lvl = infoGeter.getInt("lvl");
                        int gift = infoGeter.getInt("gift");
                        if (lvl >= 30) {
                            if (gift == 3) {
                                statement.execute("UPDATE discordmerico SET gift=gift+1 where id = '" + id + "' and serverId = '" + serverId + "'");
                                statement.execute("UPDATE discordmerico SET cash=cash+1000 where id = '" + id + "' and serverId = '" + serverId + "'");
                                event.getChannel().sendMessage("You received a gift for 30 lvl its 1000 coins! :gift: ");
                            } else event.getChannel().sendMessage("You already received a gift for 30 lvl :cocktail: ");
                        } else
                            event.getChannel().sendMessage("You dont have 30 lvl to get the gift . :stars: Your lvl:" + lvl + ":stars:");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Gifts
                    if (event.getMessageContent().equalsIgnoreCase("~getGiftLvl40") && !event.getMessageAuthor().isBotUser()) {
                        ResultSet infoGeter = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "'and serverId = '" + serverId + "'");
                        infoGeter.next();
                        int lvl = infoGeter.getInt("lvl");
                        int gift = infoGeter.getInt("gift");
                        if (lvl >= 40) {
                            if (gift == 4) {
                                statement.execute("UPDATE discordmerico SET gift=gift+1 where id = '" + id + "' and serverId = '" + serverId + "'");
                                statement.execute("UPDATE discordmerico SET cash=cash+1000 where id = '" + id + "' and serverId = '" + serverId + "'");
                                event.getChannel().sendMessage("You received a gift for 40 lvl its 1000 coins! :gift: ");
                            } else event.getChannel().sendMessage("You already received a gift for 40 lvl :cocktail: ");
                        } else
                            event.getChannel().sendMessage("You dont have 40 lvl to get the gift . :stars: Your lvl:" + lvl + ":stars:");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Gifts
                    if (event.getMessageContent().equalsIgnoreCase("~getGiftLvl50") && !event.getMessageAuthor().isBotUser()) {
                        ResultSet infoGeter = statement.executeQuery("SELECT * from discordmerico where id = '" + id + "'and serverId = '" + serverId + "'");
                        infoGeter.next();
                        int lvl = infoGeter.getInt("lvl");
                        int gift = infoGeter.getInt("gift");
                        if (lvl >= 50) {
                            if (gift == 5) {
                                statement.execute("UPDATE discordmerico SET gift=gift+1 where id = '" + id + "' and serverId = '" + serverId + "'");
                                statement.execute("UPDATE discordmerico SET cash=cash+1000 where id = '" + id + "' and serverId = '" + serverId + "'");
                                event.getChannel().sendMessage("You received a gift for 50 lvl its 1000 coins! :gift: ");
                            } else event.getChannel().sendMessage("You already received a gift for 50 lvl :cocktail: ");
                        } else
                            event.getChannel().sendMessage("You dont have 50 lvl to get the gift . :stars: Your lvl:" + lvl + ":stars:");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------Gifts
                    if (event.getMessageContent().equalsIgnoreCase("~help") && !event.getMessageAuthor().isBotUser()) {
                        new MessageBuilder()
                                .appendCode("HTTP", "**Commands for server members** ")
                                .appendCode("HTTP", "~profile                      (User's profile)\n" +
                                        "~money/balance/wallet                (Wallet)\n" +
                                        "~send <Person>                (Money transfer)\n" +
                                        "~shop                              (Roles shop)\n" +
                                        "~buy <Role's name>         (Buying the role)\n" +
                                        "~roulette (<even><odd><1st12><2nd12><2nd12><1to18><19to36><red><black>) <amount>   (Money game)\n" +
                                        "~coinflip (<tails> <heads>) <amount>              (Money game)\n" +
                                        "~getGiftLvl5-10-20-30-40-50                     (Money gifts for lvls )\n")
                                .send(event.getChannel());
                    }
//---------------------------------------------------------------------------------------------------------------------------------------
                    if (event.getMessageContent().equalsIgnoreCase("~helpAd") && !event.getMessageAuthor().isBotUser()) {
                        if(event.getMessageAuthor().isServerAdmin()) {
                            EmbedBuilder emb = new EmbedBuilder()
                                    .setTitle("**To make the bot work better , make sure that his role is on top**")
                                    .setColor(Color.RED);
                            event.getChannel().sendMessage(emb);
                            new MessageBuilder()
                                    .appendCode("HTTP", "**Commands for server admins** ")
                                    .appendCode("HTTP", "~getmoney <amount>              (unlimited money)\n" +
                                            "~shopAdd <Role name> <cost>           (You can add only role which exists on server / roles with spaces does'nt work )\n" +
                                            "~shopChange <Role name> <NewCost>      (Change the role's cost)\n" +
                                            "~shopDelete <Role name>                 (delete role from shop)\n" +
                                            "~shopList           (list of products in shop/ not really needed command)\n" +
                                            "~guestRoleAdd <Role name>   (Gives role everybody after making first message in chat)\n" +
                                            "~guestRoleDelete (Delete the role which gives to everybody \n")
                                    .send(event.getChannel());
                        } else event.getChannel().sendMessage("You dont have permissions. :x: ");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------
                    if (event.getMessageContent().startsWith("~guestRoleAdd") && !event.getMessageAuthor().isBotUser()) {
                        boolean roleBoolean = false;

                        if (event.getMessageAuthor().isServerAdmin()) {
                            ResultSet infoGeter = statement.executeQuery("SELECT * from starterrole where id = '" + id + "'and serverId = '" + serverId + "'");
                            String msg = event.getMessageContent();
                            String[] parts = msg.split("\\s+");
                            if (parts.length == 2) {
                                String nameOfStarterRole = parts[1];

                                for (Role role : roles) {
                                    if (role.getName().equals(nameOfStarterRole)) {
                                        roleBoolean = true;
                                        break;
                                    }
                                }
                                if (!infoGeter.next()) {
                                    if (roleBoolean) {
                                        statement.execute("INSERT INTO starterrole values (" + id + ", '" + nameOfStarterRole + "', '" + serverName + "' ," + serverId + ")");
                                        event.getChannel().sendMessage(":moyai: The starter role has been added :moyai:  ");
                                    } else event.getChannel().sendMessage("I dont see this role on server :x: ");
                                } else event.getChannel().sendMessage("You already have starter role  :x: .");
                            } else event.getChannel().sendMessage("Wrong command :x: ");
                        } else event.getChannel().sendMessage("You dont have permissions. :x: ");
                    }


//---------------------------------------------------------------------------------------------------------------------------------------
                    if (event.getMessageContent().startsWith("~guestRoleDelete") && !event.getMessageAuthor().isBotUser()) {
                        if (event.getMessageAuthor().isServerAdmin()) {
                            boolean roleBoolean = false;

                            ResultSet infoGeter = statement.executeQuery("SELECT * from starterrole where id = '" + id + "'and serverId = '" + serverId + "'");

                            if (infoGeter.next()) {
                                String roleName = infoGeter.getString("rolename");
                                String msg = event.getMessageContent();
                                String[] parts = msg.split("\\s+");
                                if (parts.length == 1) {

                                    for (Role role : roles) {
                                        if (role.getName().equals(roleName)) {
                                            roleBoolean = true;
                                            break;
                                        }
                                    }
                                    if (roleBoolean) {
                                        statement.execute("DELETE FROM starterrole where id = '" + id + "'and serverId = '" + serverId + "'");
                                        event.getChannel().sendMessage(":moyai: Starter role have been deleted :moyai:  ");
                                    } else event.getChannel().sendMessage("I dont see this role on server :x: ");
                                } else event.getChannel().sendMessage("Wrong command :x: ");
                            } else event.getChannel().sendMessage("You dont have the starter role :x: .");
                        } else event.getChannel().sendMessage("You dont have permissions :x: ");
                    }
//---------------------------------------------------------------------------------------------------------------------------------------
                    if (!event.getMessageAuthor().isBotUser()) {
                        int number = 0;
                        ResultSet infoGeter = statement.executeQuery("SELECT * from starterrole where id = '" + id + "'and serverId = '" + serverId + "'");
                        if (infoGeter.next()) {

                            String roleName = infoGeter.getString("rolename");
                            for (Role role : roles) {
                                if (role.getName().equals(roleName)) {
                                    number = role.getPosition();
                                    break;
                                }
                            }
                        }
                        userDiscord.addRole(roles.get(number));
                    }
//---------------------------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------------------------------------------------------------

                }
//---------------------------------------------------------------------------------------------------------------------------------------


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//----------------------------------------------------------------------------------------Функция q


//--------------------------------------------------------------------------
//--------------------------------------------------------------------------


//--------------------------------------------------------------------------
//--------------------------------------------------------------------------

        });

        System.out.println("Bots link: " + api.createBotInvite());
    }


}
