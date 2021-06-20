package edu.wong.entity;

public class Attendance {
        private int sId;
        private double sLat;
        private double sLon;
        private int sClassId;
        private int state;
        private String code;
        private int tId;

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
//        private int tId;
//        private double tLat;
//        private double tLon;


        public int getsId() {
            return sId;
        }

        public void setsId(int sId) {
            this.sId = sId;
        }

        public double getsLat() {
            return sLat;
        }

        public void setsLat(double sLat) {
            this.sLat = sLat;
        }

        public double getsLon() {
            return sLon;
        }

        public void setsLon(double sLon) {
            this.sLon = sLon;
        }

        public int getsClassId() {
            return sClassId;
        }

        public void setsClassId(int sClassId) {
            this.sClassId = sClassId;
        }

//        public int gettId() {
//            return tId;
//        }
//
//        public void settId(int tId) {
//            this.tId = tId;
//        }
//
//        public double gettLat() {
//            return tLat;
//        }
//
//        public void settLat(double tLat) {
//            this.tLat = tLat;
//        }
//
//        public double gettLon() {
//            return tLon;
//        }
//
//        public void settLon(double tLon) {
//            this.tLon = tLon;
//        }


}


