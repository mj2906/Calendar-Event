import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class CalendarEvent {
    private static Map<LocalDate, List<String>> events = new HashMap<>();
    private static YearMonth currentMonth = YearMonth.now();
    private static JPanel calendarPanel;
    private static Color calendarBgColor = Color.WHITE;
    private static JTextArea eventTextArea;
    private static boolean isDarkTheme = false; // For theme toggling

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> LoginWindow());
    }

    private static void LoginWindow() {
        JFrame loginFrame = new JFrame("Login Form");
        loginFrame.setSize(450, 350);
        loginFrame.setLayout(null);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.LIGHT_GRAY);
        loginPanel.setBounds(20, 20, 400, 200);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setBounds(22, 20, 100, 30);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passLabel.setBounds(22, 70, 100, 30);

        JTextField userInput = new JTextField();
        userInput.setBounds(130, 20, 230, 30);

        JPasswordField passwordInput = new JPasswordField();
        passwordInput.setBounds(130, 70, 230, 30);

        JButton loginBtn = new JButton("Log in");
        loginBtn.setBounds(130, 120, 100, 40);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(260, 120, 100, 40);

        JButton guestModeBtn = new JButton("Guest Mode");
        guestModeBtn.setBounds(20, 240, 200, 40);

        loginBtn.addActionListener(e -> {
            String username = userInput.getText();
            String password = String.valueOf(passwordInput.getPassword());

            if (!username.isEmpty() && !password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Login Successful as " + username + "!");
                loginFrame.dispose();
                showMainCalendarUI();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Please enter both username and password.");
            }
        });

        cancelBtn.addActionListener(e -> {
            userInput.setText("");
            passwordInput.setText("");
        });

        guestModeBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(loginFrame, "Continuing as Guest.");
            loginFrame.dispose();
            showMainCalendarUI();
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(passLabel);
        loginPanel.add(userInput);
        loginPanel.add(passwordInput);
        loginPanel.add(loginBtn);
        loginPanel.add(cancelBtn);
        loginFrame.add(loginPanel);
        loginFrame.add(guestModeBtn);

        loginFrame.setVisible(true);
    }

    private static void showMainCalendarUI() {
        JFrame mainFrame = new JFrame("Calendar");
        mainFrame.setSize(1000, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        JPanel calendarControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevBtn = new JButton("<");
        JButton nextBtn = new JButton(">");
        JLabel monthLabel = new JLabel("", JLabel.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 18));
        updateMonthLabel(monthLabel);

        prevBtn.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
            updateMonthLabel(monthLabel);
        });

        nextBtn.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
            updateMonthLabel(monthLabel);
        });

        calendarControlPanel.add(prevBtn);
        calendarControlPanel.add(monthLabel);
        calendarControlPanel.add(nextBtn);

        calendarPanel = new JPanel(new GridLayout(0, 7));
        updateCalendar();

        eventTextArea = new JTextArea(12, 30);
        eventTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        eventTextArea.setEditable(false);
        eventTextArea.setLineWrap(true);
        eventTextArea.setWrapStyleWord(true);
        JScrollPane eventScroll = new JScrollPane(eventTextArea);
        eventScroll.setPreferredSize(new Dimension(350, 300));

        JLabel eventLabel = new JLabel("Events", JLabel.CENTER);
        eventLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Background Color button
        JButton colorBtn = new JButton("Set Background Color");
        colorBtn.addActionListener(e -> {
            Color chosenColor = JColorChooser.showDialog(mainFrame, "Choose Background Color", calendarBgColor);
            if (chosenColor != null) {
                calendarBgColor = chosenColor;
                updateCalendar();
            }
        });

        // Theme toggle button
        JButton themeBtn = new JButton("Toggle Theme");
        themeBtn.addActionListener(e -> {
            isDarkTheme = !isDarkTheme;
            applyTheme(mainFrame);
        });

        JPanel bottomButtons = new JPanel();
        bottomButtons.setLayout(new GridLayout(2, 1));
        bottomButtons.add(colorBtn);
        bottomButtons.add(themeBtn);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(eventLabel, BorderLayout.NORTH);
        rightPanel.add(eventScroll, BorderLayout.CENTER);
        rightPanel.add(bottomButtons, BorderLayout.SOUTH);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(calendarControlPanel, BorderLayout.NORTH);
        leftPanel.add(calendarPanel, BorderLayout.CENTER);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(leftPanel, BorderLayout.CENTER);
        mainFrame.add(rightPanel, BorderLayout.EAST);

        applyTheme(mainFrame);
        mainFrame.setVisible(true);
    }

    private static void applyTheme(JFrame frame) {
        Color bg = isDarkTheme ? Color.DARK_GRAY : Color.WHITE;
        Color fg = isDarkTheme ? Color.WHITE : Color.BLACK;
        calendarBgColor = bg;
        eventTextArea.setBackground(bg);
        eventTextArea.setForeground(fg);
        updateCalendar();
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private static void updateMonthLabel(JLabel label) {
        label.setText(currentMonth.getMonth().toString() + " " + currentMonth.getYear());
    }

    private static void updateCalendar() {
        calendarPanel.removeAll();
        calendarPanel.setBackground(calendarBgColor);

        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String header : headers) {
            JLabel lbl = new JLabel(header, JLabel.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setForeground(isDarkTheme ? Color.WHITE : Color.BLACK);
            calendarPanel.add(lbl);
        }

        LocalDate firstOfMonth = currentMonth.atDay(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
        int lengthOfMonth = currentMonth.lengthOfMonth();

        for (int i = 0; i < startDayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= lengthOfMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            JButton dayBtn = new JButton(String.valueOf(day));

            if (events.containsKey(date) && !events.get(date).isEmpty()) {
                dayBtn.setBackground(Color.YELLOW);
            }

            if (isDarkTheme) {
                dayBtn.setForeground(Color.WHITE);
                dayBtn.setBackground(Color.GRAY);
            }

            dayBtn.addActionListener(e -> handleDateClick(date));
            calendarPanel.add(dayBtn);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private static void handleDateClick(LocalDate date) {
        List<String> currentEvents = new ArrayList<>(events.getOrDefault(date, new ArrayList<>()));

        JTextArea textArea = new JTextArea(10, 20);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        Object[] options = {"Save (Add)", "Delete All", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                null,
                scrollPane,
                "Add new events for " + date,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            String input = textArea.getText().trim();
            if (!input.isEmpty()) {
                String[] lines = input.split("\\n");
                List<String> updatedEvents = events.getOrDefault(date, new ArrayList<>());
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.isEmpty() && !updatedEvents.contains(trimmed)) {
                        updatedEvents.add(trimmed);
                    }
                }
                events.put(date, updatedEvents);

                // Reminder: if the event is today
                if (date.equals(LocalDate.now())) {
                    JOptionPane.showMessageDialog(null, "Reminder: You have an event today!");
                }
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            events.remove(date);
        }

        showEventsForDate(date);
        updateCalendar();
    }

    private static void showEventsForDate(LocalDate date) {
        eventTextArea.setText("Events for " + date + ":\n");
        List<String> eventList = events.get(date);
        if (eventList != null && !eventList.isEmpty()) {
            for (String event : eventList) {
                eventTextArea.append(" - " + event + "\n");
            }
        } else {
            eventTextArea.append("The event has been accomplished");
        }
    }
}