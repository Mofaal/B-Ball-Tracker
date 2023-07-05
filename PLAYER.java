import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class Player {
    private String name;
    private int points;

    public Player(String name) {
        this.name = name;
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}

class Team {
    private String name;
    private Queue<Player> players;

    public Team(String name) {
        this.name = name;
        this.players = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void addPlayer(Player player) {
        if (players.size() < 5) {
            players.add(player);
        } else {
            JOptionPane.showMessageDialog(null, "The team is already full.", "Team Full", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Queue<Player> getPlayers() {
        return players;
    }
}

class BasketballGame {
    private Queue<Team> teamsOnCourt;
    private Queue<Team> upNextTeams;
    private int gamesWonByTeam1;
    private int gamesWonByTeam2;
    private LocalDateTime startTime;

    public BasketballGame() {
        this.teamsOnCourt = new LinkedList<>();
        this.upNextTeams = new LinkedList<>();
        this.gamesWonByTeam1 = 0;
        this.gamesWonByTeam2 = 0;
        this.startTime = LocalDateTime.now();
    }

    public void addTeam(String teamName) {
        if (teamsOnCourt.size() < 2) {
            Team team = new Team(teamName);
            teamsOnCourt.add(team);
        } else {
            Team team = new Team(teamName);
            upNextTeams.add(team);
        }
    }

    public void addPlayerToTeam(String teamName, String playerName) {
        Team team = findTeam(teamName);
        if (team != null) {
            Player player = new Player(playerName);
            team.addPlayer(player);
        } else {
            JOptionPane.showMessageDialog(null, "Team not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Team findTeam(String teamName) {
        for (Team team : teamsOnCourt) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }
        for (Team team : upNextTeams) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    public String getElapsedTime() {
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void displayScoreAndTeams(JPanel teamsPanel) {
        teamsPanel.removeAll();
        teamsPanel.setLayout(new BoxLayout(teamsPanel, BoxLayout.Y_AXIS));

        StringBuilder scoreContent = new StringBuilder();
        scoreContent.append("<html><body style='font-family: Arial, sans-serif; font-size: 20px; text-align: center;'>");
        scoreContent.append("<h2>Score:</h2>");
        scoreContent.append("<h3>Teams on the court:</h3>");
        for (Team team : teamsOnCourt) {
            scoreContent.append("<h4>").append(team.getName()).append("</h4>");
            scoreContent.append("<ul>");

            for (Player player : team.getPlayers()) {
                scoreContent.append("<li>").append(player.getName()).append("</li>");
            }
            scoreContent.append("</ul>");
        }

        scoreContent.append("<h3>Elapsed Time:</h3>");
        scoreContent.append("<h4>").append(getElapsedTime()).append("</h4>");
        scoreContent.append("</body></html>");

        JLabel scoreLabel = new JLabel(scoreContent.toString());
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        teamsPanel.add(scoreLabel);
        teamsPanel.revalidate();
        teamsPanel.repaint();
    }

    public void displayGamesWon() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: Arial, sans-serif; font-size: 20px; text-align: center;'>");
        sb.append("<h2>Games won by each team:</h2>");
        sb.append("Team 1: ").append(gamesWonByTeam1).append(" games<br>");
        sb.append("Team 2: ").append(gamesWonByTeam2).append(" games<br>");
        sb.append("</body></html>");

        JLabel label = new JLabel(sb.toString());
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        JOptionPane.showMessageDialog(null, label, "Games Won", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateScore(String teamName, String playerName, int points) {
        Team team = findTeam(teamName);
        if (team != null) {
            Player player = findPlayer(team, playerName);
            if (player != null) {
                player.addPoints(points);
            } else {
                JOptionPane.showMessageDialog(null, "Player not found in the team.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Team not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Player findPlayer(Team team, String playerName) {
        for (Player player : team.getPlayers()) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    public void incrementGamesWon(String teamName) {
        if (teamName.equals(teamsOnCourt.peek().getName())) {
            gamesWonByTeam1++;
        } else if (teamName.equals(teamsOnCourt.peek().getName())) {
            gamesWonByTeam2++;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid team name.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void switchTeams() {
        if (upNextTeams.size() > 0) {
            teamsOnCourt.poll();
            teamsOnCourt.add(upNextTeams.poll());
        } else {
            JOptionPane.showMessageDialog(null, "No teams in the queue.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void displayUpNextTeams(JPanel upNextPanel) {
        upNextPanel.removeAll();
        upNextPanel.setLayout(new BoxLayout(upNextPanel, BoxLayout.Y_AXIS));

        StringBuilder upNextContent = new StringBuilder();
        upNextContent.append("<html><body style='font-family: Arial, sans-serif; font-size: 20px; text-align: center;'>");
        upNextContent.append("<h2>UpNext teams:</h2>");
        for (Team team : upNextTeams) {
            upNextContent.append("<h4>").append(team.getName()).append("</h4>");
        }
        upNextContent.append("</body></html>");

        JLabel upNextLabel = new JLabel(upNextContent.toString());
        upNextLabel.setFont(new Font("Arial",Font.PLAIN, 20));

        upNextPanel.add(upNextLabel);
        upNextPanel.revalidate();
        upNextPanel.repaint();
    }

    public static void main(String[] args) {
        // Set UIManager properties for a modern and user-friendly look
        UIManager.put("OptionPane.background", new Color(37, 116, 169));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Panel.background", new Color(37, 116, 169));
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(255, 255, 255));
        UIManager.put("Button.foreground", new Color(37, 116, 169));
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 16));

        BasketballGame basketballGame = new BasketballGame();

        JFrame frame = new JFrame("Basketball Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(new Color(37, 116, 169));
        frame.setLayout(new BorderLayout());

        JPanel scorePanel = new JPanel();
        scorePanel.setBackground(new Color(37, 116, 169));
        scorePanel.setLayout(new BorderLayout());

        JPanel teamsPanel = new JPanel();
        teamsPanel.setBackground(new Color(37, 116, 169));
        teamsPanel.setLayout(new BoxLayout(teamsPanel, BoxLayout.Y_AXIS));

        scorePanel.add(teamsPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(37, 116, 169));
        controlPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton addTeamButton = new JButton("Add Team");
        addTeamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String teamName = JOptionPane.showInputDialog(null, "Enter the team name:");
                basketballGame.addTeam(teamName);
            }
        });
        controlPanel.add(addTeamButton, gbc);

        gbc.gridy++;
        JButton addPlayerButton = new JButton("Add Player to Team");
        addPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String teamName = JOptionPane.showInputDialog(null, "Enter the team name:");
                String playerName = JOptionPane.showInputDialog(null, "Enter the player name:");
                basketballGame.addPlayerToTeam(teamName, playerName);
            }
        });
        controlPanel.add(addPlayerButton, gbc);

        gbc.gridy++;
        JButton updateScoreButton = new JButton("Update Score");
        updateScoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String teamName = JOptionPane.showInputDialog(null, "Enter the team name:");
                String playerName = JOptionPane.showInputDialog(null, "Enter the player name:");
                Object[] updateOptions = {"+2", "+1", "+3"};
                int updateChoice = JOptionPane.showOptionDialog(null, "Select the points to add:", "Update Score", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, updateOptions, updateOptions[0]);
                int updatePoints = 0;
                if (updateChoice == 0) {
                    updatePoints = 2;
                } else if (updateChoice == 1) {
                    updatePoints = 1;
                } else if (updateChoice == 2) {
                    updatePoints = 3;
                }
                basketballGame.updateScore(teamName, playerName, updatePoints);
                basketballGame.displayScoreAndTeams(teamsPanel);
            }
        });
        controlPanel.add(updateScoreButton,gbc);

        gbc.gridy++;
        JButton incrementGamesButton = new JButton("Increment Games Won");
        incrementGamesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String teamName = JOptionPane.showInputDialog(null, "Enter the team name:");
                basketballGame.incrementGamesWon(teamName);
                basketballGame.displayGamesWon();
            }
        });
        controlPanel.add(incrementGamesButton, gbc);

        JPanel upNextPanel = new JPanel();
        upNextPanel.setBackground(new Color(37, 116, 169));
        upNextPanel.setLayout(new BoxLayout(upNextPanel, BoxLayout.Y_AXIS));

        JButton switchTeamsButton = new JButton("Switch Teams");
        switchTeamsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                basketballGame.switchTeams();
                basketballGame.displayScoreAndTeams(teamsPanel);
                basketballGame.displayUpNextTeams(upNextPanel);  // Update the UpNext panel
            }
        });

        controlPanel.add(switchTeamsButton, gbc);

        frame.add(scorePanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(upNextPanel, BorderLayout.EAST);
        frame.setVisible(true);

        // Start a separate thread to update the UpNext teams continuously
        Thread upNextThread = new Thread(() -> {
            while (true) {
                basketballGame.displayUpNextTeams(upNextPanel);

                try {
                    // Update every second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        upNextThread.setDaemon(true);
        upNextThread.start();
    }
}
