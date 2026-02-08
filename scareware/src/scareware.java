package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import javax.sound.sampled.*;

public class scareware {
    private static boolean isRunning = true;
    private static Clip clip; // Khai báo Clip ở đây để có thể dừng âm thanh khi xong

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
        JLabel label = new JLabel("<html><center><h1 style='color:red; font-size:40px;'>警告！這個系統正在被摧毀</h1>"
                + "<p style='color:white;'>正在重新格式化整個 C:\\ 和 D:\\ 磁碟機…</p></center></html>");
        
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setPreferredSize(new Dimension(600, 40));
        pb.setStringPainted(true);
        pb.setForeground(Color.RED);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; frame.add(label, gbc);
        gbc.gridy = 1; frame.add(pb, gbc);

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

                    Thread.sleep(200);
                }
            } catch (Exception e) {}
        }).start();

        Timer timer = new Timer(400, new ActionListener() {
            int progress = 0;
            java.util.Random rand = new java.util.Random();
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rand.nextInt(10) > 7){
                    progress++; 
                    pb.setValue(progress);
                    pb.setString("DELETING DATA... " + progress + "%");
                } 
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
                    
                    if (clip != null) clip.stop();
                    
                    // Mở Rickroll video
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Rickroll failed, but you're safe!");
                    }
                    
                    // Đợi 5 giây rồi thoát hẳn để nạn nhân kịp xem màn hình chúc mừng
                    new Timer(5000, ae -> System.exit(0)).start();
                }
            }
        });
        timer.start();
    }

    public static void playWarningSound(String fileName) {
        new Thread(() -> {
            try {
                InputStream audioSrc = scareware.class.getResourceAsStream("/" + fileName);
                if (audioSrc == null) {
                    audioSrc = scareware.class.getResourceAsStream("/src/" + fileName);
                }
                if (audioSrc == null) {
                    audioSrc = scareware.class.getResourceAsStream(fileName);
                }

                if (audioSrc != null) {
                    InputStream bufferedIn = new BufferedInputStream(audioSrc);
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    clip.start();
                }
            } catch (Exception e) {
                System.out.println("Lỗi âm thanh: " + e.getMessage());
            }
        }).start();
    }
}