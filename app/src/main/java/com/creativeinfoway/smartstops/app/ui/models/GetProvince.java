package com.creativeinfoway.smartstops.app.ui.models;

import java.util.List;

public class GetProvince {
    /**
     * status : 1
     * data : [{"id":"1","state":"British Columbia"},{"id":"2","state":"Alberta"},{"id":"3","state":"Manitoba"},{"id":"4","state":"New Brunswick"},{"id":"5","state":"Newfoundland"},{"id":"6","state":"Newfoundland and Labrador"},{"id":"7","state":"Northwest Territories"},{"id":"8","state":"Nova Scotia"},{"id":"9","state":"Nunavut"},{"id":"10","state":"Ontario"},{"id":"11","state":"Prince Edward Island"},{"id":"12","state":"Quebec"},{"id":"13","state":"Saskatchewan"},{"id":"14","state":"Yukon"},{"id":"15","state":"Yukon Territory"},{"id":"16","state":"Alabama"},{"id":"17","state":"Alaska"},{"id":"18","state":"Arizona"},{"id":"19","state":"Arkansas"},{"id":"20","state":"Armed Forces US"},{"id":"21","state":"California"},{"id":"22","state":"Colorado"},{"id":"23","state":"Connecticut"},{"id":"24","state":"Delaware"},{"id":"25","state":"District of Columbia"},{"id":"26","state":"Florida"},{"id":"27","state":"Georgia"},{"id":"28","state":"Guangdong"},{"id":"29","state":"Hawaii"},{"id":"30","state":"Idaho"},{"id":"31","state":"Illinois"},{"id":"32","state":"Indiana"},{"id":"33","state":"Iowa"},{"id":"34","state":"Kansas"},{"id":"35","state":"Kentucky"},{"id":"36","state":"Louisiana"},{"id":"37","state":"Maine"},{"id":"38","state":"Maryland"},{"id":"39","state":"Massachusetts"},{"id":"40","state":"Michigan"},{"id":"41","state":"Minnesota"},{"id":"42","state":"Mississippi"},{"id":"43","state":"Missouri"},{"id":"44","state":"Montana"},{"id":"45","state":"Nebraska"},{"id":"46","state":"New Hampshire"},{"id":"47","state":"New Jersey"},{"id":"48","state":"New Mexico"},{"id":"49","state":"New York"},{"id":"50","state":"North Carolina"},{"id":"51","state":"North Dakota"},{"id":"52","state":"Ohio"},{"id":"53","state":"Oklahoma"},{"id":"54","state":"Oregon"},{"id":"55","state":"Pennsylvania"},{"id":"56","state":"Rhode Island"},{"id":"57","state":"South Carolina"},{"id":"58","state":"South Dakota"},{"id":"59","state":"Tennessee"},{"id":"60","state":"Texas"},{"id":"61","state":"Utah"},{"id":"62","state":"Vermont"},{"id":"63","state":"Virginia"},{"id":"64","state":"Washington"},{"id":"65","state":"West Virginia"},{"id":"66","state":"Wisconsin"},{"id":"67","state":"Wyoming"}]
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
         * id : 1
         * state : British Columbia
         */

        private String id;
        private String state;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
