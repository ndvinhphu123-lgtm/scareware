import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Scareware {
    public static void main(String[] args) {
        // Tạo cửa sổ chính
        JFrame frame = new JFrame("CRITICAL SYSTEM ERROR");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Không cho bấm X để thoát
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true); // Luôn hiện lên trên cùng

        // Panel thông báo tiếng Trung (Dịch: Cảnh báo! Hệ thống bị nhiễm mã độc. Đang xóa dữ liệu...)
        JPanel panel = new JPanel();
        panel.setBackground(Color.RED);
        JLabel label = new JLabel("<html><center><h1>警告！系统检测 lừa đảo</h1><p>正在删除驱动器 C:\\ Windows \\ System32 ...</p></center></html>");
        label.setForeground(Color.WHITE);
        panel.add(label);

        // Thanh tiến trình giả
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.GREEN);
        progressBar.setBackground(Color.BLACK);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(progressBar, BorderLayout.SOUTH);
        frame.setVisible(true);

        // Chạy tiến trình xóa giả
        Timer timer = new Timer(100, new ActionListener() {
            int progress = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                progress++;
                progressBar.setValue(progress);
                progressBar.setString("Deleting C:\\Users\\... " + progress + "%");
                
                if (progress >= 100) {
                    ((Timer)e.getSource()).stop();
                    JOptionPane.showMessageDialog(frame, "C:\\ System Deleted Successfully. Restarting...", "System Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        });
        timer.start();
    }
}