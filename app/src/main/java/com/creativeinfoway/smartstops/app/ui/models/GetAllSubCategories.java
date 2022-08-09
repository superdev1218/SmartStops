package com.creativeinfoway.smartstops.app.ui.models;

import java.util.List;

public class GetAllSubCategories {


    /**
     * status : 1
     * data : [{"id":"191","name":"Free Fire Wood","total_waypoint_by_sub_cat":"0"},{"id":"188","name":"Free Overnight Parking","total_waypoint_by_sub_cat":"4"},{"id":"198","name":"Free Parking","total_waypoint_by_sub_cat":"0"},{"id":"195","name":"Free Picnic Area","total_waypoint_by_sub_cat":"0"},{"id":"192","name":"Free Potable Water","total_waypoint_by_sub_cat":"0"},{"id":"193","name":"Free Rinse Water","total_waypoint_by_sub_cat":"0"},{"id":"190","name":"Free RV & Tent Camping","total_waypoint_by_sub_cat":"0"},{"id":"194","name":"Free Sanidump","total_waypoint_by_sub_cat":"0"},{"id":"196","name":"Free Shower","total_waypoint_by_sub_cat":"0"},{"id":"197","name":"Free WiFi","total_waypoint_by_sub_cat":"0"},{"id":"189","name":"Smartstop","total_waypoint_by_sub_cat":"0"}]
     */

    private String status;
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

    public static class DataBean {
        /**
         * id : 191
         * name : Free Fire Wood
         * total_waypoint_by_sub_cat : 0
         */

        private String id;
        private String name;
        private String total_waypoint_by_sub_cat;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTotal_waypoint_by_sub_cat() {
            return total_waypoint_by_sub_cat;
        }

        public void setTotal_waypoint_by_sub_cat(String total_waypoint_by_sub_cat) {
            this.total_waypoint_by_sub_cat = total_waypoint_by_sub_cat;
        }
    }
}
