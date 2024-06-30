    package models;

    public class Photo {
        private int id;
        private int sol;
        private Camera camera;
        private String img_src;
        private String earth_date;
        private Rover rover;


        public int getId() {
            return id;
        }

        public Rover getRover() {
            return rover;
        }

        public String getEarth_date() {
            return earth_date;
        }

        public String getImg_src() {
            return img_src;
        }

        public Camera getCamera() {
            return camera;
        }

        public int getSol() {
            return sol;
        }
        @Override
        public String toString() {
            return "ID: " + id + ", Sol: " + sol + ", Cámara: " + camera.getFullName()+ ", Fecha: " + earth_date;
        }
    }
