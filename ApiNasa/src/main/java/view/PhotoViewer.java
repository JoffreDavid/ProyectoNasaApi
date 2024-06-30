package view;

import models.Photo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;




public class PhotoViewer extends JFrame {
    private final List<Photo> photos;
    private final JLabel statusLabel;
    private final JComboBox<String> filterOptions;
    private final JTable dataTable;
    private final JCheckBox sequentialCheckBox;
    private final JCheckBox parallelCheckBox;

    public PhotoViewer(List<Photo> photos) {
        this.photos = photos;
        this.statusLabel = new JLabel("Seleccione un filtro y presione 'Aplicar Filtro'.");
        this.filterOptions = new JComboBox<>(new String[]{"FHAZ", "NAVCAM", "MAST", "CHEMCAM", "MAHLI", "MARDI", "RHAZ"});
        this.dataTable = new JTable();
        this.sequentialCheckBox = new JCheckBox("Filtrado Secuencial");
        this.parallelCheckBox = new JCheckBox("Filtrado Paralelo");

        initialize();
    }

    private void initialize() {
        setTitle("Mars Rover Photos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel();
        JButton applyFilterButton = new JButton("Aplicar Filtro");
        applyFilterButton.addActionListener(e -> applyFilter());

        JButton showImageButton = new JButton("Mostrar Imagen");
        showImageButton.addActionListener(e -> showSelectedImage());

        filterPanel.add(filterOptions);
        filterPanel.add(applyFilterButton);
        filterPanel.add(showImageButton);
        filterPanel.add(sequentialCheckBox);
        filterPanel.add(parallelCheckBox);

        add(statusLabel, BorderLayout.NORTH);
        add(new JScrollPane(dataTable), BorderLayout.CENTER);
        add(filterPanel, BorderLayout.SOUTH);

        sequentialCheckBox.addActionListener(e -> {
            if (sequentialCheckBox.isSelected()) {
                parallelCheckBox.setSelected(false);
            }
        });

        parallelCheckBox.addActionListener(e -> {
            if (parallelCheckBox.isSelected()) {
                sequentialCheckBox.setSelected(false);
            }
        });

        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detectar doble clic
                    showSelectedImage();
                }
            }
        });
    }

    private void applyFilter() {
        String selectedCamera = (String) filterOptions.getSelectedItem();
        List<Photo> filteredPhotos;

        if (sequentialCheckBox.isSelected()) {
            long startTime = System.nanoTime();
            filteredPhotos = applySequentialFilter(selectedCamera);
            long endTime = System.nanoTime();
            displayFilteredResults(filteredPhotos);
            displayTimeElapsed(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
        } else if (parallelCheckBox.isSelected()) {
            long startTime = System.nanoTime();
            filteredPhotos = applyParallelFilter(selectedCamera);
            long endTime = System.nanoTime();
            displayFilteredResults(filteredPhotos);
            displayTimeElapsed(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de filtrado (secuencial o paralelo).", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private List<Photo> applySequentialFilter(String selectedCamera) {
        return photos.stream()
                .filter(photo -> photo.getCamera().getName().equals(selectedCamera))
                .collect(Collectors.toList());
    }

    private List<Photo> applyParallelFilter(String selectedCamera) {
        return photos.parallelStream()
                .filter(photo -> photo.getCamera().getName().equals(selectedCamera))
                .collect(Collectors.toList());
    }

    private void displayFilteredResults(List<Photo> filteredPhotos) {
        String[] columnNames = {"ID", "Sol", "Camera", "Earth Date", "Image URL"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Photo photo : filteredPhotos) {
            Object[] rowData = {
                    photo.getId(),
                    photo.getSol(),
                    photo.getCamera().getName(),
                    photo.getEarth_date(),
                    photo.getImg_src()
            };
            model.addRow(rowData);
        }

        dataTable.setModel(model);
        statusLabel.setText("Mostrando " + filteredPhotos.size() + " resultados filtrados.");
    }

    private void showSelectedImage() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow != -1) {
            String imageUrl = (String) dataTable.getValueAt(selectedRow, 4); // Columna de Image URL

            // Convertir http a https si es necesario
            imageUrl = convertToHttps(imageUrl);

            // Mostrar imagen en una nueva ventana
            String finalImageUrl = imageUrl;
            SwingUtilities.invokeLater(() -> {
                ImageDisplayFrame imageFrame = new ImageDisplayFrame(finalImageUrl);
                imageFrame.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para mostrar la imagen.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String convertToHttps(String imageUrl) {
        // Convertir la URL a HTTPS si comienza con HTTP
        if (imageUrl.startsWith("http://")) {
            imageUrl = imageUrl.replace("http://", "https://");
        }
        return imageUrl;
    }

    private void displayTimeElapsed(long milliseconds) {
        JOptionPane.showMessageDialog(this, "Tiempo transcurrido: " + milliseconds + " ms", "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}