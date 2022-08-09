package com.creativeinfoway.smartstops.app.ui.models;

import java.io.Serializable;
import java.util.List;

public class GetAllLatLongPoints implements Serializable {

    /**
     * status : 1
     * data : [{"id":"127","cat_id":"20","sub_cat_id":"","latitude":"22 15 50","longitude":"70 47 32","latitude_decimal":"22.263888888889","longitude_decimal":"70.792222222222","name":"Krishnanagar Civic centre","waypoint_id":"19205","address":"Krishnanagar, Krishna Nagar, Sardar Nagar, Rajkot, Gujarat 360004","additional_info":"","country":"Canada","email":"","phone_number":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/187fa5a2ce1d39eebcdc5797033da0f1.png","waypoint_image":"http://canadasmartstops.com/smartstop/admin/image/8fdaffdde2fe053df93e806703257ef1.jpg"},{"id":"125","cat_id":"38","sub_cat_id":"","latitude":"22 15 53","longitude":"70 47 45","latitude_decimal":"22.264722222222","longitude_decimal":"70.795833333333","name":"H J Doshi  Hospital","waypoint_id":"74202","address":"Gondal Road Near Bajaj Show Room, Ganpati Nagar, Malaviya Nagar, Rajkot, Gujarat 360004","additional_info":"","country":"Canada","email":"","phone_number":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/acebbf9a436737eb0e6e790fa8785464.png","waypoint_image":"http://canadasmartstops.com/smartstop/admin/image/smartstopslogo.png"},{"id":"126","cat_id":"119","sub_cat_id":"","latitude":"22 16 01","longitude":"70 47 44","latitude_decimal":"22.266944444444","longitude_decimal":"70.795555555556","name":"Shraddha Garden","waypoint_id":"44555","address":"Gunatit Nagar, Ganpati Nagar, Malaviya Nagar, Gunatit Nagar, Ganpati Nagar, Malaviya Nagar, Rajkot, Gujarat 360004","additional_info":"","country":"Canada","email":"","phone_number":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/f6a9e2c949a479f0081e3104cb2979c8.png","waypoint_image":"http://canadasmartstops.com/smartstop/admin/image/d8d175f850a1372494794d62d5c5cee4.jpg"}]
     */

    private String status;
    private String msg;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 127
         * cat_id : 20
         * sub_cat_id :
         * latitude : 22 15 50
         * longitude : 70 47 32
         * latitude_decimal : 22.263888888889
         * longitude_decimal : 70.792222222222
         * name : Krishnanagar Civic centre
         * waypoint_id : 19205
         * address : Krishnanagar, Krishna Nagar, Sardar Nagar, Rajkot, Gujarat 360004
         * additional_info :
         * country : Canada
         * email :
         * phone_number :
         * waypoint_icon_image : http://canadasmartstops.com/smartstop/admin/image/187fa5a2ce1d39eebcdc5797033da0f1.png
         * waypoint_image : http://canadasmartstops.com/smartstop/admin/image/8fdaffdde2fe053df93e806703257ef1.jpg
         */

        private String id;
        private String cat_id;
        private String sub_cat_id;
        private String latitude;
        private String longitude;
        private String latitude_decimal;
        private String longitude_decimal;
        private String name;
        private String waypoint_id;
        private String address;
        private String additional_info;
        private String country;
        private String email;
        private String phone_number;
        private String waypoint_icon_image;
        private String waypoint_image;
        private String option_image_1;
        private String option_image_2;
        private String option_image_3;

        public String getOption_image_1() {
            return option_image_1;
        }

        public void setOption_image_1(String option_image_1) {
            this.option_image_1 = option_image_1;
        }

        public String getOption_image_2() {
            return option_image_2;
        }

        public void setOption_image_2(String option_image_2) {
            this.option_image_2 = option_image_2;
        }

        public String getOption_image_3() {
            return option_image_3;
        }

        public void setOption_image_3(String option_image_3) {
            this.option_image_3 = option_image_3;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getSub_cat_id() {
            return sub_cat_id;
        }

        public void setSub_cat_id(String sub_cat_id) {
            this.sub_cat_id = sub_cat_id;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude_decimal() {
            return latitude_decimal;
        }

        public void setLatitude_decimal(String latitude_decimal) {
            this.latitude_decimal = latitude_decimal;
        }

        public String getLongitude_decimal() {
            return longitude_decimal;
        }

        public void setLongitude_decimal(String longitude_decimal) {
            this.longitude_decimal = longitude_decimal;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWaypoint_id() {
            return waypoint_id;
        }

        public void setWaypoint_id(String waypoint_id) {
            this.waypoint_id = waypoint_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAdditional_info() {
            return additional_info;
        }

        public void setAdditional_info(String additional_info) {
            this.additional_info = additional_info;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getWaypoint_icon_image() {
            return waypoint_icon_image;
        }

        public void setWaypoint_icon_image(String waypoint_icon_image) {
            this.waypoint_icon_image = waypoint_icon_image;
        }

        public String getWaypoint_image() {
            return waypoint_image;
        }

        public void setWaypoint_image(String waypoint_image) {
            this.waypoint_image = waypoint_image;
        }
    }
}
