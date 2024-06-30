package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ImageDisplayFrame extends JFrame {
    private final JLabel imageLabel;

    public ImageDisplayFrame(String imageUrl) {
        setTitle("Imagen Detallada");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        imageLabel = new JLabel();
        loadImage(imageUrl);

        add(new JScrollPane(imageLabel), BorderLayout.CENTER);
    }

    private void loadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            Image image = ImageIO.read(url);
            imageLabel.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen desde la URL.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
